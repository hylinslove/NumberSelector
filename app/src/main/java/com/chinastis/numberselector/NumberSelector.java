package com.chinastis.numberselector;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.annotation.Nullable;

import android.support.annotation.RequiresApi;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by xianglong on 2019/7/13.
 */


public class NumberSelector extends LinearLayout {

    private Context context;
    private Paint paint;

    private float width;
    private float height;
    private int strokeWidth;

    private int textSize;
    private int color;

    private int maxWeight;
    private int minWeight;
    private float range;

    private RecyclerView degreeRV;
    private RecyclerView numberRV;

    private List<Integer> numberList = new ArrayList<>();
    private List<Integer> degreeList = new ArrayList<>();

    private final int NUMBER_TYPE = 0;
    private final int DEGREE_TYPE = 1;

    private LinearLayoutManager layoutManagerDegree;
    private LinearLayoutManager layoutManagerNumber;
    private int selectedPosition = -1;

    private int selectedPositionNum = -1;

    private SparseArray<ImageView> imageViews = new SparseArray<>();
    private SparseArray<TextView> numberTexts = new SparseArray<>();

    private Drawable degree;
    private Drawable degree1;
    private Drawable degree2;
    private Drawable degree3;


    private float originalNumber;
    private OnNumberChangeListener onNumberChangeListener;
    private Bitmap bitmap;

    public void setOnNumberChangeListener(OnNumberChangeListener listener) {
        this.onNumberChangeListener = listener;
    }

    public void setOriginalNumber(float originalNumber) {
        this.originalNumber = originalNumber;
        int position = (int) (originalNumber * 10);

        NumberSelectorAdapter adapter = (NumberSelectorAdapter) degreeRV.getAdapter();

        Log.e("MENG","total:"+adapter.totalHolderCount);
        Log.e("MENG","scroll to:"+position);


    }

    public NumberSelector(Context context) {
        super(context);
    }

