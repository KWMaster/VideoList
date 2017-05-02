package com.example;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

/**
 * Created by Wantrer on 2017/4/24 0024.
 */

public class DaoMaker {
    public static void main(String[] args) {
        //生成数据库的实体类,还有版本号
        Schema schema = new Schema(1, "com.VideoInfo.entity");
        addStudent(schema);
        //指定dao
        schema.setDefaultJavaPackageDao("com.VideoInfo.dao");
        try {
            //指定路径
            new DaoGenerator().generateAll(schema, "D:\\AndroidStudioProjects\\MyVideoList\\app\\src\\main\\java-gen");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 创建数据库的表
     *
     * @param schema
     */
    public static void addStudent(Schema schema) {
        //创建数据库的表
        Entity entity = schema.addEntity("VideoInfo");
        //主键 是int类型
        entity.addIdProperty();
        //视频名称
        entity.addStringProperty("file_Name");
        //视频大小
        entity.addStringProperty("file_Size");
        //视频文件路径
        entity.addStringProperty("file_Path");
        //视频持续时间
        entity.addStringProperty("file_Duration");
        //视频扩展（格式）
        entity.addStringProperty("file_Extention");
//        entity.addBooleanProperty("file_check");
        Entity filefolder = schema.addEntity("FileFolder");
        filefolder.addIdProperty();
        filefolder.addStringProperty("file_Name");
        filefolder.addStringProperty("file_Path");
        filefolder.addBooleanProperty("file_check");
    }
}
