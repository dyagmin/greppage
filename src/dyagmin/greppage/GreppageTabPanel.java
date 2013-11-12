package dyagmin.greppage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.io.File;

import java.lang.Thread;

import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

class GreppageTabPanel extends JPanel implements ThreadListener {

    private JButton mSaveButton;
    private JLabel mStatusLabel;
    private int mFilesSearched = 0;
    private int mFilesTotal = 0;
    private String mCurrentFile;
    private String STATUS_TEXT = "Searched %d out of %d files. Currently searching: %s";

    public GreppageTabPanel(File searchPath, Map optionMap) {
        super();
        GreppageWindow window = GreppageWindow.getInstance();
        String searchString;
        if(optionMap.containsKey("searchString")) {
            searchString = (String) optionMap.get("searchString");
        } else {
            searchString = ((Pattern) optionMap.get("searchPattern")).toString();
        }


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

        this.mStatusLabel = new JLabel();
        GridBagConstraints statusLabelConstraints = new GridBagConstraints();
        statusLabelConstraints.fill = GridBagConstraints.NONE;
        statusLabelConstraints.weightx = 0.5;
        statusLabelConstraints.weighty = 0.1;
        statusLabelConstraints.gridx = 0;
        statusLabelConstraints.gridy = 2;
        this.add(this.mStatusLabel, statusLabelConstraints);

        this.mSaveButton = new JButton("Save");
        this.mSaveButton.setEnabled(false);
        GridBagConstraints saveButtonConstraints = new GridBagConstraints();
        saveButtonConstraints.fill = GridBagConstraints.NONE;
        saveButtonConstraints.weightx = 0.5;
        saveButtonConstraints.weighty = 0.1;
        saveButtonConstraints.gridx = 0;
        saveButtonConstraints.gridy = 3;
        this.add(this.mSaveButton, saveButtonConstraints);
        this.mSaveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                // TODO Save
            }

        });

        // TODO Maybe this should be in a post(new Runnable()) thing
        model.thread.addListener(this);
        model.thread.start();
    }

    private void updateStatusLabel() {
        this.mStatusLabel.setText(String.format(this.STATUS_TEXT, this.mFilesSearched, this.mFilesTotal, this.mCurrentFile));
    }

    public void setCurrentFile(String currentFile) {
        this.mCurrentFile = currentFile;
        this.updateStatusLabel();
    }

    public void incrementFilesSearched() {
        this.mFilesSearched++;
        this.updateStatusLabel();
    }

    public void increaseFilesTotal(int i) {
        this.mFilesTotal = this.mFilesTotal + i;
        this.updateStatusLabel();
    }

    public void complete() {
        this.mStatusLabel.setText(String.format("Done searching %d out of %d files.", this.mFilesSearched, this.mFilesTotal));
        this.mSaveButton.setEnabled(true);
    }

}
