package com.example.studentskill;

import java.sql.*;
import java.util.*;

public class StudentDAO {

    // 1. Add Student
    public void addStudent(Student s) throws SQLException {

        if (s.getProgrammingScore() < 0 || s.getProgrammingScore() > 100 ||
                s.getMathematicsScore() < 0 || s.getMathematicsScore() > 100) {
            throw new IllegalArgumentException("Scores must be between 0 and 100");
        }

        String sql = "INSERT INTO students VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, s.getStudentId());
            ps.setString(2, s.getStudentName());
            ps.setString(3, s.getEmail());
            ps.setString(4, s.getCourseName());
            ps.setDouble(5, s.getProgrammingScore());
            ps.setDouble(6, s.getMathematicsScore());

            ps.executeUpdate();

            String skillSql =
                    "INSERT INTO student_skills(student_id, skill_name) VALUES (?, ?)";

            try (PreparedStatement skillPs = con.prepareStatement(skillSql)) {
                for (String skill : s.getTechnicalSkills()) {
                    skillPs.setInt(1, s.getStudentId());
                    skillPs.setString(2, skill);
                    skillPs.addBatch();
                }
                skillPs.executeBatch();
            }
        }
    }

    // Helper: Get technical skills
    private List<String> getSkills(Connection con, int id) throws SQLException {

        List<String> skills = new ArrayList<>();
        String sql = "SELECT skill_name FROM student_skills WHERE student_id=?";

        try (PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    skills.add(rs.getString("skill_name"));
                }
            }
        }
        return skills;
    }

    // Helper: Convert ResultSet into Student object
    private Student mapStudent(ResultSet rs, Connection con) throws SQLException {

        int id = rs.getInt("student_id");

        return new Student(
                id,
                rs.getString("student_name"),
                rs.getString("email"),
                rs.getString("course_name"),
                rs.getDouble("programming_score"),
                rs.getDouble("mathematics_score"),
                getSkills(con, id)
        );
    }

    // 2. Retrieve All Students
    public List<Student> getAllStudents() throws SQLException {

        List<Student> students = new ArrayList<>();
        String sql = "SELECT * FROM students";

        try (Connection con = DBConnection.getConnection();
             Statement st = con.createStatement();
             ResultSet rs = st.executeQuery(sql)) {

            while (rs.next()) {
                students.add(mapStudent(rs, con));
            }
        }
        return students;
    }

    // 3. Search by ID
    public Student searchById(int id) throws SQLException {

        String sql = "SELECT * FROM students WHERE student_id=?";

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapStudent(rs, con);
                }
            }
        }
        return null;
    }

    // 4. Search by Email
    public List<Student> searchByEmail(String email) throws SQLException {

        List<Student> result = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM students WHERE email=?")) {

            ps.setString(1, email);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapStudent(rs, con));
                }
            }
        }
        return result;
    }

    // 5. Search by Course
    public List<Student> searchByCourse(String course) throws SQLException {

        List<Student> result = new ArrayList<>();

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "SELECT * FROM students WHERE course_name=?")) {

            ps.setString(1, course);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    result.add(mapStudent(rs, con));
                }
            }
        }
        return result;
    }

    // 6. Update Scores and Skills
    public void updateStudent(int id, double programming,
                              double mathematics,
                              List<String> skills) throws SQLException {

        if (programming < 0 || programming > 100 ||
                mathematics < 0 || mathematics > 100) {
            throw new IllegalArgumentException("Scores must be between 0 and 100");
        }

        try (Connection con = DBConnection.getConnection()) {

            con.setAutoCommit(false);

            try {
                String sql = "UPDATE students SET programming_score=?, " +
                        "mathematics_score=? WHERE student_id=?";

                try (PreparedStatement ps = con.prepareStatement(sql)) {
                    ps.setDouble(1, programming);
                    ps.setDouble(2, mathematics);
                    ps.setInt(3, id);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "DELETE FROM student_skills WHERE student_id=?")) {
                    ps.setInt(1, id);
                    ps.executeUpdate();
                }

                try (PreparedStatement ps = con.prepareStatement(
                        "INSERT INTO student_skills(student_id, skill_name) VALUES (?, ?)")) {
                    for (String skill : skills) {
                        ps.setInt(1, id);
                        ps.setString(2, skill);
                        ps.addBatch();
                    }
                    ps.executeBatch();
                }

                con.commit();

            } catch (SQLException e) {
                con.rollback();
                throw e;
            }
        }
    }

    // 7. Delete Student
    public void deleteStudent(int id) throws SQLException {

        try (Connection con = DBConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(
                     "DELETE FROM students WHERE student_id=?")) {

            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    // 8. Calculate Average
    public double calculateAverage(Student s) {
        return (s.getProgrammingScore() +
                s.getMathematicsScore()) / 2.0;
    }

    // 9. Performance Category
    public String getPerformanceCategory(Student s) {

        double avg = calculateAverage(s);

        if (avg >= 85) return "Excellent";
        else if (avg >= 70) return "Good";
        else if (avg >= 50) return "Satisfactory";
        else return "Needs Improvement";
    }

    // 10. Stronger Subject
    public String getStrongerSubject(Student s) {

        if (s.getProgrammingScore() > s.getMathematicsScore())
            return "Programming";
        else if (s.getMathematicsScore() > s.getProgrammingScore())
            return "Mathematics";
        else
            return "Both Equal";
    }

    // 11. Merit List (Descending Order)
    public List<Student> getMeritList() throws SQLException {

        List<Student> students = getAllStudents();

        students.sort((a, b) ->
                Double.compare(calculateAverage(b), calculateAverage(a)));

        return students;
    }

    // 12. Students having same technical skills
    public Map<Set<String>, List<Integer>> studentsWithSameSkills()
            throws SQLException {

        Map<Set<String>, List<Integer>> skillMap = new HashMap<>();

        for (Student s : getAllStudents()) {

            Set<String> skills = new TreeSet<>(s.getTechnicalSkills());

            skillMap.computeIfAbsent(skills, k -> new ArrayList<>())
                    .add(s.getStudentId());
        }

        skillMap.entrySet().removeIf(
                entry -> entry.getValue().size() < 2);

        return skillMap;
    }

    // 13. Programming Needs Improvement and Maths >= 75
    public List<Student> getStudentsNeedingProgrammingImprovement()
            throws SQLException {

        List<Student> result = new ArrayList<>();

        for (Student s : getAllStudents()) {
            if (s.getProgrammingScore() < 50 &&
                    s.getMathematicsScore() >= 75) {
                result.add(s);
            }
        }

        return result;
    }
}
