CREATE TABLE lobby_players (
       lobby_id BIGINT NOT NULL,
       player_id BIGINT NOT NULL,
       PRIMARY KEY (lobby_id, player_id),
       FOREIGN KEY (lobby_id) REFERENCES lobbies(id)
);
