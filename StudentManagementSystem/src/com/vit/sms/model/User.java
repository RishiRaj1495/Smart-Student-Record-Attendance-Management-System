package com.vit.sms.model;

import java.io.Serializable;

/**
 * User Model - Authentication (Unit 1 & 2)
 */
public class User implements Serializable {

    private static final long serialVersionUID = 1002L;

    private int     id;
    private String  username;
    private String  password;
    private String  role;      // ADMIN, FACULTY
    private String  fullName;
    private String  email;
    private boolean active;

    public User() { this.active = true; }

    public User(String username, String password, String role, String fullName, String email) {
        this.username = username;
        this.password = password;
        this.role     = role;
        this.fullName = fullName;
        this.email    = email;
        this.active   = true;
    }

    public int     getId()                    { return id; }
    public void    setId(int id)              { this.id = id; }
    public String  getUsername()              { return username; }
    public void    setUsername(String u)      { this.username = u; }
    public String  getPassword()              { return password; }
    public void    setPassword(String p)      { this.password = p; }
    public String  getRole()                  { return role; }
    public void    setRole(String r)          { this.role = r; }
    public String  getFullName()              { return fullName; }
    public void    setFullName(String n)      { this.fullName = n; }
    public String  getEmail()                 { return email; }
    public void    setEmail(String e)         { this.email = e; }
    public boolean isActive()                 { return active; }
    public void    setActive(boolean a)       { this.active = a; }
}
