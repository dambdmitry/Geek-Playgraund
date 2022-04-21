CREATE TABLE IF NOT EXISTS solution
(
    id            SERIAL PRIMARY KEY,
    tournament_id INT          NOT NULL,
    player_name   VARCHAR(100) NOT NULL,
    code          TEXT         NOT NULL,
    language      VARCHAR(50),
    FOREIGN KEY (tournament_id) REFERENCES tournament (id)
);

CREATE TABLE IF NOT EXISTS round
(
    id            SERIAL PRIMARY KEY,
    tournament_id INT          NOT NULL,
    host_name     VARCHAR(100),
    guest_name    VARCHAR(100),
    winner        VARCHAR(100),
    failure_id    INT,
    FOREIGN KEY (tournament_id) REFERENCES tournament (id),
    FOREIGN KEY (failure_id) REFERENCES failure(id)
);

CREATE TABLE IF NOT EXISTS round_step
(
    id          SERIAL PRIMARY KEY,
    round_id    INT          NOT NULL,
    action      VARCHAR(200) NOT NULL,
    step_number INT          NOT NULL,
    player_name VARCHAR(100),
    FOREIGN KEY (round_id) REFERENCES round (id)
);

CREATE TABLE IF NOT EXISTS failure
(
    id          SERIAL PRIMARY KEY,
    author_name VARCHAR(100) NOT NULL,
    description TEXT         NOT NULL
);