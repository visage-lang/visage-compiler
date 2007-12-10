/*
 * Regression test: assertion failure: void conditionals & block expressions
 *
 * @test
 * @run
 */
import java.lang.System;

public class FontStyle {
    public attribute id: Integer;
    public attribute name: String;
    
    public static attribute BOLD = FontStyle {id: 1, name: "BOLD"};
    public static attribute PLAIN = FontStyle {id: 2, name: "PLAIN"};
    public static attribute ITALIC = FontStyle {id: 3, name: "ITALIC"};
}

public class Foo {
    public attribute style: FontStyle[];
    
    public function Font(faceName:String, style:String[], size:Integer){
        for (i in style) {
            if (i == "PLAIN") then {
                insert FontStyle.PLAIN into this.style
            } else if (i == "ITALIC") then {
                insert FontStyle.ITALIC into this.style
            } else if (i == "BOLD") then{
                insert FontStyle.BOLD into this.style
            } else {
                throw new java.lang.Throwable("Bad font style {i}: expected PLAIN, BOLD, or ITALIC")
            }
        }
    }
}
