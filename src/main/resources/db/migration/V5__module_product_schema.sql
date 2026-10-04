CREATE TYPE product_status AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'DRAFT'
);

CREATE TABLE products (
    id              UUID PRIMARY KEY DEFAULT uuidv7(),
    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(255) UNIQUE NOT NULL,
    price_old       NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_old >= 0),
    price_new       NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_new >= 0),
    discount        INT NOT NULL DEFAULT 0 CHECK (discount BETWEEN 0 AND 100),
    stock           INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    avatar          VARCHAR(500) NOT NULL,
    images          JSONB NOT NULL DEFAULT '[]',
    specifications  JSONB NOT NULL DEFAULT '{}',
    view            INT NOT NULL DEFAULT 0,
    rating_avg      NUMERIC(3, 2) NOT NULL DEFAULT 0,
    rating_count    INT NOT NULL DEFAULT 0,
    description     TEXT,
    content         TEXT,
    status          product_status NOT NULL DEFAULT 'ACTIVE',
    created_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by      UUID,
    updated_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by      UUID,
    deleted_at      TIMESTAMPTZ,
    deleted_by      UUID,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE product_categories (
    id           UUID PRIMARY KEY DEFAULT uuidv7(),
    product_id   UUID NOT NULL,
    category_id  UUID NOT NULL,

    UNIQUE (product_id, category_id),
    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);

CREATE TABLE product_options (
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    product_id  UUID NOT NULL,
    name        VARCHAR(255) NOT NULL,

    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE product_option_values (
    id                 UUID PRIMARY KEY DEFAULT uuidv7(),
    product_option_id  UUID NOT NULL,
    label              VARCHAR(255) NOT NULL,
    value              VARCHAR(255) NOT NULL,

    FOREIGN KEY (product_option_id) REFERENCES product_options(id) ON DELETE CASCADE
);

CREATE TABLE product_variants (
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    product_id  UUID NOT NULL,
    sku         VARCHAR(255) UNIQUE NOT NULL,
    avatar      VARCHAR(500),
    price_old   NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_old >= 0),
    price_new   NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_new >= 0),
    stock       INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    status      SMALLINT DEFAULT 1,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (product_id) REFERENCES products(id) ON DELETE CASCADE
);

CREATE TABLE product_variant_option_values (
    id                       UUID PRIMARY KEY DEFAULT uuidv7(),
    product_variant_id       UUID NOT NULL,
    product_option_value_id  UUID NOT NULL,

    FOREIGN KEY (product_variant_id) REFERENCES product_variants(id) ON DELETE CASCADE,
    FOREIGN KEY (product_option_value_id) REFERENCES product_option_values(id)
);
