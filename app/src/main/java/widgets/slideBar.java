package widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import com.example.myqq.R;
import com.hyphenate.util.DensityUtil;

/**
 * Created by 我是小丑逼 on 2017/1/9.
 */

public class slideBar extends View {
    private static final String[] SECTIONS = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N", "O",
            "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};
    private Paint mPaint;
    private float mTextSize = 20;

    public slideBar(Context context) {
        this(context, null);
    }

    public slideBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setTextSize(DensityUtil.sp2px(getContext(), 12));
        mPaint.setAntiAlias(true);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setColor(getResources().getColor(R.color.slide_bar_text_color));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {

        mTextSize = h*1.0f / SECTIONS.length;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        float x = getWidth()*1.0f / 2;
        float y = mTextSize;
        for (int i = 0; i < SECTIONS.length; i++) {
            canvas.drawText(SECTIONS[i], x, y, mPaint);
            y+=mTextSize;
        }
    }
}
