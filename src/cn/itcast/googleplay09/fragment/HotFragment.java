package cn.itcast.googleplay09.fragment;

import java.util.ArrayList;
import java.util.Random;

import cn.itcast.googleplay09.http.protocol.HotProtocol;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.FlowLayout;
import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import cn.itcast.googleplay09.ui.widget.MyFlowLayout;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.graphics.drawable.StateListDrawable;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 排行的Fragment
 * @author zhengping
 *
 */
public class HotFragment extends BaseFragment {

	private ArrayList<String> data;

	@Override
	public View fragmentCreateSuccessView() {
		
		ScrollView sc = new ScrollView(UiUtils.getContext());
		
		MyFlowLayout layout = new MyFlowLayout(UiUtils.getContext());
		int padding = UiUtils.dip2px(10);
		sc.setPadding(padding, padding, padding, padding);
		layout.setPadding(padding, padding, padding, padding);
		for(int i=0;i<data.size();i++) {
			TextView tv = new TextView(UiUtils.getContext());
			//背景颜色：随机
			Random random = new Random();
			int red = 90 + random.nextInt(131);
			int green = 90 + random.nextInt(131);
			int blue = 90 + random.nextInt(131);
			int backgroundColor = Color.rgb(red, green, blue);
			//tv.setBackgroundColor(backgroundColor);
			GradientDrawable normalDrawable = UiUtils.getGradientDrawable(UiUtils.dip2px(5), backgroundColor);
			GradientDrawable pressedDrawable = UiUtils.getGradientDrawable(UiUtils.dip2px(5), Color.rgb(200, 200, 200));
			
			StateListDrawable selector = UiUtils.getSelector(pressedDrawable, normalDrawable);
			
			tv.setBackgroundDrawable(selector);
			tv.setTextColor(Color.WHITE);
			tv.setPadding(padding, padding, padding, padding);
			if(i==10) {
				tv.setText(data.get(i) + "\n" + data.get(i));
			} else {
				tv.setText(data.get(i));
			}
			
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 16);
			tv.setGravity(Gravity.CENTER);
			layout.addView(tv);
			tv.setTag(data.get(i));
			tv.setOnClickListener(listener);
			
		}
		//ScrollView只能有一个孩子view对象
		sc.addView(layout);
		
		return sc;
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			//String text = (String) v.getTag();
			TextView tv = (TextView) v;
			Toast.makeText(UiUtils.getContext(), tv.getText(), Toast.LENGTH_SHORT).show();
		}
	};

	@Override
	public ResultState fragmentLoadData() {
		HotProtocol protocol = new HotProtocol();
		data = protocol.getData();
		return checkData(data);
	}
}
