CREATE TYPE account_status AS ENUM (
    'ACTIVE',
    'INACTIVE',
    'BANNED',
    'LOCKED'
);

CREATE TYPE gender_type AS ENUM (
    'MALE',
    'FEMALE',
    'OTHER'
);

CREATE TABLE accounts (
    id             UUID PRIMARY KEY DEFAULT uuidv7(),
    email          VARCHAR(255) NOT NULL UNIQUE,
    phone_number   VARCHAR(20) NOT NULL UNIQUE,
    password       VARCHAR(500),
    status         account_status NOT NULL DEFAULT 'ACTIVE',
    last_login_at  TIMESTAMPTZ,
    created_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by     UUID,
    updated_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by     UUID,
    deleted_at     TIMESTAMPTZ,
    deleted_by     UUID,
    is_deleted     BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE roles (
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    code        VARCHAR(50) NOT NULL UNIQUE,
    name        VARCHAR(255) NOT NULL,
    created_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by  UUID,
    updated_at  TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by  UUID,
    deleted_at  TIMESTAMPTZ,
    deleted_by  UUID,
    is_deleted  BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE account_roles (
    id          UUID PRIMARY KEY DEFAULT uuidv7(),
    account_id  UUID NOT NULL,
    role_id     UUID NOT NULL,

    UNIQUE (account_id, role_id),
    FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE,
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE RESTRICT
);

CREATE TABLE permissions (
    id    UUID PRIMARY KEY DEFAULT uuidv7(),
    code  VARCHAR(50) NOT NULL UNIQUE,
    name  VARCHAR(255) NOT NULL
);

CREATE TABLE role_permissions (
    id             UUID PRIMARY KEY DEFAULT uuidv7(),
    role_id        UUID NOT NULL,
    permission_id  UUID NOT NULL,

    UNIQUE (role_id, permission_id),
    FOREIGN KEY (role_id) REFERENCES roles (id) ON DELETE CASCADE,
    FOREIGN KEY (permission_id) REFERENCES permissions (id) ON DELETE RESTRICT
);

CREATE TABLE user_profiles (
    id             UUID PRIMARY KEY DEFAULT uuidv7(),
    account_id     UUID NOT NULL UNIQUE,
    full_name      VARCHAR(255) NOT NULL,
    date_of_birth  DATE,
    avatar         VARCHAR(500),
    gender         gender_type DEFAULT 'OTHER',
    total_point     INT DEFAULT 0,
    used_point      INT DEFAULT 0,
    created_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by     UUID,
    updated_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by     UUID,
    deleted_at     TIMESTAMPTZ,
    deleted_by     UUID,
    is_deleted     BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE TABLE admin_profiles (
    id             UUID PRIMARY KEY DEFAULT uuidv7(),
    account_id     UUID NOT NULL UNIQUE,
    job_title      VARCHAR(255),
    full_name      VARCHAR(255) NOT NULL,
    date_of_birth  DATE,
    avatar         VARCHAR(500),
    gender         gender_type DEFAULT 'OTHER',
    created_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    created_by     UUID,
    updated_at     TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP,
    updated_by     UUID,
    deleted_at     TIMESTAMPTZ,
    deleted_by     UUID,
    is_deleted     BOOLEAN NOT NULL DEFAULT FALSE,

    FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE
);

CREATE TABLE user_providers (
    id           UUID PRIMARY KEY DEFAULT uuidv7(),
    account_id   UUID NOT NULL,
    provider     VARCHAR(50) NOT NULL,
    provider_id  VARCHAR(255) NOT NULL,

    UNIQUE (provider, provider_id),
    FOREIGN KEY (account_id) REFERENCES accounts (id) ON DELETE CASCADE
);
