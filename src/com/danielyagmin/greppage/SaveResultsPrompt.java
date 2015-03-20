package com.danielyagmin.greppage;

import com.danielyagmin.greppage.model.ResultTableModel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

public class SaveResultsPrompt {

    private ResultTableModel mModel;
    private JDialog mDialog;
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
        mDialog.add(mSaveAsTextField, constraints);
        mSaveAsTextField.setMinimumSize(mSaveAsTextField.getPreferredSize());

        mSaveAsTextField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent ae) {
                File file = new File(mSaveAsTextField.getText()).getParentFile();
                if (file == null || !file.canWrite()) {
                    mSavePathErrorLabel.setText("Unwritable file path.");
                } else {
                    mSavePathErrorLabel.setText("");
                }
            }

        });

    }

    private void addSaveAsButton() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 1;
        constraints.gridy = 0;
        constraints.insets = new Insets(5, 5, 5, 5);
        mDialog.add(mSaveAsButton, constraints);

        mSaveAsButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                JFileChooser chooser = new JFileChooser();
                File file = new File(mSaveAsTextField.getText());
                chooser.setDialogTitle("Save As");
                chooser.setAcceptAllFileFilterUsed(false);
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.setApproveButtonText("Save As");
                chooser.setCurrentDirectory(file.getParentFile());
                if (chooser.showSaveDialog(mDialog) == JFileChooser.APPROVE_OPTION) {
                    mSaveAsTextField.setText(chooser.getSelectedFile().toString());
                }
            }

        });

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
        mDialog.add(mSavePathErrorLabel, constraints);
    }

    private void addSaveButton() {
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0.5;
        constraints.weighty = 0.1;
        constraints.gridx = 0;
        constraints.gridy = 2;
        constraints.insets = new Insets(5, 5, 5, 5);
        mDialog.add(mSaveButton, constraints);
        mSaveButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {
                File outputFile = new File(mSaveAsTextField.getText());
                SearchResultsCSVWriter writer = new SearchResultsCSVWriter(outputFile);
                mSavePathErrorLabel.setText("");
                try {
                    writer.write(mModel);
                } catch (IOException e) {
                    mSavePathErrorLabel.setText("Could not save file.");
                }
                mDialog.dispose();
            }

        });
    }

    public SaveResultsPrompt(JFrame window, SearchResults searchResults) {
        mDialog = new JDialog(window, "Save Results", Dialog.ModalityType.TOOLKIT_MODAL);
        mDialog.getRootPane().getContentPane().setLayout(new GridBagLayout());

        addSavePathTextField();
        addSaveAsButton();
        addSavePathErrorLabel();
        addSaveButton();

        mDialog.pack();
        mDialog.setResizable(false);
        mDialog.setVisible(true);
    }

}
