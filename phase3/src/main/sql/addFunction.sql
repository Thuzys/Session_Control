drop function if exists get_sessions_by(int, varchar, varchar, int, varchar, varchar, int, int);
drop function if exists compare_name(varchar, varchar);
drop function if exists add_owner_to_session();

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
    return name1 ILIKE '%' || name2 || '%';
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
            (gameName is null or s.game_name = gameName)
        group by s.sid, s.capacity, s.game_name, s.date, s.gid, s.owner
        having (state is null) or
            (state = 'OPEN' and count(u.pid) < s.capacity and now() <= s.date) or
            (state = 'CLOSE' and (count(u.pid) = s.capacity or now() > s.date))
        limit l offset o;
end
$$ language plpgsql;

