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
     
    private attribute isPressed: Boolean;
    public attribute enabled: Boolean = true;
    public attribute pressed: Node;
    public attribute normal: Node;
    public attribute action: function();
    
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

    private attribute isPressed: Boolean;
    public attribute enabled: Boolean;
    public attribute pressed: Node;
    public attribute normal: Node;
    public attribute slide: function(dx:Number, dy:Number);

    function composeNode(): Node {
        Group {
            content: bind if (isPressed) then pressed else normal
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
    attribute skinFileChooser: FileChooser;
    attribute fileChooser: FileChooser;
    attribute active: Boolean;
    attribute skinUrl: String = bind (skinUrls[selectedSkinUrl]);
    attribute skinUrls: String[];
    attribute selectedSkinUrl: Integer;
    attribute backgroundImage: Image = bind
        Image {url: skinUrl + "/main.bmp"};
    attribute titleBarImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "TitleBar.bmp")};
    attribute playPauseImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "PlayPaus.bmp")};
    attribute posbarImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "PosBar.bmp")};
    attribute monosterImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "MonoSter.bmp")};
    attribute shufrepImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "ShufRep.bmp")};
    attribute numbersImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "Numbers.bmp")};
    attribute textImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "Text.bmp")};
    attribute volumeImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "Volume.bmp")};
    attribute equalizerImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "EqMain.bmp")};
    attribute songUrl: String;
    attribute equalizerBackground: Node =         
        ImageView {
            clip: Clip {shape: Rect {x: 0, y: 0, height: 116, width: 275}}
            image: bind equalizerImage
        }
    ;

    attribute volumeX: Number = 54/2 
        on replace {
            volume = (volumeX/54 * 15).intValue();
            gain = volumeX / 54;
        }
    ;

    attribute player: BasicPlayer = new BasicPlayer()
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
                //TODO progressJFX - this is a work around until long and byte[] can be used as parameter
                public function progressJFX(bytesRead: Integer, 
                                         microseconds: Integer,
                                         streamProperties: Map): Void {
                    //TODO DO LATER - this is a work around until a more permanent solution is provided
                    javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                        public function run():Void {
                            if (self.playing) {
                                self.elapsedMicroseconds = microseconds;
                                self.elapsedMinutes = ((microseconds/1000000).intValue() / 60).intValue();
                                self.elapsedSeconds = ((microseconds/1000000).intValue() % 60).intValue();
                                var bitrate = streamProperties.get("bitrate") as Number;
                                if (bitrate <> null) {
                                      self.bitRate = Math.round(bitrate/1000) as Integer;
                                }
                                var fr = streamProperties.get("audio.framerate.fps") as Number;
                                var fs = streamProperties.get("audio.framesize.bytes") as Number;
                                if (fr <> null) {
                                    self.frameRate = Math.round(fs *fr *8);
                                }
                                var samplerate = streamProperties.get("audio.samplerate.hz") as Number;
                                if (samplerate <> null) {
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
    attribute playerListener: BasicPlayerListener;
    attribute controller: BasicController;

    attribute playing: Boolean
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
    
    attribute paused: Boolean
        on replace {
            if (paused) {
                controller.pause();
            } else {
                controller.resume();
            }
        }
    ;

    attribute working: Boolean;
    attribute stereo: Boolean = true;
    attribute shuffle: Boolean;
    attribute repeat: Boolean;
    attribute volume: Integer;
    attribute gain: Number
        on replace {
            if (playing) {
                player.setGain(gain);
            }
        }
    ;
    attribute duration: Number; // song duration in microseconds

    attribute frameRate: Number;
    attribute bitRate: Integer;
    attribute sampleRate: Integer;

    attribute moveFrame: function(dx:Number, dy:Number);

    attribute pos: Number = bind if (duration == 0) then 0 else elapsedMicroseconds/duration;

    attribute elapsedMicroseconds: Number;
    attribute elapsedMinutes: Number;
    attribute elapsedSeconds: Number;

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
//                    songUrl = "C:/ProgramData/SonicStage/Packages/Romano Hip Hop/04-Romano Hip Hop.mp3";
//                    controller.open(new File(songUrl));
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
            if (is <> null) {
                return "{baseURL}/{imageFile}";
            }
        } catch (e) {
        }
        return "{baseURL}/{imageFile.toLowerCase()}";
    }

    attribute controlButtonsImage: Image = bind
        Image {url: this.getImageURL(skinUrl, "CButtons.bmp")};
        
    attribute controlButtonsNormal: Node[] = 
        for (i in [0..4])
            Clip {
                antialias: true
                transform: Translate {x: -i*23, y: 0}
                shape: Rect {x: i*23, y: 0, height: 18, width: if (i == 4) then 22 else 23}
                content: ImageView {image: bind controlButtonsImage}
            }
    ;
    
    attribute controlButtonsPressed: Node[] = 
        for (i in [0..4])
            Clip {
                antialias: true
                transform: Translate {x: -i*23, y: -18}
                shape: Rect {x: i*23, y: 18, height: 18, width: if (i == 4) then 22 else 23}
                content: bind ImageView {image: bind controlButtonsImage}
            }
    ;
    
    attribute loadButtonNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: -114, y: 0}
            shape: Rect {x: 114, y: 0, height: 16, width: 22}
            content: ImageView {image: bind controlButtonsImage}
        }
    ;

    attribute loadButtonPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: -114, y: -16}
            shape: Rect {x: 114, y: 16, height: 16, width: 22}
            content: ImageView {image: bind controlButtonsImage}
        }
    ;


    attribute stereoOn: Node = 
        Clip {
            shape: Rect {x: 0, y: 0, height: 12, width: 29}
            content: ImageView {image: bind monosterImage}
        }
    ;

    attribute stereoOff: Node = 
        Clip {
            transform: Translate {x: 0, y: -12}
            shape: Rect {x: 0, y: 12, height: 12, width: 29}
            content: ImageView {image: bind monosterImage}
        }
    ;

    attribute monoOn: Node = 
        Clip {
            transform: Translate {x: -29, y: 0}
            shape: Rect {x: 29, y: 0, height: 12, width: 27}
            content: ImageView {image: bind monosterImage}
        }
    ;

    attribute monoOff: Node = 
        Clip {
            transform: Translate {x: -29, y: -12}
            shape: Rect {x: 29, y: 12, height: 12, width: 27}
            content: ImageView {image: bind monosterImage}
        }
    ;

    attribute shuffleOnNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: -30}
            shape: Rect {x: 28, y: 30, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    attribute shuffleOnPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: -45}
            shape: Rect {x: 28, y: 45, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    attribute shuffleOffNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: 0}
            shape: Rect {x: 28, y: 0, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    attribute shuffleOffPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: -28, y: -15}
            shape: Rect {x: 28, y: 15, height: 15, width: 47}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    attribute repeatOnNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: -30}
            shape: Rect {x: 0, y: 30, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    attribute repeatOnPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: -45}
            shape: Rect {x: 0, y: 45, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    attribute repeatOffNormal: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: 0}
            shape: Rect {x: 0, y: 0, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;

    attribute repeatOffPressed: Node =
        Clip {
            antialias: true
            transform: Translate {x: 0, y: -15}
            shape: Rect {x: 0, y: 15, height: 15, width: 28}
            content: ImageView {image: bind shufrepImage}
        }
    ;


    attribute backButton: ButtonView = 
        ButtonView {
            toolTipText: "Previous"
            normal: bind controlButtonsNormal[0]
            pressed: bind controlButtonsPressed[0]
            action: function() {this.back();}
        }
    ;

    attribute playButton: ButtonView =
        ButtonView {
            enabled: bind songUrl <> null
            toolTipText: "Play"
            normal: bind controlButtonsNormal[1]
            pressed: bind controlButtonsPressed[1]
            action: function() {this.play();}
        }
    ;
    
    attribute pauseButton: ButtonView = 
        ButtonView {
            enabled: bind songUrl <> null
            toolTipText: "Pause"
            normal: bind controlButtonsNormal[2]
            pressed: bind controlButtonsPressed[2]
            action: function() {this.pause();}
        }
    ;

    attribute stopButton: ButtonView = 
        ButtonView {
            enabled: bind songUrl <> null
            toolTipText: "Stop"
            normal: bind controlButtonsNormal[3]
            pressed: bind controlButtonsPressed[3]
            action: function() {this.stop();}
        }
    ;

    attribute forwardButton: ButtonView = 
        ButtonView {
            toolTipText: "Next"
            normal: bind controlButtonsNormal[4]
            pressed: bind controlButtonsPressed[4]
            action: function() {this.forward();}
        }
    ;

    attribute loadButton: ButtonView = 
        ButtonView {
            toolTipText: "Load/Eject"
            normal: bind loadButtonNormal
            pressed: bind loadButtonPressed
            action: function() {this.load();}
        }
    ;

    attribute volumeSlider: SliderView = 
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

    attribute shuffleOnButton: ButtonView = 
        ButtonView {
            visible: bind shuffle
            normal: bind shuffleOnNormal
            pressed: bind shuffleOnPressed
            action: function() {shuffle = false;}
        }
    ;

    attribute shuffleOffButton: ButtonView = 
        ButtonView {
            visible: bind not shuffle
            normal: bind shuffleOffNormal
            pressed: bind shuffleOffPressed
            action: function() {shuffle = true;}
        }
    ;

    attribute repeatOnButton: ButtonView = 
        ButtonView {
            visible: bind repeat
            normal: bind repeatOnNormal
            pressed: bind repeatOnPressed
            action: function() {repeat = false;}
        }
    ;

    attribute repeatOffButton: ButtonView = 
        ButtonView {
            visible: bind not repeat
            normal: bind repeatOffNormal
            pressed: bind repeatOffPressed
            action: function() {repeat = true;}
        }
    ;

    attribute titleBarFocused: Node = 
        Clip {
            transform: Translate {x: -27, y: 0}
            shape: Rect {x: 27, y: 0, height: 14, width: 275}
            content: ImageView {image: bind titleBarImage}
        }
    ;
    
    attribute titleBarUnfocused: Node = 
        Clip {
            transform: Translate {x: -27, y: -15}
            shape: Rect {x: 27, y: 15, height: 14, width: 275}
            content: ImageView {image: bind titleBarImage}
        }
    ;
    
    attribute minimizeButtonNormal: Node;
    attribute minimizeButtonPressed: Node;
    
    attribute closeButtonNormal: Node =
        Clip {
            selectable: true
            transform: Translate {x: -18, y: 0}
            shape: Rect {x: 18, y: 0, height: 9, width: 9}
            content: ImageView {image: bind titleBarImage}
        }
    ;

    attribute closeButtonPressed: Node = 
        Clip {
            selectable: true
            transform: Translate {x: -18, y: -9}
            shape: Rect {x: 18, y: 9, height: 9, width: 9}
            content: ImageView {image: bind titleBarImage}
        }
    ;
    
    attribute optionButtonNormal: Node;
    attribute optionButtonPressed: Node;

    attribute playIndicator: Node = 
        Clip {
            transform: Translate {x: 0, y: 0}
            shape: Rect {x: 0, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;
    
    attribute pauseIndicator: Node = 
        Clip {
            transform: Translate {x: -9, y: 0}
            shape: Rect {x: 9, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    attribute stopIndicator: Node = 
        Clip {
            transform: Translate {x: -18, y: 0}
            shape: Rect {x: 18, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    attribute workIndicatorOn: Node = 
        Clip {
            transform: Translate {x: -39, y: 0}
            shape: Rect {x: 39, y: 0, height: 9, width: 3}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    attribute workIndicatorOff: Node;
    attribute playPauseBackground: Node = 
        Clip {
            transform: Translate {x: -27, y: 0}
            shape: Rect {x: 27, y: 0, height: 9, width: 9}
            content: ImageView {image: bind playPauseImage}
        }
    ;

    attribute posbarSlideBar: Node = 
        Clip {
            antialias: true
            shape: Rect {x: 0, y: 0, height: 9, width: 248}
            content: ImageView {image: bind posbarImage}
        }
    ;

    attribute posbarSlider: Node = 
        Clip {
            antialias: true
            transform: Translate {x: -248, y: 0}
            shape: Rect {x: 248, y: 0, height: 9, width: 29}
            content: ImageView {image: bind posbarImage}
        }
    ;

    attribute posbarSliderPushed: Node = 
        Clip {
            transform: Translate {x: -278, y: 0}
            shape: Rect {x: 278, y: 0, height: 9, width: 29}
            content: ImageView {image: bind posbarImage}
        }
    ;

    // TODO CANVASIMAGE - refactor as soon as CanvasImage.getImage() is implemented
    //                    (requires com.sun.scenario.scenegraph.JSGPanel.toIcon())
//    attribute numbers: Image[] = 
//        for (i in [0..9])
//            CanvasImage {
//                content: Clip {
//                    transform: Translate {x: -i*9, y: 0}
//                    shape: Rect {x: i*9, y: 0, height: 13, width: 9}
//                    content: ImageView {image: bind numbersImage}
//                }
//            }
//    ;
    attribute minDigitNode1: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    attribute minDigitNode2: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    attribute secDigitNode1: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    attribute secDigitNode2: Node[] =
        for (i in [0..9])
            Clip {
                transform: Translate {x: -i*9, y: 0}
                shape: Rect {x: i*9, y: 0, height: 13, width: 9}
                content: ImageView {image: bind numbersImage}
            }
    
    attribute text1: Image[] = 
        for (n in [0..10])
            CanvasImage {
                content: Clip {
                      transform: Translate {x: -n*5, y: 0}
                      shape: Rect {x: n*5, y: 0, height: 6, width: 5}
                      content: ImageView {image: bind textImage}
                }
            }
    ;
    
    attribute text2: Image[] = 
        for (n in [0..10])
            CanvasImage {
                content: Clip {
                      transform: Translate {x: -n*5, y: -6}
                      shape: Rect {x: n*5, y: 6, height: 6, width: 5}
                      content: ImageView {image: bind textImage}
                }
            }
    ;
    
    attribute text3: Image[] = 
        for (n in [0..10])
            CanvasImage {
                content: Clip {
                      transform: Translate {x: -n*5, y: -12}
                      shape: Rect {x: n*5, y: 12, height: 6, width: 5}
                      content: ImageView {image: bind textImage}
                }
            }
    ;

    attribute numberMinus: Node = 
        Clip {
            transform: Translate {x: -20, y: -6}
            shape: Rect {x: 20, y: 6, height: 1, width: 5}
        }
    ;
    
    attribute numberNotMinus: Node = 
        Clip {
            transform: Translate {x: -9, y: -6}
            shape: Rect {x: 9, y: 6, height: 1, width: 5}
        }
    ;

    attribute volumeViews: Node[] = 
        for (n in [0..15])
            Clip {
                transform: Translate {x: 0, y: -n*15}
                shape: Rect {x: 0, y: n*15, height: 13, width: 68}
                content: ImageView {image: bind volumeImage}
            }
    ;
    
    attribute volumeSliderNormal: Node =
        Clip {
            transform: Translate {x: 0, y: -422}
            shape: Rect {x: 0, y: 422, height: 11, width: 14}
            content: ImageView {image: bind volumeImage}
        }
    ;

    attribute volumeSliderPressed: Node =
        Clip {
            transform: Translate {x: -15, y: -422}
            shape: Rect {x: 15, y: 422, height: 11, width: 14}
            content: ImageView {image: bind volumeImage}
        }
    ;
    
    attribute font: Font = 
        Font {
            faceName: "Arial"
            style: FontStyle {name: "BOLD"}
            size: 8
        }
    ;
    
    attribute textColor: Paint  = Color.WHITE;
    
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
                 visible: false
                 // Song title display
                 transform: Translate {x: 112, y: 27}
                 content: Text { 
                     font: bind font, 
                     fill: bind textColor,
                     //content: bind songTitle 
                }
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
            Clip {
                visible: false
                transform: Translate {x: 112, y: 27}
                shape: Rect {height: 6, width: 152}
                content: Text {
                    visible: bind songUrl <> null
                    font: bind font
                    fill: bind textColor
                    content: bind songUrl
                }
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
            },
            Group {
               // equalizer
               visible: false
               transform: Translate {x: 0, y: 116}
               content: Group {
                    content: bind 
                    [equalizerBackground]
               }
            }
            ]
        };
    };
}

var fxframe: Frame;

fxframe = Frame {
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

