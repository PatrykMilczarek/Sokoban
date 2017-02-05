package com.company;



import javax.swing.*;
import java.awt.*;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;


/**
 * Klasa rysujaca pole gry.
 */
public class PanelGry extends JPanel implements KeyListener {
    /**
     * Zmienna przechowujaca pola w grze.
     */
    static int[][] plansza=new int[Odczytywanie.odczytana_dlugosc_planszy][Odczytywanie.odczytana_szerokosc_planszy];
    /**
     * Zmienna przechowujaca maksymalna liczba skrzynek.
     */
    static int MAX_liczba_skrzynek; //10
    /**
     * Zmienna przechowujaca maksymalna liczbe scian.
     */
    static int MAX_liczba_scian;  //62

    /**
     * Domyslna szerokosc okna gry.
     */
    private static final int DEFAULT_WIDTH = Odczytywanie.odczytana_szerokosc_planszy * Odczytywanie.odczytana_dlugosc_kroku;

    /**
     * Domyslna wysokosc okna gry.
     */
    private static final int DEFAULT_HEIGHT = Odczytywanie.odczytana_dlugosc_planszy * Odczytywanie.odczytana_dlugosc_kroku;

    /**
     * Zmienna przechowujaca ilosc skrzynek na miejscu.
     */
    private int stopien_powodzenia = 0;

    /**
     * Tablica przechowujaca ilosc skrzynek do polozenia na miejsce i stopien powodzenia.
     */
    boolean tablica_prawdy[] = new boolean[100];
    /**
     * Zmienna mowiaca o tym, czy skrzynka trafila na miejsce.
     */
    boolean skrzynka_na_miejscu = false;
    /**
     * Zmienna sluzaca do skalowania zmiany pozycji w poziomie.
     */
    private double pozycjaX= DEFAULT_WIDTH/Odczytywanie.odczytana_szerokosc_planszy;
    /**
     * Zmienna sluzaca do sklaowania zmiany pozycji w pionie.
     */
    private double pozycjaY= DEFAULT_HEIGHT/Odczytywanie.odczytana_dlugosc_planszy;

    /**
     * Bufer graficzny.
     */
    private Graphics buffer;

    /**
     * Obrazek buforowany
     */
    private BufferedImage imgb;

    /**
     * Zmienna sluzaca do skalowania ruchu gracza w pionie.
     */
    double stepY = 0;
    /**
     * Zmienna sluzaca do skalowania ruchu gracza w poziomie.
     */
    double stepX = 0;
    /**
     * Zmienna sluzaca do skalowania ruchu skrzynki w poziomie.
     */
    double krokSkrzynkiX = 0;
    /**
     * Zmienna sluzaca do skalowania ruchu skrzynki w pionie.
     */
    double krokSkrzynkiY = 0;

    /**
     * Flaga mowiaca o tym, czy ruch sie skonczyl i mozna zaczac nastepny.
     */
    boolean flagaruchu = true;
    /**
     * Flaga mowiaca o tym, czy pierwsza skrzynka trafia na skrzynke.
     */
    boolean trafiono_na_skrzynke = false;

    /**
     * Flaga mowiaca o tym, czy gracz moze sie ruszyc, czy nie.
     */
    static boolean gracz_nie_ruszyl_sie = true;

    /**
     * Lista przechowujaca obiekty scian.
     */
    private ArrayList<Sciana> sciany2;
    /**
     * Lista przechowujaca obiekty skrzynek.
     */
    private ArrayList<Skrzynka> skrzynie2;
    /**
     * Lista przechowujaca obiekty miejsc na skrzynki.
     */
    private ArrayList<MiejscaNaSkrzynki> miejsca2;
    /**
     * Lista przechowujaca wszystkie pola.
     */
    private ArrayList<Pole> swiat;

    /**
     * Zmienna przechowujaca obiekt Gracza.
     */
    private Gracz gracz;
    /**
     * Zmienna przechowujaca indeks drugiej skrzynki, gdy przesuwamy dwie na raz.
     */
    private int indeks_drugiej_skrzynki;
    /**
     * Zmienna mowiaca o tym, czy zaczynamy przesuwac druga skrzynke.
     */
    private boolean przesuwam_druga_skrzynke=false;


    /**
     * Obrazek sciany.
     */
    Image scianaIMG = new ImageIcon("sciana.jpg").getImage();
    /**
     * Obrazek podlogi.
     */
    Image podlogaIMG = new ImageIcon("podloga2.jpg").getImage();
    /**
     * Obrazek skrzynki.
     */
    Image skrzynkaIMG = new ImageIcon("pudelko.jpg").getImage();
    /**
     * Obrazek miejsca na skrzynki.
     */
    Image miejsceIMG = new ImageIcon("miejsce2.jpg").getImage();
    /**
     * Obrazek gracza.
     */
    Image sokobanIMG = new ImageIcon("ruch/dol2.png").getImage();


