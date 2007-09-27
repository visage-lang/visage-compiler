import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.IntLocation;

/**
 * Dispatch$Impl
 *
 * @author Brian Goetz
 */

interface Base$Intf extends FXObject {
    public IntLocation get$n();
    public void init$n(IntLocation location);

    public int foo(int a);
    public int moo(int a);
    public int bar(int a);
}

interface OtherBase$Intf extends FXObject {
    public int bork(int a);
}

interface Dispatch$Intf extends Base$Intf, OtherBase$Intf {
}

class Base$Impl implements Base$Intf {
    private IntLocation a;

    public IntLocation get$n() { return a; }
    public void init$n(IntLocation location) { a = location; }
    public void setDefaults$(InitHelper<?> helper) { }
    public void userInit$() { }

    protected static int foo(Base$Intf receiver, int a) { return a + receiver.get$n().get() + 1; }
    protected static int moo(Base$Intf receiver, int a) { return a + receiver.get$n().get() + 2; }
    protected static int bar(Base$Intf receiver, int a) { return a + receiver.get$n().get() + 3; }

    public int foo(int a) { return foo(this, a); }
    public int moo(int a) { return moo(this, a); }
    public int bar(int a) { return bar(this, a); }
}

class OtherBase$Impl implements OtherBase$Intf {
    public void setDefaults$(InitHelper<?> helper) { }
    public void userInit$() { }

    protected static int bork(OtherBase$Intf receiver, int a) { return a + 4; }

    public int bork(int a) { return bork(this, a); }
}


public class Dispatch$Impl implements Dispatch$Intf {
    Base$Impl base = new Base$Impl();
    OtherBase$Impl otherBase = new OtherBase$Impl();

    public IntLocation get$n() { return base.get$n(); }
    public void init$n(IntLocation location) { base.init$n(location); }

    public void setDefaults$(InitHelper<?> helper) { }
    public void userInit$() { }

    protected static int foo(Dispatch$Intf receiver, int a) { return a + receiver.get$n().get() + 5; }
    public int foo(int a) { return foo(this, a); }
    public int moo(int a) { return Base$Impl.moo(this, a); }
    public int bar(int a) { return Base$Impl.bar(this, a); }
    public int bork(int a) { return OtherBase$Impl.bork(this, a); }
}
