package cn.itcast.googleplay09.holder;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MoreHolder extends BaseHolder<Integer> {

	public static final int STATE_HAS_MORE = 0;
	public static final int STATE_NO_MORE = 1;
	public static final int STATE_ERROR = 2;
	public static final int STATE_HIDE_TIPS = 3;

	// private int mCurrentState;

	private ProgressBar pb;
	private TextView tvTips;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.list_item_loading);
		pb = (ProgressBar) view.findViewById(R.id.pb);
		tvTips = (TextView) view.findViewById(R.id.tvTips);
		return view;
	}

	@Override
	public void refreshView() {
		switch (mData) {
		case STATE_HAS_MORE:
			pb.setVisibility(View.VISIBLE);
			tvTips.setText("正在加载...");
			break;
		case STATE_NO_MORE:
			pb.setVisibility(View.GONE);
			tvTips.setText("没有更多数据了!!!");
			break;
		case STATE_ERROR:
			pb.setVisibility(View.GONE);
			tvTips.setText("加载失败,请点击重试...");
			break;
		case STATE_HIDE_TIPS:
			pb.setVisibility(View.GONE);
			tvTips.setVisibility(View.GONE);
			break;

		}
		// tvTips.setText(mData);
	}

}
