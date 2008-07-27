package javafx.async;

import java.lang.*;
import com.sun.javafx.runtime.async.AsyncOperationListener;

public abstract class AbstractAsyncOperation {
    public var done : Boolean;
    public var canceled : Boolean;
    public var failed : Boolean;
    public var failureText : String;
    public var progressCur : Integer;
    public var progressMax : Integer;
    public var onDone : function(success : Boolean) : Void;

    private def self = this;
    protected var listener = AsyncOperationListener {
        override function onCancel() : Void {
            canceled = true;
            done = true;
            if (onDone != null) then onDone(false);
        }

        override function onException(exception : Exception) : Void {
            failureText = exception.getMessage();
            exception.printStackTrace();
            failed = true;
            done = true;
            if (onDone != null) then onDone(false);
        }

        override function onCompletion(value : Object) : Void {
            done = true;
            self.onCompletion(value);
            if (onDone != null) then onDone(true);
        }

        override function onProgress(cur : Integer, max : Integer) : Void {
            progressCur = cur;
            progressMax = max;
        }
    }

    public abstract function cancel() : Void;

    protected abstract function start() : Void;

    protected abstract function onCompletion(value : Object) : Void;

    init {
        start();
    }
}

