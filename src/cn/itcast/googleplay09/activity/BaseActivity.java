package cn.itcast.googleplay09.activity;

import cn.itcast.googleplay09.R;
import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
/**
 * 这个项目中，所有Activity的基类
 * 做共有的事情
 * 
 * 为了让所有的Activity都拥有上下兼容的ActionBar效果
 * 1、让Activity继承ActionBarActivity
 * 2、如果要使用ActionBarActivity的话，就必须要设置AppCompat的主题
 * @author zhengping
 *
 */
public class BaseActivity extends ActionBarActivity {
	
	
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(R.anim.from_right_in, R.anim.to_left_out);
	}
	
	@Override
	public void finish() {
		super.finish();
		overridePendingTransition(R.anim.from_left_in, R.anim.to_right_out);
	}

}
