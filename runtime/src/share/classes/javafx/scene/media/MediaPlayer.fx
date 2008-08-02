/*
 * Copyright 2008 Sun Microsystems, Inc.  All Rights Reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Sun Microsystems, Inc., 4150 Network Circle, Santa Clara,
 * CA 95054 USA or visit www.sun.com if you need additional information or
 * have any questions.
 */ 

package javafx.scene.media;

import javafx.lang.Duration;
import com.sun.media.jmc.MediaProvider;
import com.sun.media.jmc.control.AudioControl;
import java.net.URI;
import java.lang.System;
import java.lang.Exception;

private /* const */ def DURATION_UNKNOWN:Duration = -1ms;

/**
 * Value of {@code repeatCount} for no repeating (play once)
 * @profile common
 */
public /* const */ def REPEAT_NONE:Number = 1;

/**
 * Value of {@code repeatCount} to repeat forever
 * @profile common
 */
public /* const */ def REPEAT_FOREVER:Integer = -1;//infinity;// where is Number.infinity;

/**
 * Status value when player is paused
 *
 * @profile common
 */
public /* const */ def PAUSED:Integer=0;

/**
 * status value when player is playing
 *
 * @profile common
 */
public /* const */ def PLAYING:Integer=2;

/**
 * Status value when player is buffering.
 * Buffering may occur when player is paused or playing
 *
 * @profile common
 */
public /* const */ def BUFFERING: Integer=3;

/**
 * Status value when player is stalled.
 * {@code STALLED} occurs when media is being played, but
 * data is not being delivered fast enough to continue playing
 * @see onStalled
 *
 * @profile common
 */
public /* const */ def STALLED: Integer=4; // occurs during play

/**
 * The {@code MediaPlayer} class provides the controls for playing media.
 * It is used in combination with the {@code Media} and {@code MediaViewer}
 * classes to display and control media playing.
 * @profile common
 * @see Media MediaViewer
 */
public class MediaPlayer {
    attribute mediaProvider:MediaProvider = new MediaProvider();
    private attribute view:MediaView;
    // FIXME: multiple views

    /**
     * Defines the source {@code Media} to be played
     * @see Media
     * @profile common
     */
    public attribute media:Media on replace {
        try {
            mediaProvider.setSource(new URI(media.source));
        } catch (e:java.lang.Exception) {
            var error:MediaError;
            error = MediaError.exceptionToError(e);
            handleError(error);
       
            
        }
        view.setComponent();
        startTime = Duration.valueOf(mediaProvider.getStartTime()*1000.0);
        
        stopTime = Duration.valueOf(mediaProvider.getStopTime()*1000.0);
        if (autoPlay) {
            play();
        }        

    }
    
    /**
     * If {@code autoPlay} is {@code true}, playing will start as soon
     * as possible
     *
     * @profile commom
     */
    public attribute autoPlay:Boolean on replace {
        if (autoPlay) {
            play();
        }
    }

     
    /**
     * Starts or resumes playing
     *
     * @profile common
     */
    public function play() {
        mediaProvider.play();
        paused = false;
    }

    /**
     * Pauses playing
     *
     * @profile common
     */
    public function pause() {
        mediaProvider.pause();
        paused = true;
    }

    /**
     * Indicated if the player has been paused, either programatically,
     * by the user, or because the media has finished playing
     *
     * @profile common
     */
    public attribute paused:Boolean;

    /**
     * Defines the rate at which the media is being played.
     * Rate {@code 1.0} is normal play, {@code 2.0} is 2 time normal,
     * {@code -1.0} is backwards, etc...
     *
     * @profile common
     */
    public attribute rate:Number on replace {
        mediaProvider.setRate(rate);
    }

    /**
     * Defines the volume at which the media is being played.
     * {@code 1.0} is full volume, which is the default.
     *
     * @profile common
     */
    public attribute volume:Number = 1.0 on replace {
        var ac : AudioControl;
        if ((ac = MediaHelper.getAudioControl(mediaProvider)) != null) {
            ac.setVolume(volume.floatValue());
        }
    }

