package cn.itcast.googleplay09.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;

public class RecommendProtocol extends BaseProtocol<ArrayList<String>> {

	private ArrayList<String> datas = new ArrayList<String>();
	
	@Override
	public String getKey() {
		return "recommend";
	}

	@Override
	public String getParams() {
		return "";//如果没有参数的话，不能return null应该return一个空串
	}
	
	@Override
	public ArrayList<String> parseJson(String json) {
		try {
			JSONArray ja = new JSONArray(json);
			for(int i=0;i<ja.length();i++) {
				String keyword = ja.getString(i);
				datas.add(keyword);
			}
		}catch(Exception e) {
			datas = null;
		}
		
		return datas;
	}


}