    public NumberSelector(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.number_selector_layout,this);
        bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.icon_index);
        TypedArray ta = context.obtainStyledAttributes(attrs,R.styleable.NumberSelector);
        maxWeight = ta.getInteger(R.styleable.NumberSelector_max_number,100);
        minWeight = ta.getInteger(R.styleable.NumberSelector_min_number,0);
        strokeWidth = ta.getInteger(R.styleable.NumberSelector_stroke_width,4);
        range = ta.getFloat(R.styleable.NumberSelector_range,0.1f);
        ta.recycle();
        initData();
        intView();
        intiPaint();
        setWillNotDraw(false);

        this.postDelayed(new Runnable() {
            @Override
            public void run() {
                int position = (int) (originalNumber * 10);
                int first = layoutManagerDegree.findFirstVisibleItemPosition();
                int last = layoutManagerDegree.findLastVisibleItemPosition();
                degreeRV.smoothScrollToPosition(position+(last-first)/2+1);

//                refreshSelectNumber();

            }
        },1000);

    }



    private void initData() {

        for (int i = 0 ;i<maxWeight*10;i++) {
            degreeList.add(i);

            if (i%10 == 0) {
                numberList.add(i/10);
            }
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

//        setMeasuredDimension((int)width,(int)height);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);


        if (widthMode == MeasureSpec.EXACTLY ) {
            width = widthSize;
        } else {
            width = getMeasuredWidth();
        }

        if (heightMode == MeasureSpec.EXACTLY ) {
            height = heightSize;
        } else {
            height = getMeasuredHeight();
        }


    }



    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        paint.setStyle(Paint.Style.STROKE);
        canvas.drawArc(strokeWidth/2,strokeWidth/2,height-strokeWidth/2,height-strokeWidth/2,
                90,180,false,paint);
        canvas.drawArc(width-height-strokeWidth,strokeWidth/2,width-strokeWidth/2,height-strokeWidth/2,
                -90,180,false,paint);

        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(height/2,0,width-height/2,strokeWidth,paint);
        canvas.drawRect(height/2,height-strokeWidth,width-height/2,height,paint);




        canvas.drawBitmap(bitmap,
                width/2-bitmap.getWidth()/2,
                height - bitmap.getHeight()-2,paint);

        Log.e("MENG","on draw");
        super.onDraw(canvas);
    }


    private void intView() {
        degreeRV = (RecyclerView) this.findViewById(R.id.degree_rv);
        numberRV = (RecyclerView) this.findViewById(R.id.number_rv);

        degree = context.getResources().getDrawable(R.drawable.degree);
        degree1 = context.getResources().getDrawable(R.drawable.degree1);
        degree2 = context.getResources().getDrawable(R.drawable.degree2);
        degree3 = context.getResources().getDrawable(R.drawable.degree3);

        layoutManagerDegree = new LinearLayoutManager(context);
        layoutManagerDegree.setOrientation(LinearLayoutManager.HORIZONTAL);
        degreeRV.setLayoutManager(layoutManagerDegree);

        layoutManagerNumber = new LinearLayoutManager(context);
        layoutManagerNumber.setOrientation(LinearLayoutManager.HORIZONTAL);
        numberRV.setLayoutManager(layoutManagerNumber);

        degreeRV.setAdapter(new NumberSelectorAdapter(degreeList,context,DEGREE_TYPE));
        numberRV.setAdapter(new NumberSelectorAdapter(numberList,context,NUMBER_TYPE));

        degreeRV.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    numberRV.scrollBy(dx, dy);
                    refreshSelectNumber();
                }
            }
        });

        numberRV.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getScrollState() != RecyclerView.SCROLL_STATE_IDLE) {
                    degreeRV.scrollBy(dx, dy);
                    refreshSelectNumber();

                }
            }
        });


    }

    private void refreshSelectNumber() {
        int first = layoutManagerDegree.findFirstVisibleItemPosition();
        int last = layoutManagerDegree.findLastVisibleItemPosition();
        int oldSelectedPosition = selectedPosition;
        selectedPosition = (last-first)/2+first;

        if (onNumberChangeListener!=null) {
            float ori = degreeList.get(selectedPosition);
            float zhengshu = ori/10;

            onNumberChangeListener.onNumberChanged(zhengshu);
        }

        if (selectedPosition!= oldSelectedPosition) {
//            try {
//                imageViews.get(selectedPosition).setImageDrawable(degree1);
//                imageViews.get(selectedPosition-1).setImageDrawable(degree2);
//                imageViews.get(selectedPosition+1).setImageDrawable(degree2);
//                imageViews.get(selectedPosition-2).setImageDrawable(degree3);
//                imageViews.get(selectedPosition+2).setImageDrawable(degree3);
//            } catch (Exception e) {
//                Log.e("MENG","null:"+e);
//            }
//
//            for (int i = 0; i<imageViews.size(); i++) {
//                if (selectedPosition-imageViews.keyAt(i)>2 || selectedPosition-imageViews.keyAt(i)<-2) {
//                    imageViews.valueAt(i).setImageDrawable(degree);
//                }
//            }

            if (selectedPosition % 10 == 0) {
                selectedPositionNum = selectedPosition/10;

                if (numberTexts.get(selectedPositionNum)!=null) {
                    numberTexts.get(selectedPositionNum).setTextColor(Color.rgb(242, 131, 76));
                }


            } else {
                if (numberTexts.get(selectedPositionNum)!=null) {
                    numberTexts.get(selectedPositionNum).setTextColor(Color.LTGRAY);
                }
            }

        }

    }

    public NumberSelector(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }



    private void intiPaint() {
        this.paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.LTGRAY);
        paint.setStrokeWidth(strokeWidth);

    }

    class NumberSelectorAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

        public int totalHolderCount = 0;
        public int totalTextCount = 0;
        private List<Integer> data;
        private Context mContext;
        private LayoutInflater inflater;

        private int type;

        public NumberSelectorAdapter(List<Integer> data, Context mContext, int type) {
            this.data = data;
            this.mContext = mContext;
            this.type = type;
            this.inflater = LayoutInflater.from(this.mContext);
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View itemView;

            if (type == DEGREE_TYPE) {
                totalHolderCount++;
                itemView = inflater.inflate(R.layout.item_degree,parent,false);
            } else {
                totalTextCount++;
                itemView = inflater.inflate(R.layout.item_number,parent,false);
            }

            RecyclerView.ViewHolder holder = new NumberSelectorViewHolder(itemView,type);

            return holder;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

            NumberSelectorViewHolder viewHolder = (NumberSelectorViewHolder) holder;
            if (type == DEGREE_TYPE) {
//                Log.e("MENG","add -------------------:"+position);
//
//                imageViews.put(position,viewHolder.image);
//                if (imageViews.size()>totalHolderCount) {
//                    imageViews.remove(position-totalHolderCount);
//                    imageViews.remove(position+totalHolderCount);
//                }
//
//                for (int i = 0; i<imageViews.size();i++) {
//                    Log.e("MENG","key----:"+imageViews.keyAt(i));
//                }


            }  else {
                numberTexts.put(position,viewHolder.text);
                if (numberTexts.size()>totalTextCount) {
                    numberTexts.remove(position-totalTextCount);
                    numberTexts.remove(position+totalTextCount);
                }

                if (data.get(position) < 10)
                    viewHolder.text.setText("" + data.get(position));
                else
                    viewHolder.text.setText(data.get(position) + "");

            }
        }

        @Override
        public int getItemCount() {
            return data == null ? 0 : data.size();
        }


        class NumberSelectorViewHolder extends RecyclerView.ViewHolder {

            TextView text;
            ImageView image;

            public NumberSelectorViewHolder(View itemView,int type) {
                super(itemView);
                if (type == DEGREE_TYPE) {
                    image = (ImageView) itemView.findViewById(R.id.item_degree_text);
                } else {
                    text = (TextView) itemView.findViewById(R.id.item_number_text);

                }
            }
        }
    }


    interface OnNumberChangeListener {
        void onNumberChanged(float number);
    }
}
