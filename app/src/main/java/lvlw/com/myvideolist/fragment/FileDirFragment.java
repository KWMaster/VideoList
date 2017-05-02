package lvlw.com.myvideolist.fragment;

import android.media.MediaMetadataRetriever;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.VideoInfo.entity.FileFolder;
import com.VideoInfo.entity.VideoInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Vector;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.adapter.MyAdapter;
import lvlw.com.myvideolist.entity.QFileInfo;
import lvlw.com.myvideolist.events.GoToFragment;
import lvlw.com.myvideolist.events.ScanResultEvent;
import lvlw.com.myvideolist.fileutils.FileDirScaner;
import lvlw.com.myvideolist.fileutils.QFileScaner;
import lvlw.com.myvideolist.greendao.CommonUtils;
import lvlw.com.myvideolist.utils.MyComparator;

/**
 * Created by Wantrer on 2017/4/22 0022.
 */

public class FileDirFragment extends Fragment {

    public TextView getFileparent() {
        return fileparent;
    }

    @InjectView(R.id.fileparent)
    TextView fileparent;
    @InjectView(R.id.filedir)
    ListView filedir;

    @InjectView(R.id.ibtn_right_menu1)
    LinearLayout ibtnRightMenu1;


    public ListView getFiledir() {
        return filedir;
    }

    private LayoutInflater layoutInflater;
    private List<QFileInfo> mDatas;
    private TextView cancel;

    public CommonUtils getDbUtils() {
        return dbUtils;
    }

    private CommonUtils dbUtils;

