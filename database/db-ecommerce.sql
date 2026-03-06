CREATE DATABASE db_ecommerce;
USE db_ecommerce;

/* ====================== AUTH ========================= */
CREATE TABLE `users` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `email` varchar(255) UNIQUE,
  `phone_number` varchar(20) UNIQUE NOT NULL,
  `password` varchar(500),
  `provider` varchar(255) DEFAULT 'LOCAL' COMMENT 'LOCAL, GOOGLE, FACEBOOK',
  `provider_id` varchar(255) COMMENT 'ID định danh từ Google/Facebook trả về (NULL nếu là LOCAL)',
  `is_email_verified` boolean DEFAULT false,
  `is_phone_verified` boolean DEFAULT false,
  `full_name` varchar(255) NOT NULL,
  `date_of_birth` date,
  `avatar_url` varchar(500),
  `gender` ENUM ('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER',
  `status` ENUM ('ACTIVE', 'INACTIVE', 'BANNED', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
  `last_login_at` datetime,
  `login_ip` varchar(50),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `roles` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` VARCHAR(50) UNIQUE NOT NULL,
  `name` varchar(255) NOT NULL,
  `description` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime
);

CREATE TABLE `permissions` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(50) UNIQUE NOT NULL,
  `name` varchar(255) NOT NULL,
  `module` varchar(255),
  `description` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `user_roles` (
  `user_id` bigint NOT NULL,
  `role_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`user_id`, `role_id`)
);

CREATE TABLE `role_permissions` (
  `role_id` bigint NOT NULL,
  `permission_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`role_id`, `permission_id`)
);


/* ====================== USER ========================= */
CREATE TABLE `customers` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint UNIQUE NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `employees` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `user_id` bigint UNIQUE NOT NULL,
  `branch_id` bigint NOT NULL COMMENT 'Làm việc tại chi nhánh nào',
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã định danh: NV001, KT002...',
  `job_title` varchar(255) COMMENT 'Cửa hàng trưởng, Kỹ thuật viên, Thu ngân...',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `addresses` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `customer_id` bigint NOT NULL,
  `receiver_name` varchar(255) NOT NULL,
  `receiver_phone` varchar(255) NOT NULL,
  `province_code` int COMMENT 'Mã tỉnh (VD: 201)',
  `province_name` varchar(255) NOT NULL COMMENT 'Tên tỉnh (VD: Hà Nội)',
  `district_code` int COMMENT 'Mã quận/huyện (VD: 1442)',
  `district_name` varchar(255) NOT NULL,
  `ward_code` varchar(255) COMMENT 'Mã phường/xã (VD: 20101)',
  `ward_name` varchar(255) NOT NULL,
  `detail_address` varchar(255) NOT NULL COMMENT 'Số nhà, tên đường',
  `address_type` ENUM ('HOME', 'OFFICE', 'OTHER') DEFAULT 'HOME',
  `address_name` varchar(255),
  `is_default` boolean DEFAULT false COMMENT 'Địa chỉ mặc định',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


/* ====================== CATALOG ========================= */
CREATE TABLE `brands` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL,
  `slug` varchar(255) UNIQUE NOT NULL,
  `description` text,
  `logo_url` varchar(255),
  `country` varchar(255) COMMENT 'Xuất xứ: USA, Korea, China',
  `website_url` varchar(255) COMMENT 'Trang chủ hãng',
  `meta_title` varchar(255),
  `meta_description` varchar(255),
  `meta_keywords` varchar(255),
  `status` ENUM ('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `categories` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `parent_id` bigint COMMENT 'Danh mục cha. NULL nếu là gốc',
  `name` varchar(255) UNIQUE NOT NULL,
  `slug` varchar(255) UNIQUE NOT NULL,
  `icon_url` varchar(255) COMMENT 'Icon nhỏ hiển thị trên Mega Menu (SVG/PNG)',
  `banner_url` varchar(255) COMMENT 'Ảnh bìa to hiển thị trang danh sách',
  `description` text,
  `position` int DEFAULT 0 COMMENT 'Thứ tự hiển thị (Sort Order)',
  `path` varchar(255) COMMENT 'Lưu đường dẫn ID: 1/5/10. Giúp query con cháu cực nhanh',
  `level` int DEFAULT 0 COMMENT 'Độ sâu của menu: 0=Root, 1=F1, 2=F2',
  `meta_title` varchar(255),
  `meta_description` varchar(255),
  `meta_keywords` varchar(255),
  `status` ENUM ('ACTIVE', 'INACTIVE', 'HIDDEN') DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `products` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `brand_id` bigint NOT NULL,
  `name` varchar(255) NOT NULL,
  `slug` varchar(255) UNIQUE NOT NULL,
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã chung: IP15PM',
  `has_serial_number` boolean DEFAULT true COMMENT 'True: Bắt nhập IMEI (Điện thoại), False: Không (Ốp lưng)',
  `warranty_months` int DEFAULT 12,
  `price` decimal(15, 2) NOT NULL DEFAULT 0 COMMENT 'Giá thấp nhất trong các variant (Cache)',
  `old_price` decimal(15, 2),
  `thumbnail` text NOT NULL,
  `description` longtext,
  `short_description` text,
  `meta_title` varchar(255),
  `meta_description` varchar(255),
  `status` ENUM ('ACTIVE', 'INACTIVE', 'INCOMING') NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `product_categories` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `category_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint
);

