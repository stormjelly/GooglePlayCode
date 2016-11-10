package cn.itcast.googleplay09.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.itcast.googleplay09.bean.CategoryInfo;

public class CategoryProtocol extends BaseProtocol<ArrayList<CategoryInfo>> {

	private ArrayList<CategoryInfo> datas = new ArrayList<CategoryInfo>();

	@Override
	public String getKey() {
		return "category";
	}

	@Override
	public String getParams() {
		return "";
	}
	
	@Override
	public ArrayList<CategoryInfo> parseJson(String json) {
		try {
			JSONArray ja = new JSONArray(json);
			for(int i=0;i<ja.length();i++) {
				JSONObject jo = ja.getJSONObject(i);
				String title = jo.getString("title");
				CategoryInfo titleInfo = new CategoryInfo();
				titleInfo.title = title;
				titleInfo.isTitle = true;
				datas.add(titleInfo);
				JSONArray ja2 = jo.getJSONArray("infos");
				for(int j=0;j<ja2.length();j++) {
					JSONObject jo2 = ja2.getJSONObject(j);
					CategoryInfo info = new CategoryInfo();
					info.name1 = jo2.getString("name1");
					info.name2 = jo2.getString("name2");
					info.name3 = jo2.getString("name3");
					info.url1 = jo2.getString("url1");
					info.url2 = jo2.getString("url2");
					info.url3 = jo2.getString("url3");
					datas.add(info);
				}
				
				
				
			}
		}catch(Exception e) {
			datas = null;
		}
		
		return datas;
	}

}
