package com.company;

/**
 * Created by Patryk on 2016-06-17.
 */
public class Para {
    /**
     * Nick gracza
     */
    private String nick;
    /**
     * Liczba punktów gracza.
     */
    private int punkty;

    /**
     * Konstruktor klasy. Ustawai odpowiedni nick i liczbe punktów graczowi.
     * @param nick nick
     * @param punkty liczba punktów
     */
    public Para(String nick, int punkty){
        this.nick=nick;
        this.punkty=punkty;
    }

    /**
     * Metoda zwracająca nicku gracza.
     * @return nick gracza.
     */

    public String zwrocNick() {
        return nick;
    }

    /**
     * Metoda zwracająca liczbę punktów gracza.
     * @return punkty gracza.
     */

    public int zwrocPunkty() {
        return punkty;
    }

}