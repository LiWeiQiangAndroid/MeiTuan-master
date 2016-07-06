//TODO: 2016/6/23 右侧滑动条
package com.lzctbyong.www.meituan.CityPicker.V;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.lzctbyong.www.meituan.CityPicker.Interface.OnLetterChangedListener;
import com.lzctbyong.www.meituan.R;

import java.util.ArrayList;

/**
 * Created by lzctbyong on 2016/6/23.
 */
public class SideLetterBar extends View {


    private String[] letter;
    private static final String TAG = "SideLetterBar";
    private ArrayList<String> mStrings = new ArrayList<>();

    public SideLetterBar(Context context) {
        super(context);
    }

    public SideLetterBar(Context context, AttributeSet attrs) {
        super(context, attrs);
    }
    
    public SideLetterBar(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setLetter(String mLetter) {
        mStrings.add(mLetter);
    }

    public String[] getLetter() {
        return letter;
    }

    int mHeight;
    int mWidth;
    int choose = -1;
    boolean bg = false;
    
    @SuppressWarnings("deprecation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        letter= (String[]) mStrings.toArray(new String[0]);
        /*按下后，右侧栏的画布背景颜色*/
        if (bg) {
            canvas.drawColor(Color.TRANSPARENT);
        }
        /*屏幕高度*/
        mHeight = getHeight();
        /*屏幕宽度*/
        mWidth = getWidth();
        /*每个letter的高度，均分*/
        int mLetterHeight = mHeight / letter.length;
        /*绘制内容*/
        for (int i = 0; i < letter.length; i++) {
            Paint mPaint = new Paint();
            /* set textsize  */
            mPaint.setTextSize(getResources().getDimension(R.dimen.side_letter_bar_letter_size));
            /* set textcolor */
            mPaint.setColor(getResources().getColor(R.color.bg));
            /* anti—aliasing 抗锯齿 */
            mPaint.setAntiAlias(true);
            /* if select , to deep gray*/
            if (i == choose) {
                mPaint.setColor(getResources().getColor(R.color.colorAccent));
            }
            /* X pos */
            float mX = mWidth / 2 - mPaint.measureText(letter[i]) / 2;
            /* Y pos */
            int mY = mLetterHeight * i + mLetterHeight;
            /* draw */
            canvas.drawText(letter[i], mX, mY, mPaint);
            /* reset Paint attr */
            mPaint.reset();
        }
        
    }
    
    private OnLetterChangedListener onLetterChangedListener;
    /*中央文本*/
    TextView overlaytext;
    
    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        /* 动作 */
        int mAction = event.getAction();
        /* Y坐标 */
        float mY = event.getY();
        OnLetterChangedListener minterface = onLetterChangedListener;
        /* 选择 */
        int mChoose = choose;
        /*  mY = mHeight / letter.length * i + letter.length */
        final int c = (int) (mY / getHeight() * letter.length);
        
        switch (mAction) {
            /* 按下 */
            case MotionEvent.ACTION_DOWN:
                /* 使能画布背景 */
                bg = true;

                /* 区域内 */
                if (mChoose != c && minterface != null) {
                    if (c >= 0 && c < letter.length) {
                        minterface.onLetterChanged(letter[c], c);
                        /* 更新位置 */
                        mChoose = c;
                        /* 刷新 -> ondraw() */
                        invalidate();
                        /* 显示文本 */
                        overlaytext.setVisibility(VISIBLE);
                        overlaytext.setText(letter[c]);
                    }
                }
                break;

            /* 移动 */
            case MotionEvent.ACTION_MOVE:
                if (mChoose != c && minterface != null) {
                    if (c >= 0 && c < letter.length) {
                        minterface.onLetterChanged(letter[c], c);
                        choose = c;
                         /* 刷新 -> ondraw() */
                        invalidate();
                         /* 显示文本 */
                        overlaytext.setVisibility(VISIBLE);
                        overlaytext.setText(letter[c]);
                    }
                }
                break;

            /* 抬起 */
            case MotionEvent.ACTION_UP:
                /* 不要画布背景 */
                bg = false;
                /* 重置 */
                choose = -1;
                 /* 刷新 -> ondraw() */
                invalidate();
                /* 文本消失 */
                overlaytext.setVisibility(GONE);
                break;
        }
        return true;
    }

    
    public void setOnLetterChangedListener(OnLetterChangedListener onLetterChangedListener) {
        this.onLetterChangedListener = onLetterChangedListener;
    }

    /*得到textview控件*/
    public void postview(TextView overlay) {
        this.overlaytext = overlay;
    }

    
}
