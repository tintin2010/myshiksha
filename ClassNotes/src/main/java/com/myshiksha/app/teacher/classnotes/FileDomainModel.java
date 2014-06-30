package com.myshiksha.app.teacher.classnotes;

import com.myshiksha.app.teacher.classnotes.utils.FileExtensionType;

import java.io.File;

/**
 * Created by seinjutichatterjee on 5/23/14.
 */
public class FileDomainModel {
    private final File file;
    private boolean selected;

    public FileDomainModel(File file) {
        this.file = file;
        selected = false;
    }

    public String getName() {
        return file.getName();
    }

    public FileExtensionType getType() {
        return FileExtensionType.getType(file);
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public String getSize() {
        return new Long(file.length()).toString() + " bytes";
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("{name=")
          .append(getName())
          .append(" size=")
          .append(getSize())
          .append(" type=")
          .append(getType())
          .append("}");
        return sb.toString();
    }
}
