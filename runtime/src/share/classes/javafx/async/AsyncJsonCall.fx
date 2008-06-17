package javafx.async;

import java.lang.*;
import javafx.json.*;

public class AsyncJsonCall extends AbstractAsyncOperation {
    private attribute peer : com.sun.javafx.runtime.async.RemoteTextDocument;
    public attribute document : JSONObject;
    public attribute url : String;
    public attribute method : String = "GET";
    public attribute referer: String;

    public function cancel() : Void {
        if (peer <> null) then peer.cancel();
    }

    protected function onCompletion(value : Object) : Void {
        document = JSONObject { text: value as String};
    }

    protected function start() : Void {
        // TODO: ensure that resulting document has type text/javascript -- must be done in peer, so subclass RTD
        peer = new com.sun.javafx.runtime.async.RemoteTextDocument(listener, url, method);
        peer.setHeader("Referer", referer);
        peer.start();
    }
}
