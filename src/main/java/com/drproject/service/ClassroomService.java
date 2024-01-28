package com.drproject.service;

import com.drproject.entity.Classroom;
import com.drproject.entity.ClassroomRole;
import com.drproject.entity.RoleInClassroom;
import com.drproject.entity.User;
import com.drproject.repository.ClassroomRepository;
import com.drproject.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
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
        if(classroomRepository.existsByCode(UUID.fromString(code))){
            Classroom classroom = classroomRepository.getClassroomByCode(UUID.fromString(code));
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
        return res;
    }

    public HashMap<String, Object> isInClass(String username, String code){
        HashMap<String, Object> res = new HashMap<>();
        if(classroomRepository.existsByCode(UUID.fromString(code))){
            User user = userRepository.getUserByUsername(username);
            Classroom classroom = classroomRepository.getClassroomByCode(UUID.fromString(code));
            ArrayList<ClassroomRole> classroomRoles = classroom.getRoleInClassrooms();
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
}
