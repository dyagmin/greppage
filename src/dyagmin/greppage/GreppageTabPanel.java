package dyagmin.greppage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.io.File;

import java.lang.Thread;

import java.util.Map;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class GreppageTabPanel extends JPanel implements ThreadListener {

    private JButton saveButton;
    private JLabel statusLabel;

    public GreppageTabPanel(File searchPath, Map optionMap) {
        super();
        GreppageWindow window = GreppageWindow.getInstance();
        String title = (String) optionMap.get("searchString") + " (" + String.valueOf(window.tabbedPane.getTabCount() + ")");
        window.tabbedPane.addTab(title, this);
        this.setLayout(new GridBagLayout());

        JLabel summaryLabel = new JLabel("Searching for " + (String) optionMap.get("searchString") + " in " + searchPath.getAbsolutePath());
        GridBagConstraints summaryLabelConstraints = new GridBagConstraints();
        summaryLabelConstraints.fill = GridBagConstraints.NONE;
        summaryLabelConstraints.weightx = 0.5;
        summaryLabelConstraints.weighty = 0.1;
        summaryLabelConstraints.gridx = 0;
        summaryLabelConstraints.gridy = 0;
        this.add(summaryLabel, summaryLabelConstraints);

        GreppageTableModel model = new GreppageTableModel(searchPath, optionMap);
        JTable table = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(table);
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.fill = GridBagConstraints.BOTH;
        scrollPaneConstraints.weightx = 0.5;
        scrollPaneConstraints.weighty = 0.8;
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 1;
        this.add(scrollPane, scrollPaneConstraints);

        this.statusLabel = new JLabel();
        GridBagConstraints statusLabelConstraints = new GridBagConstraints();
        statusLabelConstraints.fill = GridBagConstraints.NONE;
        statusLabelConstraints.weightx = 0.5;
        statusLabelConstraints.weighty = 0.1;
        statusLabelConstraints.gridx = 0;
        statusLabelConstraints.gridy = 2;
        this.add(this.statusLabel, statusLabelConstraints);

        this.saveButton = new JButton("Save");
        this.saveButton.setEnabled(false);
        GridBagConstraints saveButtonConstraints = new GridBagConstraints();
        saveButtonConstraints.fill = GridBagConstraints.NONE;
        saveButtonConstraints.weightx = 0.5;
        saveButtonConstraints.weighty = 0.1;
        saveButtonConstraints.gridx = 0;
        saveButtonConstraints.gridy = 3;
        this.add(this.saveButton, saveButtonConstraints);
        this.saveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Save
            }

        });

        model.thread.addListener(this);
        model.thread.start();
    }

    public void update(String s) {
        this.statusLabel.setText(s);
    }

    public void complete() {
        this.saveButton.setEnabled(true);
    }

}
