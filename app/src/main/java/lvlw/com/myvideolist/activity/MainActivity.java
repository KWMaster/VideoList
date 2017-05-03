package lvlw.com.myvideolist.activity;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;
import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.adapter.MyAdapter;
import lvlw.com.myvideolist.entity.QFileInfo;
import lvlw.com.myvideolist.events.GoToFragment;
import lvlw.com.myvideolist.fragment.FileDirFragment;
import lvlw.com.myvideolist.fragment.FileFragment;
import lvlw.com.myvideolist.fragment.FileScanSetFragment;
import lvlw.com.myvideolist.permission.PermissionsActivity;
import lvlw.com.myvideolist.permission.PermissionsChecker;

public class MainActivity extends AppCompatActivity {

    @InjectView(R.id.myvideo)
    TextView myvideo;
    @InjectView(R.id.edit)
    TextView edit;
    @InjectView(R.id.seltall)
    TextView seltall;
    @InjectView(R.id.reseltall)
    TextView reseltall;
    @InjectView(R.id.toolbar)
    Toolbar toolbar;
    @InjectView(R.id.content)
    FrameLayout content;

    private static final int REQUEST_CODE = 0; // 请求码

    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
    };
    @InjectView(R.id.scanSDCard)
    TextView scanSDCard;

    private PermissionsChecker mPermissionsChecker; // 权限检测器


    private FileDirFragment filedirfragment;
    private FileFragment filefragment;
    private FileScanSetFragment fileScanSetFragment;
    private List<Fragment> mFragments;
    private int mIndex;
    private MyAdapter mAdapter;
    private List<QFileInfo> mDatas;
    private boolean selectedall = false;
    private boolean firstIn = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPermissionsChecker = new PermissionsChecker(this);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 缺少权限时, 进入权限配置页面
        if (firstIn) {
            if (Build.VERSION.SDK_INT >= 23) {
                if (mPermissionsChecker.lacksPermissions(PERMISSIONS)) {
                    startPermissionsActivity();
                } else {
                    initView();
                    firstIn = false;
                }
            } else {
                initView();
                firstIn = false;
            }
        }
//        if (filefragment != null) {
//            setIndexSelected(1);
//        }
    }

    private void startPermissionsActivity() {
        PermissionsActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void initView() {
                toolbar.setNavigationIcon(R.mipmap.logo_yunpan_home);
                toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
//                        filedirfragment.getIbtnRightMenu1().setOnClickListener(filedirfragment.getAddSharedPrefrenceInstance());
                        if (edit.getVisibility()==View.VISIBLE&&edit.getText().toString().equals("编辑")){
                            if (fileScanSetFragment==null){
                                fileScanSetFragment=new FileScanSetFragment();
                                mFragments.add(fileScanSetFragment);
                            }
                            myvideo.setVisibility(View.GONE);
                            scanSDCard.setVisibility(View.GONE);
                            edit.setVisibility(View.GONE);
                            setIndexSelected(2);
                        }
                    }
                });
        filedirfragment = new FileDirFragment();
        filefragment = new FileFragment();
        mFragments=new ArrayList<>();
        mFragments.add(filedirfragment);
        mFragments.add(filefragment);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.add(R.id.content, filedirfragment).commit();
        setIndexSelected(1);
    }

    @OnClick({R.id.myvideo, R.id.scanSDCard, R.id.edit, R.id.seltall, R.id.reseltall})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.myvideo:
