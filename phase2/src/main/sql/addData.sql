-- Insert values into GENRES table
INSERT INTO GENRE (name)
VALUES ('Action'), ('Adventure'), ('RPG'), ('Simulation'), ('Turn-based Strategy'), ('Sports'), ('Puzzle');

-- Insert values into GAME table
INSERT INTO GAME (name, developer)
VALUES ('Game 1', 'Developer A'), ('Game 2', 'Developer B'), ('Game 3', 'Developer C'), ('Game 4', 'Developer D'), ('Game 5', 'Developer E');

-- Insert values into GAME_GENRES table
INSERT INTO GAME_GENRE (gid, genre)
VALUES (1, 'Action'), (1, 'Adventure'), (2, 'RPG'), (3, 'Simulation'), (4, 'Turn-based Strategy'), (5, 'Sports');

-- Insert values into PLAYER table
INSERT INTO PLAYER (name, email, token)
VALUES ('Player 1', 'player1@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2'),
       ('Player 2', 'player2@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2'),
       ('Player 3', 'player3@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2'),
       ('Player 4', 'player4@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2'),
       ('Player 5', 'player5@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2');

-- Insert values into SESSION table
INSERT INTO SESSION (capacity, gid, date)
VALUES
    (10, 1, '2024-11-01'),
    (8, 2, '2024-11-02'),
    (12, 3, '2024-11-03'),
    (15, 4, '2024-11-04'),
    (10, 5, '2024-11-05'),
    (8, 1, '2024-11-06'),
    (12, 2, '2024-11-07'),
    (15, 3, '2024-11-08'),
    (10, 4, '2024-11-09'),
    (8, 5, '2024-11-10'),
    (12, 1, '2024-11-11'),
    (15, 2, '2024-11-12'),
    (10, 3, '2024-11-13'),
    (8, 4, '2024-11-14'),
    (12, 5, '2024-11-15'),
    (15, 1, '2024-11-16'),
    (10, 2, '2024-11-17'),
    (8, 3, '2024-11-18'),
    (12, 4, '2024-11-19'),
    (10, 1, '2024-11-20'),
    (10, 1, '2024-11-21'),
    (10, 1, '2024-11-22'),
    (10, 1, '2024-11-23'),
    (10, 1, '2024-11-24'),
    (10, 1, '2024-11-25'),
    (10, 1, '2024-11-26'),
    (10, 1, '2024-11-27'),
    (10, 1, '2024-11-28'),
    (10, 1, '2024-11-29'),
    (10, 1, '2024-11-30'),
    (15, 5, '2024-11-30');

-- Insert values into PLAYER_SESSION table
INSERT INTO PLAYER_SESSION (pid, sid)
VALUES
    (1, 1),
    (1, 2),
    (1, 3),
    (2, 2),
    (3, 3),
    (4, 4),
    (5, 5),
    (1, 6),
    (2, 7),
    (3, 8),
    (4, 9),
    (5, 10),
    (1, 11),
    (2, 12),
    (3, 13),
    (4, 14),
    (5, 15),
    (1, 16),
    (2, 17),
    (3, 18),
    (4, 19),
    (5, 20);