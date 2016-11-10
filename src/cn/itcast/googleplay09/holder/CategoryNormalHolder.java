package cn.itcast.googleplay09.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.CategoryInfo;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.manager.MyBitmapManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class CategoryNormalHolder extends BaseHolder<CategoryInfo> {

	private ImageView image1;
	private ImageView image2;
	private ImageView image3;
	private TextView text1;
	private TextView text2;
	private TextView text3;

	@Override
	public View initView() {

		View view = UiUtils.inflateView(R.layout.layout_category_item);
		image1 = (ImageView) view.findViewById(R.id.image1);
		image2 = (ImageView) view.findViewById(R.id.image2);
		image3 = (ImageView) view.findViewById(R.id.image3);

		text1 = (TextView) view.findViewById(R.id.text1);
		text2 = (TextView) view.findViewById(R.id.text2);
		text3 = (TextView) view.findViewById(R.id.text3);

		return view;
	}

	@Override
	public void refreshView() {
		text1.setText(mData.name1);
		text2.setText(mData.name2);
		text3.setText(mData.name3);
		MyBitmapManager.getInstance().getBitmapUtils()
				.display(image1, HttpHelper.URL + "image?name=" + mData.url1);
		MyBitmapManager.getInstance().getBitmapUtils()
				.display(image2, HttpHelper.URL + "image?name=" + mData.url2);
		MyBitmapManager.getInstance().getBitmapUtils()
				.display(image3, HttpHelper.URL + "image?name=" + mData.url3);

	}

}
