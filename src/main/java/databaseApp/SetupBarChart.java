package databaseApp;

import statistics.BarChart;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.List;

public class SetupBarChart implements ActionListener {
    private final JFrame frame;
    private final Connection connection;
    private final List<QueryButton> queryButtons;
    private final JFrame helpFrame = new JFrame();

    public SetupBarChart(JFrame frame, Connection connection, List<QueryButton> queryButtons) {
        this.frame = frame;
        this.connection = connection;
        this.queryButtons = queryButtons;
    }
    private JPanel setTextInPanel(String text){
        JLabel label1 = new JLabel(text);
        label1.setFont(new Font("Comic Sans", Font.BOLD, 15));
        label1.setHorizontalTextPosition(JLabel.CENTER);
        label1.setHorizontalTextPosition(JLabel.CENTER);
        label1.setForeground(Color.BLACK);
        JPanel f = new JPanel();
        f.setBackground(new Color(52, 47, 77, 236));
        f.add(label1);
        return f;
    }
    @Override
    public void actionPerformed(ActionEvent e) {
        frame.setEnabled(false);

        helpFrame.setUndecorated(true);
        helpFrame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        helpFrame.setVisible(true);
        helpFrame.setSize(new Dimension(510,200));
        helpFrame.setLocationRelativeTo(null);
        helpFrame.setResizable(false);
        helpFrame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        helpFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });
        JPanel helpPanel = new JPanel(new GridLayout(5,3));
        helpFrame.add(helpPanel);

        JComboBox<String> whateverChoose = new JComboBox<>();
        JComboBox<String> intChoose = new JComboBox<>();
        JComboBox<QueryButton> choose = new JComboBox<>();
        choose.setBackground(new Color(133, 139, 255));
        whateverChoose.setBackground(new Color(56, 51, 94));
        intChoose.setBackground(new Color(56, 51, 94));
        choose.setForeground(Color.WHITE);
        whateverChoose.setForeground(Color.WHITE);
        intChoose.setForeground(Color.WHITE);


        final ResultSet[] res = new ResultSet[1];
        choose.addItemListener(e1 -> {
            if(e1.getStateChange() == ItemEvent.SELECTED){
                try {
                    intChoose.removeAllItems();
                    whateverChoose.removeAllItems();
                    res[0] = connection.getMetaData()
                            .getColumns(null,null,
                                    ((QueryButton) e1.getItem()).getSQLQuery().split(" ")
                                            [((QueryButton) e1.getItem()).getSQLQuery().split(" ").length-1].toLowerCase(Locale.ROOT),
                                    null);
                    while(res[0].next()){
                        intChoose.addItem(res[0].getString("COLUMN_NAME"));
                        whateverChoose.addItem(res[0].getString("COLUMN_NAME"));
                    }
                    res[0].close();
                } catch (SQLException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });

        for (QueryButton q:
                queryButtons) {
            choose.addItem(q);
        }

        int count = 0;
        java.util.List<JPanel> panels = new ArrayList<>(List.of(new JPanel(), new JPanel(),
                new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel(), new JPanel()));
        panels.add(new JPanel());

        panels.forEach(p->p.setBackground(new Color(52, 47, 77, 236)));
        helpPanel.add(panels.get(count++));
        helpPanel.add(setTextInPanel("Выберите таблицу"));
        helpPanel.add(panels.get(count++));
        helpPanel.add(panels.get(count++));
        helpPanel.add(choose);
        helpPanel.add(panels.get(count++));
        helpPanel.add(whateverChoose);
        helpPanel.add(panels.get(count++));
        helpPanel.add(intChoose);
        helpPanel.add(setTextInPanel("Любой тип данных"));
        helpPanel.add(panels.get(count++));
        helpPanel.add(setTextInPanel("Число"));
        helpPanel.add(panels.get(count++));

        JButton createButton = new JButton("Создать график");
        createButton.setFocusable(false);
        createButton.setBackground(new Color(133, 139, 255));
        createButton.addActionListener(e12 -> {
            try {
                String query = "SELECT " + intChoose.getSelectedItem() + " FROM " +
                        ((QueryButton)(choose.getSelectedItem())).getSQLQuery().split(" ")
                                [((QueryButton)(choose.getSelectedItem())).getSQLQuery()
                                .split(" ").length-1].toLowerCase(Locale.ROOT) + " LIMIT 1";

                Statement statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                resultSet.getMetaData().getColumnClassName(1);
                String currentValue;
                switch (resultSet.getMetaData().getColumnClassName(1)){
                    case "java.lang.Integer":
                        currentValue = "java.lang.Integer";
                        break;
                    case "org.postgresql.util.PGmoney":
                        currentValue = "org.postgresql.util.PGmoney";
                        break;
                    default:
                        throw new RuntimeException();
                }
                ResultSet resultSet1 =  statement.executeQuery("SELECT " + whateverChoose.getSelectedItem() +
                        ", " + intChoose.getSelectedItem() + " FROM " +
                        ((QueryButton)(choose.getSelectedItem())).getSQLQuery().split(" ")
                                [((QueryButton)(choose.getSelectedItem())).getSQLQuery()
                                .split(" ").length-1].toLowerCase(Locale.ROOT));
                Map<Object, Double> map = new HashMap<>();
                while (resultSet1.next()){
                    for (int i = 1; i < resultSet1.getMetaData().getColumnCount()+1; i++) {
                        Double doubleValue;
                        switch (currentValue){
                            case "java.lang.Integer":
                                doubleValue = (double) resultSet1.getInt(i + 1);
                                break;
                            case "org.postgresql.util.PGmoney":
                                doubleValue = resultSet1.getDouble(i+1);
                                break;
                            default: doubleValue = 0.0;
                        }
                        map.put(resultSet1.getObject(i), doubleValue);
                        i=2;
                    }
                }
                new BarChart(map);
                onCancel();
            }
            catch (Exception exception){
                exception.printStackTrace();
                JOptionPane.showMessageDialog(null, "Wrong input", "Wrong input", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(exception);
            }

        });
        helpPanel.add(createButton);
        helpPanel.add(panels.get(count));
    }
    public void onCancel(){
        frame.setEnabled(true);
        helpFrame.dispose();
    }
}