//                filedirfragment.getIbtnRightMenu1().setOnClickListener(filedirfragment.getScanFileInstance());
                //                filedirfragment.setSharedPreferences(false);
                if (mFragments.get(mIndex) instanceof FileFragment) {
                    ((FileFragment) mFragments.get(mIndex)).getIbtnRightMenu2().setVisibility(View.GONE);
                    mAdapter = (MyAdapter) filefragment.getFile().getAdapter();
                    mDatas = mAdapter.getmDatas();
                    for (int i = 0; i < mDatas.size(); i++) {
                        mDatas.get(i).setCheck(false);
                    }
                    ;
                    if (mAdapter.flage) {
                        mAdapter.flage = !mAdapter.flage;
                    }
                    seltall.setVisibility(View.GONE);
                    reseltall.setVisibility(View.GONE);
                    edit.setText("编辑");
                }
                filedirfragment.initData();
                filedirfragment.getFileparent().setText(Environment.getExternalStorageDirectory().getPath());
                setIndexSelected(0);
                myvideo.setVisibility(View.GONE);
                scanSDCard.setVisibility(View.GONE);
                edit.setVisibility(View.GONE);
                seltall.setVisibility(View.VISIBLE);
                reseltall.setVisibility(View.VISIBLE);
                //                CommonUtils dbUtils=new CommonUtils(this);
                //                dbUtils.deleteStudentAll();
                //                setEditVisiable(mFragments[mIndex]);
                break;
            case R.id.scanSDCard:
                break;
            case R.id.edit:
