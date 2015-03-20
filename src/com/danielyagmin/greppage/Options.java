package com.danielyagmin.greppage;

import java.io.File;
import java.util.regex.Pattern;

class Options {

    public File rootDirectory;
    public Pattern searchPattern;
    public boolean includeSubDirectories;
    public boolean caseInsensitive;
    public String[] fileExtensionsAllowed;

}
