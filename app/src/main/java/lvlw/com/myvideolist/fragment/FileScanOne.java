package lvlw.com.myvideolist.fragment;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.VideoInfo.entity.FileFolder;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.adapter.ScanSetAdapter;
import lvlw.com.myvideolist.greendao.CommonUtils;

/**
 * Created by Wantrer on 2017/5/1 0001.
 */

public class FileScanOne extends Fragment {
    @InjectView(R.id.filter_folder)
    ListView filterFolder;
    @InjectView(R.id.remove_filter_folder)
    LinearLayout removeFilterFolder;

    private List<FileFolder> mDatas;
    private CommonUtils dbUtils;
    private final String dbName="filefolder.db";
    private ScanSetAdapter mAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = LayoutInflater.from(getActivity()).inflate(R.layout.filescan_one_frg, null);
        ButterKnife.inject(this, rootView);
        Handler ha = new Handler();
        initData();
        ha.postDelayed(new Runnable() {
            @Override
            public void run() {
                initView();
            }
        }, 10);
        return rootView;
    }

    public void initView() {
        removeFilterFolder.setOnClickListener(new RemoveSharedPrefrenceonClickListener());
        mAdapter=new ScanSetAdapter(getActivity(),mDatas);
        filterFolder.setAdapter(mAdapter);
    }

    public void initData() {
        mDatas = new ArrayList<>();
        dbUtils = new CommonUtils(getActivity(),dbName);
        mDatas = dbUtils.listAllFileFolder();
        dbUtils.getDaoManager().closeDaoSession();
        //                dbUtils.getDaoManager().closeHelper();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.reset(this);
    }


    public class RemoveSharedPrefrenceonClickListener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            mAdapter= (ScanSetAdapter) filterFolder.getAdapter();
            mDatas=mAdapter.getmDatas();
            if (mDatas.size()>0){
                new AsyncTask<String,String,List<FileFolder>>(){
                    private boolean delete_a_sucsess=false;
                    private boolean already_selected_data=true;
                    private ProgressDialog dialog;
                    @Override
                    protected void onPreExecute() {
                        dialog = ProgressDialog.show(getActivity(), "",
                                "正在添加,请稍候....");
                        super.onPreExecute();
                    }

                    @Override
                    protected List<FileFolder> doInBackground(String... params) {
                        List<FileFolder> deletedFilter=new ArrayList<>();
                        dbUtils=new CommonUtils(getActivity(),dbName);
                        for (int i = 0; i < mDatas.size(); i++) {
                            if (mDatas.get(i).getFile_check()){
                                deletedFilter.add(mDatas.get(i));
                            }
                        }
                        if (deletedFilter.size()>0){
                            for (FileFolder fileFolder : deletedFilter) {
                                delete_a_sucsess=dbUtils.deleteFileFolder(fileFolder);
                                if (!delete_a_sucsess){
                                    break;
                                }
                                mDatas.remove(fileFolder);
                            }
                        }else {
                            already_selected_data=false;
                        }

                        return null;
                    }

                    @Override
                    protected void onPostExecute(List<FileFolder> fileFolders) {
                        dialog.dismiss();
                        if (already_selected_data){
                            if (delete_a_sucsess){
                                Toast.makeText(getActivity(), "成功移除", Toast.LENGTH_SHORT).show();
                            }else {
                                Toast.makeText(getActivity(), "移除失败", Toast.LENGTH_SHORT).show();
                            }
                            mAdapter.notifyDataSetChanged();
                        }else {
                            Toast.makeText(getActivity(), "请选择要移除的文件夹", Toast.LENGTH_SHORT).show();
                        }
                        super.onPostExecute(fileFolders);
                    }
                }.execute();
            }else {
                Toast.makeText(getActivity(), "没有文件夹", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
