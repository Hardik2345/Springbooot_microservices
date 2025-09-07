package com.ncu.collage.irepository;

import java.util.List;
import com.ncu.collage.model.Course;
public interface ICourseRepository {
    public List<Course> GetAllCourses();
    Course GetCoursebyId(String courseId);
    String GetCourseNameById(String courseId);
    Course AddCourse(Course course);
}
