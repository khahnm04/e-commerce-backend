CREATE DATABASE db_ecommerce;
USE db_ecommerce;
ALTER DATABASE db_ecommerce CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

CREATE TABLE `users` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `email` VARCHAR(255) UNIQUE,
    `phone_number` VARCHAR(20) UNIQUE NOT NULL,
    `password` VARCHAR(500),
    `is_email_verified` BOOLEAN DEFAULT FALSE,
    `is_phone_verified` BOOLEAN DEFAULT FALSE,
    `full_name` VARCHAR(255) NOT NULL,
    `date_of_birth` date,
    `avatar` VARCHAR(500),
    `gender` ENUM ('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER',
    `status` ENUM ('ACTIVE', 'INACTIVE', 'BANNED', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    `last_login_at` DATETIME,
    `login_ip` VARCHAR(50),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `user_providers` (
     `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
     `user_id` BIGINT NOT NULL,
     `provider` VARCHAR(50) DEFAULT 'LOCAL',
     `provider_id` VARCHAR(255),
     `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
     `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
     UNIQUE (provider, provider_id)
);

CREATE TABLE `roles` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `description` TEXT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `permissions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `sub_module` VARCHAR(255),
    `module` VARCHAR(255),
    `description` TEXT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `user_roles` (
    `user_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`user_id`, `role_id`)
);

CREATE TABLE `role_permissions` (
    `role_id` BIGINT NOT NULL,
    `permission_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (`role_id`, `permission_id`)
);

CREATE TABLE `customers` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT UNIQUE NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `employees` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `user_id` BIGINT UNIQUE NOT NULL,
    `code` VARCHAR(255) UNIQUE NOT NULL,
    `job_title` VARCHAR(255),
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `addresses` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `customer_id` BIGINT NOT NULL,
    `receiver_name` VARCHAR(255) NOT NULL,
    `receiver_phone` VARCHAR(20) NOT NULL,
    `province_code` VARCHAR(50),
    `province_name` VARCHAR(255) NOT NULL,
    `district_code` VARCHAR(50),
    `district_name` VARCHAR(255) NOT NULL,
    `ward_code` VARCHAR(50),
    `ward_name` VARCHAR(255) NOT NULL,
    `detail_address` VARCHAR(255) NOT NULL,
    `address_type` ENUM ('HOME', 'OFFICE', 'OTHER') DEFAULT 'HOME',
    `address_name` VARCHAR(255),
    `is_default` BOOLEAN DEFAULT FALSE,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `brands` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) UNIQUE NOT NULL,
    `slug` VARCHAR(255) UNIQUE NOT NULL,
    `description` TEXT,
    `logo` VARCHAR(255),
    `status` ENUM ('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `categories` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `parent_id` BIGINT,
    `name` VARCHAR(255) UNIQUE NOT NULL,
    `slug` VARCHAR(255) UNIQUE NOT NULL,
    `thumbnail` VARCHAR(255),
    `icon` VARCHAR(255) NULL,
    `is_featured` BOOLEAN DEFAULT FALSE,
    `description` TEXT,
    `position` INT DEFAULT 0,
    `status` ENUM ('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `attributes` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `products` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `brand_id` BIGINT NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `slug` VARCHAR(255) UNIQUE NOT NULL,
    `code` VARCHAR(255) UNIQUE NOT NULL,
    `warranty_months` INT DEFAULT 12,
    `price` DECIMAL(15, 2) NOT NULL DEFAULT 0,
    `old_price` DECIMAL(15, 2),
    `thumbnail` VARCHAR(500) NOT NULL,
    `short_description` TEXT,
    `description` LONGTEXT,
    `status` ENUM ('ACTIVE', 'INACTIVE', 'INCOMING') NOT NULL DEFAULT 'ACTIVE',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `product_categories` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `category_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME,
    UNIQUE (product_id, category_id)
);

CREATE TABLE `product_attribute_values` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `attribute_id` BIGINT NOT NULL,
    `value` VARCHAR(255) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME,
    UNIQUE (product_id, attribute_id)
);

CREATE TABLE `product_images` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `image` TEXT NOT NULL,
    `position` INT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `variants` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `variant_values` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `variant_id` BIGINT NOT NULL,
    `value` VARCHAR(255) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME,
    UNIQUE (`variant_id`, `value`)
);

CREATE TABLE `product_variants` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_id` BIGINT NOT NULL,
    `sku` VARCHAR(255) UNIQUE NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `price` DECIMAL(15, 2) NOT NULL,
    `old_price` DECIMAL(15, 2),
    `image` VARCHAR(500),
    `status` ENUM('ACTIVE', 'INACTIVE', 'OUT_OF_STOCK') NOT NULL DEFAULT 'ACTIVE',
    `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `product_variant_values` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `product_variant_id` BIGINT NOT NULL,
    `variant_value_id` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME,
    UNIQUE (`product_variant_id`, `variant_value_id`)
);

CREATE TABLE `carts` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `customer_id` BIGINT UNIQUE NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `cart_items` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `cart_id` BIGINT NOT NULL,
    `product_variant_id` BIGINT NOT NULL,
    `quantity` INT,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `inventories` (
   `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
   `product_variant_id` BIGINT UNIQUE NOT NULL,
   `quantity` INT,
   `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
   `created_by` BIGINT,
   `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
   `updated_by` BIGINT,
   `deleted_at` DATETIME
);

CREATE TABLE `coupons` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(100) UNIQUE NOT NULL,        -- Mã giảm giá
    `name` VARCHAR(255) NOT NULL,               -- Tên mã giảm giá
    `description` TEXT,                         -- Mô tả giới thiệu
    `type_discount` ENUM('PERCENTAGE', 'FIXED') NOT NULL DEFAULT 'PERCENTAGE', -- Kiểu giảm giá: PERCENTAGE – Phần trăm, FIXED – Số tiền cố định
    `value` DECIMAL(15,2) NOT NULL,             -- Giá trị giảm (VD: 10 = 10%, hoặc 10000 = 10,000đ)
    `min_order_value` DECIMAL(15,2) DEFAULT 0,  -- Giá trị đơn hàng tối thiểu để áp dụng mã
    `max_discount_value` DECIMAL(15,2),         -- Số tiền giảm tối đa (chỉ áp dụng khi type_discount = PERCENTAGE)
    `usage_limit` INT,                          -- Giới hạn số lần sử dụng tổng cộng (NULL = không giới hạn)
    `used_count` INT NOT NULL DEFAULT 0,        -- Số lần đã sử dụng
    `start_date` DATETIME NOT NULL,             -- Ngày bắt đầu hiệu lực
    `end_date` DATETIME NOT NULL,               -- Ngày hết hạn
    `type_display` ENUM('PUBLIC', 'PRIVATE') NOT NULL DEFAULT 'PRIVATE',  -- Kiểu hiển thị: PUBLIC – Công khai, PRIVATE – Riêng tư
    `status` ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'INACTIVE',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `coupon_usages` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `coupon_id` BIGINT NOT NULL,
    `customer_id` BIGINT NOT NULL,
    `order_id` BIGINT NOT NULL,
    `discount_amount` DECIMAL(15, 2) NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `orders` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(100) UNIQUE NOT NULL,
    `customer_id` BIGINT NOT NULL,
    `coupon` VARCHAR(100),
    `sub_total` DECIMAL(15,2) NOT NULL, # tổng tiền hàng trước khi giảm
    `discount` DECIMAL(15,2) DEFAULT 0, # số tiền giảm từ coupon.
    `total` DECIMAL(15,2) NOT NULL,     # số tiền cuối cùng khách phải trả.
    `order_status` ENUM(
        'PENDING',
        'CONFIRMED',
        'SHIPPING',
        'COMPLETED',
        'CANCELLED',
        'RETURNED'
    ) NOT NULL DEFAULT 'PENDING',
    `shipping_name` VARCHAR(255) NOT NULL,
    `shipping_phone` VARCHAR(20) NOT NULL,
    `shipping_province` VARCHAR(255) NOT NULL,
    `shipping_district` VARCHAR(255) NOT NULL,
    `shipping_ward` VARCHAR(255) NOT NULL,
    `shipping_address_detail` VARCHAR(255) NOT NULL,
    `note` TEXT,
    `expected_delivery_date` DATETIME,  # Ngày giao hàng dự kiến
    `actual_delivery_date` DATETIME,    # Ngày giao hàng thực tế
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `order_details` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id` BIGINT NOT NULL,
    `product_variant_id` bigint NOT NULL,
    `unit_price` DECIMAL(15, 2) NOT NULL,
    `quantity` BIGINT NOT NULL,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE `payments` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `order_id` BIGINT UNIQUE NOT NULL,
    `payment_method` ENUM('MONEY', 'VNPAY', 'ZALOPAY'),
    `payment_status` ENUM('UNPAID', 'PAID', 'REFUNDED') NOT NULL DEFAULT 'UNPAID',
    `amount` DECIMAL(15, 2) NOT NULL,
    `transaction_code` VARCHAR(255),
    `paid_at` DATETIME,
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME
);

CREATE TABLE banners (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `image` VARCHAR(500) NOT NULL,
    `position` ENUM('HOME_TOP', 'HOME_MIDDLE') NOT NULL DEFAULT 'HOME_TOP',
    `sort_order` INT NOT NULL DEFAULT 0,
    `label` VARCHAR(100)  NULL COMMENT 'VD: BST MỚI NHẤT 2026',
    `heading` VARCHAR(255) NULL COMMENT 'Tiêu đề lớn chính',
    `description` VARCHAR(500) NULL COMMENT 'Mô tả phụ, VD: Giảm đến 45%',
    `cta_text` VARCHAR(100) NULL COMMENT 'Text nút bấm, VD: Khám phá',
    `cta_url` VARCHAR(500) NULL COMMENT 'Link nút bấm (có thể trùng link_url)',
    `countdown_end` DATETIME NULL,
    `status` ENUM ('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    `created_at` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `created_by` BIGINT,
    `updated_at` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    `updated_by` BIGINT,
    `deleted_at` DATETIME,
    `deleted_by` BIGINT
);

/* ====================== FOREIGN KEY ========================= */
/* ====================== AUTH ========================= */
ALTER TABLE `user_roles` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `user_roles` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
ALTER TABLE `role_permissions` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
ALTER TABLE `role_permissions` ADD FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`);
ALTER TABLE `user_providers` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
/* ====================== USER ========================= */
ALTER TABLE `customers` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `employees` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `addresses` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
/* ====================== CATALOG ========================= */
ALTER TABLE `categories` ADD FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`);
ALTER TABLE `products` ADD FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`);
ALTER TABLE `product_categories` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `product_categories` ADD FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);
ALTER TABLE `product_attribute_values` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `product_attribute_values` ADD FOREIGN KEY (`attribute_id`) REFERENCES `attributes` (`id`);
ALTER TABLE `product_images` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `variant_values` ADD FOREIGN KEY (`variant_id`) REFERENCES `variants` (`id`);
ALTER TABLE `product_variants` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `product_variant_values` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
ALTER TABLE `product_variant_values` ADD FOREIGN KEY (`variant_value_id`) REFERENCES `variant_values` (`id`);
/* ====================== CART ========================= */
ALTER TABLE `carts` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
ALTER TABLE `cart_items` ADD FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`);
ALTER TABLE `cart_items` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
/* ====================== INVENTORY ========================= */
ALTER TABLE `inventories` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
/* ====================== COUPON ========================= */
ALTER TABLE `coupon_usages` ADD FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`);
ALTER TABLE `coupon_usages` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
ALTER TABLE `coupon_usages` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);
/* ====================== ORDER ========================= */
ALTER TABLE `orders` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
ALTER TABLE `order_details` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);
ALTER TABLE `order_details` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
/* ====================== PAYMENT ========================= */
ALTER TABLE `payments` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);


