package Keybord.Controler;

import java.awt.event.*;
import Keybord.View.*;
import Keybord.Model.*;

public class PitchModGraphControl implements MouseMotionListener 
{
  private PitchModGraph v;
  private SynthModel model;
	public PitchModGraphControl(PitchModGraph v,SynthModel model)
	{
    this.model = model;
    this.v = v;
	}
 public void mouseMoved(MouseEvent e) {
     //System.out.println("moved x:"+e.getX()+"/y:"+e.getY());
  }

  public void mouseDragged(MouseEvent e) {
    int height = this.v.getHeight();
    int trueX = (e.getX()*16384)/height;
    int trueY = (e.getY()*127)/height;
    if(trueX<0)
      trueX=0;
    else if(trueY<=0)
      trueY=0;
    else if(trueY>height-5)
      trueY=height;
    else if(trueX>(height*16384)/height)
      trueX=16384;
    //System.out.println("Y:"+e.getY()+" ajusté:"+trueY);

    //System.out.println("dragged x:"+e.getX()+"/y:"+e.getY());
    this.v.setPitch(trueX);
    this.v.setMod(trueY);
    //on applique les filtre au model
    this.model.setEffect(trueX,224,false);
    this.model.setEffect(trueY,1,false);
    //this.model.setEffect(trueY,2,false);

  }
}