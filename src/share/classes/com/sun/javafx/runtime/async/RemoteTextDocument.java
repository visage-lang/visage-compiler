package com.sun.javafx.runtime.async;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * RemoteTextDocument
 *
 * @author Brian Goetz
 */
public class RemoteTextDocument extends AbstractAsyncOperation<String> {

    public static final int BUF_SIZE = 8192;
    private final String url;

    public RemoteTextDocument(AsyncOperationListener<String> listener, String url) {
        super(listener);
        this.url = url;
    }

    public String call() throws IOException {
        URL u = new URL(url);

        HttpURLConnection conn = (HttpURLConnection) u.openConnection();
        int fileSize = conn.getContentLength();
        String encoding = conn.getContentEncoding();
        setProgressMax(fileSize);

        Reader reader = new InputStreamReader(new ProgressInputStream(conn.getInputStream()), encoding);
        // @@@ Might be too large -- is content-length the length of the actual resource, or the encoded length?
        StringBuilder sb = new StringBuilder(fileSize);
        try {
            char buf[] = new char[BUF_SIZE];
            while (true) {
                int nRead = reader.read(buf);
                if (nRead == -1)
                    break;
                else
                    sb.append(buf, 0, nRead);
            }
            return sb.toString();
        }
        finally {
            reader.close();
        }
    }
}
