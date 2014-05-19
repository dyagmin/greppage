package com.danielyagmin.greppage.view;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.io.File;

import java.lang.Integer;
import java.lang.Thread;

import java.util.Map;
import java.util.regex.Pattern;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JFileChooser;
import javax.swing.SwingConstants;

import com.danielyagmin.greppage.model.ResultTableModel;

public class ResultTabPanel extends JPanel {

    private JButton mSaveButton = new JButton("Save");
    private JLabel mFilesErroredLabel = new JLabel();
    private JLabel mFilesSearchedLabel = new JLabel();
    private JLabel mFilesSkippedLabel = new JLabel();
    private JLabel mOptionsLabel = new JLabel();
    private JTable mTable;

    private void add(JComponent comp, int gridx, int gridy) {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = gridx;
        constraints.gridy = gridy;
        add(comp, constraints);
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
        add(scrollPane, scrollPaneConstraints);
    }

    private void initialize(ResultTableModel model) {
        setLayout(new GridBagLayout());
        addTable(model);
        mSaveButton = new JButton("Save");
        mSaveButton.setEnabled(false);
        add(mFilesErroredLabel, 0, 1);
        add(mFilesSearchedLabel, 1, 1);
        add(mFilesSkippedLabel, 2, 1);
        add(mOptionsLabel, 0, 2);
        add(mSaveButton, 1, 3);
    }

    public ResultTabPanel(ResultTableModel model) {
        super();
        initialize(model);
    }

    public void complete() {
        JTabbedPane parent = (JTabbedPane) getParent();
        int i = parent.indexOfComponent(this);
        if(i != -1) {
            parent.setTitleAt(i, "Done");
        }
        mSaveButton.setEnabled(true);
    }

    public String getTitle() {
        return "Searching...";
    }

    public void setFilesSkipped(int filesSkipped) {
        mFilesSkippedLabel.setText(String.format("%d files skipped", filesSkipped));
    }

    public void setFilesSearched(int filesSearched) {
        mFilesSearchedLabel.setText(String.format("%d files searched", filesSearched));
    }

    public void setFilesErrored(int filesErrored) {
        mFilesErroredLabel.setText(String.format("%d files errored", filesErrored));
    }

}
