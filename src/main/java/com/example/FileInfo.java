package com.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

public class FileInfo {
    public enum FileType{
        FILE("FILE"), DIRECTORY("DIR");
private String name;

        FileType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;

        }



        public void setName(String name) {
            this.name = name;
        }
    }

    private String fileName;
    private long size;
    private FileType type;
    private LocalDateTime lastModified;

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public void setSize(long size) {
        this.size = size;
    }

    public void setType(FileType type) {
        this.type = type;
    }

    public void setLastModified(LocalDateTime lastModified) {
        this.lastModified = lastModified;
    }

    public String getFileName() {
        return fileName;
    }

    public long getSize() {
        return size;
    }

    public FileType getType() {
        return type;
    }

    public LocalDateTime getLastModified() {
        return lastModified;
    }

    public FileInfo(Path path) {
        try {
            this.fileName = path.getFileName().toString();
            this.size = Files.size(path);
            this.type = Files.isDirectory(path) ? FileType.DIRECTORY : FileType.FILE;
            if (this.type == FileType.DIRECTORY){
                this.size = -1L;
            }
            this.lastModified = LocalDateTime.ofInstant(Files.getLastModifiedTime(path).toInstant(),
                    ZoneOffset.ofHours(3));
        } catch (IOException e) {
            throw new RuntimeException("Unable create file info from path");
        }
    }
}
