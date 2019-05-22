package Main.View;
import javax.swing.*;
import java.awt.Dimension;
public class ListeWidget extends JPanel
{
	public ListeWidget()
	{
		super();
	}
	public void addWidget(JComponent c)
	{
		this.add(c);
		this.repaint();
	}
}