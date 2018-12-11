package com.xiaoyu.cmdtool.cmd.base;

import java.util.List;
/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public abstract class BaseRtsCmd implements IRtsCmd {
    @Override
    public List<Byte> toByte() {
        return null;
    }
}
