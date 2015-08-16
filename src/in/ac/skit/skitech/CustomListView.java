package in.ac.skit.skitech;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;

public class CustomListView extends ListView {

	int a[] = new int[] { 0, 0, 0, 0, 0, 0, 0, 0 };

	private static final int NEW_ROW_DURATION = 500;
	private ViewGroup mView;

	private DecelerateInterpolator sOvershootInterpolator;

	ProgressBar mProgress;

	private Context mContext;

	private List<ListItemObject> mData;

	public CustomListView(Context context) {
		super(context);

		mView = this;
		init(context);
	}

	public CustomListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		mView = this;
		init(context);
	}

	public CustomListView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		mView = this;
		init(context);
	}

	public void init(Context context) {
		setDivider(null);
		mContext = context;
		new ArrayList<BitmapDrawable>();
		sOvershootInterpolator = new DecelerateInterpolator();
	}

	public void addRow(ListItemObject newObj) {

	}

	public void addProgress() {

		final ViewTreeObserver observer = getViewTreeObserver();

		observer.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

			@Override
			public void onGlobalLayout() {
				if (mData.isEmpty() && mView.getChildCount() == 0) {
					mProgress = new ProgressBar(mContext);
					LinearLayout.LayoutParams pLayoutParams = new LinearLayout.LayoutParams(
							LayoutParams.WRAP_CONTENT,
							LayoutParams.WRAP_CONTENT);
					pLayoutParams.gravity = Gravity.CENTER_HORIZONTAL
							| Gravity.CENTER_VERTICAL;
					mProgress.setLayoutParams(pLayoutParams);

					mView.addView(mProgress);
				}

			}

		});
	}

	public void removeProgress() {
		if (!mData.isEmpty() && mProgress != null) {
			this.removeView(mProgress);
		}
	}

	public void addLists(List<ListItemObject> obj) {
		final CustomListAdapter adapter = (CustomListAdapter) getAdapter();

		getChildCount();
		mData.addAll(obj);
		adapter.addStableIds();
		adapter.notifyDataSetChanged();

		final ViewTreeObserver observer = getViewTreeObserver();

		observer.addOnPreDrawListener(new ViewTreeObserver.OnPreDrawListener() {
			public boolean onPreDraw() {
				getViewTreeObserver().removeOnPreDrawListener(this);

				int firstVisiblePosition = getFirstVisiblePosition();
				for (int i = firstVisiblePosition; i < getChildCount(); i++) {
					View child = getChildAt(i);

					int childWidth = child.getWidth();
					int startWidth = -childWidth;

					TranslateAnimation animation = new TranslateAnimation(0,
							startWidth, 0, 0);
					animation.setFillAfter(true);
					animation.setDuration(0);
					child.startAnimation(animation);
					
					animation = new TranslateAnimation(
							startWidth, 0, 0, 0);
					animation.setDuration(NEW_ROW_DURATION);
					animation.setStartOffset((int) (i + 1)
							* (int) (NEW_ROW_DURATION / 3.5f));
					animation.setInterpolator(sOvershootInterpolator);
					animation.setAnimationListener(new AnimationListener() {
						@Override
						public void onAnimationEnd(Animation animation) {
							setEnabled(true);
							invalidate();
						}

						@Override
						public void onAnimationRepeat(Animation animation) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onAnimationStart(Animation animation) {
							// TODO Auto-generated method stub

						}
					});
					child.setAnimation(animation);
				}

				setEnabled(false);
				return true;

			}

		});
	}

	public Point getLocationOnScreen(View v) {
		DisplayMetrics dm = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay()
				.getMetrics(dm);

		int[] location = new int[2];
		v.getLocationOnScreen(location);

		return new Point(location[0], location[1]);
	}

	public void setData(List<ListItemObject> data) {
		mData = data;
	}

	public void setLayout(FrameLayout layout) {
	}

	@Override
	protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
		return super.drawChild(canvas, child, drawingTime);
	}

	@Override
	protected View findViewTraversal(int id) {

		return super.findViewTraversal(id);
	}

	@Override
	protected View findViewWithTagTraversal(Object tag) {

		return super.findViewWithTagTraversal(tag);
	}

	@Override
	protected void layoutChildren() {

		super.layoutChildren();
		if (a[3] <= 1) {

		}
	}

	@Override
	protected void onFinishInflate() {

		super.onFinishInflate();
	}

	@Override
	protected void onFocusChanged(boolean gainFocus, int direction,
			Rect previouslyFocusedRect) {

		super.onFocusChanged(gainFocus, direction, previouslyFocusedRect);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {

		super.onSizeChanged(w, h, oldw, oldh);
	}

	@Override
	protected void onDraw(Canvas canvas) {

		super.onDraw(canvas);
	}

}
