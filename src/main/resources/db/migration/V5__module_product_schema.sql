CREATE TYPE product_status AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'DRAFT'
);

CREATE TABLE product_groups (
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(255) UNIQUE NOT NULL,
    images      JSONB DEFAULT '[]',

    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by  UUID,
    updated_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by  UUID,
    deleted_at  TIMESTAMPTZ,
    deleted_by  UUID,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE product_versions (
    id              UUID PRIMARY KEY DEFAULT uuidv7(),
    group_id        UUID NOT NULL,
    name            VARCHAR(255) NOT NULL,
    slug            VARCHAR(255) UNIQUE NOT NULL,
    version         VARCHAR(50) NOT NULL,
    rating_avg      NUMERIC(3, 2) NOT NULL DEFAULT 0,
    rating_count    INT NOT NULL DEFAULT 0,
    price_old       NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_old >= 0),
    price_new       NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_new >= 0),
    total_stock     INT NOT NULL DEFAULT 0 CHECK (total_stock >= 0),
    sold_count      INT NOT NULL DEFAULT 0 CHECK (sold_count >= 0),
    specifications  JSONB NOT NULL DEFAULT '[]',
    description     TEXT,
    content         TEXT,

    created_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by      UUID,
    updated_at      TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by      UUID,
    deleted_at      TIMESTAMPTZ,
    deleted_by      UUID,
    is_deleted      BOOLEAN NOT NULL DEFAULT FALSE,

    UNIQUE (group_id, version),
    FOREIGN KEY (group_id) REFERENCES product_groups(id) ON DELETE CASCADE
);

CREATE TABLE products (
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    version_id  UUID NOT NULL,
    name        VARCHAR(255) NOT NULL,
    slug        VARCHAR(255) UNIQUE NOT NULL,
    color       VARCHAR(255) NOT NULL,
    sku         VARCHAR(255) UNIQUE,
    is_default  BOOLEAN NOT NULL DEFAULT FALSE,
    price_old   NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_old >= 0),
    price_new   NUMERIC(15, 2) NOT NULL DEFAULT 0 CHECK (price_new >= 0),
    discount    INT NOT NULL DEFAULT 0 CHECK (discount BETWEEN 0 AND 100),
    stock       INT NOT NULL DEFAULT 0 CHECK (stock >= 0),
    avatar      VARCHAR(500) NOT NULL,
    status      product_status NOT NULL DEFAULT 'ACTIVE',

    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by  UUID,
    updated_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by  UUID,
    deleted_at  TIMESTAMPTZ,
    deleted_by  UUID,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (version_id) REFERENCES product_versions(id) ON DELETE CASCADE
);

CREATE TABLE product_group_categories (
    id           UUID PRIMARY KEY DEFAULT uuidv7(),
    group_id     UUID NOT NULL,
    category_id  UUID NOT NULL,

    UNIQUE (group_id, category_id),
    FOREIGN KEY (group_id) REFERENCES product_groups(id) ON DELETE CASCADE,
    FOREIGN KEY (category_id) REFERENCES categories(id) ON DELETE RESTRICT
);
