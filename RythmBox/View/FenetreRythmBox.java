package RythmBox.View;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import javax.swing.event.*;
import javax.sound.midi.*;
import java.util.Vector;
import RythmBox.Controler.*;
import RythmBox.Model.*;;

public class FenetreRythmBox extends JPanel{   
    private RythmModel model;
    public FenetreRythmBox(JFrame f) 
    {
        this.setLayout(new BorderLayout(5,0));
        EmptyBorder eb = new EmptyBorder(5,5,5,5);
        this.setBorder(eb);
        this.model = new RythmModel();
        this.add(new TempoDial(this.model),BorderLayout.CENTER);
        JButton boutton = new JButton("Tempo");
        this.add(boutton,BorderLayout.SOUTH);
        boutton.addActionListener(new ActionListener()
        {
        	public void actionPerformed(ActionEvent e)
        	{
        		model.setIsPlayingToogle();
        	}
        });
        f.addWindowListener(new WindowListener()
        {
			public void windowActivated(WindowEvent e)
			{

			}
			public void windowClosed(WindowEvent e)
			{
				model.close();
			}
			public void windowClosing(WindowEvent e)
			{
				model.close();
			}
			public void windowDeactivated(WindowEvent e)
			{
				
			}
			public void windowDeiconified(WindowEvent e)
			{
				
			}
			public void windowIconified(WindowEvent e)
			{
				
			}
			public void windowOpened(WindowEvent e)
			{
				
			}
        });
    }
}