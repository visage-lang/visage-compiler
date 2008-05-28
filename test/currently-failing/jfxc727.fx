/*
 * @test
 * @run/fail
 * 
 */

public class One {
    private function func() {
        java.lang.System.out.println("One.func() called");
    }
    
    public function pub() {
        func();
    }
}

public class Two extends One {
    private function func() {
        java.lang.System.out.println("Two.func() called");
    }
}

Two{}.pub(); 
