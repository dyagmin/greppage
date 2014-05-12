package com.danielyagmin.greppage.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.io.File;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NewSearchDialog extends JDialog {

    private JTextField mRootDirectoryTextField;
    private JLabel mRootDirectoryErrorLabel = new JLabel("");
    private JTextField mSearchPatternTextField;
    private JTextField mFileExtensionsAllowedTextField;
    private JButton mStartSearchButton = new JButton("Search");
    private JLabel mSearchPatternErrorLabel = new JLabel("");
    private JCheckBox mIncludeSubDirectoriesCheckBox;
    private JCheckBox mCaseInsensitiveCheckBox;
    private Window mWindow;
    private JButton mRootDirectoryChooserButton;
    private int mRowNum = 0;

    private void add(JComponent comp, int x, int fill) {
        Container contentPane = getRootPane().getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = fill;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = x;
        constraints.gridy = mRowNum;
        contentPane.add(comp, constraints);
    }

    private void add(JComponent comp, int x) {
        add(comp, x, GridBagConstraints.NONE);
    }

    private void addRootDirectoryRow() {
        add(new JLabel("Search Path:"), 0);
        mRootDirectoryTextField = new JTextField("", 15);
        add(mRootDirectoryTextField, 1, GridBagConstraints.HORIZONTAL);

        mRootDirectoryChooserButton = new JButton("Select Directory");
        add(mRootDirectoryChooserButton, 2);
        mRowNum++;

        mRootDirectoryErrorLabel.setForeground(Color.RED);
        add(mRootDirectoryErrorLabel, 1);
        mRowNum++;
    }

    public void addRootDirectoryChooserButtonListener(ActionListener listener) {
        mRootDirectoryChooserButton.addActionListener(listener);
    }

    public void addRootDirectoryTextFieldListener(DocumentListener dl) {
        mRootDirectoryTextField.getDocument().addDocumentListener(dl);
    }

    public void addFileExtensionsAllowedTextFieldListener(DocumentListener dl) {
        mFileExtensionsAllowedTextField.getDocument().addDocumentListener(dl);
    }

    public void addSearchPatternTextFieldListener(DocumentListener dl) {
        mSearchPatternTextField.getDocument().addDocumentListener(dl);
    }

    public String getRootDirectory() {
        return mRootDirectoryTextField.getText();
    }

    public void setRootDirectory(String path) {
        mRootDirectoryTextField.setText(path);
    }

    public String getSearchPattern() {
        return mSearchPatternTextField.getText();
    }

    private void addSearchPatternRow() {
        add(new JLabel("Search Pattern:"), 0);
        mSearchPatternTextField = new JTextField("", 15);
        add(mSearchPatternTextField, 1, GridBagConstraints.HORIZONTAL);
        mRowNum++;

        mSearchPatternErrorLabel.setForeground(Color.RED);
        add(mSearchPatternErrorLabel, 1);
        mRowNum++;
    }

    private void addOptionRows() {
        // Include subdirectories
        add(new JLabel("Include Subdirectories:"), 0);
        mIncludeSubDirectoriesCheckBox = new JCheckBox();
        add(mIncludeSubDirectoriesCheckBox, 1);
        mRowNum++;

        // Case insensitive
        add(new JLabel("Case Insensitive:"), 0);
        mCaseInsensitiveCheckBox = new JCheckBox();
        add(mCaseInsensitiveCheckBox, 1);
        mRowNum++;

        // File extensions allowed
        add(new JLabel("File extensions (e.g. \"txt, csv\")"), 0);
        mFileExtensionsAllowedTextField = new JTextField("", 15);
        add(mFileExtensionsAllowedTextField, 1);
        mRowNum++;
    }

    public String getFileExtensionsAllowed() {
        return mFileExtensionsAllowedTextField.getText();
    }

    public boolean getCaseInsensitive() {
        return mCaseInsensitiveCheckBox.isSelected();
    }

    public boolean getIncludeSubDirectories() {
        return mIncludeSubDirectoriesCheckBox.isSelected();
    }

    private void addStartSearchButton() {
        add(mStartSearchButton, 1);
        mRowNum++;
    }

    public void addIncludeSubDirectoriesCheckBoxListener(ActionListener al) {
        this.mIncludeSubDirectoriesCheckBox.addActionListener(al);
    }

    public void addCaseInsensitiveCheckBoxListener(ActionListener al) {
        this.mCaseInsensitiveCheckBox.addActionListener(al);
    }

    public void addStartSearchButtonListener(ActionListener al) {
        this.mStartSearchButton.addActionListener(al);
    }

    public void setSearchPatternError(String error) {
        mSearchPatternErrorLabel.setText(error);
    }

    public void setRootDirectory(File file) {
        mRootDirectoryTextField.setText(file.getAbsolutePath());
    }

    public void setRootDirectoryError(String error) {
        mRootDirectoryErrorLabel.setText(error);
    }

    public NewSearchDialog(Window owner) {

        super((Frame) owner, "New Search", Dialog.ModalityType.TOOLKIT_MODAL);

        mWindow = owner;
        createRootPane();
        getRootPane().getContentPane().setLayout(new GridBagLayout());

        addRootDirectoryRow();
        addSearchPatternRow();
        addOptionRows();
        addStartSearchButton();

        pack();
        setResizable(false);
    }

    public void ready() {
        setVisible(true);
    }

    public JFrame getOwner() {
        return mWindow;
    }

}
