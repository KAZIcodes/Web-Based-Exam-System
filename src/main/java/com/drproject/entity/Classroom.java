package com.drproject.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import javax.management.relation.Role;
import java.security.MessageDigest;
import java.util.*;

@Entity
@Table(name="classrooms")
public class Classroom {
    @Id
    @Column(name = "UUID") // database id
    private String id;

    @Column(name="code") // classroom code that is used to join
    private String code;

    @PrePersist
    public void prePersist(){
        this.id = toBase64(UUID.randomUUID().toString());
        this.code = toBase64(UUID.randomUUID().toString());
    }


    @Column(name = "RoleInClassrooms")
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private List<ClassroomRole> RoleInClassrooms;

    @Column(name = "sections")
    @OneToMany(mappedBy = "classroom", cascade = CascadeType.ALL)
    private List<Section> sections;

    @Column(name = "glossaryEntries")
    @OneToMany(mappedBy = "tUUID", cascade = CascadeType.ALL)
    private List<GlossaryEntry> glossaryEntries;



    @Column(name = "name")
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="startDate")
    private String startDate;
    @Column(name="endDate")
    private String endDate;


    public List<ClassroomRole> getRoleInClassrooms() {
        return RoleInClassrooms;
    }

    public void setRoleInClassrooms(ArrayList<ClassroomRole> roleInClassrooms) {
        RoleInClassrooms = roleInClassrooms;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
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

    public List<Section> getSections() {
        return sections;
    }

    public void setSections(ArrayList<Section> sections) {
        this.sections = sections;
    }

    public void setRoleInClassrooms(List<ClassroomRole> roleInClassrooms) {
        RoleInClassrooms = roleInClassrooms;
    }

    public void setSections(List<Section> sections) {
        this.sections = sections;
    }

    public List<GlossaryEntry> getGlossaryEntries() {
        return glossaryEntries;
    }

    public void setGlossaryEntries(List<GlossaryEntry> glossaryEntries) {
        this.glossaryEntries = glossaryEntries;
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
