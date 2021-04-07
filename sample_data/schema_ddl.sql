ALTER TABLE IF EXISTS ONLY public.game_state
    DROP CONSTRAINT IF EXISTS fk_player_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.maps
    DROP CONSTRAINT IF EXISTS fk_game_state_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.maps
    DROP CONSTRAINT IF EXISTS fk_game_map_id CASCADE;

DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state
(
    id        serial                                                NOT NULL PRIMARY KEY,
    save_name text                                                  NOT NULL,
    saved_at  timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer                                               NOT NULL
);

DROP TABLE IF EXISTS public.player;
CREATE TABLE public.player
(
    id           serial  NOT NULL PRIMARY KEY,
    player_name  text    NOT NULL,
    hp           integer NOT NULL,
    x            integer NOT NULL,
    y            integer NOT NULL,
    experience   integer NOT NULL,
    strength     integer NOT NULL,
    poison_count integer NOT NULL,
    inventory    text[]  NOT NULL
);

DROP TABLE IF EXISTS public.maps;
CREATE TABLE public.maps
(
    game_state_id integer NOT NULL,
    map_id        integer NOT NULL
);

DROP TABLE IF EXISTS public.game_map;
CREATE TABLE public.game_map
(
    id             serial  NOT NULL PRIMARY KEY,
    map_layout     text    NOT NULL,
    is_current_map boolean NOT NULL,
    game_level     integer NOT NULL
);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player (id);

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT fk_game_state_id FOREIGN KEY (game_state_id) REFERENCES public.game_state (id);

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT fk_game_map_id FOREIGN KEY (map_id) REFERENCES public.game_map (id);