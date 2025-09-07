package com.ncu.collage.dto;

public class CourseDto {
    String _CourseName;
    int _Credit;

    public CourseDto(){

    }
    public CourseDto(String courseName, int credit) {
        this._CourseName = courseName;
        this._Credit = credit;
    }

    public String get_CourseName() {
        return _CourseName;
    }

    public int get_Credit() {
        return _Credit;
    }

    public void set_CourseName(String _CourseName) {
        this._CourseName = _CourseName;
    }

    public void set_Credits(int credits)
    {
        this._Credit=credits;
    }
}
