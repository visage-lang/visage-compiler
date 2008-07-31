package javafx.async;

import java.lang.*;
import javafx.json.*;

public class AsyncJsonCall extends AbstractAsyncOperation {
    private var peer : com.sun.javafx.runtime.async.RemoteTextDocument;
    public var document : JSONObject;
    public var url : String;
    public var method : String = "GET";
    public var referer: String;
    public var outboundContent: String;
    public var contentType: String;

    public override function cancel() : Void {
        if (peer != null) then peer.cancel();
    }

    protected override function onCompletion(value : Object) : Void {
        document = JSONObject { text: value as String};
    }

    protected override function start() : Void {
        // TODO: ensure that resulting document has type text/javascript -- must be done in peer, so subclass RTD
        peer = new com.sun.javafx.runtime.async.RemoteTextDocument(listener, url, method, outboundContent);
        peer.setHeader("Referer", referer);
        if(contentType != null) {
            peer.setHeader("Content-Type", contentType);
        }
        peer.start();
    }
}
