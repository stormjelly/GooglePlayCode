package cn.itcast.googleplay09.fragment;

import java.util.ArrayList;

import cn.itcast.googleplay09.adapter.MyBaseAdapter;
import cn.itcast.googleplay09.bean.SubjectInfo;
import cn.itcast.googleplay09.holder.BaseHolder;
import cn.itcast.googleplay09.holder.SubjectHolder;
import cn.itcast.googleplay09.http.protocol.SubjectProtocol;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import android.view.View;
import android.widget.ListView;

/**
 * 专题的Fragment
 * @author zhengping
 *
 */
public class SubjectFragment extends BaseFragment {

	private ArrayList<SubjectInfo> data;

	@Override
	public View fragmentCreateSuccessView() {
		ListView listView = new ListView(UiUtils.getContext());
		listView.setAdapter(new MyAdapter(data));
		return listView;
	}
	
	@Override
	public ResultState fragmentLoadData() {
		SubjectProtocol protocol = new SubjectProtocol();
		data = protocol.getData();
		return checkData(data);
		//return ResultState.EMPTY;
	}
	
	class MyAdapter extends MyBaseAdapter<SubjectInfo> {

		public MyAdapter(ArrayList<SubjectInfo> datas) {
			super(datas);
		}

		@Override
		public ArrayList<SubjectInfo> onLoadMore() {
			SubjectProtocol protocol = new SubjectProtocol();
			protocol.setIndex(getListSize());
			ArrayList<SubjectInfo> moreData = protocol.getData();
			return moreData;
		}

		@Override
		public BaseHolder getHolder(int position) {
			return new SubjectHolder();
		}
		
	}
}
