package lvlw.com.myvideolist.adapter;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;
import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.entity.QFileInfo;
import lvlw.com.myvideolist.fileutils.FileDirScaner;
import lvlw.com.myvideolist.ijkplayer.IjkFullscreenActivity;
import lvlw.com.myvideolist.utils.MyComparator;


public class MyAdapter extends BaseAdapter {

    private Context mContext;

    private List<QFileInfo> mDatas;

    public List<QFileInfo> getmDatas() {
        return mDatas;
    }

    private LayoutInflater mInflater;

    public boolean flage = false;
    public boolean isAdd = false;

    public Map<Integer, String> selected;

    private TextView fileparent;


    private Activity mainActivity;

    public MyAdapter(Context mContext, List<QFileInfo> mDatas,TextView fileparent,Activity mainActivity) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        this.fileparent=fileparent;
        this.mainActivity = mainActivity;
        mInflater = LayoutInflater.from(this.mContext);
        selected = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int i) {
        return mDatas.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {

        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.filedir_item, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final QFileInfo fileInfo = mDatas.get(position);
        if (fileInfo != null) {
            holder.ivFileIcon.setImageResource(fileInfo.get_fileIcon());
            holder.tvFileName.setText(fileInfo.get_fileName());
            holder.tvFileMsg.setText(fileInfo.get_fileDesc());

            // 根据isSelected来设置checkbox的显示状况
            if (flage) {
                holder.ibtnFileOperate.setVisibility(View.VISIBLE);
            } else {
                holder.ibtnFileOperate.setVisibility(View.GONE);
            }

            if (selected.containsKey(position))

                holder.ibtnFileOperate.setChecked(true);
            else
                holder.ibtnFileOperate.setChecked(false);

            holder.ibtnFileOperate.setChecked(fileInfo.isCheck());

            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            holder.ibtnFileOperate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileInfo.isCheck()) {
                        fileInfo.setCheck(false);
                    } else {
                        fileInfo.setCheck(true);
                    }
                }
            });
            if (fileparent!=null){
                holder.ivFileIcon.setOnClickListener(new InFileDironClickListener(mContext,fileInfo.get_filePath(),position));
                holder.fileDetial.setOnClickListener(new InFileDironClickListener(mContext,fileInfo.get_filePath(),position));
            }else {
//                ArrayList<String> videopath=new ArrayList<String>();
//                videopath.add(fileInfo.get_filePath());
                if (isAdd){
                    holder.ivFileIcon.setOnClickListener(new AddonClickListener(position));
                    holder.fileDetial.setOnClickListener(new AddonClickListener(position));
                }else {
                    holder.ivFileIcon.setOnClickListener(new PlayeronClickListener(mainActivity,fileInfo));
                    holder.fileDetial.setOnClickListener(new PlayeronClickListener(mainActivity,fileInfo));
                }
            }
        }

        return convertView;
    }


    class ViewHolder {
        @InjectView(R.id.iv_file_icon)
        ImageView ivFileIcon;
        @InjectView(R.id.tv_file_name)
        TextView tvFileName;
        @InjectView(R.id.tv_file_msg)
        TextView tvFileMsg;
        @InjectView(R.id.file_detial)
        LinearLayout fileDetial;
        @InjectView(R.id.ibtn_file_operate)
        CheckBox ibtnFileOperate;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private class AddonClickListener implements View.OnClickListener{

        private int positon;

        private AddonClickListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onClick(View v) {
            if (mDatas.get(positon).isCheck()){
                mDatas.get(positon).setCheck(false);
            }else {
                mDatas.get(positon).setCheck(true);
            }
            notifyDataSetChanged();
        }
    }
    private class PlayeronClickListener implements View.OnClickListener{
        private Activity fromActivity;

        public PlayeronClickListener(Activity fromActivity, QFileInfo videoInfo) {
            this.fromActivity = fromActivity;
            this.videoInfo = videoInfo;
        }

        private QFileInfo videoInfo;

        @Override
        public void onClick(View v) {
            Intent intent = new Intent(fromActivity, IjkFullscreenActivity.class);
            Bundle bundle=new Bundle();
            Gson gson=new Gson();
            bundle.putString("playlist",gson.toJson(videoInfo));
            intent.putExtras(bundle);
            fromActivity.startActivity(intent);
        }
    }
    private class InFileDironClickListener implements View.OnClickListener{

        private Context context;
        private String filepath;
        private int positon;

        public InFileDironClickListener(Context context,String filepath,int positon) {
            this.context = context;
            this.filepath=filepath;
            this.positon=positon;
        }

        @Override
        public void onClick(View v) {
            new AsyncTask<Context,Integer,List<QFileInfo>>(){
                List<QFileInfo> fileInfos=new ArrayList<QFileInfo>();
                private ProgressDialog dialog;
                @Override
                protected void onPreExecute() {
                    dialog = ProgressDialog.show(context, "",
                            "正在扫描SD卡,请稍候....");
                    super.onPreExecute();
                }

                @Override
                protected List<QFileInfo> doInBackground(Context... params) {
                    FileDirScaner scaner=new FileDirScaner();
                    scaner.Start(new File(filepath));
                    fileInfos=scaner.get_resultFiles();
                    String[] sortBy = new String[] { "_fileName" };
                    int orderBy = 1;//1:升序，-1：降序
                    MyComparator myCmp = new MyComparator(sortBy, orderBy);
                    Collections.sort(fileInfos, myCmp);
                    return null;
                }

                @Override
                protected void onPostExecute(List<QFileInfo> qFileInfos) {
                    dialog.dismiss();
                    // TODO: create test data
                    if (fileInfos.size() >=0) {
                        if (fileInfos.size()==0){
//                            Toast.makeText(context,"没有文件夹",Toast.LENGTH_SHORT).show();
                            if (mDatas.get(positon).isCheck()){
                                mDatas.get(positon).setCheck(false);
                            }else {
                                mDatas.get(positon).setCheck(true);
                            }
                            notifyDataSetChanged();
                        }else {
                            fileparent.setText(filepath);
                            mDatas=fileInfos;
                            notifyDataSetChanged();
                        }
                    }
                    super.onPostExecute(qFileInfos);
                }
            }.execute();
        }
    }
}
