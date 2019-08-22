package com.haohaishengshi.haohaimusic.service.backgroundtask;

import android.content.Context;
import android.net.Uri;
import android.os.Environment;

import com.zhiyicx.baseproject.impl.photoselector.ImageBean;
import com.zhiyicx.common.utils.FileUtils;
import com.zycx.luban.Checker;
import com.zycx.luban.CompressionPredicate;
import com.zycx.luban.Engine;
import com.zycx.luban.InputStreamProvider;
import com.zycx.luban.Luban;
import com.zycx.luban.OnCompressListener;
import com.zycx.luban.OnRenameListener;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @Author Jliuer
 * @Date 2018/06/11/14:10
 * @Email Jliuer@aliyun.com
 * @Description 适合ts+的图片压缩，继承于Luban
 */
public class ImageCompress extends Luban {

    public ImageCompress(Builder builder) {
        super(builder);
    }

    public static TBuilder with(Context context) {
        return new TBuilder(context);
    }

    public static class TBuilder extends Builder {
        public TBuilder(Context context) {
            super(context);
        }


        public TBuilder load(final ImageBean imageBean) {
            mStreamProviders.add(new DataProvider() {
                @Override
                public InputStream open() throws IOException {
                    return new FileInputStream(imageBean.getImgUrl());
                }

                @Override
                public String getPath() {
                    return imageBean.getImgUrl();
                }

                @Override
                public ImageBean getImageBean() {
                    return imageBean;
                }
            });
            return this;
        }

        @Override
        public <T> TBuilder load(List<T> list) {
            for (T src : list) {
                if (src instanceof String) {
                    load((String) src);
                } else if (src instanceof File) {
                    load((File) src);
                } else if (src instanceof Uri) {
                    load((Uri) src);
                } else if (src instanceof ImageBean) {
                    load((ImageBean) src);
                } else {
                    throw new IllegalArgumentException("Incoming data type exception, it must be String, File, Uri or Bitmap");
                }
            }
            return this;
        }

        @Override
        protected ImageCompress build() {
            return new ImageCompress(this);
        }

        @Override
        public TBuilder load(InputStreamProvider inputStreamProvider) {
            super.load(inputStreamProvider);
            return this;
        }

        @Override
        public TBuilder load(File file) {
            super.load(file);
            return this;
        }

        @Override
        public TBuilder load(String string) {
            super.load(string);
            return this;
        }

        @Override
        public TBuilder load(Uri uri) {
            super.load(uri);
            return this;
        }

        @Override
        public TBuilder putGear(int gear) {
            super.putGear(gear);
            return this;
        }

        @Override
        public TBuilder setRenameListener(OnRenameListener listener) {
            super.setRenameListener(listener);
            return this;
        }

        @Override
        public TBuilder setCompressListener(OnCompressListener listener) {
            super.setCompressListener(listener);
            return this;
        }

        @Override
        public TBuilder setTargetDir(String targetDir) {
            super.setTargetDir(targetDir);
            return this;
        }

        @Override
        public TBuilder ignoreBy(int size) {
            super.ignoreBy(size);
            return this;
        }

        @Override
        public TBuilder filter(CompressionPredicate compressionPredicate) {
            super.filter(compressionPredicate);
            return this;
        }

        @Override
        public void launch() {
            super.launch();
        }

        @Override
        public File get(String path) throws IOException {
            return super.get(path);
        }

        @Override
        public List<File> get() throws IOException {
            return build().get(context);
        }
    }

    @Override
    protected File get(InputStreamProvider path, Context context) throws IOException {
        return compress(context, path);
    }

    @Override
    protected List<File> get(Context context) throws IOException {
        List<File> results = new ArrayList<>();
        Iterator<InputStreamProvider> iterator = mStreamProviders.iterator();

        while (iterator.hasNext()) {
            results.add(compress(context, iterator.next()));
            iterator.remove();
        }

        return results;
    }

    @Override
    protected File compress(Context context, InputStreamProvider path) throws IOException {
        File result;

        File outFile = getImageCacheFile(context, Checker.SINGLE.extSuffix(path.getPath()));

        if (mRenameListener != null) {
            String filename = mRenameListener.rename(path.getPath());
            String suffix = FileUtils.getSuffix(new File(path.getPath()));
            if (suffix != null) {
                filename += "." + suffix;
            }
            outFile = getImageCustomFile(context, filename);
        }

        // 自动以的 过滤 & 内容提供者
        Filter filter = null;
        DataProvider provider = null;

        if (mCompressionPredicate != null) {
            if (mCompressionPredicate instanceof Filter) {
                filter = (Filter) mCompressionPredicate;
            }
            if (path instanceof DataProvider) {
                provider = (DataProvider) path;
            }
            if (filter != null && provider != null) {
                if (filter.apply(path.getPath()) && filter.apply(provider.getImageBean())
                        && Checker.SINGLE.needCompress(mLeastCompressSize, path.getPath())) {
                    result = new Engine(path, outFile).compress();
                } else {
                    result = new File(path.getPath());
                }
            } else {
                if (mCompressionPredicate.apply(path.getPath())
                        && Checker.SINGLE.needCompress(mLeastCompressSize, path.getPath())) {
                    result = new Engine(path, outFile).compress();
                } else {
                    result = new File(path.getPath());
                }
            }
        } else {
            result = Checker.SINGLE.needCompress(mLeastCompressSize, path.getPath()) ?
                    new Engine(path, outFile).compress() :
                    new File(path.getPath());
        }
        if (provider != null) {
            provider.getImageBean().setImgUrl(result.getAbsolutePath());
        }
        return result;
    }

    public abstract static class DataProvider implements InputStreamProvider {
        public abstract ImageBean getImageBean();
    }

    public abstract static class Filter implements CompressionPredicate {
        @Override
        public boolean apply(String path) {
            return false;
        }

        public abstract boolean apply(ImageBean imageBean);
    }

    private String getPath() {
        String path = Environment.getExternalStorageDirectory() + "/TSPlusPhotoView/compress/";
        File file = new File(path);
        if (file.mkdirs()) {
            return path;
        }
        return path;
    }
}