    /**
     * Metoda mowiaca o zdarzeniach po wcisnieciu odpowiednich klawiszy.
     * @param e obiekt zdarzenia wcisniecia klawisza
     */
    public void keyPressed(KeyEvent e) {


        if(e.getKeyCode() == KeyEvent.VK_Z){

            if (Sokoban.liczba_przesuniec_dwoch != Odczytywanie.odczytana_liczba_przesuniec_dwoch) {
                Sokoban.liczba_przesuniec_dwoch++;
                Sokoban.przesuniecie_dwoch=true;
                Info.aktualizujPrzesunieciaDwoch();

                repaint();
            }

            repaint();
        }
        if (e.getKeyCode() == KeyEvent.VK_SPACE) {

            if (!Sokoban.pauza_gry) {
                Sokoban.pauza_gry = true;
                repaint();
            } else if (Sokoban.pauza_gry) {
                Sokoban.pauza_gry = false;
                repaint();
            }
        }

        if (!Sokoban.pauza_gry) {

            Runnable r = new AkcjaRunnable(e);
            Thread pressT = new Thread(r);
            pressT.start();
        }


        if (e.getKeyCode() == KeyEvent.VK_X) {
            System.out.println("X");

            if (Sokoban.liczba_usuniec_scian != Odczytywanie.odczytana_liczba_wyburzen) {
                Sokoban.liczba_usuniec_scian++;
                Sokoban.mozna_usunac_sciane = true;
                Info.aktualizujWyburzenia();

                repaint();
            }

            repaint();
        }

        if (e.getKeyCode() == KeyEvent.VK_R) {
            System.out.println("R");

            if (Sokoban.liczba_powtorzen == Odczytywanie.odczytana_liczba_powtorzen) {
                KoniecGry();
            } else {


            Sokoban.liczba_powtorzen++;
            for (int i = 0; i < skrzynie2.size(); i++) {
                skrzynie2.get(i).pozX = 0;
                skrzynie2.get(i).pozY = 0;
            }


            for (int i = 0; i < MAX_liczba_scian - Sokoban.liczba_usuniec_scian; i++) {
                swiat.remove(0);
            }

            for (int i = 0; i < MAX_liczba_scian; i++) {
                swiat.add(0, sciany2.get(i));
            }
            Sokoban.liczba_usuniec_scian = 0;
                Sokoban.liczba_przesuniec_dwoch=0;
            Info.aktualizujWyburzenia();
                Info.aktualizujPowtorzenia();
                Info.aktualizujPrzesunieciaDwoch();

            gracz.pozX = 0;
            gracz.pozY = 0;


            repaint();
            }


        }



    }


    public void keyReleased(KeyEvent e) {


    }

    public void keyTyped(KeyEvent e) {
    }

