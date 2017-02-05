package com.company;

import javax.swing.*;
import java.awt.event.KeyEvent;
import java.util.Comparator;

/**
 * @author Patryk Milczarek
 * @author Patrycja Karwat
 * */

/**
 * Klasa przechowujaca informacje o graczu.
 */
public class Gracz extends Pole implements Comparable<Gracz>{
    /**
     * Zmienna przechowujaca nick gracza.
     */
     static String nick;

    /**
     * Zmienna przechowujaca liczbe punktow gracza w danym poziomie.
     */
    static int liczba_punktow;

    /**
     * Zmienna przechowujaca liczbe ruchow w danym poziomie.
     */
    static int liczba_ruchow;

    /**
     * Zmienna przechowujaca sume punktow uzyskana przez gracza ze wszystkich poziomow, ktore przeszedl.
     */
    static int laczna_liczba_punktow;

    /**
     * Zmienna pozwalajaca na wypisanie punktow zdobytych przez graczy w liscie najlepszych wynikow.
     */
    public int pkt_do_listy;

    /**
     * Zmienna pozwalajaca na wypisanie nickow graczy w liscie najlepszych wynikow.
     */
    private String nick_do_listy;

    /**
     * Zmienne pozwalajace na okreslenie, w ktora strone ma poruszac sie gracz.
     */
    private boolean gora,dol,prawo,lewo;

    /**
     * Pozycja pozioma gracza.
     */
    public int pozX=0;

    /**
     * Pozycja pionowa gracza.
     */
    public int pozY=0;


    /**
     * Metoda pozwalajaca porownac wyniki punktowe graczy. Potrzeba do sortowania w liscie najlepszych wynikow.
     * @param gracz1 gracz, ktorego punkty sa porownywane
     * @return wartosc do sortowania malejacego od gory
     */
    public int compareTo(Gracz gracz1){

       return gracz1.pkt_do_listy-this.laczna_liczba_punktow;


    }

    /**
     * Konstruktor bezparametrowy tworzący obiekt gracza.
     * */
    public Gracz(){


    }

    /**
     * Konstruktor z parametrami tworzacy obiekt gracza. Uzywany do wypisania wynikow w liscie najlepszych wynikow.
     * @param lp liczba punktow
     * @param nicz nick
     */
    public Gracz(int lp, String nicz){
        pkt_do_listy=lp;
        nick_do_listy=nicz;
    }


    /**
     * Metoda do zmiany pozycji gracza w pionie lub w poziomie.
     */
    public void przesun(){
        if(this.gora) {

            pozY-=1;
            this.gora=false;
        }
        else if(this.dol){
            pozY+=1;
            this.dol=false;
        }
        else if(this.prawo){
            pozX+=1;
            this.prawo=false;
        }
        else if(this.lewo){
            pozX-=1;
            this.lewo=false;
        }

    }


    /**
     * Metoda do zdarzenia wcisniecia przycisku pozwalajaca na zdecydowanie, czy gracz ma ruszyc sie w lewo, w prawo, w gore czy w dol.
     * @param e obiekt zdarzenia wcisniecia strzalki
     */

    public void keyPressed(KeyEvent e) {

            switch (e.getKeyCode()) {
                case KeyEvent.VK_RIGHT: {
                    this.prawo = true;
                    break;
                }
                case KeyEvent.VK_LEFT: {
                    this.lewo =true;
                    break;
                }case KeyEvent.VK_UP: {
                    this.gora = true;
                    break;
                }case KeyEvent.VK_DOWN: {
                    this.dol = true;
                    break;
                }

        }
    }


    /**
     * Metoda zamieniająca nick i liczbe punktów na obiekt typu String, używana przy tworzeniu listy najlepszych wyników
     * @return Nick i liczbe punktów w typie string
     */
    public String toString(){
        return "Nick: "+ nick_do_listy + "      " + "Liczba punktów: " + pkt_do_listy;
    }
    /**
     * Metoda zwracająca liczbe punktów
     * @return Liczba Punktow Gracza
     */
    public int zwrocPunkty(){return pkt_do_listy;}
    /**
     * Metoda zwracająca nick gracza
     * @return Nick Gracza
     */
    public String zwrocNick(){return nick_do_listy;}


    /**
     * Metoda ustawia nick gracza w liscie najlepszych wynikow.
     * @param n nick gracza 
     */
    public void ustawNick(String n){nick_do_listy = n;}

    /**
     * Metoda ustawiająca odpowiednie dane gracza w danym poziomie 
     * @param lp liczba punktów na start 
     */
    public void ustawGracza(int lp){
       liczba_punktow= lp;
        liczba_ruchow=0;

    }

    /**
     * Metoda ustawiajaca liczbe punktow gracza w liscie najlepszych wynikow.
     * @param p liczba punktow
     */
    public void wstawPunktyDoListy(int p){
        pkt_do_listy=p;
    }

}
