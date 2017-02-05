package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa przechowujaca informacje o scianach.
 */
public class Sciana extends Pole {
    /***
     * Zmienna przechowująca liczbę sciian na planszy.
     */
   public static int ilosc_scian=0;
    /***
     * Obrazek reprezentujcy ściane na planszy gry.
     */
    Image scianaIMG = new ImageIcon("sciana2.jpg").getImage();
    /**
     * Pozycja X-owa Sciany
     */
    int x;
    /**
     * Pozycja Y-owa sciany
     */
    int y;

    /**
     * Nadpisała metoda paint, służąca do rysowania ścian.
     * @param g kontekst graficzny
     */
    public void paint(Graphics g){
        g.drawImage(scianaIMG,x,y,50,50,null);

}


}
