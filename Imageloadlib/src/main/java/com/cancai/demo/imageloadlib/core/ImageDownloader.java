package com.cancai.demo.imageloadlib.core;

import android.content.Context;
import android.net.Uri;

import com.cancai.demo.imageloadlib.utils.IoUtil;
import com.cancai.demo.imageloadlib.utils.L;

import java.io.BufferedInputStream;
import java.io.FilterInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片下载
 *
 * @author peter_wang
 * @create-time 15/7/2 14:30
 */
class ImageDownloader {
    /**
     * {@value}
     */
    private static final int DEFAULT_HTTP_CONNECT_TIMEOUT = 5 * 1000; // milliseconds
    /**
     * {@value}
     */
    private static final int DEFAULT_HTTP_READ_TIMEOUT = 20 * 1000; // milliseconds
    /**
     * {@value}
     */
    private static final String ALLOWED_URI_CHARS = "@#&=*+-_.,:!?()/~'%";
    /**
     * {@value}
     */
    private static final int BUFFER_SIZE = 32 * 1024; // 32 Kb

    protected final Context mContext;

    public ImageDownloader(Context context) {
        mContext = context;
    }

    public InputStream getStreamFromNetwork(String imageUri) throws IOException {
        HttpURLConnection conn = createConnection(imageUri);

        InputStream imageStream;
        imageStream = conn.getInputStream();
        if (conn.getResponseCode() != 200) {
            IoUtil.closeStream(imageStream);
            throw new IOException("Image request failed with response code " + conn.getResponseCode());
        }
        final String contentLengthStr = conn.getHeaderField("content-length");
        L.d(String.format("image size stream:%1$.3fM[%2$s]", Double.valueOf(contentLengthStr) / 1024 / 1024,imageUri));
        return new FlushedInputStream(new BufferedInputStream(imageStream, BUFFER_SIZE));
    }

    private HttpURLConnection createConnection(String url) throws IOException {
        String encodedUrl = Uri.encode(url, ALLOWED_URI_CHARS);
        HttpURLConnection conn = (HttpURLConnection) new URL(encodedUrl).openConnection();
        conn.setConnectTimeout(DEFAULT_HTTP_CONNECT_TIMEOUT);
        conn.setReadTimeout(DEFAULT_HTTP_READ_TIMEOUT);
        return conn;
    }

    /**
     * a bug in the previous versions of BitmapFactory.decodeStream may prevent this code from working over a slow connection.
     * Decode a new FlushedInputStream(inputStream) instead to fix the problem.
     */
    final class FlushedInputStream extends FilterInputStream {
        public FlushedInputStream(InputStream inputStream) {
            super(inputStream);
        }

        @Override
        public long skip(long n) throws IOException {
            long totalBytesSkipped = 0L;
            while (totalBytesSkipped < n) {
                long bytesSkipped = in.skip(n - totalBytesSkipped);
                if (bytesSkipped == 0L) {
                    int bytes = read();
                    if (bytes < 0) {
                        break;  // we reached EOF
                    } else {
                        bytesSkipped = 1; // we read one byte
                    }
                }
                totalBytesSkipped += bytesSkipped;
            }
            return totalBytesSkipped;
        }
    }
}  
