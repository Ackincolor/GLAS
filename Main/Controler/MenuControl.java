package Main.Controler;
import Main.Model.*;
import Main.View.*;
import java.awt.event.*;

import java.awt.*;

import Keybord.Controler.*;
import RythmBox.Controler.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.util.*;

public class MenuControl implements ActionListener
{
    TreeMap<String,JPanel> panels;
    
	public MenuControl(TreeMap<String,JPanel> p)
	{
        this.panels = p;
	}
    
    public MenuControl()
    {
        
    }
    
	public void actionPerformed(ActionEvent e) 
	{ 
    	//afficher la fenetre du piano
        
    	if(((JMenuItem)e.getSource()).getText().startsWith("Piano"))
    		javax.swing.SwingUtilities.invokeLater(new KeybordMain());
    	else if(((JMenuItem)e.getSource()).getText().startsWith("RythmBox"))
            javax.swing.SwingUtilities.invokeLater(new RythmBoxMain());
        else if(((JMenuItem)e.getSource()).getText().startsWith("Ouvrir"))
        {
            JFileChooser choixwindow = new JFileChooser();
            choixwindow.setFileFilter(new FileNameExtensionFilter("Wave File", "wav", "wave"));
            choixwindow.addChoosableFileFilter(new FileNameExtensionFilter("MIDI File", "mid", "midi"));
            choixwindow.setCurrentDirectory(new File("."));
            int retour = choixwindow.showOpenDialog((JMenuItem)e.getSource());
            if(retour == JFileChooser.APPROVE_OPTION)
            {
                File file = choixwindow.getSelectedFile();
                System.out.println(file.getName()+"opened");
                //comparaison midi et wav
                JPanel soundview = (JPanel)this.panels.get("Soundview");
                CardLayout cl = (CardLayout)soundview.getLayout();
                //soundview.removeAll();

                if(getFileExtension(file).equals("mid"))
                {
                    System.out.println("MIDIIIII");
                    MidiView wave = (MidiView)this.panels.get("Wave");
                    cl.show(soundview,"V2");
                    wave.draw(file);
                }
                else
                {

                    System.out.println("WAVVVVVVV");
                    Onde onde = (Onde)this.panels.get("Onde");
                    cl.show(soundview,"V1");
                    onde.draw(file);
                     //((Onde)this.panels.get("Onde")).draw(file);
                    Son son = new Son(file); 
                    ((Playback)this.panels.get("Playback")).setSound(son); 
                    ((Sliders)this.panels.get("Sliders")).setSound(son);
                    son.setCursor(onde.getTimeCursor());
                    onde.getTimeCursor().setSon(son);
                }
                //((Onde)this.panels.get("Onde")).draw(file);
                //((Playback)this.panels.get("Playback")).setSound(((Onde)this.panels.get("Onde")).getSound());
                //((Sliders)this.panels.get("Sliders")).getSliderControl().setSound(((Onde)this.panels.get("Onde")).getSound());
            }
        }
        else if(((JMenuItem)e.getSource()).getText().startsWith("Accueil"))
        {
            JPanel soundview = (JPanel)this.panels.get("Soundview");
            CardLayout cl = (CardLayout)soundview.getLayout();
            cl.show(soundview,"V3");
        }
        else if(((JMenuItem)e.getSource()).getText().startsWith("Sauvegarder"))
        {
            JFileChooser choixwindow = new JFileChooser();
            choixwindow.setCurrentDirectory(new File("."));
            int retour = choixwindow.showSaveDialog((JMenuItem)e.getSource());
            if(retour == JFileChooser.APPROVE_OPTION)
            {
                File file = choixwindow.getSelectedFile();
                System.out.println(file.getName()+"opened");
            }
        }
    	else
    		System.out.println("rien a faire");
	}
    private String getFileExtension(File file) {
        String name = file.getName();
        try {
            //System.out.println(name.substring(name.lastIndexOf(".") + 1));
            return name.substring(name.lastIndexOf(".") + 1);
        } catch (Exception e) {
            return "";
        }
    }
}