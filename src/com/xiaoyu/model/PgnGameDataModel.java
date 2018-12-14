package com.xiaoyu.model;

/**
 * @Author: lyd
 * @Date: 2018/12/14 上午10:03
 * @Version 1.0.0
 */
public class PgnGameDataModel {

    private byte[] pgnDatas;
    private String gameName;
    private int index;

    public PgnGameDataModel(byte[] pgnDatas, String gameName,int index) {
        this.pgnDatas = pgnDatas;
        this.gameName = gameName;
        this.index = index;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public byte[] getPgnDatas() {
        return pgnDatas;
    }

    public void setPgnDatas(byte[] pgnDatas) {
        this.pgnDatas = pgnDatas;
    }

    public String getGameName() {
        return gameName;
    }

    public void setGameName(String gameName) {
        this.gameName = gameName;
    }
}
