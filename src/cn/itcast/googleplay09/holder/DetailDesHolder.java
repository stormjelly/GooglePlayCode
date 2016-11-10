package cn.itcast.googleplay09.holder;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.Animator.AnimatorListener;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class DetailDesHolder extends BaseHolder<AppInfo> implements OnClickListener {

	private TextView des;
	private TextView appauthor;
	private ImageView arrow;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.layout_detail_des);
		des = (TextView) view.findViewById(R.id.des);
		appauthor = (TextView) view.findViewById(R.id.appauthor);
		
		arrow = (ImageView) view.findViewById(R.id.arrow);
		arrow.setOnClickListener(this);
		
		return view;
	}

	@Override
	public void refreshView() {
		des.setText(mData.des);
		appauthor.setText(mData.author);
		
		LayoutParams layoutParams = des.getLayoutParams();
		layoutParams.height = getInitHeight();
		des.setLayoutParams(layoutParams);
	}

	
	private boolean isOpen = false;
	
	@Override
	public void onClick(View v) {
		if(isOpen) {
			close();
			isOpen = false;
		} else {
			open();
			isOpen = true;
		}
	}

	private void open() {
		ValueAnimator animator = ValueAnimator.ofInt(getInitHeight(),getMaxHeight());
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				int tempHeight = (Integer) arg0.getAnimatedValue();
				LayoutParams layoutParams = des.getLayoutParams();
				layoutParams.height = tempHeight;
				des.setLayoutParams(layoutParams);
			}
		});
		//动画状态发生改变的时候，会进行的回调
		animator.addListener(new AnimatorListener() {
			
			@Override
			public void onAnimationStart(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationRepeat(Animator arg0) {
				
			}
			
			@Override
			public void onAnimationEnd(Animator arg0) {
				//让ScrollView自动滚动到底部
				/*if(sc != null) {
					sc.fullScroll(ScrollView.FOCUS_DOWN);
				}*/
				ScrollView scrollView = getScrollView(des);
				if(scrollView != null) {
					scrollView.fullScroll(ScrollView.FOCUS_DOWN);
				}
			}
			
			@Override
			public void onAnimationCancel(Animator arg0) {
				
			}
		});
		animator.setDuration(1000);
		animator.start();
	}

	private void close() {
		ValueAnimator animator = ValueAnimator.ofInt(getMaxHeight(),getInitHeight());
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator arg0) {
				int tempHeight = (Integer) arg0.getAnimatedValue();
				LayoutParams layoutParams = des.getLayoutParams();
				layoutParams.height = tempHeight;
				des.setLayoutParams(layoutParams);
			}
		});
		animator.setDuration(1000);
		animator.start();
	}
	
	private int getInitHeight() {
		//获取des 7行时候的高度
		//des.setMaxLines(7);
		//创建一个和des完全一样的TextView，然后给这个TextView设置最大的行数为7
		TextView tempTextView = new TextView(UiUtils.getContext());
		tempTextView.setText(mData.des);
		tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		tempTextView.getPaint().setFakeBoldText(true);//设置粗体
		tempTextView.setMaxLines(7);
		int maxWidth = UiUtils.getScreenWidth() - UiUtils.dip2px(10)*2;
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
		tempTextView.measure(widthMeasureSpec, 0);
		return tempTextView.getMeasuredHeight();
	}
	
	private int getMaxHeight() {
		TextView tempTextView = new TextView(UiUtils.getContext());
		tempTextView.setText(mData.des);
		tempTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP,14);
		tempTextView.getPaint().setFakeBoldText(true);//设置粗体
		//tempTextView.setMaxLines(7);
		int maxWidth = UiUtils.getScreenWidth() - UiUtils.dip2px(10)*2;
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
		tempTextView.measure(widthMeasureSpec, 0);
		return tempTextView.getMeasuredHeight();
	}
	
	private ScrollView sc;
	public void setScrollView(ScrollView sc) {
		this.sc = sc;
	}

	//使用此方法，必须得保证视图树中至少得有一个ScrollView，否则会直接栈溢出
	private ScrollView getScrollView(View v) {
		ViewParent parent = v.getParent();
		if(parent instanceof ScrollView) {
			return (ScrollView) parent;
		} else {
			return getScrollView((View) parent);
		}
	}
}
