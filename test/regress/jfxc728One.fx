/*
 * @subtest jfxc728
 */

public class One {
    var attr :Integer;
    public function getAttr1() :Integer { attr; }
    public function setAttr1(val :Integer) :Void  { attr = val }
}

mixin public class Two {
    var attr : Number = 2.5;
    public function getAttr2() :Number { attr; }
    public function setAttr2(val :Number) :Void  { attr = val }
}

