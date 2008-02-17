import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.AbstractVariable;
import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.location.IntVariable;
import com.sun.javafx.runtime.location.Location;

/**
 * SimpleAttribute
 *
 * @author Brian Goetz
 */
public class SimpleAttribute$Impl implements SimpleAttribute$Intf {
    public SimpleAttribute$Impl() {
        addTriggers$(this);
    }

    private final IntVariable a = IntVariable.make();
    private AbstractVariable[] attributes = { a };

    public IntVariable get$a() {
        return a;
    }

    protected static void addTriggers$(final SimpleAttribute$Intf receiver) {
        // Call superclass addTriggers$()
        receiver.get$a().addChangeListener(new ChangeListener() {
            public boolean onChange(Location location) {
                System.out.println("a is now " + receiver.get$a().getAsInt());
                return true;
            }
        });
    }

    protected static void initAttributes$(final SimpleAttribute$Intf receiver) {
        // Call superclass setDefaults$()
        if (receiver.get$a().needDefault())
            receiver.get$a().setAsInt(3);
    }

    protected static void userInit$(final SimpleAttribute$Intf receiver) {
        // Call superclass userInit$()
        System.out.println("a = " + receiver.get$a().getAsInt());
    }

    public void initialize$() {
        initAttributes$(this);
        userInit$(this);
        InitHelper.finish(attributes);
        attributes = null;
    }

    public static void main(String[] args) {
        SimpleAttribute$Impl instance = new SimpleAttribute$Impl();
        instance.initialize$();
        System.out.println(instance.get$a().getAsInt());

        instance = new SimpleAttribute$Impl();
        instance.get$a().setAsInt(4);
        instance.initialize$();
        System.out.println(instance.get$a().getAsInt());
    }
}

interface SimpleAttribute$Intf extends FXObject {
    public IntVariable get$a();
}
