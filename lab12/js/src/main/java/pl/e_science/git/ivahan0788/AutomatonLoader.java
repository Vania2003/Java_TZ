package pl.e_science.git.ivahan0788;

import javax.script.*;
import java.io.FileReader;
import java.nio.file.Path;
import org.openjdk.nashorn.api.scripting.ScriptObjectMirror;

public class AutomatonLoader {
    private ScriptEngine engine;
    private Invocable invocable;

    public AutomatonLoader(Path scriptPath) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        engine = manager.getEngineByName("nashorn");
        engine.eval(new FileReader(scriptPath.toFile()));
        invocable = (Invocable) engine;
    }

    public int[][] nextGeneration(int[][] grid) throws Exception {
        Object result = invocable.invokeFunction("nextGeneration", (Object) grid);
        if (!(result instanceof ScriptObjectMirror)) {
            throw new IllegalStateException("Unexpected result type from JS.");
        }
        ScriptObjectMirror outer = (ScriptObjectMirror) result;

        int height = outer.size();
        ScriptObjectMirror firstRow = (ScriptObjectMirror) outer.getSlot(0);
        int width = firstRow.size();

        int[][] converted = new int[height][width];

        for (int y = 0; y < height; y++) {
            ScriptObjectMirror row = (ScriptObjectMirror) outer.getSlot(y);
            for (int x = 0; x < width; x++) {
                converted[y][x] = ((Number) row.getSlot(x)).intValue();
            }
        }

        return converted;
    }
}