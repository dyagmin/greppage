package com.danielyagmin.greppage.model;

import java.io.File;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

public class ResultTableModel extends AbstractTableModel {

    private String[] columnNames = {"Line Number", "Line", "File"};
    private ArrayList<String[]> data = new ArrayList<String[]>();

    public ResultTableModel() {
        super();
    }

    public int getColumnCount() {
        return this.columnNames.length;
    }

    public int getRowCount() {
        return this.data.size();
    }

    public String getColumnName(int col) {
        return this.columnNames[col];
    }

    public Object getValueAt(int row, int col) {
        return this.data.get(row)[col];
    }

    public void setValueAt(Object value, int row, int col) {
        this.fireTableCellUpdated(row, col);
    }

    public void addRow(String[] row) {
        this.data.add(row);
        this.fireTableDataChanged();
    }

}
