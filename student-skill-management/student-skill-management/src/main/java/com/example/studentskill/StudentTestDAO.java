
package com.example.studentskill;

import java.util.Arrays;

public class StudentTestDAO {

    public static void main(String[] args) {

        StudentDAO dao = new StudentDAO();

        try {
            // Insert sample students
            dao.addStudent(new Student(101, "Rahul Sharma",
                    "rahul@gmail.com", "B.Tech CSE",
                    88, 92, Arrays.asList("Java", "Python", "SQL")));

            dao.addStudent(new Student(102, "Priya Verma",
                    "priya@gmail.com", "B.Tech CSE",
                    45, 85, Arrays.asList("Java", "Python", "SQL")));

            dao.addStudent(new Student(103, "Aman Gupta",
                    "aman@gmail.com", "B.Tech IT",
                    75, 65, Arrays.asList("C++", "Python")));

            dao.addStudent(new Student(104, "Neha Singh",
                    "neha@gmail.com", "B.Tech IT",
                    95, 90, Arrays.asList("Java", "SQL")));

            // Display all students
            System.out.println("ALL STUDENTS:");
            dao.getAllStudents().forEach(System.out::println);

            // Average and performance category
            System.out.println("\nPERFORMANCE:");
            for (Student s : dao.getAllStudents()) {
                System.out.println(s.getStudentName()
                        + " Average: " + dao.calculateAverage(s)
                        + " Category: " + dao.getPerformanceCategory(s)
                        + " Stronger Subject: " + dao.getStrongerSubject(s));
            }

            // Merit List
            System.out.println("\nMERIT LIST:");
            dao.getMeritList().forEach(System.out::println);

            // Same technical skills
            System.out.println("\nSAME TECHNICAL SKILLS:");
            System.out.println(dao.studentsWithSameSkills());

            // Programming improvement
            System.out.println("\nPROGRAMMING NEEDS IMPROVEMENT:");
            dao.getStudentsNeedingProgrammingImprovement()
                    .forEach(System.out::println);

            // Search by ID
            System.out.println("\nSEARCH BY ID:");
            System.out.println(dao.searchById(101));

            // Search by Email
            System.out.println("\nSEARCH BY EMAIL:");
            System.out.println(dao.searchByEmail("rahul@gmail.com"));

            // Search by Course
            System.out.println("\nSEARCH BY COURSE:");
            System.out.println(dao.searchByCourse("B.Tech CSE"));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
