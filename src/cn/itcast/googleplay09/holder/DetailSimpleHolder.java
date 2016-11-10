package cn.itcast.googleplay09.holder;

import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.manager.MyBitmapManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class DetailSimpleHolder extends BaseHolder<AppInfo> {

	private ImageView appicon;
	private TextView appname;
	private TextView appdownload;
	private TextView appversion;
	private TextView apptime;
	private TextView appsize;
	private RatingBar appstar;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.layout_appinfo_item);
		appicon = (ImageView) view.findViewById(R.id.appicon);
		appname = (TextView) view.findViewById(R.id.appname);
		appdownload = (TextView) view.findViewById(R.id.appdownload);
		appversion = (TextView) view.findViewById(R.id.appversion);
		apptime = (TextView) view.findViewById(R.id.apptime);
		appsize = (TextView) view.findViewById(R.id.appsize);
		appstar = (RatingBar) view.findViewById(R.id.appstar);
		return view;
	}

	@Override
	public void refreshView() {
		MyBitmapManager.getInstance().getBitmapUtils().display(appicon, HttpHelper.URL + "image?name=" + mData.iconUrl);
		appname.setText(mData.name);
		appdownload.setText(mData.downloadNum);
		appversion.setText(mData.version);
		apptime.setText(mData.date);
		appsize.setText(mData.size);
		Float rating = Float.parseFloat(mData.stars);
		appstar.setRating(rating);
		
		
	}

}
