package marview.qwy.com.marview;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.ViewFlipper;

import java.util.ArrayList;
import java.util.List;

/**
 * 作者：仇伟阳
 */
public class MarqueeView extends ViewFlipper {

    private Context mContext;
    private List<String> notices;


    private int interval = 2000;
    private int animDuration = 500;//时间
    private int textSize = 14;
    private int textColor = 0xffffffff;

    private boolean singleLine = false;
    private int gravity = Gravity.LEFT | Gravity.CENTER_VERTICAL;
    private boolean isSetAnimDuration = false;

    private static final int TEXT_GRAVITY_LEFT = 0, TEXT_GRAVITY_CENTER = 1, TEXT_GRAVITY_RIGHT = 2;
    private OnItemClickListener onItemClickListener;

    public MarqueeView(Context context) {
        this(context, null);
    }

    public MarqueeView(Context context, AttributeSet attrs) {
        super(context, attrs);

        init(context, attrs, 0);
    }

    private void init(Context context, AttributeSet attrs, int defStyleAttr) {
        this.mContext = context;
        //初始化存放数据的集合
        if (notices == null) notices = new ArrayList<String>();

        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.MarqueeViewStyle, defStyleAttr, 0);

        interval = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvInterval, interval);
        isSetAnimDuration = typedArray.hasValue(R.styleable.MarqueeViewStyle_mvAnimDuration);
        singleLine = typedArray.getBoolean(R.styleable.MarqueeViewStyle_mvSingleLine, false);
        animDuration = typedArray.getInteger(R.styleable.MarqueeViewStyle_mvAnimDuration, animDuration);

        if(typedArray.hasValue(R.styleable.MarqueeViewStyle_mvTextSize)){
            textSize = (int) typedArray.getDimension(R.styleable.MarqueeViewStyle_mvTextSize, textSize);
            textSize = DisplayUtil.px2sp(mContext, textSize);
        }

        textColor = typedArray.getColor(R.styleable.MarqueeViewStyle_mvTextColor, textColor);
        int graityType = typedArray.getInt(R.styleable.MarqueeViewStyle_mvGravity, TEXT_GRAVITY_LEFT);
        switch (graityType){
            case TEXT_GRAVITY_CENTER:
                gravity = Gravity.CENTER;
            case TEXT_GRAVITY_RIGHT:
                gravity = Gravity.RIGHT;
        }
        typedArray.recycle();

        setFlipInterval(interval);

        Animation animIN = AnimationUtils.loadAnimation(mContext, R.anim.marquee_anim_in);
        if(isSetAnimDuration) animIN.setDuration(animDuration);
        setInAnimation(animIN);

        Animation animOut = AnimationUtils.loadAnimation(mContext, R.anim.marquee_anim_out);
        if (isSetAnimDuration) animOut.setDuration(animDuration);
        setOutAnimation(animOut);
    }

        public void startWithText(final String notice){
            if(TextUtils.isEmpty(notice)) return;

            getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener(){

                @Override
                public void onGlobalLayout() {
                    getViewTreeObserver().removeGlobalOnLayoutListener(this);
                    startWithFixedWidth(notice,getWidth());
                }
            });
        }
    // 根据公告字符串列表启动轮播
    public void startWithList(List<String> notices) {
        setNotices(notices);
        start();
    }



    private void startWithFixedWidth(String notice, int width) {
        int noticeLength = notice.length();

        int dpW = DisplayUtil.px2dip(mContext, width);
        int limit = dpW / textSize;
        if(dpW == 0){
            throw new RuntimeException("Please set MarqueeView width !");
        }
        if(noticeLength <= limit){
            notices.add(notice);
        }else{
            int size = noticeLength / limit + (noticeLength % limit != 0 ? 1 : 0);
            for(int i =0; i< size; i++){
                int startIndex = i * limit;
                int endIndex = (i + 1) * limit >= noticeLength ? noticeLength : (i + 1) * limit;

                notices.add(notice.substring(startIndex,endIndex));
            }
        }
        start();
    }

    private boolean start() {
        if(notices == null || notices.size() == 0) return false;

        removeAllViews();

        for(int i =0;i<notices.size();i++){
            final TextView textView = createTextView(notices.get(i),i);
            final int finalI =i;
            textView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(onItemClickListener != null){
                        onItemClickListener.onItemClick(finalI,textView);
                    }
                }
            });

            addView(textView);
        }

        if(notices.size()>1){
            startFlipping();
        }
        return true;
    }

    private TextView createTextView(String text, int position) {
        TextView tv = new TextView(mContext);
        tv.setGravity(gravity);
        tv.setText(text);
        tv.setTextColor(textColor);
        tv.setTextSize(textSize);
        tv.setSingleLine(singleLine);
        tv.setTag(position);
        return tv;
    }
    public int getPosition() {

        return (int) getCurrentView().getTag();
    }
    private void setNotices(List<String> notices) {
        this.notices = notices;
    }

    public List<String> getNotices() {
        return notices;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(int position, TextView textView);
    }
}
