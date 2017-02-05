package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/***
 * Klasa Serwer, dziedziczy Jframe, Wyswietla się zaraz po włączeniu aplikacji. Służy do przejścia do dalszej części aplikacji.
 */
public class Serwer extends JFrame {

    /***
     * Port na którym działa serwer. Jest potrzebny do połączenia się z kliente. Musi być wolny. Jest przechowywany w plkiu ipconfig.txt.
     */
    private int port_serwera;
    /**
     * Przycisk włączajcy główną część aplikacji.
     * Po jego naciśnięciu serwer, zmienia status na online i jest gotowy na przyjęcię żądnia.
     */
    private JButton przycisk_wlacz;
    /***
     * Zmienna określająca czy serwer jest połączony czy nie.
     */
    private static boolean serwer_online=false;

    /***
     * Konstruktor klasy Serwer. Ustawia posdtawowe parametry okienka: tytuł, przycik, tworzy wątek odpalający serwer.
     */
    public Serwer(){
        setTitle("Aplikacja Serwera");
        przycisk_wlacz = new JButton("Włącz");

        Thread t = new Thread(new Runnable() {
            @Override
            public void run() {
                odpalSerwer();


            }
        });
        /***
         * Przetwarza zdarzenie, które ma byc wykonane po wciśnięciu przyciusku włącz.
         * Odbywa się to w zależności czy serwer jest noline czy nie. Jeśli nie, to zmieniamy jestwartość na true i odpalamy serwer.
         * Jęsli jest już online to wyświetla się okienko z kominikatem że serwer jest podłączony i przyciskiem ok.
         */
        przycisk_wlacz.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(!serwer_online){
                    serwer_online=true;
                t.start();}
                else{
                    JFrame serwer_juz_wlaczony = new JFrame("Serwer jest podlaczony!");
                    JButton ok= new JButton("Ok");
                    serwer_juz_wlaczony.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    serwer_juz_wlaczony.add(new JLabel("Serwer jest juz wlaczony!"));
                    serwer_juz_wlaczony.add(ok);
                    serwer_juz_wlaczony.setLayout(new FlowLayout());
                    serwer_juz_wlaczony.setSize(200,80);


/***
 * Określa  dzialanie wywołane naciśniećiem przycisku ok, zamyka nasze okienko.
 */
                    ok.addActionListener(new ActionListener() {
                        @Override
                        public void actionPerformed(ActionEvent e) {
                            serwer_juz_wlaczony.dispose();
                        }
                    });
                    pack();
                    serwer_juz_wlaczony.setResizable(false);
                    serwer_juz_wlaczony.setVisible(true);
                }

            }
        });



        setLayout(new FlowLayout());
        add(przycisk_wlacz);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        pack();
        setResizable(false);
        setVisible(true);
    }

    /***
     * Metoda służąco do przygotowania serwer do działania. Pobiera z pliku numer portu. Tworzy z jego pomoc odpowiednie gniiazdo.
     * Oczekuje na połączeni od kllienta i tworzy nowy wątek.
     */
    public void odpalSerwer() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\ip.txt"));
            port_serwera = Integer.parseInt(br.readLine());
            ServerSocket serverSocket = new ServerSocket(port_serwera);
            System.out.println("Serwer jest online! Oczekiwanie na połączenie...");
            br.close();
            while (true) {
                Socket socket = serverSocket.accept();
                new Thread(new WatekSerwera(socket)).start();
            }

        }
        catch (IOException e){
            System.out.println("Serwer nie mógł zostać uruchomiony");
            System.err.println(e);
        }
    }



}
