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
    private static final int NUM$FIELDS = 1;

    public static int getNumFields$() {
        return NUM$FIELDS;
    }

    private IntLocation a;
    private InitHelper initHelper = new InitHelper(NUM$FIELDS);

    public IntLocation get$a() { return a; }

    public void init$a(IntLocation location) {
        InitHelper.assertNonNull(a, "Base.a");
        initHelper.add(this.a = location);
    }

    protected static void setDefaults$(final Base$Intf receiver) {
        if (receiver.get$a() == null) receiver.init$a(IntVar.make(3));
    }

    public static void userInit$(final Base$Intf receiver) { }

    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper.initialize();
        initHelper = null;
    }
}

class OtherBase$Impl implements OtherBase$Intf {
    private static final int NUM$FIELDS = 1;

    public static int getNumFields$() {
        return NUM$FIELDS;
    }

    private IntLocation b;
    private InitHelper initHelper = new InitHelper(NUM$FIELDS);

    public IntLocation get$b() { return b; }

    public void init$b(IntLocation location) {
        InitHelper.assertNonNull(b, "OtherBase.b");
        initHelper.add(this.b = location);
    }

    protected static void setDefaults$(final OtherBase$Intf receiver) {
        if (receiver.get$b() == null) receiver.init$b(IntVar.make(4));
    }

    public static void userInit$(final OtherBase$Intf receiver) { }

    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper.initialize();
        initHelper = null;
    }
}

public class Subclass$Impl implements Subclass$Intf {
    private static final int NUM$FIELDS = 1 + Base$Impl.getNumFields$() + OtherBase$Impl.getNumFields$();

    public static int getNumFields$() {
        return NUM$FIELDS;
    }

    private IntLocation a;
    private IntLocation b;
    private IntLocation c;
    private InitHelper initHelper = new InitHelper(NUM$FIELDS);

    public IntLocation get$a() { return a; }
    public IntLocation get$b() { return b; }
    public IntLocation get$c() { return c; }

    public void init$a(IntLocation location) {
        InitHelper.assertNonNull(b, "Subclass.a");
        initHelper.add(this.a = location);
    }

    public void init$b(IntLocation location) {
        InitHelper.assertNonNull(b, "Subclass.b");
        initHelper.add(this.b = location);
    }

    public void init$c(IntLocation location) {
        InitHelper.assertNonNull(c, "Subclass.c");
        initHelper.add(this.c = location);
    }

    protected static void setDefaults$(final Subclass$Intf receiver) {
        Base$Impl.setDefaults$(receiver);
        OtherBase$Impl.setDefaults$(receiver);
        if (receiver.get$c() == null) receiver.init$c(IntVar.make(5));
    }

    public static void userInit$(final Subclass$Intf receiver) {
        Base$Impl.userInit$(receiver);
        OtherBase$Impl.userInit$(receiver);
    }


    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper.initialize();
        initHelper = null;
    }

    public static void main(String[] args) {
        Subclass$Impl instance = new Subclass$Impl();
        instance.init$a(IntVar.make(1));
        instance.init$b(IntVar.make(2));
        instance.initialize$();
    }
}

