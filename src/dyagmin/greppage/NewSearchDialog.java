package dyagmin.greppage;

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

public class NewSearchDialog extends JDialog {

    private JTextField mSearchPathTextField;
    private JLabel mSearchPathErrorLabel;
    private JTextField mSearchStringTextField;
    private JLabel mSearchStringErrorLabel;
    private JCheckBox mIncludeSubDirectoriesCheckBox;
    private JCheckBox mUseRegularExpressionCheckBox;
    private File mSearchPath;

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

    public NewSearchDialog(GreppageWindow owner) {

        super((Frame) owner, "New Search", Dialog.ModalityType.TOOLKIT_MODAL);

        this.createRootPane();
        
        Container contentPane = this.getRootPane().getContentPane();
        contentPane.setLayout(new GridBagLayout());

        JLabel searchPathLabel = new JLabel("Search Path:");
        this.add(searchPathLabel, 0, 0);

        this.mSearchPathTextField = new JTextField("", 15);
        this.add(this.mSearchPathTextField, 1, 0, GridBagConstraints.HORIZONTAL);

        this.mSearchPathErrorLabel = new JLabel("");
        this.mSearchPathErrorLabel.setForeground(Color.RED);
        this.add(this.mSearchPathErrorLabel, 1, 1);

        JButton searchFileChooser = new JButton("Select Directory");
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
                    NewSearchDialog.this.mSearchPath = chooser.getCurrentDirectory();
                    NewSearchDialog.this.mSearchPathTextField.setText(NewSearchDialog.this.mSearchPath.getPath());
                }
            }

        });

        JLabel searchStringLabel = new JLabel("Search String:");
        this.add(searchStringLabel, 0, 2);

        this.mSearchStringTextField = new JTextField("", 15);
        this.add(this.mSearchStringTextField, 1, 2, GridBagConstraints.HORIZONTAL);

        this.mSearchStringErrorLabel = new JLabel("");
        this.mSearchStringErrorLabel.setForeground(Color.RED);
        this.add(this.mSearchStringErrorLabel, 1, 3);

        JLabel includeSubDirectoriesLabel = new JLabel("Include Subdirectories:");
        this.add(includeSubDirectoriesLabel, 0, 4);

        this.mIncludeSubDirectoriesCheckBox = new JCheckBox();
        this.add(this.mIncludeSubDirectoriesCheckBox, 1, 4);

        JLabel useRegularExpressionLabel = new JLabel("Use Regular Expressions:");
        this.add(useRegularExpressionLabel, 0, 5);

        this.mUseRegularExpressionCheckBox = new JCheckBox();
        this.add(this.mUseRegularExpressionCheckBox, 1, 5);

        JButton newSearchButton = new JButton("Search");
        this.add(newSearchButton, 1, 6);
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
                        optionMap.put("includeSubDirectories", NewSearchDialog.this.mIncludeSubDirectoriesCheckBox.isSelected());
                        if(NewSearchDialog.this.mUseRegularExpressionCheckBox.isSelected()) {
                            try {
                                Pattern searchPattern = Pattern.compile(searchString);
                                optionMap.put("searchPattern", searchPattern);
                                new GreppageTabPanel(NewSearchDialog.this.mSearchPath, optionMap);
                            } catch(PatternSyntaxException patternException) {
                                NewSearchDialog.this.mSearchPathErrorLabel.setText("Invalid regular expression.");
                            }
                        } else {
                            optionMap.put("searchString", searchString);
                            new GreppageTabPanel(NewSearchDialog.this.mSearchPath, optionMap);
                        }
                        NewSearchDialog.this.dispose();
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