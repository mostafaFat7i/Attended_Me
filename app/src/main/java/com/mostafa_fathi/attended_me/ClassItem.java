package com.mostafa_fathi.attended_me;

public class ClassItem {
     private String ClassName,SubjectName;
     private long cid;

    public ClassItem() {
    }

    public ClassItem( long cid, String className, String subjectName) {
        this.cid = cid;
        ClassName = className;
        SubjectName = subjectName;
    }

    public ClassItem(String className, String subjectName) {
        ClassName = className;
        SubjectName = subjectName;
    }

    public String getClassName() {
        return ClassName;
    }

    public void setClassName(String className) {
        ClassName = className;
    }

    public String getSubjectName() {
        return SubjectName;
    }

    public void setSubjectName(String subjectName) {
        SubjectName = subjectName;
    }

    public long getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }
}
