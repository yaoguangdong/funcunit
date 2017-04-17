package com.yaogd.web.common;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.view.accessibility.AccessibilityManager;
import android.webkit.CookieManager;
import android.webkit.SslErrorHandler;
import android.webkit.ValueCallback;
import android.webkit.WebBackForwardList;
import android.webkit.WebChromeClient;
import android.webkit.WebHistoryItem;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.yaogd.lib.CommonUtils;
import com.yaogd.lib.DiskHelper;
import com.yaogd.lib.L;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.net.URLDecoder;
import java.net.URLEncoder;

import jumpgo.jtechme.com.yaogd_lib_web.R;

/**
 * Description:广告落地页浏览器
 * Created by yaoguangdong on 2017/1/22.
 */
public class YBrowserActivity extends Activity {

    /**
     * 自定义“网页找不到”的样子为白页
     */
    private static final String CUSTOM_ERROR_PAGE = "about:blank";

    private TextView mTVTitle;
    private WebView mWebView;
    private ProgressBar mProgressBar;
    private TextView mErrorLayout;

    //上传文件相关>>>>>>
    private String mCameraPhotoPath;
    private ValueCallback<Uri> mUploadMessage;
    private ValueCallback<Uri[]> mFilePathCallback;
    //<<<<<<上传文件相关

