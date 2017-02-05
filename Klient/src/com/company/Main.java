package com.company;

import java.io.*;
import java.net.Socket;



/**
 * Klasa glowna, z ktorej inicjujemy glowne menu.
 */
public class Main {


    /**
     * Adres Ip serwera
     */
    private static String adres_IP;
    /**
     * nr_portu serwera
     */
    private static int nr_portu;

    /**
     * Metoda main clienta
     * @param args argumenty
     */
    public static void main(String[] args){
        AppKlient.rozpocznijGre(polaczZSerwerem());

    }


    /**
     * Metoda odpowiedzialna za nawiązanie połączenia. Najpierw wysylana jest komenda ZALOGUJ i jezeli otrzymamy potwierdzenie z serwera,
     * to zwracany jest gniazdo tego serwera.
     * @return gniazdo serwera
     */
    private static Socket polaczZSerwerem() {
        try {
            BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\IPCONFIG.txt"));
            adres_IP=br.readLine();
            nr_portu=Integer.parseInt(br.readLine());
            Socket serverSocket = new Socket(adres_IP, nr_portu);
            OutputStream os = serverSocket.getOutputStream();
            PrintWriter pw = new PrintWriter(os, true);
            pw.println("ZALOGUJ");

            InputStream is = serverSocket.getInputStream();

            br = new BufferedReader(new InputStreamReader(is));


            if(br.readLine().contains("ZALOGOWANO POMYSLNIE")){

                return serverSocket;
            }
            else{

                return null;
            }



        }
        catch (Exception e) {
            System.out.println("Połączenie nie mogło zostać zestawione...");
            System.out.println("Blad: "+e);
        }
        return null;
    }






}


