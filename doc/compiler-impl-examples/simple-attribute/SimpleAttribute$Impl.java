import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.*;

/**
 * SimpleAttribute
 *
 * @author Brian Goetz
 */
public class SimpleAttribute$Impl implements SimpleAttribute$Intf {
    private static final int NUM$FIELDS = 1;

    public static int getNumFields$() {
        return NUM$FIELDS;
    }

    private IntLocation a;
    private InitHelper initHelper = new InitHelper(NUM$FIELDS);

    public IntLocation get$a() {
        return a;
    }

    public void init$a(IntLocation location) {
        InitHelper.assertNonNull(a, "SimpleAttribute.a");
        initHelper.add(this.a = location);
    }

    protected static void setDefaults$(final SimpleAttribute$Intf receiver) {
        if (receiver.get$a() == null) receiver.init$a(LegacyIntVar.make(3));
        // @@@ FIXME: need to set up bindings and then set up dependencies

        receiver.get$a().addChangeListener(new ChangeListener() {
            public boolean onChange(Location location) {
                System.out.println("a is now " + receiver.get$a().getAsInt());
                return true;
            }
        });
    }

    protected static void userInit$(final SimpleAttribute$Intf receiver) {
        System.out.println("a = " + receiver.get$a().getAsInt());
    }

    public void initialize$() {
        setDefaults$(this);
        userInit$(this);
        initHelper.initialize();
        initHelper = null;
    }

    public static void main(String[] args) {
        SimpleAttribute$Impl instance = new SimpleAttribute$Impl();
        instance.initialize$();
        System.out.println(instance.get$a().getAsInt());

        instance = new SimpleAttribute$Impl();
        instance.init$a(LegacyIntVar.make(4));
        instance.initialize$();
        System.out.println(instance.get$a().getAsInt());
    }
}

interface SimpleAttribute$Intf extends FXObject {
    public IntLocation get$a();

    public void init$a(IntLocation location);
}
