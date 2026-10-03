CREATE TYPE brand_status AS ENUM (
    'ACTIVE',
    'INACTIVE'
);

CREATE TABLE brands (
    id           UUID PRIMARY KEY DEFAULT uuidv7(),
    name         VARCHAR(255) UNIQUE NOT NULL,
    slug         VARCHAR(255) UNIQUE NOT NULL,
    avatar       VARCHAR(500),
    description  TEXT,
    status       brand_status NOT NULL DEFAULT 'ACTIVE',
    created_at   TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by   UUID,
    updated_at   TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by   UUID,
    deleted_at   TIMESTAMPTZ,
    deleted_by   UUID,
    is_deleted   BOOLEAN NOT NULL DEFAULT FALSE
);
