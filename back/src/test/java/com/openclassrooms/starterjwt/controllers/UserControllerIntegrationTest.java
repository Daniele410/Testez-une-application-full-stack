package com.openclassrooms.starterjwt.controllers;

import com.openclassrooms.starterjwt.models.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@SpringBootTest
@AutoConfigureMockMvc
class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;



    @Test
    @WithMockUser
    void findById_ValidId_ReturnsOk() throws Exception {
        // given
        long userId = 1L;

        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", userId))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isOk())
                // then
                .andExpect(MockMvcResultMatchers
                        .content()
                        .contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    @WithMockUser
    void findById_InvalidId_ReturnsBadRequest() throws Exception {
        // given
        String invalidUserId = "nonNumericId";
        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", invalidUserId))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isBadRequest())
                // then
                .andExpect(MockMvcResultMatchers.status().isBadRequest());
    }

    @Test
    @WithMockUser
    void findById_InvalidId_ReturnsNotFound() throws Exception {
        // given
        User user = null;
        // when
        mockMvc.perform(MockMvcRequestBuilders.get("/api/user/{id}", user))
                .andExpect(MockMvcResultMatchers
                        .status()
                        .isNotFound())
                // then
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    void deleteById_ValidIdAndUnauthorizedUser_ReturnsUnauthorized() throws Exception {
        // given // when

        long userId = 1L;
        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    @WithMockUser(username = "testUser", password = "password", roles = "USER")
    void deleteById_shouldReturnUnauthorized() throws Exception {
        // given // when
        long userId = 1L;
        // then
        mockMvc.perform(MockMvcRequestBuilders.delete("/api/user/{id}", userId))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}