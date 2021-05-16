/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package antiban.unused;

import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import static org.dreambot.api.methods.MethodProvider.log;

/**
 *
 * @author t7emon
 */
public class AntiPattern implements MouseListener 
{
    public static final List<Point> storedMousePoints = new ArrayList();
    
    public AntiPattern() 
    {
    }
    
    @Override
    public void mouseClicked(MouseEvent e) 
    {
       Point point = e.getLocationOnScreen();
       log("Clicked mouse point: " + point);
       if (storedMousePoints.contains(point)) 
       {
           log("Mouse point already stored!");
           return;
       }
       storedMousePoints.add(point);
    }

    @Override
    public void mousePressed(MouseEvent e) 
    {
    }

    @Override
    public void mouseReleased(MouseEvent e) 
    {
    }

    @Override
    public void mouseEntered(MouseEvent e) 
    {
    }

    @Override
    public void mouseExited(MouseEvent e) 
    {
    }
    
    public static void main(String args[]) 
    {
        new AntiPattern();
    }
    
}
