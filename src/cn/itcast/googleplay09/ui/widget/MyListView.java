package cn.itcast.googleplay09.ui.widget;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.AttributeSet;
import android.widget.ListView;

public class MyListView extends ListView {

	public MyListView(Context context) {
		super(context);
		init();
	}

	public MyListView(Context context, AttributeSet attrs) {
		super(context, attrs);
		init();
	}

	public MyListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}
	
	private void init() {
		//1、去除ListView默认的item状态选择器
		setSelector(new ColorDrawable());//此处不能传null
		
		//2、去除分割线
		setDivider(null);
		
		//3、去除在有些情况下，滑动listView的时候出现的黑色背景
		setCacheColorHint(Color.TRANSPARENT);
	}

}
