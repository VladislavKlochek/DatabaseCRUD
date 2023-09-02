package databaseApp;

import javax.swing.*;
import java.awt.*;

public class DeletableJPanel extends JPanel {
    private final int id;
    private final Color previousColor;
    private boolean isSelected;
    private final String tableName;

    public DeletableJPanel(int id, LayoutManager layout, Color previousColor, String tableName) {
        super(layout, true);
        this.id = id;
        this.previousColor = previousColor;
        this.tableName = tableName;
        setBackground(previousColor);
    }


    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public Color getPreviousColor() {
        return previousColor;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public String getTableName() {
        return tableName;
    }

    public int getId() {
        return id;
    }

}
