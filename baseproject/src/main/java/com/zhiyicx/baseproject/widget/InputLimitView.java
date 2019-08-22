package com.zhiyicx.baseproject.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.StringRes;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.klinker.android.link_builder.Link;
import com.zhiyicx.baseproject.R;
import com.zhiyicx.common.config.MarkdownConfig;
import com.zhiyicx.common.utils.ConvertUtils;
import com.zhiyicx.common.utils.DeviceUtils;
import com.zhiyicx.common.utils.RegexUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


/**
 * @Describe 限制输入控件 view
 * @Author Jungle68
 * @Date 2017/1/12
 * @Contact master.jungle68@gmail.com
 */

public class InputLimitView extends FrameLayout {

    protected TextView mTvLimitTip;
    protected TextView mBtSend;
    protected ImageView mIvEmoji;

    private int mTopWithKeyboard;
    private int mTopWithEmoji;
    private int mInputViewHeight;
    private View mContentView;


    protected EditText mEtContent;
    protected EditText mEtEmpty;

    private int mLimitMaxSize;// 最大输入值
    private int mShowLimitSize;// 当输入值达到 mShowLimitSize 时，显示提示

    private String mLimitTipStr = "{}/";// 添加格式符号，用户ColorPhrase


    private OnSendClickListener mOnSendClickListener;
    private OnAtTriggerListener mOnAtTriggerListener;

    /**
     * 内容，记录用来比较，判断文字变化是新增还是删除
     */
    private int mContentChar;

    public InputLimitView(Context context) {
        super(context);
        init(context, null);
    }

    public InputLimitView(Context context, int limitMaxSize, int showLimitSize) {
        super(context);
        mLimitMaxSize = limitMaxSize;
        mShowLimitSize = showLimitSize;
        init(context, null);
    }

    public InputLimitView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public InputLimitView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(final Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.view_input_limit_viewgroup, this);
        mTvLimitTip = (TextView) findViewById(R.id.tv_limit_tip);
        mBtSend = (TextView) findViewById(R.id.bt_send);
        mEtContent = (EditText) findViewById(R.id.et_content);
        mEtEmpty = (EditText) findViewById(R.id.et_empty);
        mIvEmoji = (ImageView) findViewById(R.id.iv_emoji);
        if (attrs != null) {
            TypedArray array = context.obtainStyledAttributes(attrs,
                    R.styleable.inputLimitView);
            mLimitMaxSize = array.getInteger(R.styleable.inputLimitView_limitSize, context.getResources().getInteger(R.integer.comment_input_max_size));
            mShowLimitSize = array.getInteger(R.styleable.inputLimitView_showLimitSize, context.getResources().getInteger(R.integer.show_comment_input_size));
            array.recycle();
        }
        if (mLimitMaxSize == 0) {
            mLimitMaxSize = context.getResources().getInteger(R.integer.comment_input_max_size);
        }
        if (mLimitMaxSize == 0) {
            context.getResources().getInteger(R.integer.show_comment_input_size);
        }


        mEtContent.setFilters(new InputFilter[]{new DeleteAtInputFilter(mEtContent), new InputFilter.LengthFilter(mLimitMaxSize)});

        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (TextUtils.isEmpty(s.toString().trim())) {
                    mBtSend.setEnabled(false);
                } else {
                    mBtSend.setEnabled(true);
                }
                if (s.length() >= mShowLimitSize) {
//                    mLimitTipStr = "<" + s.length() + ">" + "/" + mLimitMaxSize;
//                    CharSequence chars = ColorPhrase.from(mLimitTipStr).withSeparator("<>")
//                            .innerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .format();
                    mLimitTipStr = s.length() + "/" + mLimitMaxSize;
//                    CharSequence chars = ColorPhrase.from(mLimitTipStr).withSeparator("<>")
//                            .innerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .outerColor(ContextCompat.getColor(context, R.color.normal_for_assist_text))
//                            .format();
                    mTvLimitTip.setText(mLimitTipStr);
                    mTvLimitTip.setVisibility(VISIBLE);
                } else {
                    mTvLimitTip.setVisibility(GONE);
                }

