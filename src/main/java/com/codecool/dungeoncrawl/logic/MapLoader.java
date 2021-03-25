package com.codecool.dungeoncrawl.logic;

import com.codecool.dungeoncrawl.logic.actors.*;
import com.codecool.dungeoncrawl.logic.item.Boat;
import com.codecool.dungeoncrawl.logic.item.Potion;
import com.codecool.dungeoncrawl.logic.item.Sword;
import com.codecool.dungeoncrawl.logic.item.Key;

import java.io.InputStream;
import java.util.Scanner;

public class MapLoader {
    public static GameMap loadMap(String mapTxt) {
        InputStream is = MapLoader.class.getResourceAsStream(mapTxt);
        Scanner scanner = new Scanner(is);
        int width = scanner.nextInt();
        int height = scanner.nextInt();

        scanner.nextLine(); // empty line

        GameMap map = new GameMap(width, height, CellType.EMPTY);
        for (int y = 0; y < height; y++) {
            String line = scanner.nextLine();
            for (int x = 0; x < width; x++) {
                if (x < line.length()) {
                    Cell cell = map.getCell(x, y);
                    switch (line.charAt(x)) {
                        case ' ':
                            cell.setType(CellType.EMPTY);
                            break;
                        case '#':
                            cell.setType(CellType.WALL);
                            break;
                        case '.':
                            cell.setType(CellType.FLOOR);
                            break;
                        case 's':
                            cell.setType(CellType.FLOOR);
                            map.addMonster(new Skeleton(cell));
                            break;
                        case '@':
                            cell.setType(CellType.FLOOR);
                            map.setPlayer(new Player(cell));
                            break;
                        case 'X':
                            cell.setType(CellType.FLOOR);
                            new Sword(cell);
                            break;
                        case 'O':
                            cell.setType(CellType.FLOOR);
                            new Potion(cell);
                            break;
                        case 'g':
                            cell.setType(CellType.FLOOR);
                            map.addMonster(new Ghost(cell));
                            break;
                        case 'p':
                            cell.setType(CellType.FLOOR);
                            map.addMonster(new Spider(cell));
                            break;
                        case 'B':
                            cell.setType(CellType.FLOOR);
                            map.addMonster(new Boss(cell));
                            break;
                        case 'k':
                            cell.setType(CellType.FLOOR);
                            new Key(cell);
                            break;
                        case 'd':
                            cell.setType(CellType.CLOSED_DOOR);
                            break;
                        case 'b':
                            cell.setType(CellType.STAIRS_DOWN);
                            break;
                        case 'T':
                            cell.setType(CellType.STAIRS_UP);
                            break;
                        case 'f':
                            cell.setType(CellType.LAVA);
                            break;
                        case 'w':
                            cell.setType(CellType.WATER);
                            break;
                        case 'h':
                            cell.setType(CellType.BRIDGE);
                            break;
                        default:
                            throw new RuntimeException("Unrecognized character: '" + line.charAt(x) + "'");
                    }
                }
            }
        }
        return map;
    }

}
