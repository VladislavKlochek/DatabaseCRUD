package databaseApp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.util.*;
import java.util.List;

public class ChoiceWindow extends JWindow {

    private final List<QueryButton> tables;
    private final JPanel helpPanel = new JPanel();
    private final JPanel panel;
    private final ChoiceWindowAction action;
    private final Connection connection;
    private final JFrame mainFrame;
    private final List<JLabel> labelsToAdd = new ArrayList<>();
    private final List<List<JLabel>> allLabels = new ArrayList<>();
    private final List<JPanel> allPanels = new ArrayList<>();

    ChoiceWindow(Component component, ChoiceWindowAction action, JPanel panel, Connection connection,
                 JFrame mainFrame, List<QueryButton> queryButtons){
        this.action = action;
        this.panel = panel;
        this.connection = connection;
        this.mainFrame = mainFrame;
        tables = queryButtons;

        QueryButton d = tables.stream().max(Comparator.comparingDouble(o -> o.getPreferredSize().getWidth())).get();
        tables.forEach(e -> e.setPreferredSize(d.getPreferredSize()));
        GridLayout g = new GridLayout(tables.size(), 1);
        helpPanel.setLayout(g);
        tables.forEach(helpPanel::add);
        add(helpPanel);
        setVisible(true);

        setSize(component.getWidth(), (int) this.getPreferredSize().getHeight());
        setLocation((int) component.getLocationOnScreen().getX(),
                (int) (component.getLocationOnScreen().getY()-this.getHeight()));

        setButtonsAction();
    }

    private void setButtonsAction(){
        switch (action) {
            case READ:
                removeAllListeners();
                tables.forEach(queryButton -> queryButton.addActionListener(new ReadActionListener(panel, connection,
                        labelsToAdd, allLabels, allPanels)));
                break;

            case CREATE:
                removeAllListeners();
                tables.forEach(queryButton -> queryButton.addActionListener(new CreateActionListener(queryButton,
                        connection, mainFrame)));
                break;

            case UPDATE:
                removeAllListeners();
                tables.forEach(queryButton -> queryButton.addActionListener(new UpdateActionListener(panel, connection,
                        labelsToAdd, allLabels, allPanels, mainFrame)));
                break;

            case DELETE:
                removeAllListeners();
                tables.forEach(queryButton -> queryButton.addActionListener(new DeleteActionListener(panel, connection,
                        labelsToAdd, allLabels, allPanels)));
                break;
        }
    }
    private void removeAllListeners(){
        for (QueryButton queryButton : tables) {
            for (ActionListener ac:
                    queryButton.getActionListeners()) {
                queryButton.removeActionListener(ac);
            }
        }
    }
}

