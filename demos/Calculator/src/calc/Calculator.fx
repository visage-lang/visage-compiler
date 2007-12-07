package calc;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.text.DecimalFormat;
import java.lang.System;
import java.awt.Container;
import java.awt.BorderLayout;
import java.lang.Object;
import java.lang.System;

class CalcButton extends CompositeNode {
    attribute pressedImage: Image;
    attribute image: Image;
    attribute pressed: Boolean;
    attribute action: function():Void;
    
    public function composeNode(): Node {
        ImageView {
            onMousePressed: function(e:CanvasMouseEvent):Void  {pressed = true;}
            onMouseReleased:  function(e:CanvasMouseEvent):Void {
                pressed = false; 
                //TODO hover
                //if (hover and action <> null) {
                if (action <> null) {
                    action();
                }
            }
            image: bind if (hover and pressed) then pressedImage else image
        };
    }
    
    public attribute isSelectionRoot: Boolean = true;
}


public class Calculator extends CompositeNode {
    attribute reg1: Number;
    attribute reg2: Number;
    attribute mem: Number;
    attribute operator: String;
    attribute isFixReg: Boolean = true;
    attribute text: String= "0";
    function calculate(op:String, r1:Number, r2:Number):Number {
        if (op == "+")
        then r1 + r2
        else if (op == "-") 
        then r1 - r2
        else if (op == "*")
        then r1 * r2
        else if (op == "/")
        then r1 / r2
        else r2
    ;        
    }
    private attribute df:DecimalFormat = new DecimalFormat("###,###,###,###.########");
    function formatNum(n:Number):String { 
        return df.format(n);
        //"{%###,###,###,###.######## n}"; 
    }
    function input(value:String):Void {
        if (value == "decimal") {
            value = ".";
        }
        if (value == "c") {
            reg1 = 0;
            reg2 = 0;
            operator = "";
            text = "0";
            isFixReg = true;
        } else if (value == "del") {
            if (text.length() > 1) {
                text = text.substring(0, text.length()-1);
            } else {
                text = "0";
                isFixReg = true;
            }
        //} else if (value in (select "{i}" from i in [0..9]) or value == ".") {
        } else if (value == "0" or value == "1" or value == "2" or value == "3" or
                   value == "4" or value == "5" or value == "6" or
                   value == "7" or value == "8" or value == "9" or
                   value == ".") {
            if (isFixReg) {
                if (value == ".") {
                    text = "0.";
                } else {
                    text = "{value}";
                }
            } else if (value <> "." or text.indexOf(".") < 0) {
                text = "{text}{value}";

            }
            isFixReg = false;

        } else if (value == "+" or value == "-" or value == "*" or value == "/" or value == "=") {
            var num = df.parse(text);
            reg2 = num.doubleValue();
            reg1 = calculate(operator, reg1, reg2);
            operator = value;
            text = formatNum(reg1);
            isFixReg = true;
        } else if (value == "mr") {
            text = formatNum(mem);
            operator = "";
            isFixReg = true;
        } else if (value == "m+") {
            reg1 = calculate(operator, reg1, reg2);
            text = formatNum(reg1);
            mem = mem + reg1;
            operator = "";
            isFixReg = true;
        } else if (value == "m-") {
            reg1 = calculate(operator, reg1, reg2);
            text = formatNum(reg1);
            mem = mem - reg1;
            operator = "";
            isFixReg = true;
        } else if (value == "mc") {
            mem = 0;
        }
        
    }
    
