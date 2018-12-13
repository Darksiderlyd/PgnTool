package com.xiaoyu.common.utils;

import com.xiaoyu.common.cmdtool.cmd.base.IRtsCmd;

import java.util.List;
/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public class CmdUtils {

    protected static String cmd2Str(IRtsCmd cmd) {
        byte[] bytes;
        List<Byte> aByte = cmd.toByte();
        bytes = new byte[aByte.size()];
        for (int i = 0; i < aByte.size(); i++) {
            bytes[i] = aByte.get(i);
        }
        return Base64Util.byte2base64String(bytes);
    }

}
