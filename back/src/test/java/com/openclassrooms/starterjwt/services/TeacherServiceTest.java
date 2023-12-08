package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.repository.TeacherRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeacherServiceTest {
    @InjectMocks
    private TeacherService teacherService;
    @Mock
    private TeacherRepository teacherRepository;

    @Test
    void testFindById_TeacherExists() {
        // given
        Long teacherId = 1L;
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(teacherId);
        mockTeacher.setFirstName("Mario");
        mockTeacher.setLastName("Rossi");
        when(teacherRepository.findById(teacherId)).thenReturn(Optional.of(mockTeacher));

        // when
        Teacher resultTeacher = teacherService.findById(teacherId);

        // then
        assertNotNull(resultTeacher);
        assertEquals(teacherId, resultTeacher.getId());
        assertEquals("Mario", resultTeacher.getFirstName());
        assertEquals("Rossi", resultTeacher.getLastName());
        verify(teacherRepository, times(1)).findById(teacherId);
    }

    @Test
    void findAllTeachers() {
        // given
        Teacher mockTeacher = new Teacher();
        mockTeacher.setId(1L);
        mockTeacher.setFirstName("Mario");
        mockTeacher.setLastName("Rossi");

        Teacher mockTeacher1 = new Teacher();
        mockTeacher.setId(1L);
        mockTeacher.setFirstName("Luigi");
        mockTeacher.setLastName("Verdi");
        List<Teacher> mockTeachers = new ArrayList<>();
        mockTeachers.add(mockTeacher);
        mockTeachers.add(mockTeacher1);

        when(teacherRepository.findAll()).thenReturn(mockTeachers);

        // when
        List<Teacher> resultTeacher = teacherService.findAll();

        // then
        assertNotNull(resultTeacher);
        assertEquals(2, resultTeacher.size());
        assertEquals("Luigi", resultTeacher.get(0).getFirstName());
        assertEquals("Verdi", resultTeacher.get(0).getLastName());
        verify(teacherRepository, times(1)).findAll();
    }
}