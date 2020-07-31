/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package stdClasses;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

/**
 *
 * @author user
 */
public class LogoDscCombo extends JComboBox {
    
    private ArrayList<String> descriptions;
    private ArrayList<ImageIcon> images;

    private final int IMG_WIDTH = 60;
    private final int IMG_HEIGHT = 40;
    
    private final Dimension COMBO_SIZE = new Dimension(250, 50);
    
    public LogoDscCombo(ArrayList<String> descriptions, ArrayList<ImageIcon> images) {
        
        super();
    
        this.descriptions = descriptions;
        this.images = images;
        
        for (int items = 0; items < descriptions.size(); items++) {
            addItem(new Integer(items));
        }
        
        ComboBoxRenderer renderer= new ComboBoxRenderer();
        renderer.setPreferredSize(COMBO_SIZE); 
        setRenderer(renderer);
        setMaximumRowCount(3);

    }
    
    class ComboBoxRenderer extends JLabel implements ListCellRenderer {
        private Font daFont;

        public ComboBoxRenderer() {
            setOpaque(true);
            setHorizontalAlignment(LEFT);
            setVerticalAlignment(CENTER);
        }

        /*
         * This method finds the image and text corresponding
         * to the selected value and returns the label, set up
         * to display the text and image.
         */
        @Override
        public Component getListCellRendererComponent(
                                           JList list,
                                           Object value,
                                           int index,
                                           boolean isSelected,
                                           boolean cellHasFocus) {
            //Get the selected index. (The index param isn't
            //always valid, so just use the value.)
            int selectedIndex = ((Integer)value).intValue();

            if (isSelected) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            //Set the icon and text.  If icon was null, say so.
            
            ImageIcon icon = images.get(selectedIndex);
            String dsc = descriptions.get(selectedIndex);
            
            if (icon != null) {
                Image img = getScaledImage(icon.getImage(), IMG_WIDTH, IMG_HEIGHT);
                setIcon(new ImageIcon(img));
            }            
            else {
                setIcon(icon);
            }
            
            if (icon != null) {
                setText(dsc);
                setFont(list.getFont());
            } else {
                if (selectedIndex > 0) {
                    daText(dsc + " (no image available)", list.getFont());
                }
            }

            return this;
        }

        //Set the font and text when no image was found.
        protected void daText(String daText, Font normalFont) {
            if (daFont == null) { //lazily create this font
                daFont = normalFont.deriveFont(Font.ITALIC);
            }
            setFont(daFont);
            setText(daText);
        }
    }
    
    private Image getScaledImage(Image srcImg, int w, int h){
        BufferedImage resizedImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = resizedImg.createGraphics();

        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(srcImg, 0, 0, w, h, null);
        g2.dispose();

        return resizedImg;
    }
    
    public String getSelectedDsc() {
        return descriptions.get(getSelectedIndex());
    }
}
