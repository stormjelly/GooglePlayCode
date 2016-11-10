package cn.itcast.googleplay09.fragment;

import java.util.ArrayList;

import cn.itcast.googleplay09.adapter.MyBaseAdapter;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.holder.BaseHolder;
import cn.itcast.googleplay09.holder.HomeHolder;
import cn.itcast.googleplay09.http.protocol.AppProtocol;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import android.view.View;
import android.widget.ListView;

/**
 * 应用的Fragment
 * @author zhengping
 *
 */
public class AppFragment extends BaseFragment {

	private ArrayList<AppInfo> data;

	@Override
	public View fragmentCreateSuccessView() {
		ListView listView = new ListView(UiUtils.getContext());
		listView.setAdapter(new MyAdapter(data));
		return listView;
	}
	@Override
	public ResultState fragmentLoadData() {
		AppProtocol protocol = new AppProtocol();
		data = protocol.getData();
		return checkData(data);
	}
	
	class MyAdapter extends MyBaseAdapter<AppInfo> {

		public MyAdapter(ArrayList<AppInfo> datas) {
			super(datas);
		}

		@Override
		public ArrayList<AppInfo> onLoadMore() {
			AppProtocol protocol = new AppProtocol();
			protocol.setIndex(getListSize());
			ArrayList<AppInfo> moreData = protocol.getData();
			
			return moreData;
		}

		@Override
		public BaseHolder getHolder(int position) {
			return new HomeHolder();
		}
		
	}

	
	
}
