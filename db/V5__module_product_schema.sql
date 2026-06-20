CREATE TABLE products (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    slug VARCHAR(255) UNIQUE NOT NULL,
    price_old DECIMAL(15, 2) DEFAULT 0.00,
    price_new DECIMAL(15, 2) DEFAULT 0.00,
    discount INT DEFAULT 0,
    stock INT DEFAULT 0,
    thumbnail VARCHAR(500) NOT NULL,
    images JSON,
    specifications JSON,
    view INT DEFAULT 0,
    description TEXT,
    content TEXT,
    status ENUM('DRAFT', 'ACTIVE', 'INACTIVE') DEFAULT 'DRAFT',
    created_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6),
    created_by VARCHAR(255),
    updated_at DATETIME(6) DEFAULT CURRENT_TIMESTAMP(6) ON UPDATE CURRENT_TIMESTAMP(6),
    updated_by VARCHAR(255),
    deleted_at DATETIME(6),
    deleted_by VARCHAR(255),
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(slug, (IF(is_deleted = 0, 1, NULL)))
);

CREATE TABLE product_categories (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    category_id BIGINT NOT NULL
);

CREATE TABLE product_options (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL
);

CREATE TABLE product_option_values (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_option_id BIGINT NOT NULL,
    label VARCHAR(255) NOT NULL,
    value VARCHAR(255) NOT NULL
);

CREATE TABLE product_variants (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_id BIGINT NOT NULL,
    sku VARCHAR(255) NOT NULL,
    image VARCHAR(500),
    price_old DECIMAL(15, 2) DEFAULT 0.00,
    price_new DECIMAL(15, 2) DEFAULT 0.00,
    stock INT DEFAULT 0,
    status TINYINT(1) DEFAULT 1,
    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,
    UNIQUE(sku, (IF(is_deleted = 0, 1, NULL)))
);

CREATE TABLE product_variant_option_values (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    product_variant_id BIGINT NOT NULL,
    product_option_value_id BIGINT NOT NULL
);

ALTER TABLE product_categories
    ADD CONSTRAINT fk_product_categories__products
    FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE product_categories
    ADD CONSTRAINT fk_product_categories__categories
    FOREIGN KEY (category_id) REFERENCES categories(id);

ALTER TABLE product_options
    ADD CONSTRAINT fk_product_options__products
    FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE product_option_values
    ADD CONSTRAINT fk_product_option_values__product_options
    FOREIGN KEY (product_option_id) REFERENCES product_options(id);

ALTER TABLE product_variants
    ADD CONSTRAINT fk_product_variants__products
    FOREIGN KEY (product_id) REFERENCES products(id);

ALTER TABLE product_variant_option_values
    ADD CONSTRAINT fk_product_variant_option_values__product_variants
    FOREIGN KEY (product_variant_id) REFERENCES product_variants(id);

ALTER TABLE product_variant_option_values
    ADD CONSTRAINT fk_product_variant_option_values__product_option_values
    FOREIGN KEY (product_option_value_id) REFERENCES product_option_values(id);
