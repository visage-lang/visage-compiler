package javafx.async;

import java.lang.*;
import com.sun.javafx.runtime.async.AsyncOperationListener;

public abstract class AbstractAsyncOperation {
    public attribute done : Boolean;
    public attribute canceled : Boolean;
    public attribute failed : Boolean;
    public attribute failureText : String;
    public attribute progressCur : Integer;
    public attribute progressMax : Integer;
    public attribute onDone : function(success : Boolean) : Void;

    private attribute self = this;
    protected attribute listener = AsyncOperationListener {
        function onCancel() : Void {
            canceled = true;
            done = true;
            if (onDone <> null) then onDone(false);
        }

        function onException(exception : Exception) : Void {
            failureText = exception.getMessage();
            exception.printStackTrace();
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

    public abstract function cancel() : Void;

    protected abstract function start() : Void;

    protected abstract function onCompletion(value : Object) : Void;

    init {
        start();
    }
}

