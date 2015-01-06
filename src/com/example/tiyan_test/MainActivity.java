package com.example.tiyan_test;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;
import android.widget.Toast;

/**
 * @author jiantoa88
 * 
 */
public class MainActivity extends Activity implements OnPageChangeListener {
	// ��̬���ͼƬ��ֻ�轫ͼƬ�����������м���
	private int[] guides = { R.drawable.g1, R.drawable.g2, R.drawable.g3,
			R.drawable.g4 };
	private static final int TO_END = 0;// ���ñ�� ���������һ��ʱ
	private static final int LEAVE_FROAM_END = 1;// ���뿪���һ��ʱ
	private List<View> guider_views = new ArrayList<View>();
	private ViewPager viewPager;
	private ImageView iv_start;
	private ImageView iv_currentDot;// ��ǰ�������
	private LinearLayout dotLayout;// ����������
	private int offset;// ��¼������λ����
	private int currentPos = 0;// ��¼��ǰ��λ��
	private GuidePaperAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		initView();
	}

	private void initView() {
		// TODO Auto-generated method stub
		dotLayout = (LinearLayout) findViewById(R.id.dot_contain);
		viewPager = (ViewPager) findViewById(R.id.viewPager);
		iv_currentDot = (ImageView) findViewById(R.id.cur_dot);
		iv_start = (ImageView) findViewById(R.id.open);
		iv_start.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Toast.makeText(MainActivity.this, "���������", Toast.LENGTH_SHORT)
						.show();
			}
		});

		initDot();
		initGuide();
		// ��iv_currentDot�����ڵ����β�ν�Ҫ�����ʱ �˷���������
		iv_currentDot.getViewTreeObserver().addOnPreDrawListener(
				new OnPreDrawListener() {

					@Override
					public boolean onPreDraw() {
						// TODO Auto-generated method stub
						// ��ȡ��ͼƬ�Ŀ�ȣ�����ƶ�����ʱ��
						offset = iv_currentDot.getWidth();
						return true;
					}
				});
		adapter = new GuidePaperAdapter(guider_views);
		viewPager.setAdapter(adapter);
		viewPager.setOnPageChangeListener(this);
	}

	private void initGuide() {
		// TODO Auto-generated method stub
		guider_views.clear();
		ImageView view = null;
		for (int i = 0; i < guides.length; i++) {
			view = buildImageView(guides[i]);
			guider_views.add(view);
		}
	}

	/**
	 * @param id
	 * @return ��ʼ��guide�� view
	 */
	private ImageView buildImageView(int id) {
		ImageView iv = new ImageView(this);
		iv.setImageResource(id);
		ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(
				ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.MATCH_PARENT);
		iv.setLayoutParams(params);
		iv.setScaleType(ScaleType.FIT_XY);
		return iv;
	}

	/**
	 * * ��ʼ���� ImageVIew
	 * 
	 * @return ����true˵����ʼ����ɹ�������ʵ����ʧ��
	 */
	private boolean initDot() {
		if (guides.length > 0) {
			ImageView dotView;
			for (int i = 0; i < guides.length; i++) {
				dotView = new ImageView(this);
				dotView.setImageResource(R.drawable.dot1_w);
				dotView.setLayoutParams(new LinearLayout.LayoutParams(
						ViewGroup.LayoutParams.WRAP_CONTENT,
						ViewGroup.LayoutParams.WRAP_CONTENT, 1.0f));
				dotLayout.addView(dotView);
			}
			return true;
		} else {
			return false;
		}
	}

	class GuidePaperAdapter extends PagerAdapter {
		private List<View> views;

		public GuidePaperAdapter(List<View> views) {
			super();
			this.views = views;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return views.size();
		}

		@Override
		public Object instantiateItem(ViewGroup container, int position) {
			// TODO Auto-generated method stub
			View view = views.get(position);
			container.addView(view);
			return view;
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			// TODO Auto-generated method stub
			// super.destroyItem(container, position, object);
			container.removeView(views.get(position));
		}

		@Override
		public boolean isViewFromObject(View arg0, Object arg1) {
			// TODO Auto-generated method stub
			return arg0 == arg1;
		}

	}

	/**
	 * ��handler֪ͨiv_start ��ʾ
	 */
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == TO_END) {
				iv_start.setVisibility(View.VISIBLE);
			} else if (msg.what == LEAVE_FROAM_END) {
				iv_start.setVisibility(View.GONE);
			}
		};
	};

	@Override
	public void onPageScrollStateChanged(int arg0) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageScrolled(int arg0, float arg1, int arg2) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onPageSelected(int position) {
		// �ڴ�����dot���ƶ�����
		moveCursorTo(position);
		// �������һ��ʱ
		if (position == guides.length - 1) {
			handler.sendEmptyMessage(TO_END);
		} else {
			handler.sendEmptyMessage(LEAVE_FROAM_END);
		}
		currentPos = position;
	}

	/**
	 * ����ʱ �������ƶ�����
	 * 
	 * @param position
	 */
	private void moveCursorTo(int position) {
		AnimationSet animationSet = new AnimationSet(true);
		TranslateAnimation translateAnimation = new TranslateAnimation(offset
				* currentPos, offset * position, 0, 0);
		animationSet.addAnimation(translateAnimation);
		animationSet.setDuration(300);
		animationSet.setFillAfter(true);
		iv_currentDot.startAnimation(animationSet);
	}
}
