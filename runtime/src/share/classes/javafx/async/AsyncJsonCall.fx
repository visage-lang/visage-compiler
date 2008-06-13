package javafx.async;

import java.lang.*;
import javafx.json.*;

public class AsyncJsonCall extends AbstractAsyncOperation {
    private attribute peer : com.sun.javafx.runtime.async.RemoteTextDocument;
    attribute document : JSONObject;
    attribute url : String;
    attribute method : String = "GET";
    attribute referer: String;

    function cancel() : Void {
        if (peer <> null) then peer.cancel();
    }

    protected function onCompletion(value : Object) : Void {
        var p = new Parser();
        document = p.parseString(value as String);
    }

    protected function start() : Void {
        // TODO: ensure that resulting document has type text/javascript -- must be done in peer, so subclass RTD
        peer = new com.sun.javafx.runtime.async.RemoteTextDocument(listener, url, method);
        peer.setHeader("Referer", referer);
        peer.start();
    }
}
