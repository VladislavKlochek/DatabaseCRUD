package statistics;

import javax.swing.*;
import java.awt.*;
public class InfoWindow extends JWindow {
    JLabel name = new JLabel();
    JLabel value = new JLabel();
    public InfoWindow(Component component){
        name.setText(((Column)component).getDrugName());
        value.setText(String.valueOf(((Column)component).getValue()));
        name.setAlignmentX(Component.CENTER_ALIGNMENT);
        value.setAlignmentX(Component.CENTER_ALIGNMENT);

        int labelHeight = name.getPreferredSize().height + value.getPreferredSize().height;
        int windowWidth = Math.max(name.getPreferredSize().width, value.getPreferredSize().width) + 30;
        int windowHeight = labelHeight + 10;

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(name);
        panel.add(value);
        add(panel);

        setSize(windowWidth,windowHeight);
        setVisible(true);
    }
}