    private MyAdapter mAdapter;
    private Context context;
    private List<VideoInfo> videoInfos;
    private MediaMetadataRetriever mMetadataRetriever;
    private GoToFragment gotofrg=new GoToFragment(1);
    private TextView myvideo;
    private TextView scanSDCard;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context=getActivity();
        View rootView = LayoutInflater.from(context).inflate(R.layout.fragment_main, null);
        ButterKnife.inject(this, rootView);
        cancel = (TextView) getActivity().findViewById(R.id.edit);
        myvideo= (TextView) getActivity().findViewById(R.id.myvideo);
        scanSDCard= (TextView) getActivity().findViewById(R.id.scanSDCard);
        initView();
        return rootView;
    }

    private void initView() {
        fileparent.setText(Environment.getExternalStorageDirectory().getPath());
        fileparent.setOnClickListener(new DackFileDironClickListener());
        ibtnRightMenu1.setOnClickListener(new ScanFileonClickListener());
    }


    public void initData() {
        mDatas = new ArrayList<>();
        new AsyncTask<Context, Integer, List<QFileInfo>>() {
            private ProgressDialog dialog;

            @Override
            protected void onPreExecute() {
                dialog = ProgressDialog.show(context, "",
                        "正在扫描SD卡,请稍候....");
                super.onPreExecute();
            }

            @Override
            protected List<QFileInfo> doInBackground(Context... params) {
                FileDirScaner scaner = new FileDirScaner();
                scaner.Start(Environment.getExternalStorageDirectory());
                mDatas = scaner.get_resultFiles();
                String[] sortBy = new String[] { "_fileName" };
                int orderBy = 1;//1:升序，-1：降序
                MyComparator myCmp = new MyComparator(sortBy, orderBy);
                Collections.sort(mDatas, myCmp);
                return null;
            }

            @Override
            protected void onPostExecute(List<QFileInfo> qFileInfos) {
                dialog.dismiss();
                // TODO: create test data
                if (mDatas.size() > 0) {
                    mAdapter = new MyAdapter(context, mDatas, fileparent,getActivity());
                    mAdapter.flage=true;
                    filedir.setAdapter(mAdapter);
                }
                super.onPostExecute(qFileInfos);
            }
        }.execute();
        //        mDatas = new ArrayList<>();
        //        for (int i = 0; i < 20; i++) {
        //
        //            QFileInfo fileinfo = new QFileInfo(R.mipmap.folder, "123", "/storage/emulator/0", "456789", false);
        //            mDatas.add(fileinfo);
        //        }
        //
        //        mAdapter = new MyAdapter(getActivity(), mDatas);
        //        filedir.setAdapter(mAdapter);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public void onKeyDown(int keyCode, KeyEvent event) {
//        mAdapter = (MyAdapter) filedir.getAdapter();
        if (cancel.getText().equals("取消")) {
            cancel.performClick();
        }
        myvideo.setVisibility(View.VISIBLE);
        scanSDCard.setVisibility(View.VISIBLE);
        fileparent.setText(Environment.getExternalStorageDirectory().getPath());
        EventBus.getDefault().post(gotofrg);
    }


    public class DackFileDironClickListener implements View.OnClickListener {

        private String filepath;

        @Override
        public void onClick(View v) {
            filepath = fileparent.getText().toString();
            if (!filepath.equals(Environment.getExternalStorageDirectory().getPath())) {
                mAdapter = (MyAdapter) filedir.getAdapter();
                if (!mAdapter.flage) {
                    cancel.performClick();
                }
                new AsyncTask<Context, Integer, List<QFileInfo>>() {
                    private ProgressDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        dialog = ProgressDialog.show(context, "",
                                "正在扫描SD卡,请稍候....");
                        cancel.performClick();
                        super.onPreExecute();
                    }

                    @Override
                    protected List<QFileInfo> doInBackground(Context... params) {
                        FileDirScaner scaner = new FileDirScaner();

                        scaner.Start(new File(filepath).getParentFile());
                        List<QFileInfo> fileInfos = scaner.get_resultFiles();
                        if (fileInfos.size() > 0) {
                            String[] sortBy = new String[] { "_fileName" };
                            int orderBy = 1;//1:升序，-1：降序
                            MyComparator myCmp = new MyComparator(sortBy, orderBy);
                            Collections.sort(fileInfos, myCmp);
                            mDatas = fileInfos;
                        }
                        return null;
                    }

                    @Override
                    protected void onPostExecute(List<QFileInfo> qFileInfos) {
                        dialog.dismiss();
                        // TODO: create test data
                        if (mDatas.size() >= 0) {
                            if (mDatas.size() == 0) {
                                Toast.makeText(context, "没有文件夹", Toast.LENGTH_SHORT).show();
                            } else {
                                fileparent.setText(new File(filepath).getParent());
                                mAdapter = new MyAdapter(context, mDatas, fileparent,getActivity());
                                mAdapter.flage=true;
                                filedir.setAdapter(mAdapter);
                            }
                        }
                        super.onPostExecute(qFileInfos);
                    }
                }.execute();
            }
        }
    }

