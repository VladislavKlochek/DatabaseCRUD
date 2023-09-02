package databaseApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.List;

class DeleteActionListener implements ActionListener {
    private final JPanel panel;
    private final Connection connection;
    private final List<JLabel> labelsToAdd;
    private final List<List<JLabel>> allLabels;
    private final List<JPanel> allPanels;

    public DeleteActionListener(JPanel panel, Connection connection, List<JLabel> labelsToAdd,
                                List<List<JLabel>> allLabels, List<JPanel> allPanels) {
        this.panel = panel;
        this.connection = connection;
        this.labelsToAdd = labelsToAdd;
        this.allLabels = allLabels;
        this.allPanels = allPanels;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        var RAL = new ReadActionListener(panel, connection, labelsToAdd, allLabels, allPanels);
        RAL.actionPerformed(e);
        allPanels.forEach(e1 -> e1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!((DeletableJPanel) e.getComponent()).isSelected()) {
                        e.getComponent().setBackground(new Color(155, 34, 34));
                        ButtonDropUpListener.deleteWindow.addRecordToDelete((DeletableJPanel) e.getComponent());
                        ((DeletableJPanel) e.getComponent()).setSelected(true);
                    } else {
                        e.getComponent().setBackground(((DeletableJPanel) e.getComponent()).getPreviousColor());
                        ButtonDropUpListener.deleteWindow.removeRecordFromListToDelete((DeletableJPanel) e.getComponent());
                        ((DeletableJPanel) e.getComponent()).setSelected(false);
                    }
                    if (allPanels.stream().anyMatch(e1 -> ((DeletableJPanel) e1).isSelected())) {
                        if(!ButtonDropUpListener.deleteWindow.isShowing()) {
                            ButtonDropUpListener.deleteWindow.unCheck();}
                    }
                    else {ButtonDropUpListener.deleteWindow.check();}
                }
            }
        }));
    }
}