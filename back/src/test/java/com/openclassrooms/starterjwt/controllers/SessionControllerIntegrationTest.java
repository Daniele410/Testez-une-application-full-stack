package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.dto.SessionDto;
import com.openclassrooms.starterjwt.models.Session;
import com.openclassrooms.starterjwt.models.User;
import com.openclassrooms.starterjwt.repository.SessionRepository;
import com.openclassrooms.starterjwt.repository.UserRepository;
import com.openclassrooms.starterjwt.services.SessionService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@AutoConfigureMockMvc
@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) //erase database each test
@WithMockUser(username = "Admin", authorities = {"ADMIN"})
public class SessionControllerIntegrationTest {

    @Autowired
    private SessionController sessionController;

    @Autowired
    private SessionService sessionService;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    UserRepository userRepository;


    @Test
    void testFindGetAllSessions_shouldReturnResponseOk() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);

        // when
        ResponseEntity<?> response = sessionController.findAll();

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().toString().length());
    }

    @Test
    void testFindById_shouldReturnResponseOk() {
        // given
        Session realSession1 = new Session();
        realSession1.setId(1L);
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());

        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setId(2L);
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());

        sessionRepository.save(realSession2);

        // when
        ResponseEntity<?> response = sessionController.findById("1");

        // then
        Session session = sessionRepository.getById(realSession1.getId());
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testFindById_SessionDoesNotExist_shouldReturnResponseNotFound() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);

        // when
        ResponseEntity<?> response = sessionController.findById("3");

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testFindById_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = sessionController.findById("nonNumericId");
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testCreateSession_shouldReturnResponseOk() {
        // given
        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);

        // when
        ResponseEntity<?> response = sessionController.create(mockSessionDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateSession_shouldReturnResponseOk() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setId(1L);
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);

        // when
        ResponseEntity<?> response = sessionController.update(String.valueOf(realSession1.getId()), mockSessionDto);

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testUpdateSession_SessionDoesNotExist_shouldReturnResponseNotFound() {
        // given
        SessionDto mockSessionDto = new SessionDto();
        mockSessionDto.setId(3L);
        mockSessionDto.setName("Mario");
        mockSessionDto.setDate(new Date("2021/01/01"));
        mockSessionDto.setDescription("mySession");
        mockSessionDto.setTeacher_id(1L);

        // when
        ResponseEntity<?> response = sessionController.update("", mockSessionDto);

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testSaveSession_shouldReturnResponseOk() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        // when
        ResponseEntity<?> response = sessionController.save(String.valueOf(realSession1.getId()));

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testSaveSession_SessionDoesNotExist_shouldReturnResponseNotFound() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        // when
        ResponseEntity<?> response = sessionController.save("3");

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testSaveSession_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = sessionController.save("nonNumericId");
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testDeleteSession_shouldReturnResponseOk() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        // when
        ResponseEntity<?> response = sessionController.save(String.valueOf(realSession1.getId()));

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testDeleteSession_SessionDoesNotExist_shouldReturnResponseNotFound() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        // when
        ResponseEntity<?> response = sessionController.save("3");

        // then
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testDeleteSession_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = sessionController.save("nonNumericId");
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testPartecipateSession_shouldReturnResponseOk() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        User realUser1 = new User();
        realUser1.setId(1L);
        realUser1.setFirstName("Mario");
        realUser1.setLastName("Rossi");
        realUser1.setEmail("mario@gmail.com");
        realUser1.setPassword("password");
        realUser1.setCreatedAt(LocalDateTime.now());
        realUser1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(realUser1);

        // when
        ResponseEntity<?> response = sessionController.participate("1", "1");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testPartecipateSession_SessionDoesNotExist_shouldReturnResponseNotFound() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        User realUser1 = new User();
        realUser1.setId(1L);
        realUser1.setFirstName("Mario");
        realUser1.setLastName("Rossi");
        realUser1.setEmail("mario@mail.com");
        realUser1.setPassword("password");
        realUser1.setCreatedAt(LocalDateTime.now());
        realUser1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(realUser1);

        // when
        ResponseEntity<?> response = sessionController.participate("asdfg", "1");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void testPartecipateSession_UserDoesNotExist_shouldReturnResponseNotFound() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        User realUser1 = new User();
        realUser1.setId(1L);
        realUser1.setFirstName("Mario");
        realUser1.setLastName("Rossi");
        realUser1.setEmail("mario@mail.com");
        realUser1.setPassword("password");
        realUser1.setCreatedAt(LocalDateTime.now());
        realUser1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(realUser1);

        // when
        ResponseEntity<?> response = sessionController.participate("1", "asdfg");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

    }

    @Test
    void testPartecipateSession_InvalidId_shouldReturnBadRequest() {
        // given // when
        ResponseEntity<?> response = sessionController.participate("nonNumericId", "1");
        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testNoLongerPartecipateSession_shouldReturnResponseOk() {
        // given
        Session realSession1 = new Session();
        realSession1.setId(1L);
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionRepository.save(realSession2);

        User realUser1 = new User();
        realUser1.setId(1L);
        realUser1.setFirstName("Mario");
        realUser1.setLastName("Rossi");
        realUser1.setEmail("mario@mail.com");
        realUser1.setPassword("password");
        realUser1.setCreatedAt(LocalDateTime.now());
        realUser1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(realUser1);

        sessionController.participate("1", "1");

        // when
        ResponseEntity<?> response = sessionController.noLongerParticipate(realSession1.getId().toString(), "1");

        // then
        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    void testNoLongerPartecipateSession_SessionDoesNotExist_shouldReturnResponseNotFound() {
        // given
        Session realSession1 = new Session();
        realSession1.setName("Mario");
        realSession1.setDate(new Date("2021/01/01"));
        realSession1.setDescription("mySession");
        realSession1.setCreatedAt(LocalDateTime.now());
        realSession1.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession1);
        sessionRepository.save(realSession1);

        Session realSession2 = new Session();
        realSession2.setName("Luigi");
        realSession2.setDate(new Date("2021/01/01"));
        realSession2.setDescription("mySession");
        realSession2.setCreatedAt(LocalDateTime.now());
        realSession2.setUpdatedAt(LocalDateTime.now());
        sessionService.findAll().add(realSession2);
        sessionRepository.save(realSession2);

        User realUser1 = new User();
        realUser1.setId(1L);
        realUser1.setFirstName("Mario");
        realUser1.setLastName("Rossi");
        realUser1.setEmail("mario@mail.com");
        realUser1.setPassword("password");
        realUser1.setCreatedAt(LocalDateTime.now());
        realUser1.setUpdatedAt(LocalDateTime.now());
        userRepository.save(realUser1);

        // when
        ResponseEntity<?> response = sessionController.noLongerParticipate("asdfg", "1");

        // then
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

}
