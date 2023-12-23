package br.com.rillis.chess;

import br.com.rillis.chess.gui.Window;
import br.com.rillis.chess.img.Images;
import br.com.rillis.chess.table.Table;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        JOptionPane.showMessageDialog(null, "Welcome to Chess!\n\nThis is a simple chess game made in Java.\n\nMade by Rillis.\n\nhttps://github.com/rillis/chess\n\nCommands:\n(ESC) Quit\n(R) Reset\n(F) Copy FEN code\n(C) Copy table code\n(L) Load from table code", "Chess", JOptionPane.INFORMATION_MESSAGE);

        Images.init();

        Table table = new Table();
        Window window = new Window(table);
    }
}