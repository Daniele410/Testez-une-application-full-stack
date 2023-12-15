package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.mapper.SessionMapper;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

@AutoConfigureMockMvc
@ExtendWith(MockitoExtension.class)
class SessionControllerTest {

    @InjectMocks
    private SessionController sessionController;

    @Mock
    private SessionMapper sessionMapper;

    @Mock
    private SessionService sessionService;

    @Test
    public void testSessionFindById_shouldReturnResponseOk() {
        // given
        long sessionId = 1L;
        Session mockSession = Session.builder()
                .id(1L)
                .name("Mario")
                .date(new Date("2021/01/01"))
                .description("mySession")
                .teacher(Teacher.builder().id(1L).build())
                .build();

        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);


        when(sessionService.getById(1L)).thenReturn(mockSession);
        when(sessionMapper.toDto(mockSession)).thenReturn(mockSessionDto);

        // when
        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(sessionId));

        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
        assertEquals(mockSession.getId(), mockSessionDto.getId());
    }

    @Test
    public void testSessionFindById_shouldReturnResponseNotFoundCode() {
        // given
        long sessionId = 2L;

        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);


        when(sessionService.getById(2L)).thenReturn(null);

        // when
        ResponseEntity<?> responseEntity = sessionController.findById(String.valueOf(sessionId));

        // then
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void testSessionFindById_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = sessionController.findById("nonNumericId");
        // then
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testSessionGetAllSession_shouldReturnResponseOk() {
        // given
        Session mockSession = Session.builder()
                .id(1L)
                .name("Mario")
                .date(new Date("2021/01/01"))
                .description("mySession")
                .teacher(Teacher.builder().id(1L).build())
                .build();

        Session mockSession1 = Session.builder()
                .id(2L)
                .name("Luigi")
                .date(new Date("2021/01/02"))
                .description("mySession")
                .teacher(Teacher.builder().id(1L).build())
                .build();
        List<Session> sessions = new ArrayList<>();
        sessions.add(mockSession);
        sessions.add(mockSession1);

        when(sessionService.findAll()).thenReturn(sessions);

        // when
        ResponseEntity<?> response = sessionController.findAll();

        // then
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(2, (response.getBody()).toString().length());
    }

    @Test
    public void testSessionCreate_shouldReturnResponseOk() {
        // given
        Session mockSession = Session.builder()
                .id(1L)
                .name("Mario")
                .date(new Date("2021/01/01"))
                .description("mySession")
                .teacher(Teacher.builder().id(1L).build())
                .build();

        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);

        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.create(mockSession)).thenReturn(mockSession);

        // when
        ResponseEntity<?> responseEntity = sessionController.create(mockSessionDto);

        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testSessionUpdate_shouldReturnResponseOk() {
        // given
        long sessionId = 1L;
        Session mockSession = Session.builder()
                .id(1L)
                .name("Mario")
                .date(new Date("2021/01/01"))
                .description("mySession")
                .teacher(Teacher.builder().id(1L).build())
                .build();

        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);

        when(sessionMapper.toEntity(mockSessionDto)).thenReturn(mockSession);
        when(sessionService.update(sessionId, mockSession)).thenReturn(mockSession);

        // when
        ResponseEntity<?> responseEntity = sessionController.update(String.valueOf(sessionId), mockSessionDto);

        // then
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testSessionUpdate_shouldReturnResponseBadRequest() {
        // given
        ResponseEntity<?> responseEntity = sessionController.update("nonNumericId", null);
        // then
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testSessionSave_shouldReturnResponseOk() {
        // given
        long sessionId = 1L;
        Session mockSession = Session.builder()
                .id(1L)
                .name("Mario")
                .date(new Date("2021/01/01"))
                .description("mySession")
                .teacher(Teacher.builder().id(1L).build())
                .build();

        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);

        when(sessionService.getById(sessionId)).thenReturn(mockSession);


        // when
        ResponseEntity<?> responseEntity = sessionController.save(String.valueOf(sessionId));

        // then
        verify(sessionService, times(1)).delete(anyLong());
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testSessionSave_shouldReturnResponseNotFound() {
        // given
        long sessionId = 1L;
        when(sessionService.getById(sessionId)).thenReturn(null);

        // when
        ResponseEntity<?> responseEntity = sessionController.save(String.valueOf(sessionId));

        // then
        assertEquals(404, responseEntity.getStatusCodeValue());
    }

    @Test
    void testSessionSave_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = sessionController.save("nonNumericId");

        // then
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void testSessionParticipate_shouldReturnResponseOk() {
        // given
        long sessionId = 1L;
        long userId = 1L;

        // when
        ResponseEntity<?> responseEntity = sessionController.participate(String.valueOf(sessionId), String.valueOf(userId));

        // then
        assertEquals(ResponseEntity.ok().build(), responseEntity);
    }

    @Test
    public void testSessionParticipate_shouldReturnResponseBadRequest() {
        // given
        String sessionId = "nonNumericId";
        long userId = 2L;

        // when
        ResponseEntity<?> responseEntity = sessionController.participate(sessionId, String.valueOf(userId));

        // then
        assertEquals(400, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testSessionNoLongerParticipate_shouldReturnResponseOk() {
        // given
        long sessionId = 1L;
        long userId = 2L;

        // when
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(String.valueOf(sessionId), String.valueOf(userId));

        // then
        verify(sessionService, times(1)).noLongerParticipate(sessionId, userId);
        assertEquals(200, responseEntity.getStatusCodeValue());
    }

    @Test
    public void testSessionNoLongerParticipate_shouldReturnResponseBadRequest() {
        // given
        String sessionId = "nonNumericId";
        long userId = 2L;

        // when
        ResponseEntity<?> responseEntity = sessionController.noLongerParticipate(sessionId, String.valueOf(userId));

        // then
        assertEquals(400, responseEntity.getStatusCodeValue());
    }
}