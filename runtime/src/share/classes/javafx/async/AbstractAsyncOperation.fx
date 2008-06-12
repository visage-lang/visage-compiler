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
    attribute onDone : function() : Void;

    private attribute self = this;
    protected attribute listener = AsyncOperationListener {
        function onCancel() {
            canceled = true;
        }

        function onException(exception : Exception) {
            failureText = exception.getMessage();
            failed = true;
            System.out.println("fail {failureText}");
            exception.printStackTrace();
        }

        function onCompletion(value : Object) {
            done = true;
            self.onCompletion(value);
            if (onDone <> null) then onDone();
        }

        function onProgress(cur : Integer, max : Integer) {
            progressCur = cur;
            progressMax = max;
        }
    }

    abstract function start() : Void;

    abstract function cancel() : Void;

    protected abstract function onCompletion(value : Object) : Void;

    init {
        start();
    }
}

