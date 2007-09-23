
import java.lang.*;

class Main {
    
    public void javafx$init$() {
    }
    
    Main() {
        super();
    }
    
    public void javafx$run$() {
        Main.Foo fo = {
            Foo $objlit$synth$0 = new Foo();
            $objlit$synth$0.javafx$init$();
            $objlit$synth$0
        };
        Main.Foo ho = {
            Foo $objlit$synth$1 = new Foo();
            $objlit$synth$1.javafx$init$();
            $objlit$synth$1
        };
        fo.a.set("shake");
        fo.c.set("rattle");
        ho.a.set("rats");
        ho.mud();
        ho.mud();
        fo.mud();
        fo.mud();
        System.out.print("fo.a: ");
        System.out.println(fo.a.get());
        System.out.print("fo.c: ");
        System.out.println(fo.c.get());
        System.out.print("fo.b: ");
        System.out.println(fo.b.get());
        System.out.print("ho.b: ");
        System.out.println(ho.b.get());
        fo.a.set(fo.bleep());
        System.out.print("fo.a: ");
        System.out.println(fo.a.get());
        System.out.print("fo.c: ");
        System.out.println(fo.c.get());
        System.out.print("fo.b: ");
        System.out.println(fo.b.get());
    }
    
    static class Foo {
        
        public void javafx$init$() {
            if (a == null) a = com.sun.javafx.runtime.location.ObjectVar<java.lang.String>.make(null);
            if (b == null) b = com.sun.javafx.runtime.location.ObjectExpression<String>.make(new com.sun.javafx.runtime.location.ObjectBindingExpression(){
                
                public java.lang.String get() {
                    return a.get();
                }
            }, a);
            if (c == null) c = com.sun.javafx.runtime.location.ObjectVar<java.lang.String>.make(null);
        }
        
        Foo() {
            super();
        }
        com.sun.javafx.runtime.location.ObjectLocation<java.lang.String> a;
        com.sun.javafx.runtime.location.ObjectLocation<java.lang.String> b;
        com.sun.javafx.runtime.location.ObjectLocation<java.lang.String> c;
        
        java.lang.String bleep() {
            return "roll";
        }
        
        int mud() {
            System.out.println(this.a.get());
            return 0;
        }
    }
    
    public static void main(String[] args) {
        new Main().javafx$run$();
    }
}