package Keybord.View;
import javax.swing.*;
import java.awt.event.*;
import Keybord.Model.*;
import javax.swing.event.*;
import Keybord.Controler.*;
public class SelectOctave extends JPanel
{
	private SynthModel model;
	private JLabel label;
	private int numOctave = -1;
	private JButton plus,moins;
	public SelectOctave(SynthModel model)
	{
		this.model = model;

		this.moins = new JButton("-");
		this.plus = new JButton("+");
		this.moins.addActionListener(new ListenerBtn(true));
		this.plus.addActionListener(new ListenerBtn(false));
		this.label = new JLabel(Integer.toString(numOctave));
		this.add(moins);
		this.add(this.label);
		this.add(plus);
	}
	class ListenerBtn implements ActionListener
	{
		boolean moins;
		public ListenerBtn(boolean x)
		{
			this.moins = x;
		}
		public void actionPerformed(ActionEvent e)
		{
			if(this.moins)
			{
				model.setOctave(model.getOctave()-1);
			}
			else
			{
				model.setOctave(model.getOctave()+1);
			}
			label.setText(Integer.toString(model.getOctave()));
		}
	}
	public void setFocus(PianoView v)
    {
        this.moins.addMouseListener(new FocusPiano(v));
        this.plus.addMouseListener(new FocusPiano(v));
    }
}