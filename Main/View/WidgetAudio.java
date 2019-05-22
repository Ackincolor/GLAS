package Main.View;
import Main.Controler.*;
import javax.swing.*;
import java.awt.*;
public class WidgetAudio extends JPanel
{
	public WidgetAudio(String s)
	{
		this.add(new JLabel(s));
		this.setBackground(Color.GRAY);
		this.setBorder(BorderFactory.createLineBorder(Color.BLACK,1,true));
	}
}