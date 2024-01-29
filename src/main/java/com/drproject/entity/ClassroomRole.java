package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name="classroomRoles")
public class ClassroomRole {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "UUID", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @Column(name="roleInClassroom")
    private RoleInClassroom roleInClassroom;
    @ManyToOne
    @JoinColumn(name="classroom")
    private Classroom classroom;

    @ManyToOne
    @JoinColumn(name="user")
    private User user;

    public UUID getId() {
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
}