    /**
     * Defines the balance, or left right setting,  of the audio output.
     * Value ranges continuously from {@code -1.0} being left,
     * {@code 0} being center, and {@code 1.0} being right.
     *
     * @profile common
     */
    public attribute balance:Number=0 on replace {
        ;
    }

    /**
     * The fader, or forward and back setting, of audio output
     * on 4+ channel output.
     * value ranges continuously from {@code -1.0} being rear,
     * {@code 0} being center, and {@code 1.0} being forward.
     *
     * @profile common
     */
    public attribute fader:Number on replace {
        ;
    }

    /**
     * Defines the time offset where media should start playing,
     * or restart from when repeating
     *
     * @profile common
     */
    public attribute startTime:Duration on replace {
        mediaProvider.setStartTime(1000.0*(startTime.toMillis()));
    }

    /**
     * Defines the time offset where media should stop playing
     * or restart when repeating
     *
     * @profile common
     */
    public attribute stopTime:Duration = DURATION_UNKNOWN on replace {
        if (stopTime == DURATION_UNKNOWN) {
            // do nothing for now, 
            // mediaProvider.setStopTime(java.lang.Double.POSITIVE_INFINITY);
        } else {
            mediaProvider.setStopTime(1000*stopTime.toMillis());
        }
    }

    /**
     * Defines the current media time
     *
     * @profile common
     */
    public attribute currentTime:Duration on replace {
        mediaProvider.setMediaTime(1000 * currentTime.toMillis());
    }
   
    /**
     * Defines the media timers for this player
     *
     * @profile common
     */
    public attribute timers:MediaTimer[];
   
    /**
     * Defines the number of times the media should repeat.
     * if repeatCount is 1 the media will play once.
     * if it is REPEAT_FOREVER, it will repeat indefinitely
     * In this implementation, these are the only values currently supported
     * @profile common
     */
    public attribute repeatCount: Number = 1 on replace {
        if (repeatCount == REPEAT_FOREVER) {
            mediaProvider.setRepeating(true);
        } else {
            mediaProvider.setRepeating(false);
        }
    }

    /**
     * Defines the current number of time the media has repeated
     * @profile common
     */
    public attribute currentCount:Number=0; // How many times have we repeated

   
    /**
     * Equals {@code true} if the player's audio is muted, false otherwise.
     * @profile common
     * @see volume
     */
    public attribute mute: Boolean on replace {
        var ac : AudioControl = MediaHelper.getAudioControl(mediaProvider);
        
        if (ac != null) {
            ac.setMute(mute);
        } else {
            System.out.println("No Audio Control!");
        }
    }

    /**
     * Current status of player
     *
     * @profile common
     */
    public attribute status:Integer;

    /**
     * The {@code onError} function is called when a {@code mediaError} occurs
     * on this player.
     *
     * @profile common
     * @see MediaError
     */
    public attribute onError: function (e: MediaError):Void; // Error in Media


    /**
     * Invoked when the player reaches the end of media
     *
     * @profile common
     */
    public attribute onEndOfMedia: function():Void; // Media has reached its end.

    /**
     * Invoked when the player reaches the end of media.
     *
     * @profile common
     * @see repeatCount
     */
    public attribute onRepeat:function():Void; // Media has hit the end and is repeating
    
    /**
     * Invoked when the player is buffering data.
     * {@code timeRemaining} is an estimate of how much time it will take before
     * the {@code mediaPlayer} can play the media
     *
     * @profile common
     */
    public attribute onBuffering:function(timeRemaining: Duration):Void;

    /**
     * Invoked when the player has stalled
     * because it was not receiving data fast enough to continue playing.
     * {@code timeRemaining} is an estimate of how much time it will take before
     * the {@code mediaPlayer} can continue playing.
     *
     * @profile common
     */
    public attribute onStalled:function(timeRemaining: Duration):Void;
    
    /**
     * Indicates if this player can have multiple views
     * associated with it.
     * @see MediaView
     *
     * @profile common
     */
    public attribute supportsMultiViews:Boolean;
   
    // this can fail, or effect other views
    function addView(view:MediaView) {
        this.view = view;
    }

    private function handleError(error: MediaError):Void {
        if (onError != null) {
            onError(error);
        }
    }

}
