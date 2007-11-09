/**
 * regression test:  Bug jfxc-180
 * @test
 * @run
 */
import java.lang.Object;

class Bar {
	attribute a : Integer;
    	public attribute enabled: Boolean = true;
	public attribute action: function():Void;
}

class BarUser {
	attribute b : Bar[] 
	on insert [indx] (newValue) { 
            var k = newValue.a; 
	    newValue.action();
	    var vvv = Object {
		public function isEnabled():Boolean {
                        return newValue.enabled;
		}
	    }
	}
	on delete [indx] (newValue) { 
	}
	on replace [indx] (newValue) { 
	}
}