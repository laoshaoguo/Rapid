package com.haohaishengshi.haohaimusic.data.source.local.data_convert;

import com.zhiyicx.common.utils.ConvertUtils;

import org.greenrobot.greendao.converter.PropertyConverter;

/**
 * @Author Jliuer
 * @Date 2017/07/13/10:05
 * @Email Jliuer@aliyun.com
 * @Description
 */
public class BaseConvert<T> implements PropertyConverter<T,String> {
    @Override
    public T convertToEntityProperty(String databaseValue) {
        if (databaseValue == null) {
            return null;
        }
        return ConvertUtils.base64Str2Object(databaseValue);
    }

    @Override
    public String convertToDatabaseValue(T entityProperty) {
        if (entityProperty == null) {
            return null;
        }
        return ConvertUtils.object2Base64Str(entityProperty);
    }
}
