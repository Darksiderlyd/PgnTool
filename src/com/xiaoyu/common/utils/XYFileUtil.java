package com.xiaoyu.common.utils;

import java.io.*;
import java.nio.channels.FileChannel;
import java.util.Enumeration;
import java.util.List;
import java.util.zip.*;
/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public class XYFileUtil {

    public static File byteToFile(String filePath, String fileName, byte[] data){
        File fileDir = new File(filePath);
        if (!fileDir.exists() && !fileDir.isDirectory()) {
            fileDir.mkdirs();
        }

        File file = new File(filePath, fileName);
        try {
            if (!file.exists()) {
                file.createNewFile();
            }
            FileOutputStream outputStream = new FileOutputStream(file, true);
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return file;
    }

    /**
     * 压缩文件
     *
     * @param ConfigTAG
     * @param file       源文件
     * @param gzFileName 目标文件路径
     */
    public static void zipFile(String ConfigTAG, File file, String gzFileName) {
        try {
            FileInputStream in = new FileInputStream(file);
            GZIPOutputStream out = new GZIPOutputStream(new FileOutputStream(gzFileName));
            byte[] buf = new byte[1024];
            int len = 0;
            while ((len = in.read(buf)) != -1) {
                out.write(buf, 0, len);
            }
            in.close();
            out.flush();
            out.close();
        } catch (Exception ex) {
            System.out.println(ConfigTAG + ex.toString());
        }
    }

    /**
     * 压缩文件
     *
     * @param ConfigTAG
     * @param files      源文件列表
     * @param gzFileName 目标文件路径
     */
    public static void zipFils(String ConfigTAG, List<File> files, String gzFileName) {
        if (files == null) return;
        File file = new File(gzFileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            byte[] buffer = new byte[1024];
            FileOutputStream fos = new FileOutputStream(gzFileName);
            ZipOutputStream zos = new ZipOutputStream(fos);
            for (int i = 0; i < files.size(); i++) {
                FileInputStream fis = new FileInputStream(files.get(i));
                zos.putNextEntry(new ZipEntry(files.get(i).getName()));
                int length;
                while ((length = fis.read(buffer)) > 0) {
                    zos.write(buffer, 0, length);
                }
                zos.closeEntry();
                fis.close();
            }
            zos.close();
        } catch (IOException ioe) {
            System.out.println(ConfigTAG + ioe.toString());
        }
    }

    /**
     * 解压缩
     *
     * @param zipFilePath 需要解压缩的文件
     * @param descDir     解压后的目标目录
     */
    public static void unZipFiles(String zipFilePath, String descDir) {
        try {
            File file = new File(zipFilePath);
            if (!file.exists()) return;
            ZipFile zf = new ZipFile(file);
            for (Enumeration entries = zf.entries(); entries.hasMoreElements(); ) {
                ZipEntry entry = (ZipEntry) entries.nextElement();
                String zipEntryName = entry.getName();
                InputStream in = zf.getInputStream(entry);
                OutputStream out = new FileOutputStream(descDir + zipEntryName);
                byte[] buf1 = new byte[1024];
                int len;
                while ((len = in.read(buf1)) > 0) {
                    out.write(buf1, 0, len);
                }
                in.close();
                out.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isZipFile(File file) {
        try {
            if (!file.exists()) return false;
            ZipFile zf = new ZipFile(file);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * 根据文件路径拷贝文件
     *
     * @param src          源文件
     * @param destFilePath 目标文件路径
     * @return boolean 成功true、失败false
     */
    public static boolean copyFile(File src, String destFilePath) {
        boolean result = false;
        if ((src == null) || (destFilePath == null)) {
            return result;
        }
        File dest = new File(destFilePath);
        if (dest != null && dest.exists()) {
            dest.delete(); // delete file
        }
        try {
            dest.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        FileChannel srcChannel = null;
        FileChannel dstChannel = null;

        try {
            srcChannel = new FileInputStream(src).getChannel();
            dstChannel = new FileOutputStream(dest).getChannel();
            srcChannel.transferTo(0, srcChannel.size(), dstChannel);
            result = true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return result;
        } catch (IOException e) {
            e.printStackTrace();
            return result;
        }
        try {
            srcChannel.close();
            dstChannel.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    public static void writeFile(File src, byte[] data) {
        FileOutputStream fos = null;
        try {
            if (src.exists()) {
                src.delete();
            }
            src.createNewFile();
            fos = new FileOutputStream(src);
            fos.write(data);
        } catch (IOException e) {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
            e.printStackTrace();
        }
    }

    /**
     * 解压文件
     */
    public static void gUnZipFile(String gzFilePath, String finalFilePath) {

        byte[] buffer = new byte[1024];

        try {
            String tmpFileName = gzFilePath + ".ungz";
            GZIPInputStream gzis = new GZIPInputStream(new FileInputStream(gzFilePath));
            FileOutputStream out = new FileOutputStream(tmpFileName);

            int len;
            while ((len = gzis.read(buffer)) > 0) {
                out.write(buffer, 0, len);
            }
            gzis.close();
            out.close();

            File newFile = new File(tmpFileName);
            newFile.renameTo(new File(finalFilePath));
            new File(gzFilePath).delete();
        } catch (Exception ex) {
            System.out.println("download err opps:" + ex.toString());
        }
    }

}
