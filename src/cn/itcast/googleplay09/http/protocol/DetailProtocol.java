package cn.itcast.googleplay09.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.bean.SafeInfo;

public class DetailProtocol extends BaseProtocol<AppInfo> {

	private String packageName;

	public void setPackageName(String packageName) {
		this.packageName = packageName;
	}

	@Override
	public String getKey() {
		return "detail";
	}

	@Override
	public String getParams() {
		return "?packageName=" + packageName;
	}

	@Override
	public AppInfo parseJson(String json) {

		try {
			JSONObject jo = new JSONObject(json);
			AppInfo appInfo = new AppInfo();
			appInfo.author = jo.getString("author");
			appInfo.date = jo.getString("date");
			appInfo.des = jo.getString("des");
			appInfo.downloadNum = jo.getString("downloadNum");
			appInfo.downloadUrl = jo.getString("downloadUrl");
			appInfo.iconUrl = jo.getString("iconUrl");
			appInfo.id = jo.getString("id");
			appInfo.name = jo.getString("name");
			appInfo.packageName = jo.getString("packageName");
			appInfo.size = jo.getString("size");
			appInfo.stars = jo.getString("stars");
			appInfo.version = jo.getString("version");

			JSONArray ja = jo.getJSONArray("safe");
			ArrayList<SafeInfo> safeList = new ArrayList<SafeInfo>();
			for (int i = 0; i < ja.length(); i++) {
				JSONObject jsonObject = ja.getJSONObject(i);
				SafeInfo safeInfo = new SafeInfo();
				safeInfo.safeDes = jsonObject.getString("safeDes");
				safeInfo.safeDesColor = jsonObject.getString("safeDesColor");
				safeInfo.safeDesUrl = jsonObject.getString("safeDesUrl");
				safeInfo.safeUrl = jsonObject.getString("safeUrl");
				safeList.add(safeInfo);
			}

			appInfo.safe = safeList;

			JSONArray ja2 = jo.getJSONArray("screen");
			ArrayList<String> screenList = new ArrayList<String>();
			for (int i = 0; i < ja2.length(); i++) {
				String screenPic = ja2.getString(i);
				screenList.add(screenPic);
			}
			appInfo.screen = screenList;
			return appInfo;
		} catch (Exception e) {
			return null;
		}

	}
}
