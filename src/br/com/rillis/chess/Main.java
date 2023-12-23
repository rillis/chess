package br.com.rillis.chess;

import br.com.rillis.chess.gui.Window;
import br.com.rillis.chess.img.Images;
import br.com.rillis.chess.table.Table;

public class Main {
    public static void main(String[] args) {
        Images.init();

        Table table = new Table();
        Window window = new Window(table);
    }
}