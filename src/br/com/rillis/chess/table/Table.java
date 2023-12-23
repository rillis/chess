package br.com.rillis.chess.table;

import br.com.rillis.chess.piece.Piece;
import br.com.rillis.chess.theory.Notation;

import javax.swing.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class Table {

    public Piece[][] table = new Piece[8][8];
    private boolean whiteTurn = true;

    public int[] selected = null;

    public Piece movedLast = null;

    public Table(String positions){
        String[] pos = positions.split(",");
        String turn = pos[pos.length-1];
        whiteTurn = turn.equals("WHITE");
        int x = 0;
        int y = 0;
        for (String line : pos){
            if(line.equals(turn)) break;
            String[] pieces = line.split("]");
            for(String piece : pieces){
                piece = piece.split("\\[")[1];
                if(!piece.equals("")){
                    Piece pieceObj = null;
                    if(!piece.equals("-")){
                        String p = piece.split(":")[0];
                        boolean moved = Objects.equals(piece.split(":")[1], "Y");
                        pieceObj = new Piece(p.charAt(0) == 'w' ? Piece.WHITE : Piece.BLACK, String.valueOf(p.charAt(1)), new int[]{y, x}, moved);
                    }
                    table[y][x] = pieceObj;
                    x++;
                    if(x>7){
                        x=0;
                        y++;
                    }
                }
            }
        }

    }

    public Table(){
        this("[bR:N][bN:N][bB:N][bQ:N][bK:N][bB:N][bN:N][bR:N],[bp:N][bp:N][bp:N][bp:N][bp:N][bp:N][bp:N][bp:N],[-][-][-][-][-][-][-][-],[-][-][-][-][-][-][-][-],[-][-][-][-][-][-][-][-],[-][-][-][-][-][-][-][-],[wp:N][wp:N][wp:N][wp:N][wp:N][wp:N][wp:N][wp:N],[wR:N][wN:N][wB:N][wQ:N][wK:N][wB:N][wN:N][wR:N],WHITE");
    }

    public void reset(){
        this.table = new Table().table;
        this.whiteTurn = true;
        this.selected = null;
        this.movedLast = null;
    }


    @Override
    public String toString(){
        StringBuilder s = new StringBuilder();
        for(Piece[] piece : table){
            for(Piece p : piece){
                if(p==null) s.append("[-]");
                else s.append("[").append(p.getColor().charAt(0)).append(p.getType().charAt(0)).append(":").append(p.hasMoved()?"Y":"N").append("]");
            }
            s.append(",");
        }
        s.append(whiteTurn?"WHITE":"BLACK");
        return s.toString();
    }

    //check if king is in check
    public boolean isCheck(String color){
        int[] kingCoordinate = null;
        for(int i=0; i<table.length; i++){
            for(int j=0; j<table[i].length; j++){
                if(table[i][j]!=null && table[i][j].getType().equals(Piece.KING) && table[i][j].getColor().equals(color)){
                    kingCoordinate = new int[]{i, j};
                    break;
                }
            }
        }
        if(kingCoordinate==null) return false;
        for(Piece[] piece : table){
            for(Piece p : piece){
                if(p!=null && !p.getColor().equals(color)){
                    for(String move: getAllowedMoves(p.getPosition())){
                        if(move.equals(Notation.getNotation(kingCoordinate[0], kingCoordinate[1]))) return true;
                    }
                }
            }
        }
        return false;
    }

    public Piece getKing(String color){
        for(Piece[] piece : table){
            for(Piece p : piece){
                if(p!=null && p.getType().equals(Piece.KING) && p.getColor().equals(color)){
                    return p;
                }
            }
        }
        return null;
    }

    public boolean isMate(String color){
        if(!isCheck(color)) return false;
        for(Piece[] piece : table){
            for(Piece p : piece){
                if(p!=null && p.getColor().equals(color)){
                    for(String move: getAllowedMoves(p.getPosition())){
                        Table t = new Table(toString());
                        t.forceMove(p.getPosition(), move);
                        if(!t.isCheck(color)){
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    private boolean wouldCollide(String from, String to){
        if(!getPiece(from).getType().equals(Piece.KNIGHT)){
            int[] fromCoordinate = Notation.getCoordinate(from);
            int[] toCoordinate = Notation.getCoordinate(to);
            int[] direction = new int[]{0,0};
            if(fromCoordinate[0] > toCoordinate[0]) direction[0] = -1;
            else if(fromCoordinate[0] < toCoordinate[0]) direction[0] = 1;
            if(fromCoordinate[1] > toCoordinate[1]) direction[1] = -1;
            else if(fromCoordinate[1] < toCoordinate[1]) direction[1] = 1;
            int[] currentCoordinate = new int[]{fromCoordinate[0], fromCoordinate[1]};
            while(true){
                currentCoordinate[0]+=direction[0];
                currentCoordinate[1]+=direction[1];
                if(currentCoordinate[0]==toCoordinate[0] && currentCoordinate[1]==toCoordinate[1]) break;
                if(getPiece(Notation.getNotation(currentCoordinate[0], currentCoordinate[1]))!=null) return true;
            }
        }
        return false;
    }

    private boolean tryingToCaptureHimself(String from, String to){
        if(getPiece(to)==null) return false;
        return getPiece(from).getColor().equals(getPiece(to).getColor());
    }

    public String[] getAllowedMoves(String notation){
        //System.out.println("Checking moves for "+notation+ " piece: "+getPiece(notation).getType()+" color: "+getPiece(notation).getColor()+" moved: "+getPiece(notation).hasMoved());
        int[] coordinate = Notation.getCoordinate(notation);
        Piece piece = table[coordinate[0]][coordinate[1]];
        if(piece==null) return null;

        String[] moves = piece.getAllowedMoves(this);
        ArrayList<String> validMoves = new ArrayList<>();

        //System.out.println(Arrays.toString(moves));
        for(String move : moves){

            if(move!=null && !wouldCollide(notation, move) && !tryingToCaptureHimself(notation, move)) {
                validMoves.add(move);
            }
        }

        return validMoves.toArray(new String[validMoves.size()]);
    }


    public void forceMove(String from, String to){
        int[] fromCoordinate = Notation.getCoordinate(from);
        int[] toCoordinate = Notation.getCoordinate(to);
        table[toCoordinate[0]][toCoordinate[1]] = table[fromCoordinate[0]][fromCoordinate[1]];
        table[fromCoordinate[0]][fromCoordinate[1]] = null;
        table[toCoordinate[0]][toCoordinate[1]].setPosition(to);

        movedLast = table[toCoordinate[0]][toCoordinate[1]];
    }

    private boolean checkKingPath(String from, String[] tos, String color){
        for(String to : tos){
            Table t = new Table(toString());
            t.forceMove(from, to);
            if (t.isCheck(color)) return true;
        }
        return false;
    }
    public void move(String from, String to){
        if(selected==null || getPiece(from)==null) return;

        //check if its the right turn
        if((whiteTurn && getPiece(from).getColor().equals(Piece.BLACK)) || (!whiteTurn && getPiece(from).getColor().equals(Piece.WHITE))){
            System.out.println("Illegal move: Wrong turn");
            return;
        }

        //if its allowed
        String[] allowedMoves = getAllowedMoves(from);
        boolean allowed = false;
        for(String move : allowedMoves){
            if(move!=null && move.equals(to)){
                allowed = true;
                break;
            }
        }
        if(!allowed){
            System.out.println("Illegal move: Not allowed");
            return;
        }

        Table futureTable = new Table(this.toString());
        futureTable.forceMove(from, to);
        boolean selfCheck = futureTable.isCheck(getPiece(from).getColor());
        if(selfCheck){
            System.out.println("Illegal move: Self check");
            return;
        }

        //Identify if it was en passant
        if(getPiece(from).getType().equals(Piece.PAWN) && getPiece(to) == null && from.charAt(0)!=to.charAt(0)){
            if(getPiece(from).getColor().equals(Piece.WHITE)) {
                table[Notation.getCoordinate(to)[0] + 1][Notation.getCoordinate(to)[1]] = null;
            }else{
                table[Notation.getCoordinate(to)[0] - 1][Notation.getCoordinate(to)[1]] = null;
            }
        }

        //Identify if it was castling
        if(getPiece(from).getType().equals(Piece.KING) && Math.abs(Notation.getCoordinate(from)[1]-Notation.getCoordinate(to)[1])==2){
            if(isCheck(getPiece(from).getColor())){
                System.out.println("Illegal move: Checked king");
                return;
            }
            //check for check in king path
            boolean check = false;
            switch (to) {
                case "g1" -> check = checkKingPath("e1", new String[]{"f1", "g1"}, Piece.WHITE);
                case "c1" -> check = checkKingPath("e1", new String[]{"d1", "c1"}, Piece.WHITE);
                case "g8" -> check = checkKingPath("e8", new String[]{"f8", "g8"}, Piece.BLACK);
                case "c8" -> check = checkKingPath("e8", new String[]{"d8", "c8"}, Piece.BLACK);
            }
            if(check || isCheck(getPiece(from).getColor())){
                System.out.println("Illegal move: Checked king");
                return;
            }

            switch (to) {
                case "g1" -> forceMove("h1", "f1");
                case "c1" -> forceMove("a1", "d1");
                case "g8" -> forceMove("h8", "f8");
                case "c8" -> forceMove("a8", "d8");
            }
        }



        forceMove(from, to);

        whiteTurn = getPiece(to).getColor().equals(Piece.BLACK);

        if(getPiece(to).getType().equals(Piece.PAWN) && (to.charAt(1)=='1' || to.charAt(1)=='8')){
            System.out.println("Promotion");

            //Promotion popup
            String[] options = {"Queen", "Rook", "Bishop", "Knight"};
            int response = JOptionPane.showOptionDialog(null, "Choose a piece to promote to", "Promotion", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
            String type = switch (response) {
                case 0 -> Piece.QUEEN;
                case 1 -> Piece.ROOK;
                case 2 -> Piece.BISHOP;
                case 3 -> Piece.KNIGHT;
                default -> Piece.QUEEN;
            };
            table[Notation.getCoordinate(to)[0]][Notation.getCoordinate(to)[1]] = new Piece(getPiece(to).getColor(), type, to);
        }

        if(isCheck(Piece.WHITE)){
            System.out.println("White is in check");
            if(isMate(Piece.WHITE)){
                System.out.println("White is in mate");
                JOptionPane.showMessageDialog(null, "Black wins");
                reset();
            }
        }
        if(isCheck(Piece.BLACK)){
            System.out.println("Black is in check");
            if(isMate(Piece.BLACK)){
                System.out.println("Black is in mate");
                JOptionPane.showMessageDialog(null, "White wins");
                reset();
            }
        }
    }

    public Piece[][] getTable() {
        return table;
    }

    public Piece getPiece(String notation){
        int[] coordinate = Notation.getCoordinate(notation);
        return table[coordinate[0]][coordinate[1]];
    }


    public void click(String notation, int[] coordinate) {
        if(selected==null && getPiece(notation)!=null && ((whiteTurn && getPiece(notation).getColor().equals(Piece.WHITE)) || (!whiteTurn && getPiece(notation).getColor().equals(Piece.BLACK)))){
            selected = coordinate;
        }else{
            if(selected!=null){
                move(Notation.getNotation(selected[0], selected[1]), notation);
                selected = null;
            }
        }
    }


    public String toFEN(){
        StringBuilder fen = new StringBuilder();
        for(int i=0; i<table.length; i++){
            int empty = 0;
            for(int j=0; j<table[i].length; j++){
                if(table[i][j]==null){
                    empty++;
                }else{
                    if(empty>0){
                        fen.append(empty);
                        empty = 0;
                    }
                    fen.append(table[i][j].getColor().equals(Piece.WHITE) ? table[i][j].getType().toUpperCase() : table[i][j].getType().toLowerCase());
                }
            }
            if(empty>0){
                fen.append(empty);
            }
            if(i<table.length-1){
                fen.append("/");
            }
        }
        fen.append(" ").append(whiteTurn?"w":"b");

        String castling = "";

        if (getPiece("e1") != null && getPiece("h1") != null && getPiece("e1").getType().equals(Piece.KING) && !getPiece("e1").hasMoved() && getPiece("h1").getType().equals(Piece.ROOK) && !getPiece("h1").hasMoved()){
            castling += "K";
        }
        if (getPiece("e1") != null && getPiece("a1") != null && getPiece("e1").getType().equals(Piece.KING) && !getPiece("e1").hasMoved() && getPiece("a1").getType().equals(Piece.ROOK) && !getPiece("a1").hasMoved()){
            castling += "Q";
        }
        if (getPiece("e8") != null && getPiece("h8") != null && getPiece("e8").getType().equals(Piece.KING) && !getPiece("e8").hasMoved() && getPiece("h8").getType().equals(Piece.ROOK) && !getPiece("h8").hasMoved()){
            castling += "k";
        }
        if (getPiece("e8") != null && getPiece("a8") != null && getPiece("e8").getType().equals(Piece.KING) && !getPiece("e8").hasMoved() && getPiece("a8").getType().equals(Piece.ROOK) && !getPiece("a8").hasMoved()){
            castling += "q";
        }
        if(castling.isEmpty()) castling = "-";
        fen.append(" ").append(castling);

        String enPassant = "-";
        if(movedLast!=null && movedLast.getType().equals(Piece.PAWN) ){
            if(movedLast.isEnPassant()) {
                enPassant = Notation.getNotation(Notation.getCoordinate(movedLast.getPosition())[0]+ (movedLast.getColor().equals(Piece.WHITE) ? 1 : -1), Notation.getCoordinate(movedLast.getPosition())[1]);
            }
        }
        if(enPassant.isEmpty()) enPassant = "-";
        fen.append(" ").append(enPassant);

        fen.append(" 0 1");

        return fen.toString();
    }

    public void load(String code) {
        Table newTable = new Table(code);
        this.table = newTable.table;
        this.whiteTurn = newTable.whiteTurn;
        this.selected = null;
        this.movedLast = null;
    }
}
