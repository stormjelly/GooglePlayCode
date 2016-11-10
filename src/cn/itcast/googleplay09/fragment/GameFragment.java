package cn.itcast.googleplay09.fragment;

import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import android.view.View;

/**
 * 游戏的Fragment
 * @author zhengping
 *
 */
public class GameFragment extends BaseFragment {

	@Override
	public View fragmentCreateSuccessView() {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public ResultState fragmentLoadData() {
		return ResultState.ERROR;
	}

}
