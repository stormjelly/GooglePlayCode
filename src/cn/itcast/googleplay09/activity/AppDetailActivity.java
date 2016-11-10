package cn.itcast.googleplay09.activity;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.application.MyApplication;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.holder.DetailActionHolder;
import cn.itcast.googleplay09.holder.DetailDesHolder;
import cn.itcast.googleplay09.holder.DetailSafeHolder;
import cn.itcast.googleplay09.holder.DetailScreenHolder;
import cn.itcast.googleplay09.holder.DetailSimpleHolder;
import cn.itcast.googleplay09.holder.HomeHolder;
import cn.itcast.googleplay09.http.protocol.DetailProtocol;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;
import android.widget.TextView;

public class AppDetailActivity extends BaseActivity {
	
	private AppInfo appInfo;
	private AppInfo data;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LoadingPage loadingPage = new LoadingPage(UiUtils.getContext()){

			@Override
			public View createSuccessView() {
				return activityCreateSuccessView();
			}

			@Override
			public ResultState onLoad() {
				return activityLoadData();
			}};
		setContentView(loadingPage);
		loadingPage.loadData();//加载网络数据
		
		appInfo = MyApplication.instance.appInfo;
		
		/*TextView tvName = (TextView) findViewById(R.id.tvName);
		tvName.setText(appInfo.name);*/
		
	}
	
	private ResultState activityLoadData() {
		DetailProtocol protocol = new DetailProtocol();
		protocol.setPackageName(appInfo.packageName);
		data = protocol.getData();
		if(data == null) {
			return ResultState.ERROR;
		}
		return ResultState.SUCCESS;
	}
	
	private View activityCreateSuccessView() {
		View view = UiUtils.inflateView(R.layout.layout_detail);
		//1、简要信息区域
		FrameLayout flSimple = (FrameLayout) view.findViewById(R.id.flSimple);
		DetailSimpleHolder simpleHolder = new DetailSimpleHolder();
		simpleHolder.setData(data);
		flSimple.addView(simpleHolder.convertView);
		//2、审核区域
		FrameLayout flSafe = (FrameLayout) view.findViewById(R.id.flSafe);
		DetailSafeHolder safeHolder = new DetailSafeHolder();
		safeHolder.setData(data);
		flSafe.addView(safeHolder.convertView);
		//3、截图区域
		FrameLayout flScreen = (FrameLayout) view.findViewById(R.id.flScreen);
		HorizontalScrollView hsv = new HorizontalScrollView(UiUtils.getContext());
		DetailScreenHolder screenHolder = new DetailScreenHolder();
		screenHolder.setData(data);
		hsv.addView(screenHolder.convertView);
		flScreen.addView(hsv);
		
		//4、描述区域
		//ScrollView sc = (ScrollView) view.findViewById(R.id.sc);
		FrameLayout  flDes = (FrameLayout) view.findViewById(R.id.flDes);
		DetailDesHolder desHolder = new DetailDesHolder();
		//desHolder.setScrollView(sc);
		desHolder.setData(data);
		flDes.addView(desHolder.convertView);
		
		//5、操作区域
		FrameLayout flAction = (FrameLayout) view.findViewById(R.id.flAction);
		DetailActionHolder actionHolder = new DetailActionHolder();
		actionHolder.setData(data);
		flAction.addView(actionHolder.convertView);
		return view;
	}
	
	
}
