package cn.itcast.googleplay09.ui.widget;

import cn.itcast.googleplay09.R;
import cn.itcast.googleplay09.manager.MyThreadPoolManager;
import cn.itcast.googleplay09.ui.utils.UiUtils;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
/**
 * 作为每一个Fragment需要展示的View对象
 * 这个View对象包含了好几种状态
 * 
 * 1、STATE_LOADING--->mLoadingView
 * 2、STATE_ERROR -->mErrorView
 * 3、STATE_EMPTY -->mEmptyView
 * 4、STATE_SUCCESS -->mSuccessView
 * @author zhengping
 *
 */

public abstract class LoadingPage extends FrameLayout {
	
	public static final int STATE_NONE = 0;
	public static final int STATE_LOADING = 1;
	public static final int STATE_ERROR = 2;
	public static final int STATE_EMPTY = 3;
	public static final int STATE_SUCCESS = 4;
	
	private  int mCurrentState = STATE_NONE;//定义当前的状态
	
	private View mLoadingView;
	private View mErrorView;
	private View mEmptyView;
	private View mSuccessView;

	public LoadingPage(Context context) {
		super(context);
		initView();
	}

	public LoadingPage(Context context, AttributeSet attrs) {
		super(context, attrs);
		initView();
	}

	public LoadingPage(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		initView();
	}
	
	private void initView() {
		if(mLoadingView == null) {
			mLoadingView = createLoadingView();
			this.addView(mLoadingView);
		}
		
		if(mErrorView == null) {
			mErrorView = createErrorView();
			this.addView(mErrorView);
		}
		if(mEmptyView == null) {
			mEmptyView = createEmptyView();
			this.addView(mEmptyView);
		}
		
		showRightPage() ;
	}
	
	//加载网络数据
	public void loadData() {
		if(mCurrentState == STATE_LOADING) {
			return;
		}
		mCurrentState = STATE_LOADING;
		showRightPage();//mCurrentState的值改变了之后，需要动态更新View的显示
		
		//启动子线程去加载网络数据
		MyThreadPoolManager.getInstance().execute(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(2000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				//mCurrentState = onLoad();
				ResultState result = onLoad();
				if(result != null) {
					mCurrentState = result.state;
				}
				UiUtils.runOnUiThread(new Runnable() {
					
					@Override
					public void run() {
						showRightPage();
					}
				});
				
			}
		});
		//new Thread().start();
		
	}
	
	

	//根据当前的状态对View的可见度进行控制
	private void showRightPage() {
		/*if(mCurrentState == STATE_LOADING) {
			mLoadingView.setVisibility(View.VISIBLE);
			mErrorView.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.GONE);
		} else if (mCurrentState == STATE_ERROR) {
			mLoadingView.setVisibility(View.GONE);
			mErrorView.setVisibility(View.VISIBLE);
			mEmptyView.setVisibility(View.GONE);
		} else if (mCurrentState == STATE_EMPTY) {
			mLoadingView.setVisibility(View.GONE);
			mErrorView.setVisibility(View.GONE);
			mEmptyView.setVisibility(View.VISIBLE);
		}*/
		//三元表达式
		mLoadingView.setVisibility(mCurrentState == STATE_LOADING ? View.VISIBLE:View.GONE);
		mErrorView.setVisibility(mCurrentState == STATE_ERROR ? View.VISIBLE:View.GONE);
		mEmptyView.setVisibility(mCurrentState == STATE_EMPTY ? View.VISIBLE:View.GONE);
		
		
		if(mCurrentState == STATE_SUCCESS) {
			if(mSuccessView == null) {
				mSuccessView = createSuccessView();
				if(mSuccessView != null) {
					addView(mSuccessView);
				}
			}
		}
		if(mSuccessView != null) {
			mSuccessView.setVisibility(mCurrentState == STATE_SUCCESS ? View.VISIBLE:View.GONE);
		}
		
	}
	//由于LoadingPage不清楚每一个子类中成功之后的布局究竟长什么样子，所以LoadingPage无法完成mSuccessView的创建这一部分工作
	//所以应该把这项工作转交出去，转交给子类来做
	public abstract View createSuccessView() ;
	
	//由于LoadingPage无法确定每一个模块中网络数据访问的URL是什么，所以LoadingPage无法完成这一项工作，需要转交出去
	public abstract ResultState onLoad() ;

	private View createEmptyView() {
		View view = UiUtils.inflateView(R.layout.layout_empty);
		return view;
	}

	private View createErrorView() {
		View view = UiUtils.inflateView(R.layout.layout_error);
		Button btnRetry = (Button) view.findViewById(R.id.btnRetry);
		btnRetry.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//加载网络数据
				loadData();
			}
		});
		return view;
	}

	private View createLoadingView() {
		View view = UiUtils.inflateView(R.layout.layout_loading);
		return view;
	}
	
	//需要使用枚举将返回值的类型的值控制在一定的范围之类
	/**
	 * 1、关键字不同
	 * 2、创建对象的写法简化
	 * 3、构造方法私有
	 * 		构造方法一旦私有，在外部就不可能创建对象
	 * 		通过构造方法的私有化，将对象的数量进行控制。
	 * 		一般情况下，我们会使用枚举来表示一些状态，显得更加专业
	 * @author zhengping
	 *
	 */
	public enum ResultState {
		
		LOADING(STATE_LOADING),ERROR(STATE_ERROR),EMPTY(STATE_EMPTY),SUCCESS(STATE_SUCCESS);
		
		public int state;
		
		private ResultState(int state) {
			this.state = state;
		}
		
	}

}
