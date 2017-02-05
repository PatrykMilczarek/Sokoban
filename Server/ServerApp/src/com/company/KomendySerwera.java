package com.company;

/**
 * Created by Patryk on 2016-06-17.
 */


import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Klasa odpowiadajaca za wykonywanie komend przeslanych do serwera przez klienta. Opisuje sposób działania serwera w zależności od otrzymanej komendy od klienta.
 */

public final class KomendySerwera {


    /**
     * Metoda obslugująca zadania od serwera.
     * Gdy żądanie to POBIERZ_POZIOM lub WYNIK_GRY, to dzielimy to na komendę i parametr, który potem wykorzystujemy do metod.
     * Np w przypadku pobierz poziom jest to numer poziomu.
     * @param command komenda otrzymana od klienta
     * @return zwraca odpoweidź serwera
     */
    public static String dzialanieSerwera(String command){
        String komenda_serwera = command;
        String komenda_oryginalna= command;


        if(command.contains("POBIERZ_POZIOM:")){
            komenda_oryginalna=command;   
            komenda_serwera=("POBIERZ_POZIOM");
        }
            if(command.contains("WYNIK_GRY:")){
            komenda_oryginalna=command;
            komenda_serwera="WYNIK_GRY";
        }


        String wiadomosc_serwera;
        switch (komenda_serwera){
            case "ZALOGUJ":
                wiadomosc_serwera=zaloguj();
                break;
            case "POBIERZ_USTAWIENIA":
                wiadomosc_serwera=pobierzUstawienia();
                break;

            case "POBIERZ_WYNIKI":
                wiadomosc_serwera=pobierzWyniki();
                break;

            case "POBIERZ_POZIOM":
                String str[] = komenda_oryginalna.split(":");
                wiadomosc_serwera=pobierzPoziom(Integer.parseInt(str[1]));
                break;
            case "WYNIK_GRY":

                String str1[] = komenda_oryginalna.split(":");
                String str2[] = str1[1].split("@");
                wiadomosc_serwera=aktualizacjaWynikow(str2[0],Integer.parseInt(str2[1]));
                break;

            default:
                wiadomosc_serwera="NIEPRAWIDLOWA_KOMENDA";
        }
        return wiadomosc_serwera;
    }

    /**
     * Metoda obsługująca logowanie. Określa działanie serwera na żądanie ZALOGUJ.
     * @return wiadomość od serwera na POBIERZ_USTAWIENIA.
     */

    private static String zaloguj() {
        String wiadomosc_serwera;

            wiadomosc_serwera = "ZALOGOWANO POMYSLNIE";

            return wiadomosc_serwera;
        }


    /**
     * Metoda obslugująca wysylanie do klienta ustawień. Okresla działanie serwera na żądanie POBIERZ_USTAWIENIA.
     * Odpowiedż serwera na zadanie POBIERZ_USTAWIENIA.
     * @return ciag znakow z ustawieniami
     */
    private static String pobierzUstawienia(){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\Ustawienia.txt"))){
            String currentLine;
            while ((br.readLine()) != null) {
                currentLine =br.readLine();
                System.out.println(currentLine);
                sb.append(currentLine+"@");
            }
			br.close();
        }
        catch (Exception e){

        }

