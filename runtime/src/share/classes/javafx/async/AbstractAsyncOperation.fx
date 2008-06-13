package javafx.async;

import java.lang.*;
import com.sun.javafx.runtime.async.AsyncOperationListener;

public abstract class AbstractAsyncOperation {
    attribute done : Boolean;
    attribute canceled : Boolean;
    attribute failed : Boolean;
    attribute failureText : String;
    attribute progressCur : Integer;
    attribute progressMax : Integer;
    attribute onDone : function(success : Boolean) : Void;

    private attribute self = this;
    protected attribute listener = AsyncOperationListener {
        function onCancel() : Void {
            canceled = true;
            done = true;
            if (onDone <> null) then onDone(false);
        }

        function onException(exception : Exception) : Void {
            failureText = exception.getMessage();
            failed = true;
            done = true;
            if (onDone <> null) then onDone(false);
        }

        function onCompletion(value : Object) : Void {
            done = true;
            self.onCompletion(value);
            if (onDone <> null) then onDone(true);
        }

        function onProgress(cur : Integer, max : Integer) : Void {
            progressCur = cur;
            progressMax = max;
        }
    }

    abstract function cancel() : Void;

    protected abstract function start() : Void;

    protected abstract function onCompletion(value : Object) : Void;

    init {
        start();
    }
}

