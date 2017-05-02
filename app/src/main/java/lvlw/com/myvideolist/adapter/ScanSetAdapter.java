package lvlw.com.myvideolist.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.VideoInfo.entity.FileFolder;

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
import lvlw.com.myvideolist.utils.MyComparator;

/**
 * Created by Wantrer on 2017/5/2 0002.
 */

public class ScanSetAdapter extends BaseAdapter {
    private Context mContext;

    private List<FileFolder> mDatas;

    public List<FileFolder> getmDatas() {
        return mDatas;
    }

    private LayoutInflater mInflater;

    public boolean flage = false;

    public Map<Integer, String> selected;


    public ScanSetAdapter(Context mContext, List<FileFolder> mDatas) {
        this.mContext = mContext;
        this.mDatas = mDatas;
        mInflater = LayoutInflater.from(this.mContext);
        selected = new HashMap<>();
    }

    @Override
    public int getCount() {
        return mDatas.size();
    }

    @Override
    public Object getItem(int position) {
        return mDatas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;

        if (convertView == null) {
            // 下拉项布局
            convertView = mInflater.inflate(R.layout.filescan_item, null);

            holder = new ViewHolder(convertView);
            convertView.setTag(holder);

        } else {

            holder = (ViewHolder) convertView.getTag();
        }

        final FileFolder fileFolder = mDatas.get(position);
        if (fileFolder != null) {
            holder.scanFileIcon.setImageResource(R.mipmap.folder);
            holder.scanTvFileName.setText(fileFolder.getFile_Name());

            // 根据isSelected来设置checkbox的显示状况
//            if (flage) {
//                holder.ibtnFileCheck.setVisibility(View.VISIBLE);
//            } else {
//                holder.ibtnFileCheck.setVisibility(View.GONE);
//            }

            if (selected.containsKey(position))

                holder.ibtnFileCheck.setChecked(true);
            else
                holder.ibtnFileCheck.setChecked(false);

            holder.ibtnFileCheck.setChecked(fileFolder.getFile_check());

            //注意这里设置的不是onCheckedChangListener，还是值得思考一下的
            holder.ibtnFileCheck.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (fileFolder.getFile_check()) {
                        fileFolder.setFile_check(false);
                    } else {
                        fileFolder.setFile_check(true);
                    }
                }
            });
            holder.scanFileIcon.setOnClickListener(new CheckonClickListener(position));
            holder.scanTvFileName.setOnClickListener(new CheckonClickListener(position));
        }

        return convertView;
    }

    static class ViewHolder {
        @InjectView(R.id.ibtn_file_check)
        CheckBox ibtnFileCheck;
        @InjectView(R.id.scan_file_icon)
        ImageView scanFileIcon;
        @InjectView(R.id.scan_tv_file_name)
        TextView scanTvFileName;

        ViewHolder(View view) {
            ButterKnife.inject(this, view);
        }
    }

    private class CheckonClickListener implements View.OnClickListener{

        private int positon;

        private CheckonClickListener(int positon) {
            this.positon = positon;
        }

        @Override
        public void onClick(View v) {
            if (mDatas.get(positon).getFile_check()){
                mDatas.get(positon).setFile_check(false);
            }else {
                mDatas.get(positon).setFile_check(true);
            }
            notifyDataSetChanged();
        }
    }
}
