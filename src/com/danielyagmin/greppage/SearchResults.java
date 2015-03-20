package com.danielyagmin.greppage;

import com.danielyagmin.greppage.model.ResultTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

public class SearchResults {

    private JButton mSaveButton = new JButton("Save");
    private JLabel mFilesErroredLabel = new JLabel();
    private JLabel mFilesSearchedLabel = new JLabel();
    private JLabel mFilesSkippedLabel = new JLabel();
    private JPanel mPanel;

    private void add(JComponent comp, int gridx, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        mPanel.add(comp, constraints);
    }

    private void addTable(ResultTableModel model) {
        JTable mTable = new JTable(model);
        JScrollPane scrollPane = new JScrollPane(mTable);
        GridBagConstraints scrollPaneConstraints = new GridBagConstraints();
        scrollPaneConstraints.fill = GridBagConstraints.BOTH;
        scrollPaneConstraints.weightx = 0.5;
        scrollPaneConstraints.weighty = 0.8;
        scrollPaneConstraints.gridx = 0;
        scrollPaneConstraints.gridy = 0;
        scrollPaneConstraints.gridwidth = 3;
        mPanel.add(scrollPane, scrollPaneConstraints);
    }

    public SearchResults(Options options, JPanel panel) {
        ResultTableModel model = new ResultTableModel();
        SearchThread thread = new SearchThread(options);
        mPanel = panel;
        thread.setModel(model);

        mPanel.setLayout(new GridBagLayout());
        addTable(model);
        mSaveButton = new JButton("Save");
        mSaveButton.setEnabled(false);
        add(mFilesSearchedLabel, 0, 1);
        add(mSaveButton, 1, 1);
        add(mFilesErroredLabel, 0, 2);
        add(mFilesSkippedLabel, 1, 2);

        Map<String, String> optionsMap = new HashMap<String, String>();
        optionsMap.put("Search Pattern", options.searchPattern.toString());
        optionsMap.put("Search Path", options.rootDirectory.toString());

        int index = 3;
        for(Map.Entry<String, String> entry : optionsMap.entrySet()) {
            add(new JLabel(String.format("%s:", entry.getKey())), 0, index);
            add(new JLabel(entry.getValue()), 1, index++);
        }

        thread.addListener(new SearchThreadListener() {

            int mFilesSearched = 0;
            int mFilesErrored = 0;
            int mFilesSkipped = 0;

            public void setSkippedFile(String currentfile) {
                mFilesSkipped++;
                mFilesSkippedLabel.setText(String.format("%d files skipped", mFilesSkipped));
            }

            public void setSearchingFile(String currentFile) {
                mFilesSearched++;
                mFilesSearchedLabel.setText(String.format("%d files searched", mFilesSearched));
            };

            public void setErroredOnFile(String currentFile) {
                mFilesErrored++;
                mFilesErroredLabel.setText(String.format("%d files errored", mFilesErrored));
            };

            public void complete() {
                SearchResults.this.complete();
            };

        });

        thread.start();
    }

    public void setSaveActionListener(ActionListener al) {
        mSaveButton.addActionListener(al);
    }

    public void complete() {
        JTabbedPane parent = (JTabbedPane) mPanel.getParent();
        int i = parent.indexOfComponent(mPanel);
        if(i != -1) {
            parent.setTitleAt(i, "Done");
        }
        mSaveButton.setEnabled(true);
    }

}
