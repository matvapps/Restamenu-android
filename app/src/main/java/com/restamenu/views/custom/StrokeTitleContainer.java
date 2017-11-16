package com.restamenu.views.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.widget.FrameLayout;

import com.restamenu.R;
import com.restamenu.util.DimensionUtil;

/**
 * Created by Alexandr.
 */

public class StrokeTitleContainer extends FrameLayout {

    private RectF rectDest;
    private RectF rectSrc;

    private int viewWidth;
    private int viewHeight;

    private float strokeSize;
    private float radius;
    private String hintText;
    private float fontSize = DimensionUtil.convertDpToPixel(12, getContext());
    private int textColor;
    private int strokeColor;


    private Paint strokePaint;
    private Paint fontPaint;



    private void init() {

        setWillNotDraw(false);
        rectDest = new RectF();
        rectSrc = new RectF();
        setLayerType(LAYER_TYPE_SOFTWARE, null);

        strokePaint = new Paint();
        fontPaint = new Paint();

        fontPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        fontPaint.setColor(textColor);
        fontPaint.setTextSize(fontSize);
        fontPaint.setStyle(Paint.Style.FILL_AND_STROKE);


        strokePaint.setStyle(Paint.Style.STROKE);
        strokePaint.setColor(strokeColor);
        strokePaint.setStrokeWidth(strokeSize);


        int padding = (int) DimensionUtil.convertDpToPixel(16, getContext());
        setPadding(padding, padding, padding, padding);
    }

    private void getStyleableAttributes(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.StrokeTitleContainer);

        strokeSize = typedArray.getDimension(
                        R.styleable.StrokeTitleContainer_stc_stroke_size,
                        2);


        radius = typedArray.getFloat(
                        R.styleable.StrokeTitleContainer_stc_radius,
                        2);

        hintText = typedArray.getString(
                R.styleable.StrokeTitleContainer_stc_hintText);

        fontSize = typedArray.getDimension(
                R.styleable.StrokeTitleContainer_stc_font_size,
                fontSize);

        strokeColor = typedArray.getColor(R.styleable.StrokeTitleContainer_stc_stroke_color,
                ContextCompat.getColor(getContext(), R.color.colorMaterialGrey));

        textColor = typedArray.getColor(R.styleable.StrokeTitleContainer_stc_text_color,
                ContextCompat.getColor(getContext(), R.color.colorMaterialGrey));

        typedArray.recycle();

    }

    public StrokeTitleContainer(Context context, float strokeSize, float radius, String hintText, float fontSize) {
        super(context);

        this.strokeSize = strokeSize;
        this.radius = radius;
        this.hintText = hintText;
        this.fontSize = fontSize;

        init();
    }

    public StrokeTitleContainer(Context context, AttributeSet attrs) {
        super(context, attrs);

        getStyleableAttributes(context, attrs);
        init();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = viewWidth;
        int height = viewHeight;

        float textShift = DimensionUtil.convertDpToPixel(4, getContext());
        float shift = radius + DimensionUtil.convertDpToPixel(16, getContext());

        rectDest.set(fontSize / 2,
                fontSize / 2,
                width - fontSize / 2,
                height - fontSize / 2);


        strokePaint.setStrokeWidth(strokeSize);
        canvas.drawRoundRect(rectDest, radius, radius, strokePaint);  // stroke

        if (hintText != null) {
            float textWidth = fontPaint.measureText(hintText);
            rectSrc.set(shift, 0, textWidth + shift + 2 * textShift, fontSize);

            canvas.drawRect(rectSrc, getFillPaint(Color.RED, PorterDuff.Mode.DST_OUT)); // text hole
            canvas.drawText(hintText, shift + textShift, fontSize - 2 * strokeSize, fontPaint); // text
        }

    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);



    }

    private Paint getFillPaint(int color, PorterDuff.Mode mode) {
        Paint paint = new Paint();

        paint.setAntiAlias(true);
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(color);

        paint.setXfermode(new PorterDuffXfermode(mode));

        return paint;
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewWidth = w;
        viewHeight = h;

    }

    public float getFontSize() {
        return fontSize;
    }

    public void setFontSize(float fontSize) {
        this.fontSize = fontSize;
    }

    public float getStrokeSize() {
        return strokeSize;
    }

    public void setStrokeSize(float strokeSize) {
        this.strokeSize = strokeSize;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public String getHintText() {
        return hintText;
    }

    public void setHintText(String hintText) {
        this.hintText = hintText;
    }

}