CREATE TABLE `product_images` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `image_url` text NOT NULL,
  `display_order` int,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `attributes` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) UNIQUE NOT NULL,
  `description` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `product_attribute_values` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `attribute_id` bigint NOT NULL,
  `value` varchar(255) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `variants` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) NOT NULL,
  `code` varchar(255) UNIQUE NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `variant_values` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `variant_id` bigint NOT NULL,
  `code` varchar(255),
  `value` varchar(255) NOT NULL,
  `display_order` int,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `product_variants` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `product_id` bigint NOT NULL,
  `sku` varchar(255) UNIQUE NOT NULL,
  `name` varchar(255) NOT NULL,
  `price` decimal(15, 2) NOT NULL,
  `old_price` decimal(15, 2),
  `image` text,
  `status` ENUM('ACTIVE', 'INACTIVE', 'OUT_OF_STOCK') NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `product_variant_values` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `product_variant_id` bigint NOT NULL,
  `variant_value_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `branches` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `name` varchar(255) UNIQUE NOT NULL,
  `address_detail` varchar(255) NOT NULL,
  `ward` varchar(255),
  `district` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `latitude` decimal(15, 2),
  `longitude` decimal(15, 2),
  `phone_number` varchar(255) NOT NULL,
  `email` varchar(255),
  `opening_hours` varchar(255),
  `status` ENUM('ACTIVE', 'INACTIVE') NOT NULL DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);


/* ====================== INVENTORY ========================= */
CREATE TABLE `warehouses` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `branch_id` bigint NOT NULL COMMENT 'Thuộc chi nhánh nào',
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã kho: HN01-W1, HN01-SR',
  `name` varchar(255) NOT NULL COMMENT 'Tên kho',
  `type` ENUM ('MAIN', 'SHOWROOM', 'BACKUP') NOT NULL DEFAULT 'MAIN',
  `capacity` int COMMENT 'Sức chứa tối đa (số sản phẩm)',
  `status` ENUM ('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `suppliers` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã NCC: SUP001',
  `name` varchar(255) NOT NULL COMMENT 'Tên nhà cung cấp',
  `contact_person` varchar(255) COMMENT 'Người liên hệ',
  `phone` varchar(255),
  `email` varchar(255),
  `address` text,
  `tax_code` varchar(255) COMMENT 'Mã số thuế',
  `payment_terms` varchar(255) COMMENT 'Điều khoản thanh toán: Trả sau 30 ngày',
  `status` ENUM ('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `serial_numbers` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `serial_code` varchar(255) UNIQUE NOT NULL COMMENT 'IMEI hoặc Serial Number - DUY NHẤT',
  `product_variant_id` bigint NOT NULL COMMENT 'Thuộc variant nào',
  `warehouse_id` bigint COMMENT 'Đang ở kho nào (NULL nếu đã bán)',
  `status` ENUM ('IN_STOCK', 'SOLD', 'TRANSFERRED', 'DEFECTIVE', 'WARRANTY') NOT NULL DEFAULT 'IN_STOCK',
  `purchase_price` decimal(15, 2) COMMENT 'Giá nhập thực tế của sản phẩm này',
  `sold_price` decimal(15, 2) COMMENT 'Giá bán thực tế',
  `sold_date` datetime COMMENT 'Ngày bán',
  `warranty_start_date` date COMMENT 'Ngày bắt đầu bảo hành',
  `warranty_end_date` date COMMENT 'Ngày hết hạn bảo hành',
  `notes` text COMMENT 'Ghi chú về tình trạng (trầy xước, lỗi...)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP COMMENT 'Ngày nhập kho lần đầu',
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `import_orders` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã phiếu: IMP20250122001',
  `warehouse_id` bigint NOT NULL COMMENT 'Nhập vào kho nào',
  `supplier_id` bigint NOT NULL COMMENT 'Nhập từ nhà cung cấp nào',
  `import_date` datetime NOT NULL COMMENT 'Ngày nhập hàng',
  `total_quantity` int NOT NULL DEFAULT 0 COMMENT 'Tổng số lượng sản phẩm',
  `total_amount` decimal(15, 2) NOT NULL DEFAULT 0 COMMENT 'Tổng giá trị đơn nhập',
  `status` ENUM ('PENDING', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
  `notes` text COMMENT 'Ghi chú',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `approved_by` bigint COMMENT 'Người duyệt phiếu',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `import_details` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `import_order_id` bigint NOT NULL COMMENT 'Thuộc phiếu nhập nào',
  `serial_number_id` bigint NOT NULL COMMENT 'Serial/IMEI được nhập',
  `unit_price` decimal(15, 2) NOT NULL COMMENT 'Giá nhập của sản phẩm này',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint
);

CREATE TABLE `transfer_orders` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã phiếu: TRF20250122001',
  `from_warehouse_id` bigint NOT NULL COMMENT 'Kho xuất hàng',
  `to_warehouse_id` bigint NOT NULL COMMENT 'Kho nhận hàng',
  `transfer_date` datetime NOT NULL COMMENT 'Ngày tạo phiếu',
  `total_quantity` int NOT NULL DEFAULT 0,
  `status` ENUM ('PENDING', 'IN_TRANSIT', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'PENDING',
  `shipping_method` varchar(255) COMMENT 'Phương thức vận chuyển: Xe tải, Giao hàng nhanh',
  `received_date` datetime COMMENT 'Ngày kho đích nhận được hàng',
  `notes` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `approved_by` bigint COMMENT 'Người duyệt',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `transfer_details` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `transfer_order_id` bigint NOT NULL,
  `serial_number_id` bigint NOT NULL COMMENT 'Serial được chuyển',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint
);

CREATE TABLE `inventory_checks` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã phiếu kiểm kê: CHK20250122001',
  `warehouse_id` bigint NOT NULL COMMENT 'Kiểm kê kho nào',
  `check_date` datetime NOT NULL COMMENT 'Ngày kiểm kê',
  `check_type` varchar(255) NOT NULL COMMENT 'PERIODIC: Định kỳ, RANDOM: Đột xuất, YEAR_END: Cuối năm',
  `system_quantity` int NOT NULL DEFAULT 0 COMMENT 'Số lượng theo hệ thống',
  `actual_quantity` int NOT NULL DEFAULT 0 COMMENT 'Số lượng đếm thực tế',
  `difference` int NOT NULL DEFAULT 0 COMMENT 'Chênh lệch actual - system',
  `status` ENUM ('IN_PROGRESS', 'COMPLETED', 'CANCELLED') NOT NULL DEFAULT 'IN_PROGRESS',
  `notes` text,
  `checked_by` bigint COMMENT 'Người kiểm kê',
  `approved_by` bigint COMMENT 'Người duyệt',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime
);

CREATE TABLE `inventory_check_details` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `inventory_check_id` bigint NOT NULL,
  `product_variant_id` bigint NOT NULL COMMENT 'Kiểm kê variant nào',
  `system_quantity` int NOT NULL DEFAULT 0 COMMENT 'SL hệ thống',
  `actual_quantity` int NOT NULL DEFAULT 0 COMMENT 'SL thực tế',
  `difference` int NOT NULL DEFAULT 0 COMMENT 'Chênh lệch',
  `reason` text COMMENT 'Lý do chênh lệch (nếu có)',
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint
);

CREATE TABLE `serial_movement_logs` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `serial_number_id` bigint NOT NULL COMMENT 'Serial nào di chuyển',
  `movement_type` ENUM ('IMPORT', 'TRANSFER', 'SALE', 'WARRANTY', 'ADJUSTMENT') NOT NULL COMMENT 'IMPORT: Nhập, TRANSFER: Chuyển, SALE: Bán, WARRANTY: Bảo hành, ADJUSTMENT: Điều chỉnh',
  `from_warehouse_id` bigint COMMENT 'Từ kho nào (NULL nếu nhập mới)',
  `to_warehouse_id` bigint COMMENT 'Đến kho nào (NULL nếu bán)',
  `reference_type` varchar(255) COMMENT 'Loại chứng từ: IMPORT_ORDER, TRANSFER_ORDER, ORDER',
  `reference_id` bigint COMMENT 'ID chứng từ',
  `movement_date` datetime NOT NULL COMMENT 'Ngày di chuyển',
  `notes` text,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint COMMENT 'Người thực hiện'
);

CREATE TABLE `inventory_summary` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `product_variant_id` bigint NOT NULL,
  `warehouse_id` bigint NOT NULL,
  `total_quantity` int NOT NULL DEFAULT 0 COMMENT 'Tổng số lượng (đếm từ serial_numbers)',
  `available_quantity` int NOT NULL DEFAULT 0 COMMENT 'Số lượng có thể bán (status = IN_STOCK)',
  `last_import_date` datetime COMMENT 'Ngày nhập gần nhất',
  `last_sale_date` datetime COMMENT 'Ngày bán gần nhất',
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT 'Cập nhật lần cuối'
);


/* ====================== CART ========================= */
CREATE TABLE `carts` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `customer_id` bigint UNIQUE NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);

CREATE TABLE `cart_items` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `cart_id` bigint NOT NULL,
  `product_variant_id` bigint NOT NULL,
  `quantity` int,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
);


/* ====================== ORDER ========================= */
CREATE TABLE `orders` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `order_code` varchar(255) UNIQUE NOT NULL,
  `customer_id` bigint NOT NULL,
  `employee_id` bigint,
  `branch_id` bigint NOT NULL COMMENT 'Chi nhánh xử lý đơn',
  `warehouse_id` bigint COMMENT 'Kho xuất hàng',
  `total_amount` decimal(15, 2) NOT NULL,
  `order_status` varchar(255),
  `shipping_name` varchar(255) NOT NULL,
  `shipping_phone` varchar(255) NOT NULL,
  `shipping_province` varchar(255) NOT NULL,
  `shipping_district` varchar(255) NOT NULL,
  `shipping_ward` varchar(255) NOT NULL,
  `shipping_address_detail` varchar(255) NOT NULL,
  `note` text,
  `required_date` date,
  `shipped_date` date,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `order_details` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `product_variant_id` bigint NOT NULL,
  `serial_number_id` bigint COMMENT 'Serial cụ thể được bán (nếu có)',
  `unit_price` decimal(15, 2) NOT NULL,
  `quantity` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);


/* ====================== PAYMENT ========================= */
CREATE TABLE `payments` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `order_id` bigint NOT NULL,
  `payment_method` varchar(255),
  `payment_status` varchar(255),
  `amount` decimal(15, 2) NOT NULL,
  `transaction_code` varchar(255),
  `paid_at` datetime,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);


/* ====================== PROMOTION ========================= */
CREATE TABLE `coupons` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL,
  `discount_type` varchar(255),
  `discount_value` decimal(15, 2) NOT NULL,
  `min_order_value` decimal(15, 2),
  `max_usage` int,
  `usage_count` int,
  `start_date` datetime,
  `end_date` datetime,
  `active` boolean,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `coupon_usages` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `coupon_id` bigint NOT NULL,
  `customer_id` bigint NOT NULL,
  `order_id` bigint NOT NULL,
  `discount_amount` decimal(15, 2) NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE `banners` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `name` varchar(255) UNIQUE NOT NULL,
  `image` text NOT NULL,
  `link_url` varchar(255),
  `position` varchar(255),
  `display_order` int,
  `status` varchar(255),
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `deleted_at` datetime
);


/* ====================== NEWS ========================= */
CREATE TABLE `news` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `slug` varchar(255) UNIQUE NOT NULL,
  `image` text,
  `summary` text,
  `content` longtext NOT NULL,
  `view_count` bigint,
  `is_featured` boolean,
  `status` varchar(255),
  `published_at` datetime,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP,
  `created_by` bigint,
  `updated_at` datetime DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `updated_by` bigint,
  `deleted_at` datetime
);

CREATE TABLE `news_products` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `news_id` bigint NOT NULL,
  `product_id` bigint NOT NULL,
  `created_at` datetime DEFAULT CURRENT_TIMESTAMP
);


/* ====================== WARRANTY ========================= */
CREATE TABLE `warranty_records` (
  `id` bigint PRIMARY KEY AUTO_INCREMENT,
  `code` varchar(255) UNIQUE NOT NULL COMMENT 'Mã phiếu bảo hành: WR20250122001',
  `serial_number_id` bigint NOT NULL COMMENT 'Sản phẩm nào cần bảo hành',
  `customer_id` bigint COMMENT 'Khách hàng nào (có thể NULL nếu không có TK)',
  `order_id` bigint COMMENT 'Thuộc đơn hàng nào',
  `warehouse_id` bigint NOT NULL COMMENT 'Chi nhánh tiếp nhận bảo hành',
  `receive_date` datetime NOT NULL COMMENT 'Ngày nhận máy bảo hành',
  `issue_description` text NOT NULL COMMENT 'Mô tả lỗi: Màn hình bị đơ, không lên nguồn...',
  `warranty_type` ENUM ('REPAIR', 'REPLACE', 'REFUND') NOT NULL COMMENT 'REPAIR: Sửa chữa, REPLACE: Đổi mới, REFUND: Hoàn tiền',
  `status` varchar(255) NOT NULL DEFAULT 'PENDING' COMMENT 'PENDING: Chờ xử lý, IN_PROGRESS: Đang sửa, COMPLETED: Hoàn thành, REJECTED: Từ chối',
  `repair_cost` decimal(15, 2) DEFAULT 0 COMMENT 'Chi phí sửa chữa (nếu ngoài BH)',
  `completion_date` datetime COMMENT 'Ngày hoàn thành',
  `technician_notes` text COMMENT 'Ghi chú kỹ thuật viên: Đã thay màn hình mới',
  `rejection_reason` text COMMENT 'Lý do từ chối (nếu REJECTED): Hết BH, lỗi do người dùng',
  `created_at` datetime,
  `created_by` bigint,
  `updated_at` datetime,
  `updated_by` bigint,
  `deleted_at` datetime
);



/* ====================== FOREIGN KEY ========================= */

/* ====================== AUTH ========================= */
ALTER TABLE `user_roles` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `user_roles` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
ALTER TABLE `role_permissions` ADD FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`);
ALTER TABLE `role_permissions` ADD FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`);

/* ====================== USER ========================= */
ALTER TABLE `customers` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `employees` ADD FOREIGN KEY (`user_id`) REFERENCES `users` (`id`);
ALTER TABLE `employees` ADD FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`);
ALTER TABLE `addresses` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);

