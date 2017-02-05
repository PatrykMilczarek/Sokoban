package com.company;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;
import java.util.StringTokenizer;

/**
 * Created by Patryk on 2016-06-17.
 */
public class WatekSerwera implements Runnable {
    /***
     * Gniazdo serwera
     */
    private Socket socket;
    /***
     *Obiekt klasy BufferdReader, umożliwia odpczytywanie tekstu liniami, dzięki metody readLine().
     */
    private BufferedReader input;
    /***
     * Obiekt klasy PrintWriter, strumień służoący do odczytania z plkiu.
     */
    private PrintWriter output;

    /**
     * Konstruktor klasy.
     *
     * @param socket gniazdo
     */
    public WatekSerwera(Socket socket) {
        this.socket = socket;
    }

    /**
     * Metoda obsługująca wszystkie zdarzenia pomiedzy klientem i serwerem. Odczytuje i  wysyła wiadomości między nimi.
     * Określa zadanie utworzonego wątku.
     */

    @Override
    public void run() {
        try {
            while (true) {



                InputStream inputStream = socket.getInputStream();
                input = new BufferedReader(new InputStreamReader(inputStream));
                OutputStream os = socket.getOutputStream();
               output = new PrintWriter(os, true);




                String od_klienta = input.readLine();





                if (od_klienta != null) {
                    System.out.println("WIADOMOSC OD KLIENTA: " +od_klienta);
                    String wiadomosc_serwerowa = KomendySerwera.dzialanieSerwera(od_klienta);


                        output.println(wiadomosc_serwerowa);
                        output.flush();
                        System.out.println("WYSYLAM DO KLIENTA: " + wiadomosc_serwerowa);


                } else {
                    
                    System.out.println("Pusta komenda");
                }



            }
        } catch (Exception e) {
            System.out.println("WYLOGOWANO");
        }

    }



}

