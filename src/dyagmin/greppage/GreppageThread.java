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
import java.util.regex.Pattern;

class GreppageThread extends Thread {

    private File mRootPath;
    private String mSearchString;
    private Pattern mSearchPattern;
    private boolean mIncludeSubDirectories;
    private boolean mUseRegularExpression;
    private boolean mHasAllFiles;
    private List<ThreadListener> mListeners;
    private List<File> mFilesQueue;
    private GreppageTableModel mGreppageTableModel;

    public GreppageThread(GreppageTableModel model, File searchPath, Map optionMap) {
        super();
        this.mListeners = new ArrayList<ThreadListener>();
        this.mFilesQueue = new ArrayList<File>();
        this.mGreppageTableModel = model;
        this.mRootPath = searchPath;
        this.mIncludeSubDirectories = (Boolean) optionMap.get("includeSubDirectories"); // TODO Implement
        this.mUseRegularExpression = optionMap.containsKey("searchPattern");
        if(this.mUseRegularExpression) {
            this.mSearchPattern = (Pattern) optionMap.get("searchPattern");
        } else {
            this.mSearchString = (String) optionMap.get("searchString");
        }
    }

    public void addListener(ThreadListener tl) {
        this.mListeners.add(tl);
    }

    public void run() {
        this.queueFile(this.mRootPath, true);
        this.dequeue();
        for(ThreadListener tl: this.mListeners) {
            tl.complete();
        }
    }

    private void dequeue() {
        this.dequeue(this.mFilesQueue.size());
    }

    private void dequeue(int max) {
        for(int i = 0; i < max; i++) {
            for(ThreadListener tl: this.mListeners) {
                tl.setCurrentFile(this.mFilesQueue.get(0).getAbsolutePath());
            }
            this.getInstances(this.mFilesQueue.get(0));
            this.mFilesQueue.remove(0);
            for(ThreadListener tl: this.mListeners) {
                tl.incrementFilesSearched();
            }
        }
    }

    private void queueFile(File file, boolean searchSubDirectories) {
        int maxQueue = 100;
        if(this.mFilesQueue.size() > maxQueue) {
            this.dequeue(maxQueue);
        }
        if(file.isDirectory()) {
            if(searchSubDirectories) {
                for(File listedFile: file.listFiles()) {
                    this.queueFile(listedFile, this.mIncludeSubDirectories);
                }
            }
        } else {
            this.mFilesQueue.add(file);
            for(ThreadListener tl: this.mListeners) {
                tl.increaseFilesTotal(1);
            }
        }
    }

    private void getInstances(File file) {
        BufferedReader br = null;
        try {
            String currentLine;
            int lineNumber = 0;
            br = new BufferedReader(new FileReader(file));
            while((currentLine = br.readLine()) != null) {
                lineNumber++;
                if(this.mUseRegularExpression) {
                    if(this.mSearchPattern.matcher(currentLine).find()) {
                        String[] row = {String.valueOf(lineNumber), currentLine.trim(), file.getAbsolutePath()};
                        this.mGreppageTableModel.addRow(row);
                    }
                } else {
                    if(currentLine.contains(this.mSearchString)) {
                        String[] row = {String.valueOf(lineNumber), currentLine.trim(), file.getAbsolutePath()};
                        this.mGreppageTableModel.addRow(row);
                    }
                }
            }
        } catch(FileNotFoundException e) {
            //e.printStackTrace();
        } catch(IOException e) {
            //e.printStackTrace();
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
