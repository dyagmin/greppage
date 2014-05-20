package com.danielyagmin.greppage.view;

import java.awt.event.ActionListener;
import java.awt.Dialog;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JTextField;

import javax.swing.event.DocumentListener;

public class SaveResultsDialog extends JDialog {

    private Window mWindow;
    private JTextField mSaveAsTextField = new JTextField("");
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
        add(mSaveAsTextField, constraints);
        mSaveAsTextField.setColumns(15);
    }

    private void addSaveAsButton() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        add(mSaveAsButton, constraints);
    }

    private void addSavePathErrorLabel() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 1;
        add(mSavePathErrorLabel, constraints);
    }

    private void addSaveButton() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 2;
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

    public void setSavePathError(String path) {
        mSavePathErrorLabel.setText(path);
    }

    public String getSaveAs() {
        return mSaveAsTextField.getText();
    }

}
