package dyagmin.greppage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.IOException;

import java.lang.Thread;

class GreppageThread extends Thread {

    private File rootPath;
    private String searchString;
    private boolean includeSubDirectories;
    private boolean useRegularExpression;
    private GreppageTableModel greppageTableModel;

    public GreppageThread(GreppageTableModel model, File searchPath, String searchStringArg, boolean includeSubDirectoriesArg, boolean useRegularExpression) {

        super();

        this.greppageTableModel = model;
        this.rootPath = searchPath;
        this.searchString = searchStringArg;
        this.includeSubDirectories = includeSubDirectoriesArg; // TODO Use this
        // TODO Use regular expressions

    }

    public void run() {
        this.readFiles(this.rootPath);
    }

    private void readFiles(File file) {
        if(file.isDirectory()) {
            File[] files = file.listFiles();
            for(int i = 0; i < files.length; i++) {
                this.readFiles(files[i]);
            }
        } else {
            this.findInstances(file);
        }
    }

    private void findInstances(File file) {
        BufferedReader br = null;
        try {
            String currentLine;
            int lineNumber = 0;
            br = new BufferedReader(new FileReader(file));
            System.out.println("reading file: " + file.getName());
            while((currentLine = br.readLine()) != null) {
                lineNumber++;
                if(currentLine.contains(this.searchString)) {
                    String[] row = {String.valueOf(lineNumber), currentLine, file.getName()};
                    this.greppageTableModel.addRow(row);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(br != null) {
                    br.close();
                }
            } catch(IOException ex) {
                ex.printStackTrace();
            }
        }
    }

}
