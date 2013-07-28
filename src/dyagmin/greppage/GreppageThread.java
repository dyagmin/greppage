package dyagmin.greppage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

class GreppageThread extends Thread {

    private File rootPath;
    private String searchString;
    private boolean includeSubDirectories;
    private boolean useRegularExpression;
    private List<ThreadListener> listeners;
    private GreppageTableModel greppageTableModel;
    private int filesSearched = 0;
    private int filesSkipped = 0;

    public GreppageThread(GreppageTableModel model, File searchPath, Map optionMap) {
        super();
        this.listeners = new ArrayList<ThreadListener>();
        this.greppageTableModel = model;
        this.rootPath = searchPath;
        this.searchString = (String) optionMap.get("searchString");
        this.includeSubDirectories = (Boolean) optionMap.get("includeSubDirectories"); // TODO
        // TODO Use regular expressions
    }

    public void addListener(ThreadListener tl) {
        this.listeners.add(tl);
    }

    public void run() {
        this.readFiles(this.rootPath);
        for(ThreadListener tl: this.listeners) {
            tl.update("Done");
            tl.complete();
        }
    }

    private void readFiles(File file) {
        for(ThreadListener tl: this.listeners) {
            tl.update("Reading file: " + file.getAbsolutePath() + "...");
        }
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
            while((currentLine = br.readLine()) != null) {
                lineNumber++;
                if(currentLine.contains(this.searchString)) {
                    String[] row = {String.valueOf(lineNumber), currentLine, file.getAbsolutePath()};
                    this.greppageTableModel.addRow(row);
                }
            }
            this.filesSearched++;
        } catch (FileNotFoundException e) {
            //e.printStackTrace();
            this.filesSkipped++;
        } catch (IOException e) {
            //e.printStackTrace();
            this.filesSkipped++;
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
