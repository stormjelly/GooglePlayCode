package cn.itcast.googleplay09.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;

import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.bean.HomeData;

public class HomeProtocol extends BaseProtocol<ArrayList<AppInfo>> {

	private ArrayList<AppInfo> retDatas = new ArrayList<AppInfo>();
	private ArrayList<String> picList = new ArrayList<String>();

	
	private int index;
	
	public void setIndex(int index) {
		this.index = index;
	}
	
	@Override
	public String getKey() {
		return "home";
	}

	@Override
	public String getParams() {
		return "?index=" + index;
	}
	
	
	public ArrayList<String> getPicList() {
		return picList;
	}
	
	/**
	 * 1、Gson、fastJson
	 * 		定义javabean
	 * 			{}---类
	 * 			[]---ArrayList
	 * 			什么符号都没有 --- 基本数据类型
	 * 
	 * 2、使用JSONObject
	 * 			{}---JSONObject
	 * 			[]---JSONArray
	 * 			什么符号都没有 --- 基本数据类型
	 */
	@Override
	public ArrayList<AppInfo> parseJson(String json) {
		
		/*Gson gson = new Gson();
		HomeData homeData = gson.fromJson(json, HomeData.class);
		return homeData.list;*/
		
		try {
			JSONObject jo = new JSONObject(json);
			JSONArray ja = jo.getJSONArray("list");
			for(int i=0;i<ja.length();i++) {
				JSONObject jo2 = ja.getJSONObject(i);
				AppInfo info = new AppInfo();
				info.des = jo2.getString("des");
				info.downloadUrl = jo2.getString("downloadUrl");
				info.iconUrl = jo2.getString("iconUrl");
				info.id = jo2.getString("id");
				info.name = jo2.getString("name");
				info.packageName = jo2.getString("packageName");
				info.size = jo2.getString("size");
				info.stars = jo2.getString("stars");
				retDatas.add(info);
			}
			JSONArray ja2 = jo.getJSONArray("picture");
			for(int i=0;i<ja2.length();i++) {
				String picUrl = ja2.getString(i);
				picList.add(picUrl);
			}
		}catch(Exception e) {
			retDatas = null;//在出现错误的时候，需要把retDatas变为null，这样才能让Fragment显示正常的布局情况
		}
		
		return retDatas;
	}

}