        return sb.toString();
    }



    /**
     *  Metoda obsługująca wysyłanie do klienta ustawień.
     *  Określa działanie serwera na żądanie POBIERZ_WYNIKI.
     *  Dane sa pobierane z odpowiedzniego plkiu konfiguracyjnego.
     *  Między poszczególnymi informacjami dodawana jest "@".
     * @return odpowiedż serwera na żądanie POBIERZ_WYNIKI.
     */

    private static String pobierzWyniki(){

        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\Wyniki.txt"))){
            String currentLine;

            br.readLine();
            while ((currentLine = br.readLine()) != null) {
                sb.append(currentLine+"@");
            }
			br.close();
        }
        catch (Exception e){

        }

        return sb.toString();
    }


    /**
     * Metoda pobieraj�ca dane poziomu. Jest on pobierany z odpowiednigo pliku.Określa sposób odpowiedniego utworzenia wiadomosci.
     * @param nr_poziomu numer poziomu. Określa, z którego pliku ma być wczytany dany poziom.
     * @return odpowiedź od serwera
     */

    private static String pobierzPoziom(int nr_poziomu){
        StringBuilder sb = new StringBuilder();
        try (BufferedReader br = new BufferedReader(new FileReader("PlikiKonfiguracyjne\\"+"Poziom"+nr_poziomu+".txt"))){
            String currentLine;
            while ((currentLine=br.readLine()) != null) {
                if(currentLine.equals("#PLANSZA"))
                    while(!(currentLine=br.readLine()).equals("#"))
                    sb.append(currentLine+"@");
                else {
                    currentLine=br.readLine();
                    sb.append(currentLine+"@");
                }
            }
			br.close();
        }
        catch (Exception e){

        }
        return sb.toString();
    }

    /**
     * Metoda aktualizująca liste wyników. Jej argumenty, określają jakie są dane gracza, którego chcemy wpisac na liste.
     * Następnie sprawdzamy, czy jego wynik jest na tyle duży, że można go zapisać.
     * Jeśli tak odpowdż to ZAPISANO DO LISTY, a jeśli nie możnatego zrobić, bo wynik gracza jest za mały to wysyła NIE ZAPISANO DO LISTY.
     * @param nick nick gracza
     * @param punkty licbza zdobytych punktów przez gracza
     * @return odpowiedź od serwera, zależna od tego, czy gracz trafił na listę najlepszych wyników, czy nie.
     */

    private static String aktualizacjaWynikow(String nick, int punkty){

        ArrayList<Para> lista_najlepszych_wynikow;
        lista_najlepszych_wynikow = new ArrayList<>();
        lista_najlepszych_wynikow=zaladujZPliku(lista_najlepszych_wynikow);
        String odpowiedz="NIE ZAPISANO DO LISTY";
        for(int i=0;i<10;i++){
            if(lista_najlepszych_wynikow.get(i).zwrocPunkty()<punkty){
                odpowiedz="ZAPISANO DO LISTY";
                break;
            }
        }

        dodajWynik(lista_najlepszych_wynikow,nick,punkty);
        return odpowiedz;
    }

    /**
     * Metoda ta służy do dodaniu wyniku gracza do listy.Jest uruchamiana posrednio, przez komendę AKTUALIZUJWYNIKI.
     * Wywołuje ona metodę sortowania, aby lista zaczynała się na największym wyniku i szła do najmniejszego.
     * @param lista_najlepszych_wynikow lista wyników, składająca się z obiektów par, gdzie każda para to nick i liczba punktów.
     * @param nick nick gracza
     * @param punkty liczba punktów
     */

    private static void dodajWynik(ArrayList<Para> lista_najlepszych_wynikow,String nick, int punkty){

        lista_najlepszych_wynikow.add(new Para(nick,punkty));
        posortujListe(lista_najlepszych_wynikow);
        zapiszWynikiDoPliku(lista_najlepszych_wynikow);
    }

    /**
     * Metoda wczytująca liste wynikow z pliku. Kazda para jest oddzielona @, a wewnątrz niej, nick i liczba punktów oddzielone są dodatkowo ",".
     * @param lista_najlepszych_wynikow lista wyników, składa się z obiektów par, czyli zawierającym nick i liczbe punktów gracza.
     * @return odpowied� od serwera
     */

    private static ArrayList<Para> zaladujZPliku(ArrayList<Para> lista_najlepszych_wynikow) {
            String listaString = pobierzWyniki();
            String str1[] = listaString.split("@");

            for (int i = 0; i < 10; i++) {
                String str2[] = str1[i].split(",");
                lista_najlepszych_wynikow.add(new Para(str2[0], Integer.parseInt(str2[1])));
            }

            return lista_najlepszych_wynikow;
        
    }

    /**
     * Metoda zapisująca wyniki do pliku. Metoda ta zapisuje wyniki aktualne do pliku tekstowego.
     * @param lista_najlepszych_wynikow  lista wyników, Są w niej przechowywane nick i liczba punktów gracza.
     */

    private static void zapiszWynikiDoPliku(ArrayList<Para> lista_najlepszych_wynikow) {

        try{
            File sciezka = new File("PlikiKonfiguracyjne\\Wyniki.txt");
            BufferedWriter zapis = new BufferedWriter(new FileWriter(sciezka));
            zapis.write("#Najlepsze Wyniki \n");
            for(int i =0; i <10;i++) {
                zapis.write(lista_najlepszych_wynikow.get(i).zwrocNick() + "," + lista_najlepszych_wynikow.get(i).zwrocPunkty()+"\n");
            }
            zapis.close();
        }
        catch(IOException e)
        {
            e.printStackTrace();
        }

    }

    /**
     * Metoda sortująca liste wyników. Porównuje pokolei liczbę punktów zdobytych przez graczy i zwraca 1, gdy jest większa, 0 gdy są takie same i -1 gdy wynik gracza jest mniejszy.
     * @param lista_najlepszych_wynikow lista wyników
     */

    private static void posortujListe(ArrayList<Para> lista_najlepszych_wynikow) {
        Collections.sort(lista_najlepszych_wynikow, new Comparator<Para>() {
            @Override
            public int compare(Para Para, Para t1) {
                if (Para.zwrocPunkty() < t1.zwrocPunkty()) {
                    return 1;
                }
                if (Para.zwrocPunkty() > t1.zwrocPunkty()) {
                    return -1;
                } else
                    return 0;


            }
        });
    }






}

