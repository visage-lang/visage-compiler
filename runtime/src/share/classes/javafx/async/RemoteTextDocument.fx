package javafx.async;

import java.lang.*;

public class RemoteTextDocument extends AbstractAsyncOperation {
    private var peer : com.sun.javafx.runtime.async.RemoteTextDocument;
    public var document : String;
    public var url : String;
    public var method : String = "GET";

    public override function cancel() : Void {
        if (peer != null) then peer.cancel();
    }

    protected override function onCompletion(value : Object) : Void {
        document = value as String;
    }

    protected override function start() : Void {
        peer = new com.sun.javafx.runtime.async.RemoteTextDocument(listener, url, method);
        peer.start();
    }
}
