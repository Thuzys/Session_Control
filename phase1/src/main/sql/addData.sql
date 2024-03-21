insert into courses(name) values ('LEIC');
insert into students(course, number, name) values (1, 12345, 'Alice');
insert into students(course, number, name) select cid as course, 12346 as number, 'Bob' as name from courses where name = 'LEIC';
insert into test(name) values ('LEIC');

insert into game(gid, name, developer) values (1, 'test', 'test');
insert into player(pid, name, email) values (1, 'test', 'test@exemplo.com');
insert into player(pid, name, email) values (2, 'test2', 'test2@exemplo.com');
