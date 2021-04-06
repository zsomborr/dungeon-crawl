ALTER TABLE IF EXISTS ONLY public.game_state DROP CONSTRAINT IF EXISTS fk_player_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.inventory DROP CONSTRAINT IF EXISTS fk_player_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.inventory DROP CONSTRAINT IF EXISTS fk_item_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.maps DROP CONSTRAINT IF EXISTS fk_game_state_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.maps DROP CONSTRAINT IF EXISTS fk_map_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.map_items DROP CONSTRAINT IF EXISTS fk_map_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.map_items DROP CONSTRAINT IF EXISTS fk_item_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.map_actors DROP CONSTRAINT IF EXISTS fk_map_id CASCADE;
ALTER TABLE IF EXISTS ONLY public.map_actors DROP CONSTRAINT IF EXISTS fk_actor_id CASCADE;

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

DROP TABLE IF EXISTS public.inventory;
CREATE TABLE public.inventory
(
    player_id  integer NOT NULL,
    item_id    integer NOT NULL,
    item_count integer NOT NULL
);

DROP TABLE IF EXISTS public.actor;
CREATE TABLE actor
(
    id   serial NOT NULL PRIMARY KEY,
    name text   NOT NULL
);

DROP TABLE IF EXISTS public.map;
CREATE TABLE public.map
(
    id             serial  NOT NULL PRIMARY KEY,
    is_current_map boolean NOT NULL
);

DROP TABLE IF EXISTS public.item;
CREATE TABLE item
(
    id   serial NOT NULL PRIMARY KEY,
    name text   NOT NULL
);


ALTER TABLE ONLY public.game_state
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT fk_player_id FOREIGN KEY (player_id) REFERENCES public.player(id);

ALTER TABLE ONLY public.inventory
    ADD CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES public.item(id);

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT fk_game_state_id FOREIGN KEY (game_state_id) REFERENCES public.game_state(id);

ALTER TABLE ONLY public.maps
    ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.map(id);

ALTER TABLE ONLY public.map_items
    ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.map(id);

ALTER TABLE ONLY public.map_items
    ADD CONSTRAINT fk_item_id FOREIGN KEY (item_id) REFERENCES public.item(id);

ALTER TABLE ONLY public.map_actors
    ADD CONSTRAINT fk_map_id FOREIGN KEY (map_id) REFERENCES public.map(id);

ALTER TABLE ONLY public.map_actors
    ADD CONSTRAINT fk_actor_id FOREIGN KEY (actor_id) REFERENCES public.actor(id);