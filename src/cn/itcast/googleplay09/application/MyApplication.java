package cn.itcast.googleplay09.application;

import cn.itcast.googleplay09.bean.AppInfo;
import android.app.Application;
import android.content.Context;
import android.os.Handler;
/**
 * 1、单实例
 * 2、生命周期长：比任何的Activity、service都要长
 * 3、onCreate可以认为是一个应用程序的入口
 * 4、onCreate方法是在主线程中运行
 * 
 * 注意：如果使用自定义的Application的话，需要在清单文件中进行注册
 * 
 * @author zhengping
 *
 */
public class MyApplication extends Application {
	
	public  Context context;
	public  Handler handler;
	public  int mainThreadId;
	
	public  AppInfo appInfo;
	
	public static MyApplication instance;

	@Override
	public void onCreate() {
		super.onCreate();
		
		//1、获取全局Context对象，供其他人调用   new出一个View、Toast等等操作都需要使用Context
		context = getApplicationContext();
		
		//2、初始化主线程的Handler对象
		//handler.sendEmptyMessage(0)  把消息通知给主线程
		//handler到底维度的是主线程的消息队列呢还是子线程的消息队列?
		//得看handler在哪一个线程中new出来
		handler = new Handler();
		
		//3、获取主线程的线程id
		mainThreadId = android.os.Process.myTid();
		
		//Application对象的创建是有系统来完成，不能自己new出Application的实例对象
		instance = this;
		
	}

}
