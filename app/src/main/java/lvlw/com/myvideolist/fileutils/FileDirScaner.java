package lvlw.com.myvideolist.fileutils;

import android.os.Environment;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

import lvlw.com.myvideolist.R;
import lvlw.com.myvideolist.entity.QFileInfo;

/**
 * Created by Wantrer on 2017/4/22 0022.
 */

public class FileDirScaner {

    /**
     * 过滤的文件文件夹
     */
    private List<String> filterfolder;

    /**
     * 存储扫描到的文件夹
     */
    private List<QFileInfo> resultFiles;

    private String s1="/.";

    public List<QFileInfo> get_resultFiles() {
        return resultFiles;
    }

    public FileDirScaner() {
        filterfolder=new ArrayList<>();
//        filterfolder.add(Environment.getExternalStorageDirectory().getPath()+"/TWRP");
//        filterfolder.add("Android");
        resultFiles =new ArrayList<>();
    }

    public void Start(File file){
        file.listFiles(new FileFilter() {
            @Override
            public boolean accept(File pathname) {
                if (pathname.isDirectory()){
                    if (!checkFilterDir(pathname,filterfolder)){
                        QFileInfo fileinfo=new QFileInfo();
                        fileinfo.set_fileIcon(R.mipmap.folder);
                        fileinfo.set_fileName(pathname.getName());
                        fileinfo.set_fileDesc(getHasNext(pathname)+"  项");
                        fileinfo.set_filePath(pathname.getPath());
                        resultFiles.add(fileinfo);
                        return true;
                    }
                }else {
                    return false;
                }
                return false;
            }
        });
//        if (file.isDirectory()){
//            if (!checkFilterDir(file,filterfolder)){
//                for (int i = 0; i < file.listFiles().length; i++) {
//                    if (file.listFiles()[i].isDirectory()){
//                        QFileInfo fileinfo=new QFileInfo();
//                        fileinfo.set_fileIcon(R.mipmap.folder);
//                        fileinfo.set_fileName(file.listFiles()[i].getName());
//                        fileinfo.set_fileDesc(getHasNext(file.listFiles()[i])+"  项");
//                        fileinfo.set_filePath(file.listFiles()[i].getPath());
//                        resultFiles.add(fileinfo);
//                    }
//                }
//            }
//        }
    }

    private int getHasNext(File file){
        int hasnext=0;
        for (File file1 : file.listFiles()) {
            if (file1.isDirectory()){
                hasnext++;
            }
        }
        return hasnext;
    }
    private boolean checkFilterDir(File file,List<String> filter){
        boolean exist=false;
        if (file.getPath().contains(s1)) {
            exist = true;
        }else {
            for (String s : filter) {
                if (file.getPath().equals(s)){
                    exist=true;
                    break;
                }
            }
        }
        return exist;
    }
}
