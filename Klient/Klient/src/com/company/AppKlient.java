package com.company;



import javax.swing.*;
import java.net.Socket;

/**
* @author Patryk Milczarek
 * @author Patrycja Karwat
 * */

/**
 *  Klasa sluzaca do zaladowania glownego okna menu.
 */
public class AppKlient {

    /**
     * Zmienna przetrzymujaca glowne okno menu.
     */
    public static GlowneOkno glowne_okno;

  

    /**
     * Metoda startu programu. Jezeli uda sie ustanowic polaczenie serwerem, to mozna grac online.
     * Jezeli nie, to uzytkownik ma do wyboru, czy chce grac offline, czy wcale.
     * @param socket Socket serwera
     */
    public static void rozpocznijGre (Socket socket) {
        Socket serwerSocket = socket;
        if(serwerSocket!=null) {
            glowne_okno= new GlowneOkno(serwerSocket);
        }
        else {
            Object[] options={
                    "Tak","Nie"
            };
            System.out.println("Offline");
            switch(JOptionPane.showOptionDialog(null, "Nie moglismy polaczyc cie z serwerem. Czy chcesz zagrać offline?", "Chcesz zagrać offline?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE,null,options,options[1])){
                case JOptionPane.YES_OPTION:
                    glowne_okno= new GlowneOkno(serwerSocket);
                    break;
                case JOptionPane.NO_OPTION:
                    System.exit(0);
                    break;
                default:
                    System.exit(0);
                    break;
            }
        }



    }






}