//                if (mFragments.get(mIndex) == filedirfragment) {
//                    mAdapter = (MyAdapter) filedirfragment.getFiledir().getAdapter();
//                    if (mAdapter.getCount() > 0) {
//                        mAdapter.flage = !mAdapter.flage;
//
//                        if (mAdapter.flage) {
//                            filedirfragment.getIbtnRightMenu1().setVisibility(View.VISIBLE);
//                            mDatas = ((MyAdapter) filedirfragment.getFiledir().getAdapter()).getmDatas();
//                            for (int i = 0; i < mDatas.size(); i++) {
//                                mDatas.get(i).setCheck(false);
//                            }
//                            seltall.setVisibility(View.VISIBLE);
//                            reseltall.setVisibility(View.VISIBLE);
//                            edit.setText("取消");
//                        } else {
//                            seltall.setVisibility(View.GONE);
//                            reseltall.setVisibility(View.GONE);
//                            filedirfragment.getIbtnRightMenu1().setVisibility(View.GONE);
//                            edit.setText("编辑");
//                        }
//
//                        mAdapter.notifyDataSetChanged();
//                    }
                if (mFragments.get(mIndex) == filefragment){
                    mAdapter = (MyAdapter) filefragment.getFile().getAdapter();
                    if (mAdapter.getCount() > 0) {
                        mAdapter.flage = !mAdapter.flage;

                        if (mAdapter.flage) {
                            mDatas = ((MyAdapter) filefragment.getFile().getAdapter()).getmDatas();
                            for (int i = 0; i < mDatas.size(); i++) {
                                mDatas.get(i).setCheck(false);
                            }
                            ;
                            seltall.setVisibility(View.VISIBLE);
                            reseltall.setVisibility(View.VISIBLE);
                            filefragment.getIbtnRightMenu2().setVisibility(View.VISIBLE);
                            myvideo.setVisibility(View.GONE);
                            scanSDCard.setVisibility(View.GONE);
                            mAdapter.isAdd = true;
                            edit.setText("取消");
                        } else {
                            seltall.setVisibility(View.GONE);
                            reseltall.setVisibility(View.GONE);
                            filefragment.getIbtnRightMenu2().setVisibility(View.GONE);
                            myvideo.setVisibility(View.VISIBLE);
                            scanSDCard.setVisibility(View.VISIBLE);
                            mAdapter.isAdd = false;
                            edit.setText("编辑");
                        }

                        mAdapter.notifyDataSetChanged();
                    }
                }

                break;
            case R.id.seltall:
                if (mFragments.get(mIndex) == filedirfragment) {
                    mAdapter = (MyAdapter) filedirfragment.getFiledir().getAdapter();
                    //                    setEditVisiable(filedirfragment);
//                    if (mAdapter.flage) {
                        selectedall = !selectedall;
                        mDatas = ((MyAdapter) filedirfragment.getFiledir().getAdapter()).getmDatas();
                        if (selectedall) {
                            for (int i = 0; i < mDatas.size(); i++) {
                                mDatas.get(i).setCheck(true);
                            }
                        } else {
                            for (int i = 0; i < mDatas.size(); i++) {
                                mDatas.get(i).setCheck(false);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
//                    }
                } else if (mFragments.get(mIndex) == filefragment){
                    mAdapter = (MyAdapter) filefragment.getFile().getAdapter();
                    //                    setEditVisiable(filefragment);
                    if (mAdapter.flage) {
                        selectedall = !selectedall;
                        mDatas = ((MyAdapter) filefragment.getFile().getAdapter()).getmDatas();
                        if (selectedall) {
                            for (int i = 0; i < mDatas.size(); i++) {
                                mDatas.get(i).setCheck(true);
                            }
                        } else {
                            for (int i = 0; i < mDatas.size(); i++) {
                                mDatas.get(i).setCheck(false);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
            case R.id.reseltall:
                if (mFragments.get(mIndex) == filedirfragment) {
                    mAdapter = (MyAdapter) filedirfragment.getFiledir().getAdapter();
                    //                    setEditVisiable(filedirfragment);
//                    if (mAdapter.flage) {
                        mDatas = ((MyAdapter) filedirfragment.getFiledir().getAdapter()).getmDatas();
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (mDatas.get(i).isCheck()) {
                                mDatas.get(i).setCheck(false);
                            } else {
                                mDatas.get(i).setCheck(true);
                            }
                        }

                        mAdapter.notifyDataSetChanged();
//                    }
                } else if (mFragments.get(mIndex) == filefragment){
                    mAdapter = (MyAdapter) filefragment.getFile().getAdapter();
                    //                    setEditVisiable(filefragment);
                    if (mAdapter.flage) {
                        mDatas = ((MyAdapter) filefragment.getFile().getAdapter()).getmDatas();
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (mDatas.get(i).isCheck()) {
                                mDatas.get(i).setCheck(false);
                            } else {
                                mDatas.get(i).setCheck(true);
                            }
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                }
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (mFragments.get(mIndex) instanceof FileDirFragment) {
            ((FileDirFragment) mFragments.get(mIndex)).onKeyDown(keyCode, event);
            return true;
        } else if (mFragments.get(mIndex) instanceof FileFragment) {
            ((FileFragment) mFragments.get(mIndex)).onKeyDown(keyCode, event);
            return true;
        }else if (mFragments.get(mIndex) instanceof FileScanSetFragment){
            ((FileScanSetFragment) mFragments.get(mIndex)).onKeyDown(keyCode, event);
            return true;
        }
        return false;
    }

    private void setIndexSelected(int index) {
        if (mIndex == index) {
            return;
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft = fragmentManager.beginTransaction();
        //        ft.replace(R.id.content,mFragments[index]);
        ft.hide(mFragments.get(mIndex));

        if (!mFragments.get(index).isAdded()) {
            ft.add(R.id.content, mFragments.get(index));
        } else {
            ft.show(mFragments.get(index));
        }
        ft.commit();
        mIndex = index;
    }

    private void setEditVisiable(Fragment fragment) {
        if (fragment == filedirfragment) {
            edit.setVisibility(View.VISIBLE);
            seltall.setVisibility(View.VISIBLE);
            reseltall.setVisibility(View.VISIBLE);
        } else {
            edit.setVisibility(View.GONE);
            seltall.setVisibility(View.GONE);
            reseltall.setVisibility(View.GONE);
        }
    }

    @Subscribe
    public void onEventMainThread(GoToFragment goToFragment) {
        setIndexSelected(goToFragment.getIndex());
        myvideo.setVisibility(View.VISIBLE);
        scanSDCard.setVisibility(View.VISIBLE);
        edit.setVisibility(View.VISIBLE);
        seltall.setVisibility(View.GONE);
        reseltall.setVisibility(View.GONE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        //        if(mFragments[mIndex] instanceof FileDirFragment){
        //            ((FileDirFragment)mFragments[mIndex]).getDbUtils().getDaoManager().closeConnection();
        //        }else if (mFragments[mIndex] instanceof FileFragment){
        //            ((FileFragment)mFragments[mIndex]).getDbUtils().getDaoManager().closeConnection();
        //        }
    }
}
