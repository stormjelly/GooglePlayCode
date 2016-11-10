package cn.itcast.googleplay09.fragment;

import java.util.ArrayList;

import cn.itcast.googleplay09.adapter.MyBaseAdapter;
import cn.itcast.googleplay09.bean.CategoryInfo;
import cn.itcast.googleplay09.holder.BaseHolder;
import cn.itcast.googleplay09.holder.CategoryNormalHolder;
import cn.itcast.googleplay09.holder.CategoryTitileHolder;
import cn.itcast.googleplay09.http.protocol.CategoryProtocol;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import android.view.View;
import android.widget.ListView;

/**
 * 分类的Fragment
 * @author zhengping
 *
 */
public class CategoryFragment extends BaseFragment {

	private ArrayList<CategoryInfo> data;

	@Override
	public View fragmentCreateSuccessView() {
		ListView listView = new ListView(UiUtils.getContext());
		listView.setAdapter(new MyAdapter(data));
		return listView;
	}
	
	@Override
	public ResultState fragmentLoadData() {
		CategoryProtocol protocol = new CategoryProtocol();
		data = protocol.getData();
		return checkData(data);
	}
	
	class MyAdapter extends MyBaseAdapter<CategoryInfo> {

		public MyAdapter(ArrayList<CategoryInfo> datas) {
			super(datas);
		}
		
		@Override
		public int getViewTypeCount() {
			return super.getViewTypeCount()+1;
		}
		
		@Override
		public int getInnerType(int position) {
			CategoryInfo info = (CategoryInfo) getItem(position);
			if(info.isTitle) {
				return TYPE_TITLE;
			} else {
				return TYPE_NORMAL;
			}
			//return super.getInnerType();
		}
		
		public boolean hasMore() {
			return false;
		}
		
		@Override
		public boolean hideTips() {
			return true;
		}

		@Override
		public BaseHolder getHolder(int position) {
			//加载不同的布局文件
			CategoryInfo info = (CategoryInfo) getItem(position);
			if(info.isTitle) {
				return new CategoryTitileHolder();
			} else {
				return new CategoryNormalHolder();
			}
			//return null;
		}
		
		@Override
		public ArrayList<CategoryInfo> onLoadMore() {
			return null;
		}
		
	}
}
