package cn.itcast.googleplay09.holder;

import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.animation.ValueAnimator;
import com.nineoldandroids.animation.ValueAnimator.AnimatorUpdateListener;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.bean.AppInfo;
import cn.itcast.googleplay09.bean.SafeInfo;
import cn.itcast.googleplay09.http.HttpHelper;
import cn.itcast.googleplay09.manager.MyBitmapManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;

public class DetailSafeHolder extends BaseHolder<AppInfo> implements
		OnClickListener {

	private ImageView[] images;
	private ImageView arrow;
	private ImageView[] images2s;
	private TextView[] text2s;
	private LinearLayout[] lls;
	private LinearLayout ll_footer;

	@Override
	public View initView() {
		View view = UiUtils.inflateView(R.layout.layout_detail_safe);
		images = new ImageView[4];
		images[0] = (ImageView) view.findViewById(R.id.imagesafe1);
		images[1] = (ImageView) view.findViewById(R.id.imagesafe2);
		images[2] = (ImageView) view.findViewById(R.id.imagesafe3);
		images[3] = (ImageView) view.findViewById(R.id.imagesafe4);

		arrow = (ImageView) view.findViewById(R.id.arrow);

		images2s = new ImageView[4];
		text2s = new TextView[4];

		images2s[0] = (ImageView) view.findViewById(R.id.ll1_image1);
		images2s[1] = (ImageView) view.findViewById(R.id.ll2_image2);
		images2s[2] = (ImageView) view.findViewById(R.id.ll3_image3);
		images2s[3] = (ImageView) view.findViewById(R.id.ll4_image4);

		text2s[0] = (TextView) view.findViewById(R.id.ll1_text1);
		text2s[1] = (TextView) view.findViewById(R.id.ll2_text2);
		text2s[2] = (TextView) view.findViewById(R.id.ll3_text3);
		text2s[3] = (TextView) view.findViewById(R.id.ll4_text4);

		lls = new LinearLayout[4];
		lls[0] = (LinearLayout) view.findViewById(R.id.ll1);
		lls[1] = (LinearLayout) view.findViewById(R.id.ll2);
		lls[2] = (LinearLayout) view.findViewById(R.id.ll3);
		lls[3] = (LinearLayout) view.findViewById(R.id.ll4);

		ll_footer = (LinearLayout) view.findViewById(R.id.ll_footer);

		arrow.setOnClickListener(this);

		// ll_footer.setVisibility(View.GONE);
		LayoutParams layoutParams = ll_footer.getLayoutParams();
		layoutParams.height = 0;
		ll_footer.setLayoutParams(layoutParams);
		return view;
	}

	@Override
	public void refreshView() {

		for (int i = 0; i < mData.safe.size(); i++) {
			SafeInfo safeInfo = mData.safe.get(i);
			MyBitmapManager
					.getInstance()
					.getBitmapUtils()
					.display(images[i],
							HttpHelper.URL + "image?name=" + safeInfo.safeUrl);
			MyBitmapManager
					.getInstance()
					.getBitmapUtils()
					.display(
							images2s[i],
							HttpHelper.URL + "image?name="
									+ safeInfo.safeDesUrl);
			text2s[i].setText(safeInfo.safeDes + safeInfo.safeDes);
			lls[i].setVisibility(View.VISIBLE);
		}

	}

	private boolean isOpen = false;

	@Override
	public void onClick(View v) {
		if (isOpen) {
			close();
			isOpen = false;
		} else {
			open();
			isOpen = true;
		}

	}

	// 10 20 30....30
	private void open() {

		/*new Thread(new Runnable() {

			@Override
			public void run() {
				for (int i = 0; i < 300; i++) {
					final int temp = i;
					UiUtils.runOnUiThread(new Runnable() {

						@Override
						public void run() {
							LayoutParams layoutParams = ll_footer
									.getLayoutParams();
							layoutParams.height = temp;
							ll_footer.setLayoutParams(layoutParams);
						}
					});

					try {
						Thread.sleep(10);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
		}).start();*/
		
		//ValueAnimator他其实是一个值的产生器，它能够产生一系列的中间值
		ValueAnimator animator = ValueAnimator.ofInt(0,getMaxHeight());
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int tempHeight = (Integer) animation.getAnimatedValue();
				System.out.println("tempHeight=" + tempHeight);
				LayoutParams layoutParams = ll_footer.getLayoutParams();
				layoutParams.height = tempHeight;
				ll_footer.setLayoutParams(layoutParams);
			}
		});
		animator.setDuration(1000);
		animator.start();
		/**
		 * target：产生中间值的时候，需要作用的对象
		 * property:当中间值产生的时候，所会作用到的属性
		 * this.setHaha(int temp)
		 */
		ObjectAnimator obj = ObjectAnimator.ofFloat(ll_footer, "alpha", 0,1);
		obj.setDuration(1000);
		obj.start();

		// ll_footer.setVisibility(View.VISIBLE);
		//ll_footer.setAlpha(alpha)
		//ll_footer.setTranslationX(translationX)
	}
	
	public void setHaha(int temp) {
		
	}

	public void setHaha(float temp) {
		LayoutParams layoutParams = ll_footer.getLayoutParams();
		layoutParams.height = (int) temp;
		ll_footer.setLayoutParams(layoutParams);
	}
	
	//V4--Fragment
	//V7--ActionBar
	//属性动画兼容---nineoldandroid.jar
	 private void close() {
		// ll_footer.setVisibility(View.GONE);
		/*LayoutParams layoutParams = ll_footer.getLayoutParams();
		layoutParams.height = 0;
		ll_footer.setLayoutParams(layoutParams);*/
		ValueAnimator animator = ValueAnimator.ofInt(getMaxHeight(),0);
		animator.addUpdateListener(new AnimatorUpdateListener() {
			
			@Override
			public void onAnimationUpdate(ValueAnimator animation) {
				int tempHeight = (Integer) animation.getAnimatedValue();
				System.out.println("tempHeight=" + tempHeight);
				LayoutParams layoutParams = ll_footer.getLayoutParams();
				layoutParams.height = tempHeight;
				ll_footer.setLayoutParams(layoutParams);
			}
		});
		animator.setDuration(1000);
		animator.start();
	}
	
	
	private int getMaxHeight() {
		//获取ll_footer展开时候的高度信息
		//ll_footer.getHeight();
		int maxWidth = UiUtils.getScreenWidth() - UiUtils.dip2px(5);
		int widhtMeasureSpec = MeasureSpec.makeMeasureSpec(maxWidth, MeasureSpec.AT_MOST);
		ll_footer.measure(widhtMeasureSpec, 0);
		return ll_footer.getMeasuredHeight();
	}

}
