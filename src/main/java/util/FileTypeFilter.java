package util;

// Source of this code: http://www.codejava.net/java-se/swing/file-picker-component-in-swing

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class FileTypeFilter extends FileFilter {

    private String[] extensions;
    private String description;

    public FileTypeFilter(String[] extensions, String description) {
        this.extensions = extensions;
        this.description = description;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        for(String extension : extensions) {
            if (file.getName().toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    public String getDescription() {
        return description;
    }
}
