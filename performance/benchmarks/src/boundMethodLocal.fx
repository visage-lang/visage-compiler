/*
 * boundMethod.fx
 *
 * Created on Dec 22, 2009, 8:03:22 AM
 */


/**
 * @author ksrini
 */


class boundMethodLocal extends cbm {
    public-read var max:Number = 1000;
    public-read var min:Number = 0;
    function getSum(x: Number, y: Number) : Number {
        return x + y;
    }

    override public function test() {
        var i = min;
        var j = max;
        def sum = bind getSum(i, j);
        while (i < max) {
            while (j > min) {
                //debugOutln("i={i}, j={j}, mag={sum}");
                j--;
            }
            j=max;
            i++;
        }
        return 0;
    }//test

};//class

/**
 * define a run method to call runtests passing args and instance of this class
 */
public function run(args:String[]) {
    var t = new boundMethodLocal();
    cbm.runtest(args,t);
}
