package com.codecool.dungeoncrawl;

import com.codecool.dungeoncrawl.logic.Drawable;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

import java.util.HashMap;
import java.util.Map;

public class Tiles {
    public static int TILE_WIDTH = 32;

    private static Image tileset = new Image("/tiles.png", 543 * 2, 543 * 2, true, false);
    private static Map<String, Tile> tileMap = new HashMap<>();
    public static class Tile {
        public final int x, y, w, h;
        Tile(int i, int j) {
            x = i * (TILE_WIDTH + 2);
            y = j * (TILE_WIDTH + 2);
            w = TILE_WIDTH;
            h = TILE_WIDTH;
        }
    }

    static {
        // board
        tileMap.put("empty", new Tile(0, 0));
        tileMap.put("wall", new Tile(0, 13));
        tileMap.put("floor", new Tile(2, 0));
        tileMap.put("water", new Tile(8, 5));
        tileMap.put("lava", new Tile(15, 10));
        tileMap.put("bridge", new Tile(6, 5));
        // enemies
        tileMap.put("player", new Tile(22, 8));
        tileMap.put("skeleton", new Tile(29, 6));
        tileMap.put("spider", new Tile(29, 5));
        tileMap.put("ghost", new Tile(24, 7));
        tileMap.put("boss", new Tile(22, 9));
        // items
        tileMap.put("sword", new Tile(1, 30));
        tileMap.put("potion", new Tile(18, 25));
        tileMap.put("key", new Tile(16, 23));
        tileMap.put("boat", new Tile(11, 19));
        //interactive
        tileMap.put("closed door", new Tile(4, 9));
        tileMap.put("open door", new Tile(6, 9));
        tileMap.put("stairsUp", new Tile(2, 6));
        tileMap.put("stairsDown", new Tile(3, 6));
    }

    public static void drawTile(GraphicsContext context, Drawable d, int x, int y) {
        Tile tile = tileMap.get(d.getTileName());
        context.drawImage(tileset, tile.x, tile.y, tile.w, tile.h,
                x * TILE_WIDTH, y * TILE_WIDTH, TILE_WIDTH, TILE_WIDTH);
    }
}
