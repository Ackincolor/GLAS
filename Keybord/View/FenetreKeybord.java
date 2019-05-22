package Keybord.View;

import javax.swing.*;
import java.awt.*;
import Keybord.Model.*;
import Keybord.Controler.*;
public class FenetreKeybord extends JFrame
{
	private PianoView piano;
	private OptionPanel optionPanel;
	private SynthModel synthModel;
	private JScrollPane instruPanel;
	final int hauteur = 100;
	final int largeur = 22;
	public FenetreKeybord()
	{
		this.synthModel = new SynthModel();
		//this.setLayout(new GridLayout(2,0));
		this.setSize(70*22+22,700);
		this.optionPanel = new OptionPanel(this.synthModel);
		this.piano = new PianoView(this.synthModel,this.optionPanel);
		this.optionPanel.setFocus(this.piano);
		this.instruPanel = this.optionPanel.getListe();
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.add(this.piano,BorderLayout.NORTH);
		this.add(this.optionPanel,BorderLayout.CENTER);
		this.add(this.instruPanel,BorderLayout.SOUTH);
		this.addWindowListener(new WindowListenerPiano(this));
	}
	public void close()
	{
		this.synthModel.close();
	}
	public PianoView getPianoView()
	{
		return this.piano;
	}
}