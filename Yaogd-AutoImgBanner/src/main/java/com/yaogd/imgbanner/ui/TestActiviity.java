package com.yaogd.imgbanner.ui;

import android.app.Activity;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.SparseArray;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.yaogd.imgbanner.R;
import com.yaogd.imgbanner.view.BannerPager;
import com.yaogd.imgbanner.view.CircleFlowIndicator;
import com.yaogd.lib.A;
/**
 * 
 * @author yaoguangdong
 * 2014-4-29
 */
public class TestActiviity extends Activity {

    private BannerPager banner;  
    private CircleFlowIndicator indicator;// 焦点图 dot
    
    private ImageView imgview;  
    private boolean mUseFullscreen ;
    private SparseArray<String> imgUrls ;
    
    public static final String[] IMAGES = new String[] {
            "http://c.hiphotos.baidu.com/image/w%3D310/sign=79f08e463b12b31bc76ccb28b6193674/09fa513d269759ee371072cab0fb43166d22df6a.jpg",
            "http://a.hiphotos.baidu.com/image/pic/item/09fa513d269759ee0d0254d7b0fb43166d22df5f.jpg",
            "http://e.hiphotos.baidu.com/image/pic/item/2934349b033b5bb56d18a45937d3d539b700bc4e.jpg",
            "http://g.hiphotos.baidu.com/image/pic/item/e1fe9925bc315c603a71d5998fb1cb13485477db.jpg",
            "http://a.hiphotos.baidu.com/image/w%3D230/sign=c32917f2d762853592e0d522a0ee76f2/32fa828ba61ea8d3a5661904950a304e251f586d.jpg"
            } ;
    
    @Override  
    public void onCreate(Bundle savedInstanceState){  
        super.onCreate(savedInstanceState); 
        
        getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        
        setContentView(R.layout.banner_pager);  
        
        banner = (BannerPager)findViewById(R.id.banner_pager_focus_pager);
        indicator = (CircleFlowIndicator) findViewById(R.id.banner_pager_indicator);
        indicator.setCount(4) ;
        
        imgUrls = new SparseArray<String>();
        imgUrls.append(0, IMAGES[0]) ;
        imgUrls.append(1, IMAGES[1]) ;
        imgUrls.append(2, IMAGES[2]) ;
        imgUrls.append(3, IMAGES[3]) ;
        //banner内部使用SmartImageView来加载的，感觉轻量一些
        banner.setImgUrls(imgUrls , R.drawable.miaomiao)
        .setCycle(true)
        .setInterval(3000)
        .startAutoScroll();
        
        banner.setOnPageChangeListener(new OnPageChangeListener() {
            
            @Override
            public void onPageSelected(int position) {
                indicator.setCurrentIndex(position % imgUrls.size()) ;
            }
            
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            	position = position % imgUrls.size();
            	A.d("position" + position + "positionOffset" + positionOffset + "positionOffsetPixels" + positionOffsetPixels) ;
                
            }
            
            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub
                
            }
        }) ;
        
        //API3.0以上的页面滑动
        banner.setPageTransformer(true, new ViewPager.PageTransformer(){

			@Override
			public void transformPage(View page, float position) {
				A.d("page.getWidth()" + page.getWidth() + ",position" + position) ;
			}
        	
        });
        
        
        
        
        //测试Universal image loader
        imgview = (ImageView)findViewById(R.id.banner_pager_Img);
        //测试全屏模式切换
        imgview.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                Window w = getWindow();
                if(mUseFullscreen) {
                    w.addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                    w.clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                } else {
                    w.addFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);
                    w.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
                }
                mUseFullscreen = !mUseFullscreen;
                
                //测试动态替换banner图片
                imgUrls.remove(0) ;
                imgUrls.append(0, IMAGES[4]) ;
            }
        }) ;
        
        //初始化，一般在Application中做一次
        ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(this));
        
        //使用ImageLoader加载
        DisplayImageOptions options = new DisplayImageOptions.Builder()
        .showImageForEmptyUri(R.drawable.miaomiao)
        .showImageOnFail(R.drawable.miaomiao)
        .resetViewBeforeLoading(true)
        .cacheOnDisk(true)
        .imageScaleType(ImageScaleType.EXACTLY)
        .bitmapConfig(Bitmap.Config.RGB_565)
        .considerExifParams(true)
        .displayer(new FadeInBitmapDisplayer(300))
        .build();
        
        ImageLoader.getInstance().displayImage(IMAGES[4], imgview, options, new ImageLoadingListener() {
            
            @Override
            public void onLoadingStarted(String imageUri, View view) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onLoadingFailed(String imageUri, View view, FailReason failReason) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
                // TODO Auto-generated method stub
                
            }
            
            @Override
            public void onLoadingCancelled(String imageUri, View view) {
                // TODO Auto-generated method stub
                
            }
        }) ;
        
    }
    
    @Override
    protected void onDestroy() {
        System.exit(0);
        super.onDestroy();
    }

}
