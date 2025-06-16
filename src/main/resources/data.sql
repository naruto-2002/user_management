-- RESET DATABASE STATE (chỉ dùng trong môi trường dev/test)
DROP TABLE IF EXISTS user_roles;
DROP TABLE IF EXISTS role_permissions;
DROP TABLE IF EXISTS user;
DROP TABLE IF EXISTS role;
DROP TABLE IF EXISTS permission;
DROP TABLE IF EXISTS invalidated_token;

-- Bảng invalidated_token
CREATE TABLE invalidated_token
(
    id          VARCHAR(255) NOT NULL,
    expiry_time DATETIME(6),
    PRIMARY KEY (id)
);

-- Bảng permission
CREATE TABLE permission
(
    name     VARCHAR(255) NOT NULL,
    endpoint VARCHAR(255),
    method   VARCHAR(255),
    PRIMARY KEY (name)
);

INSERT INTO permission (name, endpoint, method)
VALUES ('CREATE_PERMISSION', '/user-management/permissions', 'POST'),
       ('CREATE_ROLE', '/user-management/roles', 'POST'),
       ('DELETE_PERMISSION', '/user-management/permissions/{userId}', 'DELETE'),
       ('DELETE_ROLE', '/user-management/roles/{name}', 'DELETE'),
       ('DELETE_USER', '/user-management/users/{userId}', 'DELETE'),
       ('READ_MY_INFORMATION', '/user-management/users/my-info', 'GET'),
       ('READ_PERMISSION_LIST', '/user-management/permissions', 'GET'),
       ('READ_ROLE_DETAIL', '/user-management/roles/{name}', 'GET'),
       ('READ_ROLE_LIST', '/user-management/roles', 'GET'),
       ('READ_USER_DETAIL', '/user-management/users/{userId}', 'GET'),
       ('READ_USER_LIST', '/user-management/users', 'GET'),
       ('UPDATE_PERMISSION', '/user-management/permissions/{name}', 'PUT'),
       ('UPDATE_ROLE', '/user-management/roles/{name}', 'PUT'),
       ('UPDATE_USER', '/user-management/users/{userId}', 'PUT');

-- Bảng role
CREATE TABLE role
(
    name VARCHAR(255) NOT NULL,
    PRIMARY KEY (name)
);

INSERT INTO role (name)
VALUES ('ADMIN'),
       ('USER');

-- Bảng role_permissions
CREATE TABLE role_permissions
(
    role_name        VARCHAR(255) NOT NULL,
    permissions_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (role_name, permissions_name),
    FOREIGN KEY (role_name) REFERENCES role (name),
    FOREIGN KEY (permissions_name) REFERENCES permission (name)
);

INSERT INTO role_permissions (role_name, permissions_name)
VALUES ('ADMIN', 'CREATE_PERMISSION'),
       ('ADMIN', 'CREATE_ROLE'),
       ('ADMIN', 'DELETE_PERMISSION'),
       ('ADMIN', 'DELETE_ROLE'),
       ('ADMIN', 'DELETE_USER'),
       ('ADMIN', 'READ_MY_INFORMATION'),
       ('ADMIN', 'READ_PERMISSION_LIST'),
       ('ADMIN', 'READ_ROLE_DETAIL'),
       ('ADMIN', 'READ_ROLE_LIST'),
       ('ADMIN', 'READ_USER_DETAIL'),
       ('ADMIN', 'READ_USER_LIST'),
       ('ADMIN', 'UPDATE_PERMISSION'),
       ('ADMIN', 'UPDATE_ROLE'),
       ('ADMIN', 'UPDATE_USER'),
       ('USER', 'READ_MY_INFORMATION'),
       ('USER', 'READ_USER_DETAIL');

-- Bảng user
CREATE TABLE user
(
    id           VARCHAR(255) NOT NULL,
    deleted_user VARCHAR(255),
    dob          DATE,
    first_name   VARCHAR(255),
    last_name    VARCHAR(255),
    password     VARCHAR(255),
    username     VARCHAR(255),
    PRIMARY KEY (id),
    UNIQUE (username)
);

-- (Có thể thêm dữ liệu user nếu cần)

-- Bảng user_roles
CREATE TABLE user_roles
(
    user_id    VARCHAR(255) NOT NULL,
    roles_name VARCHAR(255) NOT NULL,
    PRIMARY KEY (user_id, roles_name),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (roles_name) REFERENCES role (name)
);

-- (Có thể thêm dữ liệu user_roles nếu cần)
