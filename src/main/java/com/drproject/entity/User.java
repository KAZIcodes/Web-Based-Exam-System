package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.Array;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "UUID", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name="firstName")
    String firstName;
    @Column(name="LastName")
    String lastName;
    @Column(name="username")
    String username;
    @Column(name="email")
    String email;
    @Column(name="password")
    String password;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL)
    private ArrayList<ClassroomRole> roleInClassrooms;

    // Constructors


    // Getters for the fields

    public UUID getId() {
        return id;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<ClassroomRole> getRoleInClassrooms() {
        return roleInClassrooms;
    }

    public void setRoleInClassrooms(ArrayList<ClassroomRole> roleInClassrooms) {
        this.roleInClassrooms = roleInClassrooms;
    }
}
