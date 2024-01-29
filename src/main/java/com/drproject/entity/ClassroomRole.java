package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.Base64;
import java.util.UUID;

@Entity
@Table(name="classroomRoles")
public class ClassroomRole {
    @Id
    @Column(name = "UUID")
    private String id;

    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
    }

    @Column(name="roleInClassroom")
    private RoleInClassroom roleInClassroom;
    @ManyToOne
    @JoinColumn(name="classroom")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    public String getId() {
        return id;
    }


    public RoleInClassroom getRoleInClassroom() {
        return roleInClassroom;
    }

    public void setRoleInClassroom(RoleInClassroom roleInClassroom) {
        this.roleInClassroom = roleInClassroom;
    }

    public Classroom getClassroom() {
        return classroom;
    }

    public void setClassroom(Classroom classroom) {
        this.classroom = classroom;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String toBase64(String input) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] md5Bytes = md.digest(input.getBytes());
            return Base64.getEncoder().encodeToString(md5Bytes);
        }
        catch (Exception e) {
            return null;
        }
    }
}