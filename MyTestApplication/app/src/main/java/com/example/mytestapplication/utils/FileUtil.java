package com.example.mytestapplication.utils;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.nio.channels.FileChannel;

/**
 * Created by 张艳 on 2016/5/19.
 */
public class FileUtil {

    public static void copyDBToSDcard(){
        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                String currentDBPath = "//data//com.aspirecn.xiaoxuntongTeacher.tj//databases//microschool_v1.db";
                String backupDBPath = "zytjtest1.db";
                File currentDB = new File(data, currentDBPath);
                File backupDB = new File(sd, backupDBPath);

                if (currentDB.exists()) {
                    FileChannel src = new FileInputStream(currentDB).getChannel();
                    FileChannel dst = new FileOutputStream(backupDB).getChannel();
                    dst.transferFrom(src, 0, src.size());
                    src.close();
                    dst.close();
                    Log.e("zy","copy success!");
                }else{
                    Log.e("zy","currentDB not exist");
                }
            }
        } catch (Exception e) {
        }
    }
    public static void copyDBToSDcrad()
    {
        String DATABASE_NAME = "microschool_v2.db";

        String oldPath = "data/data/com.aspirecn.xiaoxuntongParent.tj/databases/" + DATABASE_NAME;
        String newPath = Environment.getExternalStorageDirectory() + File.separator + DATABASE_NAME;

        copyFile(oldPath, newPath);
        Log.e("zy","拷贝成功");
    }

    /**
     * 复制单个文件
     *
     * @param oldPath
     *            String 原文件路径
     * @param newPath
     *            String 复制后路径
     * @return boolean
     */
    public static void copyFile(String oldPath, String newPath)
    {
        try
        {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            File newfile = new File(newPath);
            if (!newfile.exists())
            {
                newfile.createNewFile();
            }
            if (oldfile.exists())
            { // 文件存在时
                InputStream inStream = new FileInputStream(oldPath); // 读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                while ((byteread = inStream.read(buffer)) != -1)
                {
                    bytesum += byteread; // 字节数 文件大小
                    fs.write(buffer, 0, byteread);
                    Log.e("zy","bytesum="+bytesum);
                }
                inStream.close();
            }else{
                Log.e("zy","oldfile does not exist");
            }
        }
        catch (Exception e)
        {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();
        }

    }
}
