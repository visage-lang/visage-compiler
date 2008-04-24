package javafx.async;

import java.lang.*;
import com.sun.javafx.runtime.async.AsyncOperationListener;

public class AbstractAsyncOperation extends AsyncOperationListener {
    attribute done : Boolean;
    attribute canceled : Boolean;
    attribute failed : Boolean;
    attribute failureText : String;
    attribute progressCur : Integer;
    attribute progressMax : Integer;

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
    }

    function onProgress(cur : Integer, max : Integer) {
        progressCur = cur;
        progressMax = max;
    }
}

