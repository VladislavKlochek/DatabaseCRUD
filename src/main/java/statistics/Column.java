package statistics;

import javax.swing.*;
import java.awt.*;

public class Column extends JButton{
    private final double x;
    private final double y;
    private final double width;
    private final double height;
    private final double value;
    private final Object name;

    public Column(double x, double y, int width, double height, double value, Object name,
                  ColumnMouseListener columnMouseListener, ColumnMouseMotionListener columnMouseMotionListener) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.value = value;
        this.name = name;
        setPreferredSize(new Dimension(width, (int) height));
        addMouseListener(columnMouseListener);
        addMouseMotionListener(columnMouseMotionListener);
        setBorderPainted(false);
    }

    public int getWidth() {
        return (int) width;
    }

    public int getHeight() {
        return (int) height;
    }
    public double getValue() {
        return value;
    }

    public String getDrugName() {return name.toString();}

    public int getX() {
        return (int) x;
    }

    public int getY() {
        return (int) y;
    }
}
