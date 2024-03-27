-- Insert values into GENRES table
INSERT INTO GENRES (name) VALUES ('Action'), ('Adventure'), ('RPG'), ('Simulation');

-- Insert values into GAME table
INSERT INTO GAME (name, developer) VALUES ('Game 1', 'Developer A'), ('Game 2', 'Developer B'), ('Game 3', 'Developer C');

-- Insert values into GAME_GENRES table
INSERT INTO GAME_GENRES (gid, genre) VALUES (1, 'Action'), (1, 'Adventure'), (2, 'RPG'), (3, 'Simulation');

-- Insert values into PLAYER table
INSERT INTO PLAYER (name, email, token) VALUES ('Player 1', 'player1@example.com', 'token1'), ('Player 2', 'player2@example.com', 'token2');

-- Insert values into SESSION table
INSERT INTO SESSION (capacity, gid, date) VALUES (10, 1, '2024-03-11T12:30'), (8, 2, '2024-03-11T12:30'), (12, 3, '2024-03-13T12:30');

-- Insert values into PLAYER_SESSION table
INSERT INTO PLAYER_SESSION (pid, sid) VALUES (1, 1), (2, 2), (1, 3), (2, 3);
