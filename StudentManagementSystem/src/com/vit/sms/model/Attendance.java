package com.vit.sms.model;

import java.io.Serializable;

/**
 * Attendance Model (Unit 1 & 3)
 */
public class Attendance implements Serializable {

    private static final long serialVersionUID = 1003L;

    private int    id;
    private int    studentId;
    private String studentName;
    private String regNo;
    private String subject;
    private String attendanceDate;
    private String status;   // PRESENT / ABSENT / LATE
    private String remarks;

    public Attendance() {}

    public Attendance(int studentId, String subject, String date, String status) {
        this.studentId      = studentId;
        this.subject        = subject;
        this.attendanceDate = date;
        this.status         = status;
    }

    public int    getId()                        { return id; }
    public void   setId(int id)                  { this.id = id; }
    public int    getStudentId()                 { return studentId; }
    public void   setStudentId(int s)            { this.studentId = s; }
    public String getStudentName()               { return studentName; }
    public void   setStudentName(String n)       { this.studentName = n; }
    public String getRegNo()                     { return regNo; }
    public void   setRegNo(String r)             { this.regNo = r; }
    public String getSubject()                   { return subject; }
    public void   setSubject(String s)           { this.subject = s; }
    public String getAttendanceDate()            { return attendanceDate; }
    public void   setAttendanceDate(String d)    { this.attendanceDate = d; }
    public String getStatus()                    { return status; }
    public void   setStatus(String s)            { this.status = s; }
    public String getRemarks()                   { return remarks; }
    public void   setRemarks(String r)           { this.remarks = r; }
}
