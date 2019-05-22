package Keybord.Controler;
import Keybord.Model.*;
import Keybord.View.*;
import javax.swing.event.*;
import javax.swing.*;

public class SliderControler implements ChangeListener
{
	private SynthModel synthModel;
	private GraphicsSon graph;
	private int mod;
	public SliderControler(SynthModel synthModel,int mod,GraphicsSon graph)
	{
		this.synthModel = synthModel;
		this.mod = mod;
		this.graph = graph;
	}
	public void stateChanged(ChangeEvent e)
	{
		if(this.mod==0)
		{
			JSlider source = (JSlider)e.getSource();
			this.synthModel.setVelocity(source.getValue());
		}
		else if(this.mod>1)
		{
			JSlider source = (JSlider)e.getSource();
			//3==attacktime
			if(this.mod==73)
			{
				this.graph.setAttack(source.getValue());
				this.graph.repaint();
			}
			else if(this.mod==72)
			{
				this.graph.setRelease(source.getValue());
				this.graph.repaint();
			}
			this.synthModel.setEffect(source.getValue(),this.mod,false);
		}
	}
}