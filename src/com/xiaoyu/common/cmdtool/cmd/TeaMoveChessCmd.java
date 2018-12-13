package com.xiaoyu.common.cmdtool.cmd;

import com.xiaoyu.common.cmdtool.ActionStep;
import com.xiaoyu.common.cmdtool.ParserManager;
import com.xiaoyu.common.cmdtool.cmd.base.BaseMoveChessCmd;

import java.util.List;
/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public class TeaMoveChessCmd extends BaseMoveChessCmd{

    public TeaMoveChessCmd(int role, int x, int y, int x2, int y2) {
        super(role, x, y, x2, y2);
    }

    @Override
    public List<Byte> toByte() {
        return ParserManager.getPkgBytes(ActionStep.TEA_MOVE_CHESS, this.role, this.x, this.y, this.x2, this.y2);
    }

}
