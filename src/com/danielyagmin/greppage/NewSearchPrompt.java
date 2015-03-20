package com.danielyagmin.greppage;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

public class NewSearchPrompt {

    private JTextField mRootDirectoryTextField;
    private JTextField mFileExtensionsAllowedTextField;
    private JLabel mSearchPatternErrorLabel = new JLabel("");
    private JCheckBox mIncludeSubDirectoriesCheckBox;
    private JCheckBox mCaseInsensitiveCheckBox;

    private void add(JDialog dialog, JComponent comp, int x, int y, int fill) {
        Container contentPane = dialog.getRootPane().getContentPane();
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = fill;
        constraints.weightx = 0.5;
        constraints.weighty = 0.5;
        constraints.gridx = x;
        constraints.gridy = y;
        constraints.insets = new Insets(5, 5, 5, 5);
        constraints.anchor = GridBagConstraints.WEST;
        contentPane.add(comp, constraints);
    }

    private void add(JDialog dialog, JComponent comp, int x, int y) {
        add(dialog, comp, x, y, GridBagConstraints.NONE);
    }

    private boolean validateRootDirectory(String rawPath, Options options) {
        File path = new File(rawPath);
        if(!path.canRead()) {
            return true;
        } else {
            options.rootDirectory = path;
            return false;
        }
    }

    public NewSearchPrompt(JFrame window, final NewSearchListener newSearchListener) {
        int rowNum = 0;
        final JDialog dialog = new JDialog(window, "New Search", JDialog.ModalityType.TOOLKIT_MODAL);
        final Options options = new Options();
        final JLabel rootDirectoryErrorLabel = new JLabel("");

        dialog.getRootPane().getContentPane().setLayout(new GridBagLayout());

        add(dialog, new JLabel("Search Path:"), 0, rowNum);
        mRootDirectoryTextField = new JTextField("", 15);
        mRootDirectoryTextField.setMinimumSize(mRootDirectoryTextField.getPreferredSize());
        add(dialog, mRootDirectoryTextField, 1, rowNum++, GridBagConstraints.HORIZONTAL);
        mRootDirectoryTextField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                String rawPath = mRootDirectoryTextField.getText();
                if(validateRootDirectory(rawPath, options)) {
                    rootDirectoryErrorLabel.setText("Cannot read path.");
                } else {
                    rootDirectoryErrorLabel.setText("");
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

        });

        JButton rootDirectoryChooserButton = new JButton("Select Directory");
        add(dialog, rootDirectoryChooserButton, 2, rowNum);
        rowNum++;

        rootDirectoryErrorLabel.setForeground(Color.RED);
        add(dialog, rootDirectoryErrorLabel, 1, rowNum);
        rootDirectoryChooserButton.addActionListener(
                new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent event) {
                        JFileChooser chooser = new JFileChooser();
                        String rawPath = mRootDirectoryTextField.getText();
                        File path = new File(rawPath);
                        if(!path.canRead()) {
                            path = new File("~");
                        }
                        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                        chooser.setDialogTitle("Select a Folder");
                        chooser.setAcceptAllFileFilterUsed(false);
                        chooser.setCurrentDirectory(path);
                        if(chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                            File newPath = chooser.getSelectedFile();
                            if(newPath.canRead()) {
                                mRootDirectoryTextField.setText(newPath.toString());
                            } else {
                                rootDirectoryErrorLabel.setText("Could not find directory.");
                            }
                        }
                    }
                }
        );
        rowNum++;

        add(dialog, new JLabel("Search Pattern:"), 0, rowNum);
        final JTextField searchPatternTextField = new JTextField("", 15);
        searchPatternTextField.setMinimumSize(searchPatternTextField.getPreferredSize());
        add(dialog, searchPatternTextField, 1, rowNum++, GridBagConstraints.HORIZONTAL);
        searchPatternTextField.getDocument().addDocumentListener(new DocumentListener() {

            private void update() {
                options.searchPattern = getSearchPattern(searchPatternTextField.getText(), mCaseInsensitiveCheckBox.isSelected());
                if(options.searchPattern == null) {
                    mSearchPatternErrorLabel.setText("Invalid regular expression.");
                } else {
                    mSearchPatternErrorLabel.setText("");
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                update();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                update();
            }

        });

        mSearchPatternErrorLabel.setForeground(Color.RED);
        add(dialog, mSearchPatternErrorLabel, 1, rowNum++);

        // Include subdirectories
        add(dialog, new JLabel("Include Subdirectories:"), 0, rowNum);
        mIncludeSubDirectoriesCheckBox = new JCheckBox();
        add(dialog, mIncludeSubDirectoriesCheckBox, 1, rowNum++);

        // Case insensitive
        add(dialog, new JLabel("Case Insensitive:"), 0, rowNum);
        mCaseInsensitiveCheckBox = new JCheckBox();
        add(dialog, mCaseInsensitiveCheckBox, 1, rowNum++);

        // File extensions allowed
        add(dialog, new JLabel("File extensions (e.g. \"txt, csv\")"), 0, rowNum);
        mFileExtensionsAllowedTextField = new JTextField("", 15);
        mFileExtensionsAllowedTextField.setMinimumSize(mFileExtensionsAllowedTextField.getPreferredSize());
        add(dialog, mFileExtensionsAllowedTextField, 1, rowNum++);

        JButton startSearchButton = new JButton("Search");
        add(dialog, startSearchButton, 1, rowNum);
        startSearchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent event) {

                options.includeSubDirectories = mIncludeSubDirectoriesCheckBox.isSelected();
                options.searchPattern = getSearchPattern(searchPatternTextField.getText(), mCaseInsensitiveCheckBox.isSelected());

                String extensions = mFileExtensionsAllowedTextField.getText();
                if(extensions.length() > 0) {
                    options.fileExtensionsAllowed = extensions.split("[\\s,]+");
                }

                if (options.searchPattern != null && options.rootDirectory != null) {
                    newSearchListener.newSearch(options);
                    dialog.dispose();
                }
            }

        });

        dialog.pack();
        dialog.setResizable(false);
        dialog.setVisible(true);

    }

    private Pattern getSearchPattern(String text, boolean caseInsensitive) {
        try {
            if (caseInsensitive) {
                return Pattern.compile(text, Pattern.CASE_INSENSITIVE);
            } else {
                return Pattern.compile(text);
            }
        } catch(PatternSyntaxException ex) {
            return null;
        }
    }

}
