package com.danielyagmin.greppage.controller;

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

import com.danielyagmin.greppage.model.ResultTableModel;

public class SearchThread extends Thread {

    private File mRootPath;
    private String mSearchString;
    private Pattern mSearchPattern;
    private boolean mIncludeSubDirectories;
    private boolean mUseRegularExpression;
    private boolean mHasAllFiles;
    private SearchThreadListener mListener;
    private List<File> mFilesQueue;
    private ResultTableModel mResultTableModel;

    public SearchThread(SearchThreadListener listener, ResultTableModel model, File searchPath, Map optionMap) {
        super();
        this.mListener = listener;
        this.mFilesQueue = new ArrayList<File>();
        this.mResultTableModel = model;
        this.mRootPath = searchPath;
        this.mIncludeSubDirectories = (Boolean) optionMap.get("includeSubDirectories");
        this.mSearchPattern = (Pattern) optionMap.get("searchPattern");
    }

    public void run() {
        this.queueFile(this.mRootPath, true);
        this.dequeue();
        this.mListener.complete();
    }

    private void dequeue() {
        this.dequeue(this.mFilesQueue.size());
    }

    private void dequeue(int max) {
        for(int i = 0; i < max; i++) {
            this.mListener.setCurrentFile(this.mFilesQueue.get(0).getAbsolutePath());
            this.getInstances(this.mFilesQueue.get(0));
            this.mFilesQueue.remove(0);
            this.mListener.incrementFilesSearched();
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
            this.mListener.increaseFilesTotal(1);
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
                if(this.mSearchPattern.matcher(currentLine).find()) {
                    String[] row = {String.valueOf(lineNumber), currentLine.trim(), file.getAbsolutePath()};
                    this.mResultTableModel.addRow(row);
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
