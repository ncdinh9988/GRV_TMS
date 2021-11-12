package com.FiveSGroup.TMS;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class FileUtils {



    //hàm lấy tất cả các tên file trong thư mục
    public static String[] getListFileName(String folderPath)
    {
        File filePath = new File(folderPath);
        String listfile="";
        if (!filePath.exists()) {
            return null;
        }
        File[] files = filePath.listFiles();
        for (File file : files){
            listfile+=file.getPath() + ",";
        }
        if(!listfile.equals(""))
        {
            listfile = listfile.substring(0,listfile.lastIndexOf(","));
            return listfile.split(",");
        }
        return null;
    }


    //hàm ghi dữ liệu vào file txt
    public static void writefile(String folder,String filename,String content)
    {
        FileUtils.writefile(folder, filename, content,false);
    }


    //hàm ghi dữ liệu vào file txt
    public static void writefile(String folder,String filename,String content,Boolean deleteFile)
    {

        File f = new File(folder, filename);
        try {

            // TODO Auto-generated method stub
            if(!f.exists()){
                f = FileUtils.CreateFile(folder,filename);
            }

            FileOutputStream data = new FileOutputStream(f, false);
            OutputStreamWriter writer = new OutputStreamWriter(data, "UTF-8");
            try {
                writer.append(String.valueOf(content) +	"\r\n");
                writer.flush();
                writer.close();
                data.close();
            } catch (FileNotFoundException e) {
                Log.i("FileUtils",e.getMessage());
            } catch (IOException e) {
                Log.i("FileUtils",e.getMessage());
            }
        } catch (Exception ex) {
            Log.i("FileUtils",ex.getMessage());
        }
    }


    //hàm xóa file
    public static void deleteFile(String folderName, String filename) {
        try {

            String path = Environment.getExternalStorageDirectory()
                    + File.separator + folderName + File.separator + filename; // folder name
            File filePath = new File(path);

            if(!filePath.exists())
                return;

            if (filePath.exists())
            {
                filePath.delete();
            }

        } catch (Exception e) {
            // TODO: handle exception

        }

    }

    //hàm xóa thư mục
    public static void deleteFolder(String folderName) {
        try {

            File dir = new File(Environment.getExternalStorageDirectory()+  File.separator  + folderName);
            if (dir.isDirectory()) {
                String[] children = dir.list();
                for (int i = 0; i < children.length; i++) {
                    new File(dir, children[i]).delete();
                }
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    //tạo file
    public static File CreateFile(String folderName, String filename) {
        try {
            String path = Environment.getExternalStorageDirectory()
                    + File.separator + folderName; // folder name

            File filePath = new File(path);

            if (!filePath.exists()) {
                filePath.mkdirs();
            }

            File f = new File(path, filename);
            try {
                if (!f.exists()) {
                    f.createNewFile();
                }

                return f;
            } catch (Exception exp) {

                Log.i("FileUtils",exp.getMessage());
            }
            return null;
        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    //hàm ghi dữ liệu vào file, xóa các dữ liệu đã ghi trước đó trong file
    public static void writefileNotAppend(String folder,String filename,String content)
    {
        // TODO Auto-generated method stub

        try {
            File f = FileUtils.CreateFile(folder,filename);
            // WRITE EXIST FILE
            FileOutputStream data = new FileOutputStream(f, false);
            OutputStreamWriter writer = new OutputStreamWriter(data, "UTF-8");
            try {
                writer.write(String.valueOf(
                        content));
                writer.flush();
                writer.close();
                data.close();

            } catch (FileNotFoundException e) {
                Log.i("FileUtils",e.getMessage());
            } catch (IOException e) {
                Log.i("FileUtils",e.getMessage());
            }
        } catch (Exception ex) {
            Log.i("FileUtils",ex.getMessage());
        }
    }


    public static byte[] ByteArrayCopy(byte[] source,long beginIndex,long endIndex)
    {
        try {
            byte[] temp = new byte[(int)endIndex];
            System.arraycopy(source,(int)beginIndex, temp, 0, temp.length);
            return temp;

        } catch (Exception e) {
            // TODO: handle exception
            return null;
        }

    }

    //hàm copy file
    public static boolean copyFile(File source, File dest) {

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;
        try {
            bis = new BufferedInputStream(new FileInputStream(source));
            bos = new BufferedOutputStream(new FileOutputStream(dest, false));

            byte[] buf = new byte[bis.available()];
            bis.read(buf);

            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            return false;
        } finally {
            try {
                if (bis != null)
                    bis.close();
                if (bos != null)
                    bos.close();
            } catch (IOException e) {
                Log.i("ConnectFPTServer",e.getMessage());
                return false;
            }
        }

        return true;
    }

    // WARNING ! Inefficient if source and dest are on the same filesystem !
    public static boolean moveFile(File source, File dest) {
        try {
            return copyFile(source, dest) && source.delete();
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }

    // Returns true if the sdcard is mounted rw
    public static boolean isSDMounted() {
        try {
            return Environment.getExternalStorageState().equals(
                    Environment.MEDIA_MOUNTED);
        } catch (Exception e) {
            // TODO: handle exception
            return false;
        }

    }

}
