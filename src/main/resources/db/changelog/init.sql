-- enum types
CREATE TYPE role_type AS ENUM ('ROLE_ADMIN', 'ROLE_SELLER', 'ROLE_USER');
CREATE TYPE payment_type AS ENUM ('PAYPAL', 'STRIPE', 'RAZORPAY', 'GOOGLE_PAY', 'APPLE_PAY', 'AMAZON_PAY');
CREATE TYPE payment_method AS ENUM ('DEBIT_CARD', 'CREDIT_CARD', 'PAYPAL_BALANCE');
CREATE TYPE order_status AS ENUM ('PENDING', 'DELIVERED', 'CANCELLED', 'ACCEPTED', 'REJECTED');

-- Tables
CREATE TABLE tbl_role
(
    id        BIGSERIAL PRIMARY KEY,
    role_name role_type NOT NULL UNIQUE
);

CREATE TABLE tbl_category
(
    id   BIGSERIAL PRIMARY KEY,
    name VARCHAR(15) NOT NULL UNIQUE
);

CREATE TABLE tbl_user
(
    id       BIGSERIAL PRIMARY KEY,
    username VARCHAR(30)  NOT NULL UNIQUE,
    email    VARCHAR(30)  NOT NULL UNIQUE,
    password VARCHAR(100) NOT NULL
);

CREATE TABLE tbl_product
(
    id                    BIGSERIAL PRIMARY KEY,
    image                 VARCHAR(255),
    product_name          VARCHAR(255)   NOT NULL,
    product_description   TEXT           NOT NULL,
    product_quantity      INT            NOT NULL,
    product_price         DECIMAL(10, 2) NOT NULL,
    product_special_price DECIMAL(10, 2),
    unavailable           BOOLEAN DEFAULT false,
    user_id               BIGINT         NOT NULL,
    category_id           BIGINT         NOT NULL,
    CONSTRAINT fk_seller_product FOREIGN KEY (user_id) REFERENCES tbl_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_product_category FOREIGN KEY (category_id) REFERENCES tbl_category (id) ON DELETE RESTRICT
);

CREATE TABLE tbl_cart
(
    id          BIGSERIAL PRIMARY KEY,
    user_id     BIGINT NOT NULL UNIQUE, -- One cart per user
    total_price DECIMAL(10, 2) DEFAULT 0,
    CONSTRAINT fk_user_cart FOREIGN KEY (user_id) REFERENCES tbl_user (id) ON DELETE CASCADE
);

CREATE TABLE tbl_cart_item
(
    id            BIGSERIAL PRIMARY KEY,
    cart_id       BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    quantity      INT DEFAULT 1,
    price         DECIMAL(10, 2) NOT NULL,
    special_price DECIMAL(10, 2),
    CONSTRAINT fk_item_cart FOREIGN KEY (cart_id) REFERENCES tbl_cart (id) ON DELETE CASCADE,
    CONSTRAINT fk_cart_item_product FOREIGN KEY (product_id) REFERENCES tbl_product (id) ON DELETE CASCADE,
    CONSTRAINT uk_cart_product UNIQUE (cart_id, product_id) -- Prevent duplicate products in cart
);

CREATE TABLE tbl_address
(
    id            BIGSERIAL PRIMARY KEY,
    street_number VARCHAR(6)  NOT NULL,
    street_name   VARCHAR(20) NOT NULL,
    city          VARCHAR(15) NOT NULL,
    state         VARCHAR(15),
    postal_code   VARCHAR(6)  NOT NULL,
    user_id       BIGINT      NOT NULL,
    CONSTRAINT fk_user_address FOREIGN KEY (user_id) REFERENCES tbl_user (id) ON DELETE CASCADE
);

CREATE TABLE tbl_payment
(
    id                       BIGSERIAL PRIMARY KEY,
    type                     payment_type   NOT NULL,
    method                   payment_method NOT NULL,
    gateway_id               VARCHAR(255),
    gateway_status           VARCHAR(255),
    gateway_response_message VARCHAR(255)
);

CREATE TABLE tbl_order
(
    id              BIGSERIAL PRIMARY KEY,
    user_address_id BIGINT         NOT NULL,
    payment_id      BIGINT         NOT NULL,
    order_date      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_amount    DECIMAL(10, 2) NOT NULL,
    status          order_status   NOT NULL,
    CONSTRAINT fk_order_address FOREIGN KEY (user_address_id) REFERENCES tbl_address (id) ON DELETE RESTRICT,
    CONSTRAINT fk_order_payment FOREIGN KEY (payment_id) REFERENCES tbl_payment (id) ON DELETE RESTRICT
);

CREATE TABLE tbl_order_item
(
    id            BIGSERIAL PRIMARY KEY,
    order_id      BIGINT         NOT NULL,
    product_id    BIGINT         NOT NULL,
    quantity      INT            NOT NULL DEFAULT 1,
    price         DECIMAL(10, 2) NOT NULL,
    special_price DECIMAL(10, 2),
    CONSTRAINT fk_item_order FOREIGN KEY (order_id) REFERENCES tbl_order (id) ON DELETE CASCADE,
    CONSTRAINT fk_order_item_product FOREIGN KEY (product_id) REFERENCES tbl_product (id) ON DELETE RESTRICT
);

CREATE TABLE tbl_user_role
(
    user_id BIGINT NOT NULL,
    role_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, role_id),
    CONSTRAINT fk_user_role_user FOREIGN KEY (user_id) REFERENCES tbl_user (id) ON DELETE CASCADE,
    CONSTRAINT fk_user_role_role FOREIGN KEY (role_id) REFERENCES tbl_role (id) ON DELETE CASCADE
);

-- Indexes
CREATE INDEX idx_product_user ON tbl_product (user_id);
CREATE INDEX idx_product_category ON tbl_product (category_id);
CREATE INDEX idx_cart_item_cart ON tbl_cart_item (cart_id);
CREATE INDEX idx_cart_item_product ON tbl_cart_item (product_id);
CREATE INDEX idx_address_user ON tbl_address (user_id);
CREATE INDEX idx_order_address ON tbl_order (user_address_id);
CREATE INDEX idx_order_payment ON tbl_order (payment_id);
CREATE INDEX idx_order_item_order ON tbl_order_item (order_id);
CREATE INDEX idx_order_item_product ON tbl_order_item (product_id);
CREATE INDEX idx_user_role_user ON tbl_user_role (user_id);
CREATE INDEX idx_user_role_role ON tbl_user_role (role_id);