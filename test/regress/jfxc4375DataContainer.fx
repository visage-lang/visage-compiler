/*
 * Regression - JFXC-4375 - Compilation fails when using java classes with generics.
 *
 * @subtest
 *
 */

import java.lang.UnsupportedOperationException;

package class jfxc4375DataContainer {
    public-init var data:String;
    public function perform (proc:jfxc4375Processor):Void {
        println(proc.process(jfxc4375FxGetter{container:this}));
    }
    public function getData():Object {
        data
    }
}
class jfxc4375FxGetter extends jfxc4375Getter {
    var container:jfxc4375DataContainer;
    override public function get () : Object {
        container.getData();
    }
}
