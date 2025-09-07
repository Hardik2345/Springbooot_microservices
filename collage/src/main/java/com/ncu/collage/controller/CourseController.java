package com.ncu.collage.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ncu.collage.dto.CourseDto;
import com.ncu.collage.service.CourseService;

@RequestMapping("/courses")
@RestController// app.post('/courses/course',()=>{})
public class CourseController {
    private final CourseService _CourseService;

    @Autowired
    CourseController(CourseService courseService){
        this._CourseService = courseService;
    }

    @GetMapping("/allcourses")
    public List<CourseDto> GetAllCourses(){
        return _CourseService.GetAllCourses();
    }

    @GetMapping("/course/courseId/{courseId}")
    public CourseDto GetCourseById(@PathVariable("courseId") String courseId){
        return _CourseService.GetCourseById(courseId);
    }

    @GetMapping("/course/coursename/{courseId}")
    public String GetCourseNameById(@PathVariable("courseId") String courseId){
        return _CourseService.GetCourseNameById(courseId);
    }

    @PostMapping("/course")
    public CourseDto AddCourse(CourseDto courseDto){
        return _CourseService.AddCourse(courseDto);
    }
}
