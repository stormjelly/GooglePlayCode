package cn.itcast.googleplay09.ui.widget;

import java.util.ArrayList;

import cn.itcast.googleplay09.ui.utils.UiUtils;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 * 1、确定哪些元素属于第一行，哪些元素属于第二行，把所有行对象存储在lineList里面 
 * 2、确定MyFlowLayout的高度，每一行的高度进行累加
 * 3、确定每一个View的位置摆放
 * 4、将每一行的剩余空间平均分配给这行中的所有元素
 * 5、细节性考虑：
 * 	5.1.onMeasure调用多次
 * 	5.2.最后一行别忘了
 * 	5.3.padding
 * 	5.4.当一行中的元素高度不一致的情况之下，这一行的高度以最高的元素为准，其他的元素居中显示
 * 
 * @author zhengping
 * 
 */
public class MyFlowLayout extends ViewGroup {

	public MyFlowLayout(Context context) {
		super(context);
	}

	public MyFlowLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public MyFlowLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	protected void onLayout(boolean changed, int l, int t, int r, int b) {

		int left = getPaddingLeft();
		int top = getPaddingTop();
		for(int i=0;i<lineList.size();i++) {
			Line line = lineList.get(i);
			line.reMeasure();
			line.layoutView(left, top);
			top = top + line.mMaxHeight + mVerticalSpacing;
		}
		
	}

	private int mUsedWidth = 0;
	private Line mCurrentLine;// 当前行
	private int mHorizontalSpacing = UiUtils.dip2px(10);
	private int mVerticalSpacing = UiUtils.dip2px(10);
	
	private void reset() {
		mCurrentLine = new Line();
		lineList.clear();
		mUsedWidth = 0;
	}

	//onMeasure可能会调用多次
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		reset();
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);// 获取MyFlowLayout的宽度的大小
		widthSize = widthSize - getPaddingLeft() - getPaddingRight();
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);// 获取MyFlowLayout宽度的模式

		int heightSize = MeasureSpec.getSize(heightMeasureSpec);
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);

		mCurrentLine = new Line();
		int childCount = getChildCount();
		for (int i = 0; i < childCount; i++) {
			View childView = getChildAt(i);
			int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize,
					MeasureSpec.AT_MOST);// 至多模式，最多不能超过父控件的宽度
			childView.measure(childWidthMeasureSpec, 0);
			// childView.measure(0, 0);//手动测量，将测量的工作交给系统来完成，代码不参与任何的限制
			int childWidth = childView.getMeasuredWidth();// 获取孩子的测量之后宽度大小
			mUsedWidth = mUsedWidth + childWidth;
			if (mUsedWidth < widthSize) {
				// 有剩余空间，把这个元素添加到当前这一行
				mCurrentLine.addLineView(childView);
				mUsedWidth = mUsedWidth + mHorizontalSpacing;
			} else {
				// 没有剩余空间，把这个元素放到新起的一行中
				newLine();
				mCurrentLine.addLineView(childView);
				mUsedWidth = mUsedWidth + childWidth + mHorizontalSpacing;
			}
		}
		
		//最后一行肯定不会调用newLine的方法，所以会遗漏最后一行
		if(!lineList.contains(mCurrentLine)) {
			lineList.add(mCurrentLine);
		}
		

		// 计算MyFlowLayout的高度
		System.out.println("lineList.size=" + lineList.size());
		int totalHeight = 0;
		for (int i = 0; i < lineList.size(); i++) {
			Line line = lineList.get(i);
			totalHeight = totalHeight + line.mMaxHeight + mVerticalSpacing;
		}

		totalHeight = totalHeight - mVerticalSpacing;// 减去最后一个竖直间距
		
		totalHeight = totalHeight  + getPaddingTop() + getPaddingBottom();
		// 重新生成heightMeasureSpec
		heightMeasureSpec = MeasureSpec.makeMeasureSpec(totalHeight,
				MeasureSpec.EXACTLY);

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	private void newLine() {
		lineList.add(mCurrentLine);
		mCurrentLine = new Line();
		mUsedWidth = 0;
	}

	private ArrayList<Line> lineList = new ArrayList<Line>();

	class Line {
		private ArrayList<View> lineViews = new ArrayList<View>();

		public int mMaxHeight;

		public void addLineView(View childView) {
			if (!lineViews.contains(childView)) {
				lineViews.add(childView);
				if (mMaxHeight < childView.getMeasuredHeight()) {
					mMaxHeight = childView.getMeasuredHeight();
				}
			}
		}

		public void layoutView(int left, int top) {
			for (int i = 0; i < lineViews.size(); i++) {
				View childView = lineViews.get(i);
				int topOffset = mMaxHeight/2 - childView.getMeasuredHeight()/2;
				childView.layout(left, top+topOffset,
						left + childView.getMeasuredWidth(),
						top +topOffset+ childView.getMeasuredHeight());// 确定一行中每一个元素的位置信息
				left = left + childView.getMeasuredWidth() + mHorizontalSpacing;
			}
		}
		
		public void reMeasure() {
			//1、获取剩余空间
			int lineWidth = 0;
			for (int i = 0; i < lineViews.size(); i++) {
				View childView = lineViews.get(i);
				int childWidth = childView.getMeasuredWidth();
				int childHeight = childView.getMeasuredHeight();
				lineWidth = lineWidth + childWidth + mHorizontalSpacing;
				
			}
			lineWidth = lineWidth - mHorizontalSpacing;
			
			int surplusSpacing = getMeasuredWidth()-getPaddingLeft() - getPaddingRight() - lineWidth;
			if(surplusSpacing > 0) {
				//有剩余空间
				//2、平均分配
				int splitSpacing = surplusSpacing/lineViews.size();
				
				for (int i = 0; i < lineViews.size(); i++) {
					View childView = lineViews.get(i);
					int childWidth = childView.getMeasuredWidth();
					int childHeight = childView.getMeasuredHeight();
					
					childWidth = childWidth + splitSpacing;
					
					int childWidthMeasureSpec = MeasureSpec.makeMeasureSpec(childWidth, MeasureSpec.EXACTLY);
					int childHeightMeasureSpec = MeasureSpec.makeMeasureSpec(childHeight, MeasureSpec.EXACTLY);
					//重新的测量
					childView.measure(childWidthMeasureSpec, childHeightMeasureSpec);
					
				}
				
			}
			
			
		}
	}

}
