package br.com.rillis.chess.theory;

import br.com.rillis.chess.piece.Piece;
import br.com.rillis.chess.table.Table;

import java.util.Arrays;

public class Notation {
    public static String getNotation(int number, int letter){
        return (char)('a'+letter)+""+(8-number);
    }

    public static int[] getCoordinate(String notation){
        int letter = Character.toLowerCase(notation.substring(0,1).charAt(0)) - 'a';
        int number = 9 - Integer.parseInt(notation.substring(1,2)) - 1;
        return new int[]{number, letter};
    }

    public static String[] pawnMoves(String position, String color, Table table){
        int[] coordinate = getCoordinate(position);
        int number = coordinate[0];
        int letter = coordinate[1];
        String[] moves = new String[5];
        int index = 0;
        if(color.equals("w")){
            if(number!=0){
                if(table.getPiece(getNotation(number-1, letter)) == null){
                    moves[index] = getNotation(number-1, letter);
                }
                if(number==6 && table.getPiece(getNotation(number-2, letter)) == null) {
                    moves[index + 1] = getNotation(number - 2, letter);
                }

                if(letter != 0 && table.getPiece(getNotation(number-1, letter-1)) != null){
                    moves[index + 2] = getNotation(number-1, letter-1);
                }
                if(letter!=7 && table.getPiece(getNotation(number-1, letter+1)) != null){
                    moves[index + 3] = getNotation(number-1, letter+1);
                }

                //En passant
                if(number == 3){
                    if(letter != 0 && table.getPiece(getNotation(number, letter-1)) != null && table.getPiece(getNotation(number, letter-1)).isEnPassant() && table.movedLast.getPosition().equals(table.getPiece(getNotation(number, letter-1)).getPosition())){
                        moves[index + 4] = getNotation(number-1, letter-1);
                    }
                    if(letter != 7 && table.getPiece(getNotation(number, letter+1)) != null && table.getPiece(getNotation(number, letter+1)).isEnPassant() && table.movedLast.getPosition().equals(table.getPiece(getNotation(number, letter+1)).getPosition())){
                        moves[index + 4] = getNotation(number-1, letter+1);
                    }
                }
            }

        }else{
            if(number!=7){
                if(table.getPiece(getNotation(number+1, letter)) == null){
                    moves[index] = getNotation(number+1, letter);
                }
                if(number==1 && table.getPiece(getNotation(number+2, letter)) == null) {
                    moves[index + 1] = getNotation(number + 2, letter);
                }
                if(letter!=0 && table.getPiece(getNotation(number+1, letter-1)) != null){
                    moves[index + 2] = getNotation(number+1, letter-1);
                }
                if(letter!=7 && table.getPiece(getNotation(number+1, letter+1)) != null){
                    moves[index + 3] = getNotation(number+1, letter+1);
                }

                //En passant
                if(number == 4){
                    if(letter != 0 && table.getPiece(getNotation(number, letter-1)) != null && table.getPiece(getNotation(number, letter-1)).isEnPassant() && table.movedLast.getPosition().equals(table.getPiece(getNotation(number, letter-1)).getPosition())){
                        moves[index + 4] = getNotation(number+1, letter-1);
                    }
                    if(letter != 7 && table.getPiece(getNotation(number, letter+1)) != null && table.getPiece(getNotation(number, letter+1)).isEnPassant() && table.movedLast.getPosition().equals(table.getPiece(getNotation(number, letter+1)).getPosition())){
                        moves[index + 4] = getNotation(number+1, letter+1);
                    }
                }
            }

        }
        return moves;
    }

    public static String[] rookMoves(String position, String color){
        int[] coordinate = getCoordinate(position);
        int number = coordinate[0];
        int letter = coordinate[1];
        String[] moves = new String[14];
        int index = 0;
        for(int i=0; i<8; i++){
            if(i!=number){
                moves[index] = getNotation(i, letter);
                index++;
            }
        }
        for(int i=0; i<8; i++){
            if(i!=letter){
                moves[index] = getNotation(number, i);
                index++;
            }
        }
        return moves;
    }
    public static String[] knightMoves(String position, String color){
        int[] coordinate = getCoordinate(position);
        int number = coordinate[0];
        int letter = coordinate[1];
        String[] moves = new String[8];
        int index = 0;
        if(number+2<8 && letter+1<8){
            moves[index] = getNotation(number+2, letter+1);
            index++;
        }
        if(number+2<8 && letter-1>=0){
            moves[index] = getNotation(number+2, letter-1);
            index++;
        }
        if(number-2>=0 && letter+1<8){
            moves[index] = getNotation(number-2, letter+1);
            index++;
        }
        if(number-2>=0 && letter-1>=0){
            moves[index] = getNotation(number-2, letter-1);
            index++;
        }
        if(number+1<8 && letter+2<8){
            moves[index] = getNotation(number+1, letter+2);
            index++;
        }
        if(number+1<8 && letter-2>=0){
            moves[index] = getNotation(number+1, letter-2);
            index++;
        }
        if(number-1>=0 && letter+2<8){
            moves[index] = getNotation(number-1, letter+2);
            index++;
        }
        if(number-1>=0 && letter-2>=0){
            moves[index] = getNotation(number-1, letter-2);
            index++;
        }
        return moves;
    }

