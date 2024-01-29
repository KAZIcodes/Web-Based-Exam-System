package com.drproject.service;

import com.drproject.entity.*;
import com.drproject.repository.ClassroomRepository;
import com.drproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

@Service
public class ClassroomService {
    private final ClassroomRepository classroomRepository;
    private final UserRepository userRepository;
    public ClassroomService(ClassroomRepository classroomRepository, UserRepository userRepository) {
        this.classroomRepository = classroomRepository;
        this.userRepository = userRepository;
    }

    public HashMap<String, Object> joinClassroom(String code, String username){
        HashMap<String, Object> res = new HashMap<>();
        if(classroomRepository.existsByCode(code)){
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            User user = userRepository.getUserByUsername(username);
            ClassroomRole classroomRole = new ClassroomRole();
            classroomRole.setClassroom(classroom);
            classroomRole.setUser(user);
            classroomRole.setRoleInClassroom(RoleInClassroom.STUDENT);
            classroom.getRoleInClassrooms().add(classroomRole);
            classroomRepository.save(classroom);
            user.getRoleInClassrooms().add(classroomRole);
            userRepository.save(user);
            res.put("msg","successfully joined classroom");
            res.put("status", true);
            return res;
        }
        res.put("msg","failed to join classroom");
        res.put("status", false);
        return res;
    }
    public HashMap<String, Object> addClassroom(String username, Classroom classroom){
        HashMap<String, Object> res = new HashMap<>();
        ArrayList<ClassroomRole> classroomRoles = new ArrayList<>();
        ClassroomRole classroomRole = new ClassroomRole();
        classroomRole.setRoleInClassroom(RoleInClassroom.ADMIN);
        classroomRole.setUser(userRepository.getUserByUsername(username));
        classroomRole.setClassroom(classroom);
        classroomRoles.add(classroomRole);
        classroom.setRoleInClassrooms(classroomRoles);
        classroomRepository.save(classroom);
        res.put("status",true);
        res.put("msg", "successfully created new classroom. user = classroom admin.");
        res.put("obj", null);
        return res;
    }

    public HashMap<String, Object> isInClass(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        if(classroomRepository.existsByCode(code)){
            User user = userRepository.getUserByUsername(username);
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            List<ClassroomRole> classroomRoles = classroom.getRoleInClassrooms();
            for (ClassroomRole c : classroomRoles){
                if (c.getUser().equals(user)){
                    res.put("msg","user found in classroom");
                    res.put("status", true);
                    res.put("obj", classroom.getSections());
                    return res;
                }
            }
        }
        res.put("status", false);
        res.put("msg", "user not in classroom or classroom does not exist");
        res.put("obj", null);
        return res;
    }

    public HashMap<String, Object> getUserRole(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        User user = userRepository.getUserByUsername(username);
        if(classroomRepository.existsByCode(code)) {
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            for (ClassroomRole c : classroom.getRoleInClassrooms()){
                if(c.getUser().equals(user)){
                    if(c.getRoleInClassroom().equals(RoleInClassroom.ADMIN)){
                        res.put("obj", "admin");
                        res.put("status", true);
                        res.put("msg", "role found");
                        return res;
                    }
                    if(c.getRoleInClassroom().equals(RoleInClassroom.TEACHER)){
                        res.put("obj", "teacher");
                        res.put("status", true);
                        res.put("msg", "role found");
                        return res;
                    }
                    if(c.getRoleInClassroom().equals(RoleInClassroom.STUDENT)){
                        res.put("obj", "student");
                        res.put("status", true);
                        res.put("msg", "role found");
                        return res;
                    }
                }
            }
        }
        res.put("obj", null);
        res.put("status", false);
        res.put("msg", "role/classroom not found");
        return res;
    }

    public HashMap<String , Object> getClassroomGlossary(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        List<HashMap<String, String>> output =new ArrayList<>();
        if(classroomRepository.existsByCode(code)) {
            Classroom classroom = classroomRepository.getClassroomByCode(code);
            List<GlossaryEntry> glossaryEntries = classroom.getGlossaryEntries();
            for(GlossaryEntry g : glossaryEntries){
                HashMap<String , String> entry = new HashMap<>();
                entry.put("value", g.getGlossaryValue());
                entry.put("key", g.getGlossaryKey());
                output.add(entry);
            }
            res.put("status", true);
            res.put("obj", output);
            res.put("msg", "successfully returned glossary");
            return res;
        }
        res.put("status", false);
        res.put("obj", output);
        res.put("msg", "failed to fetch glossary. Classroom may not exist");
        return res;
    }

    public HashMap<String, Object> updateClassroomGlossary(String classroomCode, List<HashMap<String,String>> newGlossaryList){
        HashMap<String, Object> res = new HashMap<>();
        List<HashMap<String, String>> output =new ArrayList<>();
        if(classroomRepository.existsByCode(classroomCode)) {
            Classroom classroom = classroomRepository.getClassroomByCode(classroomCode);
            List<GlossaryEntry> newEntries = new ArrayList<>();
            for (HashMap<String, String> h : newGlossaryList) {
                GlossaryEntry glossaryEntry = new GlossaryEntry();
                glossaryEntry.setClassroom(classroom);
                glossaryEntry.setGlossaryKey(h.get("key"));
                glossaryEntry.setGlossaryValue(h.get("value"));
                newEntries.add(glossaryEntry);
                System.out.println("\n\n\n\n\n\n\n\n\n" + "new: "+glossaryEntry.getGlossaryKey()+" " + glossaryEntry.getGlossaryValue()+"\n\n\n\n\n\n\n\n");

            }
            classroom.setGlossaryEntries(newEntries);
            List<GlossaryEntry> e = classroom.getGlossaryEntries();
            for(GlossaryEntry glossaryEntry : e){
                System.out.println("\n\n\n\n\n\n\n\n\n"+"already in class"+glossaryEntry.getGlossaryKey()+" " + glossaryEntry.getGlossaryValue()+"\n\n\n\n\n\n\n\n");
            }
            classroomRepository.saveAndFlush(classroom);
        }
        res.put("status", true);
        res.put("obj", null);
        res.put("msg", "successfully changed glossary for classroom");
        return res;
    }


    // ArrayList<HashMap<String, String>>

    //
/*
    public HashMap<String, Object> getUserGrades(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        HashMap<String , String> entries = new HashMap<>();
        if(classroomRepository.existsByCode(code)) {

    }


 */


}
