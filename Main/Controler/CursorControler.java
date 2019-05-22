package Main.Controler;
import java.awt.event.*;
import Main.View.Cursor;
import Main.Model.Son;
import javax.swing.JComponent;

public class CursorControler implements MouseMotionListener, MouseListener
{
    private Cursor mousecursor;
    private Cursor timecursor;
    private Son son;
    
    public CursorControler(Cursor c, Cursor tc)
    {
        this.mousecursor = c;
        this.timecursor = tc;
    }
    
    public void	mouseDragged(MouseEvent e)
    {
        
    }
    public void	mouseMoved(MouseEvent e)
    {
        this.mousecursor.changeCursor(e.getX());
    }

    public void	mouseClicked(MouseEvent e)
    {
        this.timecursor.changeCursor(e.getX());
        this.timecursor.getSon().getClip().setFramePosition((int)(e.getX()/((JComponent)e.getSource()).getWidth()));
    }
    
    public void	mouseEntered(MouseEvent e)
    {

    }
    
    public void	mouseExited(MouseEvent e)
    {
        this.mousecursor.changeCursor(0);
    }
    
    public void	mousePressed(MouseEvent e)
    {
        
    }
    
    public void	mouseReleased(MouseEvent e)
    {
        
    }
    
    
}
