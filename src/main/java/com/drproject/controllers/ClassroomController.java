package com.drproject.controllers;

import com.drproject.service.ARservice;
import com.drproject.service.ClassroomService;
import com.drproject.entity.Classroom;
import jakarta.servlet.http.HttpSession;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/classrooms")
public class ClassroomController {
    private final ClassroomService classromService;
    private final ARservice ARserivce;

    public ClassroomController(ARservice ARserivce, ClassroomService classromService) {   /////user service class needed
        this.ARserivce = ARserivce;
        this.classromService = classromService;
    }

    //////isInClass method needed in classRoom service which if the user has access to the class then returns a MAP(json) of classroom data else null
    //returns classroom Data (not needed classroom data is the same as sections)
    /*@GetMapping("/{classroomId}")
    public ResponseEntity<?> signUpUser(@PathVariable String classroomId, HttpSession session) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        Map<String, Object> res = classromService.isInClass(session.getAttribute("username"), classroomId);
        if (res != null){
            return ResponseEntity.ok(res);
        }else {
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "You don't have access to this class or it does not exist!");
            return ResponseEntity.ok(error);
        }
    }*/

    @PostMapping("/join")
    public ResponseEntity<?> joinClassroom(@RequestBody Map<String, Object> obj, HttpSession session) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }
        //joinclassroom method which takes code off the class and adds the user to classroom
        Map<String, Object> res = classromService.joinClassroom((String) obj.get("linkCode"), (String) session.getAttribute("username"));
        return ResponseEntity.ok(res);
    }

    //adds a classroom
    @PostMapping("/add")
    public ResponseEntity<?> addClassroom(@RequestBody Classroom classroomInfo, HttpSession session) {  ///////Classroom entity needed
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }
        ////////addClass method which returns status and msg
        Map<String, Object> res = classromService.addClassroom((String) session.getAttribute("username"), classroomInfo);
        return ResponseEntity.ok(res);
    }

    //returns sections of a classroom
    @GetMapping("/{classroomId}/sections")
    public ResponseEntity<?> getClassroomSections(HttpSession session, @PathVariable String classroomId) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        //////isInClass method needed in classRoom service which if the user has access to the class then returns a MAP(json) of classroom data else nul
        Map<String, Object> res = classromService.isInClass((String) session.getAttribute("username"), classroomId);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{classroomId}/sections")
    public ResponseEntity<?> updateClassroomSection(HttpSession session, @PathVariable String classroomId, @RequestBody HashMap<String, Object> section) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        //////isInClass method needed in classRoom service which if the user has access to the class then returns a MAP(json) of classroom data else nul
        Map<String, Object> res = classromService.getUserRole((String) session.getAttribute("username"), classroomId);
        if (res.get("obj").equals("teacher") || res.get("obj").equals("admin")){
            if (section.get("addNew").equals(false)){
                res = classromService.updateClassroomSections((String) section.get("newTitle"), classroomId, (String) section.get("sectionId"));
            }
            else {
                res = classromService.updateClassroomSections((String) section.get("newTitle"), classroomId, "");
            }
            //////////////////updateClassroomSections which takes user_id and list of all sections(updated) to replace the DB
        }
        return ResponseEntity.ok(res);
    }

    //get user grades
    @GetMapping("/{classroomId}/studentGrades")
    public ResponseEntity<?> getGrades(HttpSession session, @PathVariable String classroomId) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        //////getUserGrades method needed that takes username and classroomID and returns a list of sections in which there are the list of acticity of that section and corresponding grade of the user of that activity and the name of the activity and the name of the section
        Map<String, Object> res = classromService.getUserGrades((String) session.getAttribute("username"), classroomId);

        return ResponseEntity.ok(res);
    }

    //returns glosery of the classroom
    @GetMapping("/{classroomId}/glossary")
    public ResponseEntity<?> getClassroomGlossary(HttpSession session, @PathVariable String classroomId) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        //////getClassroomGlossary method needed in classRoom service which returns and array of objsct that has two fields: 1.key 2.value
        Map<String, Object> res = classromService.getClassroomGlossary((String) session.getAttribute("username"), classroomId);
        return ResponseEntity.ok(res);
    }

    @PatchMapping("/{classroomId}/glossary")
    public ResponseEntity<?> updateClassroomGlossary(HttpSession session, @PathVariable String classroomId, @RequestBody HashMap<String, Object> glossaryList) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        /////////////////updateClassroomGlossary method needed in classRoom service which takes new glossary list and updates it for the classroom id it takes
        Map<String, Object> res = classromService.getUserRole((String) session.getAttribute("username"), classroomId);
        if (res.get("obj").equals("teacher") || res.get("obj").equals("admin")){
            res = classromService.updateClassroomGlossary(classroomId, (List<HashMap<String, String>>) glossaryList.get("glossary"));
            return ResponseEntity.ok(res);
        }
        else {
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "You don't have access to this classroom!");
            return ResponseEntity.ok(error);
        }
    }

    @GetMapping("/{classroomId}/grades")
    public ResponseEntity<?> updateClassroomTopicGrades(HttpSession session, @PathVariable String classroomId) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        /////////////////updateClassroomGlossary method needed in classRoom service which takes new glossary list and updates it for the classroom id it takes
        Map<String, Object> res = classromService.getUserRole((String) session.getAttribute("username"), classroomId);
        if (res.get("obj").equals("teacher") || res.get("obj").equals("admin")){
            res = ARserivce.getAllGradesForClassroom(classroomId);
            return ResponseEntity.ok(res);
        }
        else {
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "You don't have access to this classroom!");
            return ResponseEntity.ok(error);
        }
    }

    //returns AR data for teacher to tashih or for student to do the exam for example
    @GetMapping("/{classroomId}/AR/{ARid}")
    public ResponseEntity<?> getClassroomSections(HttpSession session, @PathVariable String ARid , @PathVariable String classroomId) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        Map<String, Object> res = classromService.getUserRole((String) session.getAttribute("username"), classroomId);
        if(res.get("obj").equals("student")){
            res = ARserivce.getARdata(ARid);  ///////AR data for student
            return ResponseEntity.ok(res);
        }
        else if (res.get("obj").equals("teacher") || res.get("obj").equals("admin")){
            res = ARserivce.getARdataTeacher(ARid);  /////////////AR data for teacher to tashih : [{firstName, lastName, studentGrade}]
            return ResponseEntity.ok(res);
        }
        else
            return ResponseEntity.status(302).header("Location", "/login?msg=403!").build();
    }

    @GetMapping("/{classroomId}/quiz/{quizId}")
    public ResponseEntity<?> getQuestion(HttpSession session, @PathVariable String quizId , @PathVariable String classroomId) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        Map<String, Object> res = classromService.isInClass((String) session.getAttribute("username"), classroomId);
        if(res.get("status").equals(true)){
            res = ARserivce.getQuizQuestions(quizId);  ////////////////////////getQuizQ for student that returns a list of question objects(hash map)
            return ResponseEntity.ok(res);
        }
        else
            return ResponseEntity.status(302).header("Location", "/login?msg=403!").build();
    }
    @PatchMapping("/{classroomId}/quiz/{quizId}")
    public ResponseEntity<?> putQuestionUser(HttpSession session, @PathVariable String quizId , @PathVariable String classroomId, @RequestBody List<HashMap<String, String>> answers) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        Map<String, Object> res = classromService.getUserRole((String) session.getAttribute("username"), classroomId);
        if(res.get("obj").equals("student")){
            res = ARserivce.putUserAnswers((String) session.getAttribute("username"), quizId, answers);
            return ResponseEntity.ok(res);
        }
        else
            return ResponseEntity.status(302).header("Location", "/login?msg=403!").build();
    }

    @PostMapping("/{classroomId}/section/{sectionId}/addQuiz")
    public ResponseEntity<?> getClassroomSections(HttpSession session, @PathVariable String classroomId, @PathVariable String sectionId, @RequestBody HashMap<String, Object> object) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        Map<String, Object> res = classromService.getUserRole((String) session.getAttribute("username"), classroomId);
        if(res.get("obj").equals("teacher") || res.get("obj").equals("admin")){
            res = ARserivce.addQuiz(classroomId, sectionId, object);
            return ResponseEntity.ok(res);
        }
        else
            return ResponseEntity.status(302).header("Location", "/login?msg=403!").build();
    }

    @PatchMapping("/{classroomId}/quiz/{quizId}/setUserGrade")
    public ResponseEntity<?> putUserGrade(HttpSession session, @PathVariable String quizId , @PathVariable String classroomId, @RequestBody Map<String, Object> answer) {
        if (session.getAttribute("username") == null){
            Map<String, Object> error = new HashMap<>();
            error.put("status", false);
            error.put("msg", "Sign in first!");
            return ResponseEntity.ok(error);
        }

        Map<String, Object> res = ARserivce.setUserGrade(quizId, (String) answer.get("username"), (String) answer.get("newGrade"));
        return ResponseEntity.ok(res);
    }


}
