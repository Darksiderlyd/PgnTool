package com.xiaoyu.common.cmdtool.cmd;

import com.google.gson.Gson;
import com.xiaoyu.common.cmdtool.ActionStep;
import com.xiaoyu.common.cmdtool.ParserManager;
import com.xiaoyu.common.cmdtool.cmd.base.BaseRtsCmd;
import com.xiaoyu.model.ChessType;

import java.util.List;

/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 * 老师端新建棋局(可扩展)
 * update:新建 67号命令
 */
public class TeaNewGameBoardExtensionCmd extends BaseRtsCmd {


    public final ChessType type;

    private final Gson gson;

    //新建棋局名称

    public String name;

    // 1 有规则 0 无规则
    public int hasRule;

    public Ext ext;

    private String extJSON = "";

    public static final int MIN_SUPPORT_CMD_VERSION = 13;

    public static class Ext {

        /**
         * 0，老师黑子，学生白子
         * 1，老师白子，学生黑子
         * 2，老师vs老师
         * 3，学生vs学生
         */
        private int role = -1;

        /**
         * 0黑先，1白先
         */
        private int order = -1;

        /**
         * 0初始棋盘，1当前棋盘，2历史棋盘
         */
        private int layout = -1;

        /**
         * 历史棋盘序号
         */
        private int layoutIndex = -1;

        /**
         * 白方名称
         */
        private String whiteName;
        /**
         * 白方头像
         */
        private String whiteUrl;
        /**
         * 黑方名称
         */
        private String blackName;
        /**
         * 黑方头像
         */
        private String blackUrl;

        /**
         * pgn
         */
        private String pgn;

        public String getPgn() {
            return pgn;
        }

        public void setPgn(String pgn) {
            this.pgn = pgn;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getOrder() {
            return order;
        }

        public void setOrder(int order) {
            this.order = order;
        }

        public int getLayout() {
            return layout;
        }

        public void setLayout(int layout) {
            this.layout = layout;
        }

        public int getLayoutIndex() {
            return layoutIndex;
        }

        public void setLayoutIndex(int layoutIndex) {
            this.layoutIndex = layoutIndex;
        }

        public String getWhiteName() {
            return whiteName;
        }

        public void setWhiteName(String whiteName) {
            this.whiteName = whiteName;
        }

        public String getWhiteUrl() {
            return whiteUrl;
        }

        public void setWhiteUrl(String whiteUrl) {
            this.whiteUrl = whiteUrl;
        }

        public String getBlackName() {
            return blackName;
        }

        public void setBlackName(String blackName) {
            this.blackName = blackName;
        }

        public String getBlackUrl() {
            return blackUrl;
        }

        public void setBlackUrl(String blackUrl) {
            this.blackUrl = blackUrl;
        }

        private Ext() {

        }

        public static class Builder {
            private int role = -1;
            private int order = -1;
            private int layout = -1;
            private int layoutIndex = -1;
            private String whiteName;
            private String whiteUrl;
            private String blackName;
            private String blackUrl;
            private String pgn;

            public Builder setRole(int role) {
                this.role = role;
                return this;
            }

            public Builder setOrder(int order) {
                this.order = order;
                return this;
            }

            public Builder setLayout(int layout) {
                this.layout = layout;
                return this;
            }

            public Builder setLayoutIndex(int layoutIndex) {
                this.layoutIndex = layoutIndex;
                return this;
            }

            public Builder setWhiteName(String whiteName) {
                this.whiteName = whiteName;
                return this;
            }

            public Builder setWhiteUrl(String whiteUrl) {
                this.whiteUrl = whiteUrl;
                return this;
            }

            public Builder setBlackName(String blackName) {
                this.blackName = blackName;
                return this;
            }

            public Builder setBlackUrl(String blackUrl) {
                this.blackUrl = blackUrl;
                return this;
            }

            public Builder setPgn(String pgn) {
                this.pgn = pgn;
                return this;
            }

            public Ext build() {
                Ext ext = new Ext();
                ext.role = this.role;
                ext.order = this.order;
                ext.layout = this.layout;
                ext.layoutIndex = this.layoutIndex;
                ext.blackName = this.blackName;
                ext.blackUrl = this.blackUrl;
                ext.whiteName = this.whiteName;
                ext.whiteUrl = this.whiteUrl;
                ext.pgn = this.pgn;
                return ext;
            }
        }
    }

    public TeaNewGameBoardExtensionCmd(ChessType type, String name, int hasRule, Ext ext) {
        this.type = type;
        this.name = name;
        this.hasRule = hasRule;
        this.ext = ext;
        gson = new Gson();
    }


    @Override
    public List<Byte> toByte() {
        String s = gson.toJson(this.ext, Ext.class);
//        String s1 = JSON.toJSONString(this.ext);
        System.out.println("Gson : " + s + "\n" + "FastJson : ");
//        System.out.println("Gson : " + s + "\n" + "FastJson : " + s1);
        return ParserManager.getPkgBytes(ActionStep.TEA_NEW_GAME_BOARD_EXTENSION, this.type.getCode(), this.name, this.hasRule, s);
    }
}