    /**
     * Metoda inicjujaca czynnosci potrzebne do zakonczenia poziomu lub calej gry.
     */
    public void KoniecGry(){



            Sokoban.KONIEC_GRY = true;

        Gracz.laczna_liczba_punktow += Gracz.liczba_punktow;
            Gracz.liczba_punktow = 0;
            Gracz.liczba_ruchow = 0;

            MAX_liczba_skrzynek = 0;

            plansza = new int[Odczytywanie.odczytana_dlugosc_planszy][Odczytywanie.odczytana_szerokosc_planszy];






        if (Sokoban.liczba_powtorzen == Odczytywanie.odczytana_liczba_powtorzen){
            GlowneOkno.podniesPoziom();
            GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli, "4");
        }else {
            Obrazek_Konca_Poziomu.panel_z_wynikami.setText("Twój wynik:" + Gracz.laczna_liczba_punktow);
            GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli, "5");
            GlowneOkno.podniesPoziom();
        }



    }
    /**
     * Metoda odczytująca liczbe scian, skrzynek na podstawie pliku konfiguracyjnego
     */
    public void odczytajLiczbePol() {
        for (int i = 0; i < plansza.length; i++) {
            for (int j = 0; j < plansza[i].length; j++) {
                switch (plansza[i][j]) {
                    case 0:

                        MAX_liczba_scian++;
                        break;
                    case 2:
                        MAX_liczba_skrzynek++;
                        break;

                }
            }
        }
    }

    /**
     * Metoda ustawiająca sciany, skrzynki na odpowiednich miejscach na planszy. W niej dochodzi do zmiany pozycji w czasie ruchu.
     * Pierwszym argumentem metody setBounds jest pozioma pozycja pola. Jest to szerokosc okna podzielona przez
     * szerokosc planszy (ilosc kolumn w wierszu) i pomnozona przez numer wiersza tablicy planszy, co daje nam odleglosc miedzy kolejnymi polami.
     * <p>
     * Drugim argumentem analogiczne jest pozycja pionowa.
     * <p>
     * Trzecim argumentem jest szerokosc jednego pola.
     * Jest to szerokość okna podzielona przez odczytana szerokosc planszy (ilosc kolumn w wierszu planszy).
     * <p>
     * Czwartym argumentem jest analogicznie wysokość jednego pola.
     */
    public void ustawPozycjePol() {
        int sk = 0;
        int s = 0;
        int m = 0;



            for (int i = 0; i < plansza.length; i++) {
                for (int j = 0; j < plansza[i].length; j++) {
                    switch (plansza[i][j]) {
                        case 0:

                            /**
                             * Ustawianie pozycji oraz wymiarów każdej ściany.
                             */
                            sciany2.get(s).setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy)*j),
                                    (((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy)*i)),50,
                                    50);
                            s++;

                            if (s == MAX_liczba_scian) s = 0;
                            break;
                        case 2:



                            krokSkrzynkiX = skrzynie2.get(sk).pozX * (pozycjaX / (double) Odczytywanie.odczytana_dlugosc_kroku); //obliczanie poziomego kroku skrzynki
                            krokSkrzynkiY = skrzynie2.get(sk).pozY * (pozycjaY / (double) Odczytywanie.odczytana_dlugosc_kroku);




                            skrzynie2.get(sk).setBounds((int)((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy)*j+krokSkrzynkiX),
                                    (int)((DEFAULT_HEIGHT /Odczytywanie.odczytana_dlugosc_planszy)*i+krokSkrzynkiY),
                                    DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy, (int)DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy);

                            sk++;

                            if (sk == MAX_liczba_scian) sk = 0;
                            break;
                        case 3:



                            stepY = gracz.pozY * (pozycjaY / (double) Odczytywanie.odczytana_dlugosc_kroku);
                            stepX = gracz.pozX * (pozycjaX / (double) Odczytywanie.odczytana_dlugosc_kroku);


                            gracz.setBounds((int)((DEFAULT_WIDTH/Odczytywanie.odczytana_szerokosc_planszy)*j + stepX),
                                    (int) ((DEFAULT_HEIGHT/Odczytywanie.odczytana_dlugosc_planszy)*i+ stepY),
                                    DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy, DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy);
                            break;
                        case 4:

                            miejsca2.get(m).setBounds(((DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy)*j),((DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy)*i),
                                    DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy,  DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy);
                            m++;

                            if (m == MAX_liczba_skrzynek) m = 0;
                            break;


                    }
                }
            }
        }




    /**
     * Konstruktor bezparametrowy panelu gry.
     * Tworzy liste elementów, dodaje je do jednej wspólnej listy "swiat".
     * W tym miejscu dodajemy słuchaczy wydarzeń przycisków.
     */
    public PanelGry() {

        sciany2 = new ArrayList<Sciana>();
        skrzynie2 = new ArrayList<Skrzynka>();
        miejsca2 = new ArrayList<MiejscaNaSkrzynki>();
        gracz = new Gracz();
        swiat = new ArrayList<Pole>();





        odczytajLiczbePol();
        for (int i = 0; i < MAX_liczba_scian; i++)
            sciany2.add(new Sciana());

        for (int i = 0; i < MAX_liczba_skrzynek; i++)
            skrzynie2.add(new Skrzynka());

        for (int i = 0; i < MAX_liczba_skrzynek; i++)
            miejsca2.add(new MiejscaNaSkrzynki());



        tablica_prawdy = new boolean[MAX_liczba_skrzynek];
        swiat.addAll(sciany2);
        swiat.addAll(miejsca2);
        swiat.addAll(skrzynie2);


        swiat.add(gracz);





        setVisible(true);


    }



    /**
     * Przeciążona metoda paintComponent, tworzy plansze.
     * Najpierw obliczana jest tutaj nowa pozycja wszystkich elementów.
     * Jezeli wlaczono pauze, to rysowany jest napis Pauza
     * Następnie są one rysowane.
     * @param g kontekst graficzny
     *
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        GlowneOkno.klasa_planszy.setFocusable(true);
        GlowneOkno.klasa_planszy.requestFocusInWindow();


        if (!Sokoban.KONIEC_GRY) {
            ustawPozycjePol();
            tworzplansze(buffer);
            g.drawImage(this.imgb, 0, 0, this.getWidth(), this.getHeight(), null);
            this.buffer.clearRect(0, 0, this.getWidth(), this.getHeight());
        }

        if(Sokoban.pauza_gry){
            g.setFont(new Font("Serif",Font.PLAIN,50));
            g.setColor(Color.WHITE);
            g.drawString("PAUZA",this.getWidth()/3,this.getHeight()/2);

        }

    }


    /**
     * Metoda tworzaca bufor do rysowania.
     */
    public void init() {
        this.imgb = new BufferedImage(DEFAULT_WIDTH,DEFAULT_HEIGHT,2);
        this.buffer = this.imgb.getGraphics();
    }


    /**
     * Metoda do wywolania metody tworzacej bufor na samym poczatku.
     */
    public void addNotify() {
        super.addNotify();

        this.init();
    }

    /**
     * Metoda rysuje w odpowiednich miejscach odpowiednie obrazki symbolizujące różne obiekty.
     * Rysowanie odbywa się poprzez odczytywanie pozycji oraz rozmiarów odpowiednich obiektów.
     *
     * @param g kontekst graficzny
     */
    public void tworzplansze(Graphics g){


        g.drawImage(podlogaIMG, 0, 0, DEFAULT_WIDTH/Odczytywanie.odczytana_szerokosc_planszy, DEFAULT_HEIGHT/Odczytywanie.odczytana_dlugosc_planszy, this);

        for (int i =0;i<Odczytywanie.odczytana_dlugosc_planszy;i++){
            for (int j =0; j<Odczytywanie.odczytana_szerokosc_planszy;j++) {
                g.copyArea(0, 0, DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy, DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy,
                        (DEFAULT_WIDTH / Odczytywanie.odczytana_szerokosc_planszy)*j, (DEFAULT_HEIGHT / Odczytywanie.odczytana_dlugosc_planszy)*i);
            }
        }



        for(int k =0; k <swiat.size();k++){
            if(swiat.get(k) instanceof Sciana){
                g.drawImage(scianaIMG,(int)swiat.get(k).getX(),(int)swiat.get(k).getY(),(int) swiat.get(k).getWidth(),
                        (int) swiat.get(k).getHeight(),this);

            }

            else if(swiat.get(k) instanceof MiejscaNaSkrzynki){
                g.drawImage(miejsceIMG,(int)swiat.get(k).getX(),(int)swiat.get(k).getY(),(int) swiat.get(k).getWidth(),
                        (int) swiat.get(k).getHeight(),this);

            }
            else if(swiat.get(k) instanceof Skrzynka){
                g.drawImage(skrzynkaIMG,(int)swiat.get(k).getX(),(int)swiat.get(k).getY(),(int) pozycjaX,
                        (int) pozycjaY,this);

            }
            else if(swiat.get(k) instanceof Gracz){

                g.drawImage(sokobanIMG,(int)gracz.getX(),(int)gracz.getY(),(int) gracz.getWidth(),
                        (int) gracz.getHeight(),this);

            }

        }
    }

    /**
     * Metoda sluzaca do usuniecia sciany na przeciwko.
     * @param i indeks sciany w liscie ze wszystkimi polami
     */
    public void usunSciane(int i){


    Info.aktualizujPktzaWyburzenie();

    swiat.remove(i);



}

        /**
         * Klasa z definicją wątku odpowiedzialnego za ruch.
         */
        class AkcjaRunnable implements Runnable {

            /**
             * Zmienna przechowujaca wcisniety klawisz.
             */
            KeyEvent klawis;

            /**
             * Konstruktor z parametrami.
             *
             * @param klawi wciśnięty przycisk
             */
            public AkcjaRunnable(KeyEvent klawi) {

                klawis = klawi;

            }

            /**
             * Przedefiniowana metoda run, która najpierw sprawdza flage zakończenia ruchu
             * i czy wszystkie skrzynki sa ustawione na miejscach, a następnie wywołuje kolejny ruch.
             */
            public void run() {
                if (flagaruchu && !tablica_prawdy[MAX_liczba_skrzynek - 1])
                    akcja(klawis);

            }
        }

    /**
     * Metoda sluzaca do uspienia watku.
     * @param ms ilosc miliseknd
     */
    public void sleeep(int ms){
        try{
            Thread.sleep(ms);
        }catch(InterruptedException e){

        }
    }

        /**
         * Główna metoda odpowiedzialna za ruch w grze. Najpierw sprawdzany jest wciśnięty przycisk.
         * Potem zaczynamy wykonywać przesuwanie gracza. Po każdym przesunięciu sprawdzana jest kolizja z otoczeniem.
         * Jeżeli dojdzie do kolizji ze ściana, to gracz nie rusza się.
         * Jeżeli dojdzie do kolizji ze skrzynką, to dodatkowo sprawdzana jest kolizja tej skrzynki ze ścianą, inną skrzynką, lub miejscem na skrzynki.
         * Po zakończeniu ruchu flagaruchu ustawiana jest na wartość false, tym samym umożliwiając ewentualne rozpoczęcie nowego wątku z ruchem.
         *
         * @param e wciśnięty przycik, który decyduje o odpowiednim ruchu
         */
        public synchronized void akcja(KeyEvent e) {
                int kod_klucza= e.getKeyCode();
            flagaruchu = false;
            gracz_nie_ruszyl_sie = false;
            int m=0;
            if (kod_klucza == KeyEvent.VK_DOWN) {

                    sokobanIMG = new ImageIcon("ruch/dol2.png").getImage();
                repaint();

                for (int i = 0; i < Odczytywanie.odczytana_dlugosc_kroku; i++) {
                    try {



                        for (int j = 0; j < swiat.size(); j++) { //sprawdzam wszystkie pola

                            if (gracz.dolnaKolizja(swiat.get(j))) {  //kolizja gracza z jakimkolwiek obiektem po lewej stronie


                                if (swiat.get(j) instanceof Skrzynka) {  //kolizja gracza ze skrzynka


                                    for (int l = 0; l < swiat.size(); l++) { //sprawdzenie wszystkich elementow do kolizji ze skrzynka

                                        if(swiat.get(j).dolnaKolizja(swiat.get(l)))
                                              //  gracz_nie_ruszyl_sie=true;
                                            if(swiat.get(l) instanceof MiejscaNaSkrzynki)
                                                trafiono_na_skrzynke=true;

                                            else if(swiat.get(l) instanceof Skrzynka){

                                                if(Sokoban.przesuniecie_dwoch){
                                                    przesuwam_druga_skrzynke=true;
                                                    indeks_drugiej_skrzynki=l;
                                                    for(int k =0; k<swiat.size();k++){
                                                        if(swiat.get(l).dolnaKolizja(swiat.get(k))) // kolizja drugiej skrzynki
                                                            if(swiat.get(k) instanceof MiejscaNaSkrzynki){
                                                                gracz_nie_ruszyl_sie=false;

                                                            }else{
                                                                gracz_nie_ruszyl_sie=true;

                                                            }

                                                    }




                                                } else gracz_nie_ruszyl_sie=true;

                                            } else gracz_nie_ruszyl_sie=true;


                                    }

                                    // zacznij ruch
                                    if (!gracz_nie_ruszyl_sie) {
                                        gracz_nie_ruszyl_sie=true;
                                        for(int k=0; k<Odczytywanie.odczytana_dlugosc_kroku;k++) {

                                            ((Skrzynka) swiat.get(j)).przesun(0, 1);

                                            if(przesuwam_druga_skrzynke){
                                                ((Skrzynka) swiat.get(indeks_drugiej_skrzynki)).przesun(0, 1);

                                            }


                                            gracz.keyPressed(e);
                                            gracz.przesun();

                                            if(m<10){
                                                sokobanIMG = new ImageIcon("ruch/dol1.png").getImage();
                                                m++;
                                            }else if(m>=10 && m<20){
                                                sokobanIMG = new ImageIcon("ruch/dol3.png").getImage();
                                                m++;
                                            }else if(m==20){
                                                m=0;
                                            }

                                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                                sokobanIMG = new ImageIcon("ruch/dol2.png").getImage();

                                            if(trafiono_na_skrzynke){
                                                trafiono_na_skrzynke=false;
                                                skrzynka_na_miejscu = true;
                                            }
                                            repaint();
                                            Thread.sleep(10);
                                        }
                                        indeks_drugiej_skrzynki=0;
                                       Sokoban.przesuniecie_dwoch=false;
                                        przesuwam_druga_skrzynke=false;
                                        Info.aktualizujRuch();
                                    }


                                } else if (swiat.get(j) instanceof MiejscaNaSkrzynki) {
                                    gracz_nie_ruszyl_sie = false;
                                } else {
                                    if(Sokoban.mozna_usunac_sciane) {
                                        usunSciane(j);
                                        repaint();
                                        Sokoban.mozna_usunac_sciane=false;
                                        gracz_nie_ruszyl_sie = true;
                                    }
                                    gracz_nie_ruszyl_sie = true;
                                }

                            }

                        }


                        if (!gracz_nie_ruszyl_sie) {
                            gracz.keyPressed(e);
                            gracz.przesun();

                            if(m<10){
                                sokobanIMG = new ImageIcon("ruch/dol1.png").getImage();
                                m++;
                            }else if(m>=10 && m<20){
                                sokobanIMG = new ImageIcon("ruch/dol3.png").getImage();
                                m++;
                            }else if(m==20){
                                m=0;
                            }

                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                sokobanIMG = new ImageIcon("ruch/dol2.png").getImage();
                            repaint();
                            Thread.sleep(10);




                        }

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!gracz_nie_ruszyl_sie) {
                    Info.aktualizujRuch();

                }

                if(skrzynka_na_miejscu) {

                    skrzynka_na_miejscu=false;

                    tablica_prawdy[stopien_powodzenia] = true;
                    stopien_powodzenia++;

                    if (tablica_prawdy[MAX_liczba_skrzynek - 1])
                        KoniecGry();
                }


            } else if (kod_klucza == KeyEvent.VK_RIGHT) {

                    sokobanIMG = new ImageIcon("ruch/prawo2.png").getImage();
                repaint();
                for (int i = 0; i < Odczytywanie.odczytana_dlugosc_kroku; i++) {
                    try {



                        for (int j = 0; j < swiat.size(); j++) { //sprawdzam wszystkie pola

                            if (gracz.prawaKolizja(swiat.get(j))) {  //kolizja gracza z jakimkolwiek obiektem po lewej stronie


                                if (swiat.get(j) instanceof Skrzynka) {  //kolizja gracza ze skrzynka


                                    for (int l = 0; l < swiat.size(); l++) { //sprawdzenie wszystkich elementow do kolizji ze skrzynka

                                        if(swiat.get(j).prawaKolizja(swiat.get(l)))

                                            if(swiat.get(l) instanceof MiejscaNaSkrzynki)
                                                trafiono_na_skrzynke=true;

                                            else if(swiat.get(l) instanceof Skrzynka){

                                                if(Sokoban.przesuniecie_dwoch){
                                                    przesuwam_druga_skrzynke=true;
                                                    indeks_drugiej_skrzynki=l;
                                                    for(int k =0; k<swiat.size();k++){
                                                        if(swiat.get(l).prawaKolizja(swiat.get(k))) // kolizja drugiej skrzynki
                                                            if(swiat.get(k) instanceof MiejscaNaSkrzynki){
                                                                skrzynka_na_miejscu=true;
                                                                gracz_nie_ruszyl_sie=false;

                                                            }else{
                                                                gracz_nie_ruszyl_sie=true;

                                                            }

                                                    }




                                                } else gracz_nie_ruszyl_sie=true;

                                            } else gracz_nie_ruszyl_sie=true;


                                    }

                                    // zacznij ruch
                                    if (!gracz_nie_ruszyl_sie) {
                                        gracz_nie_ruszyl_sie=true;
                                        for(int k=0; k<Odczytywanie.odczytana_dlugosc_kroku;k++) {

                                            ((Skrzynka) swiat.get(j)).przesun(1, 0);

                                            if(przesuwam_druga_skrzynke){
                                                ((Skrzynka) swiat.get(indeks_drugiej_skrzynki)).przesun(1, 0);

                                            }


                                            gracz.keyPressed(e);
                                            gracz.przesun();

                                            if(m<10){
                                                sokobanIMG = new ImageIcon("ruch/prawo1.png").getImage();
                                                m++;
                                            }else if(m>=10 && m<20){
                                                sokobanIMG = new ImageIcon("ruch/prawo3.png").getImage();
                                                m++;
                                            }else if(m==20){
                                                m=0;
                                            }

                                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                                sokobanIMG = new ImageIcon("ruch/prawo2.png").getImage();

                                            if(trafiono_na_skrzynke){
                                                trafiono_na_skrzynke=false;
                                                skrzynka_na_miejscu = true;
                                            }
                                            repaint();
                                            Thread.sleep(10);
                                        }
                                        indeks_drugiej_skrzynki=0;
                                        Sokoban.przesuniecie_dwoch=false;
                                        przesuwam_druga_skrzynke=false;
                                        Info.aktualizujRuch();
                                    }


                                } else if (swiat.get(j) instanceof MiejscaNaSkrzynki) {
                                    gracz_nie_ruszyl_sie = false;
                                } else {
                                    if(Sokoban.mozna_usunac_sciane) {
                                        usunSciane(j);
                                        repaint();
                                        Sokoban.mozna_usunac_sciane=false;
                                        gracz_nie_ruszyl_sie = true;
                                    }
                                    gracz_nie_ruszyl_sie = true;
                                }

                            }

                        }


                        if (!gracz_nie_ruszyl_sie) {
                            gracz.keyPressed(e);
                            gracz.przesun();

                            if(m<10){
                                sokobanIMG = new ImageIcon("ruch/prawo1.png").getImage();
                                m++;
                            }else if(m>=10 && m<20){
                                sokobanIMG = new ImageIcon("ruch/prawo3.png").getImage();
                                m++;
                            }else if(m==20){
                                m=0;
                            }

                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                sokobanIMG = new ImageIcon("ruch/prawo2.png").getImage();
                            repaint();
                            Thread.sleep(10);




                        }

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!gracz_nie_ruszyl_sie) {
                    Info.aktualizujRuch();

                }

                if(skrzynka_na_miejscu) {

                    skrzynka_na_miejscu=false;

                    tablica_prawdy[stopien_powodzenia] = true;
                    stopien_powodzenia++;

                    if (tablica_prawdy[MAX_liczba_skrzynek - 1])
                        KoniecGry();
                }


            } else if (kod_klucza == KeyEvent.VK_LEFT) {
                sokobanIMG = new ImageIcon("ruch/lewo2.png").getImage();
                repaint();

                for (int i = 0; i < Odczytywanie.odczytana_dlugosc_kroku; i++) {
                    try {



                        for (int j = 0; j < swiat.size(); j++) { //sprawdzam wszystkie pola

                            if (gracz.lewaKolizja(swiat.get(j))) {  //kolizja gracza z jakimkolwiek obiektem po lewej stronie


                                if (swiat.get(j) instanceof Skrzynka) {  //kolizja gracza ze skrzynka


                                    for (int l = 0; l < swiat.size(); l++) { //sprawdzenie wszystkich elementow do kolizji ze skrzynka

                                        if(swiat.get(j).lewaKolizja(swiat.get(l)))
                                            //  gracz_nie_ruszyl_sie=true;
                                            if(swiat.get(l) instanceof MiejscaNaSkrzynki)
                                                trafiono_na_skrzynke=true;

                                            else if(swiat.get(l) instanceof Skrzynka){

                                                if(Sokoban.przesuniecie_dwoch){
                                                    przesuwam_druga_skrzynke=true;
                                                    indeks_drugiej_skrzynki=l;
                                                    for(int k =0; k<swiat.size();k++){
                                                        if(swiat.get(l).lewaKolizja(swiat.get(k))) // kolizja drugiej skrzynki
                                                            if(swiat.get(k) instanceof MiejscaNaSkrzynki){

                                                                gracz_nie_ruszyl_sie=false;

                                                            }else{
                                                                gracz_nie_ruszyl_sie=true;

                                                            }

                                                    }




                                                } else gracz_nie_ruszyl_sie=true;

                                            } else gracz_nie_ruszyl_sie=true;


                                    }

                                    // zacznij ruch
                                    if (!gracz_nie_ruszyl_sie) {
                                        gracz_nie_ruszyl_sie=true;
                                        for(int k=0; k<Odczytywanie.odczytana_dlugosc_kroku;k++) {

                                            ((Skrzynka) swiat.get(j)).przesun(-1, 0);

                                            if(przesuwam_druga_skrzynke){
                                                ((Skrzynka) swiat.get(indeks_drugiej_skrzynki)).przesun(-1, 0);

                                            }


                                            gracz.keyPressed(e);
                                            gracz.przesun();

                                            if(m<10){
                                                sokobanIMG = new ImageIcon("ruch/lewo1.png").getImage();
                                                m++;
                                            }else if(m>=10 && m<20){
                                                sokobanIMG = new ImageIcon("ruch/lewo3.png").getImage();
                                                m++;
                                            }else if(m==20){
                                                m=0;
                                            }

                                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                                sokobanIMG = new ImageIcon("ruch/lewo2.png").getImage();

                                            if(trafiono_na_skrzynke){
                                                trafiono_na_skrzynke=false;
                                                skrzynka_na_miejscu = true;
                                            }
                                            repaint();
                                            Thread.sleep(10);
                                        }
                                        indeks_drugiej_skrzynki=0;
                                        Sokoban.przesuniecie_dwoch=false;
                                        przesuwam_druga_skrzynke=false;
                                        Info.aktualizujRuch();
                                    }


                                } else if (swiat.get(j) instanceof MiejscaNaSkrzynki) {
                                    gracz_nie_ruszyl_sie = false;
                                } else {
                                    if(Sokoban.mozna_usunac_sciane) {
                                        usunSciane(j);
                                        repaint();
                                        Sokoban.mozna_usunac_sciane=false;
                                        gracz_nie_ruszyl_sie = true;
                                    }
                                    gracz_nie_ruszyl_sie = true;
                                }

                            }

                        }


                        if (!gracz_nie_ruszyl_sie) {
                            gracz.keyPressed(e);
                            gracz.przesun();

                            if(m<10){
                                sokobanIMG = new ImageIcon("ruch/lewo1.png").getImage();
                                m++;
                            }else if(m>=10 && m<20){
                                sokobanIMG = new ImageIcon("ruch/lewo3.png").getImage();
                                m++;
                            }else if(m==20){
                                m=0;
                            }

                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                sokobanIMG = new ImageIcon("ruch/lewo2.png").getImage();
                            repaint();
                            Thread.sleep(10);




                        }

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!gracz_nie_ruszyl_sie) {
                    Info.aktualizujRuch();

                }

                if(skrzynka_na_miejscu) {

                    skrzynka_na_miejscu=false;

                    tablica_prawdy[stopien_powodzenia] = true;
                    stopien_powodzenia++;

                    if (tablica_prawdy[MAX_liczba_skrzynek - 1])
                        KoniecGry();
                }



            } else if (kod_klucza==KeyEvent.VK_UP){
                sokobanIMG = new ImageIcon("ruch/gora2.png").getImage();
                repaint();
                //  sokobanIMG = new ImageIcon("ruchGora.gif").getImage();
                for (int i = 0; i < Odczytywanie.odczytana_dlugosc_kroku; i++) {
                    try {



                        for (int j = 0; j < swiat.size(); j++) { //sprawdzam wszystkie pola

                            if (gracz.gornaKolizja(swiat.get(j))) {  //kolizja gracza z jakimkolwiek obiektem po lewej stronie


                                if (swiat.get(j) instanceof Skrzynka) {  //kolizja gracza ze skrzynka


                                    for (int l = 0; l < swiat.size(); l++) { //sprawdzenie wszystkich elementow do kolizji ze skrzynka

                                        if(swiat.get(j).gornaKolizja(swiat.get(l)))
                                            //  gracz_nie_ruszyl_sie=true;
                                            if(swiat.get(l) instanceof MiejscaNaSkrzynki)
                                                trafiono_na_skrzynke=true;

                                            else if(swiat.get(l) instanceof Skrzynka){

                                                if(Sokoban.przesuniecie_dwoch){
                                                    przesuwam_druga_skrzynke=true;
                                                    indeks_drugiej_skrzynki=l;
                                                    for(int k =0; k<swiat.size();k++){
                                                        if(swiat.get(l).gornaKolizja(swiat.get(k))) // kolizja drugiej skrzynki
                                                            if(swiat.get(k) instanceof MiejscaNaSkrzynki){
                                                                trafiono_na_skrzynke=true;
                                                                gracz_nie_ruszyl_sie=false;

                                                            }else{
                                                                gracz_nie_ruszyl_sie=true;

                                                            }

                                                    }




                                                } else gracz_nie_ruszyl_sie=true;

                                            } else gracz_nie_ruszyl_sie=true;


                                    }

                                    // zacznij ruch
                                    if (!gracz_nie_ruszyl_sie) {
                                        gracz_nie_ruszyl_sie=true;
                                        for(int k=0; k<Odczytywanie.odczytana_dlugosc_kroku;k++) {

                                            ((Skrzynka) swiat.get(j)).przesun(0, -1);

                                            if(przesuwam_druga_skrzynke){
                                                ((Skrzynka) swiat.get(indeks_drugiej_skrzynki)).przesun(0, -1);

                                            }


                                            gracz.keyPressed(e);
                                            gracz.przesun();

                                            if(m<10){
                                                sokobanIMG = new ImageIcon("ruch/gora1.png").getImage();
                                                m++;
                                            }else if(m>=10 && m<20){
                                                sokobanIMG = new ImageIcon("ruch/gora3.png").getImage();
                                                m++;
                                            }else if(m==20){
                                                m=0;
                                            }

                                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                                sokobanIMG = new ImageIcon("ruch/gora2.png").getImage();

                                            if(trafiono_na_skrzynke){
                                                trafiono_na_skrzynke=false;
                                                skrzynka_na_miejscu = true;
                                            }
                                            repaint();
                                            Thread.sleep(10);
                                        }
                                        indeks_drugiej_skrzynki=0;
                                        Sokoban.przesuniecie_dwoch=false;
                                        przesuwam_druga_skrzynke=false;
                                        Info.aktualizujRuch();
                                    }


                                } else if (swiat.get(j) instanceof MiejscaNaSkrzynki) {
                                    gracz_nie_ruszyl_sie = false;
                                } else {
                                    if(Sokoban.mozna_usunac_sciane) {
                                        usunSciane(j);
                                        repaint();
                                        Sokoban.mozna_usunac_sciane=false;
                                        gracz_nie_ruszyl_sie = true;
                                    }
                                    gracz_nie_ruszyl_sie = true;
                                }

                            }

                        }


                        if (!gracz_nie_ruszyl_sie) {
                            gracz.keyPressed(e);
                            gracz.przesun();

                            if(m<10){
                                sokobanIMG = new ImageIcon("ruch/gora1.png").getImage();
                                m++;
                            }else if(m>=10 && m<20){
                                sokobanIMG = new ImageIcon("ruch/gora3.png").getImage();
                                m++;
                            }else if(m==20){
                                m=0;
                            }

                            if(i==Odczytywanie.odczytana_dlugosc_kroku-1)
                                sokobanIMG = new ImageIcon("ruch/gora2.png").getImage();
                            repaint();
                            Thread.sleep(10);




                        }

                    } catch (InterruptedException ex) {
                        ex.printStackTrace();
                    }
                }
                if (!gracz_nie_ruszyl_sie) {
                    Info.aktualizujRuch();

                }

                if(skrzynka_na_miejscu) {

                    skrzynka_na_miejscu=false;

                    tablica_prawdy[stopien_powodzenia] = true;
                    stopien_powodzenia++;

                    if (tablica_prawdy[MAX_liczba_skrzynek - 1])
                        KoniecGry();
                }

            }
            sleeep(200);
            flagaruchu = true;

        }
    }







