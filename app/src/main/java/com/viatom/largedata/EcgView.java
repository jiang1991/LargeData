package com.viatom.largedata;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

public class EcgView extends View {

    private float[] fs;

    private Paint paint;
    private Canvas canvas;

    public int mWidth;
    public int mHeight;
    public int mTop;
    public int mBottom;
    public int mBase;

    private float rowheight;

    public EcgView(Context context) {
        super(context);
        init(null, 0);
    }

//    public EcgView(Context context, byte[] bytes) {
//        super(context);
//        init(null, 0);
//        fs = ByteUtils.bytes2mvs(bytes);
//    }

    public EcgView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(attrs, 0);
    }

    public EcgView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(attrs, defStyle);
    }

    public void setData(float[] fs) {
        this.fs = fs;
//        this.invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        iniParam();

        this.canvas = canvas;

        drawBkg();
        drawWave();
    }

    private void drawBkg() {
        Path p = new Path();

        for (int i = 0; i<DataController.ECG_VIEW_ROW; i++) {
            float y = rowheight * i;
            p.moveTo(0, y);
            p.lineTo(mWidth, y);
        }

        canvas.drawPath(p, paint);
    }

    private void drawWave() {
        if (fs == null) {
            canvas.drawText("no data", mWidth/2f, mHeight/2f ,paint);
            return;
        }

        int max = (int) Math.ceil(fs.length / (double) (10*125));


//        canvas.drawText("max: " + max, mWidth/2f, mHeight/2f ,paint);

        for (int i = 0; i<max; i++) {
            // every row
            int len = 10*125;
            if (i == max-1) {
                len = fs.length - len * i;
            }

            float[] temp = new float[len];
            System.arraycopy(fs, 10*125*i, temp, 0, len);
//            for (int m = 0; m<len; m++) {
//                temp[m] = (float) (Math.sin((double) m/50)*rowheight*0.4);
//            }

            Path p = new Path();

            for (int j = 0; j<len; j++) {
                if (j == 0) {
                    p.moveTo(0, temp[0]*rowheight*0.4f + rowheight * (i+0.5f));
                } else {
                    float x = (float) mWidth * j / 1250;
                    float y = temp[j]*rowheight*0.4f + rowheight * (i+0.5f);
                    p.lineTo(x, y);
                }
            }

            canvas.drawPath(p, paint);
        }


    }


    private void iniParam() {


        mWidth = getWidth();
        mHeight = getHeight();

        mBase = (mHeight / 2);
        mTop = (int) (mBase - 200);
        mBottom = (int) (mBase + 200);

        rowheight = (float) mHeight/DataController.ECG_VIEW_ROW;
    }

    private void init(AttributeSet attrs, int defStyle) {
        // Load attributes
        final TypedArray a = getContext().obtainStyledAttributes(
                attrs, R.styleable.EcgView, defStyle, 0);

        a.recycle();

        iniPaint();

    }



    private void iniPaint() {

        paint = new Paint();
        paint.setTextAlign(Paint.Align.LEFT);
        paint.setTextSize(32);
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL_AND_STROKE);
        paint.setStrokeWidth(1.0f);
    }
}
