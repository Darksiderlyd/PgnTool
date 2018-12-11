package com.xiaoyu.cmdtool;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

/**
 * 用于命令的解析和编码
 */
public class ParserManager {

    private final static Charset CHARSET = Charset.forName("UTF-8");

    private final static byte PARAM_INT = 0; //参数类型 int
    private final static byte PARAM_LONG = 1; //参数类型 long
    private final static byte PARAM_FLOAT = 2; //参数类型 float
    private final static byte PARAM_STRING = 3; //参数类型 String


    /**
     * 返回封装后的 bytes
     * 所有的命令格式标示如下(字节)
     * 命令包长     命令编号    参数个数     参数类型     参数包长(（仅当类型为string才有）	)    参数
     * 4          1           1           1           4
     * <p>
     * 参数类型：int = 0, long = 1, float = 2, String = 3
     *
     * @param step ActionStep 中指令
     * @param args
     * @return
     */
    public static List<Byte> getPkgBytes(byte step, Object... args) {
        ArrayList<Byte> byteList = new ArrayList<>();
        byteList.add(step);

        // 1. 考虑参数不可能超过 128 个那么多
        int paramSize = args == null ? 0 : args.length;
        if (paramSize == 1) {
            if (args[0] instanceof ArrayList) {
                ArrayList arrList = (ArrayList) args[0];
                paramSize = arrList.size();
                args = arrList.toArray();
            }
        }
        byteList.add((byte) paramSize);

        // 2. 遍历参数列表，加入参数
        for (int i = 0; i < paramSize; i++) {
            Object o = args[i];

            // 2.1 判断 int, string, long, float
            if (o instanceof Integer) {
                byte[] res = getBytes((int) o);
                //添加参数类型
                byteList.add(PARAM_INT);
                addArray(byteList, res);
            } else if (o instanceof Long) {
                byte[] res = getBytes((long) o);
                byteList.add(PARAM_LONG);
                addArray(byteList, res);
            } else if (o instanceof Float) {
                byte[] res = getBytes((float) o);
                byteList.add(PARAM_FLOAT);
                addArray(byteList, res);
            } else if (o instanceof String) {
                byte[] res = getBytes((String) o);
                byteList.add(PARAM_STRING);
                //String 类型参数还需要带上参数长度
                addArray(byteList, getBytes(res.length));
                addArray(byteList, res);
            }
        }

        // 3. 将命令包长插入到最前面
        int pkgLen = byteList.size() + 4;
        byte[] pkgLenArr = getBytes(pkgLen);
        for (int i = pkgLenArr.length - 1; i >= 0; i--) {
            byteList.add(0, pkgLenArr[i]);
        }

        return byteList;
    }


    private static byte[] getBytes(int a) {
        return new byte[]{
                (byte) ((a >> 24) & 0xFF),
                (byte) ((a >> 16) & 0xFF),
                (byte) ((a >> 8) & 0xFF),
                (byte) (a & 0xFF)
        };
    }

    private static int getInt(byte[] b) {
        return b[3] & 0xFF |
                (b[2] & 0xFF) << 8 |
                (b[1] & 0xFF) << 16 |
                (b[0] & 0xFF) << 24;
    }

    private static byte[] getBytes(long a) {
        return new byte[]{
                (byte) ((a >> 56) & 0xff),
                (byte) ((a >> 48) & 0xff),
                (byte) ((a >> 40) & 0xff),
                (byte) ((a >> 32) & 0xff),
                (byte) ((a >> 24) & 0xff),
                (byte) ((a >> 16) & 0xff),
                (byte) ((a >> 8) & 0xff),
                (byte) (a & 0xff)
        };
    }

    private static long getLong(byte[] b) {
        return (0xffL & (long) b[7])
                | (0xff00L & ((long) b[6] << 8))
                | (0xff0000L & ((long) b[5] << 16))
                | (0xff000000L & ((long) b[4] << 24))
                | (0xff00000000L & ((long) b[3] << 32))
                | (0xff0000000000L & ((long) b[2] << 40))
                | (0xff000000000000L & ((long) b[1] << 48))
                | (0xff00000000000000L & ((long) b[0] << 56));
    }

    private static byte[] getBytes(float a) {
        int intBits = Float.floatToIntBits(a);
        return getBytes(intBits);
    }

    private static float getFloat(byte[] b) {
        return Float.intBitsToFloat(getInt(b));
    }

    private static byte[] getBytes(String a) {
        return a.getBytes(CHARSET);
    }

    private static String getString(byte[] b) {
        return new String(b, CHARSET);
    }

    private static void addArray(List<Byte> list, byte[] array) {
        for (byte b : array) {
            list.add(b);
        }
    }

    /**
     * 根据参数类型返回对应的参数包长
     *
     * @param paramType
     * @return 0 字符串 需要从byte数组中读取参数包场，-1 未定义参数类型
     */
    private static int getParamLengthByParamType(byte paramType) {
        if (paramType == PARAM_INT || paramType == PARAM_FLOAT) {
            return 4;
        } else if (paramType == PARAM_LONG) {
            return 8;
        } else if (paramType == PARAM_STRING) {
            return 0;
        } else {
            return -1;
        }
    }



}
