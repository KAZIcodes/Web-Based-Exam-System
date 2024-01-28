package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import javax.management.relation.Role;
import java.util.ArrayList;
import java.util.UUID;

@Entity
@Table(name="classrooms")
public class Classroom {
    @Id
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "UUID", columnDefinition = "VARCHAR(36)")
    private UUID id;

    @OneToMany(mappedBy = "classrooms", cascade = CascadeType.ALL)
    private ArrayList<ClassroomRole> RoleInClassrooms;

    @Column(name="uuid4")
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID classId;

    public ArrayList<ClassroomRole> getRoleInClassrooms() {
        return RoleInClassrooms;
    }

    public void setRoleInClassrooms(ArrayList<ClassroomRole> roleInClassrooms) {
        RoleInClassrooms = roleInClassrooms;
    }

    public UUID getClassId() {
        return classId;
    }

    public void setClassId(UUID classId) {
        this.classId = classId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }
}
