package cn.itcast.googleplay09.activity;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.R.layout;
import cn.itcast.googleplay09.application.MyApplication;
import cn.itcast.googleplay09.fragment.AppFragment;
import cn.itcast.googleplay09.fragment.BaseFragment;
import cn.itcast.googleplay09.fragment.FragmentFactory;
import cn.itcast.googleplay09.fragment.GameFragment;
import cn.itcast.googleplay09.fragment.HomeFragment;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.PagerTab;
import android.os.Bundle;
import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.Menu;
import android.view.View;

public class MainActivity extends BaseActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		// new MyApplication();
		// MyApplication.instance.context;

		// getResources().getString(id);
		/*
		 * String string = UiUtils.getString(R.string.app_name);
		 * 
		 * //View.inflate(this, R.layout.activity_main, null);
		 * UiUtils.inflateView(R.layout.activity_main);
		 * 
		 * UiUtils.runOnUiThread(new Runnable() {
		 * 
		 * @Override public void run() {
		 * 
		 * } });
		 */

		PagerTab pagerTab = (PagerTab) findViewById(R.id.pagerTab);
		ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
		viewPager.setAdapter(new MyAdapter(getSupportFragmentManager()));
		// getFragmentManager();
		// getSupportFragmentManager();
		pagerTab.setViewPager(viewPager);//将ViewPager和PagerTab绑定在一起
		
		//当ViewPager和PagerTab绑定在一起之后，事件必须设置给PagerTab
		pagerTab.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				BaseFragment fragment = FragmentFactory.getFragment(position);
				fragment.loadData();
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPixels) {
				
			}
			
			@Override
			public void onPageScrollStateChanged(int state) {
				
			}
		});

	}

	/**
	 * PagerAdapter:当ViewPager显示的是一个又一个普通的View对象的时候，使用PagerAdapter
	 * FragmentPagerAdapter
	 * ：当ViewPager显示的是一个又一个的Fragment的时候，使用FragmentPagerAdapter
	 * 
	 * @author zhengping
	 * 
	 */
	class MyAdapter extends FragmentPagerAdapter {

		private String[] mTabNames;

		public MyAdapter(FragmentManager fm) {
			super(fm);
			mTabNames = UiUtils.getStringArray(R.array.tab_names);// getResources().getStringArray(R.array.tab_names);
		}

		// 在某一个位置上需要显示的Fragment的对象
		// 需要把创建出来的Fragment对象进行缓存
		@Override
		public Fragment getItem(int position) {
			return FragmentFactory.getFragment(position);
		}

		@Override
		public int getCount() {
			// return 7;//魔术数字
			return mTabNames.length;
		}
		
		//给PagerTab调用的，用来返回在某一个位置上需要显示的标题
		@Override
		public CharSequence getPageTitle(int position) {
			return mTabNames[position];
		}

	}

}
