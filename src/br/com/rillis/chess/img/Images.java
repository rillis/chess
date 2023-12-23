package br.com.rillis.chess.img;

import br.com.rillis.chess.gui.Config;

import javax.swing.*;
import java.awt.*;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Images {
    public static Map<String, ImageIcon> images = null;

    public static void init(){
        images = new HashMap<>();

        images.put("board", resize("/br/com/rillis/chess/img/board.png", Config.windowSize));

        images.put("dot", resize("/br/com/rillis/chess/img/dot.png", (int) Math.ceil(Config.sizePerSlot*0.30)));

        images.put("check", resize("/br/com/rillis/chess/img/check.png", Config.sizePerSlot));
        images.put("selected", resize("/br/com/rillis/chess/img/selected.png", Config.sizePerSlot));

        for(String piece : new String[]{"bR", "bN", "bB", "bQ", "bK", "bp", "wR", "wN", "wB", "wQ", "wK", "wp"}){
            images.put(piece, resize("/br/com/rillis/chess/img/"+piece+".png", (int) Math.ceil(Config.sizePerSlot*0.75)));
        }



    }

    private static ImageIcon getImage(String url){
        return new ImageIcon(Objects.requireNonNull(Images.class.getResource(url)));
    }

    private static ImageIcon resize(String url, int size){
        return new ImageIcon(getImage(url).getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH));
    }
}
