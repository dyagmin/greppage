package dyagmin.greppage;

import java.io.File;

import java.util.ArrayList;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

class GreppageTableModel extends AbstractTableModel {

    private String[] columnNames = {"Line Number", "Line", "File"};
    private ArrayList<String[]> data = new ArrayList<String[]>();
    public GreppageThread thread;

    public GreppageTableModel(File searchPath, Map optionMap) {
        super();
        this.thread = new GreppageThread(this, searchPath, optionMap);
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
        int rowCount = this.getRowCount() + 1;
        this.data.add(row);
        this.fireTableCellUpdated(rowCount, 0);
        this.fireTableCellUpdated(rowCount, 1);
        this.fireTableCellUpdated(rowCount, 2);
    }

}
