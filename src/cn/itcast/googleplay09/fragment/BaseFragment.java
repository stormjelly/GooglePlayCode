package cn.itcast.googleplay09.fragment;

import java.util.List;

import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

/**
 * 这个项目中所有Fragment的基类
 * 
 * @author zhengping
 * 
 */
public abstract class BaseFragment extends Fragment {

	private LoadingPage loadingPage;

	// Fragment到底包装的是哪一个View对象，完全是有onCreateView的返回值来决定的
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		loadingPage = new LoadingPage(UiUtils.getContext()){

			@Override
			public View createSuccessView() {
				return fragmentCreateSuccessView();
			}

			@Override
			public ResultState onLoad() {
				return fragmentLoadData();
			}};
		//TextView tv = new TextView(UiUtils.getContext());
		//tv.setText(this.getClass().getSimpleName());
		return loadingPage;
	}
	
	public void loadData() {
		if(loadingPage != null) {
			loadingPage.loadData();
		}
	}
	
	public ResultState checkData(Object obj) {
		if(obj == null) {
			return ResultState.ERROR;
		} else {
			if(obj instanceof List) {
				List list = (List) obj;
				if(list.size() == 0) {
					return ResultState.EMPTY;
				} else {
					return ResultState.SUCCESS;
				}
			}
		}
		
		return ResultState.ERROR;
	}
	
	//由于BaseFragment无法确定每一个Fragment中成功之后的布局长什么样子，所以需要把这项工作转交出去
	public abstract View fragmentCreateSuccessView() ;
	
	//由于BaseFragment无法确定每一个Fragment中URL的地址等等一些具体的情况，所以BaseFragment无法完成加载网络数据这一部分的工作，需要转交出去
	public abstract ResultState fragmentLoadData() ;
	
	

}
