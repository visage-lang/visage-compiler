package calc;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.text.DecimalFormat;
import java.lang.System;

class CalcButton extends CompositeNode {
    attribute name: String;
    attribute pressedImage: Image = Image{url: this.getClass().getResource('images/d{name}.png').toString()};
    attribute image: Image = Image{url: this.getClass().getResource('images/{name}.png').toString()};
    attribute pressed: Boolean;
    attribute action: function():Void;
    override attribute isSelectionRoot = true;
    
    public function composeNode(): Node {
        ImageView {
            onMousePressed: function(e:CanvasMouseEvent):Void  {
                pressed = true
            }
            onMouseReleased:  function(e:CanvasMouseEvent):Void {
                pressed = false; 
                if (hover and action <> null) {
                    action()
                }
            }
            image: bind if (hover and pressed) then pressedImage else image
        }
    }
}

public class Calculator extends CompositeNode {
    attribute register: Number;
    attribute mem: Number;
    attribute operator: String;
    attribute isFixReg = true;
    attribute text = '0';

    override attribute focusable = true;
    override attribute onKeyDown = function(e:KeyEvent):Void {
            if (e.keyStroke == KeyStroke.ENTER) {
                input('=')
            } else if (e.keyStroke == KeyStroke.BACK_SPACE) {        
                input('del')
            }
        }
    override attribute onKeyTyped = function(e:KeyEvent):Void {
            input(e.keyChar)
        }      

    function calculate(op:String, r1:Number, r2:Number):Number {
        if (op == '+')
            then r1 + r2
        else if (op == '-') 
            then r1 - r2
        else if (op == '*')
            then r1 * r2
        else if (op == '/')
            then r1 / r2
        else r2
    }

    function update() {
        var current = df.parse(text).doubleValue();
        register = calculate(operator, register, current);
        operator = '';
        text = formatNum(register);
        isFixReg = true;
    }

    private attribute df:DecimalFormat = new DecimalFormat('###,###,###,###.########');
    function formatNum(n:Number):String { 
        return df.format(n);
    }

    function input(value:String):Void {
        //System.out.println("input: {value} reg={register} text={text} mem={mem}");
        if (value == 'c') {
            register = 0.0;
            operator = '';
            text = '0';
            isFixReg = true;
        } else if (value == 'del') {
            if (text.length() > 1) {
                text = text.substring(0, text.length()-1);
            } else {
                text = '0';
                isFixReg = true;
            }
        //} else if (value in (select '{i}' from i in [0..9])) {
        } else if (value == '0' or value == '1' or value == '2' or value == '3' or
                   value == '4' or value == '5' or value == '6' or
                   value == '7' or value == '8' or value == '9') {
            if (isFixReg) {
                text = '{value}';
            } else {
                text = '{text}{value}';
            }
            isFixReg = false;
        } else if (value == '.' or value == 'decimal') {
            if (isFixReg) {
                text = '0.';
            } else if (text.indexOf('.') < 0) {
                text = '{text}{value}';
            }
            isFixReg = false;
        } else if (value == '+' or value == '-' or value == '*' or value == '/' or value == '=') {
            update();
            operator = value;
        } else if (value == 'mr') {
            text = formatNum(mem);
            operator = '';
            isFixReg = true;
        } else if (value == 'm+') {
            update();
            mem += register;
        } else if (value == 'm-') {
            update();
            mem -= register;
        } else if (value == 'mc') {
            mem = 0;
        }    
    }

    public function composeNode(): Node {
        var bottom = 191;
        var left = 16;
        var sz = 38;
        var ssz = 28;
        Group {
            content:
            [ImageView {
                image: Image{url: this.getClass().getResource('images/Calculator.png').toString()}
            },
            ImageView { 
                transform: [Transform.translate(13, 8)]
                image: Image{url: this.getClass().getResource('images/lcd-backlight.png').toString()}
            },
            Text {
                transform: [Transform.translate(150, 18)]
                halign: HorizontalAlignment.TRAILING
                font: Font {face: FontFace.ARIAL size: 20}
                content: bind text
                fill: Color.WHITE
            },
            for(key in ['0', 'decimal', 'c',  '1','2','3',  '4','5','6',  '7','8','9']) {
                var row = indexof key / 3;
                var col = indexof key % 3;
                CalcButton {
                    transform: [Transform.translate(left + col * sz, bottom - sz * row)]
                    name: key
                    action: function():Void {input(key)}
                }
            },
            for(key in ['m+', 'm-', 'mc', 'mr']) {
                var col = indexof key;
                CalcButton {
                    transform: [Transform.translate(left + col * ssz, 52)]
                    name: key
                    action: function():Void {input(key)}
                }
            },
            {
                var divmult = ['div', 'mult', 'sub', 'add'];
                var ops = ['/', '*', '-', '+'];
                for(i in [0..<sizeof divmult])
                    CalcButton {
                        transform: [Transform.translate(131, 52 + 27 * i)]
                        name: divmult[i]
                        action: function():Void {input( ops[i] );}
                }
            },
            CalcButton {
                transform: [Transform.translate(131, 170)]
                name: 'equal'
                action: function():Void {input('=');}    
            }]
        };
    } 
}



var canvas = Canvas {
    background: Color.rgba(0, 0, 0, 0)
    content: [Calculator {
        focused: true
    }]
}

Frame {
  title: 'JavaFX Calculator'
  background: Color.WHITE
  onClose: function():Void { System.exit(0); }
  content: canvas
  visible: true
}      



