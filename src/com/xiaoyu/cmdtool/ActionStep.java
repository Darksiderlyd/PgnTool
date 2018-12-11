package com.xiaoyu.cmdtool;

/**
 * <p>
 * 命令指令
 * 所有的命令格式标示如下 (字节数)
 * 命令长度  命令编号    参数个数   |  参数类型   参数长度（String类型才有）   参数
 * 4        1           1           1         4
 * <p>
 * 参数类型：int = 0, long = 1, float = 2, String = 3
 * <p>
 * eg:
 * 老师端开始上课，String: courseId = "11223",  long: time = 123
 * 命令组成如下 byte[] 数组
 * 命令长度   命令编号        参数个数    |  参数类型  参数包长   参数    |  参数类型      参数
 * xx      7(byte值)     2(byte值)         3         5       11223       1           123
 */

public class ActionStep {

    /**
     * 老师移动棋子
     * 参数 int: role,  int: x 开始棋盘的横坐标, int: y 开始棋盘纵坐标, int: x2 落子棋盘的横坐标， int: y2 落子棋盘纵坐标
     * role:
     * 围棋: 0 黑棋， 1 白棋
     * 国际象棋: 白色 0 兵,   1 车,  2 马，  3 相，   4 后， 5皇
     * 黑色 10 兵, 11 车,  12 马， 13 相， 14 后， 15皇
     * <p>
     * 对于坐标而言,三种情况:
     * a.  x  ＝－1 and y = -1, 表示落子
     * b.  x2 = -1 and y2 =-1,  表示移掉棋子
     * c.  其他情况表示棋子移动
     */
    public static byte TEA_MOVE_CHESS = 46;


    //----------------------------------班课新增命令------------------------------------

    /**
     * 老师是否带规则(或者其他类型的棋盘通过rule)的新建棋盘类型
     */
    public static byte TEA_NEW_GAME_BOARD_EXTENSION = 67;


}
