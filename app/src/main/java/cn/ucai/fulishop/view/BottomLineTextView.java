package cn.ucai.fulishop.view;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.TextView;

import cn.ucai.fulishop.R;
import cn.ucai.fulishop.utils.PhoneUtil;

public class BottomLineTextView extends TextView {

    private Paint paint;

    private boolean isShowLine = true;

    public BottomLineTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public BottomLineTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public BottomLineTextView(Context context) {
        super(context);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(context.getResources().getColor(R.color.light_blue));
        paint.setStrokeWidth(PhoneUtil.dp2px(context, 2));
    }

    public void setShowLine(boolean isShowLine) {
        this.isShowLine = isShowLine;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (isShowLine) {
            canvas.drawLine(0, getHeight(), getWidth(),
                    getHeight(), paint);
        }
        super.onDraw(canvas);
    }

}
