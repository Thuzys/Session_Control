SELECT s1.sid, capacity, gid, date FROM
            (SELECT sid, capacity, gid, date FROM
            SESSION WHERE gid = ? OR date = ?) AS s1
            JOIN PLAYER_SESSION ON PLAYER_SESSION.sid = s1.sid
AND PLAYER_SESSION.pid = ?
GROUP BY s1.sid, capacity, gid, date
HAVING capacity = count(pid)  OFFSET ? LIMIT ?;


SELECT s.sid, s.capacity, s.gid, s.date
FROM session s
         JOIN player_session ps ON ps.sid = s.sid
WHERE (s.gid = ?)
  OR (s.date = ?)
  OR (ps.pid = ?)
GROUP BY s.sid, s.capacity, s.gid, s.date
HAVING (:state IS NULL) OR
    (:state = 'OPEN' AND s.capacity > count(ps.pid)) OR
    (:state = 'CLOSE' AND s.capacity = count(ps.pid))
OFFSET ? LIMIT ?;