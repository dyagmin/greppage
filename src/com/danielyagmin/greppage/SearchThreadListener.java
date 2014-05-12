package com.danielyagmin.greppage;

public interface SearchThreadListener {

    public void setSkippedFile(String currentfile);

    public void setSearchingFile(String currentFile);

    public void setErroredOnFile(String currentFile);

    public void complete();

}
