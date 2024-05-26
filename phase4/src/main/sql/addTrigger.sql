drop trigger if exists add_owner_to_session on session;
drop trigger if exists add_player_to_session on player_session;
drop trigger if exists check_capacity on player_session;

create or replace trigger add_owner_to_session after insert on session
    for each row
execute function add_owner_to_session();

create or replace trigger add_player_to_session before insert on player_session
    for each row
execute function is_session_closed();

create or replace trigger check_capacity before insert on player_session
    for each row
execute function check_capacity();