/*
 * ElectroSimulator.fx
 *
 * Created on Mar 27, 2008, 1:27:10 PM
 */

package jfx.assortis.samples.various.electrosym;

import javafx.ui.*;
import javafx.ui.canvas.*;

import java.lang.System;

import java.lang.Runnable;
import java.lang.Thread;


class Function{

  static function abs(x: Number):Number{
    return if ( x < 0 ) then -x else x;
  }

  static function min(x: Number, y: Number):Number{
    return if ( x < y ) then x else y;
  }
}


//  ============  Pin  ====================//

class Pin extends CompositeNode{
    attribute pos: XY;
    attribute level: Number;
    
    public function composeNode():Node{
      return Group{
        content: [ Circle{
            cx: bind pos.x
            cy: bind pos.y
            radius: 4
            fill: Color.BLUE
        }]
      };
    }

}


//  ============  ElectroScheme  ====================//

class ElectroScheme extends Runnable{
    attribute ticks: Number on replace{
	simulate();
    };

    attribute components: ElectronicComponent[];

    public function simulate(){
	System.out.println("[simulate]");
      (new Thread(this)).start();
    }
    
    public function run(){
       while(true){
          for(comp in components){
            comp.simulate();
          }
       }
    }

}

//  ============  ElectronicComponent  ====================//

abstract class ElectronicComponent extends CompositeNode{
    
    attribute name: String;
        
    attribute pos: XY;
    attribute compWidth:  Number = 20;
    attribute compHeight: Number = 15;
    
    attribute pin1: Pin;
    attribute pin2: Pin;
    
    abstract function simulate():Void;

    function switchLevels(){
      var level = pin1.level;
      pin1.level = pin2.level;
      pin2.level = level;
    }
    
}

//  ============  Wire ====================//


class Wire extends ElectronicComponent{
    init {
        name = "Wire";
        pin1 = Pin{};
        pin2 = Pin{};
    }

    public function composeNode():Node{
      return Group{
        content: [ Line{
            x1: bind pin1.pos.x
            y1: bind pin1.pos.y
            x2: bind pin2.pos.x
            y2: bind pin2.pos.y
            stroke: Color.GREEN
        }]
      };
    }

   function simulate():Void{
      switchLevels();
   }


}



//  ============  Lamp ====================//


class Lamp extends ElectronicComponent{

    attribute switchOn: Boolean = false;
    override attribute pin1 on replace oldPin=newPin {
       pin1.pos = XY{x: bind pos.x - compWidth / 2  y: bind pos.y + 2 * compHeight -5};
    }

    override attribute pin2 on replace oldPin=newPin {
      pin2.pos = XY{x: bind pos.x + compWidth / 2  y: bind pos.y + 2 * compHeight - 5};
    }

    init {
        name = "Lamp";
    }

    function simulate():Void{
      if( Function.abs( pin1.level - pin2.level) > 5 ){
        switchOn = true;
      }else{
        switchOn = false;
      } 
     switchLevels();
    }

    public function composeNode(): Node{
      var r = Function.min(compWidth, compHeight);
      var h = r / 2;
    
      return Group{
        content: [Rect{
            x: bind pos.x - h
            y: bind pos.y
            width: r
            height: 2* r
            fill: Color.SILVER
        }, Circle{
            cx: bind pos.x
            cy: bind pos.y
            radius: r
            fill: bind if (switchOn) then Color.RED else Color.GREY
        }, pin1, pin2 ]
        onMouseDragged: function (e: CanvasMouseEvent){
            pos.x += e.localDragTranslation.x;
            pos.y += e.localDragTranslation.y;
         }
      };
    }


}



//  ============  Battery ====================//


class Battery extends  ElectronicComponent {

    override attribute pin1 on replace oldPin=newPin {
       pin1.pos = XY {x: bind pos.x + compWidth - 5  y: bind pos.y - compHeight};
    }

    override attribute pin2 on replace oldPin=newPin {
      pin2.pos = XY{x: bind pos.x - compWidth + 5  y: bind pos.y - compHeight};
    }

