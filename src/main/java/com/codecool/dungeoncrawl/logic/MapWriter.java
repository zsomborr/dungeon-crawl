package com.codecool.dungeoncrawl.logic;

public class MapWriter {

    public static String getMapTxt(GameMap map) {

        int width = map.getWidth();
        int height = map.getHeight();

        StringBuilder mapTxt = new StringBuilder();
        mapTxt.append(width)
                .append(" ")
                .append(height)
                .append("\n");
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Cell cell = map.getCell(x, y);
                switch (cell.getType()) {
                    case EMPTY:
                        mapTxt.append(' ');
                        break;
                    case WALL:
                        mapTxt.append('#');
                        break;
                    case CLOSED_DOOR:
                        mapTxt.append('d');
                        break;
                    case OPEN_DOOR:
                        mapTxt.append('D');
                        break;
                    case STAIRS_DOWN:
                        mapTxt.append('b');
                        break;
                    case BRIDGE:
                        mapTxt.append('h');
                        break;
                    case WATER:
                        mapTxt.append('w');
                        break;
                    case LAVA:
                        mapTxt.append('f');
                        break;
                    case STAIRS_UP:
                        mapTxt.append('T');
                        break;
                    case FLOOR:
                        if (cell.getItem() != null) {
                            switch (cell.getItem().getTileName()) {
                                case "sword":
                                    mapTxt.append('X');
                                    break;
                                case "potion":
                                    mapTxt.append('O');
                                    break;
                                case "boat":
                                    mapTxt.append('t');
                                    break;
                                case "key":
                                    mapTxt.append('k');
                            }
                        } else if (cell.getActor() != null) {
                            switch (cell.getActor().getTileName()) {
                                case "skeleton":
                                    mapTxt.append('s');
                                    break;
                                case "spider":
                                    mapTxt.append('p');
                                    break;
                                case "ghost":
                                    mapTxt.append('g');
                                    break;
                                case "boss":
                                    mapTxt.append('B');
                                    break;
                                default:
                                    mapTxt.append('.');
                            }
                        } else {
                            mapTxt.append('.');
                        }
                }
            }
            mapTxt.append('\n');
        }
        return mapTxt.toString();
    }

}

