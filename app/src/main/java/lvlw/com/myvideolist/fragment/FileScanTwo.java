package lvlw.com.myvideolist.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.VideoInfo.entity.FileFolder;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.adapter.MyAdapter;
import lvlw.com.myvideolist.entity.QFileInfo;
import lvlw.com.myvideolist.events.AddClickTimes;
import lvlw.com.myvideolist.fileutils.FileDirScaner;
import lvlw.com.myvideolist.greendao.CommonUtils;
import lvlw.com.myvideolist.utils.MyComparator;

/**
 * Created by Wantrer on 2017/5/1 0001.
 */

public class FileScanTwo extends Fragment {
    @InjectView(R.id.fileparent)
    TextView fileparent;
    @InjectView(R.id.filedir)
    ListView filedir;
    @InjectView(R.id.ibtn_right_menu1)
    LinearLayout ibtnRightMenu1;

    private List<QFileInfo> mDatas;

    private CommonUtils dbUtils;
    private final String dbName="filefolder.db";

    private MyAdapter mAdapter;
    private List<FileFolder> fileFolders;
    private static int ADD_CLICK_TIMES=0;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.filescan_two_frg, null);
        ButterKnife.inject(this, rootView);
        initView();
        return rootView;
    }

    private void initView() {
        fileparent.setText(Environment.getExternalStorageDirectory().getPath());
        fileparent.setOnClickListener(new DackFileDironClickListener());
        ibtnRightMenu1.setOnClickListener(new AddSharedPrefrenceonClickListener());
    }
    public void initData(final Context context) {
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
                String[] sortBy = new String[]{"_fileName"};
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
                    mAdapter = new MyAdapter(context, mDatas, fileparent, getActivity());
                    mAdapter.flage=true;
                    filedir.setAdapter(mAdapter);
                }
                super.onPostExecute(qFileInfos);
            }
        }.execute();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }

    public class DackFileDironClickListener implements View.OnClickListener {

        private String filepath;

        @Override
        public void onClick(View v) {
            filepath = fileparent.getText().toString();
            if (!filepath.equals(Environment.getExternalStorageDirectory().getPath())) {
                mAdapter = (MyAdapter) filedir.getAdapter();
                new AsyncTask<Context, Integer, List<QFileInfo>>() {
                    private ProgressDialog dialog;

                    @Override
                    protected void onPreExecute() {
                        dialog = ProgressDialog.show(getActivity(), "",
                                "正在扫描SD卡,请稍候....");
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
                                Toast.makeText(getActivity(), "没有文件夹", Toast.LENGTH_SHORT).show();
                            } else {
                                fileparent.setText(new File(filepath).getParent());
                                mAdapter = new MyAdapter(getActivity(), mDatas, fileparent,getActivity());
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

    public class AddSharedPrefrenceonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mAdapter= (MyAdapter) filedir.getAdapter();
            mDatas=mAdapter.getmDatas();
            new AsyncTask<String,String,List<FileFolder>>(){
                private boolean add_a_sucsess=false;
                private boolean already_selected_data=true;
                private boolean already_add_selected_data=false;
                private ProgressDialog dialog;
                @Override
                protected void onPreExecute() {
                    dialog = ProgressDialog.show(getActivity(), "",
                            "正在添加,请稍候....");
                    super.onPreExecute();
                }

                @Override
                protected List<FileFolder> doInBackground(String... params) {
                    List<FileFolder> addToFilter=new ArrayList<>();
                    dbUtils=new CommonUtils(getActivity(),dbName);
                    List<FileFolder> queryFilter=dbUtils.listAllFileFolder();
                    for (int i = 0; i < mDatas.size(); i++) {
                        if (mDatas.get(i).isCheck()){
                            FileFolder filefolder=new FileFolder();
                            filefolder.setFile_Name(mDatas.get(i).get_fileName());
                            filefolder.setFile_Path(mDatas.get(i).get_filePath());
                            filefolder.setFile_check(false);
                            if (queryFilter.size()>0) {
                                for (FileFolder folder : queryFilter) {
                                    if (dbUtils.queryBuilderFileFolder(filefolder.getFile_Path())) {
                                        already_add_selected_data=true;
                                    }else {
                                        addToFilter.add(filefolder);
                                    }
                                }
                            }else {
                                addToFilter.add(filefolder);
                            }
                            mDatas.get(i).setCheck(false);
                        }
                    }
                    if (addToFilter.size()>0){
                        if (dbUtils.inserMultFileFolder(addToFilter)){
                            add_a_sucsess=true;
                        }
                    }else {
                        already_selected_data=false;
                    }
                    dbUtils.getDaoManager().closeDaoSession();
                    return null;
                }

                @Override
                protected void onPostExecute(List<FileFolder> fileFolders) {
                    dialog.dismiss();
                    if (already_selected_data){
                        if (add_a_sucsess){
                            AddClickTimes addClickTimes=new AddClickTimes(++ADD_CLICK_TIMES);
                            EventBus.getDefault().post(addClickTimes);
                            Toast.makeText(getActivity(), "成功添加", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "添加失败", Toast.LENGTH_SHORT).show();
                        }
                    }else {
                        if (already_add_selected_data){
                            Toast.makeText(getActivity(), "这些文件夹已经过滤", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(getActivity(), "请选择要过滤的文件夹", Toast.LENGTH_SHORT).show();
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                    super.onPostExecute(fileFolders);
                }
            }.execute();
        }
    }
}
