package dyagmin.greppage;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dialog;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Frame;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;

import java.io.File;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class NewSearchDialog extends JDialog {

    private JTextField searchPathTextField;
    private JLabel searchPathErrorLabel;
    private JTextField searchStringTextField;
    private JLabel searchStringErrorLabel;
    private JCheckBox includeSubDirectoriesCheckBox;
    private JCheckBox useRegularExpressionCheckBox;

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

        this.searchPathTextField = new JTextField("", 15);
        this.add(this.searchPathTextField, 1, 0, GridBagConstraints.HORIZONTAL);

        this.searchPathErrorLabel = new JLabel("");
        this.searchPathErrorLabel.setForeground(Color.RED);
        this.add(this.searchPathErrorLabel, 1, 1);

        JLabel searchStringLabel = new JLabel("Search String:");
        this.add(searchStringLabel, 0, 2);

        this.searchStringTextField = new JTextField("", 15);
        this.add(this.searchStringTextField, 1, 2, GridBagConstraints.HORIZONTAL);

        this.searchStringErrorLabel = new JLabel("");
        this.searchStringErrorLabel.setForeground(Color.RED);
        this.add(this.searchStringErrorLabel, 1, 3);

        JLabel includeSubDirectoriesLabel = new JLabel("Include Subdirectories:");
        this.add(includeSubDirectoriesLabel, 0, 4);

        this.includeSubDirectoriesCheckBox = new JCheckBox();
        this.add(this.includeSubDirectoriesCheckBox, 1, 4);

        JLabel useRegularExpressionLabel = new JLabel("Use Regular Expressions:");
        this.add(useRegularExpressionLabel, 0, 5);

        this.useRegularExpressionCheckBox = new JCheckBox();
        this.add(this.useRegularExpressionCheckBox, 1, 5);

        JButton newSearchButton = new JButton("Search");
        this.add(newSearchButton, 1, 6);
        newSearchButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {

                NewSearchDialog.this.searchPathErrorLabel.setText("");
                NewSearchDialog.this.searchStringErrorLabel.setText("");

                if(NewSearchDialog.this.searchPathTextField.getText().length() == 0){
                    NewSearchDialog.this.searchPathErrorLabel.setText("Search path cannot be empty.");

                } else if(NewSearchDialog.this.searchStringTextField.getText().length() == 0) {
                    NewSearchDialog.this.searchStringErrorLabel.setText("Search string cannot be empty.");

                } else {

                    File searchPath = new File(NewSearchDialog.this.searchPathTextField.getText());
                    if(searchPath.exists()) {
                        // TODO open tab in parent
                        NewSearchDialog.this.dispose();
                        
                    } else {
                        NewSearchDialog.this.searchPathErrorLabel.setText("Search path not found.");
                    }

                }
                
            }

        });

        this.pack();
        this.setResizable(false);
        this.setVisible(true);

    }

}
