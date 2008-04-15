package javafx.lang;
import com.sun.javafx.runtime.Entry;

public class DeferredTask {
    /* required */ public attribute action: function() : Void;

    /* controlled */ public attribute done : Boolean;

    postinit {
        Entry.deferTask(function() : Void { try { action(); } finally { done = true } });
    }
}