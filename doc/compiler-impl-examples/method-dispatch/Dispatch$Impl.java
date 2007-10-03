import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.IntLocation;

/**
 * Dispatch$Impl
 *
 * @author Brian Goetz
 */

interface Base1$Intf extends FXObject {
    public IntLocation get$n();
    public void init$n(IntLocation location);

    public int foo(int a);
    public int moo(int a);
    public int bar(int a);
}

interface Base2$Intf extends FXObject {
    public int bork(int a);
}

interface Dispatch$Intf extends Base1$Intf, Base2$Intf {
}

class Base1$Impl implements Base1$Intf {
    private IntLocation n;

    public IntLocation get$n() { return n; }
    public void init$n(IntLocation location) { n = location; }
    public void setDefaults$(InitHelper helper) { }
    public void userInit$() { }
    public void initialize$() { }

    protected static int foo(Base1$Intf receiver, int a) { return a + receiver.get$n().get() + 1; }
    protected  static int moo(Base1$Intf receiver, int a) { return a + receiver.get$n().get() + 2; }
    protected  static int bar(Base1$Intf receiver, int a) { return a + receiver.get$n().get() + 3; }

    public int foo(int a) { return foo(this, a); }
    public int moo(int a) { return moo(this, a); }
    public int bar(int a) { return bar(this, a); }
}

class Base2$Impl implements Base2$Intf {
    public void setDefaults$(InitHelper helper) { }
    public void userInit$() { }
    public void initialize$() { }

    protected static int bork(Base2$Intf receiver, int a) { return a + 4; }

    public int bork(int a) { return bork(this, a); }
}


public class Dispatch$Impl implements Dispatch$Intf {
    private IntLocation n;
    public IntLocation get$n() { return n; }
    public void init$n(IntLocation location) { n = location; }

    public void setDefaults$(InitHelper helper) { }
    public void userInit$() { }
    public void initialize$() { }

    protected static int foo(Dispatch$Intf receiver, int a) { return a + receiver.get$n().get() + 5; }
    public int foo(int a) { return foo(this, a); }
    public int moo(int a) { return Base1$Impl.moo(this, a); }
    public int bar(int a) { return Base1$Impl.bar(this, a); }
    public int bork(int a) { return Base2$Impl.bork(this, a); }
}
