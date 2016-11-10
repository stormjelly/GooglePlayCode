package cn.itcast.googleplay09.holder;

import com.lidroid.xutils.BitmapUtils;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.bean.DownloadInfo;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.manager.MyBitmapManager;
import cn.itcast.googleplay09.manager.MyDownloadManager;
import cn.itcast.googleplay09.manager.MyDownloadManager.DownloadObserver;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.ProgressArc;
import android.text.format.Formatter;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

public class HomeHolder extends BaseHolder<AppInfo> implements OnClickListener, DownloadObserver {

	private TextView tvContent;
	private ImageView app_icon;
	private TextView app_name;
	private TextView app_size;
	private TextView app_des;
	private RatingBar rating_bar;
	private TextView tv_download;
	private ProgressArc pa;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.list_item_home);
		app_icon = (ImageView) view.findViewById(R.id.app_icon);
		app_name = (TextView) view.findViewById(R.id.app_name);
		app_size = (TextView) view.findViewById(R.id.app_size);
		app_des = (TextView) view.findViewById(R.id.app_des);
		rating_bar = (RatingBar) view.findViewById(R.id.rating_bar);
		tv_download = (TextView) view.findViewById(R.id.tv_download);
		tv_download.setOnClickListener(this);
		
		FrameLayout fl_download = (FrameLayout) view.findViewById(R.id.fl_download);
		
		pa = new ProgressArc(UiUtils.getContext());
		
		pa.setArcDiameter(UiUtils.dip2px(26));
		pa.setProgressColor(UiUtils.getColor(R.color.progress));
		
		LayoutParams layoutParams = fl_download.getLayoutParams();
		layoutParams.width = UiUtils.dip2px(27);
		layoutParams.height = UiUtils.dip2px(27);
		fl_download.setLayoutParams(layoutParams);
		
		fl_download.addView(pa);
		
		
		MyDownloadManager.getInstance().addDownloadObserver(this);

		return view;
	}

	@Override
	public void refreshView() {
		// tvContent.setText(mData.name);
		app_name.setText(mData.name);
		//格式化文件大小
		String appSize = Formatter.formatFileSize(UiUtils.getContext(),
				Long.parseLong(mData.size));
		app_size.setText(appSize);
		app_des.setText(mData.des);
		float rating = Float.parseFloat(mData.stars);
		rating_bar.setRating(rating);

		//LRUCache:容器-->装bitmap对象
		BitmapUtils bitmapUtils = MyBitmapManager.getInstance().getBitmapUtils();//new BitmapUtils(UiUtils.getContext());
		// url的组成：主域名+模块名称+参数
		bitmapUtils.display(app_icon, HttpHelper.URL + "image?name="
				+ mData.iconUrl);
		
		DownloadInfo downloadInfo = MyDownloadManager.getInstance().mSavedDownloadInfo.get(mData.id);
		if(downloadInfo == null) {
			downloadInfo = DownloadInfo.createDownloadInfoFromAppInfo(mData);
		}
		updateUi(downloadInfo);
	}

	@Override
	public void onClick(View v) {

		// 启动子线程，在子线程中下载apk文件
		int state = MyDownloadManager.STATE_NONE;
		DownloadInfo downloadInfo = MyDownloadManager.getInstance().mSavedDownloadInfo
				.get(mData.id);
		if (downloadInfo != null) {
			state = downloadInfo.currentState;
		}

		switch (state) {
		case MyDownloadManager.STATE_NONE:
			MyDownloadManager.getInstance().startDownload(mData);
			break;
		case MyDownloadManager.STATE_WATING:
			MyDownloadManager.getInstance().pauseDownload(mData);
			break;
		case MyDownloadManager.STATE_DOWNLOADING:
			MyDownloadManager.getInstance().pauseDownload(mData);
			break;
		case MyDownloadManager.STATE_PAUSED:
			MyDownloadManager.getInstance().startDownload(mData);
			break;
		case MyDownloadManager.STATE_SUCCESS:
			MyDownloadManager.getInstance().installApk(mData);
			break;
		case MyDownloadManager.STATE_ERROR:
			MyDownloadManager.getInstance().startDownload(mData);
			break;

		}

		
	
	}

	@Override
	public void onDownloadStateChanged(final DownloadInfo downloadInfo) {
		// download_bt.setText(text);
		UiUtils.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updateUi(downloadInfo);
			}
		});

	}

	@Override
	public void onDownloadProgressChanged(final DownloadInfo downloadInfo) {
		UiUtils.runOnUiThread(new Runnable() {

			@Override
			public void run() {
				updateUi(downloadInfo);
			}
		});
	}

	private void updateUi(DownloadInfo downloadInfo) {

		if (downloadInfo.id.equals(mData.id)) {
			// download_bt.setText(text);
			int state = downloadInfo.currentState;
			long position = downloadInfo.currentPosition;
			String percent = (int) ((position * 1.0f / Long
					.parseLong(downloadInfo.size)) * 100) + "%";
			
			pa.setProgress((position * 1.0f / Long
					.parseLong(downloadInfo.size)), true);
			
			switch (state) {
			case MyDownloadManager.STATE_NONE:
				tv_download.setText("下载");
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//设置没有进度的样式
				pa.setBackgroundResource(R.drawable.ic_download);
				break;
			case MyDownloadManager.STATE_WATING:
				tv_download.setText("等待中");
				pa.setStyle(ProgressArc.PROGRESS_STYLE_WAITING);//设置没有进度的样式
				pa.setBackgroundResource(R.drawable.ic_pause);
				break;
			case MyDownloadManager.STATE_DOWNLOADING:
				tv_download.setText("下载中");
				pa.setStyle(ProgressArc.PROGRESS_STYLE_DOWNLOADING);//设置没有进度的样式
				pa.setBackgroundResource(R.drawable.ic_pause);
				pa.setProgress((position * 1.0f / Long
						.parseLong(downloadInfo.size)), true);
//				ph.setProgress((position * 1.0f / Long
//						.parseLong(downloadInfo.size)));
				break;
			case MyDownloadManager.STATE_PAUSED:
				tv_download.setText("暂停");
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//设置没有进度的样式
				pa.setBackgroundResource(R.drawable.ic_resume);
				break;
			case MyDownloadManager.STATE_SUCCESS:
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//设置没有进度的样式
				pa.setBackgroundResource(R.drawable.ic_install);
				tv_download.setText("安装");
				break;
			case MyDownloadManager.STATE_ERROR:
				pa.setStyle(ProgressArc.PROGRESS_STYLE_NO_PROGRESS);//设置没有进度的样式
				pa.setBackgroundResource(R.drawable.ic_redownload);
				tv_download.setText("失败");
				break;

			}
			
		}

	}
	

}
