/*
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package mp3;
import javafx.ui.*;
import javafx.ui.canvas.*;
import java.lang.Math;
import javazoom.jlgui.basicplayer.*;
import java.util.Map;
import java.io.File;
import java.net.URL;
import java.awt.Point;
import java.lang.System;

public class ButtonView extends CompositeNode {
     
    var isPressed: Boolean;
    public var enabled: Boolean = true;
    public var pressed: Node;
    public var normal: Node;
    public var action: function();
    
    function composeNode(): Node {
        Group {
            isSelectionRoot: true
            content: bind if (isPressed) then pressed else normal
            onMousePressed: function(e) {
                if (enabled) {
                    isPressed = true;
                }
            }
            onMouseReleased: function(e) {
                if (enabled) {
                    if (isPressed) {
                        (this.action)();
                        isPressed = false;
                    } 
                }
            }
            onMouseDragged: function(e:CanvasMouseEvent) {
               if (enabled) {
                   isPressed = this.getGlobalBounds().contains(e.x, e.y);
               }
            }
        }
    };
}

public class SliderView extends CompositeNode {

    var isPressed: Boolean;
    public var enabled: Boolean;
    public var pressed: Node;
    public var normal: Node;
    public var slide: function(dx:Number, dy:Number);

    function composeNode(): Node {
        Group {
            // TODO JFXC-782 Fix this as soon as issue is resolved
//            content: bind if (isPressed) then pressed else normal
            content: normal
            onMousePressed: function(e) {
                isPressed = true;
            }
            onMouseReleased: function(e) {
                isPressed = false;
            }
            onMouseDragged: function(e:CanvasMouseEvent) {
                 (this.slide)(e.localDragTranslation.x, e.localDragTranslation.y);
            }
        }
    };
}

public class Main extends CompositeNode {
    var skinFileChooser: FileChooser;
    var fileChooser: FileChooser;
    var skinUrl: String = bind (skinUrls[selectedSkinUrl]);
    var skinUrls: String[];
    var selectedSkinUrl: Integer;
    var backgroundImage: Image = bind
        Image {url: "{skinUrl}/main.bmp"};
    var titleBarImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "TitleBar.bmp")};
    var playPauseImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "PlayPaus.bmp")};
    var posbarImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "PosBar.bmp")};
    var monosterImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "MonoSter.bmp")};
    var shufrepImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "ShufRep.bmp")};
    var numbersImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "Numbers.bmp")};
    var textImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "Text.bmp")};
    var volumeImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "Volume.bmp")};
    var equalizerImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "EqMain.bmp")};
    var songUrl: String;
    
    var volumeX: Number = 54/2 
        on replace {
            volume = (volumeX/54 * 15).intValue();
            gain = volumeX / 54;
        }
    ;

    var player: BasicPlayer = new BasicPlayer()
        on replace {
            var self = this;
            controller = player;
            playerListener = BasicPlayerListenerAdapter {
                function setController(controller:BasicController): Void {
                   //TODO DO LATER - this is a work around until a more permanent solution is provided
                    javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                        public function run():Void {
                            self.controller = controller;
                        }
                    });
                };
                function openedJFX(stream, streamProperties:Map, framerate: Number, samplerate: Number, duration: Integer): Void {
                   //TODO DO LATER - this is a work around until a more permanent solution is provided
                    javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                        public function run():Void {
                            self.elapsedMinutes = 0;
                            self.elapsedSeconds = 0;
                            self.elapsedMicroseconds = 0;
                            var bitrate = streamProperties.get("bitrate") as Integer;
                            // TODO AMBIGUITY - this is a work around until ambiguities are resolved
                            self.bitRate = Math.round(bitrate.doubleValue() / 1000) as Integer;
                            // TODO FLOAT-PARAMETER - this is a work around until float-parameters are supported
//                            var fr = streamProperties.get("audio.framerate.fps") as Number;
                            var fs = streamProperties.get("audio.framesize.bytes") as Integer;
                            // TODO AMBIGUITY - this is a work around until ambiguities are resolved
                            self.frameRate = frameRate * fs.doubleValue() * 8;
                            var channels = streamProperties.get("audio.channels") as Integer;
                            // TODO FLOAT-PARAMETER - this is a work around until float-parameters are supported
//                            var samplerate = streamProperties.get("audio.samplerate.hz") as Number;
                            self.sampleRate = Math.round(samplerate / 1000) as Integer;
                            // TODO LONG-PARAMETER - this is a work around until float-parameters are supported
//                            var duration = streamProperties.get("duration") as Integer;
                            self.duration = duration;
                            var length = streamProperties.get("audio.length.bytes") as Integer;
                        }
                    });
                };
                //TODO progressJFX - this is a work around until byte[] can be used as parameter
                public function progressJFX(bytesRead: Integer, 
                                         microseconds, // long
                                         streamProperties: Map): Void {
                    //TODO DO LATER - this is a work around until a more permanent solution is provided
                    javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                        public function run():Void {
                            if (self.playing) {
                                self.elapsedMicroseconds = microseconds as Integer;
                                var seconds: Integer = self.elapsedMicroseconds / 1000000;
                                self.elapsedMinutes = seconds / 60;
                                self.elapsedSeconds = seconds % 60;
                                var bitrate = streamProperties.get("bitrate") as Number;
                                if (bitrate != null) {
                                      self.bitRate = Math.round(bitrate/1000) as Integer;
                                }
                                var fr = streamProperties.get("audio.framerate.fps") as Number;
                                var fs = streamProperties.get("audio.framesize.bytes") as Number;
                                if (fr != null) {
                                    self.frameRate = Math.round(fs *fr *8);
                                }
                                var samplerate = streamProperties.get("audio.samplerate.hz") as Number;
                                if (samplerate != null) {
                                    self.sampleRate = Math.round(samplerate/1000) as Integer;
                                }
                            } else {
                                self.elapsedMicroseconds = 0;
                                self.elapsedSeconds = 0;
                                self.elapsedMinutes = 0;
                            }
                        }
                    });
                };
                function stateUpdated(e:BasicPlayerEvent): Void {
                };
            };
            player.addBasicPlayerListener(playerListener);
        };
    var playerListener: BasicPlayerListener;
    var controller: BasicController;

    var playing: Boolean
        on replace {
            if (playing) {
                controller.play();
                player.setGain(gain);
            } else {
                controller.stop();
                elapsedMicroseconds = 0;
                elapsedSeconds = 0;
                elapsedMinutes = 0;
            }
        }
    ;
    
    var paused: Boolean
        on replace {
            if (paused) {
                controller.pause();
            } else {
                controller.resume();
            }
        }
    ;

    var working: Boolean;
    var stereo: Boolean = true;
    var shuffle: Boolean;
    var repeat: Boolean;
    var volume: Integer;
    var gain: Number
        on replace {
            if (playing) {
                player.setGain(gain);
            }
        }
    ;
    var duration: Number; // song duration in microseconds

    var frameRate: Number;
    var bitRate: Integer;
    var sampleRate: Integer;

    var moveFrame: function(dx:Number, dy:Number);

    var pos: Number = bind if (duration == 0) then 0 else elapsedMicroseconds/duration;

    var elapsedMicroseconds: Integer;
    var elapsedMinutes: Integer;
    var elapsedSeconds: Integer;

    function back() {
        var i = selectedSkinUrl - 1;
        if (i < 0) {
            i = sizeof skinUrls -1;
        }
        selectedSkinUrl = i;
    }
    
    function forward() {
        var i = selectedSkinUrl + 1;
        if (i >= sizeof skinUrls) {
            i = 0;
        }
        selectedSkinUrl = i;
    }
    
    function play() {
        playing = true;
        paused = false;
          // TODO DO  - this is a work around until a more permanent solution is provided
//        do {
            controller.play();
//        }
    }
    
    function pause() {
        paused = true;
    };
    
    function stop() {
        playing = false;
        paused = false;
    };
    
    function load() {
        if (fileChooser == null) {
            fileChooser = FileChooser {
               title: "Select mp3"
               // TODO FILEFILTERS filefilters are currently not supported by FileChooser
//               fileFilters: FileFilter {
//                    filter: function(f:File) {
//                        return f.isDirectory() or f.getName().endsWith(".mp3");
//                    }
//                    description: "MP3 Files (*.mp3)"        
//               }
               action: function(f:File) {
                    songUrl = f.toURL().toString();
                      // TODO DO  - this is a work around until a more permanent solution is provided
//                    do {
                       controller.open(f);
//                    }
               }
            };
        }
        fileChooser.showOpenDialog(this.getCanvas());
    };


    function loadSkin() {
        if (skinFileChooser == null) {
            skinFileChooser = FileChooser {
               title: "Select skin"
               // TODO FILEFILTERS filefilters are currently not supported by FileChooser
//               fileFilters: FileFilter {
//                    filter: function(f:File) {
//                        return f.isDirectory() or f.getName().endsWith(".wsz");
//                    }
//                    description: "WinAmp Skin Files (*.wsz)"        
//               }
               action: function(f:File) {
                    var url = "jar:{f.toURI().toString()}!";
                    var list = for (x in skinUrls where x == url) indexof x;
                    if (sizeof list > 0) { 
                        selectedSkinUrl = list[0];
                    } else {
                        insert url after skinUrls[selectedSkinUrl];
                        selectedSkinUrl++;
                    }
               }
            };
        }
        skinFileChooser.showOpenDialog(this.getCanvas());
    };
    
    function getImageURL(baseURL:String, imageFile:String): String {
        try {
            var url = new URL("{baseURL}/{imageFile}");
            var is = url.openStream();
            if (is != null) {
                return "{baseURL}/{imageFile}";
            }
        } catch (e) {
        }
        return "{baseURL}/{imageFile.toLowerCase()}";
    }

    var controlButtonsImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "CButtons.bmp")};
        
    var controlButtonsNormal: Node[] = 
        for (i in [0..4])
            Clip {
                antialias: true
                transform: Translate {x: -i*23, y: 0}
                shape: Rect {x: i*23, y: 0, height: 18, width: if (i == 4) then 22 else 23}
                content: ImageView {image: bind controlButtonsImage}
            }
    ;
    
    var controlButtonsPressed: Node[] = 
        for (i in [0..4])
            Clip {
                antialias: true
                transform: Translate {x: -i*23, y: -18}
                shape: Rect {x: i*23, y: 18, height: 18, width: if (i == 4) then 22 else 23}
                content: bind ImageView {image: bind controlButtonsImage}
            }
    ;
    
    var loadButtonNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: -114, y: 0}
            shape: Rect {x: 114, y: 0, height: 16, width: 22}
            content: ImageView {image: bind controlButtonsImage}
        }
    ;

    var loadButtonPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: -114, y: -16}
            shape: Rect {x: 114, y: 16, height: 16, width: 22}
            content: ImageView {image: bind controlButtonsImage}
        }
    ;


    var stereoOn: Node = 
        Clip {
            shape: Rect {x: 0, y: 0, height: 12, width: 29}
            content: ImageView {image: bind monosterImage}
        }
    ;

    var stereoOff: Node = 
        Clip {
            transform: Translate {x: 0, y: -12}
            shape: Rect {x: 0, y: 12, height: 12, width: 29}
            content: ImageView {image: bind monosterImage}
        }
    ;

    var monoOn: Node = 
        Clip {
            transform: Translate {x: -29, y: 0}
            shape: Rect {x: 29, y: 0, height: 12, width: 27}
            content: ImageView {image: bind monosterImage}
        }
    ;

    var monoOff: Node = 
        Clip {
            transform: Translate {x: -29, y: -12}
            shape: Rect {x: 29, y: 12, height: 12, width: 27}
            content: ImageView {image: bind monosterImage}
        }
    ;

    var shuffleOnNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: -30}
            shape: Rect {x: 28, y: 30, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    var shuffleOnPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: -45}
            shape: Rect {x: 28, y: 45, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    var shuffleOffNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: 0}
            shape: Rect {x: 28, y: 0, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    var shuffleOffPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: -15}
            shape: Rect {x: 28, y: 15, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    var repeatOnNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: -30}
            shape: Rect {x: 0, y: 30, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    var repeatOnPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: -45}
            shape: Rect {x: 0, y: 45, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    var repeatOffNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: 0}
            shape: Rect {x: 0, y: 0, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    var repeatOffPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: -15}
            shape: Rect {x: 0, y: 15, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;


    var backButton: ButtonView = 
        ButtonView {
            toolTipText: "Previous"
            normal: bind controlButtonsNormal[0]
            pressed: bind controlButtonsPressed[0]
            action: function() {this.back();}
        }
    ;

    var playButton: ButtonView =
        ButtonView {
            enabled: bind songUrl != null
            toolTipText: "Play"
            normal: bind controlButtonsNormal[1]
            pressed: bind controlButtonsPressed[1]
            action: function() {this.play();}
        }
    ;
    
    var pauseButton: ButtonView = 
        ButtonView {
            enabled: bind songUrl != null
            toolTipText: "Pause"
            normal: bind controlButtonsNormal[2]
            pressed: bind controlButtonsPressed[2]
            action: function() {this.pause();}
        }
    ;

    var stopButton: ButtonView = 
        ButtonView {
            enabled: bind songUrl != null
            toolTipText: "Stop"
            normal: bind controlButtonsNormal[3]
            pressed: bind controlButtonsPressed[3]
            action: function() {this.stop();}
        }
    ;

    var forwardButton: ButtonView = 
        ButtonView {
            toolTipText: "Next"
            normal: bind controlButtonsNormal[4]
            pressed: bind controlButtonsPressed[4]
            action: function() {this.forward();}
        }
    ;

    var loadButton: ButtonView = 
        ButtonView {
            toolTipText: "Load/Eject"
            normal: bind loadButtonNormal
            pressed: bind loadButtonPressed
            action: function() {this.load();}
        }
    ;

    var volumeSlider: SliderView = 
        SliderView {
            toolTipText: "Volume"
            pressed: bind volumeSliderPressed
            normal: bind volumeSliderNormal
            slide: function(dx: Number, dy: Number) {
                var x = volumeX + dx;
                if (x < 0) {
                   x = 0;
                }
                if (x > 54) {
                   x = 54;
                }
                volumeX  = x;
            }
        }
    ;

    var shuffleOnButton: ButtonView = 
        ButtonView {
            visible: bind shuffle
            normal: bind shuffleOnNormal
            pressed: bind shuffleOnPressed
            action: function() {shuffle = false;}
        }
    ;

    var shuffleOffButton: ButtonView = 
        ButtonView {
            visible: bind not shuffle
            normal: bind shuffleOffNormal
            pressed: bind shuffleOffPressed
            action: function() {shuffle = true;}
        }
    ;

    var repeatOnButton: ButtonView = 
        ButtonView {
            visible: bind repeat
            normal: bind repeatOnNormal
            pressed: bind repeatOnPressed
            action: function() {repeat = false;}
        }
    ;

    var repeatOffButton: ButtonView = 
        ButtonView {
            visible: bind not repeat
            normal: bind repeatOffNormal
            pressed: bind repeatOffPressed
            action: function() {repeat = true;}
        }
    ;

    var titleBarFocused: Node = 
        Clip {
            transform: Translate {x: -27, y: 0}
            shape: Rect {x: 27, y: 0, height: 14, width: 275}
            content: ImageView {image: bind titleBarImage}
        }
    ;
    
    var titleBarUnfocused: Node = 
        Clip {
            transform: Translate {x: -27, y: -15}
            shape: Rect {x: 27, y: 15, height: 14, width: 275}
            content: ImageView {image: bind titleBarImage}
        }
    ;
    
    var closeButtonNormal: Node =
        Clip {
            selectable: true
            transform: Translate {x: -18, y: 0}
            shape: Rect {x: 18, y: 0, height: 9, width: 9}
            content: ImageView {image: bind titleBarImage}
        }
    ;

    var closeButtonPressed: Node = 
        Clip {
            selectable: true
            transform: Translate {x: -18, y: -9}
            shape: Rect {x: 18, y: 9, height: 9, width: 9}
            content: ImageView {image: bind titleBarImage}
        }
    ;
    
    var playIndicator: Node = 
        Clip {
            transform: Translate {x: 0, y: 0}
            shape: Rect {x: 0, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;
    
    var pauseIndicator: Node = 
        Clip {
            transform: Translate {x: -9, y: 0}
            shape: Rect {x: 9, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    var stopIndicator: Node = 
        Clip {
            transform: Translate {x: -18, y: 0}
            shape: Rect {x: 18, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    var workIndicatorOn: Node = 
        Clip {
            transform: Translate {x: -39, y: 0}
            shape: Rect {x: 39, y: 0, height: 9, width: 3}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    var workIndicatorOff: Node;
    var playPauseBackground: Node = 
        Clip {
            transform: Translate {x: -27, y: 0}
            shape: Rect {x: 27, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    var posbarSlideBar: Node = 
        Clip {
            antialias: true
            shape: Rect {x: 0, y: 0, height: 9, width: 248}
            content: ImageView {image: bind posbarImage}
        }
    ;

    var posbarSlider: Node = 
        Clip {
            antialias: true
            transform: Translate {x: -248, y: 0}
            shape: Rect {x: 248, y: 0, height: 9, width: 29}
            content: ImageView {image: bind posbarImage}
        }
    ;

    var posbarSliderPushed: Node = 
        Clip {
            transform: Translate {x: -278, y: 0}
            shape: Rect {x: 278, y: 0, height: 9, width: 29}
            content: ImageView {image: bind posbarImage}
        }
    ;

    // TODO CANVASIMAGE - refactor as soon as CanvasImage.getImage() is implemented
    //                    (requires com.sun.scenario.scenegraph.JSGPanel.toIcon())
//    var numbers: Image[] = 
//        for (i in [0..9])
//            CanvasImage {
//                content: Clip {
//                    transform: Translate {x: -i*9, y: 0}
//                    shape: Rect {x: i*9, y: 0, height: 13, width: 9}
//                    content: ImageView {image: bind numbersImage}
//                }
//            }
//    ;
    var minDigitNode1: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    var minDigitNode2: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    var secDigitNode1: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    var secDigitNode2: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    
    var volumeViews: Node[] = 
        for (n in [0..15])
            Clip {
                transform: Translate {x: 0, y: -n*15}
                shape: Rect {x: 0, y: n*15, height: 13, width: 68}
                content: ImageView {image: bind volumeImage}
            }
    ;
    
    var volumeSliderNormal: Node =
        Clip {
            transform: Translate {x: 0, y: -422}
            shape: Rect {x: 0, y: 422, height: 11, width: 14}
            content: ImageView {image: bind volumeImage}
        }
    ;

    var volumeSliderPressed: Node =
        Clip {
            transform: Translate {x: -15, y: -422}
            shape: Rect {x: 15, y: 422, height: 11, width: 14}
            content: ImageView {image: bind volumeImage}
        }
    ;
    
    var font: Font = 
        Font {
            faceName: "Arial"
            style: FontStyle {name: "BOLD"}
            size: 8
        }
    ;
    
    var textColor: Paint  = Color.WHITE;
    
    function composeNode(): Node {
        return Group {
            content:
            [ImageView {
                image: bind backgroundImage
            },
            Group {
                toolTipText: bind songUrl
                content: bind if (this.hover) then titleBarFocused else titleBarUnfocused
                var mouseX = 0
                var mouseY = 0
                var loc = null
                onMouseClicked: function(e:CanvasMouseEvent) {
                    this.loadSkin();
                }
                onMousePressed: function(e:CanvasMouseEvent) {
                    loc = this.getCanvas().getComponent().getLocationOnScreen();
                    mouseX = e.x.intValue();
                    mouseY = e.y.intValue();
                }
                onMouseDragged: function(e:CanvasMouseEvent) {
                    var curLoc = this.getCanvas().getComponent().getLocationOnScreen();
                    var dx1 = curLoc.x - loc.x;
                    var dy1 = curLoc.y - loc.y;
                    loc = curLoc;
                    var dx = e.x - mouseX;
                    var dy = e.y - mouseY;
                    dx += dx1;
                    dy += dy1;
                    mouseX = e.x.intValue();
                    mouseY = e.y.intValue();
                    (this.moveFrame)(dx, dy);
                }
            },
            ButtonView {
                enabled: true
                isSelectionRoot: true
                transform: Translate {x: 264, y: 3}
                toolTipText: "Close"
                pressed: bind closeButtonPressed
                normal: bind closeButtonNormal
                action: function() {
                     System.exit(0);
                }
            },
            Group {
                transform: Translate {x: 24, y: 28}
                content: bind if (working) then workIndicatorOn else workIndicatorOff,
            },
            Group {
                transform: Translate {x: 24, y: 28}
                content: bind
                [playPauseBackground,
                if (paused) then pauseIndicator
                else if (playing) then playIndicator 
                else stopIndicator]
            },
            Group {
                 var minDigit1 = bind (elapsedMinutes/10).intValue()
                 var minDigit2 = bind (elapsedMinutes%10).intValue()

                 var secDigit1 = bind (elapsedSeconds/10).intValue()
                 var secDigit2 = bind (elapsedSeconds%10).intValue()
                 content: 
                 [Group {
                    transform: Translate {x: 48, y: 26}
                    content: bind minDigitNode1[minDigit1]
                 },
                 Group {
                    transform: Translate {x: 60, y: 26}
                    content: bind minDigitNode2[minDigit2]
                 },
                 Group {
                    transform: Translate {x: 78, y: 26}
                    content: bind secDigitNode1[secDigit1]
                 },
                 Group {
                    transform: Translate {x: 90, y: 26}
                    content: bind secDigitNode2[secDigit2]
                 }]
            },
            Group {
                 // bit rate
                 transform: Translate {x: 111, y: 43}
                 content: Text { 
                     font: bind font, 
                     fill: bind textColor,
                     content: bind "{%d bitRate}"
                }
            },
            Group {
                 // sample rate
                 transform: Translate {x: 156, y: 43}
                 content: Text { 
                     font: bind font, 
                     fill: bind textColor,
                     content: bind "{%d sampleRate}"
                }
            },
            Group {
                 transform: Translate {x: 212, y: 41}
                 content:
                 bind if (not stereo) then monoOn else monoOff
            },
            Group {
                 transform: Translate {x: 239, y: 41}
                 content:
                 bind if (stereo) then stereoOn else stereoOff
            },
            Group {
                 transform: Translate {x: 107, y: 57}
                 content: 
                 [Group {
                     content: bind volumeViews[volume]
                 },
                 Group {
                     transform: Translate {x: bind volumeX, y: 0}
                     content: bind volumeSlider
                 }]
            },
            Group {
                 toolTipText: "Shuffle"
                 transform: Translate {x: 164, y: 89}
                 content: 
                 [shuffleOnButton, shuffleOffButton]
            },
            Group {
                 transform: Translate {x: 210, y: 89}
                 toolTipText: "Repeat"
                 content: 
                 [repeatOnButton, repeatOffButton]
            },
            Group {
                transform: Translate {x: 16, y: 72}
                content:
                [posbarSlideBar,
                 SliderView {
                    transform: Translate {x: bind pos * (248-29), y: 0}
                    pressed: bind posbarSliderPushed
                    normal: bind posbarSlider
                    slide: function(dx:Number, dy: Number) {
                    }
                }]
            },
            Group {
                transform: Translate {x: 16, y: 88}
                var buttons = [backButton, playButton, pauseButton, stopButton, forwardButton]
                content: for (i in [0..sizeof buttons-1]) 
                    Group  {
                        transform: Translate {x: i*23, y: 0}
                        content: buttons[i]
                    }
            },
            Group {
                transform: Translate {x: 136, y: 89}
                content: loadButton
            }
            ]
        };
    };
}

var fxframe: Frame = Frame {
    undecorated: true
    visible: true
    content: Canvas {
        border: null
        cursor: Cursor.DEFAULT
        content: Main {
            textColor: Color.BLUE
            moveFrame: function(dx:Number, dy:Number) {
                fxframe.move(dx, dy);
            }
            skinUrls: 
                for (i in ["bo", "Blizzard_v2", "Bunker"])
                    "{__DIR__}skins/{i}"
        }
    }
}

fxframe.show();

