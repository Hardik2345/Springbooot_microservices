package com.ncu.collage.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ncu.collage.irepository.ICourseRepository;
import com.ncu.collage.model.Course;
import com.ncu.collage.dto.CourseDto;
import java.util.*;
import org.modelmapper.ModelMapper;

@Service
public class CourseService {

    private final ICourseRepository  _CourseRepository;
    private final ModelMapper _ModelMapper;
    
    @Autowired
    public CourseService(ICourseRepository courseRepository, ModelMapper modelMapper){
        this._CourseRepository = courseRepository;
        this._ModelMapper = modelMapper;
    }

    public List<CourseDto> GetAllCourses(){
        List<Course> courses = _CourseRepository.GetAllCourses();
        if(courses == null || courses.isEmpty()){
            return Collections.emptyList();
        }
        List<CourseDto> courseDtos = new ArrayList<>(courses.size());
        for(Course c : courses){
            courseDtos.add(_ModelMapper.map(c, CourseDto.class));
        }
        return courseDtos;
    }

     public CourseDto GetCourseById(String courseId) {
        Course c=_CourseRepository.GetCoursebyId(courseId);
        CourseDto dto= _ModelMapper.map(c, CourseDto.class);
        return dto;
    }

    public String GetCourseNameById(String courseId){
        return _CourseRepository.GetCourseNameById(courseId);
    }

    public CourseDto AddCourse(CourseDto courseDto){
        Course course = _ModelMapper.map(courseDto, Course.class);
        _CourseRepository.AddCourse(course);
        return courseDto;
    }

}
