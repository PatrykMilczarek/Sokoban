package com.company;

import javax.swing.*;
import java.awt.*;

/**
 * Klasa przechowujaca informacje o skrzynkach.
 */
public class Skrzynka extends Pole {
    /***
     * Zmienna przechowująca liczbę skrzynek na planszy
     */
    public static int ilosc_skrzynek=0;
    /***
     * Zmienna przechowująca pozycje X skrzynki
     */
    public int pozX=0;
    /***
     * Zmienna przechowująca pozycje Y skrzynki
     */
    public int pozY=0;

    /**
     * Konstruktor z parametrem
     * @param bsk długość boku skrzynki
     */
    public Skrzynka(int bsk){super(bsk);}
    public Skrzynka(){};

    /**
     * Metoda służąca do zmiany pozycji skrzynki. Przesuwa ją.
     * @param x przesunięcie w poziomie
     * @param y przesunięcie w pionie
     */
    public void przesun(int x, int y){
            pozX += x;
            pozY += y;

    }



}
