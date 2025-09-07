package com.ncu.collage.repository;

import java.util.List;
import java.util.Collections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import com.ncu.collage.irepository.ICourseRepository;
import com.ncu.collage.model.Course;

@Repository(value = "CourseRepository")
public class CourseRepository implements ICourseRepository {
    private static final Logger log = LoggerFactory.getLogger(CourseRepository.class);
    private final JdbcTemplate _JdbcTemplate;

    @Autowired
    CourseRepository(JdbcTemplate jdbcTemplate) {
        this._JdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Course> GetAllCourses(){
        String sql = "SELECT * FROM course";
        try{
            return _JdbcTemplate.query(sql, new CourseRowMapper());
        } catch(Exception e){
            log.error("Error fetching courses", e);
            return Collections.emptyList();
        }
    }

    @Override
    public Course GetCoursebyId(String courseId){
        String sql = "SELECT * FROM course WHERE c_id = ?";
        try{
            return _JdbcTemplate.queryForObject(sql, new CourseRowMapper(), courseId);
        } catch(Exception e){
            log.error("Error fetching course by ID: " + courseId, e);
            return null;
        }
    }

    @Override
    public String GetCourseNameById(String courseId){
        String sql="SELECT c_name FROM course WHERE c_id=?";
        try{
            String courseName= _JdbcTemplate.queryForObject(sql, String.class, courseId);
            return courseName;
        } catch(Exception e){
            log.error("Error fetching course name by ID: " + courseId, e);
            return null;
        }
    }

    @Override
    public Course AddCourse(Course course){
        int count=GetCourseCount();
        String courseId="c"+(count+100+1);
        course.set_CourseId(courseId);
        String sql = "INSERT INTO course (c_id, c_name, c_credit) VALUES (?, ?, ?)";
        try{
            _JdbcTemplate.update(sql, course.get_CourseId(), course.get_CourseName(), course.get_Credit());
            return course;
        } catch(Exception e){
            log.error("Error adding course", e);
            return null;
        }
    }

    int GetCourseCount(){
        String sql = "SELECT COUNT(*) FROM course";
        try{
            return _JdbcTemplate.queryForObject(sql, Integer.class);
        } catch(Exception e){
            log.error("Error fetching course count", e);
            return 0;
        }
    }
}
