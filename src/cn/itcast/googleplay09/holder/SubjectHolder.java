package cn.itcast.googleplay09.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.SubjectInfo;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.manager.MyBitmapManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class SubjectHolder extends BaseHolder<SubjectInfo> {

	private ImageView image;
	private TextView tv;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.list_item_subject);
		image = (ImageView) view.findViewById(R.id.image);
		tv = (TextView) view.findViewById(R.id.tv);

		return view;
	}

	@Override
	public void refreshView() {
		tv.setText(mData.des);
		MyBitmapManager.getInstance().getBitmapUtils()
				.display(image, HttpHelper.URL + "image?name=" + mData.url);
	}

}
