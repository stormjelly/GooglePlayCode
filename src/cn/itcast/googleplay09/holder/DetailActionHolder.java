package cn.itcast.googleplay09.holder;

import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.bean.DownloadInfo;
import cn.itcast.googleplay09.manager.MyDownloadManager;
import cn.itcast.googleplay09.manager.MyDownloadManager.DownloadObserver;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.ProgressHorizontal;

public class DetailActionHolder extends BaseHolder<AppInfo> implements
		OnClickListener, DownloadObserver {

	private Button download_bt;
	private ProgressHorizontal ph;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.layout_detail_download);
		download_bt = (Button) view.findViewById(R.id.download_bt);
		download_bt.setOnClickListener(this);

		FrameLayout  download_fl = (FrameLayout) view.findViewById(R.id.download_fl);
		ph = new ProgressHorizontal(UiUtils.getContext());
		ph.setProgressBackgroundResource(R.drawable.progress_bg);
		ph.setProgressResource(R.drawable.progress_pressed);
		ph.setProgressTextColor(Color.WHITE);
		ph.setProgressTextSize(30);
		download_fl.addView(ph);
		MyDownloadManager.getInstance().addDownloadObserver(this);
		return view;
	}

	@Override
	public void refreshView() {
		
		//初始化的UI
		DownloadInfo downloadInfo = MyDownloadManager.getInstance().mSavedDownloadInfo.get(mData.id);
		if(downloadInfo == null) {
			downloadInfo = DownloadInfo.createDownloadInfoFromAppInfo(mData);
		}
		updateUi(downloadInfo);

	}
	
	//一个应用程序就只需要一个线程池，由这个池子管理这个应用中所有的子线程的创建
	//按模块进行划分：

	// 下载应用程序的apk文件
	// 子线程的数量非常多了，应用程序卡顿
	// 保证子线程的数量得到有效的控制-->线程池：池子，装的线程
	// LRUCache:
	// Thread\Runnable
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
			download_bt.setText("state=" + state + ",percent=" + percent);
			
			
			switch (state) {
			case MyDownloadManager.STATE_NONE:
				ph.setCenterText("下载");
				break;
			case MyDownloadManager.STATE_WATING:
				ph.setCenterText("等待中");
				break;
			case MyDownloadManager.STATE_DOWNLOADING:
				ph.setCenterText("下载中:" + percent);
				ph.setProgress((position * 1.0f / Long
						.parseLong(downloadInfo.size)));
				break;
			case MyDownloadManager.STATE_PAUSED:
				ph.setCenterText("暂停中,请点击继续下载");
				break;
			case MyDownloadManager.STATE_SUCCESS:
				ph.setCenterText("安装");
				break;
			case MyDownloadManager.STATE_ERROR:
				ph.setCenterText("下载失败,请点击重试");
				break;

			}
			
		}

	}

}
