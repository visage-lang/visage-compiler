class Outer {
    attribute o : Integer;
    var v = Middle {
        a : 1
        function toString() : String { "middle" }
        listener : Listener {
            function onEvent() { println(a + o); }
        }
    }
}

class Middle {
    public attribute a : Integer;
    public attribute listener : Listener;
}

abstract class Listener {
    abstract function onEvent() : Void;
}
