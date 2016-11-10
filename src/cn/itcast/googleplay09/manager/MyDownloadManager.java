package cn.itcast.googleplay09.manager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;

import android.content.Intent;
import android.net.Uri;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.bean.DownloadInfo;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.http.HttpHelper.HttpResult;
import cn.itcast.googleplay09.ui.utils.IOUtils;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class MyDownloadManager {

	public static final int STATE_NONE = 0;// 未下载
	public static final int STATE_WATING = 1;// 等待
	public static final int STATE_DOWNLOADING = 2;// 正在下载
	public static final int STATE_PAUSED = 3;// 下载暂停
	public static final int STATE_SUCCESS = 4;// 下载成功
	public static final int STATE_ERROR = 5;// 下载失败

	private MyDownloadManager() {
	}

	private static MyDownloadManager instance;

	public synchronized static MyDownloadManager getInstance() {
		if (instance == null) {
			instance = new MyDownloadManager();
		}
		return instance;
	}

	public HashMap<String, DownloadInfo> mSavedDownloadInfo = new HashMap<String, DownloadInfo>();
	public HashMap<String, DownloadTask> mSavedDownloadTask = new HashMap<String, DownloadTask>();

	public void startDownload(AppInfo appInfo) {
		// 创建任务，丢到线程池里面去
		// appInfo：downloadUrl、id、name、packageName
		// 下载路径，下载进度，下载状态
		DownloadInfo downloadInfo = mSavedDownloadInfo.get(appInfo.id);
		if (downloadInfo == null) {
			downloadInfo = DownloadInfo.createDownloadInfoFromAppInfo(appInfo);
			mSavedDownloadInfo.put(appInfo.id, downloadInfo);
		}
		DownloadTask task = new DownloadTask(downloadInfo);
		mSavedDownloadTask.put(appInfo.id, task);
		downloadInfo.currentState = MyDownloadManager.STATE_WATING;
		notifyDownloadStateChanged(downloadInfo);
		MyThreadPoolManager.getInstance().execute(task);

	}

	class DownloadTask implements Runnable {

		private DownloadInfo downloadInfo;

		public DownloadTask(DownloadInfo downloadInfo) {
			this.downloadInfo = downloadInfo;
		}

		@Override
		public void run() {
			FileOutputStream fos = null;
			try {

				// 判断是否是第一次下载
				File downloadFile = new File(downloadInfo.filePath);
				String url = "";
				if (!downloadFile.exists()
						|| downloadInfo.currentPosition == 0
						|| downloadInfo.currentPosition != downloadFile
								.length()) {
					// 第一次下载
					downloadFile.delete();
					downloadInfo.currentPosition = 0;
					url = HttpHelper.URL + "download" + "?name="
							+ downloadInfo.downloadUrl;
					
				} else {
					//非第一次下载
					url = HttpHelper.URL + "download" + "?name="
							+ downloadInfo.downloadUrl + "&range=" + downloadInfo.currentPosition;
				}
				// 下载
				downloadInfo.currentState = MyDownloadManager.STATE_DOWNLOADING;
				notifyDownloadStateChanged(downloadInfo);
				
				HttpResult result = HttpHelper.download(url);
				if (result != null) {
					// 访问到服务器
					InputStream inputStream = result.getInputStream();
					if (inputStream != null) {

						fos = new FileOutputStream(downloadFile,true);//true代表从文件的末尾进行追加
						byte[] buffer = new byte[1024];// 300
						int length = 0;
						while ((length = inputStream.read(buffer)) != -1
								&& downloadInfo.currentState == MyDownloadManager.STATE_DOWNLOADING) {
							fos.write(buffer, 0, length);
							downloadInfo.currentPosition += length;
							notifyDownloadProgressChanged(downloadInfo);
							fos.flush();
						}

						// 是否下载成功
						// 服务器上面文件的大小
						long serverFileSize = Long.parseLong(downloadInfo.size);
						long localFileSize = downloadInfo.currentPosition;
						if (serverFileSize == localFileSize) {
							// 下载成功
							downloadInfo.currentState = MyDownloadManager.STATE_SUCCESS;
							notifyDownloadStateChanged(downloadInfo);
						} else {

							if (downloadInfo.currentState == MyDownloadManager.STATE_PAUSED) {
								// 下载暂停
								downloadInfo.currentState = MyDownloadManager.STATE_PAUSED;
								notifyDownloadStateChanged(downloadInfo);
							} else {
								// 下载失败，
								downloadInfo.currentState = MyDownloadManager.STATE_ERROR;
								notifyDownloadStateChanged(downloadInfo);
							}
						}

					} else {
						// 此时代表已经访问到服务器了，但是服务器找不到你要下载的那个文件
						// 下载失败
						downloadInfo.currentState = MyDownloadManager.STATE_ERROR;
						notifyDownloadStateChanged(downloadInfo);
					}
				} else {
					// 下载失败
					downloadInfo.currentState = MyDownloadManager.STATE_ERROR;
					notifyDownloadStateChanged(downloadInfo);
				}
			} catch (Exception e) {
				// 下载失败
				downloadInfo.currentState = MyDownloadManager.STATE_ERROR;
				notifyDownloadStateChanged(downloadInfo);
			} finally {
				IOUtils.close(fos);
			}

		}

	}

	// 定义观察者
	public interface DownloadObserver {
		public void onDownloadStateChanged(DownloadInfo downloadInfo);

		public void onDownloadProgressChanged(DownloadInfo downloadInfo);
	}

	// 保存观察者
	private ArrayList<DownloadObserver> observers = new ArrayList<DownloadObserver>();

	public void addDownloadObserver(DownloadObserver observer) {
		// this.observer = observer;
		if (!observers.contains(observer)) {
			observers.add(observer);
		}
	}

	// 通知观察者
	private void notifyDownloadStateChanged(DownloadInfo downloadInfo) {
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).onDownloadStateChanged(downloadInfo);
		}
	}

	private void notifyDownloadProgressChanged(DownloadInfo downloadInfo) {
		for (int i = 0; i < observers.size(); i++) {
			observers.get(i).onDownloadProgressChanged(downloadInfo);
		}
	}

	public void installApk(AppInfo appInfo) {
		DownloadInfo downloadInfo = mSavedDownloadInfo.get(appInfo.id);
		if (downloadInfo != null) {
			Intent intent = new Intent(Intent.ACTION_VIEW);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			intent.setDataAndType(
					Uri.fromFile(new File(downloadInfo.filePath)),
					"application/vnd.android.package-archive");
			UiUtils.getContext().startActivity(intent);
		}

	}

	public void pauseDownload(AppInfo appInfo) {
		DownloadInfo downloadInfo = mSavedDownloadInfo.get(appInfo.id);
		if (downloadInfo != null) {
			downloadInfo.currentState = MyDownloadManager.STATE_PAUSED;
			notifyDownloadStateChanged(downloadInfo);
		}

		// 需要考虑还没有被线程执行的任务
		DownloadTask task = mSavedDownloadTask.get(appInfo.id);
		MyThreadPoolManager.getInstance().cancel(task);
	}
}
