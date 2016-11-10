package cn.itcast.googleplay09.holder;

import android.view.View;
import android.widget.TextView;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.CategoryInfo;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class CategoryTitileHolder extends BaseHolder<CategoryInfo> {

	private TextView tv;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.layout_category_title);
		tv = (TextView) view.findViewById(R.id.tv);
		return view;
	}

	@Override
	public void refreshView() {
		tv.setText(mData.title);
	}

}
