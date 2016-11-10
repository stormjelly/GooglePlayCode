package cn.itcast.googleplay09.fragment;

import java.util.HashMap;

/**
 * Fragment创建的工程类 1、创建对应的Fragment对象 2、将创建的Fragment对象进行缓存
 * 
 * @author zhengping
 * 
 */
public class FragmentFactory {

	private static HashMap<Integer, BaseFragment> savedFragment = new HashMap<Integer, BaseFragment>();

	public static BaseFragment getFragment(int position) {
		BaseFragment fragment = savedFragment.get(position);
		if (fragment == null) {
			switch (position) {
			case 0:
				fragment = new HomeFragment();
				break;
			case 1:
				fragment = new AppFragment();
				break;
			case 2:
				fragment = new GameFragment();
				break;
			case 3:
				fragment = new SubjectFragment();
				break;
			case 4:
				fragment = new RecommendFragment();
				break;
			case 5:
				fragment = new CategoryFragment();
				break;
			case 6:
				fragment = new HotFragment();
				break;
			}
			savedFragment.put(position, fragment);
		}
		return fragment;
	}

}
