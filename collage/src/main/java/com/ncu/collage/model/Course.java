package com.ncu.collage.model;

public class Course {
    String _CourseId;
    String _CourseName;
    Integer _Credit;

    public Course(){
        // Default constructor
    }
    public Course(String courseId, String courseName, Integer credit) {
        this._CourseId = courseId;
        this._CourseName = courseName;
        this._Credit = credit;
    }

    // Setters and getters 
     public String get_CourseId() {
        return _CourseId;    
     }
     public void set_CourseId(String _CourseId) {
        this._CourseId = _CourseId;
     }
     public String get_CourseName() {
        return _CourseName;
     }
     public void set_CourseName(String _CourseName) {
        this._CourseName = _CourseName;
     }
     public Integer get_Credit() {
        return _Credit;
     }
     public void set_Credit(Integer _Credit) {
        this._Credit = _Credit;
     }
}