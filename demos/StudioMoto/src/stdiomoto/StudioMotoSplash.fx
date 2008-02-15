package studiomoto;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;


class StudioMotoSplash extends CompositeNode {
    attribute motoX: Number;
    attribute studioX: Number;
    attribute phoneY: Number;
    attribute alpha: Number;
    attribute backgroundAlpha: Number;
    operation doSplash();
    attribute onDone: function();
    attribute anim: KeyFrameAnimation;
}

attribute StudioMotoSplash.backgroundAlpha = 1;
attribute StudioMotoSplash.studioX = 50;
attribute StudioMotoSplash.motoX = -40;

attribute StudioMotoSplash.phoneY = -120;

trigger on StudioMotoSplash.backgroundAlpha = newValue {
    if (newValue < 0.01) {(this.onDone)();}
}

attribute StudioMotoSplash.anim = KeyFrameAnimation {
	  keyFrames:
	  [at (0s) {
	      alpha => 0;
	      backgroundAlpha => 1;
	      phoneY => -120;
	      studioX => 50;
	      motoX => -40;
	  },
	  after (1s) {
	      alpha => 1 tween LINEAR;
	      phoneY => -120;
	  },
	  after (.5s) {
                 phoneY => 200 tween EASEBOTH;
                 studioX => 50;
	         motoX => -40;
          },
	  after (.25s) {
	  	studioX => -20 tween EASEBOTH;
	  	motoX => 10 tween EASEBOTH;
	  },
	  after (.25s) {
	  	studioX => 0 tween EASEBOTH;
	  	motoX => 0 tween EASEBOTH;
	  },
          after (1.4s) {
	  	 motoX => 0;
	  	 studioX => 0;
	  },
          after (.25s) {
	  	 motoX => 20 tween LINEAR;
	  	 studioX => -20 tween LINEAR;
	  },
          after (.25s) {
	  	 motoX => -40 tween LINEAR;
	  	 studioX => 50 tween LINEAR;
		 alpha => 1;
		 backgroundAlpha => 1;
	  },
	  after (.5s) {
		 alpha => 0 tween EASEBOTH;
		 backgroundAlpha => 0 tween EASEBOTH;
	  }]
};

operation StudioMotoSplash.doSplash() {
     anim.start();
}

function StudioMotoSplash.composeNode() =
Group {
    content:
    [Rect {
        onMouseClicked: operation(e) {doSplash();}
        opacity: bind backgroundAlpha
        width: 1100
        height: 800
        fill: black
    },
    Clip {
        opacity: bind alpha
        transform: translate(1100/2, 800/2)
        valign: MIDDLE, halign: CENTER
        shape: Rect {width: 636, height: 651}
        content:
        [Group {
            content:  
            [ImageView {
                image: {url: "{__DIR__}/Image/2.jpg"}
            },
            ImageView {
                transform: bind translate(636/2, phoneY)
                image: {url: "{__DIR__}/Image/3.png"}
                halign: CENTER
            }]
        },
        Group {
            transform: translate(536/2, 631/2+5)
            opacity: bind alpha
            var font = new Font("ARIAL", "BOLD", 16)
            content:
            [HBox {
                content:
                
                [Clip {
                    shape: Rect {x: -50, width: 100, height: 15}
                    content:
                    Text {
                        transform: bind translate(studioX, 0)
                        font: font
                        content: "studio"
                        fill: yellow
                    }
                },
                Clip {
                    shape: Rect {width: 100, height: 15}
                    content:
                    Text {
                        transform: bind translate(motoX, 0)
                        font: font
                        content: "moto"
                        fill: white
                    }
                }]
            }]
        }]
    }]
};

operation testSplash() {
    var s = StudioMotoSplash {};
    s.doSplash();
    return s;
}
