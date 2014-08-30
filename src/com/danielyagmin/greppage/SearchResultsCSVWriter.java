package com.danielyagmin.greppage;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;

import java.lang.StringBuilder;

import com.danielyagmin.greppage.model.ResultTableModel;

class SearchResultsCSVWriter {

    File mOutputFile;

    public SearchResultsCSVWriter(File outputFile) {
        mOutputFile = outputFile;
    }

    public void write(ResultTableModel model) throws IOException {
        Writer writer = null;
        try {
            writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(mOutputFile)));
            for(int i = 0; i < model.getRowCount(); i++) {
                StringBuilder sb = new StringBuilder();
                if(i > 0) {
                     sb.append("\n");
                }
                for(int j = 0; j < model.getColumnCount(); j++) {
                    if(j > 0) {
                        sb.append(",");
                    }
                    String s = (String) model.getValueAt(i, j);
                    if(s.contains(",") || s.contains("\"")) {
                        s = String.format("\"%s\"", s);
                    }
                    sb.append(s);
                }
                writer.write(sb.toString());
            }
        }
        catch(IOException e) {
            throw new IOException("Could not write CSV file.");
        }
        finally {
            try {
                writer.close();
            } catch(Exception e) {
                // Nothing
            }
        }
    }

}