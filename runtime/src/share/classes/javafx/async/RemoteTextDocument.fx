package javafx.async;

import java.lang.*;
import javafx.ui.*;
import javafx.ui.canvas.*;


public class RemoteTextDocument extends AbstractAsyncOperation {
    private attribute peer : com.sun.javafx.runtime.async.RemoteTextDocument;
    attribute document : String;
    attribute url : String;

    function cancel() {
        if (peer <> null) then peer.cancel();
    }

    function onCompletion(value : Object) {
        AbstractAsyncOperation.onCompletion(value);
        document = value as String;
    }

    function start() : Void {
        peer = new com.sun.javafx.runtime.async.RemoteTextDocument(this, url);
        peer.start();
    }
}
