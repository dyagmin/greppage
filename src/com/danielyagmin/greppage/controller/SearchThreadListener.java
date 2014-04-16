package com.danielyagmin.greppage.controller;

public interface SearchThreadListener {
    
    public void setCurrentFile(String currentfile);

    public void incrementFilesSearched();

    public void increaseFilesTotal(int i);

    public void complete();

}
