package javafx.async;
import java.lang.*;

import com.sun.javafx.runtime.async.DurationListener;
import com.sun.javafx.runtime.async.Duration as JDuration;

class Duration {
    attribute min : Integer;
    attribute max : Integer;
    attribute duration : Integer;
    attribute onChange : function(value : Integer);
    operation start();
    operation cancel();

    private attribute listener : DurationListener;
    private attribute peer : JDuration;
}

// The only reason we're using a ctor here is that there's a bug that prevents us from implementing
// an interface.  So we hold an instance of an interface instead, which is initialized in the ctor.  Ideally,
// most of these interface methods will be implemented by the parent class.

operation Duration.Duration() {
    var self = this;
    listener = new DurationListener() {
        operation onTick(value : Integer) {
            (self.onChange)(value);
        }
    };
}

operation Duration.start() {
    peer = new JDuration(listener, min, max, duration);
    peer.start();
}

operation Duration.cancel() {
  peer.cancel();
}
