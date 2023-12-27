package com.softitbd.diuquestionbank.Model;

public class DocumentModel {
    private int id;
    private String name;
    private String type;
    private String file;
    private String semester;
    private String department;
    private String year;
    private String updatedAt;

    public DocumentModel() {
    }
    public DocumentModel(int id, String name, String type, String file, String semester, String department, String year, String updatedAt) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.file = file;
        this.semester = semester;
        this.department = department;
        this.year = year;
        this.updatedAt = updatedAt;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public String getFile() {
        return file;
    }
    public void setFile(String file) {
        this.file = file;
    }
    public String getSemester() {
        return semester;
    }
    public void setSemester(String semester) {
        this.semester = semester;
    }
    public String getDepartment() {
        return department;
    }
    public void setDepartment(String department) {
        this.department = department;
    }
    public String getYear() {
        return year;
    }
    public void setYear(String year) {
        this.year = year;
    }
    public String getUpdatedAt() {
        return updatedAt;
    }
    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }
}

