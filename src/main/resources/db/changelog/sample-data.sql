INSERT INTO tbl_product (
    product_name, product_description,
    product_quantity, product_price, product_special_price,
    unavailable, category_id, seller_id
) VALUES
-- Electronics
('Apple MacBook Air M2', 'Powerful and portable laptop with M2 chip and 13-inch Retina display.', 25, 1199.00, 1149.00, false, 1, 1),
('Samsung Galaxy S23', 'Flagship Android smartphone with Dynamic AMOLED display.', 40, 999.00, 949.00, false, 1, 1),
('Sony WH-1000XM5 Headphones', 'Noise cancelling wireless headphones with premium sound.', 60, 399.00, 379.00, false, 1, 1),
('Dell UltraSharp Monitor U2723QE', '27" 4K IPS monitor with USB-C and accurate color reproduction.', 15, 679.00, 649.00, false, 1, 1),
('Anker Soundcore Bluetooth Speaker', 'Portable Bluetooth speaker with 24-hour battery life.', 80, 59.00, 49.00, false, 1, 1),

-- Books
('Atomic Habits', 'An Easy & Proven Way to Build Good Habits & Break Bad Ones by James Clear.', 100, 20.00, 17.50, false, 2, 1),
('The Midnight Library', 'Novel by Matt Haig exploring regrets and alternate lives.', 90, 18.00, 16.00, false, 2, 1),
('Clean Code', 'A Handbook of Agile Software Craftsmanship by Robert C. Martin.', 75, 32.00, 29.00, false, 2, 1),
('Dune', 'Classic science fiction novel by Frank Herbert.', 50, 25.00, 21.00, false, 2, 1),
('Educated', 'Memoir by Tara Westover about growing up off the grid.', 65, 22.00, 19.00, false, 2, 1),

-- Home Appliances
('Dyson V11 Vacuum Cleaner', 'High-performance cordless vacuum for home cleaning.', 20, 599.00, 549.00, false, 3, 1),
('Philips Air Fryer XL', 'Healthy way to fry food with little or no oil.', 35, 149.00, 139.00, false, 3, 1),
('Instant Pot Duo 7-in-1', 'Electric pressure cooker with multiple functions.', 40, 89.00, 79.00, false, 3, 1),
('iRobot Roomba i7+', 'Smart vacuum robot with self-emptying bin.', 10, 799.00, 749.00, false, 3, 1),
('Breville Barista Express', 'Espresso machine with built-in grinder.', 15, 699.00, 659.00, false, 3, 1),

-- Gaming
('PlayStation 5 Console', 'Next-gen console with ultra-fast SSD and DualSense controller.', 30, 499.00, 479.00, false, 4, 1),
('Xbox Series X', 'Powerful console from Microsoft with Game Pass support.', 25, 499.00, 479.00, false, 4, 1),
('Nintendo Switch OLED', 'Hybrid gaming console with vibrant OLED screen.', 40, 349.00, 329.00, false, 4, 1),
('Logitech G Pro X Headset', 'High-quality headset with Blue Voice mic for competitive gaming.', 70, 129.00, 119.00, false, 4, 1),
('Razer DeathAdder V2 Mouse', 'Ergonomic gaming mouse with fast response.', 90, 69.00, 59.00, false, 4, 1),

-- Fitness & Health
('Fitbit Charge 5', 'Fitness tracker with stress management and heart rate monitor.', 60, 149.00, 139.00, false, 5, 1),
('Theragun Mini', 'Portable muscle treatment massage gun.', 50, 199.00, 179.00, false, 5, 1),
('Bowflex Adjustable Dumbbells', 'Compact dumbbells that adjust from 5 to 52.5 lbs.', 20, 399.00, 369.00, false, 5, 1),
('Yoga Mat by Lululemon', 'High-grip mat designed for yoga and pilates.', 100, 88.00, 79.00, false, 5, 1),
('Peloton Bike+', 'Connected indoor cycling experience with live classes.', 5, 2495.00, 2395.00, true, 5, 1);
