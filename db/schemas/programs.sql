create table if not exists programs (
    id uuid not null primary key,
    title varchar(255) not null,
    program_apply_start_at timestamp with time zone,
    program_apply_end_at timestamp with time zone,
    ensemble_apply_start_at timestamp with time zone,
    ensemble_apply_end_at timestamp with time zone,
    session_apply_start_at timestamp with time zone,
    session_apply_end_at timestamp with time zone,
    timeslot_apply_start_at timestamp with time zone,
    timeslot_apply_end_at timestamp with time zone,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,
    version integer not null default 0
);

create index idx_programs_title on programs(title);