    //bishopMoves
    public static String[] bishopMoves(String position, String color) {
        int[] coordinate = getCoordinate(position);
        int number = coordinate[0];
        int letter = coordinate[1];
        String[] moves = new String[14];
        int index = 0;

        for (int i = number - 1, j = letter - 1; i >= 0 && j >= 0; i--, j--) {
            moves[index] = getNotation(i, j);
            index++;
        }

        for (int i = number - 1, j = letter + 1; i >= 0 && j < 8; i--, j++) {
            moves[index] = getNotation(i, j);
            index++;
        }

        for (int i = number + 1, j = letter - 1; i < 8 && j >= 0; i++, j--) {
            moves[index] = getNotation(i, j);
            index++;
        }

        for (int i = number + 1, j = letter + 1; i < 8 && j < 8; i++, j++) {
            moves[index] = getNotation(i, j);
            index++;
        }

        return moves;
    }

    //queenMoves
    public static String[] queenMoves(String position, String color){
        int[] coordinate = getCoordinate(position);
        int number = coordinate[0];
        int letter = coordinate[1];
        String[] moves = new String[27];
        int index = 0;
        for(int i=0; i<8; i++){
            if(i!=number){
                moves[index] = getNotation(i, letter);
                index++;
            }
        }
        for(int i=0; i<8; i++){
            if(i!=letter){
                moves[index] = getNotation(number, i);
                index++;
            }
        }

        for (int i = number - 1, j = letter - 1; i >= 0 && j >= 0; i--, j--) {
            moves[index] = getNotation(i, j);
            index++;
        }

        for (int i = number - 1, j = letter + 1; i >= 0 && j < 8; i--, j++) {
            moves[index] = getNotation(i, j);
            index++;
        }

        for (int i = number + 1, j = letter - 1; i < 8 && j >= 0; i++, j--) {
            moves[index] = getNotation(i, j);
            index++;
        }

        for (int i = number + 1, j = letter + 1; i < 8 && j < 8; i++, j++) {
            moves[index] = getNotation(i, j);
            index++;
        }
        return moves;
    }

    //kingMoves
    public static String[] kingMoves(String position, String color, Table table){
        int[] coordinate = getCoordinate(position);
        int number = coordinate[0];
        int letter = coordinate[1];
        String[] moves = new String[10];
        int index = 0;
        if(number+1<8 && letter+1<8){
            moves[index] = getNotation(number+1, letter+1);
            index++;
        }
        if(number+1<8 && letter-1>=0){
            moves[index] = getNotation(number+1, letter-1);
            index++;
        }
        if(number-1>=0 && letter+1<8){
            moves[index] = getNotation(number-1, letter+1);
            index++;
        }
        if(number-1>=0 && letter-1>=0){
            moves[index] = getNotation(number-1, letter-1);
            index++;
        }
        if(number+1<8){
            moves[index] = getNotation(number+1, letter);
            index++;
        }
        if(number-1>=0){
            moves[index] = getNotation(number-1, letter);
            index++;
        }
        if(letter+1<8){
            moves[index] = getNotation(number, letter+1);
            index++;
        }
        if(letter-1>=0){
            moves[index] = getNotation(number, letter-1);
            index++;
        }

        //castling
        if(color.equals(Piece.WHITE)){

            if(position.equals("e1") && !table.getPiece(position).hasMoved()){
                //long castle
                if(table.getPiece("a1")!=null && table.getPiece("a1").getType().equals(Piece.ROOK) && !table.getPiece("a1").hasMoved()){
                    if(table.getPiece("b1")==null && table.getPiece("c1")==null && table.getPiece("d1")==null){
                        moves[index] = "c1";
                        index++;
                    }
                }

                //small castle
                if(table.getPiece("h1")!=null && table.getPiece("h1").getType().equals(Piece.ROOK) && !table.getPiece("h1").hasMoved()){
                    if(table.getPiece("f1")==null && table.getPiece("g1")==null){
                        moves[index] = "g1";
                        index++;
                    }
                }
            }
        }else{
            if(position.equals("e8") && !table.getPiece(position).hasMoved()){
                //long castle
                if(table.getPiece("a8")!=null && table.getPiece("a8").getType().equals(Piece.ROOK) && !table.getPiece("a8").hasMoved()){
                    if(table.getPiece("b8")==null && table.getPiece("c8")==null && table.getPiece("d8")==null){
                        moves[index] = "c8";
                        index++;
                    }
                }

                //small castle
                if(table.getPiece("h8")!=null && table.getPiece("h8").getType().equals(Piece.ROOK) && !table.getPiece("h8").hasMoved()){
                    if(table.getPiece("f8")==null && table.getPiece("g8")==null){
                        moves[index] = "g8";
                        index++;
                    }
                }
            }
        }
        return moves;
    }
}
