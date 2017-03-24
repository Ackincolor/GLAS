package Main.View;
import Main.Controler.*;
import Main.Model.*;
import java.awt.*;
import javax.swing.*;
import java.util.Vector;
import java.awt.geom.Line2D;

public class Graph extends JComponent
{
    private int[] x, y;
    private int moyenne;
    private Vector vect;
    public Graph(int[] ypoints)
    {
        this.y = ypoints;
        this.x = new int[ypoints.length];
        int somme = 0;
        for(int n : ypoints)
        {
            somme += n;
        }
        this.moyenne = somme/ypoints.length;
    }
    public Graph(Vector vect)
    {
        this.vect = vect;
    }
    
    public Graph()
    {
    }
    public void setVector(Vector lines)
    {
        this.vect = lines;
        this.repaint();
    }
    @Override
    protected void paintComponent(Graphics g)
    {
        Graphics clone = g.create();
        Graphics2D g2 = (Graphics2D)g.create();
        clone.setColor(Color.RED);
        g2.setColor(Color.RED);
        if(this.vect!=null)
        {
            for (int i = 1; i < this.vect.size(); i++) {
                        g2.draw((Line2D) this.vect.get(i));
                    }
        }/*
        else
        {
            int ytranslate[] = new int[y.length];
            
            int i = 0;
            int plot = 0;
            int increment = this.getParent().getWidth()/(this.y.length-1);
            for(i=0;i<this.x.length;i++)
            {
                this.x[i] = plot;
                plot += increment;
                
                ytranslate[i] = this.y[i] + (this.getHeight()/2 - this.moyenne);
            }
            
            clone.drawPolyline(this.x,ytranslate,this.y.length);
        }*/
    }


}