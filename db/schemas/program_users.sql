create table if not exists program_users (
    id uuid not null primary key,
    user_id uuid not null,
    program_id uuid not null unique,
    created_at timestamp with time zone not null default current_timestamp,

    constraint fk_program_users_user
        foreign key (user_id) references users(id)
        on delete cascade,

    constraint fk_program_users_program
        foreign key (program_id) references programs(id)
        on delete cascade
);

create index idx_program_users_user_id on program_users(user_id);
create index idx_program_users_program_id on program_users(program_id);
