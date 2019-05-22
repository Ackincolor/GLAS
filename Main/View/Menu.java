package Main.View;

import Main.Controler.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.*;

public class Menu extends JMenuBar
{
    public Menu(TreeMap<String, JPanel> panels)
    {
        JMenu filemenu = new JMenu("File");
        
        JMenuItem open = new JMenuItem("Ouvrir");
        open.setMnemonic(KeyEvent.VK_O);
        open.addActionListener(new MenuControl(panels));
        JMenuItem save = new JMenuItem("Sauvegarder");
        save.setMnemonic(KeyEvent.VK_S);
        save.addActionListener(new MenuControl());
        filemenu.add(open);
        filemenu.add(save);
        
        add(filemenu);
        
        JMenu editmenu = new JMenu("Edit");
        editmenu.setMnemonic(KeyEvent.VK_E);
        add(editmenu);

        
        JMenu viewmenu = new JMenu("View");
        viewmenu.setMnemonic(KeyEvent.VK_V);

        JMenuItem piano = new JMenuItem("Ouvrir Piano");
        piano.setMnemonic(KeyEvent.VK_P);
        piano.addActionListener(new AfficherPianoControler());
        viewmenu.add(piano);
         JMenuItem rythme = new JMenuItem("Ouvrir rythme");
        rythme.setMnemonic(KeyEvent.VK_P);
        rythme.addActionListener(new AfficherRythmeControler());
        JMenuItem accueil = new JMenuItem("Accueil");
        accueil.addActionListener(new MenuControl(panels));
        viewmenu.add(accueil);
        viewmenu.add(rythme);
        viewmenu.addSeparator();
        
        JMenu color = new JMenu("Couleurs");
        color.setMnemonic(KeyEvent.VK_C);
    
        
        JRadioButtonMenuItem a = new JRadioButtonMenuItem("Violet");
        a.addActionListener(new ChangerCouleurs(panels));
        JRadioButtonMenuItem b = new JRadioButtonMenuItem("Monochrome");
        b.addActionListener(new ChangerCouleurs(panels));
        JRadioButtonMenuItem c = new JRadioButtonMenuItem("Boue");
        c.addActionListener(new ChangerCouleurs(panels));
        JRadioButtonMenuItem d = new JRadioButtonMenuItem("Clinique");
        d.addActionListener(new ChangerCouleurs(panels));
        
        ButtonGroup jbg = new ButtonGroup();
        
        jbg.add(a);
        jbg.add(b);
        jbg.add(c);
        jbg.add(d);
        
        color.add(a);
        color.add(b);
        color.add(c);
        color.add(d);

        
        viewmenu.add(color);

        add(viewmenu);
        
    }
}