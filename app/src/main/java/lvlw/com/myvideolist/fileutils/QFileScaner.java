package lvlw.com.myvideolist.fileutils;

import android.media.MediaMetadataRetriever;
import android.util.Log;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.entity.QFileInfo;


/**
 * @author qfc
 */
public final class QFileScaner {


    /**
     * 文件后缀名过滤器
     */
    private List<String> _extFilter;

    /**
     * 过滤的文件文件夹
     */
    private List<String> filterfolder;

    /**
     * 存储扫描的文件
     */
    private List<QFileInfo> _resultFiles;

    public List<QFileInfo> get_resultFiles() {
        return _resultFiles;
    }


    /**
     * constructor function
     */
    public QFileScaner(List<String> filterfolder) {
        // TODO Auto-generated constructor stub
        _extFilter = new ArrayList<String>();
        _resultFiles = new ArrayList<QFileInfo>();
        this.filterfolder=filterfolder;
        InitVedioExtFilter();
//        filterfolder.add("tencent");
//        filterfolder.add("Android");
    }

    public void Start(File file, final MediaMetadataRetriever mMetadataRetriever) {


        if (file.isDirectory()) {
            boolean contain = false;
            for (String s : filterfolder) {
                if (file.getPath().equals(s)) {
                    contain = true;
                    break;
                }
            }
            if (!contain) {
                // Note:file为扫描目录
                file.listFiles(new FileFilter() {
                    @Override
                    public boolean accept(File file) {

                        if (file.isFile()) {// NOTE: 文件

                            // TODO：判断是否扫描到了文件
                            String name = file.getName();
                            //					int i = name.indexOf('.');
                            int i = name.lastIndexOf('.');

                            Log.i("SCANFILE", name);

                            // TODO：执行文件过滤
                            boolean isAccept = false;
                            String ext = null;
                            if (i == -1) {
                                isAccept = false;
                            } else {
                                ext = name.substring(i);
                                if (_extFilter.size() == 0) { // NOTE:不过滤
                                    isAccept = false;

                                } else { // NOTE:过滤
                                    if (_extFilter.contains(ext.toLowerCase(Locale.CHINA))) {
                                        isAccept = true;
                                    }
                                }
                            }

                            if (isAccept) { //NOTE:扫描到了文件
                                if (file.length() > 10485760) {
                                    QFileInfo info = new QFileInfo();
                                    info.set_fileIcon(R.mipmap.mp4);
                                    info.set_fileName(file.getName());
                                    info.set_filePath(file.getAbsolutePath());
                                    info.set_fileExt(ext);
                                    info.set_fileSize(file.length() / 1024 / 1024 + "MB");
                                    mMetadataRetriever.setDataSource(file.getAbsolutePath());
                                    info.set_fileDesc(secToTime(Integer.parseInt(mMetadataRetriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION)) / 1000));
                                    _resultFiles.add(info);
                                }
                            }

                            return isAccept;
                        } else if (file.isDirectory()) {
                            boolean contain = false;
                            for (String s : filterfolder) {
                                if (file.getPath().equals(s)) {
                                    contain = true;
                                    break;
                                }
                            }
                            if (contain) {
                                return false;
                            } else {
                                Start(file, mMetadataRetriever);
                            }
                        }
                        return false;
                    }
                });
            }
        }
    }

    private boolean checkFilterDir(File file,List<String> filter){
        boolean exist=false;
        for (String s : filter) {
            if (file.getPath().equals(s)){
                exist=true;
                break;
            }
        }
        return exist;
    }
    // a integer to xx:xx:xx
    private  String secToTime(int time) {
        String timeStr = null;
        int hour = 0;
        int minute = 0;
        int second = 0;
        if (time <= 0)
            return "00:00";
        else {
            minute = time / 60;
            if (minute < 60) {
                second = time % 60;
                timeStr = unitFormat(minute) + ":" + unitFormat(second);
            } else {
                hour = minute / 60;
                if (hour > 99)
                    return "99:59:59";
                minute = minute % 60;
                second = time - hour * 3600 - minute * 60;
                timeStr = unitFormat(hour) + ":" + unitFormat(minute) + ":" + unitFormat(second);
            }
        }
        return timeStr;
    }

    private String unitFormat(int i) {
        String retStr = null;
        if (i >= 0 && i < 10)
            retStr = "0" + Integer.toString(i);
        else
            retStr = "" + i;
        return retStr;
    }

    /**
     * 初始化视频文件后缀名过滤器
     */
    private void InitVedioExtFilter() {
        _extFilter.clear();
        _extFilter.add(".mp4");
        _extFilter.add(".3gp");
        _extFilter.add(".wmv");
        _extFilter.add(".ts");
        _extFilter.add(".rmvb");
        _extFilter.add(".mov");
        _extFilter.add(".m4v");
        _extFilter.add(".avi");
        _extFilter.add(".m3u8");
        _extFilter.add(".3gpp");
        _extFilter.add(".3gpp2");
        _extFilter.add(".mkv");
        _extFilter.add(".flv");
        _extFilter.add(".divx");
        _extFilter.add(".f4v");
        _extFilter.add(".rm");
        _extFilter.add(".asf");
        _extFilter.add(".ram");
        _extFilter.add(".mpg");
        _extFilter.add(".v8");
        _extFilter.add(".swf");
        _extFilter.add(".m2v");
        _extFilter.add(".asx");
        _extFilter.add(".ra");
        _extFilter.add(".ndivx");
        _extFilter.add(".xvid");
        _extFilter.add(".blv");

    }

}
