create table if not exists ensembles (
    id uuid not null primary key,
    program_id uuid not null,
    artist varchar(255) not null,
    title varchar(255) not null,
    vocal varchar(255),
    lead_guitar varchar(255),
    rhythm_guitar varchar(255),
    bass varchar(255),
    drum varchar(255),
    piano varchar(255),
    synth varchar(255),
    etc varchar(255),
    day_of_week varchar(10) check (day_of_week in ('MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY', 'SUNDAY')),
    start_time time,
    end_time time,
    created_at timestamp with time zone not null default current_timestamp,
    updated_at timestamp with time zone not null default current_timestamp,
    version integer not null default 1,

    constraint fk_ensembles_program
        foreign key (program_id) references programs(id)
        on delete cascade
);

create index idx_ensembles_program_id on ensembles(program_id);
create index idx_ensembles_artist on ensembles(artist);
create index idx_ensembles_title on ensembles(title);
create index idx_ensembles_day_of_week on ensembles(day_of_week);
