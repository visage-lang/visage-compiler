import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.IntVar;
import com.sun.javafx.runtime.location.ObjectLocation;
import com.sun.javafx.runtime.location.ObjectVar;

class Outer implements Outer$Intf {
    private static final int NUM$FIELDS = 1;
    private InitHelper initHelper = new InitHelper(NUM$FIELDS);
    private IntLocation o;

    final class Middle$1 extends Middle {

        public String toString() { return "middle"; }

        final class Listener$1 extends Listener {
            public void onEvent() {
                System.out.println(Middle$1.this.get$a().get()
                    + Outer.this.get$o().get());
            }
        }
    }

    public void foo() {
        ObjectLocation<Middle$Intf> v = ObjectVar.make(null);
        Middle$1 tmp = new Middle$1();
        tmp.init$a(IntVar.make(1));
        Listener tmp2 = tmp.new Listener$1();
        tmp2.initialize$();
        tmp.init$listener(ObjectVar.make((Listener$Intf) tmp2));
        tmp.initialize$();
        v.set(tmp);
    }

    public IntLocation get$o() { return o; }
    public void init$o(IntLocation loc) { o = loc; }
    protected static void setDefaults$(final Outer$Intf receiver) {
    }

    public static void userInit$(final Outer$Intf receiver) { }

    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper.initialize();
        initHelper = null;
    }
}

interface Outer$Intf {
    public IntLocation get$o();
    public void init$o(IntLocation loc);
}

interface Middle$Intf {
    public IntLocation get$a();
    public void init$a(IntLocation loc);
    public ObjectLocation<Listener$Intf> get$listener();
    public void init$listener(ObjectLocation<Listener$Intf> loc);
}

interface Listener$Intf {
    public void onEvent();
}


class Middle implements Middle$Intf {
    private IntLocation a;
    private ObjectLocation<Listener$Intf> listener;
    private static final int NUM$FIELDS = 2;
    private InitHelper initHelper = new InitHelper(NUM$FIELDS);

    public IntLocation get$a() { return a; }
    public void init$a(IntLocation loc) { a = loc; }

    public ObjectLocation<Listener$Intf> get$listener() { return listener; }
    public void init$listener(ObjectLocation<Listener$Intf> loc) { listener = loc; }

    protected static void setDefaults$(final Middle$Intf receiver) {
    }

    public static void userInit$(final Middle$Intf receiver) { }

    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper.initialize();
        initHelper = null;
    }
}

abstract class Listener implements Listener$Intf {
    private static final int NUM$FIELDS = 2;
    private InitHelper initHelper = new InitHelper(NUM$FIELDS);

    protected static void setDefaults$(final Listener$Intf receiver) {
    }

    public static void userInit$(final Listener$Intf receiver) { }

    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper.initialize();
        initHelper = null;
    }
}
