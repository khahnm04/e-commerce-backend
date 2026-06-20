CREATE TABLE `brands` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `name` VARCHAR(255) UNIQUE NOT NULL,
    `slug` VARCHAR(255) UNIQUE NOT NULL,
    `avatar` VARCHAR(500),
    `description` TEXT,
    `status` ENUM ('ACTIVE', 'INACTIVE') DEFAULT 'ACTIVE',
    `created_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    `created_by` VARCHAR(255),
    `updated_at` DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    `updated_by` VARCHAR(255),
    `deleted_at` DATETIME(6),
    `deleted_by` VARCHAR(255),
    `is_deleted` BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(slug, (IF(is_deleted = 0, 1, NULL)))
);
