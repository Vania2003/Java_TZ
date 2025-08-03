package pl.e_science.git.ivahan0788;

import java.util.Arrays;

public class AutomatonEngine {
    private int[][] grid;

    public AutomatonEngine(int width, int height) {
        grid = new int[height][width];
    }

    public int[][] getGrid() {
        return grid;
    }

    public void setGrid(int[][] newGrid) {
        this.grid = newGrid;
    }

    public void randomize(int states) {
        for (int y = 0; y < grid.length; y++) {
            for (int x = 0; x < grid[0].length; x++) {
                grid[y][x] = (int) (Math.random() * states);
            }
        }
    }
}