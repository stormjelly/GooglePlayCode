package cn.itcast.googleplay09.http.protocol;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

import org.apache.http.message.BufferedHeader;

import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.http.HttpHelper.HttpResult;
import cn.itcast.googleplay09.ui.utils.MD5Encoder;
import cn.itcast.googleplay09.ui.utils.UiUtils;

/**
 * 网络访问的基类
 * 
 * @author zhengping
 *
 */
public abstract class BaseProtocol<T> {
	
	private String url;
	public T getData() {
		return getDataFromServer();
	}
	
	public T getDataFromServerByXutils() {
		return null;
	}

	private T getDataFromServer() {
		//URL:主域名+模块名称+参数
		url = HttpHelper.URL + getKey() + getParams();//  http://127.0.0.1:8090/home?index=0
		String cacheJson = getCache();
		if(cacheJson == null) {
			HttpResult result = HttpHelper.get(url);
			if(result != null) {
				String json = result.getString();
				setCache(json);
				return parseJson(json);
			}
		} else {
			return parseJson(cacheJson);
		}
		
		return null;
	}
	
	//读缓存
	private String getCache() {
		
		try {
			File cacheDir = UiUtils.getContext().getCacheDir();
			File cacheFile = new File(cacheDir + File.separator + MD5Encoder.encode(url));
			
			FileReader reader = new FileReader(cacheFile);
			BufferedReader bReader = new BufferedReader(reader);
			String strEmpireTime = bReader.readLine();
			long empireTime = Long.parseLong(strEmpireTime);
			if(empireTime > System.currentTimeMillis()) {
				//缓存有效
				String str = null;
				StringBuffer sb = new StringBuffer();
				while((str = bReader.readLine())!= null) {
					sb.append(str + "\n");
				}
				return sb.toString();
			} else  {
				//缓存无效
			}
			
			
		}catch(Exception e) {
			
		}
		
		return null;
	}
	
	//写缓存
	private void setCache(String json) {
		//存在哪？
		//sp  存储一些配置信息
		//sqlite  关系型数据库  杀鸡焉用牛刀
		//文件    sdcard     data/data/包名/cache/   应用的内部存储空间
		try {
			File cacheDir = UiUtils.getContext().getCacheDir();
			File cacheFile = new File(cacheDir + File.separator + MD5Encoder.encode(url));
			FileWriter writer = new FileWriter(cacheFile);
			//增加缓存的有效期
			String strEmpireTime = System.currentTimeMillis() + 5*1000*60 + "";
			writer.write(strEmpireTime + "\n");
			writer.write(json);
			writer.flush();
			writer.close();
			
		}catch(Exception e) {
			
		}
		
	}
	
	public abstract T parseJson(String json) ;

	public abstract String getKey();
	public abstract String getParams();


}
