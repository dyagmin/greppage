package dyagmin.greppage;

public interface ThreadListener {
    
    public void setCurrentFile(String currentfile);

    public void incrementFilesSearched();

    public void increaseFilesTotal(int i);

    public void complete();

}
