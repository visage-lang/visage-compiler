import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.IntVar;


interface Base$Intf extends FXObject {
    public IntLocation get$a();

    public void init$a(IntLocation location);
}

interface OtherBase$Intf extends FXObject {
    public IntLocation get$b();

    public void init$b(IntLocation location);
}

interface Subclass$Intf extends Base$Intf, OtherBase$Intf {
    public IntLocation get$c();

    public void init$c(IntLocation location);
}


class Base$Impl implements Base$Intf {
    private IntLocation a;

    protected Base$Impl() { }

    public static InitHelper<Base$Intf> make() {
        return new InitHelper<Base$Intf>(new Base$Impl());
    }

    public IntLocation get$a() {
        return a;
    }

    public void init$a(IntLocation location) {
        InitHelper.assertNonNull(a, "Base.a");
        this.a = location;
    }

    public void setDefaults$(InitHelper<?> helper) {
        if (a == null) a = helper.addDefaulted(IntVar.make(3));
    }

    public void userInit$() { }
}

class OtherBase$Impl implements OtherBase$Intf {
    private IntLocation b;

    protected OtherBase$Impl() { }

    public static InitHelper<OtherBase$Intf> make() {
        return new InitHelper<OtherBase$Intf>(new OtherBase$Impl());
    }

    public IntLocation get$b() {
        return b;
    }

    public void init$b(IntLocation location) {
        InitHelper.assertNonNull(b, "OtherBase.b");
        this.b = location;
    }

    public void setDefaults$(InitHelper<?> helper) {
        if (b == null) b = helper.addDefaulted(IntVar.make(4));
    }

    public void userInit$() { }
}

public class Subclass$Impl implements Subclass$Intf {
    private IntLocation c;

    Base$Impl base = new Base$Impl();
    OtherBase$Impl otherBase = new OtherBase$Impl();

    protected Subclass$Impl() {
    }

    public static InitHelper<Subclass$Intf> make() {
        return new InitHelper<Subclass$Intf>(new Subclass$Impl());
    }

    public IntLocation get$c() {
        return c;
    }

    public void init$c(IntLocation location) {
        InitHelper.assertNonNull(c, "Subclass.c");
        this.c = location;
    }

    public IntLocation get$a() {
        return base.get$a();
    }

    public void init$a(IntLocation location) {
        base.init$a(location);
    }

    public IntLocation get$b() {
        return otherBase.get$b();
    }

    public void init$b(IntLocation location) {
        otherBase.init$b(location);
    }

    public void setDefaults$(InitHelper<?> helper) {
        base.setDefaults$(helper);
        otherBase.setDefaults$(helper);
        if (c == null) c = helper.addDefaulted(IntVar.make(5));
    }

    public void userInit$() {
        base.userInit$();
        otherBase.userInit$();
    }

    public static void main(String[] args) {
        InitHelper<Subclass$Intf> helper = Subclass$Impl.make();
        helper.getInitTarget().init$a(helper.addDefaulted(IntVar.make(1)));
        helper.getInitTarget().init$b(helper.addDefaulted(IntVar.make(2)));
        Subclass$Intf var = helper.initialize();
    }
}

