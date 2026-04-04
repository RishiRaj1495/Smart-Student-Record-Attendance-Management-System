package com.vit.sms.model;

import java.io.Serializable;

/**
 * Student Model - Java Bean + Object Serialization (Unit 1 & 2)
 * Demonstrates: Encapsulation, Serialization, Java Bean conventions
 *
 * Project  : Smart Student Record & Attendance Management System
 * Institute: VIT Bhopal University
 * Course   : CSE4019 - Advanced Java Programming
 */
public class Student implements Serializable {

    private static final long serialVersionUID = 1001L;

    private int    id;
    private String regNo;
    private String name;
    private String email;
    private String phone;
    private String branch;
    private int    semester;
    private double cgpa;
    private String gender;
    private String status;

    public Student() {
        this.status = "ACTIVE";
        this.cgpa   = 0.0;
    }

    public Student(String regNo, String name, String email, String phone,
                   String branch, int semester, double cgpa, String gender) {
        this.regNo    = regNo;
        this.name     = name;
        this.email    = email;
        this.phone    = phone;
        this.branch   = branch;
        this.semester = semester;
        this.cgpa     = cgpa;
        this.gender   = gender;
        this.status   = "ACTIVE";
    }

    public int    getId()                    { return id; }
    public void   setId(int id)              { this.id = id; }
    public String getRegNo()                 { return regNo; }
    public void   setRegNo(String r)         { this.regNo = r; }
    public String getName()                  { return name; }
    public void   setName(String n)          { this.name = n; }
    public String getEmail()                 { return email; }
    public void   setEmail(String e)         { this.email = e; }
    public String getPhone()                 { return phone; }
    public void   setPhone(String p)         { this.phone = p; }
    public String getBranch()                { return branch; }
    public void   setBranch(String b)        { this.branch = b; }
    public int    getSemester()              { return semester; }
    public void   setSemester(int s)         { this.semester = s; }
    public double getCgpa()                  { return cgpa; }
    public void   setCgpa(double c)          { this.cgpa = c; }
    public String getGender()                { return gender; }
    public void   setGender(String g)        { this.gender = g; }
    public String getStatus()                { return status; }
    public void   setStatus(String s)        { this.status = s; }

    @Override
    public String toString() {
        return "Student{regNo='" + regNo + "', name='" + name + "'}";
    }
}
