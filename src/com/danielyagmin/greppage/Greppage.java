package com.danielyagmin.greppage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.FileNotFoundException;

import java.util.regex.PatternSyntaxException;

import javax.swing.SwingUtilities;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.danielyagmin.greppage.Options;
import com.danielyagmin.greppage.SearchThread;
import com.danielyagmin.greppage.SearchThreadListener;

import com.danielyagmin.greppage.model.ResultTableModel;

import com.danielyagmin.greppage.view.DirectoryChooser;
import com.danielyagmin.greppage.view.ResultTabPanel;
import com.danielyagmin.greppage.view.NewSearchDialog;
import com.danielyagmin.greppage.view.Window;

public class Greppage {

    Window mWindow;

    public static void main(String[] args) {
        Greppage greppage = new Greppage();
        greppage.load();
    }

    public void load() {

        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                mWindow = new Window();
                mWindow.addExitListener(new ExitListener());
                mWindow.addNewSearchButtonListener(new NewSearchButtonListener());
            }

        });
    }

    private void newSearch(Options options) {
        ResultTableModel model = new ResultTableModel();
        final ResultTabPanel resultTabPanel = new ResultTabPanel(model);
        SearchThread thread = new SearchThread(options);
        thread.setModel(model);
        mWindow.addResultTabPanel(resultTabPanel);

        thread.addListener(new SearchThreadListener() {

            int mFilesSearched = 0;
            int mFilesErrored = 0;
            int mFilesSkipped = 0;

            public void setSkippedFile(String currentfile) {
                mFilesSkipped++;
                resultTabPanel.setFilesSkipped(mFilesSkipped);
            };

            public void setSearchingFile(String currentFile) {
                mFilesSearched++;
                resultTabPanel.setFilesSearched(mFilesSearched);
            };

            public void setErroredOnFile(String currentFile) {
                mFilesErrored++;
                resultTabPanel.setFilesErrored(mFilesErrored);
            };

            public void complete() {
                resultTabPanel.complete();
            };

        });

        thread.start();

    }

    class ExitListener implements ActionListener {

        @Override
        public void actionPerformed(ActionEvent e) {
            System.exit(0);
        }

    }

    class NewSearchButtonListener implements ActionListener {

        public void addListeners(final NewSearchDialog dialog, final Options options) {

            dialog.addRootDirectoryChooserButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    DirectoryChooser chooser = new DirectoryChooser(dialog);
                    try {
                        options.setRootDirectory(chooser.getDirectory());
                        dialog.setRootDirectory(options.getRootDirectory());
                    } catch (FileNotFoundException fileException) {
                        // TODO Improve
                    }
                }
            });

            dialog.addStartSearchButtonListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    if(options.isValid()) {
                        Greppage.this.newSearch(options);
                        dialog.dispose();
                    }
                }

            });

            dialog.addIncludeSubDirectoriesCheckBoxListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    options.setIncludeSubDirectories(dialog.getIncludeSubDirectories());
                }

            });

            dialog.addCaseInsensitiveCheckBoxListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    options.setCaseInsensitive(dialog.getCaseInsensitive());
                }

            });

            dialog.addFileExtensionsAllowedTextFieldListener(new DocumentListener() {

                private void update() {
                    options.setFileExtensionsAllowed(dialog.getFileExtensionsAllowed());
                }

                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                public void insertUpdate(DocumentEvent e) {
                    update();
                }

            });

            dialog.addSearchPatternTextFieldListener(new DocumentListener() {

                private void update() {
                    try {
                        options.setSearchPattern(dialog.getSearchPattern());
                        dialog.setSearchPatternError("");
                    } catch(PatternSyntaxException patternException) {
                        dialog.setSearchPatternError("Invalid regular expression.");
                    }
                }

                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                public void insertUpdate(DocumentEvent e) {
                    update();
                }

            });

            dialog.addRootDirectoryTextFieldListener(new DocumentListener() {

                private void update() {
                    try {
                        options.setRootDirectory(dialog.getRootDirectory());
                        dialog.setRootDirectoryError("");
                    } catch(FileNotFoundException fileException) {
                        dialog.setRootDirectoryError("Path not found.");
                    }
                }

                public void changedUpdate(DocumentEvent e) {
                    update();
                }

                public void removeUpdate(DocumentEvent e) {
                    update();
                }

                public void insertUpdate(DocumentEvent e) {
                    update();
                }

            });

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            final NewSearchDialog dialog = new NewSearchDialog(Greppage.this.mWindow);
            final Options options = new Options();
            addListeners(dialog, options);
            dialog.ready();
        }

    }

}
