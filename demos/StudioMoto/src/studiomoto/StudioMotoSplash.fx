package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;
import javafx.ui.animation.*;
import com.sun.javafx.runtime.PointerFactory;
import com.sun.javafx.runtime.Pointer;


class StudioMotoSplash extends CompositeNode {
    attribute motoX: Number = -40;
    attribute studioX: Number = 50;
    attribute phoneY: Number = -120;
    attribute alpha: Number on replace {
        if (alpha < 0.01) { if(onDone <> null) onDone();}
    }
    attribute backgroundAlpha: Number = 1;
    function doSplash() {
         anim.start();
    }
    attribute onDone: function();
    
    attribute pf: PointerFactory = PointerFactory{};
    attribute anim: Timeline= Timeline {
          var _alpha = pf.make(alpha).unwrap();
          var _backgroundAlpha = pf.make(backgroundAlpha).unwrap();
          var _phoneY = pf.make(phoneY).unwrap();
          var _motoX = pf.make(motoX).unwrap(); 
          var _studioX = pf.make(studioX).unwrap(); 
	  keyFrames:
	  [ KeyFrame {
                keyTime: 0s
                keyValues:  [
                    NumberValue {
                        target: _alpha
                        value: 0  
                    },
                   NumberValue {
                        target: _backgroundAlpha
                        value: 1  
                    },
                   NumberValue {
                        target: _phoneY
                        value: -120 
                    },
                   NumberValue {
                        target: _studioX
                        value: 50 
                    },
                   NumberValue {
                        target: _motoX
                        value: -40 
                    }
                ]
	  },
	  KeyFrame {
              keyTime: 1s
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _alpha
                        value: 1  
                        interpolate: NumberValue.LINEAR
                    },
                   NumberValue {
                        target: _phoneY
                        value: -120  
                    }
              ]
	  },
	  KeyFrame {
              keyTime: 500ms
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _phoneY
                        value: 200  
                        interpolate: NumberValue.EASEBOTH
                    },
                   NumberValue {
                        target: _studioX
                        value: 50 
                    },
                   NumberValue {
                        target: _motoX
                        value: -40 
                    }                    
              ]              
          },
	  KeyFrame {
              keyTime: 250ms
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _studioX
                        value: -20
                        interpolate: NumberValue.EASEBOTH
                    },
                   NumberValue {
                        target: _motoX
                        value: 20
                        interpolate: NumberValue.EASEBOTH
                    }                   
              ]               
	  },
	  KeyFrame {
              keyTime: 250ms
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _studioX
                        value: 0
                        interpolate: NumberValue.EASEBOTH
                    },
                   NumberValue {
                        target: _motoX
                        value: 0
                        interpolate: NumberValue.EASEBOTH
                    }                   
              ]                 
	  },
	  KeyFrame {
              keyTime: 1400ms
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _studioX
                        value: 0
                    },
                   NumberValue {
                        target: _motoX
                        value: 0
                    }                   
              ]               
	  },
 	  KeyFrame {
              keyTime: 250ms
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _studioX
                        value: -20
                        interpolate: NumberValue.LINEAR
                    },
                   NumberValue {
                        target: _motoX
                        value: 20
                        interpolate: NumberValue.LINEAR
                    }                   
              ]               
	  },
	  KeyFrame {
              keyTime: 250ms
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _studioX
                        value: 50
                        interpolate: NumberValue.LINEAR
                    },
                   NumberValue {
                        target: _motoX
                        value: -40
                        interpolate: NumberValue.LINEAR
                    },    
                   NumberValue {
                        target: _alpha
                        value: -1
                    },
                   NumberValue {
                        target: _backgroundAlpha
                        value: 1
                    }                    
              ]               
	  },
	  KeyFrame {
              keyTime: 500ms
              relative: true
              keyValues:  [
                    NumberValue {
                        target: _alpha
                        value: 0
                        interpolate: NumberValue.EASEBOTH
                    },
                   NumberValue {
                        target: _backgroundAlpha
                        value: 0
                        interpolate: NumberValue.EASEBOTH
                    }                   
              ]               
	  }]
      };

    
    function composeNode():Node {
        Group {
            content:
            [Rect {
                onMouseClicked: function(e) {doSplash();}
                opacity: bind backgroundAlpha
                width: 1100
                height: 800
                fill: Color.BLACK
            },
            Clip {
                opacity: bind alpha
                transform: Transform.translate(1100/2, 800/2)
                valign: VerticalAlignment.MIDDLE, halign: HorizontalAlignment.CENTER
                shape: Rect {width: 636, height: 651}
                content:
                [Group {
                    content:  
                    [ImageView {
                        image: Image{url: "{__DIR__}/Image/2.jpg"}
                    },
                    ImageView {
                        transform: bind Transform.translate(636/2, phoneY)
                        image: Image{url: "{__DIR__}/Image/3.png"}
                        halign: HorizontalAlignment.CENTER
                    }]
                },
                Group {
                    transform: Transform.translate(536/2, 631/2+5)
                    opacity: bind alpha
                    var font = Font.Font("ARIAL", ["BOLD"], 16)
                    content:
                    [HBox {
                        content:

                        [Clip {
                            shape: Rect {x: -50, width: 100, height: 15}
                            content:
                            Text {
                                transform: bind Transform.translate(studioX, 0)
                                font: font
                                content: "studio"
                                fill: Color.YELLOW
                            }
                        },
                        Clip {
                            shape: Rect {width: 100, height: 15}
                            content:
                            Text {
                                transform: bind Transform.translate(motoX, 0)
                                font: font
                                content: "moto"
                                fill: Color.WHITE
                            }
                        }]
                    }]
                }]
            }]
        };

    }
}


function testSplash():StudioMotoSplash {
    var s = StudioMotoSplash {};
    s.doSplash();
    return s;
}
