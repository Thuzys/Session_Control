drop function if exists get_sessions_by(int, varchar, varchar, int, varchar, varchar, int, int);
drop function if exists compare_name(varchar, varchar);
drop function if exists add_owner_to_session();
drop function if exists check_capacity(int);
drop function if exists is_session_closed();
drop function if exists check_capacity();
drop function if exists delete_player_from_sessions();

create or replace function check_valid_capacity() returns trigger as $$
begin
    if (new.capacity < (select count(pid) from player_session where sid = new.sid)) then
        raise exception 'Capacity must be greater than the number of players in the session';
    end if;
    return new;
end;
$$ language plpgsql;

create or replace function delete_player_from_sessions() returns trigger as $$
begin
    delete from player_session where player_session.pid = old.pid;
    return old;
end;
$$ language plpgsql;

create or replace function check_capacity() returns trigger as $$
begin
    if (
    select count(*) from player_session where player_session.sid = new.sid) >= (select capacity from session where session.sid = new.sid
    ) then
        raise exception 'Session is full';
    end if;
    return new;
end;
$$ language plpgsql;

create or replace function is_session_closed() returns trigger as $$
begin
    if (select date < now()::date from session where session.sid = new.sid) then
        raise exception 'Session is closed';
    end if;
    return new;
end;
$$ language plpgsql;

create or replace function check_capacity(sid int)
    returns boolean as $$
declare
    capacityNumber int;
begin
    select capacity into capacityNumber from session where session.sid = check_capacity.sid;
    return (select count(*) from player_session where player_session.sid = check_capacity.sid) < capacityNumber;
end;
$$ language plpgsql;

create or replace function add_owner_to_session() returns trigger as $$
begin
    insert into player_session (sid, pid) values (new.sid, new.owner);
    return new;
end
$$ language plpgsql;

create or replace function compare_name(
    name1 varchar(50), name2 varchar(50)
) returns boolean as $$
begin
    return regexp_replace(replace(name1, ' ', ''), '[^a-zA-Z0-9\a]', '', 'g') ILIKE '%' || regexp_replace(replace(name2, ' ', ''), '[^a-zA-Z0-9\a]', '', 'g') || '%';
end
$$ language plpgsql;

create or replace function get_sessions_by(
    pGid int, currDate varchar(10),
    state varchar(10), pPid int,
    pUserName varchar(50), gameName varchar(50),
    l int, o int
) returns table (
                    sid int, capacity int, game_name varchar(50),
                    date date, gid int, owner int, count bigint
                ) as $$
begin
    return query
        select s.sid, s.capacity, s.game_name, s.date, s.gid, s.owner, count(u.pid)
        from (
                 select s.sid, s.owner, s.capacity, g.name as game_name, s.date, s.gid from
                     session s natural join game g
             ) as s
                 natural join (
            select ps.pid, ps.sid from
                player_session ps natural join player p
            where
                (Pusername is null or compare_name(p.username,PuserName)) and
                (Ppid is null or ps.pid = Ppid)
        ) as u
        where
            (Pgid is null or s.gid = Pgid) and
            (currDate is null or s.date = to_date(currDate, 'YYYY-MM-DD')) and
            (gameName is null or compare_name(s.game_name, gameName))
        group by s.sid, s.capacity, s.game_name, s.date, s.gid, s.owner
        having (state is null) or
            (state = 'OPEN' and count(u.pid) < s.capacity and now() <= s.date) or
            (state = 'CLOSE' and (count(u.pid) = s.capacity or now() > s.date))
        limit l offset o;
end
$$ language plpgsql;
