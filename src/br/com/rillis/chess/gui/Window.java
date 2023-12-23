package br.com.rillis.chess.gui;

import br.com.rillis.chess.img.Images;
import br.com.rillis.chess.piece.Piece;
import br.com.rillis.chess.table.Table;
import br.com.rillis.chess.theory.Notation;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class Window extends JFrame{

    JPanel contentPane = null;
    JLabel background = null;
    Table table;

    public Window(Table table){
        this.table = table;

        int sizePerSlot = Config.windowSize/8;

        setTitle("Chess");
        setSize(Config.windowSize, Config.windowSize);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setResizable(false);

        contentPane = new JPanel();
        contentPane.setSize(Config.windowSize, Config.windowSize);
        contentPane.setLayout(null);
        setContentPane(contentPane);


        background = new JLabel(Images.images.get("board"));
        background.setBounds(0, 0, Config.windowSize, Config.windowSize);

        setVisible(true);

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseReleased(MouseEvent e) {
                int x = e.getX();
                int y = e.getY();

                int[] coordinate = new int[]{y/sizePerSlot, x/sizePerSlot};
                String notation = Notation.getNotation(coordinate[0], coordinate[1]);

                //System.out.println("Clicked x: "+coordinate[0]+" y: "+coordinate[1] + " notation: "+notation);
                table.click(notation, coordinate);
            }
        });

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ESCAPE){
                    System.exit(0);
                }
                if(e.getKeyCode()==KeyEvent.VK_C){
                    System.out.println(table);
                }
                if(e.getKeyCode()==KeyEvent.VK_R){
                    table.reset();
                }
            }
        });

        new Thread(() -> {
            while(true){
                contentPane.removeAll();
                update();
                contentPane.add(background);
                repaint();
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    public void update(){
        int size = Config.windowSize;
        int sizePerSlot = size/8;

        if(table.selected != null){


            //show dots at allowed moves
            String[] allowedMoves = table.getAllowedMoves(Notation.getNotation(table.selected[0], table.selected[1]));
            if(allowedMoves!=null){
                for(String move : allowedMoves){
                    if(move != null){
                        int[] coordinate = Notation.getCoordinate(move);
                        JLabel dot = new JLabel(Images.images.get("dot"));
                        dot.setBounds(coordinate[1]*sizePerSlot, coordinate[0]*sizePerSlot, sizePerSlot, sizePerSlot);
                        contentPane.add(dot);
                    }

                }
            }

            try {
                //show selected piece
                JLabel selected = new JLabel(Images.images.get("selected"));
                selected.setBounds(table.selected[1] * sizePerSlot, table.selected[0] * sizePerSlot, sizePerSlot, sizePerSlot);
                contentPane.add(selected);
            }catch (Exception e){
                e.printStackTrace();
            }
        }

        try{
            //show checked king
            if (table.isCheck(Piece.BLACK) || table.isCheck(Piece.WHITE)) {
                String pos;
                if (table.isCheck(Piece.BLACK)) {
                    pos = table.getKing(Piece.BLACK).getPosition();
                } else {
                    pos = table.getKing(Piece.WHITE).getPosition();
                }
                int[] coordinate = Notation.getCoordinate(pos);
                JLabel checked = new JLabel(Images.images.get("check"));
                checked.setBounds(coordinate[1] * sizePerSlot, coordinate[0] * sizePerSlot, sizePerSlot, sizePerSlot);
                contentPane.add(checked);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        for(int i=0; i<8; i++){
            for(int j=0; j<8; j++){
                if(table.table[i][j]!=null){
                    Piece piece = table.table[i][j];
                    JLabel piecelbl = new JLabel(Images.images.get(piece.getColor()+piece.getType()));
                    piecelbl.setBounds(j*sizePerSlot, i*sizePerSlot, sizePerSlot, sizePerSlot);
                    contentPane.add(piecelbl);
                }
            }
        }


    }
}
