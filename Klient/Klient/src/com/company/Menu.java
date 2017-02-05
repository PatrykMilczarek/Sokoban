package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Klasa glownego menu.
 */
public class Menu extends JPanel {


    /**
     * Przycisk inicjujacy nowej gry.
     */
    JButton p1;

    /**
     * Przycisk inicjujacy okno z najlepszymi wynikami.
     */
    JButton p2;

    /**
     * Przycisk sluzacy do wyjscia z menu.
     */
    JButton p3;

    /**
     * Zmienna przechowujaca obrazek tla menu.
     */
    Image menuIMG = new ImageIcon("menu.png").getImage();




    /**
     * Konstruktor bezparametrowy. Tutaj ustawiane sa wlasciwosci wszyskich przyciskow.
     */
    public Menu() {
        setLayout(new GridBagLayout());

        GridBagConstraints gbc;


        //ustawienie szerokosci i wysokosci ramki oraz polecenie systemowi, by ustalil jej polozenie
        gbc = new GridBagConstraints();
        gbc.insets= new Insets(5,5,50,5);



       // setContentPane(new Obrazek(image));

        gbc.weighty=1;
        gbc.anchor=GridBagConstraints.SOUTH;

        p1= new JButton("<html><b>Nowa Gra</b></html>");
        p1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli,"3");

            }
        });
        gbc.gridx=0;
        gbc.gridy=10;
        gbc.ipadx=10;
        gbc.ipady=10;

        p1.setForeground(Color.white);
        p1.setPreferredSize(new Dimension(3 * 40, 40));
        p1.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        p1.setBackground(new Color(180,70,40));
        p1.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));



        gbc.fill=GridBagConstraints.HORIZONTAL;

        add(p1,gbc);
        p2= new JButton("Najlepsze Wyniki");
        p2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli,"2");

            }
        });

        p2.setForeground(Color.white);
        p2.setPreferredSize(new Dimension(3 * 55, 40));
        p2.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        p2.setBackground(new Color(170,90,40));
        p2.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       // p2.setContentAreaFilled(false);

        gbc.gridx=1;

        add(p2,gbc);
        p3= new JButton("Wyjście");
        p3.setPreferredSize(new Dimension(3 * 40, 40));
        p3.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        p3.setForeground(Color.white);
        p3.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        p3.setBackground(new Color(170,90,40));
        p3.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
       // p3.setContentAreaFilled(false);

        gbc.gridx=2;

        add(p3,gbc);
        setVisible(true);

    }


    /**
     * Metoda odpowiadajaca za rysowanie obrazka menu w tle.
     * @param g kontekst graficzny
     */
    public void paintComponent(Graphics g){
    super.paintComponent(g);
    g.drawImage(menuIMG,0,0,this.getWidth(), this.getHeight(), this);


}

}




