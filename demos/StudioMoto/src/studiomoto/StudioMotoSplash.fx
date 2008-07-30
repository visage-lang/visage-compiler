package studiomoto;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.scene.geometry.*;
import javafx.scene.transform.*;
import javafx.scene.paint.*;
import javafx.scene.image.*;
import javafx.scene.text.*;
import java.lang.Math;
import javafx.animation.*;


class StudioMotoSplash extends CustomNode {
    attribute motoX: Number = -40;
    attribute studioX: Number = 50;
    attribute phoneY: Number = -120;
    attribute alpha: Number on replace {
        if (alpha < 0.01) { if(onDone != null) onDone();}
    }
    attribute backgroundAlpha: Number = 1;
    function doSplash() {
         anim.start();
    }
    attribute onDone: function();
    
    attribute anim: Timeline= Timeline {
 
	  keyFrames:
	  [ KeyFrame {
                time: 0s
                values: [
                    alpha => 0,
                    backgroundAlpha => 1,
                    phoneY => -120,
                    studioX => 50,
                    motoX => -40
                ]
	  },
	  KeyFrame {
              time: 1s
              values: [
                    alpha => 1 tween Interpolator.LINEAR,
                    phoneY => -120,
              ]              
	  },
	  KeyFrame {
              time: 1500ms
              values: [
                    phoneY => 200 tween Interpolator.EASEBOTH,
                    studioX => 50,
                    motoX => -40
              ]              
          },
	  KeyFrame {
              time: 1750ms
              values: [
                    studioX => -20 tween Interpolator.EASEBOTH,
                    motoX => -20 tween Interpolator.EASEBOTH,
              ]               
	  },
	  KeyFrame {
              time: 2000ms
              values: [
                    studioX => 0 tween Interpolator.EASEBOTH,
                    motoX => 0 tween Interpolator.EASEBOTH,
              ]              
	  },
	  KeyFrame { // TODO check this one
              time: 3400ms
              values: [
                    studioX => 0 tween Interpolator.EASEBOTH,
                    motoX => 0 tween Interpolator.EASEBOTH,
              ]                  
	  },
 	  KeyFrame {
              time: 3650ms
              values: [
                    studioX => -20 tween Interpolator.EASEBOTH,
                    motoX => 20 tween Interpolator.EASEBOTH,
              ]               
	  },
	  KeyFrame {
              time: 3900ms
              values: [
                    studioX => 50 tween Interpolator.LINEAR,
                    motoX => -40 tween Interpolator.LINEAR,
                    alpha => 1 ,
                    backgroundAlpha => 1 ,
              ]               
	  },
	  KeyFrame {
              time: 4400ms
              values: [
                    alpha => 0 tween Interpolator.EASEBOTH,
                    backgroundAlpha => 0 tween Interpolator.EASEBOTH,
              ]                
	  }]
      };

    
    override function create():Node {
        Group {
            content:
            [Rectangle {
                onMouseClicked: function(e) {doSplash();}
                opacity: bind backgroundAlpha
                width: 1100
                height: 800
                fill: Color.BLACK
            },
            Group {
                transform: Transform.translate(1100/2, 800/2)
                verticalAlignment: VerticalAlignment.CENTER, 
                horizontalAlignment: HorizontalAlignment.CENTER
                clip: Rectangle {width: 636, height: 651}
                opacity: bind alpha
                content:
                [Group {
                    content:  
                    [ImageView {
                        image: Image{url: "{__DIR__}Image/2.jpg"}
                    },
                    ImageView {
                        transform: bind Transform.translate(636/2, phoneY)
                        image: Image{url: "{__DIR__}Image/3.png"}
                        horizontalAlignment: HorizontalAlignment.CENTER
                    }]
                },
                Group {
                    var font = Font.font("ARIAL", FontStyle.BOLD, 16);
                    transform: Transform.translate(536/2, 631/2+5)
                    opacity: bind alpha
                    content:
                    [HBox {
                        content:
                        [Group {
                            clip: Rectangle {x: -50, width: 100, height: 15}
                            content:
                            Text {
                                textOrigin: TextOrigin.TOP
                                transform: bind Transform.translate(studioX, 0)
                                font: font
                                content: "studio"
                                fill: Color.YELLOW
                            }
                        },
                        Group {
                            clip: Rectangle {width: 100, height: 15}
                            content:
                            Text {
                                textOrigin: TextOrigin.TOP
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

