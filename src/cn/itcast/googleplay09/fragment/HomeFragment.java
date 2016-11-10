package cn.itcast.googleplay09.fragment;

import java.util.ArrayList;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.activity.AppDetailActivity;
import cn.itcast.googleplay09.adapter.MyBaseAdapter;
import cn.itcast.googleplay09.application.MyApplication;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.holder.BaseHolder;
import cn.itcast.googleplay09.holder.HomeHeaderHolder;
import cn.itcast.googleplay09.holder.HomeHolder;
import cn.itcast.googleplay09.http.protocol.HomeProtocol;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import cn.itcast.googleplay09.ui.widget.MyListView;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * 首页的Fragment
 * @author zhengping
 *
 */
public class HomeFragment extends BaseFragment {
	
	//private ArrayList<String> datas = new ArrayList<String>();
	private ArrayList<AppInfo> data;
	
	public String testStr = "";

	private ArrayList<String> picList;

	@Override
	public View fragmentCreateSuccessView() {
		//TextView tv = new TextView(UiUtils.getContext());
		//tv.setText("首页数据加载成功...");
		MyListView listView = new MyListView(UiUtils.getContext());
//		for(int i=0;i<20;i++) {
//			datas.add("测试数据:" + i);
//		}
		
		/*View headerView = UiUtils.inflateView(R.layout.xxx);
		ViewPager viewPager = headerView.findViewById(R.id.viewPager);
		viewPager.setAdapter(adapter);*/
		HomeHeaderHolder headerHolder = new HomeHeaderHolder();
		headerHolder.setData(picList);
		listView.addHeaderView(headerHolder.convertView);
		
		//去除ListView默认的item状态选择器
		//listView.setSelector(new ColorDrawable());
		
		listView.setAdapter(new MyAdapter(data));
		
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent();
				intent.setClass(UiUtils.getContext(), AppDetailActivity.class);
				AppInfo appInfo = data.get(position-1);//由于设置了头布局，所以头布局的position为0
				//1、intent：int、String...对象
				//2、View.setTag
				//3、Application
				//intent.putExtra("name", appInfo.name);
				MyApplication.instance.appInfo = appInfo;
				getActivity().startActivity(intent);
				//getActivity().overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
				
			}
		});
		
		return listView;
	}

	@Override
	public ResultState fragmentLoadData() {
		HomeProtocol protocol = new HomeProtocol();
		data = protocol.getData();
		picList = protocol.getPicList();
		return checkData(data);
		/*if(data == null) {
			return ResultState.ERROR;
		} else {
			if(data.size() == 0) {
				return ResultState.EMPTY;
			} else {
				return ResultState.SUCCESS;
			}
		}*/
		//return ResultState.SUCCESS;
	}

	
	/**
	 * 1、ListView的优化
	 * 2、ListView显示多种布局类型
	 * 3、ListView数据显示错乱的时候解决方案
	 * @author zhengping
	 *
	 */
	class MyAdapter extends MyBaseAdapter<AppInfo> {

		public MyAdapter(ArrayList<AppInfo> datas) {
			super(datas);
		}

		@Override
		public BaseHolder getHolder(int position) {
			return new HomeHolder();
		}

		//获取下一页数据
		//more
		//pageNo  pageSize  --->totalCount
		//index
		/**
		 *  第一页的数据：index=0  当前数据集合数量0
		 *  第二页的数据：index=20 当前数据集合的数量20
		 *  第三页的数据：index=40 当前数据集合的数量40
		 *  第四页的数据：index=60
		 *  
		 *  把当前数据集合中的数量当作index的值进行传递就可以获取下一页的数据
		 */
		@Override
		public ArrayList<AppInfo> onLoadMore() {
			
			HomeProtocol protocol = new HomeProtocol();
			protocol.setIndex(getListSize());
			ArrayList<AppInfo> moreData = protocol.getData();
			
			/*ArrayList<String> moreData = new ArrayList<String>();
			for(int i=0;i<20;i++) {
				//moreData.add("更多数据:" + i);
			}*/
			return moreData;
		}
		
		/*@Override
		public boolean hasMore() {
			return false;
		}*/

		/*@Override
		public int getCount() {
			return datas.size();
		}

		@Override
		public Object getItem(int position) {
			return datas.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}*/

		
		//ListView的优化
		//1、使用convertView：为了减少加载布局文件的次数
		//2、使用ViewHolder：为了减少findViewById的次数
		
		//1、加载布局文件   convertView、布局文件的id
		//2、初始化控件    convertView  holder
		//3、设置tag,存储holder对象  convertView  holder
		//4、刷新控件的数据  holder、数据对象(String)
		/*@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null) {
				convertView = UiUtils.inflateView(R.layout.list_item_home);//1、加载布局文件
				holder = new ViewHolder();
				holder.tvContent = (TextView) convertView.findViewById(R.id.tvContent);//2、初始化控件
				convertView.setTag(holder);//3、设置tag,存储holder对象
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			holder.tvContent.setText(datas.get(position));//4、刷新控件的数据
			
			return convertView;
		}*/
		
	}
	
	
	static class ViewHolder {
		TextView tvContent;
		/*public void test() {
			testStr = "123";
		}*/
	}
}
