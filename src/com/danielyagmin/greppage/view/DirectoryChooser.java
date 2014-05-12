package com.danielyagmin.greppage.view;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

public class DirectoryChooser extends JFileChooser {

    private JDialog mOwner;

    public DirectoryChooser(JDialog owner) {
        super();
        mOwner = owner;
        setCurrentDirectory(new java.io.File("."));
        setDialogTitle("Select a Folder");
        setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        setAcceptAllFileFilterUsed(false);
    }

    /**
     * @todo get selected folder too
     **/
    public String getDirectory() {
        String path = null;
        if(showOpenDialog(mOwner) == JFileChooser.APPROVE_OPTION) {
            path = getCurrentDirectory().getPath();
        }
        return path;
    }


}
