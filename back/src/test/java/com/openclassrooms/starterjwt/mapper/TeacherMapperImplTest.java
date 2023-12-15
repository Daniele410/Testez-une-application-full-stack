package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.TeacherDto;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class TeacherMapperImplTest {

    @InjectMocks
    private TeacherMapperImpl teacherMapper = new TeacherMapperImpl();

    @Test
    void toDto_WithNullEntity_ReturnsNull() {
        // Given // When
        TeacherDto result = teacherMapper.toDto((Teacher) null);

        // Then
        assertNull(result);
    }

    @Test
    void toEntity_WithNullDto_ReturnsNull() {
        // Given // When
        Teacher result = teacherMapper.toEntity((TeacherDto) null);

        // Then
        assertNull(result);
    }

    @Test
    void toEntityList_WithValidDtoList_ReturnsCorrectEntityList() {
        // Given
        TeacherDto teacherDto1 = new TeacherDto();
        teacherDto1.setId(1L);
        teacherDto1.setLastName("Doe");
        teacherDto1.setFirstName("John");

        TeacherDto teacherDto2 = new TeacherDto();
        teacherDto2.setId(2L);
        teacherDto2.setLastName("Smith");
        teacherDto2.setFirstName("Jane");

        List<TeacherDto> dtoList = Arrays.asList(teacherDto1, teacherDto2);

        // When
        List<Teacher> entityList = teacherMapper.toEntity(dtoList);

        // Then
        assertEquals(dtoList.size(), entityList.size());
        assertEquals(teacherDto1.getId(), entityList.get(0).getId());
        assertEquals(teacherDto1.getLastName(), entityList.get(0).getLastName());
        assertEquals(teacherDto1.getFirstName(), entityList.get(0).getFirstName());
        assertEquals(teacherDto2.getId(), entityList.get(1).getId());
        assertEquals(teacherDto2.getLastName(), entityList.get(1).getLastName());
        assertEquals(teacherDto2.getFirstName(), entityList.get(1).getFirstName());
    }

    @Test
    void toDtoList_WithValidEntityList_ReturnsCorrectDtoList() {
        // Given
        Teacher teacher1 = Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .build();

        Teacher teacher2 = Teacher.builder()
                .id(2L)
                .lastName("Smith")
                .firstName("Jane")
                .build();

        List<Teacher> entityList = Arrays.asList(teacher1, teacher2);

        // When
        List<TeacherDto> dtoList = teacherMapper.toDto(entityList);

        // Then
        assertEquals(entityList.size(), dtoList.size());
        assertEquals(teacher1.getId(), dtoList.get(0).getId());
        assertEquals(teacher1.getLastName(), dtoList.get(0).getLastName());
        assertEquals(teacher1.getFirstName(), dtoList.get(0).getFirstName());
        assertEquals(teacher2.getId(), dtoList.get(1).getId());
        assertEquals(teacher2.getLastName(), dtoList.get(1).getLastName());
        assertEquals(teacher2.getFirstName(), dtoList.get(1).getFirstName());
    }

    @Test
    void toDtoList_WithNullEntityList_ReturnsNull() {
        // Given // When
        List<TeacherDto> dtoList = teacherMapper.toDto((List<Teacher>) null);

        // Then
        assertNull(dtoList);
    }

    @Test
    void toEntityList_WithNullDtoList_ReturnsNull() {
        // Given // When
        List<Teacher> entityList = teacherMapper.toEntity((List<TeacherDto>) null);

        // Then
        assertNull(entityList);
    }

}