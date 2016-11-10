package cn.itcast.googleplay09.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import cn.itcast.googleplay09.bean.SubjectInfo;

public class SubjectProtocol extends BaseProtocol<ArrayList<SubjectInfo>> {
	
	private ArrayList<SubjectInfo> datas = new ArrayList<SubjectInfo>();

	private int index;
	
	public void setIndex(int index) {
		this.index = index;
	}

	@Override
	public String getKey() {
		return "subject";
	}

	@Override
	public String getParams() {
		return "?index=" + index;
	}
	
	@Override
	public ArrayList<SubjectInfo> parseJson(String json) {
		try {
			JSONArray ja = new JSONArray(json);
			for(int i=0;i<ja.length();i++) {
				JSONObject jo = ja.getJSONObject(i);
				SubjectInfo info = new SubjectInfo();
				info.des = jo.getString("des");
				info.url = jo.getString("url");
				datas.add(info);
				
			}
		}catch(Exception e) {
			datas = null;
		}
		
		return datas;
	}

}
