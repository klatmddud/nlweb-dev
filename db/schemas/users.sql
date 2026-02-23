create table if not exists users (
    id uuid not null primary key,
    username varchar(100) not null,
    password varchar(255) not null,
    full_name varchar(100) not null,
    email varchar(100) not null unique check (email ~ '^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$'),
    batch integer not null check (batch >= 0),
    session varchar(10) not null check (session in ('VOCAL', 'GUITAR', 'BASS', 'DRUM', 'KEYBOARD', 'NONE')),
    is_vocal_allowed boolean default false,
    is_admin boolean default false,
    created_at timestamp with time zone default current_timestamp,
    updated_at timestamp with time zone default current_timestamp,
    version integer not null default 1
);

create index idx_users_username on users(username);
create index idx_users_email on users(email);
create index idx_users_batch_session on users(batch, session);
create index idx_users_is_vocalable on users(is_vocal_allowed);
create index idx_users_is_admin on users(is_admin);