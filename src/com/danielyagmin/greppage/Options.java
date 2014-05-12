package com.danielyagmin.greppage;

import java.io.File;
import java.io.FileNotFoundException;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import com.danielyagmin.greppage.view.DirectoryChooser;
import com.danielyagmin.greppage.view.NewSearchDialog;


class Options {

    private File mRootDirectory;
    private Pattern mSearchPattern;
    private boolean mIncludeSubDirectories = false;
    private boolean mCaseInsensitive = false;
    private String[] mFileExtensionsAllowed = null;

    public File getRootDirectory() {
        return mRootDirectory;
    }

    public Pattern getSearchPattern() {
        return mSearchPattern;
    }

    public boolean getIncludeSubDirectories() {
        return mIncludeSubDirectories;
    }

    public boolean getCaseInsensitive() {
        return mCaseInsensitive;
    }

    public String[] getFileExtensionsAllowed() {
        return mFileExtensionsAllowed;
    }

    public void setSearchPattern(String pattern) throws PatternSyntaxException {
        if(mCaseInsensitive) {
            mSearchPattern = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
        } else {
            mSearchPattern = Pattern.compile(pattern);
        }
    }

    public void setRootDirectory(String path) throws FileNotFoundException {
        if(path != null) {
            mRootDirectory = new File(path);
        }
    }

    public void setIncludeSubDirectories(boolean includeSubDirectories) {
        mIncludeSubDirectories = includeSubDirectories;
    }

    public void setCaseInsensitive(boolean caseInsensitive) {
        mCaseInsensitive = caseInsensitive;
    }

    public void setFileExtensionsAllowed(String fileExtensionsAllowed) {
        String extensions = fileExtensionsAllowed;
        mFileExtensionsAllowed = null;
        if(extensions.length() > 0) {
            mFileExtensionsAllowed = extensions.split("[\\s,]+");
        }
    }

    public boolean isValid() {
        return mRootDirectory != null && mSearchPattern != null;
    }

}
