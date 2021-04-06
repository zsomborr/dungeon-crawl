ALTER TABLE IF EXISTS ONLY public.game_state
    DROP CONSTRAINT IF EXISTS fk_player_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.maps
    DROP CONSTRAINT IF EXISTS fk_game_state_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.maps
    DROP CONSTRAINT IF EXISTS fk_map_id CASCADE;

DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state
(
    id        serial                                                NOT NULL PRIMARY KEY,
    saved_at  timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer                                               NOT NULL
);

DROP TABLE IF EXISTS public.player;
CREATE TABLE public.player
(
    id             serial  NOT NULL PRIMARY KEY,
    player_name    text    NOT NULL,
    hp             integer NOT NULL,
    experience     integer NOT NULL,
    strength       integer NOT NULL,
    poison_count   integer NOT NULL,
    x              integer NOT NULL,
    y              integer NOT NULL
);

DROP TABLE IF EXISTS public.maps;
CREATE TABLE public.maps
(
    game_state_id integer NOT NULL,
    map_id        integer NOT NULL
);

DROP TABLE IF EXISTS public.inventory;
CREATE TABLE public.inventory
(
    player_id  integer NOT NULL,
    item_id    integer NOT NULL,
    item_count integer NOT NULL
);

DROP TABLE IF EXISTS public.map;
CREATE TABLE public.map
(
    id             serial  NOT NULL PRIMARY KEY,
    map_layout     text    NOT NULL,
    is_current_map boolean NOT NULL
);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player (id);

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player (id);

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES public.item(id);

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT fk_game_state_id FOREIGN KEY (game_state_id) REFERENCES public.game_state (id);

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.map (id);