/**
 * JFXC-500 : Access members in outer class with explicit class name.
 *
 * @test
 * @run
 */

// check that outer's shadowed member can accessed explicitly 
class jfxc500 {
    public var name = "jfxc500 class";
    public var value = 1;
    public function makeRunnable() {
        java.lang.Runnable {
            var name = "anonymous runnable class";
            override public function run() {
                println(name);         // prints this.name
                println(jfxc500.name); // prints outer's name
                // check assignment
                jfxc500.name = "changed name of jfxc500";
                jfxc500.value++;
                println(jfxc500.name); // prints outer's name
            }
        }
    }
}

jfxc500 {}.makeRunnable().run();

// make sure two level deep is fine...
class jfxc500_2 {
    public var name = "jfxc500_2 class";

    public function func() {
        java.lang.Runnable {
            var name = "anonymous runnable class";
            override public function run() {
                java.lang.Runnable {
                    override public function run() {
                        println(name); // prints outer's name
                        println(jfxc500_2.name); // prints outer's outer's name
                        // check assignment
                        jfxc500_2.name = "changed name of jfxc500_2";
                        println(jfxc500_2.name); // prints outer's outer's name
                    }
                }.run();
            }
        }.run();
    }
}

jfxc500_2 {}.func();

// check bind of OuterClass.name is fine too
// and also check OuterClass methods either qualified
// or unqualified
class jfxc500_3 {
    public var name = "jfxc500 class";

    public function outerFunc() {
        println("jfxc500_3.outerFunc called");
    }

    public function func() {
        java.lang.Runnable {
            override public function run() {
                var n = bind jfxc500_3.name.toUpperCase(); // bind outer's name
                println(n);
                jfxc500_3.name = "changed name jfxc500_3";
                println(n);
                outerFunc();
                jfxc500_3.outerFunc();
            }
        }.run();
    }
}

jfxc500_3 {}.func();
