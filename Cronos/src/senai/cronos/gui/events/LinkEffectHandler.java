package senai.cronos.gui.events;

import java.awt.Color;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import senai.cronos.gui.ColorManager;

/**
 *
 * @author sergio lisan e carlos melo
 */
public class LinkEffectHandler extends MouseAdapter {

    @Override
    public void mouseEntered(MouseEvent e) {
        JComponent com = (JComponent) e.getSource();
        com.setOpaque(true);
        com.setBackground(overbackgroundcolor);
        com.setForeground(overforegroundcolor);
    }

    @Override
    public void mouseExited(MouseEvent e) {
        JComponent com = (JComponent) e.getSource();
        com.setOpaque(false);
        com.setForeground(outforegroundcolor);
    }
    
    public static final Color overbackgroundcolor = ColorManager.getColor("background");
    public static final Color overforegroundcolor = Color.white;
    public static final Color outbackgroundcolor = Color.white;
    public static final Color outforegroundcolor = Color.black;
}
