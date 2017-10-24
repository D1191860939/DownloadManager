package com.dc3658.download.download;

import java.io.Closeable;
import java.io.IOException;

/**
 *
 * @author xlh
 * @date 2017/10/22
 */

public class StreamUtils {

    public static void close(Closeable stream) {

        try {
            if (stream != null) {
                stream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
