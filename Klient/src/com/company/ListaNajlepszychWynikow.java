package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.io.*;
import java.net.Socket;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Klasa przechowujaca i aktualizujaca liste najlepszych wynikow uzyskanych w grze.
 */
public class ListaNajlepszychWynikow extends JPanel{

    /**
     * Zmienna przechowujaca nazwe pliku z najlepszymi wynikami.
     */
    String plik_wyniki = "Wyniki.txt";

    /**
     * Zmienna przechowujaca przycisk sluzacy do powrotu do menu.
     */
    JButton p1 = new JButton("Powrót");

    /**
     *  Zmienna przechowujaca panel glowny, do ktorego dodawany jest panel z lista najlepszych wynikow.
     */
    JPanel panel_glowny;

    /**
     *  Zmienna przechowujaca liste graczy. Uzywana do wyswietlania wynikow oraz sortowania.
     */
    static ArrayList<Gracz> lista;

    /**
     * Zmienna przechowujaca panel z lista wynikow, ktory znajduje sie po lewej stronie glownego okna.
     */
    Lista panel_z_wynikami_lewy;

    /**
     *  Zmienna przechowujaca obiekt typu Odczytywanie. Sluzy do wywolania metody odczytujacej wyniki z pliku.
     */
    static Odczytywanie odcz;

    /**
     * Zmienna przechowujaca obrazek tla glownego okna.
     */
    Image listaIMG = new ImageIcon("najlepsze.png").getImage();

    /**
     * Zmienna mowiaca, czy odczytujemy wyniki z serwera, czy z lokalnego pliku.
     */
    static boolean  online=false;

    /**
     * Zmienna przechowujaca gniazdo serwera.
     */
    private static Socket serwer_socket;


    /**
     * Konstruktor bezparametrowy klasy ListaNajlepszychWynikow.
     * Tutaj lista jest odczytywana z pliku lub z serwera, a nastepnie pokazywana w oknie.
     * @param on okresla, czy lista ma byc pobrana z serwera czy z pliku
     * @param gniazdo gniazdo serwera
     */
    public ListaNajlepszychWynikow(boolean on,Socket gniazdo) {
        online=on;
        serwer_socket=gniazdo;
        JLabel napis = new JLabel("Najlepsze wyniki");
        setLayout(new BorderLayout());
        try {
            Font customFont = Font.createFont(Font.TRUETYPE_FONT, new File("LemonMilk.otf")).deriveFont(12f);
            GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("LemonMilk.otf")));
            napis.setFont(customFont);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (FontFormatException e) {
            e.printStackTrace();
        }
        odcz = new Odczytywanie();

        lista = new ArrayList();


