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
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class NewSearchDialog extends JDialog {

    private JTextField mSearchPathTextField;
    private JLabel mSearchPathErrorLabel;
    private JTextField mSearchStringTextField;
    private JLabel mSearchStringErrorLabel;
    private JCheckBox mIncludeSubDirectoriesCheckBox;
    private JCheckBox mUseRegularExpressionCheckBox;
    private JCheckBox mCaseInsensitiveCheckBox;
    private File mSearchPath;
    private Window mWindow;

    private void add(JComponent comp, int x, int y, int fill) {
        Container contentPane = this.getRootPane().getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = fill;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = x;
        constraints.gridy = y;
        contentPane.add(comp, constraints);
    }

    private void add(JComponent comp, int x, int y) {
        this.add(comp, x, y, GridBagConstraints.NONE);
    }

    public NewSearchDialog(Window owner) {

        super((Frame) owner, "New Search", Dialog.ModalityType.TOOLKIT_MODAL);

        Container contentPane;
        JLabel searchPathLabel = new JLabel("Search Path:");
        JButton searchFileChooser;
        JLabel searchStringLabel;
        JLabel includeSubDirectoriesLabel;

        this.mWindow = owner;
        this.createRootPane();
        
        contentPane = this.getRootPane().getContentPane();
        contentPane.setLayout(new GridBagLayout());

        this.add(searchPathLabel, 0, 0);

        this.mSearchPathTextField = new JTextField("", 15);
        this.add(this.mSearchPathTextField, 1, 0, GridBagConstraints.HORIZONTAL);

        this.mSearchPathTextField.getDocument().addDocumentListener(new DocumentListener() {

            public void changedUpdate(DocumentEvent e) {
                //
            }

            public void removeUpdate(DocumentEvent e) {
                //
            }

            public void insertUpdate(DocumentEvent e) {
                //
            }

        });

        this.mSearchPathErrorLabel = new JLabel("");
        this.mSearchPathErrorLabel.setForeground(Color.RED);
        this.add(this.mSearchPathErrorLabel, 1, 1);

        searchFileChooser = new JButton("Select Directory");
        this.add(searchFileChooser, 2, 0);
        searchFileChooser.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e){
                JFileChooser chooser = new JFileChooser();
                chooser.setCurrentDirectory(new java.io.File("."));
                chooser.setDialogTitle("Select a folder");
                chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                chooser.setAcceptAllFileFilterUsed(false);
                if(chooser.showOpenDialog(NewSearchDialog.this) == JFileChooser.APPROVE_OPTION) {
                    String path = chooser.getCurrentDirectory().getPath();
                    // TODO Strip "." after the end of the path
                    NewSearchDialog.this.mSearchPathTextField.setText(path);
                }
            }

        });

        searchStringLabel = new JLabel("Search String:");
        this.add(searchStringLabel, 0, 2);

        this.mSearchStringTextField = new JTextField("", 15);
        this.add(this.mSearchStringTextField, 1, 2, GridBagConstraints.HORIZONTAL);

        this.mSearchStringErrorLabel = new JLabel("");
        this.mSearchStringErrorLabel.setForeground(Color.RED);
        this.add(this.mSearchStringErrorLabel, 1, 3);

        includeSubDirectoriesLabel = new JLabel("Include Subdirectories:");
        this.add(includeSubDirectoriesLabel, 0, 4);

        this.mIncludeSubDirectoriesCheckBox = new JCheckBox();
        this.add(this.mIncludeSubDirectoriesCheckBox, 1, 4);

        JLabel useRegularExpressionLabel = new JLabel("Use Regular Expressions:");
        this.add(useRegularExpressionLabel, 0, 5);

        this.mUseRegularExpressionCheckBox = new JCheckBox();
        this.add(this.mUseRegularExpressionCheckBox, 1, 5);

        JLabel caseInsensitiveLabel = new JLabel("Case Insensitive:");
        this.add(caseInsensitiveLabel, 0, 6);

        this.mCaseInsensitiveCheckBox = new JCheckBox();
        this.add(this.mCaseInsensitiveCheckBox, 1, 6);

        JButton newSearchButton = new JButton("Search");
        this.add(newSearchButton, 1, 7);
        newSearchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                NewSearchDialog.this.mSearchPathErrorLabel.setText("");
                NewSearchDialog.this.mSearchStringErrorLabel.setText("");
                if(NewSearchDialog.this.mSearchPathTextField.getText().length() == 0){
                    NewSearchDialog.this.mSearchPathErrorLabel.setText("Search path cannot be empty.");
                } else if(NewSearchDialog.this.mSearchStringTextField.getText().length() == 0) {
                    NewSearchDialog.this.mSearchStringErrorLabel.setText("Search string cannot be empty.");
                } else {
                    if(NewSearchDialog.this.mSearchPath.exists()) {
                        Map optionMap = new HashMap();
                        String searchString = NewSearchDialog.this.mSearchStringTextField.getText();
                        Pattern searchPattern;
                        optionMap.put("includeSubDirectories", NewSearchDialog.this.mIncludeSubDirectoriesCheckBox.isSelected());
                        try {
                            if(!NewSearchDialog.this.mUseRegularExpressionCheckBox.isSelected()) {
                                searchString = Pattern.quote(searchString);
                            }
                            if(NewSearchDialog.this.mCaseInsensitiveCheckBox.isSelected()) {
                                searchPattern = Pattern.compile(searchString, Pattern.CASE_INSENSITIVE);
                            } else {
                                searchPattern = Pattern.compile(searchString);
                            }
                            optionMap.put("searchString", searchString);
                            optionMap.put("searchPattern", searchPattern);
                            new ResultTabPanel(NewSearchDialog.this.mWindow, NewSearchDialog.this.mSearchPath, optionMap);
                            NewSearchDialog.this.dispose();
                        } catch(PatternSyntaxException patternException) {
                            NewSearchDialog.this.mSearchPathErrorLabel.setText("Invalid regular expression.");
                        }
                    } else {
                        NewSearchDialog.this.mSearchPathErrorLabel.setText("Search path not found.");
                    }
                }
            }
        });

        this.pack();
        this.setResizable(false);
        this.setVisible(true);

    }

}
