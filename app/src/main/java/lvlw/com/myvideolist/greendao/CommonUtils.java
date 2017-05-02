package lvlw.com.myvideolist.greendao;

import android.content.Context;
import android.util.Log;

import com.VideoInfo.dao.FileFolderDao;
import com.VideoInfo.dao.VideoInfoDao;
import com.VideoInfo.entity.FileFolder;
import com.VideoInfo.entity.VideoInfo;

import java.util.List;

import de.greenrobot.dao.query.QueryBuilder;

/**
 * Created by Wantrer on 2017/4/24 0024.
 */

public class CommonUtils {
    //TAG
    private static final String TAG = CommonUtils.class.getSimpleName();

    public DaoManager getDaoManager() {
        return daoManager;
    }

    private DaoManager daoManager;
    //构造方法
    public CommonUtils(Context context,String DB_NAME) {
        daoManager = DaoManager.getInstance();
        daoManager.initManager(context,DB_NAME);
        //查询构建器

    }

    /**
     * 对数据库中student表的插入操作
     *
     * @param videoInfo
     * @return
     */
    public boolean insertStudent(VideoInfo videoInfo) {
        boolean flag = false;
        flag = daoManager.getDaoSession().insert(videoInfo) != -1 ? true : false;
        return flag;
    }

    /**
     * 批量插入
     *
     * @param videoInfos
     * @return
     */
    public boolean inserMultStudent(final List<VideoInfo> videoInfos) {
        //标识
        boolean flag = false;
        try {
            //插入操作耗时
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (VideoInfo videoInfo : videoInfos) {
                        daoManager.getDaoSession().insertOrReplace(videoInfo);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 修改
     *
     * @param videoInfo
     * @return
     */
    public boolean updateStudent(VideoInfo videoInfo) {
        boolean flag = false;
        try {
            daoManager.getDaoSession().update(videoInfo);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除
     *
     * @param videoInfo
     * @return
     */
    public boolean deleteStudent(VideoInfo videoInfo) {
        boolean flag = false;
        try {
            //删除指定ID
            daoManager.getDaoSession().delete(videoInfo);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //daoManager.getDaoSession().deleteAll(); //删除所有记录
        return flag;
    }

    public void deleteStudentAll(){
        daoManager.getDaoSession().deleteAll(VideoInfo.class);
    }

    /**
     * 查询单条
     *
     * @param key
     * @return
     */
    public VideoInfo listOneStudent(long key) {
        return daoManager.getDaoSession().load(VideoInfo.class, key);
    }

    /**
     * 全部查询
     *
     * @return
     */
    public List<VideoInfo> listAll() {
//        return daoManager.getDaoSession().loadAll(VideoInfo.class);
        QueryBuilder<VideoInfo> queryBuilder = daoManager.getDaoSession().queryBuilder(VideoInfo.class);
        return queryBuilder.where(VideoInfoDao.Properties.Id.isNotNull()).orderAsc(VideoInfoDao.Properties.File_Name).list();
//        return daoManager.getDaoSession().loadAll(VideoInfo.class);
    }

    /**
     * 原生查询
     */
    public void queryNative() {
        //查询条件
        String where = "where file_Extention like ? and file_Size > ?";
        //使用sql进行查询
        List<VideoInfo> list = daoManager.getDaoSession().queryRaw(VideoInfo.class, where,
                new String[]{"%mp4%", "10"});
        Log.i(TAG, list + "");
    }

    /**
     * QueryBuilder查询大于
     */
    public boolean queryBuilder(String filepath) {
        //查询年龄大于19的北京
        QueryBuilder<VideoInfo> queryBuilder = daoManager.getDaoSession().queryBuilder(VideoInfo.class);
        List<VideoInfo> list = queryBuilder.where(VideoInfoDao.Properties.File_Path.eq(filepath)).list();
        if (list.size()>0){
            return true;
        }
        Log.i(TAG, list + "");
        return false;
    }

    /**
     * 批量插入
     *
     * @param fileFolders
     * @return
     */
    public boolean inserMultFileFolder(final List<FileFolder> fileFolders) {
        //标识
        boolean flag = false;
        try {
            //插入操作耗时
            daoManager.getDaoSession().runInTx(new Runnable() {
                @Override
                public void run() {
                    for (FileFolder fileFolder : fileFolders) {
                        daoManager.getDaoSession().insertOrReplace(fileFolder);
                    }
                }
            });
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return flag;
    }

    /**
     * 删除
     *
     * @param fileFolder
     * @return
     */
    public boolean deleteFileFolder(FileFolder fileFolder) {
        boolean flag = false;
        try {
            //删除指定ID
            daoManager.getDaoSession().delete(fileFolder);
            flag = true;
        } catch (Exception e) {
            e.printStackTrace();
        }
        //daoManager.getDaoSession().deleteAll(); //删除所有记录
        return flag;
    }

    /**
     * 全部查询
     *
     * @return
     */
    public List<FileFolder> listAllFileFolder() {
        //        return daoManager.getDaoSession().loadAll(VideoInfo.class);
        QueryBuilder<FileFolder> queryBuilder = daoManager.getDaoSession().queryBuilder(FileFolder.class);
        return queryBuilder.where(FileFolderDao.Properties.Id.isNotNull()).orderAsc(FileFolderDao.Properties.File_Name).list();
        //        return daoManager.getDaoSession().loadAll(VideoInfo.class);
    }

    /**
     * QueryBuilder查询是否存在某条记录
     */
    public boolean queryBuilderFileFolder(String filepath) {
        QueryBuilder<FileFolder> queryBuilder = daoManager.getDaoSession().queryBuilder(FileFolder.class);
        List<FileFolder> list = queryBuilder.where(FileFolderDao.Properties.File_Path.eq(filepath)).list();
        if (list.size()>0){
            return true;
        }
        Log.i(TAG, list + "");
        return false;
    }
}
