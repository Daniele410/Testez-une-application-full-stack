package com.openclassrooms.starterjwt.services;

import com.openclassrooms.starterjwt.exception.BadRequestException;
import com.openclassrooms.starterjwt.exception.NotFoundException;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.Teacher;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SessionServiceTest {
    @InjectMocks
    private SessionService sessionService;
    @Mock
    private SessionRepository sessionRepository;
    @Mock
    private UserRepository userRepository;

    @Test
    void testCreateSession_shouldReturnSession() {
        // given
        Session session = Session.builder()
                .id(1L)
                .name("Session 1")
                .date(new Date())
                .description("Description 1")
                .teacher(new Teacher())
                .build();

        when(sessionRepository.save(session)).thenReturn(session);

        // when
        Session resultSession = sessionService.create(session);

        // then
        assertNotNull(resultSession);
        assertEquals(1L, resultSession.getId());
        assertEquals("Session 1", resultSession.getName());
    }

    @Test
    void testDeleteSession_shouldVerifyDeleteSession() {
        // given
        Long sessionId = 1L;

        // when
        sessionService.delete(sessionId);

        // then
        verify(sessionRepository, times(1)).deleteById(sessionId);
    }

    @Test
    void testFindAllSessions_ShouldReturnAllSessions() {
        // given
        Session mockSession = new Session();
        mockSession.setId(1L);
        mockSession.setName("Session 1");
        mockSession.setDate(new Date());
        mockSession.setDescription("Description 1");
        mockSession.setTeacher(new Teacher());
        mockSession.setUsers(new ArrayList<>());

        Session mockSession1 = new Session();
        mockSession.setId(1L);
        mockSession.setName("Session 1");
        mockSession.setDate(new Date());
        mockSession.setDescription("Description 1");
        mockSession.setTeacher(new Teacher());
        mockSession.setUsers(new ArrayList<>());

        List<Session> sessions = new ArrayList<>();
        sessions.add(mockSession);
        sessions.add(mockSession1);

        when(sessionRepository.findAll()).thenReturn(sessions);

        // when
        List<Session> result = sessionService.findAll();

        // then
        verify(sessionRepository, times(1)).findAll();
        assertEquals(sessions, result);
    }

    @Test
    void testGetByIdSession_ShouldReturnSession() {
        // given
        Long sessionId = 1L;
        Session session = Session.builder()
                .id(1L)
                .name("Session 1")
                .date(new Date())
                .description("Description 1")
                .teacher(new Teacher())
                .build();

        when(sessionRepository.findById(sessionId)).thenReturn(java.util.Optional.of(session));

        // when
        Session resultSession = sessionService.getById(sessionId);

        // then
        assertNotNull(resultSession);
        assertEquals(1L, resultSession.getId());
        assertEquals("Session 1", resultSession.getName());
    }

    @Test
    void testUpdateSession_ShouldReturnNotNull() {
        // given
        Long sessionId = 1L;
        Session session = Session.builder()
                .id(1L)
                .name("Session 1")
                .date(new Date())
                .description("Description 1")
                .teacher(new Teacher())
                .build();

        when(sessionRepository.save(session)).thenReturn(session);

        // when
        Session resultSession = sessionService.update(sessionId, session);

        // then
        assertNotNull(resultSession);
        assertEquals(1L, resultSession.getId());
        assertEquals("Session 1", resultSession.getName());
    }

    @Test
    void testParticipateSession() {
        // given
        Long sessionId = 1L;
        Long userId = 2L;
        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setUsers(new ArrayList<>());

        User mockUser = new User();
        mockUser.setId(userId);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        assertDoesNotThrow(() -> sessionService.participate(sessionId, userId));

        // then
        assertTrue(mockSession.getUsers().contains(mockUser));
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(userRepository, times(1)).findById(userId);
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    void testParticipate_ShouldReturnSessionNotFound() {
        // given
        Long sessionId = 1L;
        Long userId = 2L;

        // when
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // then
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository, times(1)).findById(sessionId);

    }

    @Test
    void testParticipate_ShouldReturnUserNotFound() {
        // given
        Long sessionId = 1L;
        Long userId = 2L;

        Session mockSession = new Session();
        mockSession.setId(sessionId);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // when // then
        assertThrows(NotFoundException.class, () -> sessionService.participate(sessionId, userId));
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testParticipate_ShouldReturnAlreadyParticipating() {
        // given
        Long sessionId = 1L;
        Long userId = 2L;

        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setName("Session 1");
        mockSession.setDate(new Date());
        mockSession.setDescription("Description 1");
        mockSession.setTeacher(new Teacher());
        mockSession.setUsers(new ArrayList<>());

        User mockUser = new User();
        mockUser.setId(userId);
        mockSession.getUsers().add(mockUser);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));
        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        // when
        assertThrows(BadRequestException.class,
                () -> sessionService.participate(sessionId, userId));

        // then
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(userRepository, times(1)).findById(userId);
        verify(sessionRepository, never()).save(mockSession);
    }

    @Test
    void testNoLongerParticipate_Successful() {
        // given
        Long sessionId = 1L;
        Long userId = 2L;

        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setName("Session 1");
        mockSession.setDate(new Date());
        mockSession.setDescription("Description 1");
        mockSession.setTeacher(new Teacher());
        mockSession.setUsers(new ArrayList<>());

        User mockUser = new User();
        mockUser.setId(userId);
        mockSession.getUsers().add(mockUser);

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        // when
        assertDoesNotThrow(() -> sessionService.noLongerParticipate(sessionId, userId));

        // then
        assertTrue(mockSession.getUsers().isEmpty());
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).save(mockSession);
    }

    @Test
    void testNoLongerParticipate_SessionNotFound() {
        // given
        Long sessionId = 1L;
        Long userId = 2L;

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.empty());

        // when
        assertThrows(NotFoundException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

        // then
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void testNoLongerParticipate_NotParticipating() {
        // given
        Long sessionId = 1L;
        Long userId = 2L;

        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setName("Session 1");
        mockSession.setDate(new Date());
        mockSession.setDescription("Description 1");
        mockSession.setTeacher(new Teacher());
        mockSession.setUsers(new ArrayList<>());

        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        // when
        assertThrows(BadRequestException.class, () -> sessionService.noLongerParticipate(sessionId, userId));

        // then
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, never()).save(any());
    }

    @Test
    void testNoLongerParticipate_FilterUsersSuccessfully() {
        // given
        Long sessionId = 1L;
        Long userIdToRemove = 2L;

        Session mockSession = new Session();
        mockSession.setId(sessionId);
        mockSession.setName("Session 1");
        mockSession.setDate(new Date());
        mockSession.setDescription("Description 1");
        mockSession.setTeacher(new Teacher());
        mockSession.setUsers(new ArrayList<>());

        User userToRemove = new User();
        userToRemove.setId(userIdToRemove);
        mockSession.getUsers().add(userToRemove);
        when(sessionRepository.findById(sessionId)).thenReturn(Optional.of(mockSession));

        // when
        assertDoesNotThrow(() -> sessionService.noLongerParticipate(sessionId, userIdToRemove));

        //then
        assertTrue(mockSession.getUsers().isEmpty());
        verify(sessionRepository, times(1)).findById(sessionId);
        verify(sessionRepository, times(1)).save(mockSession);
    }

}