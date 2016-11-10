package cn.itcast.googleplay09.http.protocol;

import java.util.ArrayList;

import org.json.JSONArray;

public class HotProtocol extends BaseProtocol<ArrayList<String>> {

	private ArrayList<String> data = new ArrayList<String>();

	@Override
	public String getKey() {
		return "hot";
	}

	@Override
	public String getParams() {
		return "";
	}

	@Override
	public ArrayList<String> parseJson(String json) {
		try {
			JSONArray ja = new JSONArray(json);
			for (int i = 0; i < ja.length(); i++) {
				String keyword = ja.getString(i);
				data.add(keyword);
			}
		} catch (Exception e) {
			data = null;
		}

		return data;
	}

}
