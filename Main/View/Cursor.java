package Main.View;
import Main.Model.Son;
import javax.swing.*;
import java.awt.*;

public class Cursor extends JComponent
{
    private int x;
    private Son son;
    
    public Cursor()
    {
        super();
        this.x = 0;
    }
    
    public void changeCursor(int nx)
    {
        this.x = nx;
        this.repaint();
    }
    
    public void changeCursor(float nx, float scale)
    {
        float calcul = (nx/scale)*(float)this.getWidth();
        this.x = (int)calcul;
        this.repaint();
    }
    
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics c = g.create();
        c.setColor(Color.RED);
        c.drawLine(this.x,0,this.x,this.getParent().getParent().getHeight());
    }
    
    public void setSon(Son s)
    {
        this.son = s;
    }
    
    public void increment()
    {
        this.x += 1;
        this.repaint();
    }
    
    public Son getSon()
    {
        return this.son;
    }
}