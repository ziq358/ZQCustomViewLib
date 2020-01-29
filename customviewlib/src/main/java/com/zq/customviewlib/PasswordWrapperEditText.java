package com.zq.customviewlib;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.text.InputType;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.inputmethod.InputConnectionWrapper;
import android.widget.EditText;

import androidx.appcompat.widget.AppCompatEditText;


public class PasswordWrapperEditText extends AppCompatEditText {
    public static final int LENGTH = 6;
    private int textLength = LENGTH;
    private Paint paint = null;
    private int size = 0;
    private Drawable enable = null;
    private Drawable disable = null;
    private boolean hideInput = true;
    private int distance;

    private volatile boolean isNew = false;
    private StringBuffer password = new StringBuffer();
    private IOnInputListener listener;

    public interface IOnInputListener {
        void onInput(boolean val, String text);
    }

    public PasswordWrapperEditText(Context context) {
        super(context);//使用this(context, null) 会弹不出键盘，待研究
        initView(context, null, 0);
    }


    public PasswordWrapperEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs, 0);
    }

    public PasswordWrapperEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs, defStyleAttr);
    }

    private void initView(Context context, AttributeSet attrs, int defStyle) {
        setInputType(InputType.TYPE_CLASS_NUMBER);
        setLongClickable(false);
        setTextIsSelectable(false);
        setCursorVisible(false);
        setBackgroundDrawable(null);

        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PasswordWrapperEditText, defStyle, 0);
        hideInput = a.getBoolean(R.styleable.PasswordWrapperEditText_hide_input, true);
        textLength = a.getInteger(R.styleable.PasswordWrapperEditText_text_length, LENGTH);
        int enableId = a.getResourceId(R.styleable.PasswordWrapperEditText_drawable_after_input, R.drawable.input_room_password_mask_enable);
        int disableId = a.getResourceId(R.styleable.PasswordWrapperEditText_drawable_before_input, R.drawable.input_room_password_mask_disable);

        size = context.getResources().getDimensionPixelSize(R.dimen.dimens_dip_12);
        size = a.getDimensionPixelSize(R.styleable.PasswordWrapperEditText_drawable_size, size);
        int sp = context.getResources().getDimensionPixelOffset(R.dimen.dimens_sp_19);
        sp = a.getDimensionPixelSize(R.styleable.PasswordWrapperEditText_text_size, sp);

        enable = context.getResources().getDrawable(enableId);
        enable.setBounds(0, 0, size, size);
        disable = context.getResources().getDrawable(disableId);
        disable.setBounds(0, 0, size, size);
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        paint.setTextSize(sp);
        paint.setColor(Color.rgb(0x35, 0x35, 0x35));
        paint.setTextAlign(Paint.Align.CENTER);
        //计算baseline 到 文本中心的偏移
        Paint.FontMetrics fontMetrics = paint.getFontMetrics();
        distance = (int)((fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        switch (heightMode) {
            case MeasureSpec.EXACTLY:
                heightSize = MeasureSpec.getSize(heightMeasureSpec);
                break;
            case MeasureSpec.AT_MOST:
                heightSize = widthSize / textLength;
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onTextChanged(CharSequence text, int start, int lengthBefore, int lengthAfter) {
        super.onTextChanged(text, start, lengthBefore, lengthAfter);
        //开始处理文本输入
        if (null == password) {
            return;
        }
        int len = password.length();
        if (len < textLength && text.length() > 0 && TextUtils.isDigitsOnly(text)) {
            password.append(text);
            isNew = true;
            notifyObservers();
        }
        //清除输入
        if (text.length() > 0) {
            setText("");
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        int len = password.length();
        if (keyCode == KeyEvent.KEYCODE_DEL && len > 0) {
            password.deleteCharAt(len - 1);
            postInvalidate();
            notifyObservers();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public InputConnection onCreateInputConnection(EditorInfo outAttrs) {
        return new CustomInputConnection(super.onCreateInputConnection(outAttrs), true);
    }

    //修复某些输入法，如google自带输入法，删除按钮，监听不回调问题
    private class CustomInputConnection extends InputConnectionWrapper {

        public CustomInputConnection(InputConnection target, boolean mutable) {
            super(target, mutable);
        }

        @Override
        public boolean deleteSurroundingText(int beforeLength, int afterLength) {
            //在删除时，输入框无内容，或者删除以后输入框无内容
            if (beforeLength == 1 || afterLength == 0 || beforeLength == 0) {
                // backspace
                return sendKeyEvent(new KeyEvent(KeyEvent.ACTION_DOWN , KeyEvent.KEYCODE_DEL))
                        && sendKeyEvent(new KeyEvent(KeyEvent.ACTION_UP, KeyEvent.KEYCODE_DEL));
            }
            return super.deleteSurroundingText(beforeLength, afterLength);
        }

    }

    private void notifyObservers() {
        if (null != listener) {
            String password = getPassword();
            listener.onInput(!TextUtils.isEmpty(password), password);
        }
        //数字显示状态时间计时
        removeCallbacks(timeRunnable);
        if (this.password.length() > 0 && isNew) {
            postDelayed(timeRunnable, 2000);
        }
    }

    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            isNew = false;
            postInvalidate();
        }
    };


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //绘制
        int vWidth = Math.min(getMeasuredHeight(), getMeasuredWidth() / textLength);
        int hSpace = getMeasuredWidth() / textLength - vWidth;
        int vSpace = (getMeasuredHeight() - size) >> 1;
        int pLength = password.length();
        canvas.save();
        for (int i = 0; i < textLength; i++) {
            canvas.translate((vWidth + hSpace - size) >> 1, vSpace);
            if (i < pLength) {
                //添加多一参数控制延时后是否隐藏
                if (!hideInput || (i == pLength - 1 && isNew)) {//显示数字
                    String c = password.substring(i, i+1);
                    int baseline = (size >> 1)+distance;//调整baseline偏移
                    canvas.drawText(c, size >> 1, baseline, paint);
                } else {//显示已输入
                    enable.draw(canvas);
                }
            } else {//显示未输入
                disable.draw(canvas);
            }
            canvas.translate((vWidth + hSpace + size) >> 1, -vSpace);
        }
        canvas.restore();
    }

    public void clear() {
        int len = password.length();
        if (len > 0) {
            password.delete(0, len);
            notifyObservers();
        }
        setText("");
    }


    public void setTextLength(int textLength) {
        this.textLength = textLength;
        postInvalidate();
    }

    public void setListener(IOnInputListener listener) {
        this.listener = listener;
    }

    //返回6位密码
    public String getPassword() {
        if (password.length() < textLength) {
            return "";
        }
        return password.toString();
    }
}
