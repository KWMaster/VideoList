package lvlw.com.myvideolist.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.adapter.ScanSetPagerAdapter;
import lvlw.com.myvideolist.events.AddClickTimes;
import lvlw.com.myvideolist.events.GoToFragment;
import lvlw.com.myvideolist.events.ScanResultEvent;
import lvlw.com.myvideolist.view.PagerSlidingTabStrip;

/**
 * Created by Wantrer on 2017/5/1 0001.
 */

public class FileScanSetFragment extends Fragment {
    @InjectView(R.id.scan_pagers)
    ViewPager scanPagers;
    @InjectView(R.id.scan_tabs)
    PagerSlidingTabStrip scanTabs;
    private GoToFragment gotofrg=new GoToFragment(1);
    private DisplayMetrics dm;
    private ScanSetPagerAdapter pagerAdapter;
    private List<Fragment> fragments;
    private static int LAST_ADD_CLICK_TIMES=0;
    private static int RECIVED_ADD_CLICK_TIMES;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.scanset_fragment, null);
        dm = getResources().getDisplayMetrics();
        ButterKnife.inject(this, rootView);
        initData();
        scanPagers.setOffscreenPageLimit(2);
        pagerAdapter=new ScanSetPagerAdapter(getChildFragmentManager(),fragments);
        scanPagers.setAdapter(pagerAdapter);
        scanTabs.setViewPager(scanPagers);
        setTabValue();
        scanPagers.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position==0&&RECIVED_ADD_CLICK_TIMES!=LAST_ADD_CLICK_TIMES){
                    Handler ha = new Handler();
                    ((FileScanOne)fragments.get(0)).initData();
                    ha.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            ((FileScanOne)fragments.get(0)).initView();
                        }
                    }, 10);
                    LAST_ADD_CLICK_TIMES=RECIVED_ADD_CLICK_TIMES;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        return rootView;
    }

    private void initData() {
        FileScanOne fileScanOne=new FileScanOne();
        FileScanTwo fileScanTwo=new FileScanTwo();
        fileScanTwo.initData(getActivity());
        fragments=new ArrayList<>();
        fragments.add(fileScanOne);
        fragments.add(fileScanTwo);
    }

    private void setTabValue() {
        // 设置Tab是自动填充满屏幕的
        scanTabs.setShouldExpand(true);
        // 设置Tab的分割线是透明的
        scanTabs.setDividerColor(Color.TRANSPARENT);
        // 设置Tab底部线的高度
        scanTabs.setUnderlineHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 1, dm));
        // 设置Tab Indicator的高度
        scanTabs.setIndicatorHeight((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 3, dm));
        // 设置Tab标题文字的大小
        scanTabs.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, dm));
        // 设置Tab Indicator的颜色
        scanTabs.setIndicatorColor(Color.parseColor("#d83737"));//#d83737   #d83737(绿)
        // 设置选中Tab文字的颜色 (这是我自定义的一个方法)
        scanTabs.setSelectedTextColor(Color.parseColor("#ffffff"));
        // 取消点击Tab时的背景色
        scanTabs.setTabBackground(0);
    }

    @Subscribe
    public void onEventMainThread(AddClickTimes addClickTimes) {
        RECIVED_ADD_CLICK_TIMES=addClickTimes.getTimes();
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
        //        mAdapter = (MyAdapter) filedir.getAdapter();
//        if (cancel.getText().equals("取消")) {
//            cancel.performClick();
//        }
//        myvideo.setVisibility(View.VISIBLE);
//        scanSDCard.setVisibility(View.VISIBLE);
//        fileparent.setText(Environment.getExternalStorageDirectory().getPath());
        EventBus.getDefault().post(gotofrg);
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
        EventBus.getDefault().unregister(this);
    }
}
