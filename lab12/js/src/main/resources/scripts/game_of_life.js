function nextGeneration(grid) {
    var height = grid.length;
    var width = grid[0].length;
    var newGrid = [];

    for (var y = 0; y < height; y++) {
        newGrid[y] = [];
        for (var x = 0; x < width; x++) {
            var live = 0;
            for (var dy = -1; dy <= 1; dy++) {
                for (var dx = -1; dx <= 1; dx++) {
                    if (dy === 0 && dx === 0) continue;
                    var ny = y + dy, nx = x + dx;
                    if (ny >= 0 && ny < height && nx >= 0 && nx < width && grid[ny][nx] === 1) live++;
                }
            }
            newGrid[y][x] = (grid[y][x] === 1 && (live === 2 || live === 3)) || (grid[y][x] === 0 && live === 3) ? 1 : 0;
        }
    }

    return newGrid;
}
