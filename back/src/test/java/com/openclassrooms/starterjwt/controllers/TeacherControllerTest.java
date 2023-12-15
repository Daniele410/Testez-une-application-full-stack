package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.mapper.TeacherMapper;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.TeacherService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class TeacherControllerTest {
    @InjectMocks
    private TeacherController teacherController;

    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;

    @Test
    void testFindTeacherById_shouldReturnResponseOk() {
        // given
        long teacherId = 1L;
        Teacher mockTeacher = Teacher.builder()
                .id(1L)
                .firstName("Mario")
                .lastName("Rossi")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();

        TeacherDto mockUserDto = new TeacherDto();
        mockUserDto.setId(1L);
        mockUserDto.setFirstName("Mario");

        when(teacherService.findById(1L)).thenReturn(mockTeacher);
        when(teacherMapper.toDto(mockTeacher)).thenReturn(mockUserDto);

        // when
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(teacherId));

        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockTeacher.getFirstName(), mockUserDto.getFirstName());
    }

    @Test
    void testFindTeacherById_findById_shouldReturnResponseNotFoundCode() {
        // given
        long teacherId = 2L;
        when(teacherService.findById(2L)).thenReturn(null);

        // when
        ResponseEntity<?> responseEntity = teacherController.findById(String.valueOf(teacherId));

        // then
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void testTeacherFindById_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = teacherController.findById("nonNumericId");

        // then
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    void testFindGetAllTeachers_shouldReturnResponseOk() {
        // given
        Teacher mockTeacher = Teacher.builder()
                .id(1L)
                .firstName("Mario")
                .lastName("Rossi")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        Teacher mockTeacher2 = Teacher.builder()
                .id(2L)
                .firstName("Luigi")
                .lastName("Verdi")
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        List<Teacher> teachers = new ArrayList<>();
        teachers.add(mockTeacher);
        teachers.add(mockTeacher2);
        when(teacherService.findAll()).thenReturn(teachers);

        // when
        ResponseEntity<?> response = teacherController.findAll();

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, (response.getBody()).toString().length());

    }


}