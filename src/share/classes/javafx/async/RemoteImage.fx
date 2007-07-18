package javafx.async;

import java.lang.*;

import javafx.ui.*;
import javafx.ui.canvas.*;

import com.sun.javafx.runtime.async.AsyncOperationListener;
import com.sun.javafx.runtime.async.RemoteImage as JRemoteImage;

/*
 * RemoteImage should be initialized with an object literal that specifies the url attribute.
 * The client can bind to or read the properties done, cancelled, failed, failureText, progressCur,
 * progressMax, and image.  It should not write to any of these.
 */

class RemoteImage {
    attribute done : Boolean;
    attribute canceled : Boolean;
    attribute failed : Boolean;
    attribute failureText : String;
    attribute image : Image;
    attribute peer : JRemoteImage;
    attribute url : String;
    attribute progressCur : Integer;
    attribute progressMax : Integer;
    private attribute listener : AsyncOperationListener;
    operation cancel();
}

// Having to do this as a property trigger seems ugly, as this is supposed to be a one-time-use class.  Would
// prefer to do this in the constructor or new trigger but that's not yet an option.

trigger on RemoteImage.url = newUrl {
    System.out.println("Starting {url}");
    peer = new JRemoteImage(listener, url);
    peer.start();
}


// The only reason we're using a ctor here is that there's a bug that prevents RemoteImage from implementing
// an interface.  So we hold an instance of an interface instead, which is initialized in the ctor.  Ideally,
// most of these interface methods will be implemented by the parent class.

operation RemoteImage.RemoteImage() {
    var self = this;
    listener = new AsyncOperationListener() {
        operation onCancel() {
            System.out.println("cancel");
            self.canceled = true;
        }

        operation onException(exception : Exception) {
            var failureText = exception.getMessage();
            self.failureText = exception.getMessage();
            self.failed = true;
            System.out.println("fail {failureText}");
            exception.printStackTrace();
        }

        operation onCompletion(value) {
            self.done = true;
            System.out.println("done");
            self.image = MemoryImage { theImage : (<<java.awt.Image>>) value };
        }

        operation onProgress(cur, max) {
            self.progressCur = cur;
            self.progressMax = max;
            System.out.println("Progress {cur} of {max}");
        }
    };
}

operation RemoteImage.cancel() {
  peer.cancel();
}

// Hack class to get around the fact that Image.image is protected
class MemoryImage extends Image {
    private attribute theImage : <<java.awt.Image>>;
}

attribute MemoryImage.image = bind theImage;

