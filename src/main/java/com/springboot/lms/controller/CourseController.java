package com.springboot.lms.controller;

import java.security.Principal;
import java.util.List;

import com.springboot.lms.dto.CourseDetailsDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.springboot.lms.model.Course;
import com.springboot.lms.service.CourseService;

@RestController
@RequestMapping("/api/course")
@CrossOrigin("http://localhost:5173")
public class CourseController {

	@Autowired
	private CourseService courseService;
	Logger logger = LoggerFactory.getLogger("CourseController");
	/*
	 * AIM: Add course to DB <-- Should be done only by AUTHOR
	 * PATH: /api/course/add
	 * METHOD: POST
	 * Response: Course
	 * Input: Course <-- request body
	 * Access: AUTHOR or EXECUTIVE
	 */
	@PostMapping("/add") // <-- /api/course/add
	public Course postCourse(Principal principal, @RequestBody Course course) {
		String username = principal.getName(); // LoggedIn author
		return courseService.postCourse(course, username);
	}

	@GetMapping("/all")
	public List<Course> getAllCourses(
			@RequestParam(name = "page", required = false, defaultValue = "0") Integer page,
			@RequestParam(name = "size", required = false, defaultValue = "1000000") Integer size) {
		if (page == 0 && size == 1000000)
			logger.info("No Pagination call for all courses");
		return courseService.getAllCourses(page, size);
	}
	@GetMapping("/by-author")
	public List<Course> getCoursesByAuthor(Principal principal) {
		String username = principal.getName(); // logged in Author
		List<Course> courses = courseService.getCoursesByAuthor(username);
		return courses;
	}

	@GetMapping("/details/{id}")
	public ResponseEntity<CourseDetailsDTO> getCourseDetails(@PathVariable int id) {
		CourseDetailsDTO dto = courseService.getCourseDetails(id);
		return ResponseEntity.ok(dto);
	}
}