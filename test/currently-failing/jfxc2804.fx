/**
 * JFXC-2804 : Bind to Cast Sorted sequence causes internal compiler error
 *
 * @test/fail
 */

import java.lang.Comparable;
import javafx.util.Sequences;

/**
 * @author forsakendaemon
 */

class MyObject extends Comparable{
    public var attr: Integer;

    public override function compareTo(o:Object):Integer {
        if (o.getClass() != this.getClass()) then 0 else
            return (o as MyObject).attr - this.attr;
    }
}

var object1:MyObject = MyObject {attr: 1};
var object2:MyObject = MyObject {attr: 2}

var objects:MyObject[];

insert object1 into objects;
insert object2 into objects;

def sortedObjects:MyObject[] = bind (Sequences.sort(objects) as MyObject[]);
