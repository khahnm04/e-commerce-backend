CREATE TABLE `categories` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `parent_id` BIGINT,
    `name` VARCHAR(255) NOT NULL,
    `slug` VARCHAR(255) NOT NULL,
    `thumbnail` VARCHAR(500),
    `is_featured` BOOLEAN DEFAULT FALSE,
    `show_in_menu` BOOLEAN DEFAULT FALSE,
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

ALTER TABLE `categories` ADD CONSTRAINT `fk_categories_parent_id` FOREIGN KEY (`parent_id`) REFERENCES `categories` (`id`);
