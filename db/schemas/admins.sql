create table if not exists admins (
    id uuid not null primary key,
    user_id uuid not null unique,
    role varchar(100) not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,
    version bigint not null default 1,

    constraint fk_admins_user
        foreign key (user_id) references users(id)
        on delete cascade
);

create index idx_admins_user_id on admins(user_id);
create index idx_admins_role on admins(role);
