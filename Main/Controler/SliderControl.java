package Main.Controler;
import javax.swing.event.*;
import javax.swing.*;
import Main.View.*;
import Main.Model.*;
import javax.sound.sampled.*;


public class SliderControl implements ChangeListener
{
    private Son son;
    private Control control;
    
    public SliderControl()
    {
        
    }
    
    public SliderControl(Son s, Control c)
    {
        this.son = s;
        this.control = c;
    }
    
    public void stateChanged(ChangeEvent e)
    {
        System.out.println("here with alone");

        JSlider slide = (JSlider) e.getSource();
        
        if(this.son != null && this.control != null)
        {
            System.out.println("here with"+this.control.toString());
            this.son.changerEffet(this.control,slide.getValue());
        }
        
    }
    
    
    
    public void setSound(Son s)
    {
        this.son = s;
    }
}