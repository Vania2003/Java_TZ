function nextGeneration(grid) {
    var height = grid.length;
    var width = grid[0].length;
    var newGrid = [];

    for (var y = 0; y < height; y++) {
        newGrid[y] = [];
        for (var x = 0; x < width; x++) {
            var state = grid[y][x];
            if (state === 0) {
                newGrid[y][x] = 0;
            } else if (state === 1) {
                newGrid[y][x] = 2;
            } else if (state === 2) {
                newGrid[y][x] = 3;
            } else if (state === 3) {
                var headCount = 0;
                for (var dy = -1; dy <= 1; dy++) {
                    for (var dx = -1; dx <= 1; dx++) {
                        if (dy === 0 && dx === 0) continue;
                        var ny = y + dy;
                        var nx = x + dx;
                        if (ny >= 0 && ny < height && nx >= 0 && nx < width && grid[ny][nx] === 1) {
                            headCount++;
                        }
                    }
                }
                newGrid[y][x] = (headCount === 1 || headCount === 2) ? 1 : 3;
            }
        }
    }

    return newGrid;
}
