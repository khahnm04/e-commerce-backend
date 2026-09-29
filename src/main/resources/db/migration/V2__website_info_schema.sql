CREATE TABLE website_infos (
    id            UUID PRIMARY KEY DEFAULT uuidv7(),
    name          VARCHAR(255) NOT NULL,
    phone_number  VARCHAR(20) NOT NULL,
    email         VARCHAR(255) NOT NULL,
    address       VARCHAR(500) NOT NULL,
    logo          VARCHAR(500) NOT NULL,
    favicon       VARCHAR(500) NOT NULL,
    created_at    TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by    UUID,
    updated_at    TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by    UUID,
    deleted_at    TIMESTAMPTZ,
    deleted_by    UUID,
    is_deleted    BOOLEAN NOT NULL DEFAULT FALSE
);
