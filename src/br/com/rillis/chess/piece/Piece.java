package br.com.rillis.chess.piece;

import br.com.rillis.chess.table.Table;
import br.com.rillis.chess.theory.Notation;

import javax.swing.*;

public class Piece {
    //Colors
    public static final String WHITE = "w";
    public static final String BLACK = "b";

    //Types
    public static final String PAWN = "p";
    public static final String ROOK = "R";
    public static final String KNIGHT = "N";
    public static final String BISHOP = "B";
    public static final String QUEEN = "Q";
    public static final String KING = "K";

    private String color;
    private String type;
    private String position;
    private boolean moved;
    private boolean dead;

    private String lastPosition = null;

    public Piece(String color, String type, String position) {
        this.color = color;
        this.type = type;
        this.position = position;
        this.moved = false;
        this.dead = false;
    }

    public Piece(String color, String type, int[] coordinates, boolean moved){
        this.color = color;
        this.type = type;
        this.position = Notation.getNotation(coordinates[0], coordinates[1]);
        this.moved = moved;
        this.dead = false;
    }

    public boolean isEnPassant(){
        if(type.equals(PAWN)){
            if(lastPosition!=null){
                if(lastPosition.charAt(0)==position.charAt(0) && Math.abs(lastPosition.charAt(1)-position.charAt(1))==2){
                    return true;
                }
            }
        }
        return false;
    }

    public void setMoved(boolean moved){
        this.moved = moved;
    }

    public ImageIcon getImage(){
        return new ImageIcon(getClass().getResource("/br/com/rillis/chess/img/"+color+type+".png"));
    }

    public String getPosition() {
        return position;
    }

    public String[] getAllowedMoves(Table table){
        String[] moves = null;
        switch (type){
            case PAWN:
                moves = Notation.pawnMoves(position, color, table);
                break;
            case ROOK:
                moves = Notation.rookMoves(position, color);
                break;
            case KNIGHT:
                moves = Notation.knightMoves(position, color);
                break;
            case BISHOP:
                moves = Notation.bishopMoves(position, color);
                break;
            case QUEEN:
                moves = Notation.queenMoves(position, color);
                break;
            case KING:
                moves = Notation.kingMoves(position, color, table);
                break;
        }
        return moves;
    }

    public void setPosition(String position) {
        this.lastPosition = this.position;
        this.position = position;
        this.moved = true;
    }

    public boolean hasMoved() {
        return moved;
    }

    public String getType() {
        return type;
    }

    public String getColor() {
        return color;
    }

    @Override
    public String toString(){
        return color+type+ " | "+position;
    }
}
