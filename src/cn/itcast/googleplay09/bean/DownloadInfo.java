package cn.itcast.googleplay09.bean;

import java.io.File;
import java.util.ArrayList;

import android.os.Environment;

import cn.itcast.googleplay09.manager.MyDownloadManager;

public class DownloadInfo {
	
	public String downloadUrl;// app/com.youyuan.yyhl/com.youyuan.yyhl.apk,
	public String id;// 1525490,
	public String name;// 有缘网,
	public String packageName;// com.youyuan.yyhl,
	public String size;// 3876203,
	//下载路径，下载进度，下载状态
	public String filePath;//   /sdcard/GooglePlay09/xxx.apk
	public long currentPosition;//当前下载的位置
	public int currentState;//下载状态
	
	public static String getFilePath(String haha) {
		// /sdcard/GooglePlay09/xxx.apk
		String dir = Environment.getExternalStorageDirectory() + "/GooglePlay09/";
		if(createDir(dir)) {
			return dir + haha + ".apk";
		}
		
		return null;
		
	}
	
	public static boolean createDir(String dir) {
		File fileDir = new File(dir);
		if(fileDir.exists() && fileDir.isDirectory()) {
			return true;
		} else {
			return fileDir.mkdirs();
		}
	}
	
	
	public static DownloadInfo createDownloadInfoFromAppInfo(AppInfo appInfo) {
		DownloadInfo downloadInfo = new DownloadInfo();
		downloadInfo.downloadUrl = appInfo.downloadUrl;
		downloadInfo.id = appInfo.id;
		downloadInfo.name = appInfo.name;
		downloadInfo.size = appInfo.size;
		downloadInfo.packageName = appInfo.packageName;
		downloadInfo.currentPosition = 0;
		downloadInfo.currentState = MyDownloadManager.STATE_NONE;
		downloadInfo.filePath = getFilePath(appInfo.name);
		return downloadInfo;
	}
	


}
