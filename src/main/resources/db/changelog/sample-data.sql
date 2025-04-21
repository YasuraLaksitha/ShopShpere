INSERT INTO tbl_product (
    image, product_name, product_description,
    product_quantity, product_price, product_special_price,
    unavailable, category_id, seller_id
) VALUES
-- Category 1: Laptops
('/images/laptops_1.jpg', 'Laptops Product 1', 'High quality laptops model 1.', 50, 1000.00, 950.00, false, 1, 1),
('/images/laptops_2.jpg', 'Laptops Product 2', 'High quality laptops model 2.', 40, 1200.00, 1150.00, false, 1, 1),
('/images/laptops_3.jpg', 'Laptops Product 3', 'High quality laptops model 3.', 30, 900.00, 850.00, true, 1, 1),
('/images/laptops_4.jpg', 'Laptops Product 4', 'High quality laptops model 4.', 20, 1500.00, 1400.00, false, 1, 1),
('/images/laptops_5.jpg', 'Laptops Product 5', 'High quality laptops model 5.', 60, 1100.00, 1050.00, false, 1, 1),

-- Category 2: Keyboards
('/images/keyboards_1.jpg', 'Keyboards Product 1', 'Ergonomic keyboard model 1.', 100, 50.00, 45.00, false, 2, 1),
('/images/keyboards_2.jpg', 'Keyboards Product 2', 'Ergonomic keyboard model 2.', 90, 60.00, 55.00, false, 2, 1),
('/images/keyboards_3.jpg', 'Keyboards Product 3', 'Ergonomic keyboard model 3.', 80, 55.00, 50.00, true, 2, 1),
('/images/keyboards_4.jpg', 'Keyboards Product 4', 'Ergonomic keyboard model 4.', 70, 65.00, 60.00, false, 2, 1),
('/images/keyboards_5.jpg', 'Keyboards Product 5', 'Ergonomic keyboard model 5.', 60, 70.00, 65.00, false, 2, 1),

-- Category 3: Monitors
('/images/monitors_1.jpg', 'Monitors Product 1', '4K monitor model 1.', 40, 300.00, 280.00, false, 3, 1),
('/images/monitors_2.jpg', 'Monitors Product 2', '4K monitor model 2.', 35, 320.00, 300.00, false, 3, 1),
('/images/monitors_3.jpg', 'Monitors Product 3', '4K monitor model 3.', 30, 310.00, 290.00, true, 3, 1),
('/images/monitors_4.jpg', 'Monitors Product 4', '4K monitor model 4.', 25, 330.00, 310.00, false, 3, 1),
('/images/monitors_5.jpg', 'Monitors Product 5', '4K monitor model 5.', 20, 340.00, 320.00, false, 3, 1),

-- Category 4: Mouses
('/images/mouses_1.jpg', 'Mouses Product 1', 'Wireless mouse model 1.', 200, 25.00, 20.00, false, 4, 1),
('/images/mouses_2.jpg', 'Mouses Product 2', 'Wireless mouse model 2.', 180, 30.00, 25.00, false, 4, 1),
('/images/mouses_3.jpg', 'Mouses Product 3', 'Wireless mouse model 3.', 160, 28.00, 23.00, true, 4, 1),
('/images/mouses_4.jpg', 'Mouses Product 4', 'Wireless mouse model 4.', 140, 32.00, 27.00, false, 4, 1),
('/images/mouses_5.jpg', 'Mouses Product 5', 'Wireless mouse model 5.', 120, 35.00, 30.00, false, 4, 1),

-- Category 5: Mobile Phones
('/images/mobilephones_1.jpg', 'Mobile Phones Product 1', 'Latest smartphone model 1.', 100, 800.00, 750.00, false, 5, 1),
('/images/mobilephones_2.jpg', 'Mobile Phones Product 2', 'Latest smartphone model 2.', 90, 850.00, 800.00, false, 5, 1),
('/images/mobilephones_3.jpg', 'Mobile Phones Product 3', 'Latest smartphone model 3.', 80, 820.00, 770.00, true, 5, 1),
('/images/mobilephones_4.jpg', 'Mobile Phones Product 4', 'Latest smartphone model 4.', 70, 870.00, 820.00, false, 5, 1),
('/images/mobilephones_5.jpg', 'Mobile Phones Product 5', 'Latest smartphone model 5.', 60, 900.00, 850.00, false, 5, 1);
