/*
 * GuitarString.fx
 *
 * Created on Dec 19, 2007, 10:02:51 AM
 */

package guitar;

import javafx.ui.canvas.CompositeNode;
import javafx.ui.canvas.Node;
import javafx.ui.canvas.Group;
import javafx.ui.canvas.Translate;
import javafx.ui.canvas.ImageView;
import javafx.ui.canvas.CanvasMouseEvent;
import javafx.ui.*;
import com.sun.scenario.animation.*;
import java.lang.Math;
import java.lang.System;
import java.net.URL;
import java.applet.AudioClip;
/**
 * @author jclarke
 */

public class GuitarString extends CompositeNode {
    attribute audioClip: AudioClip;
    attribute guitar: Guitar;
    attribute wound: Boolean = true;
    attribute note: String;
    attribute soundUrl: URL = bind if (note == null) then null else this.getClass().getResource("Resources/Wavs/{note}.wav")
        on replace {
           //TODO DO LATER - this is a work around until a more permanent solution is provided
            javax.swing.SwingUtilities.invokeLater(java.lang.Runnable {
                      public function run():Void {
                            guitar.loadingSound++;
                            //var clip;
                            //TODO DO  - this is a work around until a more permanent solution is provided
                            //javax.swing.SwingUtilities.invokeAndWait(java.lang.Runnable {
                            //    public function run():Void {
                                    audioClip = java.applet.Applet.newAudioClip(soundUrl); 
                            //    }
                           // });
                            //audioClip = clip;
                            guitar.loadingSound--;
                      }
            });
            //System.out.println("soundUrl={soundUrl}");
    }
    attribute imageUrl: String = bind this.getClass().getResource('Resources/{if (wound) then "Wound" else "Plain"}.png').toString();
    public function composeNode(): Node {
        var self = this;
        Group {
            content: [
            ImageView { 
                cursor: Cursor.HAND
                var tx = Translate {x: 0, y: 0}
                transform: [tx]
                image: Image {
                    url: bind imageUrl
                }
                var playing = false
                onMouseMoved: function(e:CanvasMouseEvent):Void {
                    if (playing) {return;}

                    guitar.play(self);

                    /**
                    var off = [59, 58, 57, 58, 59, 58, 57, 58, 59, 58, 57, 58, 59, 58];
                    for (unitinterval t in dur 500 fps 60) {
                        tx.x = off[t*sizeof off] -58;
                        playing = t < 1;
                    }
                     * */
                    var fps = 60;
                    var resolution:Integer = (1000/ fps).intValue();
                    //TODO JXFC-439 cannot resolve overloaded methods with float.
                    // the below timingEvent function is not being called from the Animation
                    // because of this.
                    var animeClip:com.sun.scenario.animation.Clip  =  
                        com.sun.scenario.animation.Clip.create(500, TimingTargetAdapter{
                            public function timingEvent(fraction:java.lang.Float):Void {
                                var x = Math.cos(fraction.doubleValue() * 10 * (Math.PI*2));
                                System.out.println("timingEvent {this} x = {x}");
                                tx.x = x;
                                playing = true;
                            }
                            public function begin():Void {
                                playing = true;
                            };                            
                            public function end():Void {
                                playing = false;
                            };
                        });
                    // Use a cosine wave to emulate the string being plucked
                        //TODO JXFC-439 cannot resolve overloaded methods with float.
                        /***
                    animeClip.setInterpolator(Interpolator {
                        // public float interpolate(float fraction)
                        public function interpolate(fraction:Number):Number {
                            return Math.cos(fraction * (Math.PI*2) + 1.0)/2.0;
                        }
                    });
                         * ***/
                    animeClip.setResolution(resolution);
                    animeClip.start();
                    
                }

            }]
        };
    }
}