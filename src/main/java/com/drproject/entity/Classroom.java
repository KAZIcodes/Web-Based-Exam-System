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

    @OneToMany(mappedBy = "classrooms", cascade = CascadeType.ALL)
    private ArrayList<Section> sections;

    @OneToMany(mappedBy = "classrooms", cascade = CascadeType.ALL)
    private ArrayList<Glossary> glossaries;

    @Column(name="code", columnDefinition = "VARCHAR(36)")
    @GeneratedValue(generator = "uuid4")
    @GenericGenerator(name = "uuid4", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID code;

    @Column(name = "name")
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="startDate")
    private String startDate;
    @Column(name="endDate")
    private String endDate;


    public ArrayList<ClassroomRole> getRoleInClassrooms() {
        return RoleInClassrooms;
    }

    public void setRoleInClassrooms(ArrayList<ClassroomRole> roleInClassrooms) {
        RoleInClassrooms = roleInClassrooms;
    }

    public UUID getCode() {
        return code;
    }

    public void setCode(UUID code) {
        this.code = code;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public ArrayList<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }
}
