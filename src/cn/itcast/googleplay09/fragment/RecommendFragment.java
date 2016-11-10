package cn.itcast.googleplay09.fragment;

import java.util.ArrayList;
import java.util.Random;

import cn.itcast.googleplay09.http.protocol.RecommendProtocol;
import cn.itcast.googleplay09.randomLayout.ShakeListener;
import cn.itcast.googleplay09.randomLayout.ShakeListener.OnShakeListener;
import cn.itcast.googleplay09.randomLayout.StellarMap;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import cn.itcast.googleplay09.ui.widget.LoadingPage;
import cn.itcast.googleplay09.ui.widget.LoadingPage.ResultState;
import android.graphics.Color;
import android.util.TypedValue;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import android.widget.Toast;

/**
 * 推荐的Fragment
 * @author zhengping
 *
 */
public class RecommendFragment extends BaseFragment implements OnShakeListener {

	private ArrayList<String> data;

	@Override
	public View fragmentCreateSuccessView() {
		mapView = new StellarMap(UiUtils.getContext());
		int padding = UiUtils.dip2px(5);
		mapView.setInnerPadding(padding, padding, padding, padding);
		mapView.setRegularity(9, 9);//设置x、y的孩子view的分布规则
		myAdapter = new MyAdapter();
		mapView.setAdapter(myAdapter);
		mapView.setGroup(0, true);//默认显示第0组的数据
		
		
		ShakeListener shakeListener = new ShakeListener(UiUtils.getContext());
		shakeListener.setOnShakeListener(this);
		
		return mapView;
	}
	
	@Override
	public ResultState fragmentLoadData() {
		RecommendProtocol protocol = new RecommendProtocol();
		data = protocol.getData();
		return checkData(data);
		//return ResultState.EMPTY;
	}
	
	private int[] colors = new int[]{Color.RED,Color.BLACK,Color.BLUE};
	
	class MyAdapter implements StellarMap.Adapter {

		//获取一共有多少组
		@Override
		public int getGroupCount() {
			return 3;//一共有三组
		}

		//返回某一组中一共有多少个元素
		@Override
		public int getCount(int group) {
			int singleCount = data.size()/getGroupCount();
			//考虑除不尽的情况
			if(group == getGroupCount()-1) {
				singleCount = singleCount + data.size()% getGroupCount();
			}
			return singleCount;
		}

		//group:当前属于哪一组
		//position：属于当前组的哪一个位置
		//  2  2   --> 
		// 0   1  2 
		// 11  11 2  -->24
		@Override
		public View getView(int group, int position, View convertView) {
			if(convertView == null) {
				convertView = new TextView(UiUtils.getContext());
			}
			
			TextView tv = (TextView) convertView;
			
			int realPosition = 0;
			for(int i=0;i<group;i++) {
				realPosition = realPosition + getCount(i);
			}
			
			realPosition = realPosition + position;
			
			final int tempPosition = realPosition;
			
			//颜色随机的  90~220
			Random random = new Random();
			int red = 90 + random.nextInt(131);
			int green = 90 + random.nextInt(131);
			int blue = 90 + random.nextInt(131);
			int color = Color.rgb(red, green, blue);
			tv.setTextColor(color);
			//大小随机  16 ~ 25 sp
			int textSize = 16 + random.nextInt(10);
			
			tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
			
			tv.setText(data.get(realPosition));
			tv.setTag(data.get(realPosition));
			
			tv.setOnClickListener(listener);
			
			
			return tv;
		}

		//获取下一组的索引
		//group:当前组的索引
		//isZoomIn:缩小
		@Override
		public int getNextGroupOnZoom(int group, boolean isZoomIn) {
			int nextGroup = group;
			if(isZoomIn) {
				//获取下一组的索引
				nextGroup = group + 1;
				if(nextGroup > getGroupCount() - 1) {
					nextGroup = 0;
				}
			} else {
				//获取上一组的索引
				nextGroup = group - 1;
				if(nextGroup < 0) {
					nextGroup = getGroupCount() - 1;
				}
			}
			return nextGroup;
		}
		
	}
	
	private OnClickListener listener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			String msg = (String) v.getTag();
			Toast.makeText(UiUtils.getContext(), msg, Toast.LENGTH_SHORT).show();
			
		}
	};
	private StellarMap mapView;
	private MyAdapter myAdapter;

	@Override
	public void onShake() {
		int currentGroup = mapView.getCurrentGroup();
		int nextGroup = currentGroup + 1;
		if(nextGroup > myAdapter.getGroupCount() - 1) {
			nextGroup = 0;
		}
		mapView.setGroup(nextGroup, true);
	}

}