/* ====================== CATALOG ========================= */
ALTER TABLE `categories` ADD FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`);
ALTER TABLE `products` ADD FOREIGN KEY (`brand_id`) REFERENCES `brands` (`id`);
ALTER TABLE `product_categories` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `product_categories` ADD FOREIGN KEY (`category_id`) REFERENCES `categories` (`id`);
ALTER TABLE `product_images` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `product_attribute_values` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `product_attribute_values` ADD FOREIGN KEY (`attribute_id`) REFERENCES `attributes` (`id`);
ALTER TABLE `variant_values` ADD FOREIGN KEY (`variant_id`) REFERENCES `variants` (`id`);
ALTER TABLE `product_variants` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);
ALTER TABLE `product_variant_values` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
ALTER TABLE `product_variant_values` ADD FOREIGN KEY (`variant_value_id`) REFERENCES `variant_values` (`id`);

/* ====================== INVENTORY ========================= */
ALTER TABLE `warehouses` ADD FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`);
ALTER TABLE `serial_numbers` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
ALTER TABLE `serial_numbers` ADD FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `import_orders` ADD FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `import_orders` ADD FOREIGN KEY (`supplier_id`) REFERENCES `suppliers` (`id`);
ALTER TABLE `import_details` ADD FOREIGN KEY (`import_order_id`) REFERENCES `import_orders` (`id`);
ALTER TABLE `import_details` ADD FOREIGN KEY (`serial_number_id`) REFERENCES `serial_numbers` (`id`);
ALTER TABLE `transfer_orders` ADD FOREIGN KEY (`from_warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `transfer_orders` ADD FOREIGN KEY (`to_warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `transfer_details` ADD FOREIGN KEY (`transfer_order_id`) REFERENCES `transfer_orders` (`id`);
ALTER TABLE `transfer_details` ADD FOREIGN KEY (`serial_number_id`) REFERENCES `serial_numbers` (`id`);
ALTER TABLE `inventory_checks` ADD FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `inventory_check_details` ADD FOREIGN KEY (`inventory_check_id`) REFERENCES `inventory_checks` (`id`);
ALTER TABLE `inventory_check_details` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
ALTER TABLE `serial_movement_logs` ADD FOREIGN KEY (`serial_number_id`) REFERENCES `serial_numbers` (`id`);
ALTER TABLE `serial_movement_logs` ADD FOREIGN KEY (`from_warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `serial_movement_logs` ADD FOREIGN KEY (`to_warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `inventory_summary` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
ALTER TABLE `inventory_summary` ADD FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`);

/* ====================== CART ========================= */
ALTER TABLE `carts` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
ALTER TABLE `cart_items` ADD FOREIGN KEY (`cart_id`) REFERENCES `carts` (`id`);
ALTER TABLE `cart_items` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);

/* ====================== ORDER ========================= */
ALTER TABLE `orders` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
ALTER TABLE `orders` ADD FOREIGN KEY (`employee_id`) REFERENCES `employees` (`id`);
ALTER TABLE `orders` ADD FOREIGN KEY (`branch_id`) REFERENCES `branches` (`id`);
ALTER TABLE `orders` ADD FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`);
ALTER TABLE `order_details` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);
ALTER TABLE `order_details` ADD FOREIGN KEY (`product_variant_id`) REFERENCES `product_variants` (`id`);
ALTER TABLE `order_details` ADD FOREIGN KEY (`serial_number_id`) REFERENCES `serial_numbers` (`id`);

