package databaseApp;

import javax.swing.*;

public class QueryButton extends JButton {
    private final String SQLQuery;
    private final String text;

    public QueryButton(String text, String SQLQuery){
        super(text);
        this.text = text;
        this.SQLQuery = SQLQuery;
    }

    public String getSQLQuery() {
        return SQLQuery;
    }

    @Override
    public String toString() {
        return text;
    }
}
