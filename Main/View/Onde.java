package Main.View;
import Main.Model.*;
import Main.Controler.CursorControler;
import java.io.File;
import javax.swing.*;
import java.awt.*;
import javax.swing.filechooser.FileNameExtensionFilter;
public class Onde extends JPanel implements SoundView
{
    public GridBagConstraints constraints;
    private Son son;
    private Graph graph; 
    private Cursor timecursor;
    public Onde()
    {
        /*ImageIcon image = new ImageIcon("Image/GlasImage.png");
        JLabel label = new JLabel("",image,JLabel.LEFT);
        add(label,BorderLayout.WEST);*/
        super();
        setLayout(new GridLayout(1,1));
        JLayeredPane layeredpane = new JLayeredPane();
        //layeredpane.setLayout(new BoxLayout());
        
        //int[] ypoints = {8,-3,5,-1,16,-3,4,6,-1,4,6,3,7,-8,3,2,-7,4,2,4,-7,5,-3,8};
        this.setBackground(Color.GRAY);
        this.constraints = new GridBagConstraints();
        this.constraints.fill = GridBagConstraints.BOTH;
        this.constraints.gridy = 0;
        this.constraints.gridx = 0;
        this.constraints.weightx = 5.0;
        this.constraints.weighty = 5.0;
        this.constraints.gridwidth = 4;
        this.constraints.gridheight = 4;
        this.constraints.insets = new Insets(2,2,2,2);
        this.graph = new Graph();
        graph.setBounds(0,0,1000,1000); // 704x619
        //this.add(this.graph);
        Cursor c = new Cursor();
        this.timecursor = new Cursor();
        c.setBounds(0,0,1000,1000);
        this.timecursor.setBounds(0,0,1000,1000);
        CursorControler ccontroler = new CursorControler(c,this.timecursor);
        c.addMouseMotionListener(ccontroler);
        c.addMouseListener(ccontroler);
        //this.add(c);
        layeredpane.add(this.graph,JLayeredPane.DEFAULT_LAYER);
        layeredpane.add(this.timecursor,JLayeredPane.PALETTE_LAYER);
        layeredpane.add(c,JLayeredPane.MODAL_LAYER);
        this.add(layeredpane);
    }
    public void draw(File f)
    {
        //this.setBackground(Color.GREEN);
        this.son = new Son(f);
        this.graph.setVector(son.createWaveForm(this.getSize()));
        //this.graph.setBackground(Color.GREEN);
        this.repaint();
    }
    public void draw()
    {
        //choix du fichier a placer dans le controler
        this.setBackground(Color.BLACK);
        File file = null;
        try
        {
         try{
             file = new File(System.getProperty("user.dir"));
                JFileChooser fc = new JFileChooser(file);
                fc.setFileFilter(new FileNameExtensionFilter("Wave FIle", "wav", "wave"));
                if (fc.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
                    file = fc.getSelectedFile();
                }
                else
                {
                    return;
                }
            } catch (SecurityException ex) { 
                //JavaSound.showInfoDialog();
                ex.printStackTrace();
                return;
            } catch (Exception ex) { 
                ex.printStackTrace();
                return;
            }
            this.son = new Son(file);
            this.graph.setVector(son.createWaveForm(this.getSize()));
            //add(new Graph());
            this.repaint();
        }catch(NullPointerException e)
        {
            e.printStackTrace();
        }
    }
    public Son getSound()
    {
        return this.son;
    }
    public Cursor getTimeCursor()
    {
        return this.timecursor;
    }
}