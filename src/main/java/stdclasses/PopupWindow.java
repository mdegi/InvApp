package stdClasses;

import java.awt.Component;
import java.awt.Container;
import java.awt.Point;
import java.awt.Window;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.util.Vector;
import javax.swing.JWindow;

public class PopupWindow {
    private JWindow mDelegate;
    private Container mContainer;
    private Vector mGrabbed=new Vector();
    private WindowListener mWindowListener;
    private ComponentListener mComponentListener;
    private MouseListener mMouseListener;
    private MouseMotionListener mMouseMotionListener;
    private Component mComponent;
    
    public PopupWindow(Container container) {
        mContainer=container;
        createDelegate();
        createListeners();
    }
    
    private void createDelegate() {
        Window window= getWindow();
        if (window != null) mDelegate=new JWindow(window);
    }
    
    public void add(Component component) {
        mComponent= component;
        if (mDelegate != null) {
            mDelegate.getContentPane().add(component);
            mDelegate.pack();
        }
    }
    
    public void show(Component relative, int x, int y) {
        if (mDelegate == null) {
            createDelegate();
            if (mDelegate == null) return;
            add(mComponent);
        }
        Point location= relative.getLocationOnScreen();
        mDelegate.setLocation(location.x +x, location.y +y);
        mDelegate.setVisible(true);
        grabContainers();
    }
    
    public void hide() {
        mDelegate.setVisible(false);
        releaseContainers();
    }
    
    private void createListeners() {
        mWindowListener= new WindowListener() {
            public void windowOpened(WindowEvent e) {}
            public void windowClosing(WindowEvent e) {hide();}
            public void windowClosed(WindowEvent e) {hide();}
            public void windowIconified(WindowEvent e) {hide();}
            public void windowDeiconified(WindowEvent e) {}
            public void windowActivated(WindowEvent e) {}
            public void windowDeactivated(WindowEvent e) {}
        };
        
        mComponentListener= new ComponentListener() {
            public void componentResized(ComponentEvent e) {hide();}
            public void componentMoved(ComponentEvent e) {hide();}
            public void componentShown(ComponentEvent e) {hide();}
            public void componentHidden(ComponentEvent e) {hide();}
        };
        
        mMouseListener= new MouseListener() {
            public void mousePressed(MouseEvent e) {hide();}
            public void mouseReleased(MouseEvent e) {}
            public void mouseEntered(MouseEvent e) {}
            public void mouseExited(MouseEvent e) {}
            public void mouseClicked(MouseEvent e) {}
        };
        
        mMouseMotionListener= new MouseMotionListener() {
            public void mouseDragged(MouseEvent e) {}
            public void mouseMoved(MouseEvent e) {}
        };
    }
    
    private Window getWindow() {
        Container c=mContainer;
        while(!(c instanceof Window) && c.getParent() != null) c=c.getParent();
        if (c instanceof Window) return (Window) c;
        return null;
    }
    
    private void grabContainers() {
        Container c=mContainer;
        while(!(c instanceof Window) && c.getParent() != null) c=c.getParent();
        grabContainer(c);
    }
    
    private void grabContainer(Container c) {
        if (c instanceof Window) {
            ((Window)c).addWindowListener(mWindowListener);
            c.addComponentListener(mComponentListener);
            mGrabbed.addElement(c);
        }
        
        synchronized (c.getTreeLock()) {
            int ncomponents=c.getComponentCount();
            Component[] component=c.getComponents();
            for (int i= 0 ; i<ncomponents ; i++) {
                Component comp=component[i];
                if(!comp.isVisible()) continue;
                comp.addMouseListener(mMouseListener);
                comp.addMouseMotionListener(mMouseMotionListener);
                mGrabbed.addElement(comp);
                if (comp instanceof Container) {
                    Container cont=(Container) comp;
                    grabContainer(cont);
                }
            }
        }
    }
    
    void releaseContainers() {
        for (int i=0; i<mGrabbed.size(); i++) {
            Component c=(Component)mGrabbed.elementAt(i);
            if (c instanceof Window) {
                ((Window)c).removeWindowListener(mWindowListener);
                ((Window)c).removeComponentListener(mComponentListener);
            } else {
                c.removeMouseListener(mMouseListener);
                c.removeMouseMotionListener(mMouseMotionListener);
            }
        }
        mGrabbed.removeAllElements();
    }
}