                boolean isDelete = mContentChar > s.length();
                mContentChar = s.length();
                int atIndex = mEtContent.getSelectionStart() - 1;
                if (atIndex < 0) {
                    return;
                }
                boolean isAt = s.length() >= 1 && MarkdownConfig.AT.equals(String.valueOf(s.charAt(atIndex)));
                if (!isDelete && isAt) {
                    if (mOnAtTriggerListener != null) {
                        mOnAtTriggerListener.onAtTrigger();
                    }
                }
            }
        });

        mBtSend.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mOnSendClickListener != null) {
                    mOnSendClickListener.onSendClick(v, getInputContent());
                    mEtContent.setText("");
                    mContentChar = 0;
                }
            }
        });


        mIvEmoji.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        mEtContent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                showKeyboard();
            }
        });
    }

    public void setEmojiIco(int id){
        mIvEmoji.setImageResource(id);
    }

    private void showEmoji() {
        DeviceUtils.hideSoftKeyboard(getContext(), mEtContent);
//
    }

    private void showKeyboard() {
    }

    private void setContentViewHeight(int i) {
//        if (mContentView == null) {
//            try {
//                mContentView = JZUtils.scanForActivity(getContext()).getWindow().getDecorView().findViewById(android.R.id.content);
//            } catch (Exception ignore) {
//                return;
//            }
//        }
        if (mContentView.getLayoutParams() == null) {
            return;
        }
        mContentView.getLayoutParams().height = i;
    }

    public void setContentView(View v) {
        mContentView = v;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (oldh == 0 && mInputViewHeight == 0) {
            mInputViewHeight = h;
        }
        if (oldh != 0 && mTopWithEmoji == 0) {
            mTopWithEmoji = h;
        }
    }

    @Override
    public void setVisibility(int visibility) {
        super.setVisibility(visibility);
        if (visibility == View.GONE) {
            resetEmoji();
        }
    }

    public void resetEmoji() {
//        mEmojiKeyboard.reset();
        setContentViewHeight(LayoutParams.MATCH_PARENT);
    }

    public void appendAt(String userName) {
        if (userName == null) {
            return;
        }

        String content = mEtContent.getText().toString();

        int selectionIndex = mEtContent.getSelectionStart();
        int atIndex = mEtContent.getSelectionStart() - 1;

        if (!TextUtils.isEmpty(content) && atIndex >= 0) {
            String lastChar = content.substring(atIndex, atIndex + 1);
            if (atIndex >= 1) {
                if (MarkdownConfig.AT_MARK.equals(content.substring(atIndex - 1, atIndex))) {
                    selectionIndex -= 1;
                }
            }
            if (MarkdownConfig.AT.equals(lastChar)) {
                mEtContent.getText().delete(atIndex, atIndex + 1);
            }
        }
        String atContent = getResources().getString(R.string.at_content, userName);
        if (TextUtils.isEmpty(mEtContent.getText().toString())) {
            // 去掉首位空格
            atContent = atContent.replaceFirst("\\u00a0", "");
        }

        if (mEtContent.getText().length() + atContent.length() > mLimitMaxSize) {
            return;
        }

        int newSelection;
        try {
            mEtContent.getText().insert(selectionIndex, atContent);
            newSelection = selectionIndex + atContent.length();
        } catch (Exception ignore) {
            mEtContent.append(atContent);
            newSelection = mEtContent.getText().length();
        }
        setLinks();
        mEtContent.setSelection(newSelection);
    }

    public EditText getEtContent() {
        return mEtContent;
    }

    /**
     * 设置发送按钮点击监听
     *
     * @param onSendClickListener
     */
    public void setOnSendClickListener(OnSendClickListener onSendClickListener) {
        mOnSendClickListener = onSendClickListener;
    }

    public void setOnAtTriggerListener(OnAtTriggerListener onAtTriggerListener) {
        mOnAtTriggerListener = onAtTriggerListener;
    }

    /**
     * 设置发送按钮是否显示
     *
     * @param isVisiable true 显示
     */
    public void setSendButtonVisiable(boolean isVisiable) {
        if (isVisiable) {
            mBtSend.setVisibility(VISIBLE);
        } else {
            mBtSend.setVisibility(GONE);
        }
    }

    /**
     * 设置 hint
     *
     * @param hintStr
     */
    public void setEtContentHint(String hintStr) {
        mEtContent.setHint(hintStr);
    }

    /**
     * 清除焦点
     */
    @Override
    public void clearFocus() {
        mEtContent.clearFocus();
    }

    public void getFocus() {
        mEtEmpty.clearFocus();
        mEtContent.requestFocus();
    }

    /**
     * 设置输入提示
     *
     * @param hint
     */
    public void setTvLimitHint(@StringRes int hint) {
        mTvLimitTip.setHint(hint);
    }

    /**
     * 获取输入内容
     *
     * @return 当前输入内容，去掉前后空格
     */
    public String getInputContent() {
        return RegexUtils.dealAtUserData(mEtContent.getText().toString()).trim();
    }

    public interface OnSendClickListener {
        void onSendClick(View v, String text);
    }

    public interface OnAtTriggerListener {
        void onAtTrigger();
    }

    private void setLinks() {
        List<Link> links = new ArrayList<>();
        Link link = new Link(Pattern.compile(MarkdownConfig.AT_FORMAT))
                .setTextColor(ContextCompat.getColor(getContext(), R.color.important_for_theme))
                .setUnderlined(false)
                .setTextColorOfHighlightedLink(ContextCompat.getColor(getContext(), R.color.important_for_content));
        links.add(link);
        ConvertUtils.stringLinkConvert(mEtContent, links, false);
    }

    public class DeleteAtInputFilter implements InputFilter {

        EditText mEditText;

        public DeleteAtInputFilter(EditText editText) {
            mEditText = editText;
        }

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (dest.length() == 0) {
                return null;
            }
            try {
                boolean isAt = MarkdownConfig.AT_MARK.equals(String.valueOf(dest.charAt(dstart)));
                if (mEditText.getTag() == null && source.length() == 0 && dend > dstart && isAt) {
                    int first = dest.toString().lastIndexOf(MarkdownConfig.AT_MARK, dstart - 1);
                    mEditText.setSelection(first, dend);
                    mEditText.setTag(1L);
                    return dest.subSequence(dstart, dend);
                }
            } catch (Exception ignore) {

            }
            mEditText.setTag(null);
            return null;
        }
    }
}
