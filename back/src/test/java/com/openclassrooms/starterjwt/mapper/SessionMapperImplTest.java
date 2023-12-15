package com.openclassrooms.starterjwt.mapper;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class SessionMapperImplTest {
    @InjectMocks
    private SessionMapperImpl sessionMapper = new SessionMapperImpl();

    @Test
    void toEntity_WithNullDto_ReturnsNull() {
        // given // when
        Session result = sessionMapper.toEntity((SessionDto) null);

        // then
        assertNull(result);
    }

    @Test
    void toEntityList_WithNullListDto_ReturnsNull() {
        // given // when
        List<Session> result = sessionMapper.toEntity((List<SessionDto>) null);

        // then
        assertNull(result);
    }


    @Test
    void toDtoList_WithNullEntityList_ReturnsNull() {
        // given // when
        List<SessionDto> result = sessionMapper.toDto((List<Session>) null);

        // then
        assertNull(result);
    }

    @Test
    void toDto_WithNullEntity_ReturnsNull() {
        // given // when
        SessionDto result = sessionMapper.toDto((Session) null);

        // then
        assertNull(result);
    }

    @Test
    void toEntity_WithValidDto_ReturnsCorrectEntity() {
        // Given
        SessionDto sessionDto = createSessionDto(1L, "Test Session");

        // When
        Session result = sessionMapper.toEntity(sessionDto);

        // Then
        assertNotNull(result);
        assertEquals(sessionDto.getId(), result.getId());
        assertEquals(sessionDto.getDescription(), result.getDescription());
        assertEquals(sessionDto.getName(), result.getName());
        assertEquals(sessionDto.getDate(), result.getDate());
        assertEquals(sessionDto.getCreatedAt(), result.getCreatedAt());
        assertEquals(sessionDto.getUpdatedAt(), result.getUpdatedAt());

    }

    @Test
    void toEntityList_WithValidDtoList_ReturnsCorrectEntityList() {
        // given
        SessionDto sessionDto1 = createSessionDto(1L, "Session 1");
        SessionDto sessionDto2 = createSessionDto(2L, "Session 2");
        List<SessionDto> dtoList = new ArrayList<>();
        dtoList.add(sessionDto1);
        dtoList.add(sessionDto2);

        // when
        List<Session> entityList = sessionMapper.toEntity(dtoList);

        // then
        assertEquals(dtoList.size(), entityList.size());
        assertEquals(sessionDto1.getId(), entityList.get(0).getId());
        assertEquals(sessionDto1.getName(), entityList.get(0).getName());
    }

    @Test
    void toDtoList_WithValidEntityList_ReturnsCorrectDtoList() {
        // given
        Session session1 = createSession(1L, "Session 1");
        Session session2 = createSession(2L, "Session 2");
        List<Session> entityList = Arrays.asList(session1, session2);

        // when
        List<SessionDto> dtoList = sessionMapper.toDto(entityList);

        // then
        assertEquals(entityList.size(), dtoList.size());
        assertEquals(session1.getId(), dtoList.get(0).getId());
        assertEquals(session1.getName(), dtoList.get(0).getName());
    }

    @Test
    void testToDto_WithValidSession_ReturnsCorrectDto() {
        // given
        SessionMapperImpl sessionMapper = new SessionMapperImpl();
        Teacher teacherWithId = Teacher.builder().id(42L).build();
        Session session = Session.builder()
                .id(1L)
                .name("Test Session")
                .description("Test Description")
                .teacher(teacherWithId)
                .build();

        // when
        SessionDto sessionDto = sessionMapper.toDto(session);

        // then
        assertEquals(session.getId(), sessionDto.getId());
        assertEquals(session.getName(), sessionDto.getName());
        assertEquals(session.getDescription(), sessionDto.getDescription());
        assertEquals(42L, sessionDto.getTeacher_id());
    }

    @Test
    void testToDto_WithSessionWithoutTeacher_ReturnsDtoWithNullTeacherId() {
        // given

        Session sessionWithoutTeacher = Session.builder()
                .id(1L)
                .name("Test Session")
                .description("Test Description")
                .build();

        // when
        SessionDto sessionDto = sessionMapper.toDto(sessionWithoutTeacher);

        // then
        assertEquals(sessionWithoutTeacher.getId(), sessionDto.getId());
        assertEquals(sessionWithoutTeacher.getName(), sessionDto.getName());
        assertEquals(sessionWithoutTeacher.getDescription(), sessionDto.getDescription());
        assertNull(sessionDto.getTeacher_id());
    }

    @Test
    void testToDto_WithNullSession_ReturnsNullDto() {
        // given
        SessionMapperImpl sessionMapper = new SessionMapperImpl();

        // when
        SessionDto sessionDto = sessionMapper.toDto((Session) null);

        // then
        assertNull(sessionDto);
    }

    private SessionDto createSessionDto(Long id, String name) {
        SessionDto sessionDto = new SessionDto();
        sessionDto.setId(id);
        sessionDto.setName(name);
        return sessionDto;
    }

    private Session createSession(Long id, String name) {
        Session.SessionBuilder sessionBuilder = Session.builder();
        sessionBuilder.id(id);
        sessionBuilder.name(name);
        return sessionBuilder.build();
    }

}
