package com.nerminturkovic.flickrtestapp.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ImageView;

/**
 * Created by nerko on 09/12/2016.
 */

public class ZoomableImageView extends ImageView implements View.OnTouchListener {

    private ScaleGestureDetector scaleGestureDetector;
    private Matrix matrix = getImageMatrix();
    private float scale = 1;

    public ZoomableImageView(Context context) {
        this(context, null);
    }

    public ZoomableImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZoomableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initView();
    }

    private void initView() {
        scaleGestureDetector = new ScaleGestureDetector(getContext(), new ScaleListener());
        setOnTouchListener(this);

        setScaleType(ScaleType.MATRIX);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        scaleGestureDetector.onTouchEvent(event);
        return true;
    }

    private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
        @Override
        public boolean onScale(ScaleGestureDetector detector) {
            scale = scale * detector.getScaleFactor();
            scale = Math.max(1f, Math.min(scale, 5.0f));
            matrix.setScale(scale, scale);
//            matrix.setRectToRect(new RectF())
            setImageMatrix(matrix);

            return true;
        }
    }
}
