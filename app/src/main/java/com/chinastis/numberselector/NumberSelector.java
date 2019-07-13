package com.chinastis.numberselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.List;

/**
 * Created by xianglong on 2019/7/13.
 */

public class NumberSelector extends LinearLayout {

    private Context context;
    private Paint paint;

    private float width;
    private float height;

    private int textSize;
    private int color;

    private float maxWeight;
    private float minWeight;
    private float range;

    private RecyclerView degreeRV;
    private RecyclerView numberRV;

    private List<Float> numberList;
    private List<Float> degreeList;

    public NumberSelector(Context context) {
        super(context);
    }

    public NumberSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.number_selector_layout,this);


        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.NumberSelector);
        maxWeight = ta.getFloat(R.styleable.NumberSelector_max_number,10);
        minWeight = ta.getFloat(R.styleable.NumberSelector_min_number,0);
        range = ta.getFloat(R.styleable.NumberSelector_range,0.1f);
        ta.recycle();
        initData();
        intView();
        intiPaint();

    }

    private void initData() {
        for (float f = minWeight; f <= maxWeight; f += range) {
            degreeList.add(f);

            Log.e("MENG","度数："+f);

            if ((f-minWeight)%range==0) {
                numberList.add(f);
                Log.e("MENG","数字："+f);
            }
        }

    }

    private void intView() {
        degreeRV = (RecyclerView) this.findViewById(R.id.degree_rv);
        numberRV = (RecyclerView) this.findViewById(R.id.number_rv);


    }

    public NumberSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    private void intiPaint() {
        this.paint = new Paint();
        paint.setAntiAlias(true);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        if (widthMode == MeasureSpec.EXACTLY) {
            width = MeasureSpec.getSize(widthMeasureSpec);
        } else {
            width = 400;
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = MeasureSpec.getSize(heightMeasureSpec);
        } else {
            height = 80;
        }

        setMeasuredDimension((int)width,(int)height);

    }

    @Override
    protected void onDraw(Canvas canvas) {

    }




    class NumberSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        private List<Float> data;
        private Context mContext;


        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }


        class NumberSelectorViewHolder extends RecyclerView.ViewHolder {

            public NumberSelectorViewHolder(View itemView) {
                super(itemView);
            }
        }
    }
}
