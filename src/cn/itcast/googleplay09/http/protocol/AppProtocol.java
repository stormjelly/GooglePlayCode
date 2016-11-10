package cn.itcast.googleplay09.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import cn.itcast.googleplay09.bean.AppData;
import cn.itcast.googleplay09.bean.AppInfo;

public class AppProtocol extends BaseProtocol<ArrayList<AppInfo>> {
	
	private ArrayList<AppInfo> datas = new ArrayList<AppInfo>();

	private int index;
	
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String getKey() {
		return "app";
	}

	@Override
	public String getParams() {
		return "?index=" + index;
	}
	
	@Override
	public ArrayList<AppInfo> parseJson(String json) {
		
		Gson gson = new Gson();
		//TypeToken包装了一种数据类型，通过getType的方式可以获取
		TypeToken<ArrayList<AppInfo>> typeToken = new TypeToken<ArrayList<AppInfo>>(){};
		ArrayList<AppInfo> data = gson.fromJson(json, typeToken.getType());
		return data;
		
		/*try {
			JSONArray ja = new JSONArray(json);
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
				datas.add(info);
			}
		}catch(Exception e) {
			datas = null;
		}
		
		return datas;*/
	}

}
