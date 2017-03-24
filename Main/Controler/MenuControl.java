package Main.Controler;
import Main.Model.*;
import Main.View.*;
import java.awt.event.*;
import Keybord.Controler.*;
import RythmBox.Controler.*;
import javax.swing.*;
import java.io.*;
import javax.swing.filechooser.FileNameExtensionFilter;
public class MenuControl implements ActionListener
{
    private Playback playback;
    private Onde wave;
    
	public MenuControl(Playback p,Onde wave)
	{
        this.playback = p;
        this.wave=wave;
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
            choixwindow.setFileFilter(new FileNameExtensionFilter("Wave FIle", "wav", "wave"));
            choixwindow.setCurrentDirectory(new File("."));
            int retour = choixwindow.showOpenDialog((JMenuItem)e.getSource());
            if(retour == JFileChooser.APPROVE_OPTION)
            {
                File file = choixwindow.getSelectedFile();
                System.out.println(file.getName()+"opened");
                this.wave.draw(file);
                this.playback.setSound(this.wave.getSound());
            }
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
}