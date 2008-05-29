/* regression test for JFXC-1099
 *
 * @test
 * @run
 */

import java.lang.System;

class TextField {};

class Button {
    attribute text: String;
    attribute action: function();
}

class Frame {
    attribute content: Button;
    attribute visible: Boolean;
}

class Foo {

    function foo() {
        var tf = TextField{};
        var button: Button = Button {
            text: "Click me"
            action: function() {                
                button.text = "foo";
            }
        };
        
        Frame {
            content: button
            visible: true
        }
    }
}
        
var f = Foo{};
var frame = f.foo();
System.out.println("Text: {frame.content.text}");
frame.content.action();
System.out.println("Text: {frame.content.text}");
