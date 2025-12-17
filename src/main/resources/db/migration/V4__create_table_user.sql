CREATE TABLE IF NOT EXISTS user (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role_id BIGINT NOT NULL,
    created_at TIMESTAMP NULL,
    updated_at TIMESTAMP NULL,
    deleted_at TIMESTAMP NULL,
    CONSTRAINT fk_user_role FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Seed default user with ROLE_ADMIN 
INSERT INTO user (email, password, role_id, created_at, updated_at)
VALUES (
    'user@email.com',
    'password',
    (SELECT r.id FROM role r WHERE r.name = 'ROLE_ADMIN' LIMIT 1),
    NOW(),
    NOW()
);