//    //多线程访问
//    private volatile static ScanFileonClickListener manager;
//    //单例模式
//    public ScanFileonClickListener getScanFileInstance() {
//        ScanFileonClickListener instance = null;
//        if (manager == null) {
//            synchronized (ScanFileonClickListener.class) {
//                if (instance == null) {
//                    instance = new ScanFileonClickListener();
//                    manager = instance;
//                }
//            }
//        }else{
//            instance=manager;
//        }
//        return instance;
//    }
    public class ScanFileonClickListener implements View.OnClickListener {


        @Override
        public void onClick(final View v) {
            mMetadataRetriever = new MediaMetadataRetriever();
            final List<String> filepaths = new ArrayList<>();
            final List<QFileInfo> Datas=((MyAdapter) filedir.getAdapter()).getmDatas();

            if (mAdapter.flage) {
                cancel.performClick();
            }
            mDatas=new ArrayList<>();
            dbUtils=new CommonUtils(context,"videoinfo.db");
            new AsyncTask<Context,Integer,List<QFileInfo>>(){
                private ProgressDialog dialog;
                boolean addtoSQLite=false;
                @Override
                protected void onPreExecute() {
                    dialog = ProgressDialog.show(context, "",
                            "正在扫描SD卡,请稍候....");
                    super.onPreExecute();
                }

                @Override
                protected List<QFileInfo> doInBackground(Context... params) {
                    List<FileFolder> queryFileFolder=dbUtils.listAllFileFolder();
                    List<String> queryFilePath=new ArrayList<String>();
                    for (FileFolder fileFolder : queryFileFolder) {
                        queryFilePath.add(fileFolder.getFile_Path());
                    }
                    for (int i = 0; i < Datas.size(); i++) {
                        if (Datas.get(i).isCheck()) {
                            filepaths.add(Datas.get(i).get_filePath());
                        }
                    }
                    for (String filepath : filepaths) {
                        QFileScaner scaner=new QFileScaner(queryFilePath);
                        scaner.Start(new File(filepath),mMetadataRetriever);
                        List<QFileInfo> fileInfos = scaner.get_resultFiles();
                        mDatas.addAll(fileInfos);
                    }
                    if (mDatas.size()>0){

                        videoInfos=new ArrayList<VideoInfo>();
                        for (QFileInfo data : mDatas) {
                            if (!dbUtils.queryBuilder(data.get_filePath())){
                                VideoInfo videoInfo=new VideoInfo();
                                videoInfo.setFile_Name(data.get_fileName());
                                videoInfo.setFile_Size(data.get_fileSize());
                                videoInfo.setFile_Path(data.get_filePath());
                                videoInfo.setFile_Duration(data.get_fileDesc());
                                videoInfo.setFile_Extention(data.get_fileExt());
                                videoInfos.add(videoInfo);
                            }
                        }
                        String[] sortBy = new String[] { "file_Name" };
                        int orderBy = 1;//1:升序，-1：降序
                        MyComparator myCmp = new MyComparator(sortBy, orderBy);
                        Collections.sort(videoInfos, myCmp);
                        if (videoInfos.size()>0){
                            if (dbUtils.inserMultStudent(videoInfos)){
                                addtoSQLite=true;
                                videoInfos=dbUtils.listAll();
                            };
                        }
                        dbUtils.getDaoManager().closeDaoSession();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(List<QFileInfo> qFileInfos) {
                    dialog.dismiss();
                    // TODO: create test data
                    if (mDatas.size() >= 0) {
                        EventBus.getDefault().post(gotofrg);
                        if (mDatas.size() == 0) {
                            Toast.makeText(context, "没有找到视频文件", Toast.LENGTH_SHORT).show();
                            ((MyAdapter) filedir.getAdapter()).notifyDataSetChanged();
                        } else {
                            if (addtoSQLite){
                                final ScanResultEvent results=new ScanResultEvent();
                                results.setAddSuccessful("添加成功");
                                Handler handler=new Handler();
                                handler.postDelayed(new Runnable() {
                                    @Override
                                    public void run() {
                                        EventBus.getDefault().post(results);
                                    }
                                },1);
                            }else {
                                Toast.makeText(context, "没有找到新的文件", Toast.LENGTH_SHORT).show();
                            }
                        }
                        myvideo.setVisibility(View.VISIBLE);
                        scanSDCard.setVisibility(View.VISIBLE);
                    }
                    super.onPostExecute(qFileInfos);
                }
            }.execute();

        }
    }

//    //多线程访问
//    private volatile static AddSharedPrefrenceonClickListener manager2;
//    //单例模式
//    public AddSharedPrefrenceonClickListener getAddSharedPrefrenceInstance() {
//        AddSharedPrefrenceonClickListener instance = null;
//        if (manager2 == null) {
//            synchronized (AddSharedPrefrenceonClickListener.class) {
//                if (instance == null) {
//                    instance = new AddSharedPrefrenceonClickListener();
//                    manager2 = instance;
//                }
//            }
//        }else{
//            instance=manager2;
//        }
//        return instance;
//    }
}
