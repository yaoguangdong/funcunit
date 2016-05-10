package cn.farcanton.horizontalScroll;

public interface OnHorizontalScrollViewScrollfinish {
	/**
	 * 左侧滑动完毕
	 */
	public void onLeftChange(int l);
	
	public void onRightChange(int l,int maxWidth);
}