        p1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {


                GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli,"1");

            }
        });

        panel_glowny = new JPanel(new GridLayout(1, 1));

        for (int i = 0; i < 10; i++) {
            lista.add(new Gracz());
        }


        if(on){
            pobierzListezSerwera(serwer_socket,lista);
        }else {
            odcz.odczytajListe(plik_wyniki, lista);
        }


        panel_z_wynikami_lewy = new Lista(10);
        panel_glowny.add(panel_z_wynikami_lewy);

        add(BorderLayout.CENTER, panel_glowny);



    }


    /**
     * Metoda sluzaca do posortowania nowego wyniku dodanego do listy oraz dodania informacji o graczu do pliku,
     * lub wyslaniu informacji o graczu  i jego punktacji na serwer.
     * @param liczbapkt liczba punktow uzyskana przez gracza
     * @param nick nick gracza
     */
    public static void dodajWynikiSortuj(int liczbapkt, String nick){

        if(online)
        {
            wyslijWynikDoSerwera(nick,liczbapkt);
        }else {


            lista.add(new Gracz(liczbapkt, nick));
            Collections.sort(lista);


            odcz.zapiszListe("Wyniki.txt", lista);
        }

    }

    /**
     * Metoda odpowiadajaca za wyslanie wynikow gracza do serwera.
     * @param nick nick gracza
     * @param pkty liczba punktow
     */
    public static void wyslijWynikDoSerwera(String nick, int pkty){

            try {
                OutputStream os = serwer_socket.getOutputStream();
                PrintWriter pw = new PrintWriter(os, true);
                pw.print("WYNIK_GRY:"+nick+"@"+pkty);


            } catch (IOException e) {
                System.out.println("Plik nie mógł zostać pobrany z serwera");
                System.out.println(e);
            }




    }


    /**
     * Pobranie listy wyników z serwera. Najpier wysylane jest zadanie "POBIERZ WYNIKI", a wtedy odbierany jest pojedynczy
     * ciag znakow, ktory nastepnie jest rozdzielany znakiem '@' na ciagi znakow skladajace sie z nicku i liczby punktow.
     * @param serverSocket gniazdo serwera
     * @param listaa lista, do ktorej zapisywane sa wyniki
     */
    public void pobierzListezSerwera(Socket serverSocket,ArrayList<Gracz> listaa){


        try {

            System.out.println(serverSocket);

            OutputStream os = serverSocket.getOutputStream();

            PrintWriter pw = new PrintWriter(os, true);

            pw.print("POBIERZ_WYNIKI");

            InputStream is = serverSocket.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(is));

            String komenda = br.readLine();



            String str[] = komenda.split("@");


            for (int i =0; i<10;i++){

                tworzListe(str[i],listaa,i);
            }




        } catch (IOException ex) {
            System.out.println("Błąd typu I/O przy pobieraniu listy z serwera");
        }


    }

    /**
     * Metoda tworzaca liste na podstawie ciagow znakow zlozonych z nicku, przecinka i liczby punktow.
     * Na poczatku ciag rozdzialny jest przecinkiem, a potem kolejny element w liscie zyskuje nick i liczbe punktow.
     * @param wiersz_danych aktualny wiersz danych
     * @param lista lista, do ktorej zapisujemy odczytane wyniki
     * @param p zmienna uzywana do zmiany indeksu listy podczas zapisywania
     */
    public void tworzListe(String wiersz_danych,ArrayList<Gracz> lista,int p){
        String[] wierszeListy = wiersz_danych.split(",");


        lista.get(p).ustawNick(wierszeListy[0]);
        lista.get(p).wstawPunktyDoListy(Integer.parseInt(wierszeListy[1]));


    }


    /**
     * Klasa panelu, w ktorej dodawane sa kolejne najlepsze wyniki z listy.
     */
    class Lista extends JPanel {


        /**
         * Konstruktor z parametrami. Tutaj ustawiane jest rozlozenie wynikow.
         * @param koniec parametr odpowiadajacy z ilosc wynikow na stronie.
         */
        public Lista(int koniec) {

           setLayout(new GridBagLayout());

            GridBagConstraints gbc = new GridBagConstraints();
            gbc.gridx = 0;
            gbc.insets = new Insets(0,-300,0,0);
            gbc.weighty = 1;
            gbc.anchor = GridBagConstraints.WEST;
            gbc.gridy = 0;
            JLabel nick = new JLabel("Nick");
            nick.setFont(new Font("Comic Sans", Font.PLAIN,20));
            nick.setForeground(Color.WHITE);
            add(nick,gbc);
            for (int i = 0; i < koniec; i++) {
                gbc.gridy=i+1;


                add(new JLabel((i+1)+". "+lista.get(i).zwrocNick()),gbc);
            }
            gbc.gridy=0;
            gbc.gridx=2;
            gbc.insets = new Insets(0,-200,0,0);
            JLabel punkty = new JLabel("Punkty");
            punkty.setForeground(Color.WHITE);
            punkty.setFont(new Font("Comic Sans", Font.PLAIN,20));
            add(punkty,gbc);

            for (int i = 0; i < koniec; i++) {
                gbc.gridy=i+1;
                add(new JLabel(""+lista.get(i).zwrocPunkty()),gbc);
            }


            gbc.gridy++;
            gbc.gridx=0;
            gbc.insets=new Insets(0,-50,0,0);

          add(p1,gbc);

        }


        /**
         * Metoda odpowiadajaca za rysowanie obrazka w oknie z lista najlepszych wynikow.
         * @param g kontekst graficzny
         */
        public void paintComponent(Graphics g){
            g.drawImage(listaIMG, 0, 0, this.getWidth(), this.getHeight(), this);
        }
    }
}

