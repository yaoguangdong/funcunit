package com.yaogd.imgbanner.view;

import java.lang.ref.WeakReference;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.MotionEventCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView.ScaleType;

import com.yaogd.imgutil.SmartImageView;

public class BannerPager extends ViewPager {

    private static final long TIME_INTERVAL = 2000; //自动轮播的时间间隔  2秒
    /** auto scroll time in milliseconds, default is {@link #DEFAULT_INTERVAL} **/
    private long interval = TIME_INTERVAL;
    
    private int fallbackResourceId ;//默认的图片的资源ID
    
    private BannerPagerAdapter adapter;
    private SparseArray<String> imgUrls ;
    //默认情况下，ViewPager在第一帧时，是无法向左滑动的。源码：if(currentItem < 0) currentItem = 0;采取一个合适中间值，作为左右滑动的基础
    private static final int INIT_CURRENT_PAGE = Integer.MAX_VALUE / 2;
    
    private boolean isCycle = true;//是否循环滚动
    private Handler handler ;
    private boolean isAutoScroll ;//是否自动滚动
    private boolean isStopByTouch ;
    private static final int PAGER_NEXT = 0x800 ;
    
    private static class XXHandler extends Handler {

        private final WeakReference<BannerPager> autoPager;

        public XXHandler(BannerPager autoPager) {
            this.autoPager = new WeakReference<BannerPager>(autoPager);
        }

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case PAGER_NEXT:
                    BannerPager pager = this.autoPager.get();
                    if (pager != null) {
                        pager.scrollOnce();
                        pager.sendScrollMessage(pager.interval);
                    }
                default:
                    break;
            }
        }
    }
    
    private void sendScrollMessage(long delayTimeInMills) {
        /** remove messages before, keeps one message is running at most **/
        handler.removeMessages(PAGER_NEXT);
        handler.sendEmptyMessageDelayed(PAGER_NEXT, delayTimeInMills);
    }
    
    /**
     * start auto scroll, first scroll delay time is {@link #getInterval()}
     */
    public void startAutoScroll() {
        isAutoScroll = true;
        sendScrollMessage(TIME_INTERVAL);
    }

    /**
     * start auto scroll
     * @param delayTimeInMills first scroll delay time
     */
    public void startAutoScroll(int delayTimeInMills) {
        isAutoScroll = true;
        sendScrollMessage(delayTimeInMills);
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        isAutoScroll = false;
        handler.removeMessages(PAGER_NEXT);
    }
    
    public BannerPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BannerPager(Context context) {
        super(context);
        init();
    }

    private void init() {
        handler = new XXHandler(this) ;
        this.setOnPageChangeListener(null) ;
    }

    /**
     * scroll only once
     */
    public void scrollOnce() {
        if (null == imgUrls || imgUrls.size() <= 1) {
            return;
        }
        int nextItem = getCurrentItem() + 1;
        setCurrentItem(nextItem, true);
    }
    
    /**
     * @param imgUrls 图片资源地址
     * @param fallbackResourceId 缺省图片
     * @return
     */
    public BannerPager setImgUrls(SparseArray<String> imgUrls, int fallbackResourceId) {
        if (null == imgUrls || imgUrls.size() == 0) {
            return null;
        }
        this.imgUrls = imgUrls ;
        if (adapter == null) {
            adapter = new BannerPagerAdapter();
            setAdapter(adapter);
        }
        adapter.notifyDataSetChanged() ;
        this.setCurrentItem(INIT_CURRENT_PAGE);
        return this ;
    }

    /**
     * <ul>
     * if stopScrollWhenTouch is true
     * <li>if event is down, stop auto scroll.</li>
     * <li>if event is up, start auto scroll again.</li>
     * </ul>
     */
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        int action = MotionEventCompat.getActionMasked(ev);

        if ((action == MotionEvent.ACTION_DOWN) && isAutoScroll) {
            isStopByTouch = true;
            stopAutoScroll();
        } else if (ev.getAction() == MotionEvent.ACTION_UP && isStopByTouch) {
            startAutoScroll();
            isStopByTouch = false;
        }

        return super.dispatchTouchEvent(ev);
    }

    private class BannerPagerAdapter extends PagerAdapter {

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ((ViewPager) container).removeView((View) object);
        }

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {
            return arg0 == arg1;
        }

        @Override
		public int getCount() {
			return Integer.MAX_VALUE;
		}

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {
          
            SmartImageView imageView = new SmartImageView(getContext());
            imageView.setScaleType(ScaleType.FIT_XY);
            
            final int posit = position % imgUrls.size();
            
            if(fallbackResourceId != 0){
                imageView.setImageUrl(imgUrls.get(posit), fallbackResourceId);
            }else{
                imageView.setImageUrl(imgUrls.get(posit));
            }
            
            container.addView(imageView);
            return imageView;
        }
    }
    
    public long getInterval() {
        return interval;
    }

    public BannerPager setInterval(long interval) {
        this.interval = interval;
        return this ;
    }

    public boolean isCycle() {
        return isCycle;
    }

    public BannerPager setCycle(boolean isCycle) {
        this.isCycle = isCycle;
        return this ;
    }

    public boolean isAutoScroll() {
        return isAutoScroll;
    }

    public BannerPager setAutoScroll(boolean isAutoScroll) {
        this.isAutoScroll = isAutoScroll;
        return this ;
    }
    
}
