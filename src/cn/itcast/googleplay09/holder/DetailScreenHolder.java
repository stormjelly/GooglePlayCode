package cn.itcast.googleplay09.holder;

import android.view.View;
import android.widget.ImageView;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.manager.MyBitmapManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class DetailScreenHolder extends BaseHolder<AppInfo> {

	private ImageView[] images;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.layout_detail_screen);
		images = new ImageView[5];
		images[0] = (ImageView) view.findViewById(R.id.image1);
		images[1] = (ImageView) view.findViewById(R.id.image2);
		images[2] = (ImageView) view.findViewById(R.id.image3);
		images[3] = (ImageView) view.findViewById(R.id.image4);
		images[4] = (ImageView) view.findViewById(R.id.image5);
		return view;
	}

	@Override
	public void refreshView() {
		for (int i = 0; i < mData.screen.size(); i++) {
			String picUrl = mData.screen.get(i);
			MyBitmapManager
					.getInstance()
					.getBitmapUtils()
					.display(images[i], HttpHelper.URL + "image?name=" + picUrl);
		}
	}

}