    public attribute focusable = true;
    public attribute onKeyDown: function(e:KeyEvent):Void = function(e:KeyEvent):Void {
        if (e.keyStroke == KeyStroke.ENTER) {
            input("=");
        } else if (e.keyStroke == KeyStroke.BACK_SPACE) {        
            input("del");
        }
    };
    public attribute onKeyTyped: function(e:KeyEvent):Void = function(e:KeyEvent):Void {
        input(e.keyChar);
    };
    public function composeNode(): Node {
        var n13 = [1,2,3];
        var n46 = [4,5,6];
        var n79 = [7,8,9];
        var dec = ["0", "decimal", "c"];
        var mr = ["m+", "m-", "mc", "mr"];
        var divmult = ["div", "mult", "sub", "add"];
        var ops = ["/", "*", "-", "+"];
        Group {
            content:
            [ImageView {
                image: Image{url: this.getClass().getResource("images/Calculator.png").toString()}
            } as Node,
            ImageView { 
                transform: [Transform.translate(13, 8)]
                image: Image{url: this.getClass().getResource("images/lcd-backlight.png").toString()}
            } as Node,
            Text {
                transform: [Transform.translate(150, 18)]
                halign: HorizontalAlignment.TRAILING
                font: Font {face: FontFace.ARIAL size: 20}
                content: bind text
                //fill: Color.rgba(0, 0, 0, 0.4)
                fill: Color.WHITE
            } as Node,
            //foreach (i in [1,2,3])
            foreach(i in [0..sizeof n13 exclusive ])
            (CalcButton {
                transform: [Transform.translate(16+i *38, 153)]
                image: Image{url: this.getClass().getResource("images/{n13[i]}.png").toString()}
                pressedImage: Image{url: this.getClass().getResource("images/d{n13[i]}.png").toString()}
                action: function():Void {input("{n13[i]}");}
            } as Object) as Node,
            //foreach (i in [4,5,6])
            foreach(i in [0..sizeof n46 exclusive ])
            (CalcButton {
                transform: [Transform.translate(16+i *38, 153-38)]
                image: Image{url: this.getClass().getResource("images/{n46[i]}.png").toString()}
                pressedImage: Image{url: this.getClass().getResource("images/d{n46[i]}.png").toString()}
                action: function():Void {input("{n46[i]}");}    
            }as Object) as Node,
            //foreach (i in [7,8,9])
            foreach(i in [0..sizeof n79 exclusive ])
            (CalcButton {
                transform: [Transform.translate(16+i *38, 153-38*2)]
                image: Image{url: this.getClass().getResource("images/{n79[i]}.png").toString()}
                pressedImage: Image{url: this.getClass().getResource("images/d{n79[i]}.png").toString()}
                action: function():Void {input("{n79[i]}");}
            }as Object) as Node,
            //foreach (i in ["0", "decimal", "c"])
            foreach(i in [0..sizeof dec exclusive ])
            (CalcButton {
                transform: [Transform.translate(16+i * 38, 191)]
                image: Image{url: this.getClass().getResource("images/{dec[i]}.png").toString()}
                pressedImage: Image{url: this.getClass().getResource("images/d{dec[i]}.png").toString()}
                action: function():Void {input(dec[i]);}    
            }as Object) as Node,
            //foreach (i in ["m+", "m-", "mc", "mr"])
            foreach(i in [0..sizeof mr exclusive ])
            (CalcButton {
                transform: [Transform.translate(16+i * 28, 52)]
                image: Image{url: this.getClass().getResource("images/{mr[i]}.png").toString()}
                pressedImage: Image{url: this.getClass().getResource("images/d{mr[i]}.png").toString()}
                action: function():Void {input(mr[i]);}
            }as Object) as Node,
            //foreach (i in ["div", "mult", "sub", "add"])
            foreach(i in [0..sizeof divmult exclusive ])
            (CalcButton {
                transform: [Transform.translate(130 + (if (i > 0) then 1 else 0), 52+27* i)]
                image: Image{url: bind this.getClass().getResource("images/{if (operator == ops[i]) then 'a{divmult[i]}' else divmult[i]}.png").toString()}
                pressedImage: Image{url: this.getClass().getResource("images/d{divmult[i]}.png").toString()}
                action: function():Void {input(ops[i]);}
            }as Object) as Node,
            (CalcButton {
                transform: [Transform.translate(131, 170)]
                image: Image{url: this.getClass().getResource("images/equal.png").toString()}
                pressedImage: Image{url: this.getClass().getResource("images/dequal.png").toString()}
                action: function():Void {input("=");}    
            }as Object) as Node]
        };
    }    
}



var canvas = Canvas {
    background: Color.rgba(0, 0, 0, 0)
    content: [ (Calculator {
        focused: true
    } as Object)as Node ]
};
//canvas;

Frame {
  title: "JavaFX Calculator"
  background: Color.WHITE
  onClose: function():Void { System.exit(0); }
  content: canvas
  visible: true
}      



