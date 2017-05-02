package lvlw.com.myvideolist.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * Created by Wantrer on 2017/5/1 0001.
 */

public class ScanSetPagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private String[] titles={"已过滤的文件夹","全部文件夹"};
    public ScanSetPagerAdapter(FragmentManager fm,List<Fragment> fragments) {
        super(fm);
        this.fragments=fragments;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                if (fragments.get(0)!=null){
                    return fragments.get(0);
                }
                break;
            case 1:
                if (fragments.get(1)!=null){
                    return fragments.get(1);
                }
                break;
            default:
                return null;
        }
        return null;
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
