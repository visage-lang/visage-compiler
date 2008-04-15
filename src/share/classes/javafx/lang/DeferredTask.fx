package javafx.lang;
import com.sun.javafx.runtime.Entry;

public class DeferredTask {
    /* required */ public attribute action: function() : Void;

    /* controlled */ public attribute done : Boolean;

    public attribute onDone : function() : Void;

    postinit {
        try {
            Entry.deferTask(action);
        }
        finally {
            done = true;
            if (onDone <> null)
                onDone();
        }
    }
}