package com.shopsphere.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import com.shopsphere.dto.*;
import com.shopsphere.entity.*;
import com.shopsphere.exceptions.ResourceNotFoundException;
import com.shopsphere.repository.*;
import com.shopsphere.service.CartService;
import com.shopsphere.service.OrderService;
import com.shopsphere.utils.AuthUtil;
import com.shopsphere.utils.OrderStatus;
import com.shopsphere.utils.PaymentGateWayTypes;
import com.shopsphere.utils.PaymentMethods;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final AuthUtil authUtil;
    private final CartRepository cartRepository;
    private final UserAddressRepository userAddressRepository;
    private final ProductRepository productRepository;
    private final CartService cartService;
    private final ModelMapper modelMapper;
    private final PaymentRepository paymentRepository;

    @Override
    public OrderDTO placeOrder(@NotNull OrderRequestDTO orderRequestDTO) {
        final UserEntity loggedInUser = authUtil.getLoggedInUser();
        final CartEntity cartEntity = cartRepository.findCartByUserEmail(loggedInUser.getEmail()).orElseThrow(
                () -> new ResourceNotFoundException("User cart", "email", loggedInUser.getEmail())
        );
        final AddressRequestDTO address = orderRequestDTO.getAddress();
        final UserAddressEntity userAddressEntity =
                userAddressRepository.findByStreetNameAndStreetNumber(
                        address.getStreetName(),
                        address.getStreetNumber()
                ).orElseThrow(
                        () -> new ResourceNotFoundException(
                                "User address", "street and street number",
                                address.getStreetNumber() + " " + address.getStreetName()
                        )
                );
        final OrderEntity orderEntity = new OrderEntity();

        orderEntity.setUserAddress(userAddressEntity);
        orderEntity.setTotalAmount(cartEntity.getTotalPrice());
        orderEntity.setOrderDate(LocalDateTime.now());
        orderEntity.setOrderStatus(OrderStatus.ACCEPTED);

        final PaymentEntity paymentEntity = getPaymentEntity(orderRequestDTO, orderEntity);
        paymentRepository.save(paymentEntity);
        orderEntity.setPayment(paymentEntity);

        final OrderEntity savedOrder = orderRepository.save(orderEntity);
        paymentRepository.save(paymentEntity);

        final List<OrderItemEntity> orderItemEntityList = new ArrayList<>();

        cartEntity.getCartItems().forEach(cartItemEntity -> {
            final OrderItemEntity orderItemEntity = new OrderItemEntity();

            orderItemEntity.setProduct(cartItemEntity.getProduct());
            orderItemEntity.setPrice(cartItemEntity.getPrice());
            orderItemEntity.setSpecialPrice(cartItemEntity.getSpecialPrice());
            orderItemEntity.setQuantity(cartItemEntity.getQuantity());
            orderItemEntity.setOrder(savedOrder);

            orderItemEntityList.add(orderItemEntity);
        });
        orderItemRepository.saveAll(orderItemEntityList);

        Iterator<CartItemEntity> cartItemEntityIterator = cartEntity.getCartItems().iterator();
        while (cartItemEntityIterator.hasNext()) {
            final CartItemEntity cartItemEntity = cartItemEntityIterator.next();
            final ProductEntity productEntity = cartItemEntity.getProduct();
            int quantity = cartItemEntity.getQuantity();
            productEntity.setProductQuantity(productEntity.getProductQuantity() - quantity);
            productRepository.save(productEntity);

            cartItemEntityIterator.remove();
            cartService.removeCartItemsByProductFromCart(productEntity.getProductName());
        }

        final OrderDTO orderDTO = modelMapper.map(savedOrder, OrderDTO.class);
        final List<OrderItemDTO> itemDTOS = orderItemEntityList.stream().map(orderItemEntity -> {
                    final OrderItemDTO orderItemDTO = modelMapper.map(orderItemEntity, OrderItemDTO.class);
                    orderItemDTO.getProduct().setProductQuantity(orderItemEntity.getQuantity());
                    return orderItemDTO;
                }
        ).toList();

        final UserAddressDTO userAddressDTO = modelMapper.map(userAddressEntity, UserAddressDTO.class);

        orderDTO.setEmail(loggedInUser.getEmail());
        orderDTO.setOrderItems(itemDTOS);
        orderDTO.setAddress(userAddressDTO);
        orderDTO.setOrderDate(savedOrder.getOrderDate());

        return orderDTO;
    }

    private static @NotNull PaymentEntity getPaymentEntity(final @NotNull OrderRequestDTO orderRequestDTO,
                                                           final OrderEntity orderEntity) {
        final PaymentEntity paymentEntity = new PaymentEntity();

        switch (orderRequestDTO.getGatewayType()) {
            case "paypal":
                paymentEntity.setType(PaymentGateWayTypes.PAYPAL);
                break;

            case "stripe":
                paymentEntity.setType(PaymentGateWayTypes.STRIPE);
                break;

            case "google pay":
                paymentEntity.setType(PaymentGateWayTypes.GOOGLE_PAY);
                break;

            case "amazon pay":
                paymentEntity.setType(PaymentGateWayTypes.AMAZON_PAY);
                break;

            case "razor pay":
                paymentEntity.setType(PaymentGateWayTypes.RAZORPAY);
                break;
        }

        switch (orderRequestDTO.getPaymentMethod()) {
            case "credit":
                paymentEntity.setMethod(PaymentMethods.CREDIT_CARD);
                break;

            case "debit":
                paymentEntity.setMethod(PaymentMethods.DEBIT_CARD);
                break;

            case "paypal balance":
                paymentEntity.setMethod(PaymentMethods.PAYPAL_BALANCE);
                break;
        }

        paymentEntity.setGateWayId(orderRequestDTO.getGatewayId());
        paymentEntity.setGateWayStatus(paymentEntity.getGateWayStatus());
        paymentEntity.setGateWayResponseMessage(orderRequestDTO.getResponseMessage());
        paymentEntity.setOrder(orderEntity);
        return paymentEntity;
    }
}
