create table if not exists amhos (
    id uuid not null primary key,
    user_code varchar(8) not null unique,
    admin_code varchar(8) not null unique,
    is_active boolean not null default true,
    created_at timestamp with time zone default current_timestamp
);

create index idx_amhos_is_active on amhos(is_active)
