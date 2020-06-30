-- h2数据库大小写不敏感，官网地址：http://www.h2database.com/html/main.html
-- 部门信息表
CREATE TABLE IF NOT EXISTS DEPT
(
    ID            IDENTITY        PRIMARY KEY NOT NULL,       -- 部门id
    NAME          VARCHAR(64),      -- 名称
    PARENT_ID     BIGINT            -- 上级部门id
);

CREATE UNIQUE INDEX IF NOT EXISTS DEPT_INDEX_NAME ON DEPT (NAME);