package com.sun.javafx.runtime.location;

import java.util.ArrayList;
import java.util.List;

import com.sun.javafx.runtime.BindingException;
import com.sun.javafx.runtime.FXObject;
import com.sun.javafx.runtime.InitHelper;
import com.sun.javafx.runtime.JavaFXTestCase;
import framework.FXObjectFactory;

/**
 * InitializationTest
 *
 * @author Brian Goetz
 */
public class InitializationTest extends JavaFXTestCase {
    /**
     * Test initialization sequences
     */
    public void testLocalInitialization() {
        final IntVariable loc = IntVariable.make();
        assertFalse(loc.isInitialized());
        assertEquals(0, loc.getAsInt());
        loc.set(3);
        assertTrue(loc.isInitialized());
	//TODO: commented-out as part of work-around for JFXC-979
	/**
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                loc.bind(false, new IntBindingExpression() {
                    public int computeValue() {
                        return 0;
                    }
                });
            }
        });
	**/

        final IntVariable loc2 = IntVariable.make(3);
        assertTrue(loc2.isInitialized());

        final IntVariable loc3 = IntVariable.make();
        assertFalse(loc3.isInitialized());
        loc3.bind(false, new IntBindingExpression() {
            public int computeValue() {
                return 0;
            }
        });
        assertTrue(loc3.isInitialized());

        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                loc3.bind(false, new IntBindingExpression() {
                    public int computeValue() {
                        return 0;
                    }
                });
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                loc3.set(9);
            }
        });

        final IntVariable loc4 = IntVariable.make(new IntBindingExpression() {
            public int computeValue() {
                return 0;
            }
        });
        assertTrue(loc4.isInitialized());
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                loc3.bind(false, new IntBindingExpression() {
                    public int computeValue() {
                        return 0;
                    }
                });
            }
        });
        assertThrows(BindingException.class, new VoidCallable() {
            public void call() throws Exception {
                loc3.set(9);
            }
        });
    }

    private static class SimpleAttribute {
        private final IntVariable a = IntVariable.make();
        private AbstractVariable[] attributes = {a};

        public IntVariable get$a() {
            return a;
        }

        protected static void addTriggers$(final SimpleAttribute receiver) {
        }

        protected static void initAttributes$(final SimpleAttribute receiver) {
            if (receiver.get$a().needDefault())
                receiver.get$a().setAsInt(3);
        }

        public void initialize$() {
            initAttributes$(this);
            InitHelper.finish(attributes);
        }
    }

    public void testSimpleAttribute() {
        // Test that default is applied if no literal
        SimpleAttribute instance = new SimpleAttribute();
        instance.initialize$();
        assertEquals(3, instance.get$a().getAsInt());
        instance.get$a().setAsInt(4);
        assertEquals(4, instance.get$a().getAsInt());

        // Test that literal is applied if present
        instance = new SimpleAttribute();
        instance.get$a().setAsIntFromLiteral(4);
        instance.initialize$();
        assertEquals(4, instance.get$a().getAsInt());

        // Test that literal is applied even if overwritten by trigger
        instance = new SimpleAttribute();
        instance.get$a().setAsIntFromLiteral(4);
        instance.get$a().set(666);
        instance.initialize$();
        assertEquals(4, instance.get$a().getAsInt());

        // Test that default is not applied if written by trigger
        instance = new SimpleAttribute();
        instance.get$a().set(4);
        instance.initialize$();
        assertEquals(4, instance.get$a().getAsInt());
    }

    private static class Temperature {
        private final IntVariable f = IntVariable.make();
        private final IntVariable c = IntVariable.make();
        private AbstractVariable[] attributes = {f, c};

        private Temperature() {
            addTriggers$(this);
        }

        public IntVariable get$f() {
            return f;
        }

        public IntVariable get$c() {
            return c;
        }

        protected static void addTriggers$(final Temperature receiver) {
            receiver.get$f().addChangeListener(new PrimitiveChangeListener<Integer>() {
                public void onChange(int oldValue, int newValue) {
                    receiver.get$c().set((receiver.get$f().get() - 30) / 2);
                }
            });
            receiver.get$c().addChangeListener(new PrimitiveChangeListener<Integer>() {
                public void onChange(int oldValue, int newValue) {
                    receiver.get$f().set(2 * receiver.get$c().get() + 30);
                }
            });
        }

        protected static void initAttributes$(final Temperature receiver) {
            if (receiver.get$f().needDefault())
                receiver.get$f().setAsInt(30);
            if (receiver.get$c().needDefault())
                receiver.get$c().setAsInt(0);
        }

        public void initialize$() {
            initAttributes$(this);
            InitHelper.finish(attributes);
        }
    }

    public void testTemeperature() {
        // Defaults only
        Temperature instance = new Temperature();
        instance.initialize$();
        assertEquals(30, instance.f);
        assertEquals(0, instance.c);
        instance.get$c().set(10);
        assertEquals(50, instance.f);
        assertEquals(10, instance.c);
        instance.get$f().set(60);
        assertEquals(60, instance.f);
        assertEquals(15, instance.c);

        // F from literal only
        instance = new Temperature();
        instance.get$f().setAsIntFromLiteral(50);
        instance.initialize$();
        assertEquals(50, instance.f);
        assertEquals(10, instance.c);

        // C from literal only
        instance = new Temperature();
        instance.get$c().setAsIntFromLiteral(10);
        instance.initialize$();
        assertEquals(50, instance.f);
        assertEquals(10, instance.c);

        // Both from literal but inconsistent
        instance = new Temperature();
        instance.get$c().setAsIntFromLiteral(10);
        instance.get$f().setAsIntFromLiteral(60);
        instance.initialize$();
        assertEquals(50, instance.f);
        assertEquals(10, instance.c);
    }

    private static class ThreeAttributes {
        private final IntVariable a = IntVariable.make();
        private final IntVariable b = IntVariable.make();
        private final IntVariable c = IntVariable.make();
        private AbstractVariable[] attributes = {a, b, c};
        public final List<String> list = new ArrayList<String>();

        private ThreeAttributes() {
            addTriggers$(this);
        }

        public IntVariable get$a() { return a; }
        public IntVariable get$b() { return b; }
        public IntVariable get$c() { return c; }

        protected static void addTriggers$(final ThreeAttributes receiver) {
            receiver.get$a().addChangeListener(new PrimitiveChangeListener<Integer>() {
                public void onChange(int oldValue, int newValue) {
                    receiver.list.add("a:" + newValue);
                }
            });
            receiver.get$b().addChangeListener(new PrimitiveChangeListener<Integer>() {
                public void onChange(int oldValue, int newValue) {
                    receiver.list.add("b:" + newValue);
                }
            });
            receiver.get$c().addChangeListener(new PrimitiveChangeListener<Integer>() {
                public void onChange(int oldValue, int newValue) {
                    receiver.list.add("c:" + newValue);
                }
            });
        }

        protected static void initAttributes$(final ThreeAttributes receiver) {
            if (receiver.get$a().needDefault())
                receiver.get$a().setAsInt(1);
            if (receiver.get$b().needDefault())
                receiver.get$b().setAsInt(0);
            if (receiver.get$c().needDefault())
                receiver.get$c().setDefault();
        }

        public void initialize$() {
            initAttributes$(this);
            InitHelper.finish(attributes);
        }
    }

    public void testTriggers() {
        // Defaults only
        ThreeAttributes instance = new ThreeAttributes();
        instance.initialize$();
        assertEquals("[a:1, b:0, c:0]", instance.list.toString());

        // A from literal
        instance = new ThreeAttributes();
        instance.get$a().setAsIntFromLiteral(4);
        instance.initialize$();
        assertEquals("[a:4, b:0, c:0]", instance.list.toString());

        // B=0, C=0 from literal
        instance = new ThreeAttributes();
        instance.get$b().setAsIntFromLiteral(0);
        instance.get$c().setAsIntFromLiteral(0);
        instance.initialize$();
        assertEquals("[a:1, b:0, c:0]", instance.list.toString());
    }

    public void testBijection() {
        ThreeAttributes instance = new ThreeAttributes();
        instance.get$a().setAsIntFromLiteral(3);
        instance.get$b().bijectiveBindFromLiteral(instance.get$a());
        instance.initialize$();
        assertEquals(3, instance.get$a().getAsInt());
        assertEquals(3, instance.get$b().getAsInt());
    }

    interface MyObject extends FXObject {
        public IntLocation get$a();
        public IntLocation get$b();
    }

    private static int addTriggerCount;
    private static int initCount;
    private static int postInitCount;

    static FXObjectFactory<MyObject> myObjectFactory = new FXObjectFactory<MyObject>(MyObject.class, new String[] { "a", "b" }) {

        public void applyDefault(MyObject receiver, String attrName, AbstractVariable attrLocation) {
            if (attrName.equals("a"))
                ((IntLocation) attrLocation).setAsInt(9);
            else if (attrName.equals("b"))
                ((IntLocation) attrLocation).setAsInt(10);
        }

        public void addTriggers(MyObject receiver) {
            ++addTriggerCount;
        }

        public void init(MyObject receiver) {
            ++initCount;
        }

        public void postInit(MyObject receiver) {
            ++postInitCount;
        }
    };

    public void testSimulatedInitialization() {
        MyObject o = myObjectFactory.make();
        o.get$a().setAsIntFromLiteral(3);
        o.get$b().setAsIntFromLiteral(4);
        o.initialize$();
        assertEquals(3, o.get$a().getAsInt());
        assertEquals(4, o.get$b().getAsInt());

        assertEquals(1, addTriggerCount);
        assertEquals(1, initCount);
        assertEquals(1, postInitCount);

        o = myObjectFactory.make();
        o.initialize$();
        assertEquals(9, o.get$a().getAsInt());
        assertEquals(10, o.get$b().getAsInt());
    }
}
