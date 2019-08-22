package com.zhiyicx.baseproject.impl.imageloader.glide.progress;

import com.zhiyicx.common.utils.log.LogUtils;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSink;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;

/**
 * @author LiuChao
 * @describe
 * @date 2017/2/14
 * @contact email:450127106@qq.com
 */

public class ProgressResponceBody extends ResponseBody {
    private ResponseBody mResponseBody;
    private BufferedSource mBufferedSource;
    private ProgressListener mProgressListener;

    public ProgressResponceBody(ResponseBody responseBody, ProgressListener progressListener) {
        mResponseBody = responseBody;
        mProgressListener = progressListener;
    }

    @Override
    public MediaType contentType() {
        return mResponseBody.contentType();
    }

    @Override
    public long contentLength() {
        return mResponseBody.contentLength();
    }

    @Override
    public BufferedSource source() {
        if (mBufferedSource == null) {
            mBufferedSource = Okio.buffer(new ForwardingSource(mResponseBody.source()) {
                private long totalReadBytes;// 所有已读的字节

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long readBytes = super.read(sink, byteCount);// 当前读的字节
                    totalReadBytes += readBytes != -1 ? readBytes : 0;
                    LogUtils.i("progress:" + "totalReadBytes-->" + totalReadBytes + "   readBytes-->" + readBytes + "  length-->" + mResponseBody.contentLength());
                    mProgressListener.progress(totalReadBytes, mResponseBody.contentLength(), readBytes == -1);
                    return readBytes;
                }
            });
        }
        return mBufferedSource;
    }
}
