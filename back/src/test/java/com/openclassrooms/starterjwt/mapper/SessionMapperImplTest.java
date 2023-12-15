package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.services.TeacherService;
import com.openclassrooms.starterjwt.services.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class SessionMapperImplTest {
    @InjectMocks
    private SessionMapperImpl sessionMapper = new SessionMapperImpl();

    @Mock
    private TeacherService teacherService;

    @Mock
    private UserService userService;

    @Test
    void toEntity_WithNullDto_ReturnsNull() {
        // given // when
        List<Session> result = sessionMapper.toEntity((List<SessionDto>) null);

        // then
        assertNull(result);
    }

    @Test
    void toDto_WithNullEntity_ReturnsNull() {
        // given // when
        List<SessionDto> result = sessionMapper.toDto((List<Session>) null);

        // then
        assertNull(result);
    }

    @Test
    void toEntityList_WithValidDtoList_ReturnsCorrectEntityList() {
        // given
        SessionDto sessionDto1 = createSessionDto(1L, "Session 1");
        SessionDto sessionDto2 = createSessionDto(2L, "Session 2");
        List<SessionDto> dtoList = new ArrayList<>();
        dtoList.add(sessionDto1);
        dtoList.add(sessionDto2);

        // Mocking external dependencies
        when(teacherService.findById(anyLong())).thenReturn(createMockTeacher());
        when(userService.findById(anyLong())).thenReturn(createMockUser());

        // When
        List<Session> entityList = sessionMapper.toEntity(dtoList);

        // Then
        assertEquals(dtoList.size(), entityList.size());
        assertEquals(sessionDto1.getId(), entityList.get(0).getId());
        assertEquals(sessionDto1.getName(), entityList.get(0).getName());
        // Add more assertions for other fields
        verify(teacherService, times(2)).findById(anyLong());
        verify(userService, times(2)).findById(anyLong());
    }

    @Test
    void toDtoList_WithValidEntityList_ReturnsCorrectDtoList() {
        // Given
        Session session1 = createSession(1L, "Session 1");
        Session session2 = createSession(2L, "Session 2");
        List<Session> entityList = Arrays.asList(session1, session2);

        // When
        List<SessionDto> dtoList = sessionMapper.toDto(entityList);

        // Then
        assertEquals(entityList.size(), dtoList.size());
        assertEquals(session1.getId(), dtoList.get(0).getId());
        assertEquals(session1.getName(), dtoList.get(0).getName());
        // Add more assertions for other fields
    }

    private SessionDto createSessionDto(Long id, String name) {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName(name);
        // Set other fields as needed
        return sessionDto;
    }

    private Session createSession(Long id, String name) {
        Session.SessionBuilder sessionBuilder = Session.builder();
        sessionBuilder.id(id);
        sessionBuilder.name(name);
        // Set other fields as needed
        return sessionBuilder.build();
    }

    private Teacher createMockTeacher() {

        return Teacher.builder()
                .id(1L)
                .lastName("Doe")
                .firstName("John")
                .createdAt(null)
                .updatedAt(null)
                .build();
    }

    private User createMockUser() {
        return User.builder()
                .id(1L)
                .firstName("John")
                .lastName("Doe")
                .password("password")
                .email("email@mail.com")
                .createdAt(null)
                .updatedAt(null)
                .build();
    }
}
