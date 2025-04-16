package com.shopsphere.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.shopsphere.dto.CartDTO;
import com.shopsphere.dto.PaginationResponseDTO;
import com.shopsphere.dto.ProductDTO;
import com.shopsphere.entity.CartEntity;
import com.shopsphere.entity.CartItemEntity;
import com.shopsphere.entity.ProductEntity;
import com.shopsphere.entity.UserEntity;
import com.shopsphere.exceptions.InsufficientResourcesException;
import com.shopsphere.exceptions.ResourceNotFoundException;
import com.shopsphere.repository.CartItemRepository;
import com.shopsphere.repository.CartRepository;
import com.shopsphere.repository.ProductRepository;
import com.shopsphere.service.CartService;
import com.shopsphere.utils.AuthUtil;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final AuthUtil authUtil;
    private final ModelMapper mapper;

    @Override
    public CartDTO addCartItem(final String productName, final Integer quantity) {
        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        final CartEntity userCart = createOrRetrieveUserCart();

        final ProductEntity productFromDB = productRepository.findByProductName(productName)
                .orElseThrow(() -> new ResourceNotFoundException("product", productName, "name"));

        if (productFromDB.getProductQuantity() < quantity) {
            throw new InsufficientResourcesException(
                    "product", productFromDB.getProductName(),
                    productFromDB.getProductQuantity()
            );
        }

        cartItemRepository.findByProductNameAndUserEmail(productFromDB.getProductName(), loggedInUser.getEmail()).ifPresent(entity -> {
            throw new DataIntegrityViolationException("Product already added to the cart");
                }
        );

        final CartItemEntity cartItemEntity = new CartItemEntity();

        cartItemEntity.setCart(userCart);
        cartItemEntity.setQuantity(quantity);
        cartItemEntity.setProduct(productFromDB);
        cartItemEntity.setPrice(productFromDB.getProductPrice());
        cartItemEntity.setSpecialPrice(productFromDB.getProductPrice());

        cartItemRepository.save(cartItemEntity);

        userCart.getCartItems().add(cartItemEntity);
        userCart.setTotalPrice(userCart.getTotalPrice() + cartItemEntity.getPrice() * quantity);

        cartRepository.save(userCart);

        final CartDTO cartDTO = mapper.map(userCart, CartDTO.class);

        final List<CartItemEntity> cartItems = userCart.getCartItems();
        final List<ProductDTO> productDtoList = cartItems.stream().map(item -> {
                    final ProductDTO productDTO = mapper.map(item.getProduct(), ProductDTO.class);
                    productDTO.setProductQuantity(quantity);
                    return productDTO;
                }
        ).toList();

        cartDTO.setProducts(productDtoList);

        return cartDTO;
    }

    @Override
    public PaginationResponseDTO<CartDTO> retrieveAllCarts(final Integer page, final Integer size) {
        final PageRequest pageRequest = PageRequest.of(page, size);

        final Page<CartEntity> cartEntityPage = cartRepository.findAll(pageRequest);

        final Set<CartDTO> cartDTOSet = cartEntityPage.getContent().stream().map(cartEntity -> {
            final CartDTO cartDTO = mapper.map(cartEntity, CartDTO.class);

            final List<ProductDTO> productDTOList = cartEntity.getCartItems().stream().map(item -> {
                        final ProductDTO productDTO = mapper.map(item.getProduct(), ProductDTO.class);
                        productDTO.setProductQuantity(item.getQuantity());
                        return productDTO;
                    }
            ).toList();

            cartDTO.setProducts(productDTOList);

            return cartDTO;
        }).collect(Collectors.toSet());

        return PaginationResponseDTO.<CartDTO>builder()
                .contentSet(cartDTOSet)
                .page(page)
                .isLast(cartEntityPage.isLast())
                .build();
    }

    public CartEntity createOrRetrieveUserCart() {

        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        final Optional<CartEntity> cartEntityOptional = cartRepository.findCartByUserEmail(loggedInUser.getEmail());

        if (cartEntityOptional.isPresent())
            return cartEntityOptional.get();

        final CartEntity cartEntity = new CartEntity();

        cartEntity.setUser(loggedInUser);
        cartEntity.setTotalPrice(0);

        return cartRepository.save(cartEntity);
    }

    @Override
    public CartDTO retrieveUserCartByEmail(final String userEmail) {

        final CartEntity cartEntity =
                cartRepository.findCartByUserEmail(userEmail).orElseThrow(
                        () -> new ResourceNotFoundException("user cart", userEmail, "email")
                );

        final CartDTO cartDTO = mapper.map(cartEntity, CartDTO.class);
        final Stream<ProductDTO> productDTOStream =
                cartEntity.getCartItems().stream().map(item -> {
                            final ProductDTO productDTO = mapper.map(item.getProduct(), ProductDTO.class);
                            productDTO.setProductQuantity(item.getQuantity());
                            return productDTO;
                        }
                );
        cartDTO.setProducts(productDTOStream.toList());

        return cartDTO;
    }

    @Override
    public CartDTO updateProductQuantityInCart(final String productName, final Integer quantity,
                                               final String operation) {

        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        final CartEntity cartEntity = cartRepository.findCartByUserEmail(loggedInUser.getEmail()).orElseThrow();

        final ProductEntity productEntity = productRepository.findByProductName(productName).orElseThrow(
                () -> new ResourceNotFoundException("product", productName, "name")
        );

        if (productEntity.getProductQuantity() == 0 || productEntity.getProductQuantity() < quantity)
            throw new InsufficientResourcesException("product", productName, quantity);

        final Optional<CartItemEntity> itemEntity = cartEntity.getCartItems().stream().filter(cartItem ->
                cartItem.getProduct().getProductName().equals(productName)).findFirst();

        if (itemEntity.isEmpty())
            throw new ResourceNotFoundException("product", productName, "name");

        final int newQuantity = Objects.equals(operation, "increment") ?
                itemEntity.get().getQuantity() + quantity :
                itemEntity.get().getQuantity() - quantity;

        final CartItemEntity cartItemEntity = itemEntity.get();
        cartItemEntity.setQuantity(newQuantity);
        cartEntity.setTotalPrice(cartItemEntity.getPrice() * newQuantity);
        cartRepository.save(cartEntity);

        final CartItemEntity savedCartitemEntity = cartItemRepository.save(cartItemEntity);
        if (savedCartitemEntity.getQuantity() == 0)
            cartItemRepository.delete(cartItemEntity);

        final CartDTO cartDTO = mapper.map(cartEntity, CartDTO.class);

        final List<CartItemEntity> cartItems = cartEntity.getCartItems();
        final List<ProductDTO> productDTOList = cartItems.stream().map(item -> {
            final ProductDTO productDTO = mapper.map(item.getProduct(), ProductDTO.class);
            productDTO.setProductQuantity(item.getQuantity());
            return productDTO;
        }).toList();

        cartDTO.setProducts(productDTOList);
        return cartDTO;
    }

    @Transactional
    @Override
    public void removeCartItemsByProductFromCart(final String productName) {
        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        final CartEntity cartEntity = cartRepository.findCartByUserEmail(loggedInUser.getEmail()).orElseThrow();

//        final Optional<CartItemEntity> optionalCartItemEntity = cartEntity.getCartItems().stream().filter(cartItem ->
//                cartItem.getProduct().getProductName().equals(productName)).findFirst();

        final CartItemEntity cartItemEntity = cartItemRepository.findByProductNameAndUserEmail(
                productName,
                loggedInUser.getEmail()
        ).orElseThrow(
                () -> new ResourceNotFoundException("product", productName, "name")
        );
        cartEntity.setTotalPrice(cartEntity.getTotalPrice() - cartItemEntity.getPrice());
        cartEntity.getCartItems().remove(cartItemEntity);
        cartItemEntity.setCart(null);

        cartRepository.saveAndFlush(cartEntity);
    }

    @Override
    public void deleteProductFromCarts(final String productName) {
        cartRepository.findAll().forEach(cartEntity -> {
            Iterator<CartItemEntity> iterator = cartEntity.getCartItems().iterator();
            while (iterator.hasNext()) {
                CartItemEntity cartItem = iterator.next();
                ProductEntity productEntity = cartItem.getProduct();
                if (productEntity.getProductName().equals(productName)) {
                    iterator.remove();
                    cartItemRepository.delete(cartItem);
                    cartEntity.setTotalPrice(cartEntity.getTotalPrice() -
                            productEntity.getProductQuantity() * productEntity.getProductPrice());
                }
            }
            cartRepository.save(cartEntity);
        });
    }
}
