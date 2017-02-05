package com.company;

import javax.swing.*;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * @author Patryk Milczarek
 * @author Patrycja Karwat
 * */

/**
 * Klasa opisujaca dzialanie glownego okna menu.
 */
public class GlowneOkno extends JFrame {

    /**
     * Zmienna przechowujaca layout pozwalajacy na podmienianie paneli
     */
    static CardLayout layout_kartkowy = new CardLayout();

    /**
     * Zmienna przechowujaca panel, ktory przechowuje wszystkie okna gry.
     */
    static JPanel zbior_paneli;

    /**
     * Zmienna przechowujaca okno menu glownego.
     */
    static Menu menu_glowne;

    /**
     * Zmienna przechowujaca okno, w ktorym użytkownik podaje swoj nick w grze.
     */
    private Nick nick;

    /**
     * Zmienna przechowujaca okno, w ktorym wyswietlane sa najlepsze wyniki.
     */
    private ListaNajlepszychWynikow lista;

    /**
     * Zmienna przechowujaca okno, w ktorym przechowywane jest okno konca poziomu i konca gry.
     */
    static Obrazek_Konca_Poziomu obrazekkonca;

    /**
     * Zmienna przechowujaca okno planszy gry.
     */
    static KlasaPlanszy klasa_planszy;

    /**
     * Zmienna przechowujaca wysokosc okna.
     */
    static int screenHeight;

    /**
     * Zmienna przechowujaca szerokosc okna.
     */
    static int screenWidth;
    /**
     * Zmienna przechowujaca aktualny poziom gry.
     */
    static int aktualny_poziom=1;

    /**
     * Zmienna przehchowujaca maksymalna liczbe poziomow.
     */
    static int maks_liczba_poziomow;


    /**
     * Socket servera
     */
    private static Socket serwerSocket;
    /**
     * Zmienna określająca czy gra jest w trybie offline czy online
     */
    private static boolean online;


    /**
     * Konstruktor klasy glownego okna z parametrem gniazda serwera.
     * Jezeli udalo sie polaczyc z serwerem, to mozemy grac online.
     * Jezeli pliki nie zostaly usuniete lub nie zmieniono ich nazw, to okno
     * @param serwerSocket gniazdo serwera
     */
        public GlowneOkno(Socket serwerSocket){

            this.serwerSocket=serwerSocket;
            online=serwerSocket==null?false:true;
            inicjalizacjaUI();
            ustawOkno();



        }

    /**
     * Metoda, ktora ustawia domyslne parametry okna.
     */
    private void ustawOkno() {

        setTitle("Sokoban");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(700,400));
        pack();
        setVisible(true);
    }

    /**
     * Metoda, w ktorej ustawiany jest styl okien oraz dodawane sa wszystkie okna oraz ustawiane sa ich parametry.
     * Tutaj tez sprawdzane jest, czy mozna uruchomic okno w przypadku nie znalezienia odpowiednich plikow.
     */
    private void inicjalizacjaUI(){
        UIManager.LookAndFeelInfo[] infos = UIManager.getInstalledLookAndFeels();
        try {
            UIManager.setLookAndFeel("javax.swing.plaf.nimbus.NimbusLookAndFeel");
        }
        catch(Exception e){
            e.printStackTrace();
        }
        SwingUtilities.updateComponentTreeUI(GlowneOkno.this);




        menu_glowne = new Menu();
        lista = new ListaNajlepszychWynikow(online,serwerSocket);
        nick = new Nick();
        klasa_planszy = new KlasaPlanszy(online,serwerSocket,aktualny_poziom);


        obrazekkonca = new Obrazek_Konca_Poziomu();


        menu_glowne.setSize(this.getWidth(),this.getHeight());
        lista.setSize(this.getWidth(),this.getHeight());
        nick.setSize(this.getWidth(),this.getHeight());
        klasa_planszy.setSize(this.getWidth(),this.getHeight());
        obrazekkonca.setSize(this.getWidth(),this.getHeight());

        screenHeight = Odczytywanie.odczytana_dlugosc_kroku*Odczytywanie.odczytana_dlugosc_planszy;

        screenWidth = Odczytywanie.odczytana_dlugosc_kroku*Odczytywanie.odczytana_szerokosc_planszy + 4*Odczytywanie.odczytana_dlugosc_kroku;

        zbior_paneli = new JPanel();
        zbior_paneli.setLayout(layout_kartkowy);

        if(Odczytywanie.mozna_uruchomic_gre) {



            zbior_paneli.add(menu_glowne, "1");
            zbior_paneli.add(lista, "2");
            zbior_paneli.add(nick, "3");
            zbior_paneli.add(klasa_planszy, "4");
            zbior_paneli.add(obrazekkonca, "5");


            layout_kartkowy.show(zbior_paneli, "1");
            add(zbior_paneli);
        }else{
            this.setVisible(false);
            this.dispose();
        }



    }

    /**
     * Metoda odpowiedzialna za podniesienie poziomu. Jezeli liczba powtorzen osiagnie maksimum, gracz przegrywa i cofa sie do poziomu 1.
     *
     */
    public static void podniesPoziom(){

        if (Sokoban.liczba_powtorzen == Odczytywanie.odczytana_liczba_powtorzen){
            Sokoban.KONIEC_GRY=false;
            Sokoban.liczba_powtorzen = 0;
                aktualny_poziom=1;
            Gracz.laczna_liczba_punktow=0;
        }else
            aktualny_poziom++;


        if(aktualny_poziom<=maks_liczba_poziomow){

        klasa_planszy= new KlasaPlanszy(online,serwerSocket,aktualny_poziom);
        zbior_paneli.add(klasa_planszy,"4");

        klasa_planszy.addKeyListener(new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {

            }

            @Override
            public void keyPressed(KeyEvent e) {
                klasa_planszy.gra.keyPressed(e);
            }

            @Override
            public void keyReleased(KeyEvent e) {

            }
        });
        klasa_planszy.setFocusable(true);
        klasa_planszy.requestFocusInWindow();}
        else{

            obrazekkonca=new Obrazek_Konca_Poziomu();
            zbior_paneli.add(obrazekkonca,"5");
            layout_kartkowy.show(zbior_paneli,"5");
        }
    }


    /**
     * Metoda ustawiajaca preferowany rozmiar okna.
     * @return rozmiar okna
     */
    public Dimension getPreferredSize(){
        return new Dimension(screenWidth, screenHeight);
    }
}
