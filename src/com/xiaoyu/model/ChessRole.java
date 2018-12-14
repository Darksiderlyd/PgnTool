package com.xiaoyu.model;

import com.xiaoyu.pgn.pgntool.Color;
import com.xiaoyu.pgn.pgntool.PGNParser;
import com.xiaoyu.sgf.base.Board;

/**
 * @Author: lyd
 * @Date: 2018/12/7 下午2:56
 * @Version 1.0.0
 */
public enum ChessRole {

    // 围棋黑棋
    Weiqi_Role_Black(0),

    // 围棋白棋
    Weiqi_Role_White(1),

    // 国际象棋 白色 兵
    Chess_Role_White_Pawn(0),

    // 国际象棋 白色 车
    Chess_Role_White_Rook(1),

    // 国际象棋 白色 马
    Chess_Role_White_Knight(2),

    // 国际象棋 白色 相
    Chess_Role_White_Bishop(3),

    // 国际象棋 白色 后
    Chess_Role_White_Queen(4),

    // 国际象棋 白色 王
    Chess_Role_White_King(5),

    // 国际象棋 黑色 兵
    Chess_Role_Black_Pawn(10),

    // 国际象棋 黑色 车
    Chess_Role_Black_Rook(11),

    // 国际象棋 黑色 马
    Chess_Role_Black_Knight(12),

    // 国际象棋 黑色 相
    Chess_Role_Black_Bishop(13),

    // 国际象棋 黑色 后
    Chess_Role_Black_Queen(14),

    // 国际象棋 黑色 王
    Chess_Role_Black_King(15),

    Feild(-1),;

    // 角色
    private int role;

    ChessRole(int role) {
        this.role = role;
    }

    public int getRole() {
        return role;
    }

    public boolean isBlackRole() {
        int roleCode = getRole();
        if (this != Weiqi_Role_White && this != Weiqi_Role_Black) {
            return roleCode >= 10 && roleCode <= 15;
        } else {
            return roleCode == 0;
        }
    }

    public boolean isWhiteRole() {
        return !isBlackRole();
    }


    public static ChessRole getChessRole(int role, ChessType chessType) {
        if (chessType == ChessType.Mode_Chess) {
            switch (role) {
                case 0:
                    return ChessRole.Chess_Role_White_Pawn;
                case 1:
                    return ChessRole.Chess_Role_White_Rook;
                case 2:
                    return ChessRole.Chess_Role_White_Knight;
                case 3:
                    return ChessRole.Chess_Role_White_Bishop;
                case 4:
                    return ChessRole.Chess_Role_White_Queen;
                case 5:
                    return ChessRole.Chess_Role_White_King;
                case 10:
                    return ChessRole.Chess_Role_Black_Pawn;
                case 11:
                    return ChessRole.Chess_Role_Black_Rook;
                case 12:
                    return ChessRole.Chess_Role_Black_Knight;
                case 13:
                    return ChessRole.Chess_Role_Black_Bishop;
                case 14:
                    return ChessRole.Chess_Role_Black_Queen;
                case 15:
                    return ChessRole.Chess_Role_Black_King;
            }
        } else {
            switch (role) {
                case 0:
                    return ChessRole.Weiqi_Role_Black;
                case 1:
                    return ChessRole.Weiqi_Role_White;
            }
        }

        return ChessRole.Weiqi_Role_Black;
    }

    /**
     * 国际象棋兵种英文缩写
     * 王-K，后-Q，车-R，马-N，象-B，兵-P
     *
     * @return
     */
    public String getSimpleName() {
        String[] roles = {"P", "R", "N", "B", "Q", "K"};
        int role = this.getRole();
        return role <= 5 ? roles[role] : roles[role - 10];
    }

    //把引擎棋子角色转换为我们的国际象棋棋子角色
    public static ChessRole fromParserRoleToChessRole(String engineRole, Color color) {
        if (engineRole.startsWith(PGNParser.PAWN)) {
            return color == Color.white ? Chess_Role_White_Pawn : Chess_Role_Black_Pawn;
        } else if (engineRole.startsWith(PGNParser.KNIGHT)) {
            return color == Color.white ? Chess_Role_White_Knight : Chess_Role_Black_Knight;
        } else if (engineRole.startsWith(PGNParser.BISHOP)) {
            return color == Color.white ? Chess_Role_White_Bishop : Chess_Role_Black_Bishop;
        } else if (engineRole.startsWith(PGNParser.ROOK)) {
            return color == Color.white ? Chess_Role_White_Rook : Chess_Role_Black_Rook;
        } else if (engineRole.startsWith(PGNParser.QUEEN)) {
            return color == Color.white ? Chess_Role_White_Queen : Chess_Role_Black_Queen;
        } else if (engineRole.startsWith(PGNParser.KING)) {
            return color == Color.white ? Chess_Role_White_King : Chess_Role_Black_King;
        } else {
            return color == Color.white ? Chess_Role_White_Pawn : Chess_Role_Black_Pawn;
        }
    }

    //把引擎棋子角色转换为我们的围棋棋子角色
    public static ChessRole fromParserRoleToWeiqiRole(int color) {
        return color == Board.BLACK ? Weiqi_Role_Black : Weiqi_Role_White;
    }


}
