CREATE TABLE roles (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       name VARCHAR(50) NOT NULL UNIQUE,
                       description VARCHAR(255),
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE users (
                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                       first_name VARCHAR(100) NOT NULL,
                       last_name VARCHAR(100) NOT NULL,
                       email VARCHAR(255) NOT NULL UNIQUE,
                       password VARCHAR(255) NOT NULL,
                       account_status VARCHAR(50) NOT NULL,
                       email_verified BOOLEAN NOT NULL DEFAULT FALSE,
                       role_id BIGINT NOT NULL,
                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
                       updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                       CONSTRAINT fk_users_role
                           FOREIGN KEY (role_id) REFERENCES roles(id)
);

CREATE TABLE refresh_tokens (
                                id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                token VARCHAR(700) NOT NULL UNIQUE,
                                user_id BIGINT NOT NULL,
                                expiry_date TIMESTAMP NOT NULL,
                                revoked BOOLEAN NOT NULL DEFAULT FALSE,
                                created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                CONSTRAINT fk_refresh_token_user
                                    FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE audit_logs (
                            id BIGINT AUTO_INCREMENT PRIMARY KEY,
                            user_id BIGINT NULL,
                            action VARCHAR(100) NOT NULL,
                            entity_name VARCHAR(100),
                            entity_id BIGINT,
                            description TEXT,
                            ip_address VARCHAR(50),
                            created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_audit_log_user
                                FOREIGN KEY (user_id) REFERENCES users(id)
);

CREATE TABLE password_reset_tokens (
                                       id BIGINT AUTO_INCREMENT PRIMARY KEY,
                                       token VARCHAR(500) NOT NULL UNIQUE,
                                       user_id BIGINT NOT NULL,
                                       expiry_date TIMESTAMP NOT NULL,
                                       used BOOLEAN NOT NULL DEFAULT FALSE,
                                       created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                                       CONSTRAINT fk_password_reset_user
                                           FOREIGN KEY (user_id) REFERENCES users(id)
);

-- Default roles
INSERT INTO roles (name, description) VALUES
                                          ('ROLE_ADMIN', 'System Administrator'),
                                          ('ROLE_DOCTOR', 'Doctor'),
                                          ('ROLE_RECEPTIONIST', 'Receptionist'),
                                          ('ROLE_PATIENT', 'Patient');