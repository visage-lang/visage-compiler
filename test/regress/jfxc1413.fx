/*
 * jfxc-1413
 * @test
 * @run
 */

// import all members and subpackage members
import java.lang.**;

// import all statics
import java.lang.Math.*;

// specific import
import java.util.Vector;

var obj = new Object();

// java.lang.ref.WeakReference
var wrc = new WeakReference(obj).getClass();

// java.lang.reflect.Method
var m : Method = wrc.getMethod("get", null);

// java.util.Vector
var vc = new Vector().getClass();

// java.lang.Math.PI and java.lang.Math.sin
System.out.println(sin(PI/2.0));
System.out.println(wrc.getName());
System.out.println(m.getName());
System.out.println(vc.getName());


