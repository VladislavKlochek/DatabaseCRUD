package databaseApp;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DeleteJWindow extends JWindow {
    private DeleteButton deleteButton;
    private final List<DeletableJPanel> toDelete = new ArrayList<>();
    private final Connection connection;
    private final JPanel panel;
    public DeleteJWindow(Connection connection, JPanel panel){
        this.connection = connection;
        this.panel = panel;
    }

    public void setDeleteButton(DeleteButton deleteButton) {
        this.deleteButton = deleteButton;
    }

    public DeleteButton getDeleteButton() {
        return deleteButton;
    }
    public void addRecordToDelete(DeletableJPanel deletableJPanel){
        toDelete.add(deletableJPanel);
        if(deleteButton.getActionListeners().length == 0){
            deleteButton.addActionListener(e -> {
                for (DeletableJPanel panel :
                        toDelete) {
                    try {
                        PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM "
                                + panel.getTableName() + " WHERE id = " + panel.getId());
                        preparedStatement.executeUpdate();
                    } catch (SQLException ex) {
                        throw new RuntimeException(ex);
                    }
                }
                toDelete.forEach(value-> value.setVisible(false));
                ButtonDropUpListener.deleteWindow.check();
                toDelete.clear();
                panel.revalidate();
                panel.repaint();
            });
        }
    }
    public void removeRecordFromListToDelete(DeletableJPanel deletableJPanel){
        toDelete.remove(toDelete.stream().filter(e->e.getId() == deletableJPanel.getId()).findAny().get());
    }
    public void check(){
        ButtonDropUpListener.deleteWindow.setVisible(false);
        ButtonDropUpListener.deleteWindow.deleteButton.atLeastOneRecordIsChosen = false;
    }
    public void unCheck(){
        ButtonDropUpListener.deleteWindow.setVisible(true);
        ButtonDropUpListener.deleteWindow.deleteButton.atLeastOneRecordIsChosen = true;
    }
}
