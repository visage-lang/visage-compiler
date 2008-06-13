package javafx.async;

import java.lang.*;

public class RemoteTextDocument extends AbstractAsyncOperation {
    private attribute peer : com.sun.javafx.runtime.async.RemoteTextDocument;
    attribute document : String;
    attribute url : String;
    attribute method : String = "GET";

    function cancel() : Void {
        if (peer <> null) then peer.cancel();
    }

    protected function onCompletion(value : Object) : Void {
        document = value as String;
    }

    protected function start() : Void {
        peer = new com.sun.javafx.runtime.async.RemoteTextDocument(listener, url, method);
        peer.start();
    }
}
