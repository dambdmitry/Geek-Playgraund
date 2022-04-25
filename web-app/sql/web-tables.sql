CREATE TABLE IF NOT EXISTS game_interface
(
    id          SERIAL PRIMARY KEY,
    name        VARCHAR(100) NOT NULL,
    description TEXT,
    rules       TEXT
);

CREATE TABLE account
(
    id       SERIAL PRIMARY KEY,
    username VARCHAR(100) UNIQUE NOT NULL,
    password VARCHAR(100)        NOT NULL,
    role     VARCHAR(50)         NOT NULL
);

CREATE TABLE IF NOT EXISTS program_template
(
    id       SERIAL PRIMARY KEY,
    language VARCHAR(50) NOT NULL,
    template TEXT        NOT NULL,
    game_id  INT         NOT NULL,
    FOREIGN KEY (game_id)
        REFERENCES game_interface (id)
);

CREATE TABLE IF NOT EXISTS tournament
(
    id            SERIAL PRIMARY KEY,
    name          VARCHAR(50) NOT NULL,
    players_count INT         NOT NULL,
    game_id       INT         NOT NULL,
    owner_id      INT         NOT NULL,
    status        VARCHAR(10) NOT NULL,
    secret_key    VARCHAR(10),
    FOREIGN KEY (game_id)
        REFERENCES game_interface (id),
    FOREIGN KEY (owner_id)
        REFERENCES account (id)
);

CREATE TABLE IF NOT EXISTS player
(
    id         SERIAL PRIMARY KEY,
    points     INT NOT NULL,
    account_id INT NOT NULL,
    FOREIGN KEY (account_id)
        REFERENCES account (id)
);

CREATE TABLE IF NOT EXISTS players_tournaments_relation
(
    id            SERIAL PRIMARY KEY,
    player_id     INT NOT NULL,
    tournament_id INT NOT NULL,
    points        INT NOT NULL,
    FOREIGN KEY (player_id)
        REFERENCES player (id),
    FOREIGN KEY (tournament_id)
        REFERENCES tournament (id)
);

CREATE TABLE IF NOT EXISTS organizer_request
(
    id            SERIAL PRIMARY KEY,
    justification TEXT NULL,
    account_id    INT  NOT NULL,
    FOREIGN KEY (account_id)
        REFERENCES account (id)
);