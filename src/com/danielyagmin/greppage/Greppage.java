package com.danielyagmin.greppage;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.util.Map;
import java.util.HashMap;

import java.util.regex.PatternSyntaxException;

import javax.swing.JFileChooser;
import javax.swing.SwingUtilities;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.danielyagmin.greppage.Options;
import com.danielyagmin.greppage.SearchThread;
import com.danielyagmin.greppage.SearchThreadListener;

import com.danielyagmin.greppage.model.ResultTableModel;

import com.danielyagmin.greppage.view.ResultTabPanel;
import com.danielyagmin.greppage.view.NewSearchDialog;
import com.danielyagmin.greppage.view.SaveResultsDialog;
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

        Map<String, String> optionsMap = new HashMap<String, String>();
        optionsMap.put("Search Pattern", options.getSearchPattern().toString());
        optionsMap.put("Search Path", options.getRootDirectory().toString());
        resultTabPanel.setOptionsTexts(optionsMap);

        resultTabPanel.addSaveResultsButtonListener(new SaveResultsButtonListener(model));

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
                    JFileChooser chooser = new JFileChooser();
                    chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
                    chooser.setDialogTitle("Select a Folder");
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.setCurrentDirectory(options.getRootDirectory());
                    if(chooser.showOpenDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                        try {
                            String path = chooser.getSelectedFile().toString();
                            options.setRootDirectory(path);
                            dialog.setRootDirectory(options.getRootDirectory());
                        } catch (FileNotFoundException e) {
                            dialog.setRootDirectoryError("Could not find directory.");
                        }
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
                    dialog.setRootDirectoryError("");
                    try {
                        options.setRootDirectory(dialog.getRootDirectory());
                    } catch(FileNotFoundException fileException) {
                        dialog.setRootDirectoryError("Cannot read path.");
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

    class SaveResultsButtonListener implements ActionListener {

        private ResultTableModel mModel;

        public SaveResultsButtonListener(ResultTableModel model) {
            mModel = model;
        }

        public void addListeners(final SaveResultsDialog dialog) {

            dialog.addSavePathTextFieldListener(new DocumentListener() {

                private void update() {
                    File file = new File(dialog.getSaveAs()).getParentFile();
                    if(file == null || !file.canWrite()) {
                        dialog.setSavePathError("Unwritable file path.");
                    } else {
                        dialog.setSavePathError("");
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

            dialog.addSaveAsButtonActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    JFileChooser chooser = new JFileChooser();
                    File file = new File(dialog.getSaveAs());
                    chooser.setDialogTitle("Save As");
                    chooser.setAcceptAllFileFilterUsed(false);
                    chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                    chooser.setApproveButtonText("Save As");
                    chooser.setCurrentDirectory(file.getParentFile());
                    if(chooser.showSaveDialog(dialog) == JFileChooser.APPROVE_OPTION) {
                        String path = chooser.getSelectedFile().toString();
                        dialog.setSavePath(path);
                    }
                }

            });

            dialog.addSaveButtonActionListener(new ActionListener() {

                @Override
                public void actionPerformed(ActionEvent event) {
                    File outputFile = new File(dialog.getSaveAs());
                    SearchResultsCSVWriter writer = new SearchResultsCSVWriter(outputFile);
                    dialog.setSavePathError("");
                    try {
                        writer.write(mModel);
                    } catch(IOException e) {
                        dialog.setSavePathError("Could not save file.");
                    }
                    dialog.dispose();
                }

            });

        }

        @Override
        public void actionPerformed(ActionEvent e) {
            SaveResultsDialog dialog = new SaveResultsDialog(Greppage.this.mWindow);
            addListeners(dialog);
            dialog.ready();
        }

    }

}
