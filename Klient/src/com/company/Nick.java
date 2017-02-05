package com.company;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * Klasa opisujaca wyglad okna do wpisywania nazwy gracza.
 */
public class Nick extends JPanel {

    /**
     * Zmienna przechowujaca przycisk sluzacy do pobrania nicku i startu gry.
     */
    JButton ok = new JButton("Ok");

    /**
     * Przycisk do powrotu do glownego menu.
     */
    JButton powrot = new JButton("Powrót");

    /**
     * Pole do wpisywania nicku.
     */
    JTextField tekst = new JTextField(10);

    /**
     * Obrazek w tle okna z nickiem.
     */
    Image nickIMG = new ImageIcon("nick.png").getImage();



    /**
     * Konstruktor bezparametrowy. Tutaj konstruowany jest wyglad calego okna, ustalane wlasciwosci przyciskow.
     */
    public Nick(){

        GridBagConstraints gbc;
        JPanel panel_dolny;
        JPanel panel_srodkowy;


        tekst.setFont(new Font("Comic Sans",Font.PLAIN,25));
        tekst.setText("Gall Anonim");


        setLayout(new GridBagLayout());



        panel_dolny= new JPanel();
        panel_srodkowy= new JPanel();

        gbc = new GridBagConstraints();
        gbc.weighty=1;
        gbc.anchor=GridBagConstraints.NORTH;
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.insets= new Insets(30,0,-15,0);


        panel_srodkowy.add(tekst);
        panel_srodkowy.setOpaque(false);
        gbc.gridy = 1;
        gbc.insets= new Insets(200,0,0,0);
        add(panel_srodkowy,gbc);

        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                Gracz.nick=tekst.getText();
                GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli,"4");
                GlowneOkno.klasa_planszy.addKeyListener(new KeyListener() {
                    @Override
                    public void keyTyped(KeyEvent e) {

                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        GlowneOkno.klasa_planszy.gra.keyPressed(e);
                    }

                    @Override
                    public void keyReleased(KeyEvent e) {

                    }
                });

                GlowneOkno.klasa_planszy.setFocusable(true);
                GlowneOkno.klasa_planszy.requestFocusInWindow();




            }
        });

        powrot.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {

                GlowneOkno.layout_kartkowy.show(GlowneOkno.zbior_paneli,"1");
            }
        });
        ok.setPreferredSize(new Dimension(3 * 50, 50));
        ok.setBackground(new Color(180,30,40));
        ok.setFont(new Font("Comic Sans", Font.PLAIN, 20));
        ok.setForeground(Color.WHITE);

        powrot.setPreferredSize(new Dimension(3 * 50, 50));
        powrot.setBackground(new Color(180,30,40));
        powrot.setForeground(Color.WHITE);
        powrot.setFont(new Font("Comic Sans", Font.PLAIN, 20));


        panel_dolny.add(ok);
        panel_dolny.setOpaque(false);
        panel_dolny.add(powrot);
        gbc.gridy= 1;
        gbc.insets= new Insets(300,0,0,0);


        add(panel_dolny,gbc);
        setVisible(true);

    }

    /**
     * Metoda sluzaca do rysowania obrazka w tle okna z miejscem na podanie nicku.
     * @param g kontekst graficzny
     */
   public void paintComponent(Graphics g){
        g.drawImage(nickIMG, 0, 0, this.getWidth(), this.getHeight(), this);
    }
}

