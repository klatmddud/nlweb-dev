create table if not exists timeslots (
    id uuid not null primary key,
    ensemble_id uuid,
    created_by uuid,
    description varchar(255),
    start_at timestamp with time zone not null,
    end_at timestamp with time zone not null,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,
    version integer not null default 1,

    constraint fk_timeslots_ensemble
        foreign key (ensemble_id) references ensembles(id)
        on delete cascade,
    constraint fk_timeslots_created_by
        foreign key (created_by) references users(id)
        on delete set null
);

create index idx_timeslots_ensemble_id on timeslots(ensemble_id);
create index idx_timeslots_created_by on timeslots(created_by);
create index idx_timeslots_start_at on timeslots(start_at);
create index idx_timeslots_end_at on timeslots(end_at);
