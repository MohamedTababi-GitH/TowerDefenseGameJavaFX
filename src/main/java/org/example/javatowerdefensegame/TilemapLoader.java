package org.example.javatowerdefensegame;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class TilemapLoader {

    public static char[][] loadTilemap(String filePath) throws IOException {
        System.out.println(System.getProperty("java.class.path"));

        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        int rows = 0;
        int cols = 0;

        // First pass to determine the number of rows and columns
        while ((line = reader.readLine()) != null) {
            rows++;
            String[] tiles = line.split(",");
            cols = tiles.length; // Set columns to the length of the split array
        }
        reader.close();

        char[][] tilemap = new char[rows][cols];

        // Second pass to read and process the tile data
        reader = new BufferedReader(new FileReader(filePath));
        int row = 0;
        while ((line = reader.readLine()) != null) {
            String[] tiles = line.split(",");
            for (int col = 0; col < tiles.length; col++) {
                tilemap[row][col] = tiles[col].charAt(0); // Assuming each tile is represented by a single character
            }
            row++;
        }
        reader.close();

        return tilemap;
    }
}
