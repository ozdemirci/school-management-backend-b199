package com.project.schoolmanagment.controller.user;

import com.project.schoolmanagment.payload.request.user.ChooseLessonProgramWithId;
import com.project.schoolmanagment.payload.request.user.StudentRequest;
import com.project.schoolmanagment.payload.request.user.StudentRequestWithoutPassword;
import com.project.schoolmanagment.payload.response.abstracts.ResponseMessage;
import com.project.schoolmanagment.payload.response.user.StudentResponse;
import com.project.schoolmanagment.service.user.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping("/save")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public ResponseEntity<ResponseMessage<StudentResponse>> saveStudent(@RequestBody @Valid StudentRequest studentRequest) {
        return ResponseEntity.ok(studentService.saveStudent(studentRequest));
    }

    @PatchMapping("/update")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseEntity<String> updateStudent(@RequestBody @Valid
                                                        StudentRequestWithoutPassword studentRequestWithoutPassword,
                                                HttpServletRequest request) {
        return studentService.updateStudentWithoutPassword(studentRequestWithoutPassword, request);
    }

    @PutMapping("/update/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage<StudentResponse> updateStudentForManagers(
            @PathVariable Long id,
            @RequestBody @Valid StudentRequest studentRequest) {
        return studentService.updateStudentForManagers(id, studentRequest);
    }

    @PostMapping("/addLessonProgramToStudent")
    @PreAuthorize("hasAnyAuthority('STUDENT')")
    public ResponseMessage<StudentResponse> addLessonProgram(HttpServletRequest request,
                                                             @RequestBody @Valid ChooseLessonProgramWithId chooseLessonProgramWithId) {
        return studentService.addLessonProgram(request, chooseLessonProgramWithId);
    }

    @GetMapping("/changeStatus")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseMessage changeStatusOfStudent(@RequestParam Long id, @RequestParam boolean status) {
        return studentService.changeStatusOfStudent(id, status);
    }


    //TODO
    // getAllByPage -> burhan
    @GetMapping("/getAllStudentByPage/{userRole}")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<Page<StudentResponse>> getAllStudentByPage(
            @PathVariable String userRole,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size,
            @RequestParam(value = "sort", defaultValue = "name") String sort,
            @RequestParam(value = "type", defaultValue = "desc") String type
    ) {
        Page<StudentResponse> studentResponse = studentService.getAllStudentByPage(page, size, sort, type, userRole);
        return new ResponseEntity<>(studentResponse, HttpStatus.OK);
    }

    //TODO
    // getAllByList (parameter-username contains as parameter) -> burhan
    @GetMapping("/getAllStudentByUsernameContains")
    @PreAuthorize("hasAnyAuthority('ADMIN','MANAGER','ASSISTANT_MANAGER')")
    public ResponseEntity<List<StudentResponse>> getAllStudentByUsernameContains(@RequestParam String username) {
        return ResponseEntity.ok(studentService.getAllStudentByUsernameContains(username));
    }

    //TODO
    // findById -> hikmet
    // getAllByList (active-passive as parameter) by list -> hikmet


}
