package com.danielyagmin.greppage;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

import java.lang.Thread;

import java.util.regex.Pattern;

import com.danielyagmin.greppage.model.ResultTableModel;

public class SearchThread extends Thread {

    private File mRootDirectory;
    private Pattern mSearchPattern;
    private boolean mIncludeSubDirectories;
    private SearchThreadListener mListener;
    private ResultTableModel mResultTableModel;
    private String[] mFileExtensionsAllowed;

    public SearchThread(Options options) {
        super();
        mRootDirectory = options.rootDirectory;
        mSearchPattern = options.searchPattern;
        mIncludeSubDirectories = options.includeSubDirectories;
        mFileExtensionsAllowed = options.fileExtensionsAllowed;
    }

    public void addListener(SearchThreadListener listener) {
        mListener = listener;
    }

    public void run() {
        grep(mRootDirectory, true);
        mListener.complete();
    }

    public void setModel(ResultTableModel model) {
        mResultTableModel = model;
    }

    private String getExtension(String fileName) {
        String ext = "";
        int i = fileName.lastIndexOf(".");
        if(i >= 0) {
            ext = fileName.substring(i + 1);
        }
        return ext;
    }

    private boolean shouldSearchFile(File file) {
        boolean should = true;
        if(mFileExtensionsAllowed != null) {
            String ext = getExtension(file.getAbsolutePath());
            should = false;
            for(String validExt: mFileExtensionsAllowed) {
                if(validExt.equalsIgnoreCase(ext)) {
                    should = true;
                    break;
                }
            }
        }
        return should;
    }

    /**
     * @TODO Consider changing naming convention
     **/
    private void grep(File file, boolean searchSubDirectories) {
        if(file.isDirectory()) {
            if(searchSubDirectories) {
                for(File listedFile: file.listFiles()) {
                    grep(listedFile, mIncludeSubDirectories);
                }
            }
        } else if(shouldSearchFile(file)){
            getInstances(file);
        } else {
            mListener.setSkippedFile(file.toString());
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
                if(mSearchPattern.matcher(currentLine).find()) {
                    String[] row = {String.valueOf(lineNumber), currentLine.trim(), file.getAbsolutePath()};
                    mResultTableModel.addRow(row);
                }
            }
            mListener.setSearchingFile(file.toString());
        } catch(FileNotFoundException e) {
            mListener.setErroredOnFile(file.toString());
        } catch(IOException e) {
            mListener.setErroredOnFile(file.toString());
        } finally {
            try {
                if(br != null) {
                    br.close();
                }
            } catch(IOException e) {

            }
        }
    }

}
