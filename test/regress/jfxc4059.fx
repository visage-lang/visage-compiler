/*
 * Regression test
 * JFXC-4059 : JavaFxScriptCompiler.compile returns null.
 *
 * @test
 * @run
 */

import com.sun.tools.javafx.script.JavaFXScriptContext;
import com.sun.tools.javafx.script.JavaFXScriptCompiler;

def context = new JavaFXScriptContext(jfxc4059.class.getClassLoader());
def compiler:JavaFXScriptCompiler = getField(context, 'compiler') as JavaFXScriptCompiler;
def classPath = java.lang.System.getProperty("java.class.path");
def scrpt = "class A \{ var x; \}\nvar x = A \{ var f:Integer = 1, x:f \};\n";
compiler.compile('Script', scrpt, null, null, classPath, null);

function getField(obj:Object, fieldName:String):Object {
    def field:java.lang.reflect.Field = obj.getClass().getDeclaredField(fieldName);
    field.setAccessible(true);
    field.get(obj);
}