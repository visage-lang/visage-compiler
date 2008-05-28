import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.swing.*;
import java.awt.*;

public class CalcJSR223 {

    public static void main(String[] argv) throws Exception {
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByExtension("fx");
        ScriptContext context = engine.getContext();
        Bindings bindings = context.getBindings(ScriptContext.ENGINE_SCOPE);
        JFrame frame = new JFrame();
        Container container = frame.getContentPane();
        bindings.put("GLOSSITOPE:java.awt.Container", container);
        String script = "import calc.Calculator;";
        engine.eval(script);
        frame.pack();
        frame.setVisible(true);
    }
}
