package com.example.studentskill;

import java.util.Arrays;

public class StudentTest {

    public static void main(String[] args) {

        // Creating Student object
        Student s1 = new Student(
                101,
                "Rahul Sharma",
                "rahul@gmail.com",
                "B.Tech CSE",
                88,
                92,
                Arrays.asList("Java", "Python", "SQL")
        );

        // Displaying student details
        System.out.println("Student Profile");
        System.out.println("-------------------------");
        System.out.println(s1);

        // Accessing data using getters
        System.out.println("\nStudent Name: " + s1.getStudentName());
        System.out.println("Average Score: " +
                (s1.getProgrammingScore() + s1.getMathematicsScore()) / 2);
    }
}
