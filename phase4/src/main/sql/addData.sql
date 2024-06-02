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

-- Insert games into the GAME table
INSERT INTO GAME (name, developer)
VALUES
    ('The Legend of Zelda: Breath of the Wild', 'Nintendo'),
    ('The Witcher 3: Wild Hunt', 'CD Projekt Red'),
    ('Minecraft', 'Mojang Studios'),
    ('Fortnite', 'Epic Games'),
    ('Dark Souls III', 'FromSoftware'),
    ('Overwatch', 'Blizzard Entertainment'),
    ('Stardew Valley', 'ConcernedApe'),
    ('Civilization VI', 'Firaxis Games'),
    ('Red Dead Redemption 2', 'Rockstar Games'),
    ('Animal Crossing: New Horizons', 'Nintendo');

-- For 'The Legend of Zelda: Breath of the Wild'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='The Legend of Zelda: Breath of the Wild' AND GAME.developer='Nintendo' AND GENRE.name IN ('RPG', 'Action', 'Adventure', 'Open world');

-- For 'The Witcher 3: Wild Hunt'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='The Witcher 3: Wild Hunt' AND GAME.developer='CD Projekt Red' AND GENRE.name IN ('RPG', 'Action', 'Open world');

-- For 'Minecraft'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Minecraft' AND GAME.developer='Mojang Studios' AND GENRE.name IN ('Sandbox', 'Survival', 'Open world');

-- For 'Fortnite'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Fortnite' AND GAME.developer='Epic Games' AND GENRE.name IN ('Shooter', 'Battle Royale', 'Survival');

-- For 'Dark Souls III'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Dark Souls III' AND GAME.developer='FromSoftware' AND GENRE.name IN ('RPG', 'Action', 'Horror');

-- For 'Overwatch'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Overwatch' AND GAME.developer='Blizzard Entertainment' AND GENRE.name IN ('Shooter', 'Action');

-- For 'Stardew Valley'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Stardew Valley' AND GAME.developer='ConcernedApe' AND GENRE.name IN ('Simulation', 'RPG');

-- For 'Civilization VI'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Civilization VI' AND GAME.developer='Firaxis Games' AND GENRE.name IN ('Turn-based Strategy', 'Simulation');

-- For 'Red Dead Redemption 2'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Red Dead Redemption 2' AND GAME.developer='Rockstar Games' AND GENRE.name IN ('RPG', 'Action', 'Open world');

-- For 'Animal Crossing: New Horizons'
INSERT INTO GAME_GENRE (gid, genre)
SELECT GAME.gid, GENRE.name FROM GAME JOIN GENRE ON GAME.name='Animal Crossing: New Horizons' AND GAME.developer='Nintendo' AND GENRE.name IN ('Simulation', 'Sandbox');

-- Insert values into PLAYER table
INSERT INTO PLAYER (name, email, token, username, password)
VALUES ('Player 1', 'player1@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 1', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 2', 'player2@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 2', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 3', 'player3@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 3', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 4', 'player4@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 4', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 5', 'player5@example.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 5', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v'),
       ('Player 5', 'player6@exemple.com', 'e247758f-02b6-4037-bd85-fc245b84d5f2', 'Player 6', 'OBF:1v2j1uum1xtv1zej1zer1xtn1uvk1v1v');