/* ====================== PROMOTION ========================= */
ALTER TABLE `coupon_usages` ADD FOREIGN KEY (`coupon_id`) REFERENCES `coupons` (`id`);
ALTER TABLE `coupon_usages` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
ALTER TABLE `coupon_usages` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

/* ====================== PAYMENT ========================= */
ALTER TABLE `payments` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);

/* ====================== NEWS ========================= */
ALTER TABLE `news_products` ADD FOREIGN KEY (`news_id`) REFERENCES `news` (`id`);
ALTER TABLE `news_products` ADD FOREIGN KEY (`product_id`) REFERENCES `products` (`id`);

/* ====================== WARRANTY ========================= */
ALTER TABLE `warranty_records` ADD FOREIGN KEY (`serial_number_id`) REFERENCES `serial_numbers` (`id`);
ALTER TABLE `warranty_records` ADD FOREIGN KEY (`customer_id`) REFERENCES `customers` (`id`);
ALTER TABLE `warranty_records` ADD FOREIGN KEY (`order_id`) REFERENCES `orders` (`id`);
ALTER TABLE `warranty_records` ADD FOREIGN KEY (`warehouse_id`) REFERENCES `warehouses` (`id`);



/* ====================== INDEX & CONSTRAINT ========================= */
CREATE UNIQUE INDEX `product_categories_unique` ON `product_categories` (`product_id`, `category_id`);
CREATE UNIQUE INDEX `product_attribute_values_unique` ON `product_attribute_values` (`product_id`, `attribute_id`);
CREATE UNIQUE INDEX `variant_values_unique` ON `variant_values` (`variant_id`, `value`);
CREATE UNIQUE INDEX `product_variant_values_unique` ON `product_variant_values` (`product_variant_id`, `variant_value_id`);
CREATE UNIQUE INDEX `import_details_unique` ON `import_details` (`import_order_id`, `serial_number_id`);
CREATE UNIQUE INDEX `transfer_details_unique` ON `transfer_details` (`transfer_order_id`, `serial_number_id`);
CREATE UNIQUE INDEX `inventory_check_details_unique` ON `inventory_check_details` (`inventory_check_id`, `product_variant_id`);
CREATE UNIQUE INDEX `inventory_summary_unique` ON `inventory_summary` (`product_variant_id`, `warehouse_id`);
CREATE UNIQUE INDEX `cart_items_unique` ON `cart_items` (`cart_id`, `product_variant_id`);
CREATE UNIQUE INDEX `order_details_unique` ON `order_details` (`order_id`, `product_variant_id`);
CREATE UNIQUE INDEX `news_products_unique` ON `news_products` (`news_id`, `product_id`);



ALTER DATABASE db_ecommerce
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

