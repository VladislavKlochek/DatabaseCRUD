package databaseApp;

import org.postgresql.util.PGmoney;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class UpdateActionListener implements ActionListener {
    private final JPanel panel;
    private final Connection connection;
    private final List<JLabel> labelsToAdd;
    private final List<List<JLabel>> allLabels;
    private List<JPanel> allPanels;
    private final JFrame frame = new JFrame();
    private final JPanel helpPanel = new JPanel();
    private final JFrame mainFrame;
    private final Map<Map.Entry<String, String>, JTextField> nameTypeField = new LinkedHashMap<>();
    private String tableName;

    public UpdateActionListener(JPanel panel, Connection connection, List<JLabel> labelsToAdd,
                              List<List<JLabel>> allLabels, List<JPanel> allPanels, JFrame mainFrame) {
        this.panel = panel;
        this.connection = connection;
        this.labelsToAdd = labelsToAdd;
        this.allLabels = allLabels;
        this.allPanels = allPanels;
        this.mainFrame = mainFrame;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        ButtonDropUpListener.deleteWindow.check();
        ReadActionListener RAL = new ReadActionListener(panel, connection, labelsToAdd, allLabels, allPanels);
        RAL.actionPerformed(e);
        ActionEvent remember = e;
        UpdateActionListener updateActionListener = this;
        tableName = RAL.getTableName();
        this.allPanels = RAL.getAllPanels();
        var nameDatatype = RAL.getDataTypes();
        allPanels.forEach(value->value.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    int count = 0;
                    int id = 0;
                    var t = nameDatatype.entrySet().stream().anyMatch(stringStringEntry -> stringStringEntry.getKey().toLowerCase(Locale.ROOT).equals("id"));
                    if (t) {
                        helpPanel.setLayout(new GridLayout(nameDatatype.size(), 2));
                    }
                    else {
                        helpPanel.setLayout(new GridLayout(nameDatatype.size()+1, 2));
                    }

                    Color color = ((QueryButton)remember.getSource()).getBackground();
                    Random random = new Random();
                    for (Map.Entry<String, String> m :
                            nameDatatype.entrySet()) {
                        Matcher matcher = Pattern.compile("<html>(.*?)</html>").matcher(((JLabel)(value.getComponent(count++))).getText());
                        matcher.matches();

                        if (!m.getKey().toLowerCase(Locale.ROOT).equals("id")){
                            JLabel label = new JLabel(m.getKey());
                            label.setHorizontalAlignment(JLabel.CENTER);
                            label.setHorizontalTextPosition(JLabel.CENTER);
                            label.setOpaque(true);
                            label.setBackground(new Color(color.getRed(), color.getGreen(),
                                    color.getBlue()+random.nextInt(50)));
                            label.setForeground(Color.WHITE);
                            helpPanel.add(label);

                            Pattern tagPattern = Pattern.compile("<br>");
                            String[] words = Arrays.stream(tagPattern.split(matcher.group(1)))
                                    .flatMap(Pattern.compile("\\s+")::splitAsStream)
                                    .filter(word -> !word.isEmpty()).toArray(String[]::new);
                            String s = String.join(" ", words);
                            JTextField textField = new JTextField(s);

                            textField.setHorizontalAlignment(JTextField.CENTER);
                            helpPanel.add(textField);
                            nameTypeField.put(m, textField);
                        }
                        else {
                            id = Integer.parseInt(matcher.group(1));
                        }
                    }

                    frame.add(helpPanel);
                    mainFrame.setEnabled(false);
                    frame.requestFocus();
                    frame.setResizable(false);
                    frame.setLocationRelativeTo(null);
                    frame.setLocation((int) (MouseInfo.getPointerInfo().getLocation().getX()),
                            (int) (MouseInfo.getPointerInfo().getLocation().getY()));

                    JButton updateButton = new JButton("Обновить");
                    updateButton.setBackground(new Color(103, 161, 80));
                    updateButton.setForeground(Color.BLACK);
                    updateButton.setFocusable(false);
                    int finalId = id;
                    updateButton.addActionListener(e12 -> {
                        executeUpdateSQLQuery(validateInfo(), finalId);
                        updateActionListener.actionPerformed(remember);
                        onCancel();
                    });
                    helpPanel.add(updateButton);

                    JButton cancelButton = new JButton("Отмена");
                    cancelButton.setBackground(new Color(169, 84, 84));
                    cancelButton.setFocusable(false);
                    cancelButton.addActionListener(e1 -> onCancel());
                    helpPanel.add(cancelButton);
                    frame.setSize(new Dimension((int) (frame.getPreferredSize().getWidth() * 2), (int) (frame.getPreferredSize().getHeight() * 2)));
                    /*frame.setSize(new Dimension(frame.getPreferredSize()));*/
                    // call onCancel() when cross is clicked
                    frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
                    frame.addWindowListener(new WindowAdapter() {
                        public void windowClosing(WindowEvent e) {
                            onCancel();
                        }
                    });

                    // call onCancel() on ESCAPE
                    helpPanel.registerKeyboardAction(e13 -> onCancel(), KeyStroke.getKeyStroke(KeyEvent.VK_ESCAPE, 0), JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT);
                    frame.setUndecorated(true);
                    frame.getRootPane().setWindowDecorationStyle(JRootPane.FRAME);
                    frame.setVisible(true);
                }
                catch (Exception exception){
                    exception.printStackTrace();
                }
            }
        }));
    }
    private void onCancel(){
        helpPanel.removeAll();
        mainFrame.setEnabled(true);
        if(frame.isShowing()){frame.dispose();}
        mainFrame.requestFocus();
    }

    private List<Object> validateInfo() {
        List<Object> toOut = new ArrayList<>();
        for (Map.Entry<Map.Entry<String, String>, JTextField> m:
                nameTypeField.entrySet()) {
            try {
                switch (m.getKey().getValue()) {
                    case "java.lang.Integer":
                        toOut.add(Integer.valueOf(m.getValue().getText()));
                        break;
                    case "java.lang.String":
                        toOut.add(String.valueOf(m.getValue().getText()));
                        break;
                    case "org.postgresql.util.PGmoney":
                        toOut.add(new PGmoney("$" + Double.valueOf(m.getValue().getText()) + "," + "0"));
                        break;
                    case "java.lang.Boolean":
                        toOut.add(Boolean.valueOf(m.getValue().getText()));
                        break;
                    case "java.sql.Timestamp":
                        toOut.add(Timestamp.valueOf(m.getValue().getText() + " 00:00:00.0"));
                        break;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            } catch (IllegalArgumentException ex) {
                JOptionPane.showMessageDialog(null, "Wrong input", "Wrong input", JOptionPane.ERROR_MESSAGE);
                throw new RuntimeException(ex);
            }

        }
        return toOut;
    }

    private void executeUpdateSQLQuery(List<Object> list, int id) {
        StringBuilder stringBuilder = new StringBuilder("UPDATE " + tableName + " SET ");

        var iterator = nameTypeField.entrySet().iterator();
        while (iterator.hasNext()) {
            var presentIteration = iterator.next();
            String next = presentIteration.getKey().getKey();
            String type = presentIteration.getKey().getValue();
            String toInsert = presentIteration.getValue().getText();
            if(!next.toLowerCase(Locale.ROOT).equals("id")) {
                stringBuilder.append(next).append(" = ").append(insertType(toInsert, type));
                if (iterator.hasNext()) {
                    stringBuilder.append(", ");
                } else {
                    stringBuilder.append(" WHERE id = ").append(id);

                }
            }
        }
        try {
            PreparedStatement preparedStatement = connection.prepareStatement(stringBuilder.toString());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private String insertType(String toValidate, String type){
        switch (type) {
            case "java.lang.String":
            case "java.sql.Timestamp":
                return "'" + toValidate + "'";
            case "java.lang.Integer":
            case "org.postgresql.util.PGmoney":
            case "java.lang.Boolean":
                return toValidate;
        }
        return "N/A";
    }
}
