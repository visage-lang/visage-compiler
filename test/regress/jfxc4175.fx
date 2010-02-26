/**
 * JFXC-4175 :  Indaba Internal compiler error incompatible types found : com.sun.javafx.runtime.Pointer required: javafx.scene.control.Button
 *
 * @test
 * @run
 */

class TextButton {
    var name: String;
    public override function toString() : String {
        return "TextButton \{ name: \"{name}\" \}";
    }
}

bound function createTextButton(text: String) {
    return TextButton { name: text };
}

var buttons = bind [
    createTextButton("OK")
];

println(buttons);
