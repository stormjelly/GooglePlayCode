package cn.itcast.googleplay09.manager;

import cn.itcast.googleplay09.ui.utils.UiUtils;

import com.lidroid.xutils.BitmapUtils;

/**
 * 单例的设计模式-->对于一些管理类一般会使用单例设计模式
 * 
 * 1、单例设计模式
 * 2、观察者设计模式(回调)-->自定义控件
 * 3、适配器     View---  adapter --  ArrayList
 * 4、MVC   MVP      C-->Activity-->太重
 * 
 * @author zhengping
 *
 */
public class MyBitmapManager {
	
	private MyBitmapManager(){}
	
	private static MyBitmapManager instance;
	
	public synchronized static MyBitmapManager getInstance() {
		if(instance == null) {
			instance = new MyBitmapManager();
		}
		
		return instance;
	}
	
	
	private BitmapUtils bitmapUtils;
	
	public BitmapUtils getBitmapUtils() {
		if(bitmapUtils == null) {
			bitmapUtils = new BitmapUtils(UiUtils.getContext());
		}
		
		return bitmapUtils;
	}


}