    //网页地址
    public String mUrl = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //浏览器View由子类调用生成，父类不做处理。
        //setContentView(initView(getLayoutInflater()));
    }

    /**
     * 设置是否支持自动横竖屏切换
     * @param autoOrientation
     */
    protected void setAutoOrientation(boolean autoOrientation){
        if (autoOrientation) {
            if (getRequestedOrientation() != ActivityInfo.SCREEN_ORIENTATION_USER) {
                setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_USER);
            }
        } else {
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
    }

    /**
     * 加载网页
     *
     * @param url
     */
    protected void load(String url) {
        mUrl = url;
        if (TextUtils.isEmpty(url) || !CommonUtils.isNetAvailable(this)) {
            showError();
            return;
        }
        //解决url开头或结尾带有非法空格问题。
        url = url.trim();
        //解决误把二层url参数解码问题。两端强制统一为需要decode方式，此种判断暂时保留
        if (url.startsWith("http%3A%2F%2F") || url.startsWith("https%3A%2F%2F")) {
            url = URLDecoder.decode(url);
        }
        showWebView();
        mWebView.loadUrl(url);
    }

    /**
     * 初始化View
     *
     * @param inflater
     * @return
     */
    protected View initView(LayoutInflater inflater) {
        LinearLayout rootView = (LinearLayout) inflater.inflate(R.layout.browser_layout, null);

        mTVTitle = (TextView) rootView.findViewById(R.id.browser_web_center_title);
        mProgressBar = (ProgressBar) rootView.findViewById(R.id.browser_progress);
        mProgressBar.setMax(100);
        //设置进度条颜色为蓝色
        Drawable drawable = new ColorDrawable(Color.parseColor("#4680d1"));
        mProgressBar.setProgressDrawable(new ClipDrawable(drawable, Gravity.LEFT, ClipDrawable.HORIZONTAL));
        mErrorLayout = (TextView) rootView.findViewById(R.id.browser_error);
        mErrorLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //重新加载网页（不使用reload）
                load(mUrl);
            }
        });

        //点击叉号，退出浏览器
        rootView.findViewById(R.id.browser_web_nav_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        mWebView = (WebView) rootView.findViewById(R.id.browser_webview);
        //设置浏览器属性
        setUpWebViewDefaults(mWebView);

        return rootView;
    }

    private void setUpWebViewDefaults(WebView webView) {
        WebSettings settings = webView.getSettings();

        // Enable Javascript需要开启，否则有些页面的视频不能播放
        settings.setJavaScriptEnabled(true);

        // 自适应屏幕,可任意比例缩放
        settings.setUseWideViewPort(true);
        settings.setLoadWithOverviewMode(true);

        // 控制页面可以缩放，但不显示控制条
        settings.setBuiltInZoomControls(true);
        settings.setSupportZoom(true);
        settings.setDisplayZoomControls(false);

        // Allow use of Local Storage
        settings.setDomStorageEnabled(true);

        // Enable remote debugging via chrome://inspect
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(L.isDebug());
        }

        //允许混合协议加载
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
        }

        //允许自动播放
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
            settings.setMediaPlaybackRequiresUserGesture(false);
        }

        //设置页面解码为utf-8
        settings.setDefaultTextEncodingName("UTF-8");

        //允许文件访问
        settings.setAllowFileAccess(true);
        settings.setAllowContentAccess(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            settings.setAllowFileAccessFromFileURLs(true);
            settings.setAllowUniversalAccessFromFileURLs(true);
        }

        //允许缓存
        settings.setAppCacheEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setAppCachePath(getCacheDir().toString());
        settings.setGeolocationEnabled(true);
        settings.setGeolocationDatabasePath(getFilesDir().toString());
        settings.setDatabaseEnabled(true);
        settings.setDomStorageEnabled(true);

        //设置User-Agent
        settings.setUserAgentString(appendUserAgent(settings.getUserAgentString()));

        //设置加载过程监听
        webView.setWebChromeClient(new AdvertWebViewChromeClient());
        webView.setWebViewClient(new AdvertWebViewClient());

        //特殊设置>>>>>>>>>>
        disableAccessibility();

        //设置保存view状态，优化显示
        webView.setSaveEnabled(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.setBackground(null);
            webView.getRootView().setBackground(null);
        }
        webView.setBackgroundColor(Color.parseColor("#f0eff5"));//color08

        //隐藏scroll bar
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setScrollbarFadingEnabled(true);

        //打开cookie 默认开启
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);
    }

    /**
     * 主要是4.x版本，关闭辅助模式
     * 防止错误系统日志打印：
     * android.app.ServiceConnectionLeaked: Activity com.autohome.mainlib.business.ui.commonbrowser.activity.CommBrowserActivity has leaked ServiceConnection
     * com.android.org.chromium.com.googlecode.eyesfree.braille.selfbraille.SelfBrailleClient$Connection@4561fd90 that was originally bound here
     * ......
     */
    private void disableAccessibility() {
        try {
            AccessibilityManager accessibilityManager = (AccessibilityManager) getSystemService(Context.ACCESSIBILITY_SERVICE);
            if (accessibilityManager == null || !accessibilityManager.isEnabled()) {
                return;
            }
            Method MethodSetState = AccessibilityManager.class.getDeclaredMethod("setState", int.class);
            MethodSetState.setAccessible(true);
            MethodSetState.invoke(accessibilityManager, 0);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置User-Agent
     * @return
     */
    public String appendUserAgent(String rawUserAgent) {
        StringBuilder userAgent = new StringBuilder();
        userAgent.append(rawUserAgent);
        userAgent.append(" ");
        userAgent.append("android");
        userAgent.append("/");
        try {
            userAgent.append(URLEncoder.encode(Build.VERSION.RELEASE, "utf-8"));
            userAgent.append(";");
            userAgent.append(URLEncoder.encode(Build.MODEL, "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        userAgent.append(" ");
        userAgent.append("yao guang dong");
        userAgent.append("/");
        userAgent.append("1.0");

        return userAgent.toString();
    }

    /**
     * 显示网络错误
     */
    protected void showError() {
        //去除系统自带的错误页面
        mWebView.loadUrl(CUSTOM_ERROR_PAGE);
        mErrorLayout.setVisibility(View.VISIBLE);
    }

    /**
     * 显示WebView
     */
    protected void showWebView() {
        mErrorLayout.setVisibility(View.GONE);
    }

    @Override
    public void onPause() {
        super.onPause();
        //如果此页面不用做浏览器，那么webview就是null
        if (mWebView != null) {
            mWebView.loadUrl("javascript:pauseAudiosVideos();");
            mWebView.onPause();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //如果此页面不用做浏览器，那么webview就是null
        if (mWebView != null) {
            mWebView.loadUrl("javascript:resumeAudiosVideos();");
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        //如果此页面不用做浏览器，那么webview就是null
        if (mWebView != null) {
            // destroy() is causing the segfault
            try {
                mWebView.stopLoading();
                mWebView.clearCache(false);
                mWebView.clearHistory();
                mWebView.destroyDrawingCache();
                mWebView.removeAllViews();
                ViewParent parent = mWebView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeAllViews();
                }
                //WebView destroy前必须从view tree解除关联
                mWebView.destroy();
            } catch (Exception ignore) {
                L.e("ad_pv", "onDestroy,error:" + ignore.getMessage());
            }
            mWebView = null;
        }
        super.onDestroy();
    }

    private class AdvertWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
            L.e("ad_pv", "onPageStarted=" + url);
            mProgressBar.setVisibility(View.VISIBLE);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            //onDestroy后置空
            if (mWebView == null) {
                return;
            }
            L.e("ad_pv", "onPageFinished=" + url);
            boolean isNetAvailable = CommonUtils.isNetAvailable(view.getContext());
            //屏蔽掉自定义加载失败页面
            if (!isNetAvailable && !CUSTOM_ERROR_PAGE.equals(url)) {
                //无网时，WebView的加载只会执行onPageFinished这一个方法，因此这里需要判断无网时的加载失败。
                showError();
                return;
            }
            mWebView.loadUrl("javascript:function pauseAudiosVideos(){var audios=document.getElementsByTagName(\"audio\");var i;for(i=0;i<audios.length;i++){var audio=audios[i];if(!audio.paused){audio.pause();audio.setAttribute('ahpaused','true')}}var videos=document.getElementsByTagName(\"video\");var j;for(j=0;j<videos.length;j++){var video=videos[j];if(!video.paused){video.pause();video.setAttribute('ahpaused','true')}}}function resumeAudiosVideos(){var audios=document.getElementsByTagName(\"audio\");var i;for(i=0;i<audios.length;i++){var audio=audios[i];if(audio.paused&&'true'==audio.getAttribute('ahpaused')){audio.play();audio.setAttribute('ahpaused','false')}}var videos=document.getElementsByTagName(\"video\");var j;for(j=0;j<videos.length;j++){var video=videos[j];if(video.paused&&'true'==video.getAttribute('ahpaused')){video.play();video.setAttribute('ahpaused','false')}}}");
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            //onDestroy后置空
            if (mWebView == null) {
                return;
            }
            L.e("ad_pv", "onReceivedError=" + description);
            view.stopLoading();
            showError();
        }

        @Override
        public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
            //忽略https错误继续加载
            //super.onReceivedSslError(view, handler, error);
            L.e("ad_pv", "onReceivedSslError=" + error);
            handler.proceed();
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            L.d("ad_pv", "shouldOverrideUrlLoading just GET=" + url);
            //app下载类协议，拦截按钮的点击（有的页面点击按钮会触发整个页面的刷新，导致页面地址也会走这里。另外这是WebView内部302以后的地址）
            if (url != null && url.startsWith("http") && url.endsWith(".apk")) {
                //下载apk文件
                onH5Download(url);
                return true;
            }
            //其他协议没有明确说明，不做拦截处理
            return super.shouldOverrideUrlLoading(view, url);
        }
    }

    /**
     * 此方法不实现，交给子类做。
     *
     * @param url
     * @return
     */
    protected boolean onH5Download(String url) {
        return false;
    }

    private final class AdvertWebViewChromeClient extends WebChromeClient {

        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            super.onProgressChanged(view, newProgress);
            L.d("ad_pv", "progress:" + newProgress);
            mProgressBar.setProgress(newProgress);
            // 页面加载完成
            if (newProgress >= 100) {
                mProgressBar.setVisibility(View.GONE);
            }
        }

        @Override
        public void onReceivedTitle(WebView view, String title) {
            super.onReceivedTitle(view, title);
            L.d("ad_pv", "title=" + title);

            String theRealTitle = null;
            if (title != null) {
                //!title.contains("无法打开") && !title.contains("找不到")，这几个汉字显示在标题上更清楚一些，不再过滤
                if (!title.contains(CUSTOM_ERROR_PAGE) && !title.contains("not available") && !title.startsWith("http")) {
                    theRealTitle = title;
                }
            }
            if (theRealTitle != null) {
                mTVTitle.setText(theRealTitle);
            }
        }

        @Override
        public void onGeolocationPermissionsShowPrompt(String origin, android.webkit.GeolocationPermissions.Callback callback) {
            // 默认允许地理位置授权
            callback.invoke(origin, true, false);
            super.onGeolocationPermissionsShowPrompt(origin, callback);
        }

        /**
         * 文件选择，三星S5,4.4.2,的文件选择不会调用这几个函数，因此无效
         *
         * @param uploadMsg
         */
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg) {
            openFileChooserImpl(uploadMsg);
        }

        /**
         * 文件选择，三星S5,4.4.2,的文件选择不会调用这几个函数，因此无效
         *
         * @param uploadMsg
         * @param acceptType
         */
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
            openFileChooserImpl(uploadMsg);
        }

        /**
         * 文件选择，三星S5,4.4.2,的文件选择不会调用这几个函数，因此无效
         *
         * @param uploadMsg
         * @param acceptType
         * @param capture
         */
        @SuppressWarnings("unused")
        public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType, String capture) {
            openFileChooserImpl(uploadMsg);
        }

        /**
         * 文件选择，华为荣耀7i，6.0系统，调用这个方法，正确上传，没有问题。
         *
         * @param webView
         * @param filePathCallback
         * @param fileChooserParams
         * @return
         */
        public boolean onShowFileChooser(WebView webView, ValueCallback<Uri[]> filePathCallback, FileChooserParams fileChooserParams) {
            openFileChooserUpAndroid5Impl(filePathCallback);
            return true;
        }
    }

    /**
     * 实现选择文件
     *
     * @param uploadMsg
     */
    public void openFileChooserImpl(ValueCallback<Uri> uploadMsg) {
        mUploadMessage = uploadMsg;
        Intent i = new Intent(Intent.ACTION_GET_CONTENT);
        i.addCategory(Intent.CATEGORY_OPENABLE);
        i.setType("*/*");
        startActivityForResult(Intent.createChooser(i, "选择文件"), 0x100);
    }

    /**
     * android5以上 实现选择文件
     *
     * @param filePathCallback
     */
    public void openFileChooserUpAndroid5Impl(ValueCallback<Uri[]> filePathCallback) {
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePathCallback;

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        //检查系统是否存在相册
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // 提供选择后的文件路径
            File photoFile = null;
            try {
                photoFile = new DiskHelper().createFile(System.currentTimeMillis() + "_.jpg");
            } catch (Exception ignore) {
            }

            // 如果成功创建文件
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }

        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");

        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }

        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "选择图片");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);

        this.startActivityForResult(chooserIntent, 0x100);
    }

    /**
     * 接受其他界面回调（文件选择）
     *
     * @param requestCode
     * @param resultCode
     * @param intent
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        //Android5以下，是用文件选择接口
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode == 0x100) {
                if (null == mUploadMessage) {
                    return;
                }
                Uri result = intent == null || resultCode != RESULT_OK ? null : intent.getData();
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }

        if (requestCode != 0x100 || mFilePathCallback == null) {
            super.onActivityResult(requestCode, resultCode, intent);
            return;
        }

        Uri[] results = null;

        // 如果选择成功，创建文件信息返回给网页
        if (resultCode == Activity.RESULT_OK) {
            if (intent == null) {
                // 这里如果为空，应该增加拍照功能，先忽略拍照。
                if (mCameraPhotoPath != null) {
                    results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                }
            } else {
                String dataString = intent.getDataString();
                if (dataString != null) {
                    results = new Uri[]{Uri.parse(dataString)};
                }
            }
        }

        mFilePathCallback.onReceiveValue(results);
        mFilePathCallback = null;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //如果不能再回退，则退出界面
            if (canGoBack()) {
                mWebView.goBack();
            } else {
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    /**
     * 判断是否可以回退，如果可以回退，则回退到上个页面
     * about:blank除外
     * @return
     */
    private boolean canGoBack(){
        boolean isGoBackSuccess = false;
        //页面内部历史回退，排除about:blank页面
        if (null != mWebView && mWebView.canGoBack()) {
            WebBackForwardList backStack = mWebView.copyBackForwardList();
            if (backStack != null && backStack.getSize() >= 2) {
                int currentIndex = backStack.getCurrentIndex();
                WebHistoryItem currentItem = backStack.getItemAtIndex(currentIndex);
                WebHistoryItem secondItem = backStack.getItemAtIndex(currentIndex -1);
                String currentUrl = currentItem == null ? null : currentItem.getUrl();
                String secondUrl = secondItem == null ? null : secondItem.getUrl();
                boolean currentBoBackOK = !TextUtils.isEmpty(currentUrl) && !CUSTOM_ERROR_PAGE.equals(currentUrl);
                boolean secondBoBackOK = !TextUtils.isEmpty(secondUrl) && !CUSTOM_ERROR_PAGE.equals(secondUrl);
                //最近两次历史记录都不是错误页面时，才允许go back
                isGoBackSuccess = currentBoBackOK && secondBoBackOK;
            }
        }
        return isGoBackSuccess;
    }
}
