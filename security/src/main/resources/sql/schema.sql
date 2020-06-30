-- h2数据库大小写不敏感，官网地址：http://www.h2database.com/html/main.html
-- 用户信息表
CREATE TABLE IF NOT EXISTS SYS_USER
(
    ID            IDENTITY        PRIMARY KEY NOT NULL,       -- 用户id
    USERNAME      VARCHAR(64),
    PASSWORD      VARCHAR(64)
);
CREATE UNIQUE INDEX IF NOT EXISTS USER_INDEX_USERNAME ON SYS_USER (USERNAME);

-- 角色信息表
