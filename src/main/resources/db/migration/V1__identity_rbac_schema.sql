CREATE TABLE `user_providers` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `account_user_id` BIGINT NOT NULL,
    `provider` VARCHAR(50) NOT NULL,
    `provider_id` VARCHAR(255) NOT NULL
);

CREATE TABLE `roles` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(50) NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    `created_by` VARCHAR(255),
    `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `updated_by` VARCHAR(255),
    `deleted_at` DATETIME(6),
    `deleted_by` VARCHAR(255),
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE (code, (IF(is_deleted = 0, 1, NULL)))
);

CREATE TABLE `permissions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `code` VARCHAR(50) UNIQUE NOT NULL,
    `name` VARCHAR(255) NOT NULL
);

CREATE TABLE `account_admin_roles` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `account_admin_id` BIGINT NOT NULL,
    `role_id` BIGINT NOT NULL
);

CREATE TABLE `role_permissions` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `role_id` BIGINT NOT NULL,
    `permission_id` BIGINT NOT NULL
);

CREATE TABLE `account_users` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(20) NOT NULL,
    `password` VARCHAR(500),
    `full_name` VARCHAR(255) NOT NULL,
    `date_of_birth` DATE,
    `avatar` VARCHAR(500),
    `gender` ENUM ('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER',
    `status` ENUM ('ACTIVE', 'INACTIVE', 'BANNED', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    `last_login_at` DATETIME,
    `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    `created_by` VARCHAR(255),
    `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `updated_by` VARCHAR(255),
    `deleted_at` DATETIME(6),
    `deleted_by` VARCHAR(255),
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(email, (IF(is_deleted = 0, 1, NULL))),
    UNIQUE(phone_number, (IF(is_deleted = 0, 1, NULL)))
);

CREATE TABLE `account_admins` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `job_title` VARCHAR(255),
    `email` VARCHAR(255) NOT NULL,
    `phone_number` VARCHAR(20) NOT NULL,
    `password` VARCHAR(500),
    `full_name` VARCHAR(255) NOT NULL,
    `date_of_birth` DATE,
    `avatar` VARCHAR(500),
    `gender` ENUM ('MALE', 'FEMALE', 'OTHER') DEFAULT 'OTHER',
    `status` ENUM ('ACTIVE', 'INACTIVE', 'BANNED', 'LOCKED') NOT NULL DEFAULT 'ACTIVE',
    `last_login_at` DATETIME,
    `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    `created_by` VARCHAR(255),
    `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `updated_by` VARCHAR(255),
    `deleted_at` DATETIME(6),
    `deleted_by` VARCHAR(255),
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(email, (IF(is_deleted = 0, 1, NULL))),
    UNIQUE(phone_number, (IF(is_deleted = 0, 1, NULL)))
);

CREATE TABLE `addresses` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `account_user_id` BIGINT NOT NULL,
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
    `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    `created_by` VARCHAR(255),
    `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `updated_by` VARCHAR(255),
    `deleted_at` DATETIME(6),
    `deleted_by` VARCHAR(255),
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE
);

/* ====================== FOREIGN KEY ========================= */
ALTER TABLE `account_admin_roles` ADD CONSTRAINT `fk_account_admin_roles_account_admins` FOREIGN KEY (`account_admin_id`) REFERENCES `account_admins` (`id`) ON DELETE RESTRICT;
ALTER TABLE `account_admin_roles` ADD CONSTRAINT `fk_account_admin_roles_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT;
ALTER TABLE `role_permissions` ADD CONSTRAINT `fk_role_permissions_roles` FOREIGN KEY (`role_id`) REFERENCES `roles` (`id`) ON DELETE RESTRICT;
ALTER TABLE `role_permissions` ADD CONSTRAINT `fk_role_permissions_permissions` FOREIGN KEY (`permission_id`) REFERENCES `permissions` (`id`) ON DELETE RESTRICT;
ALTER TABLE `user_providers` ADD CONSTRAINT `fk_user_providers_account_users` FOREIGN KEY (`account_user_id`) REFERENCES `account_users` (`id`) ON DELETE RESTRICT;
ALTER TABLE `addresses` ADD CONSTRAINT `fk_addresses_account_users` FOREIGN KEY (`account_user_id`) REFERENCES `account_users` (`id`) ON DELETE RESTRICT;
