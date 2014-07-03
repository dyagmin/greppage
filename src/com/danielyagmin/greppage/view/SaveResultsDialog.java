package com.danielyagmin.greppage.view;

import java.awt.event.ActionListener;
import java.awt.Color;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.event.DocumentListener;

public class SaveResultsDialog extends JDialog {

    private Window mWindow;
    private JTextField mSaveAsTextField = new JTextField("", 15);
    private JButton mSaveAsButton = new JButton("Save As...");
    private JLabel mSavePathErrorLabel = new JLabel("");
    private JButton mSaveButton = new JButton("Save");

    private void addSavePathTextField() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(mSaveAsTextField, constraints);
        mSaveAsTextField.setMinimumSize(mSaveAsTextField.getPreferredSize());
    }

    private void addSaveAsButton() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(mSaveAsButton, constraints);
    }

    private void addSavePathErrorLabel() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.anchor = GridBagConstraints.WEST;
        constraints.insets = new Insets(5, 5, 5, 5);
        mSavePathErrorLabel.setForeground(Color.RED);
        add(mSavePathErrorLabel, constraints);
    }

    private void addSaveButton() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(5, 5, 5, 5);
        add(mSaveButton, constraints);
    }

    public SaveResultsDialog(Window owner) {

        super((Frame) owner, "Save Results", Dialog.ModalityType.TOOLKIT_MODAL);

        mWindow = owner;
        createRootPane();
        getRootPane().getContentPane().setLayout(new GridBagLayout());

        addSavePathTextField();
        addSavePathErrorLabel();
        addSaveAsButton();
        addSavePathErrorLabel();
        addSaveButton();

        pack();
        setResizable(false);
    }

    public void ready() {
        setVisible(true);
    }

    public void addSavePathTextFieldListener(DocumentListener listener) {
        mSaveAsTextField.getDocument().addDocumentListener(listener);
    }

    public void addSaveAsButtonActionListener(ActionListener listener) {
        mSaveAsButton.addActionListener(listener);
    }

    public void addSaveButtonActionListener(ActionListener listener) {
        mSaveButton.addActionListener(listener);
    }

    public void setSavePath(String path) {
        mSaveAsTextField.setText(path);
    }

    public void setSavePathError(String error) {
        mSavePathErrorLabel.setText(error);
    }

    public String getSaveAs() {
        return mSaveAsTextField.getText();
    }

}
