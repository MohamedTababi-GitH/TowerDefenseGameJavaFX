package org.example.javatowerdefensegame;

import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

public class Tile extends Rectangle {
    private TileType type;

    public Tile(double size, TileType type) {
        super(size, size);
        this.type = type;
        setFill(getColorForType(type));
    }

    private Color getColorForType(TileType type) {
        switch (type) {
            case GRASS: return Color.GREEN;
            case SLOT: return Color.BROWN;
            case PATH: return Color.GREY;
            default: return Color.TRANSPARENT;
        }
    }

    public TileType getType() {
        return type;
    }
}
