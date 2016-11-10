package cn.itcast.googleplay09.ui.utils;

import cn.itcast.googleplay09.application.MyApplication;
import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Handler;
import android.view.View;

/**
 * 处理和UI操作相关的工具类
 * @author zhengping
 *
 */
public class UiUtils {
	
	//获取全局Context对象
	public static Context getContext() {
		return MyApplication.instance.context;
	}
	
	//获取主线程的Handler对象
	public static Handler getMainThreadHandler() {
		return MyApplication.instance.handler;
	}
	
	//获取主线程的线程id
	public static int getMainThreadId() {
		return MyApplication.instance.mainThreadId;
	}
	
	//获取字符串资源
	public static String getString(int resId) {
		return getContext().getResources().getString(resId);
	}
	
	//获取字符串数组
	public static String[] getStringArray(int resId) {
		return getContext().getResources().getStringArray(resId);
	}
	
	//获取drawable
	public static Drawable getDrawable(int resId) {
		return getContext().getResources().getDrawable(resId);
	}
	//获取颜色资源
	public static int getColor(int resId) {
		return getContext().getResources().getColor(resId);
	}
	//获取颜色的状态选择器
	public static ColorStateList getColorStateList(int resId) {
		return getContext().getResources().getColorStateList(resId);
	}
	
	//获取dimen里面的值
	public static int getDimen(int resId) {
		return getContext().getResources().getDimensionPixelSize(resId);
	}
	
	//dp-->px
	public static int dip2px(int dip) {
		//屏幕密度
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (dip*density + 0.5f);
	}
	
	//px-->dp
	public static int px2dip(int px) {
		//屏幕密度
		float density = getContext().getResources().getDisplayMetrics().density;
		return (int) (px/density + 0.5f);
	}
	
	//加载一个布局文件
	public static View inflateView(int resId) {
		return View.inflate(getContext(), resId, null);
	}
	
	//判断是否在主线程
	public static boolean isRunOnUiThread() {
		//1、获取主线程的id
		int mainThreadId = getMainThreadId();
		//2、获取当前线程的id
		int currentThreadId = android.os.Process.myTid();
		return mainThreadId == currentThreadId;
	}
	
	//保证一个任务一定是在主线程中运行
	//Thread:线程
	//Runnable:任务
	public static void runOnUiThread(Runnable r) {
		if(isRunOnUiThread()) {
			//new Thread(r).start();
			r.run();//在当前的线程中进行方法的调用
		} else {
			getMainThreadHandler().post(r);//将r丢到主线程的消息队列
		}
	}
	
	
	public static GradientDrawable getGradientDrawable(int radius,int color) {
		GradientDrawable drawable = new GradientDrawable();//创建了一个shape标签
		drawable.setShape(GradientDrawable.RECTANGLE);
		drawable.setCornerRadius(radius);
		drawable.setColor(color);
		return drawable;
		
	}
	
	public static StateListDrawable getSelector(Drawable pressedDrawable,Drawable normalDrawable) {
		StateListDrawable selector = new StateListDrawable();//创建一个selector的标签
		selector.addState(new int[]{android.R.attr.state_pressed}, pressedDrawable);
		selector.addState(new int[]{}, normalDrawable);
		return selector;
		
		
	}

	public static int getScreenWidth() {
		return getContext().getResources().getDisplayMetrics().widthPixels;
	}

}
