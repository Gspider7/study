-- h2数据库大小写不敏感，官网地址：http://www.h2database.com/html/main.html
-- 用户信息表
create table if not exists sys_user
(
    id            identity        primary key not null,
    username      varchar(64)     not null,
    password      varchar(64)     not null
);
create unique index if not exists user_index_username on sys_user (username);

-- 角色信息表
create table if not exists sys_role
(
    id            identity        primary key not null,
    name          varchar(64)     not null
);
create unique index if not exists role_index_name on sys_role (name);

-- 用户角色表
create table if not exists sys_user_role
(
    user_id       bigint          not null,
    role_id       bigint          not null
);
create unique index if not exists user_role_index_id on sys_user_role (user_id, role_id);

-- 权限信息表
create table if not exists sys_authority
(
    id            identity        primary key not null,
    name          varchar(64)     not null
);
create unique index if not exists authority_index_name on sys_authority (name);

-- 用户权限表
create table if not exists sys_user_authority
(
    user_id       bigint          not null,
    authority_id  bigint          not null
);
create unique index if not exists user_authority_index_id on sys_user_authority (user_id, authority_id);