    init {
        name = "Battery";
    }


    function simulate():Void{
      pin1.level =  3;
      pin2.level = -3;
    }

    public function composeNode():Node{
      var w = compWidth;
      var h = compHeight;
    
      return Group{
        content: [Rect{
            x: bind pos.x - w
            y: bind pos.y - h
            width:  2 * w
            height: 2 * h
            fill: Color.CHOCOLATE
            stroke: Color.BROWN
        },Line{
            x1: bind pos.x + w - 5 + 3
            y1: bind pos.y - h + 10
            x2: bind pos.x + w - 5 - 3
            y2: bind pos.y - h  + 10
            
            stroke: Color.DARKBLUE
            strokeWidth: 2
        },Line{
            x1: bind pos.x - w + 5 + 3
            y1: bind pos.y - h + 10
            x2: bind pos.x - w  + 5 - 3
            y2: bind pos.y - h  + 10
            
            stroke: Color.DARKBLUE
            strokeWidth: 2
        },Line{
            x1: bind pos.x - w + 5
            y1: bind pos.y - h + 10 + 3
            x2: bind pos.x - w  + 5
            y2: bind pos.y - h  + 10 -3
            
            stroke: Color.DARKBLUE
            strokeWidth: 2
        },pin1, pin2]
         onMouseDragged: function(e: CanvasMouseEvent){
            pos.x += e.localDragTranslation.x;
            pos.y += e.localDragTranslation.y;
         }
      };
    }

}


//  ============  Switch ====================//




class Switch extends ElectronicComponent{
    attribute switchOn: Boolean = false;
    override attribute pin1 on replace oldPin=newPin {
        pin1.pos = XY{x: bind pos.x + compWidth   y: bind pos.y };
    }
    override attribute pin2 on replace oldPin=newPin {
        pin2.pos = XY{x: bind pos.x - compWidth   y: bind pos.y };
    }

    init {
        name = "Switch";
    }

    function simulate():Void{
      if(switchOn){
        switchLevels();
      }
    }

    public function composeNode(): Node{
      return Group{
        content:  [ Circle{
            cx: bind pos.x
            cy: bind pos.y
            radius:  Function.min(compWidth, compHeight) 
            fill: Color.ORANGE
            stroke: Color.BROWN
            onMouseClicked: function(e){
                switchOn = not switchOn;
            }
            onMouseDragged: function(e){
                pos.x += e.localDragTranslation.x;
                pos.y += e.localDragTranslation.y;
            }            
        }, Group{ 
            content: bind if (switchOn) then Line{
              x1: bind pos.x - compWidth
              y1: bind pos.y
              x2: bind pos.x + compWidth
              y2: bind pos.y
              stroke: Color.BROWN
              strokeWidth: 2
          } else Line{
              x1: bind pos.x - compWidth / 1.7
              y1: bind pos.y + compHeight /1.7
              x2: bind pos.x + compWidth / 1.7
              y2: bind pos.y - compHeight /1.7
              stroke: Color.BROWN
              strokeWidth: 2
          } 
        } ,pin1, pin2]
        
      };
    }

}

var scheme = ElectroScheme{
    var wire1 = Wire{}
    var wire2 = Wire{}
    var wire3 = Wire{}
    
    components:[
    Lamp{
        pos: XY{ x: 160  y: 25}
        pin1: wire1.pin2
        pin2: wire3.pin1
    },
    Battery{
        pos: XY{ x: 100  y: 120}
        pin1: wire2.pin2
        pin2: wire1.pin1
    },Switch{
        pos: XY{ x: 180  y: 105}
        pin1: wire3.pin2
        pin2: wire2.pin1
        
    },
    
    wire1, wire2, wire3 ]
};

//scheme.simulate();

Frame{
    title: "Simple Electro Simulator"
    width: 300
    height: 200
    onClose: function(){ System.exit(0); }
    content: Canvas{
        content: [ scheme.components ]
    }
    visible: true    
}