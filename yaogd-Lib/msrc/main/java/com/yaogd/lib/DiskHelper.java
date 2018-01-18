package com.yaogd.lib;

import android.os.StatFs;
import android.text.TextUtils;

import java.io.File;
import java.io.FilenameFilter;

/**
 * 下载目录管理
 */
public class DiskHelper {

    /**
     * 广告下载顶级目录
     */
    public static final String TOP_DIR = "/temp/";

    /**
     * 缓存文件的前缀名
     */
    public static final String TEMP_FILE_FIRST_NAME = "temp_";

    /**
     * 顶级文件的存放目录
     * @return 如果SDCard没有挂载，或者故障，则返回null
     */
    protected String getTopDir(){
        return TOP_DIR;
    }

    /**
     * 顶级文件的存放目录
     * @return 如果SDCard没有挂载，或者故障，则返回null
     */
    public String findTopDir(){
        File pDir = BaseApplication.getContext().getExternalFilesDir(null);
        if (pDir == null) {
            return null;
        }
        File dir = new File(pDir.getAbsolutePath() + TOP_DIR);
        if (dir == null) {
            return null;
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }
        return dir.getAbsolutePath();
    }

    /**
     * SD卡可用内存空间
     *
     * @return
     */
    public long getAvailableSpace() {
        String topDir = findTopDir();
        if (topDir != null) {
            StatFs sf = new StatFs(topDir);
            long blockSize = sf.getBlockSize();
            long availCount = sf.getAvailableBlocks();
            return availCount * blockSize;
        }
        return 0;
    }

    /**
     * 不支持断点下载的情况下，需要在恰当时候清理垃圾残留
     */
    public void clearTempFiles(){
        String topDir = findTopDir();
        if (topDir != null) {
            File dir = new File(topDir);
            File[] list = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename != null && filename.startsWith(TEMP_FILE_FIRST_NAME);
                }
            });
            if (list != null) {
                for (File f : list){
                    if (f != null) {
                        f.delete();
                    }
                }
            }
        }
    }

    /**
     * 创建临时文件
     * @param fileName
     * @return
     * @throws Exception
     */
    public File createTempFile(String fileName) throws Exception {
        return createFile(TEMP_FILE_FIRST_NAME + fileName);
    }

    /**
     * 创建文件
     * /mnt/sdcard/xxx/advert/apks/a.apk
     * @return
     */
    public File createFile(String fileName) throws Exception {
        String topDir = findTopDir();
        if (topDir != null && !TextUtils.isEmpty(fileName)) {
            File file = new File(topDir + File.separator + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
            return file;
        }
        return null;
    }

    /**
     * 删除文件
     * @param filePath
     */
    public void deleteFile(String filePath){
        if (!TextUtils.isEmpty(filePath)) {
            File file = new File(filePath);
            if (file != null) {
                file.delete();
            }
        }
    }

    /**
     * 是否存在文件
     * /mnt/sdcard/xxx/video_recorder/video_support.zip
     *
     * @return
     */
    public File[] containsFile(final String fileNamePart) {
        String topDir = findTopDir();
        if (topDir != null && !TextUtils.isEmpty(fileNamePart)) {
            File dir = new File(topDir);
            File[] list = dir.listFiles(new FilenameFilter() {
                @Override
                public boolean accept(File dir, String filename) {
                    return filename != null && filename.contains(fileNamePart) && !filename.startsWith(TEMP_FILE_FIRST_NAME);
                }
            });
            return list;
        }
        return null;
    }

    /**
     * 重命名文件
     * @return 返回新地址
     */
    public String renameFile(File oldFile, String newFileName) {
        if (oldFile != null && oldFile.exists() && !TextUtils.isEmpty(newFileName)) {
            String oldFilePath = oldFile.getAbsolutePath();
            String dir = oldFilePath.substring(0, oldFilePath.lastIndexOf(File.separator) + 1);
            File newFile = new File(dir + newFileName);
            if (newFile.exists()) {
                return newFile.getAbsolutePath();
            }
            //如果文件名称存在非法字符，则重命名失败
            if (oldFile.renameTo(newFile)){
                return newFile.getAbsolutePath();
            }
        }
        return null;
    }

}