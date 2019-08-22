package com.zhiyicx.common.utils;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.widget.EditText;

/**
 * @author LiuChao
 * @describe 监听多个Edittext内容变化的联动，如果都有了内容返回true
 * @date ${DATE}
 * @contact email:450127106@qq.com
 */
public class ManyEdittextContentWatcher {
    private ContentWatcher contentWatcher;
    private EditText[] editTexts;

    public ManyEdittextContentWatcher(ContentWatcher contentWatcher, EditText... editTexts) {
        this.contentWatcher = contentWatcher;
        this.editTexts = editTexts;
        allEdittextHasContent(editTexts);
    }

    public void allEdittextHasContent(EditText... editTexts) {
        if (editTexts != null && editTexts.length > 0) {
            for (EditText editText : editTexts) {
                editText.addTextChangedListener(new ContentTextWatcher());
            }
        }
    }

    private class ContentTextWatcher implements TextWatcher {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            boolean hasContent = true;
            if (editTexts != null && editTexts.length > 0) {
                for (EditText editText : editTexts) {
                    if (TextUtils.isEmpty(editText.getText().toString())) {
                        hasContent = false;
                        break;
                    }
                }

            }else{
                hasContent = false;
            }
            contentWatcher.allHasContent(hasContent);
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    public interface ContentWatcher {
        void allHasContent(boolean hasContent);
    }
}
