package dyagmin.greppage;

import java.awt.BorderLayout;

import java.io.File;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class GreppagePanel extends JPanel {

    private JTable table;

    public GreppagePanel(File searchPath, String searchString, boolean includeSubDirectories, boolean useRegularExpression) {

        super();

        GreppageWindow window = GreppageWindow.getInstance();
        window.addTab(searchString, this);

        this.setLayout(new BorderLayout());
        this.table = new JTable(new GreppageTableModel(searchPath, searchString, includeSubDirectories, useRegularExpression));
        JScrollPane scrollPane = new JScrollPane(this.table);
        this.add(scrollPane);
    }

}
