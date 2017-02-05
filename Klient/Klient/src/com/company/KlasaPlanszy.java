package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.net.Socket;

/**
 * Klasa sluzaca jako ramka, na ktorej rysujemy plansze gry.
 */
public class KlasaPlanszy extends JPanel {
    /**
     * Zmienna przechowujaca obiekt klasy Sokoban.
     */
    private Sokoban sokoban;

    /**
     * Zmienna przechowujaca okno z informacjami o przebiegu gry.
     */
    private Info info;

    /**
     * Zmienna przechowujaca obiekt klasy rysujacej okno z gra.
     */
    public PanelGry gra;

    /**
     * Konstuktor z parametrami klasy KlasaPlanszy.  Tutaj tworzymy obiekty z oknem gry i oknem przebiegu gry.
     * Tutaj tworzymy obiekt klasy Sokoban odczytujacy wszystkie informacje z plikow.
     * @param on parametr mowiacy o udanym polaczeniu
     * @param serwer_Socket gniazdo serwera
     * @param nr_poziomu numer poziomu do wlaczenia
     */

    public KlasaPlanszy(boolean on, Socket serwer_Socket,int nr_poziomu) {


        sokoban = new Sokoban(on,serwer_Socket,nr_poziomu);
        setLayout(new BorderLayout());
        
        gra = new PanelGry();
        add(BorderLayout.CENTER, gra);


            info = new Info();

            info.dodajInfo(GlowneOkno.aktualny_poziom, Gracz.liczba_punktow, Gracz.liczba_ruchow,
                    Odczytywanie.odczytana_liczba_powtorzen, Odczytywanie.odczytana_liczba_wyburzen,Odczytywanie.odczytana_liczba_przesuniec_dwoch);


            add(BorderLayout.EAST, info);
        
    }





}


/**
 * Klasa, ktorej uzywamy jako panelu z informacjami o przebiegu podczas gry.
 */
class Info extends JPanel{
    /**
     *  Ramka wyswietlajaca aktualny poziom.
     */
    static JLabel aktualny_poziom;

    /**
     * Ramka wyswietlajaca liczbe powtorzen do wykorzystania, ktore zostaly graczowi.
     */
    static JLabel liczba_powtorzen;

    /**
     * Ramka wyswietlajaca liczbe przesuniec dwoch skrzynek, ktore pozostaly graczowi.
     */
    static JLabel liczba_przesuniec_dwoch;

    /**
     * Ramka wyswietlajaca aktualna liczbe punktow gracza w pojedynczym poziomie.
     */
    static JLabel liczba_pkt;

    /**
     * Ramka wyswietlajaca liczbe ruchow wykonanych przez gracza.
     */
    static JLabel liczba_ruchow;

    /**
     * Ramka wyswietlajaca liczbe wyburzen pozostalych do wykorzystania przez gracza.
     */
    static JLabel liczba_wyburzen;

    /**
     * Ramka wyswietlajaca informacje o przycisku uzywanym do pauzy.
     */
    JLabel pauza = new JLabel("Pauza - Spacja");

    /**
     * Zmienna przechowujaca obrazek tla okna z informacjami.
     */
    
    Image img = new ImageIcon("panelpodloga.png").getImage();
    
    /**
     * Konstruktor bezparametrowy z informacjami o grze. Znajduje sie po prawej stronie planszy. 
     * Dodawane sa tutaj wszystkie ramki.
     */
    public Info(){

        setSize(2*Odczytywanie.odczytana_dlugosc_kroku,this.getHeight());
        aktualny_poziom = new JLabel();
        liczba_pkt = new JLabel();
        liczba_ruchow = new JLabel();
        liczba_powtorzen = new JLabel();
        liczba_wyburzen = new JLabel();
        liczba_przesuniec_dwoch = new JLabel();






        add(aktualny_poziom);
        add(liczba_pkt);
        add(liczba_ruchow);
        add(liczba_powtorzen);

        add(liczba_wyburzen);
        add(liczba_przesuniec_dwoch);
        add(pauza);


    }
    
