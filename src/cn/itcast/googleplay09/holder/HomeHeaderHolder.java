package cn.itcast.googleplay09.holder;

import java.util.ArrayList;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.manager.MyBitmapManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.LinearLayout.LayoutParams;

public class HomeHeaderHolder extends BaseHolder<ArrayList<String>> {

	private ViewPager viewPager;
	private LinearLayout dotLayout;

	@Override
	public View initView() {
		RelativeLayout rootLayout = new RelativeLayout(UiUtils.getContext());
		AbsListView.LayoutParams params = new AbsListView.LayoutParams(
				AbsListView.LayoutParams.MATCH_PARENT, UiUtils.dip2px(150));
		rootLayout.setLayoutParams(params);

		// 1、viewPager
		viewPager = new ViewPager(UiUtils.getContext());
		RelativeLayout.LayoutParams viewPagerParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.MATCH_PARENT,
				RelativeLayout.LayoutParams.MATCH_PARENT);
		viewPager.setLayoutParams(viewPagerParams);
		rootLayout.addView(viewPager);
		// 2、LinearLayout指示器的容器
		dotLayout = new LinearLayout(UiUtils.getContext());
		RelativeLayout.LayoutParams dotParams = new RelativeLayout.LayoutParams(
				RelativeLayout.LayoutParams.WRAP_CONTENT,
				RelativeLayout.LayoutParams.WRAP_CONTENT);
		// dotLayout.setLayoutParams(dotParams);
		// rootLayout.addView(dotLayout);
		dotParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		dotParams.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
		dotParams.rightMargin = UiUtils.dip2px(5);
		dotParams.bottomMargin = UiUtils.dip2px(5);
		rootLayout.addView(dotLayout, dotParams);

		return rootLayout;
	}

	@Override
	public void refreshView() {
		viewPager.setAdapter(new MyAdapter());
		
		for(int i=0;i<mData.size();i++) {
			ImageView dot = new ImageView(UiUtils.getContext());
			if(i==0) {
				dot.setImageResource(R.drawable.indicator_selected);
			} else {
				dot.setImageResource(R.drawable.indicator_normal);
			}
			
			
			LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
			params.rightMargin = UiUtils.dip2px(3);
			
			dotLayout.addView(dot,params);
		}
		
		
		viewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				for(int i=0;i<dotLayout.getChildCount();i++) {
					ImageView dotIv = (ImageView) dotLayout.getChildAt(i);
					if(position == i) {
						dotIv.setImageResource(R.drawable.indicator_selected);
					} else {
						dotIv.setImageResource(R.drawable.indicator_normal);
					}
				}
				
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
	}

	class MyAdapter extends PagerAdapter {

		@Override
		public int getCount() {
			return mData.size();
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view == object;
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {

			ImageView iv = new ImageView(UiUtils.getContext());
			iv.setScaleType(ScaleType.CENTER_CROP);
			MyBitmapManager
					.getInstance()
					.getBitmapUtils()
					.display(
							iv,
							HttpHelper.URL + "image?name="
									+ mData.get(position));

			container.addView(iv);

			return iv;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// super.destroyItem(container, position, object);
			container.removeView((View) object);
		}

	}

}
