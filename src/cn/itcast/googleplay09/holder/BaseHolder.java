package cn.itcast.googleplay09.holder;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import android.view.View;
import android.widget.TextView;

//1、加载布局文件   convertView、布局文件的id
//2、初始化控件    convertView  holder
//3、设置tag,存储holder对象  convertView  holder
//4、刷新控件的数据  holder、数据对象(String)

//MVP    M:数据  V：视图   P-->将M和V结合起来   BaseHolder承担的是p的角色

//高内聚--低耦合

public abstract class BaseHolder<T> {
	
	public View convertView;
	public T mData;
	//private TextView tvContent;
	
	public BaseHolder() {
		convertView = initView();
		convertView.setTag(this);
	}
	
	public abstract View initView();
	
	public void setData(T data) {
		this.mData = data;
		refreshView();
	}

	public abstract void refreshView() ;

}
