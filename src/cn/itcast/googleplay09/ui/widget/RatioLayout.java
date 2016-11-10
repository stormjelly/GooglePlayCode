package cn.itcast.googleplay09.ui.widget;

import cn.itcast.googleplay09.R;
import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.FrameLayout;
/**
 * 
 * 动态计算高度，保证长宽的比例不变
 * 
 * 自定义属性的步骤
 * 1、给自定义属性起名字
 * 		自定义属性集合的名字   自定义属性自己的名字(数据格式)
 * 2、使用自定义属性
 * 		命名空间的理解
 * 3、获取自定义属性的值
 * 	3.1.
 * 	3.2.
 * @author zhengping
 *
 */
public class RatioLayout extends FrameLayout {
	
	public static final String NS = "http://schemas.android.com/apk/res/cn.itcast.googleplay09";
	private float ratio2;

	public RatioLayout(Context context) {
		super(context);
	}

	public RatioLayout(Context context, AttributeSet attrs) {
		super(context, attrs);
		
		//方式1：
		//float ratio = attrs.getAttributeFloatValue(NS, "ratio", 0);
		//System.out.println("ratio=" + ratio);
		
		//方式2：
		TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attrs, R.styleable.MyRatioLayoutAttrs);
		//自定义属性集合的名称_自定义属性的名称    ：  这个自定义属性在自定义属性集合中的下标索引
		ratio2 = obtainStyledAttributes.getFloat(R.styleable.MyRatioLayoutAttrs_ratio, 0);
		obtainStyledAttributes.recycle();//节约内存
		System.out.println("ratio2=" + ratio2);
		
	}

	public RatioLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}
	
	//onMeasure--onLayout--onDraw
	//widthMeasureSpec:宽度的信息   宽度+宽度的模式
	//heightMeasureSpec:高度的信息  高度+高度的模式
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		
		System.out.println("widthMeasureSpec=" + widthMeasureSpec);
		
		int widthSize = MeasureSpec.getSize(widthMeasureSpec);//宽度的大小
		int widthMode = MeasureSpec.getMode(widthMeasureSpec);//宽度模式
		
		int heightSize = MeasureSpec.getSize(heightMeasureSpec);//高度的大小
		int heightMode = MeasureSpec.getMode(heightMeasureSpec);//高度的模式
		
		//MeasureSpec.AT_MOST;至多的模式   wrap_content
		//MeasureSpec.EXACTLY;确定的模式  写死多少dp或者match_parent
		//MeasureSpec.UNSPECIFIED;未确定模式 ListView或者ScrollView中高度的模式，必须要等待所有的孩子的高度计算完成之后，才能计算出话ListView本身的高度
		if(ratio2 != 0 && widthMode == MeasureSpec.EXACTLY) {
			int innerWidth = widthSize - getPaddingLeft() - getPaddingRight();
			heightSize = (int) (innerWidth/ratio2 + 0.5f);
			//确定这个控件的宽度和高度
			//所以的控件想要显示出来，就必须得经历过绘制流程
			//setMeasuredDimension(widthSize, heightSize);
			//此时重新生成heightMeasureSpec
			//当大小确定下来了之后，模式就是确定的模式
			
			heightSize = heightSize + getPaddingTop() + getPaddingBottom();
			heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
		}
		
		
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

}
