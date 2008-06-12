package javafx.async;

import java.lang.*;
import javafx.ui.*;
import javafx.ui.canvas.*;


/*
 * RemoteImage should be initialized with an object literal that specifies the url attribute.
 * The client can bind to or read the properties done, cancelled, failed, failureText, progressCur,
 * progressMax, and image.  It should not write to any of these.
 */

public class RemoteImage extends AbstractAsyncOperation {
    private attribute peer : com.sun.javafx.runtime.async.RemoteImage;
    attribute image : Image;
    attribute url : String;

    function cancel() {
        if (peer <> null) then peer.cancel();
    }

    protected function onCompletion(value : Object) {
        image = MemoryImage { theImage : value as java.awt.Image };
    }

    protected function start() : Void {
        peer = new com.sun.javafx.runtime.async.RemoteImage(listener, url);
        peer.start();
    }
}

// Hack class to work around behavior of Image
class MemoryImage extends Image {
    private attribute theImage : java.awt.Image;
    override attribute image = bind theImage;
}

