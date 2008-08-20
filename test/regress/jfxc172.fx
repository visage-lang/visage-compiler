/**
 * regression test: JFXC-172:Error on if-else - incompatible types for ?: neither is a subtype of the other
 * @test
 * @run
 */
class Bar {
    var focusable : Boolean = true;
    public var focused: Boolean = false on replace { 
        if (focused) { 
            requestFocus(); 
        } else { 
            var f = focusable; 
            focusable = false; 
            focusable = f; 
        } 
    }; 

    public function requestFocus() { 
        if (focusable) { 
            bbb();
        } 
    } 

    function bbb() {
    }
}
