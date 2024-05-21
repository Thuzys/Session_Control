-- Insert values into GENRES table
INSERT INTO GENRE (name)
VALUES
    ('RPG'), ('Adventure'),
    ('Shooter'), ('Turn-based Strategy'),
    ('Action'), ('Platformer'),
    ('Puzzle'), ('Simulation'),
    ('Sports'), ('Racing'),
    ('Fighting'), ('Sandbox'),
    ('Stealth'), ('Survival'),
    ('Horror'), ('Open world'),
    ('Tactical RPG'), ('Real-time Strategy'),
    ('MMO'), ('Roguelike'),
    ('MOBA'), ('Tower Defense'),
    ('Battle Royale');

-- Insert values into GAME table
INSERT INTO GAME (name, developer)
VALUES ('Game 1', 'Developer A'), ('Game 2', 'Developer B'), ('Game 3', 'Developer C'), ('Game 4', 'Developer D'), ('Game 5', 'Developer E');

-- Insert values into GAME_GENRES table
INSERT INTO GAME_GENRE (gid, genre)
VALUES (1, 'Action'), (1, 'Adventure'), (2, 'RPG'), (3, 'Simulation'), (4, 'Turn-based Strategy'), (5, 'Sports');

-- Insert values into PLAYER table
INSERT INTO PLAYER (name, email, token, username, password)
VALUES ('Player 1', 'player1@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 1', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 2', 'player2@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 2', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 3', 'player3@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 3', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 4', 'player4@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 4', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 5', 'player5@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 5', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 5', 'player6@exemple.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 6', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v');

-- Insert values into SESSION table
INSERT INTO SESSION (capacity, gid, date, owner)
VALUES
    (10, 1, '2024-03-11', 2),
    (8, 2, '2024-03-11', 1),
    (12, 3, '2024-03-13', 1),
    (15, 4, '2024-03-14', 1),
    (10, 5, '2024-03-15', 1),
    (8, 1, '2024-03-16', 1),
    (12, 2, '2024-03-17', 1),
    (15, 3, '2024-03-18', 1),
    (10, 4, '2024-03-19', 1),
    (8, 5, '2024-03-20', 1),
    (12, 1, '2024-03-21', 1),
    (15, 2, '2024-03-22', 1),
    (10, 3, '2024-03-23', 1),
    (8, 4, '2024-03-24', 1),
    (12, 5, '2024-03-25', 1),
    (15, 1, '2024-03-26', 1),
    (10, 2, '2024-03-27', 1),
    (8, 3, '2024-03-28', 1),
    (12, 4, '2024-03-29', 1),
    (10, 1, '2024-03-11', 1),
    (10, 1, '2024-03-12', 1),
    (10, 1, '2024-03-13', 1),
    (10, 1, '2024-03-14', 1),
    (10, 1, '2024-03-15', 1),
    (10, 1, '2024-03-16', 1),
    (10, 1, '2024-03-17', 1),
    (10, 1, '2024-03-18', 1),
    (10, 1, '2024-03-19', 1),
    (10, 1, '2024-03-20', 1),
    (10, 1, '2024-03-21', 1),
    (10, 1, '2024-03-22', 1),
    (10, 1, '2024-03-23', 1),
    (10, 1, '2024-03-24', 1),
    (10, 1, '2024-03-25', 1),
    (10, 1, '2024-03-26', 1),
    (10, 1, '2024-03-27', 1),
    (10, 1, '2024-03-28', 1),
    (10, 1, '2024-03-29', 1),
    (10, 1, '2024-03-30', 1),
    (15, 5, '2024-03-30', 1);

-- Insert values into PLAYER_SESSION table
INSERT INTO PLAYER_SESSION (pid, sid)
VALUES
    (1, 1),
    (2, 1),
    (3, 1),
    (4, 1),
    (5, 1),
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

TRUNCATE TABLE PLAYER_SESSION;