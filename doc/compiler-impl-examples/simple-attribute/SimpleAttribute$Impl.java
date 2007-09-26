import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.location.ChangeListener;
import com.sun.javafx.runtime.location.IntLocation;
import com.sun.javafx.runtime.location.IntVar;

/**
 * SimpleAttribute
 *
 * @author Brian Goetz
 */
public class SimpleAttribute$Impl implements SimpleAttribute$Intf {
    private IntLocation a;

    private SimpleAttribute$Impl() { }

    public static InitHelper<SimpleAttribute$Intf> make() {
        return new InitHelper<SimpleAttribute$Intf>(new SimpleAttribute$Impl());
    }

    public IntLocation get$a() {
        return a;
    }

    public void init$a(IntLocation location) {
        InitHelper.assertNonNull(a, "SimpleAttribute.a");
        this.a = location;
    }

    public void setDefaults$(InitHelper<?> helper) {
        if (a == null) a = helper.addDefaulted(IntVar.make(3));

        a.addChangeListener(new ChangeListener() {
            public boolean onChange() {
                System.out.println("a is now " + a);
                return true;
            }
        });
    }

    public void userInit$() {
        System.out.println("a = " + a);
    }

    public static void main(String[] args) {
        InitHelper<SimpleAttribute$Intf> helper = SimpleAttribute$Impl.make();
        SimpleAttribute$Intf s1 = helper.initialize();
        System.out.println(s1.get$a().get());

        helper = SimpleAttribute$Impl.make();
        helper.getInitTarget().init$a(helper.addDefaulted(IntVar.make(4)));
        SimpleAttribute$Intf s2 = helper.initialize();
        System.out.println(s2.get$a().get());
    }
}

interface SimpleAttribute$Intf extends FXObject {
    public IntLocation get$a();
    public void init$a(IntLocation location);
}
