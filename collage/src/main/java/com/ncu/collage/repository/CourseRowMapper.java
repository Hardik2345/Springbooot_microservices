package com.ncu.collage.repository;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.ncu.collage.model.Course;

public class CourseRowMapper implements RowMapper<Course> {
    @Override
    public Course mapRow(ResultSet rs, int rowNum) throws SQLException {
        if(rs==null){
            return null;
        }
        String courseId = rs.getString("c_id");
        String courseName = rs.getString("c_name");
        Integer credits = rs.getInt("c_credit");
        return new Course(courseId, courseName, credits);
    }
}
