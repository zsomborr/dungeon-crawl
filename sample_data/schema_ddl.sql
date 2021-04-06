DROP TABLE IF EXISTS public.game_state;
CREATE TABLE public.game_state
(
    id        serial  NOT NULL PRIMARY KEY,
    saved_at  timestamp without time zone DEFAULT CURRENT_TIMESTAMP NOT NULL,
    player_id integer NOT NULL
);

DROP TABLE IF EXISTS public.player;
CREATE TABLE public.player
(
    id             serial  NOT NULL PRIMARY KEY,
    player_name    text    NOT NULL,
    hp             integer NOT NULL,
    experience     integer NOT NULL,
    strength       integer NOT NULL,
    on_item        boolean NOT NULL,
    on_stairs_down boolean NOT NULL,
    on_stairs_up   boolean NOT NULL,
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

DROP TABLE IF EXISTS public.map_items;
CREATE TABLE public.map_items
(
    map_id  integer NOT NULL,
    item_id integer NOT NULL,
    x       integer NOT NULL,
    y       integer NOT NULL
);

DROP TABLE IF EXISTS public.map_actors;
CREATE TABLE public.map_actors
(
    map_id   integer NOT NULL,
    actor_id integer NOT NULL,
    x        integer NOT NULL,
    y        integer NOT NULL
);

ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);