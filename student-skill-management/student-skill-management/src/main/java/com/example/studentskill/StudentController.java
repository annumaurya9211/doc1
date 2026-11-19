
package com.example.studentskill;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentDAO dao = new StudentDAO();

    // 1. Add Student
    @PostMapping
    public ResponseEntity<String> addStudent(
            @RequestBody Student student) throws SQLException {

        dao.addStudent(student);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Student added successfully");
    }

    // 2. Get All Students
    @GetMapping
    public List<Student> getAllStudents() throws SQLException {
        return dao.getAllStudents();
    }

    // 3. Search Student by ID
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(
            @PathVariable int id) throws SQLException {

        Student student = dao.searchById(id);

        if (student == null) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(student);
    }

    // 4. Update Scores and Skills
    // 4. Update Scores and Skills
    @PutMapping("/{id}")
    public ResponseEntity<String> updateStudent(
            @PathVariable int id,
            @RequestBody Student student) throws SQLException {

        Student existing = dao.searchById(id);

        if (existing == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student ID not found: " + id);
        }

        dao.updateStudent(
                id,
                student.getProgrammingScore(),
                student.getMathematicsScore(),
                student.getTechnicalSkills()
        );

        return ResponseEntity.ok("Student updated successfully");
    }

    // 5. Delete Student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(
            @PathVariable int id) throws SQLException {

        dao.deleteStudent(id);

        return ResponseEntity.ok("Student deleted successfully");
    }

    // 6. Merit List
    @GetMapping("/merit-list")
    public List<Student> getMeritList() throws SQLException {
        return dao.getMeritList();
    }
}
