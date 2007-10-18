import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.IntVar;
import com.sun.javafx.runtime.location.Location;

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
        if (receiver.get$a() == null) receiver.init$a(IntVar.make(3));
        // @@@ FIXME: need to set up bindings and then set up dependencies

        receiver.get$a().addChangeListener(new ChangeListener() {
            public boolean onChange(Location location) {
                System.out.println("a is now " + receiver.get$a().get());
                return true;
            }
        });
    }

    protected static void userInit$(final SimpleAttribute$Intf receiver) {
        System.out.println("a = " + receiver.get$a().get());
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
        System.out.println(instance.get$a().get());

        instance = new SimpleAttribute$Impl();
        instance.init$a(IntVar.make(4));
        instance.initialize$();
        System.out.println(instance.get$a().get());
    }
}

interface SimpleAttribute$Intf extends FXObject {
    public IntLocation get$a();

    public void init$a(IntLocation location);
}
