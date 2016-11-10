package cn.itcast.googleplay09.adapter;

import java.util.ArrayList;

import cn.itcast.googleplay09.holder.BaseHolder;
import cn.itcast.googleplay09.holder.HomeHolder;
import cn.itcast.googleplay09.holder.MoreHolder;
import cn.itcast.googleplay09.manager.MyThreadPoolManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * 1、抽取
 * 		抽取的是共同东西
 * 2、抽象
 * 		a、对于父类不知道该如何完成的工作，我们使用抽象方法的方式交给子类来完成
 * 		b、对于父类不知道的数据类型，使用泛型来实现，泛型究竟是什么类型？--->在定义子类的时候确定下来
 * 			注意事项：尖括号写在自己类名的后面，代表定义了一种泛型，如果跟着父类名称的后面，代表确定父类中的泛型类型
 * 
 * 
 * 让ListView实现上拉加载更多的功能：
 * 	把加载更多的这个View当作ListView普通的一个item进行显示，而不是脚布局
 * 	所以此时就需要ListView显示多种布局类型
 * 
 * 显示多种布局类型的步骤：
 * 1、加载更多的View和数据集合没有关系：datas.size()+1
 * 2、告诉系统你要显示几种布局类型
 * 3、定义一个规则，在什么位置上显示哪种布局类型
 * 4、根据自己定义的规则(getItemViewType的返回值)，来加载不同的布局文件以及刷新对应的数据
 * 
 * @author zhengping
 *
 * @param <T>
 */

public abstract class MyBaseAdapter<T> extends BaseAdapter {
	
	public static final int TYPE_NORMAL = 0;//布局类型的标识必须从0开始，依次递增
	public static final int TYPE_MORE = 1;
	public static final int TYPE_TITLE = 2;
	
	
	private ArrayList<T> datas;
	
	public MyBaseAdapter(ArrayList<T> datas){
		this.datas = datas;
		//String str = "";
	}
	
	@Override
	public int getViewTypeCount() {
		return super.getViewTypeCount()+1;
	}
	
	@Override
	public int getItemViewType(int position) {
		if(position == getCount() - 1) {
			//此时代表是最后一项
			return TYPE_MORE;
		} else {
			return getInnerType(position);
		}
	}


	public int getInnerType(int position) {
		return TYPE_NORMAL;
	}

	@Override
	public int getCount() {
		return datas.size()+1;
	}

	@Override
	public Object getItem(int position) {
		return datas.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public MoreHolder mMoreHolder;
	
	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		BaseHolder holder = null;
		if(convertView == null) {
			
			int itemViewType = getItemViewType(position);
			if(itemViewType != TYPE_MORE) {
				holder = getHolder(position);//new HomeHolder();
			} else {
				//加载  加载更多的布局文件
				if(mMoreHolder == null) {
					mMoreHolder = new MoreHolder();
					//mMoreHolder.setData(MoreHolder.STATE_HAS_MORE);
					mMoreHolder.setData(hideTips()?MoreHolder.STATE_HIDE_TIPS :(hasMore()?MoreHolder.STATE_HAS_MORE:MoreHolder.STATE_NO_MORE));
				}
				holder = mMoreHolder;
			}
			
			
		}else {
			holder = (BaseHolder) convertView.getTag();
		}
		
		int itemViewType = getItemViewType(position);
		if(itemViewType != TYPE_MORE) {
			holder.setData(datas.get(position));
		} else {
			//刷新  加载更多这个条目中数据
			//holder.setData(MoreHolder.STATE_HAS_MORE);
			loadMore();
		}

		return holder.convertView;
	}
	//如果父类中有默认的处理流程的话，此时可以不抽象。子类如果有特殊的处理，那么子类重写此方法就可以了
	public boolean hasMore() {
		return true;
	}
	
	public boolean hideTips() {
		return false;
	}
	
	private boolean isLoadingMore = false;
	//加载下一页数据
	private void loadMore() {
		
		//获取当前MoreHolder的状态
		if(mMoreHolder.mData != MoreHolder.STATE_HAS_MORE) {
			return;
		}
		if(isLoadingMore) {
			return;
		}
		isLoadingMore = true;
		//启动子线程去加载下一页的网络数据
		MyThreadPoolManager.getInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				
				//模拟网络延迟
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				
				final ArrayList<T> moreData = onLoadMore();
				UiUtils.runOnUiThread(new Runnable() {

					@Override
					public void run() {
						if(moreData == null) {
							mMoreHolder.setData(MoreHolder.STATE_ERROR);
						} else {
							if(moreData.size() == 0) {
								mMoreHolder.setData(MoreHolder.STATE_NO_MORE);
							} else {
								datas.addAll(moreData);
								notifyDataSetChanged();
							}
						}
					}});
				
				
				isLoadingMore = false;
			}
		});
		//new Thread().start();
	}
	
	public int getListSize() {
		if(datas == null){
			return 0;
		} else {
			return datas.size();
		}
	}
	
	//加载下一页的数据
	public abstract ArrayList<T> onLoadMore() ;

	public abstract BaseHolder getHolder(int position);

}
