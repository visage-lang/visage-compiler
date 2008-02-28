/*
 *  Copyright 2007 Sun Microsystems, Inc. All rights reserved.
 *  SUN PROPRIETARY/CONFIDENTIAL. Use is subject to license terms.
 */

package mp3;
import java.util.Map;
import javazoom.jlgui.basicplayer.BasicPlayerListener;

public abstract class BasicPlayerListenerAdapter implements BasicPlayerListener {

    public void progress(int arg0, long arg1, byte[] arg2, Map arg3) {
        progressJFX(arg0, (int)arg1, arg3);
    }

    public abstract void progressJFX(int arg0, long arg1, Map arg3 );

    public void opened(Object arg0, Map arg1) {
        double framerate = ((Float)arg1.get("audio.framerate.fps")).doubleValue();
        double samplerate = ((Float)arg1.get("audio.samplerate.hz")).doubleValue();
        int duration = ((Long)arg1.get("duration")).intValue();
        openedJFX(arg0, arg1, framerate, samplerate, duration);
    }
    
    public abstract void openedJFX(Object arg0, Map arg1, double framerate, double samplerate, int duration);
    
}
