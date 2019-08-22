package com.klinker.android.link_builder;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.RestrictTo;
import android.support.annotation.StringDef;
import android.support.v4.util.ArrayMap;

import java.io.Serializable;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Set;

import static android.support.annotation.RestrictTo.Scope.GROUP_ID;

/**
 * @Author Jliuer
 * @Date 2017/04/26/11:08
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class LinkMetadata {
    private static final String TAG = "LinkMetadata";

    public enum SpanType {

        /**
         * 用户名类型
         */
        USER_NAME,

        /**
         * 网址
         */
        NET_SITE,

        /**
         * @
         */
        AT_SITE

    }

    @LongKey
    public static final String METADATA_KEY_USER_ID = "zhiyi.common.metadata.user_id";

    @IntegerKey
    public static final String METADATA_KEY_SOURCE_ID = "zhiyi.common.metadata.source_id";

    @TextKey
    public static final String METADATA_KEY_COTENT = "zhiyi.common.metadata.content";

    @PCObjectKey
    public static final String METADATA_KEY_USER = "zhiyi.common.metadata.user";

    @SZObjectKey
    public static final String METADATA_KEY_TYPE = "zhiyi.common.metadata.type";

    private static final int METADATA_TYPE_LONG = 0;
    private static final int METADATA_TYPE_STRING = 1;
    private static final int METADATA_TYPE_INTEGER = 2;
    private static final int METADATA_TYPE_SERIALIZABLEOBJECT = 4;
    private static final int METADATA_TYPE_PARCELABLEOBJECT = 5;

    private static final ArrayMap<String, Integer> METADATA_KEYS_TYPE;

    private final Bundle mBundle;

    private LinkMetadata(Bundle bundle) {
        mBundle = bundle;
    }

    public Builder newBuilder() {
        return new Builder(this);
    }

    public static Builder builder() {
        return new Builder();
    }

    static {
        METADATA_KEYS_TYPE = new ArrayMap<>();

        METADATA_KEYS_TYPE.put(METADATA_KEY_SOURCE_ID, METADATA_TYPE_INTEGER);

        METADATA_KEYS_TYPE.put(METADATA_KEY_COTENT, METADATA_TYPE_SERIALIZABLEOBJECT);

        METADATA_KEYS_TYPE.put(METADATA_KEY_USER_ID, METADATA_TYPE_LONG);

        METADATA_KEYS_TYPE.put(METADATA_KEY_USER, METADATA_TYPE_PARCELABLEOBJECT);

        METADATA_KEYS_TYPE.put(METADATA_KEY_TYPE, METADATA_TYPE_SERIALIZABLEOBJECT);
    }

    /**
     * @hide
     */
    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_COTENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface TextKey {
    }

    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_USER_ID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface LongKey {
    }

    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_SOURCE_ID})
    @Retention(RetentionPolicy.SOURCE)
    public @interface IntegerKey {
    }

    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_USER})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PCObjectKey {
    }

    @RestrictTo(GROUP_ID)
    @StringDef({METADATA_KEY_TYPE, METADATA_KEY_COTENT})
    @Retention(RetentionPolicy.SOURCE)
    public @interface SZObjectKey {
    }

    public int size() {
        return mBundle.size();
    }

    public Set<String> keySet() {
        return mBundle.keySet();
    }

    public static final class Builder {

        private final Bundle mBundle;

        public Builder() {
            mBundle = new Bundle();
        }

        public Builder(LinkMetadata source) {
            mBundle = new Bundle(source.mBundle);
        }

        public Builder putString(@TextKey String key, String value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_STRING) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a CharSequence");
                }
            }
            mBundle.putString(key, value);
            return this;
        }


        public Builder putInteger(@IntegerKey String key, Integer value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_INTEGER) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Integer");
                }
            }
            mBundle.putInt(key, value);
            return this;
        }

        public Builder putLong(@LongKey String key, Long value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_LONG) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Long");
                }
            }
            mBundle.putLong(key, value);
            return this;
        }

        public Builder putParcelableObj(@PCObjectKey String key, Parcelable value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_PARCELABLEOBJECT) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Parcelable");
                }
            }
            mBundle.putParcelable(key, value);
            return this;
        }

        public Builder putSerializableObj(@SZObjectKey String key, Serializable value) {
            if (METADATA_KEYS_TYPE.containsKey(key)) {
                if (METADATA_KEYS_TYPE.get(key) != METADATA_TYPE_SERIALIZABLEOBJECT) {
                    throw new IllegalArgumentException("The " + key
                            + " key cannot be used to put a Serializable");
                }
            }
            mBundle.putSerializable(key, value);
            return this;
        }

        public LinkMetadata build() {
            return new LinkMetadata(mBundle);
        }
    }

    public String getString(@TextKey String key) {
        return mBundle.getString(key);
    }

    public Object getPCObj(@PCObjectKey String key) {
        return mBundle.getParcelable(key);
    }

    public Object getSeriObj(@SZObjectKey String key) {
        return mBundle.getSerializable(key);
    }

    public SpanType getSZObj(@SZObjectKey String key) {
        return (SpanType) mBundle.getSerializable(key);
    }

    public long getLong(@LongKey String key) {
        return mBundle.getLong(key, -1);
    }

    public int getInteger(@IntegerKey String key) {
        return mBundle.getInt(key, -1);
    }
}
