-- h2数据库大小写不敏感，官网地址：http://www.h2database.com/html/main.html
-- 用户信息表
CREATE TABLE IF NOT EXISTS SYS_USER
(
    ID            IDENTITY        PRIMARY KEY NOT NULL,
    USERNAME      VARCHAR(64)     NOT NULL,
    PASSWORD      VARCHAR(64)     NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS USER_INDEX_USERNAME ON SYS_USER (USERNAME);

-- 角色信息表
CREATE TABLE IF NOT EXISTS SYS_ROLE
(
    ID            IDENTITY        PRIMARY KEY NOT NULL,
    NAME          VARCHAR(64)     NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS ROLE_INDEX_NAME ON SYS_ROLE (NAME);

-- 用户角色表
CREATE TABLE IF NOT EXISTS SYS_USER_ROLE
(
    USER_ID       BIGINT          NOT NULL,
    ROLE_ID       BIGINT          NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS USER_ROLE_INDEX_ID ON SYS_USER_ROLE (USER_ID, ROLE_ID);

-- 权限信息表
CREATE TABLE IF NOT EXISTS SYS_AUTHORITY
(
    ID            IDENTITY        PRIMARY KEY NOT NULL,
    NAME          VARCHAR(64)     NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS AUTHORITY_INDEX_NAME ON SYS_AUTHORITY (NAME);

-- 用户权限表
CREATE TABLE IF NOT EXISTS SYS_USER_AUTHORITY
(
    USER_ID       BIGINT          NOT NULL,
    AUTHORITY_ID  BIGINT          NOT NULL
);
CREATE UNIQUE INDEX IF NOT EXISTS USER_AUTHORITY_INDEX_ID ON SYS_USER_AUTHORITY (USER_ID, AUTHORITY_ID);