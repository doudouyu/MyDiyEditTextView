package com.example.administrator.mydiyedittextview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;


/**
 * Created by Administrator on 2016/11/10.
 */
public class DiyEditText extends RelativeLayout {

    private EditText editText;
    private TextView textView;
    private ImageView error;
    private ImageView delete;
    private ImageView lookImage;
    private ValueAnimator animater;
    public final float scalesize = 4.0f;
    private int dy;
    private String hint;

    public DiyEditText(Context context) {
        super(context);
        initView(context,null);
    }

    public DiyEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context,attrs);
    }

    public DiyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context,attrs);
    }

    public void initView(Context context, AttributeSet attrs) {
        TypedArray array = context.obtainStyledAttributes(attrs,R.styleable.DiyEditText);
        hint = array.getString(R.styleable.DiyEditText_hint);
        View view = LayoutInflater.from(context).inflate(R.layout.diy_edit_text_layout, this);
        editText = (EditText) view.findViewById(R.id.et_login);
        textView = (TextView) view.findViewById(R.id.tv_login_name);
        error = (ImageView) view.findViewById(R.id.image_error);
        delete = (ImageView) view.findViewById(R.id.image_delete);
        lookImage = (ImageView) view.findViewById(R.id.image_look);
        //给editText设置文字改变的监听,当右键点的时候设置文本输入
        //给editText设置焦点改变的监听， //如果有焦点，就隐藏error图片//如果有焦点而且输入框中没有文字，就开启动画
        // 如果没有焦点，且文本框中没有文字，就判断动画对象是否为空，不为空，就reverse
        //给editText设置文字改变的监听，当文字改变且输入文本的长度大于0的时候，显示clear图片
        //隐藏密码的图片设置点击事件，如果点击的时候，密码没有显示就显示密码，替换图片，设置标记为true否则。。。相反
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    error.setVisibility(View.GONE);
                    editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    startAnim();
                } else if (hasFocus && editText.getText().toString().length() == 0) {
                    lookImage.setVisibility(View.GONE);

                } else if (!hasFocus && editText.getText().toString().length() == 0) {
                    if (animater != null) {
                        animater.reverse();
                    }
                    animater = null;
                    lookImage.setVisibility(View.GONE);
                    delete.setVisibility(View.GONE);
                }
            }
        });
        editText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (editText.getText().toString().length() > 0) {
                    delete.setVisibility(View.VISIBLE);
                    lookImage.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        delete.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                editText.setText("");
                delete.setVisibility(View.GONE);
                lookImage.setVisibility(View.GONE);
                editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
            }
        });
        lookImage.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                //获取当前密的显示状态
                editText.getTransformationMethod();
                if (HideReturnsTransformationMethod.getInstance().equals(editText.getTransformationMethod())) {
                       editText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }else {
                    editText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                Log.i("当前密码的状态：", editText.getTransformationMethod().toString() + "aaaaaa");

            }
        });
        if (hint!=null){
            textView.setText(hint);
        }
    }

    private void startAnim() {
        //如果animater不为空，说明当前动画未结束，退出此方法
        if (animater != null) {
            return;
        }
        dy = (editText.getHeight() - textView.getHeight() / 2) - textView.getHeight() * 2;
        animater = ValueAnimator.ofFloat(0, dy);
        Log.i("高度", (editText.getHeight() - textView.getHeight()) / 2 - textView.getHeight() + "");
        animater.setTarget(textView);
        animater.setDuration(200);
        animater.start();
        animater.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //属性动画加一个平移动画
                textView.setTranslationY((Float) animation.getAnimatedValue());
                float bit = animation.getAnimatedFraction();
                textView.setTextSize(18 - scalesize * bit);
            }
        });

    }

}
