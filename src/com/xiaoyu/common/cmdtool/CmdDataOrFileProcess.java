package com.xiaoyu.common.cmdtool;

import com.xiaoyu.common.cmdtool.cmd.base.IRtsCmd;
import com.xiaoyu.common.utils.Base64Util;
import com.xiaoyu.common.utils.XYFileUtil;
import okio.Buffer;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author: lyd
 * @Date: 2018/12/13 上午10:23
 * @Version 1.0.0
 */
public class CmdDataOrFileProcess {

    /**
     * 获取到压缩文件
     * @param filePath
     * @param fileName
     * @param bytes
     * @return
     */
    public static File getFile(String filePath, String fileName, byte[] bytes,String tag) {
        String zipName = filePath + fileName + ".gz";
        String desFileName = fileName + ".txt";

        File gzFile = new File(zipName);

        if (gzFile.exists()) {
            System.out.println("此文件已存在!!");
            return null;
        }

        File desFile = XYFileUtil.byteToFile(filePath,desFileName, bytes);
        XYFileUtil.zipFile(tag, desFile, zipName);
        desFile.delete();
        return gzFile;
    }

    //处理命令
    public static String processCmds(List<IRtsCmd> cmds,boolean isDebug) {
        List<Byte> allBytes = new ArrayList<>();
        for (IRtsCmd cmd : cmds) {
            List<Byte> toBytes = cmd.toByte();
            allBytes.addAll(toBytes);
            if (isDebug) System.out.println(toBytes);
        }

        if (allBytes.size() > 0) {
            byte[] bytes = new byte[allBytes.size()];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = allBytes.get(i);
            }
            return Base64Util.byte2base64String(bytes);
        }
        return null;
    }


    /**
     * 用户发送每一个数据包前增加包头（包长字段和时间戳字段）后再写入录制文件，每一个写入文件的包格式如下：
     * | 包长 | 时间戳 | 数据 |
     * 包长：4字节，写入文件的整个包的字节长度，包含包长和时间戳字段的长度
     * 时间戳：4字节，从会话开始到服务器收到该数据经历的相对时间，单位是毫秒
     * 数据：客户端调用 SDK 发送的实时会话数据
     *
     * @param data
     * @param time 时间占位用填多少都可以
     * @return
     */
    public static byte[] packAndStoreMsg(String data, long time) {
        int length = 0;
        try {
            //解决中文转字节后长度错误问题
            length = data.getBytes("utf-8").length;
        } catch (UnsupportedEncodingException e) {
            length = data.length();
            e.printStackTrace();
        }
        int dataLen = 8 + length;

        Buffer buffer = new Buffer().writeIntLe(dataLen)
                .writeIntLe((int) time)
                .writeUtf8(data);

        byte[] readByteArray = null;
        try {
            readByteArray = buffer.readByteArray();
        } catch (Exception ex) {
            System.out.println("xy_rts_err ex:" + ex.toString());
        } finally {
            System.out.println("MsgBytes :" + readByteArray);
            return readByteArray;
        }
    }

}
