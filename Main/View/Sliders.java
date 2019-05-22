package Main.View;
import javax.swing.*;
import java.awt.*;
import java.util.*;
import javax.sound.sampled.*;
import Main.Controler.*;
import Main.Model.*;
public class Sliders extends JPanel
{
    public GridBagConstraints constraints;
    private SliderControl slidercontrol;
    private Control[] controls;
    private TreeMap<String,JSlider> list;
    private Son son;
    
    public Sliders()
    {
        this.list = new TreeMap<String,JSlider>();
        
        /*JSlider echo = new JSlider();
        list.put("Echo",echo);
        
        JSlider reverb = new JSlider();
        list.put("Réverbe",reverb);
        
        JSlider distortion = new JSlider();
        list.put("Distortion",distortion);
        
        JSlider pitch = new JSlider();
        list.put("Pitch",pitch);
        
        JSlider vitesse = new JSlider();
        list.put("Vitesse",vitesse);
        
        JSlider compression = new JSlider(8,64);
        compression.setMinorTickSpacing(8);
        compression.setValueIsAdjusting(true);
        compression.setPaintTicks(true);
        compression.setPaintLabels(true);
        list.put("Compression",compression);

        this.slidercontrol = new SliderControl();
        
        for(String label : list.keySet())
        {
            add(new JLabel(label));
            ((JSlider)list.get(label)).setOpaque(false);
            ((JSlider)list.get(label)).setName(label);
            ((JSlider)list.get(label)).addChangeListener(this.slidercontrol);
            add(list.get(label));
            
        }*/
        
        this.constraints = new GridBagConstraints();
        this.constraints.gridy = 0;
        this.constraints.gridx = 4;
        this.constraints.gridwidth = 1;
        this.constraints.gridheight = 5;
        this.constraints.anchor = GridBagConstraints.FIRST_LINE_END;
        this.constraints.insets = new Insets(2,2,2,2);
    }
    
    public void setSound(Son s)
    {
        this.removeAll();
        this.son = s;
        this.controls = son.getControls();
        this.setLayout(new GridLayout(this.controls.length*2,1));

        JSlider tempslider;
        FloatControl tempfloat;
        BooleanControl tempbool;
        
        for(Control c: this.controls)
        {
            System.out.println("hererere insde");

            if(c instanceof FloatControl)
            {
                tempfloat = (FloatControl)c;
                tempslider = new JSlider((int)tempfloat.getMinimum(),(int)tempfloat.getMaximum());
                tempslider.addChangeListener(new SliderControl(this.son, c));
                tempslider.setName(tempfloat.toString());
                this.add(new JLabel(tempfloat.toString().split(" with")[0]));
                this.add(tempslider);
            }
            else if(c instanceof BooleanControl)
            {
                tempbool = (BooleanControl)c;
                tempslider = new JSlider(0,1);
                tempslider.addChangeListener(new SliderControl(this.son, c));
                tempslider.setName(tempbool.toString());
                this.add(new JLabel(tempbool.toString().split(" with")[0]));
                this.add(tempslider);
            }
        }
        this.getTopLevelAncestor().revalidate();
        this.getTopLevelAncestor().repaint();
    }
    @Override
    public void setBackground(Color c)
    {
        if(this.list!=null)
        for(String keys : this.list.keySet())
        {
            if(!keys.equals("Onde"))
            {
                this.list.get(keys).setBackground(c);

            }
        }
    }
    public SliderControl getSliderControl()
    {
        return this.slidercontrol;
    }
  
        
}