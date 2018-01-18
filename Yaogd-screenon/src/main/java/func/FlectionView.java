package func;//package com.yaogd.com.yaogd.myapptest.func;
//
//import android.content.Context;
//import android.graphics.Bitmap;
//import android.graphics.Bitmap.Config;
//import android.graphics.BitmapFactory;
//import android.graphics.Canvas;
//import android.graphics.Color;
//import android.graphics.LinearGradient;
//import android.graphics.Matrix;
//import android.graphics.Paint;
//import android.graphics.PorterDuffXfermode;
//import android.graphics.Shader.TileMode;
//import android.util.AttributeSet;
//import android.view.View;
///**
// * 自定义控件--图片增加倒影效果
// * @author yaoguangdong
// * 2014-7-23
// */
//public class FlectionView extends View {
//
//    Context mContext=null;
//    public FlectionView(Context context) {
//        super(context);
//    }
//
//    public FlectionView(Context context, AttributeSet attrs) {
//        super(context, attrs);
//        this.mContext=context;
//    }
//
//    @Override
//    protected void onDraw(Canvas canvas) {
//        //设置背景色
//        this.setBackgroundColor(Color.parseColor("#8B8378"));
//        Bitmap oldBitmap = BitmapFactory.decodeResource(mContext.getResources(),R.drawable.gesture1);
//        Bitmap newBitmap = createFlectionBitmap(oldBitmap);
//        canvas.drawBitmap(newBitmap,newBitmap.getWidth() ,newBitmap.getHeight(), new Paint());
//        this.invalidate();
//    }
//
//    //获取原图+倒影图的bitmap
//    private Bitmap createFlectionBitmap(Bitmap oldBitmap) {
//        int mWidth = oldBitmap.getWidth();
//        int mHeight = oldBitmap.getHeight();
//        //原图和倒影图之间的缝隙
//        int gap = 2;
//        Matrix matrix = new Matrix();
//        matrix.preScale(1, -1);
//        Bitmap flection = Bitmap.createBitmap(oldBitmap, 0, mHeight / 2,
//                mWidth, mHeight / 2, matrix, false);
//        Bitmap background = Bitmap.createBitmap(mWidth, mHeight+gap+mHeight/2, Config.ARGB_8888);
//        Canvas canvas = new Canvas(background);
//        Paint p1 = new Paint();
//        //画出原图
//        canvas.drawBitmap(oldBitmap, 0, 0, p1);
//        //画出倒影图
//        canvas.drawBitmap(flection, 0, mHeight+gap, p1);
//        Paint shaderPaint = new Paint();
//        LinearGradient shader = new LinearGradient(0, mHeight, 0,
//                flection.getHeight(), 0x70ffffff, 0x00ffffff, TileMode.MIRROR);
//        shaderPaint.setShader(shader);
//        shaderPaint.setXfermode(new PorterDuffXfermode(android.graphics.PorterDuff.Mode.DST_IN));
//        //画出渐变颜色
//        canvas.drawRect(0, mHeight+gap, mWidth, background.getHeight(), shaderPaint);
//        return background;
//    }
//
//}