    /**
     * Metoda ustawiajaca parametry ramek oraz dodajaca domyslne dane na poczatku gry.
     * @param aktpoz aktualny poziom
     * @param lp liczba punktow
     * @param lr liczba ruchow
     * @param cz liczba powtorzen
     * @param lw liczba wyburzen
     * @param pd liczba przesuniec dwoch skrzynek
     */
    public void dodajInfo(int aktpoz,int lp,int lr,int cz,int lw,int pd){
        aktualny_poziom.setFont(new Font("sanserif", Font.PLAIN, 15));
        liczba_pkt.setFont(new Font("sanserif", Font.PLAIN, 15));
        liczba_ruchow.setFont(new Font("sanserif", Font.PLAIN, 15));
        liczba_powtorzen.setFont(new Font("sanserif", Font.PLAIN, 15));
        liczba_wyburzen.setFont(new Font("sanserif", Font.PLAIN, 15));
        liczba_przesuniec_dwoch.setFont(new Font("sanserif", Font.PLAIN, 15));
        pauza.setFont(new Font("sanserif", Font.PLAIN, 15));

        aktualny_poziom.setText("Poziom: "+aktpoz);
        liczba_pkt.setText("Liczba Punktow: "+ lp);
        liczba_ruchow.setText("Liczba Ruchow: "+ lr);
        liczba_powtorzen.setText("Powtorzenia[R]: "+ cz);
        liczba_wyburzen.setText("Wyburzenia[X]: "+ lw);
        liczba_przesuniec_dwoch.setText("Przesuniecia[Z]: "+ pd);

        aktualny_poziom.setForeground(Color.WHITE);
        liczba_pkt.setForeground(Color.WHITE);
        liczba_ruchow.setForeground(Color.WHITE);
        liczba_wyburzen.setForeground(Color.WHITE);
        liczba_powtorzen.setForeground(Color.WHITE);
        liczba_przesuniec_dwoch.setForeground(Color.WHITE);
        pauza.setForeground(Color.WHITE);
    }

    /**
     * Metoda aktualizujaca informacje w ramce na temat liczby ruchow.
     */
    public static void aktualizujRuch(){

        liczba_ruchow.setText("Liczba Ruchow: "+ (++Gracz.liczba_ruchow));

        if(Gracz.liczba_punktow<Sokoban.pkt_ruchy){
            liczba_pkt.setText("Liczba Punktow: "+ (Gracz.liczba_punktow=0));
        }else
        liczba_pkt.setText("Liczba Punktow: "+ (Gracz.liczba_punktow-=Sokoban.pkt_ruchy));

    }
    /**
     * Metoda aktualizujaca informacje w ramce na temat liczby punktow po wyburzeniu sciany.
     */
    public static void aktualizujPktzaWyburzenie(){
        if(Gracz.liczba_punktow<Odczytywanie.odczytane_pkt_wyburzenie)
            liczba_pkt.setText("Liczba Punktow: "+ (Gracz.liczba_punktow=0));
        else
        liczba_pkt.setText("Liczba Punktow: "+ (Gracz.liczba_punktow-=Odczytywanie.odczytane_pkt_wyburzenie));
    }

    /**
     * Metoda aktualizujaca informacje w ramce na temat pozostalej liczby powtorzen poziomu.
     */
public static void aktualizujPowtorzenia(){

    if(Sokoban.liczba_powtorzen==Odczytywanie.odczytana_liczba_powtorzen){
        liczba_powtorzen.setText("Poddanie gry - R");
    }else
    liczba_powtorzen.setText("Powtorzenia[R]: "+ (Odczytywanie.odczytana_liczba_powtorzen-Sokoban.liczba_powtorzen));

}
    /**
     * Metoda aktualizujaca informacje w ramce na temat pozostalej liczby wyburzen.
     */
    public static void aktualizujWyburzenia(){
        liczba_wyburzen.setText("Wyburzenia[X]: "+ (Odczytywanie.odczytana_liczba_wyburzen-Sokoban.liczba_usuniec_scian));
    }

    /**
     * Metoda aktualizujaca informacje w ramce na temat pozostalej liczby przesuniec dwoch skrzynek.
     */
    public static void aktualizujPrzesunieciaDwoch(){
        liczba_przesuniec_dwoch.setText("Przesuniecia[Z]: "+ (Odczytywanie.odczytana_liczba_przesuniec_dwoch-Sokoban.liczba_przesuniec_dwoch));
    }

    /**
     * Metoda rysujaca obrazek tla w oknie z informacjami.
     * @param g kontekst graficzny
     */
    public void paintComponent(Graphics g){
        super.paintComponent(g);

        g.drawImage(img,0,0,3*Odczytywanie.odczytana_dlugosc_kroku,this.getHeight(),this);
    }

    /**
     * Metoda, ktora ustawia preferowany rozmiar panelu
     * @return Rozmiar panelu dostosowany do zmieniajacej sie wysokosci i okreslonej szerokosci.
     */
    public Dimension getPreferredSize(){
        return new Dimension(3*Odczytywanie.odczytana_dlugosc_kroku,this.getHeight());
    }
}
