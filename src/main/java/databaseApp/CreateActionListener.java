package databaseApp;

import org.postgresql.util.PGmoney;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
import java.util.List;

public class CreateActionListener implements ActionListener {
    private final String tableName;
    private Map<String, String> map;
    private final Map<JTextField, String> mapToValidate = new LinkedHashMap<>();
    private final JFrame mainFrame;
    private final Connection connection;
    private final QueryButton queryButton;
    private final JFrame window = new JFrame();

    public CreateActionListener(QueryButton queryButton, Connection connection, JFrame mainFrame) {
        this.mainFrame = mainFrame;
        this.queryButton = queryButton;
        this.connection = connection;
        this.tableName = queryButton.getSQLQuery().split(" ")[queryButton.getSQLQuery().split(" ").length-1];
        window.setResizable(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        init(((QueryButton)e.getSource()).getBackground());
    }
    private void init(Color color) {
        try {
            Statement statement =  connection.createStatement();
            ResultSet resultSet = statement.executeQuery(queryButton.getSQLQuery());
            map = new LinkedHashMap<>();
            for (int i = 1; i < resultSet.getMetaData().getColumnCount()+1; i++) {
                map.put(resultSet.getMetaData().getColumnName(i) , resultSet.getMetaData().getColumnClassName(i));
            }

        }catch (SQLException e){
            e.printStackTrace();
        }

        ButtonDropUpListener.deleteWindow.check();
        mainFrame.setEnabled(false);

        int del = 0;
        if(map.entrySet().stream().anyMatch(stringStringEntry -> stringStringEntry.getKey().toLowerCase(Locale.ROOT)
                .equals("id"))){del = 1;}

        JPanel helpPanel = new JPanel(new GridLayout(map.size() + 1-del, 2));
        window.setUndecorated(true);
        window.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
        window.setVisible(true);
        window.setLocationRelativeTo(null);
        Random random = new Random();
        for (Map.Entry<String, String> str :
                map.entrySet()) {
            if(!str.getKey().toLowerCase(Locale.ROOT).equals("id")) {
                JLabel addLabel = new JLabel(str.getKey());

                addLabel.setHorizontalTextPosition(JLabel.CENTER);
                addLabel.setHorizontalAlignment(JLabel.CENTER);
                addLabel.setOpaque(true);
                addLabel.setForeground(Color.WHITE);
                addLabel.setBackground(new Color(color.getRed(),
                        color.getGreen(), color.getBlue()+random.nextInt(50)));

                helpPanel.add(addLabel);
                JTextField textField = new JTextField();
                mapToValidate.put(textField, map.get(str.getKey()));
                helpPanel.add(textField);
            }
        }

        // call onCancel() when cross is clicked
        window.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        window.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                onCancel();
            }
        });

        // call onCancel() on ESCAPE
        helpPanel.registerKeyboardAction(e -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0)
                , JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);

        JButton OK = new JButton("Добавить");
        OK.setBackground(new Color(103, 161, 80));
        OK.setForeground(Color.BLACK);
        OK.setFocusable(false);
        OK.addActionListener(e -> {
            executeInsertSQLQuery(validateInfo());
            onCancel();
        });

        JButton Cancel = new JButton("Отменить");
        Cancel.setFocusable(false);
        Cancel.setBackground(new Color(169, 84, 84));
        Cancel.addActionListener(e -> onCancel());
        Cancel.setForeground(Color.BLACK);
        helpPanel.add(OK);
        helpPanel.add(Cancel);
        window.add(helpPanel);
        window.setSize(new Dimension((int) (window.getPreferredSize().getWidth() * 1.5),
                (int) (window.getPreferredSize().getHeight() * 1.5)));
    }

    private void onCancel() {
        // add your code here if necessary
        mainFrame.setEnabled(true);
        if(window.isShowing()){window.dispose();}
        mainFrame.requestFocus();
    }

    private java.util.List<Object> validateInfo() {
        java.util.List<Object> toOut = new ArrayList<>();
        for (Map.Entry<JTextField, String> m :
                mapToValidate.entrySet()) {
            try {
                switch (m.getValue()) {
                    case "java.lang.Integer":
                        toOut.add(Integer.valueOf(m.getKey().getText()));
                        break;
                    case "java.lang.String":
                        toOut.add(m.getKey().getText());
                        break;
                    case "org.postgresql.util.PGmoney":
                        toOut.add(new PGmoney("$" + Double.valueOf(m.getKey().getText()) + "," + "0"));
                        break;
                    case "java.lang.Boolean":
                        toOut.add(Boolean.valueOf(m.getKey().getText()));
                        break;
                    case "java.sql.Timestamp":
                        toOut.add(Timestamp.valueOf(m.getKey().getText() + " 00:00:00.0"));
                        break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Wrong input", "Wrong input",
                        JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }

        }
        return toOut;
    }

    private void executeInsertSQLQuery(List<Object> list) {
        int toDel=0;

        StringBuilder stringBuilder = new StringBuilder("INSERT INTO " + tableName);
        stringBuilder.append("(");
        Iterator<Map.Entry<String, String>> iterator = map.entrySet().iterator();
        while (iterator.hasNext()) {
            String next = iterator.next().getKey();
            if(!next.toLowerCase(Locale.ROOT).equals("id")) {
                stringBuilder.append(next);
                if (iterator.hasNext()) {
                    stringBuilder.append(", ");
                } else {
                    stringBuilder.append(") VALUES (");
                    for (int i = 0; i < map.entrySet().size() - toDel; i++) {
                        stringBuilder.append("?");
                        if (i + 1 < map.entrySet().size() - toDel) {
                            stringBuilder.append(", ");
                        } else {
                            stringBuilder.append(")");
                        }
                    }
                }
            }
            else toDel++;
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(stringBuilder.toString());
            for (int i = 0; i < list.size(); i++) {
                switch (list.get(i).getClass().getName()) {
                    case "java.lang.String":
                        preparedStatement.setString(i+1, (String) list.get(i));
                        break;
                    case "java.lang.Integer":
                        preparedStatement.setInt(i+1, (Integer) list.get(i));
                        break;
                    case "java.lang.Boolean":
                        preparedStatement.setBoolean(i+1, (Boolean) list.get(i));
                        break;
                    case "java.sql.Timestamp":
                        preparedStatement.setTimestamp(i+1, (Timestamp) list.get(i));
                        break;
                    case "org.postgresql.util.PGmoney":
                        preparedStatement.setObject(i+1, list.get(i));
                        break;
                }
            }
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
