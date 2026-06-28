package com.afisha.main.request.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.afisha.main.config.SecurityConfig;
import com.afisha.main.enums.RequestStatus;
import com.afisha.main.request.dto.ParticipationRequestDto;
import com.afisha.main.request.service.EventRequestService;

import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(EventRequestPrivateController.class)
@Import(SecurityConfig.class)
class EventRequestPrivateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private EventRequestService eventRequestService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void create_whenNoToken_thenUnauthorized() throws Exception {
        mockMvc.perform(post("/users/1/requests").param("eventId", "10"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_whenAdminRoleOnly_thenForbidden() throws Exception {
        mockMvc.perform(post("/users/1/requests")
                        .param("eventId", "10")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN"))))
                .andExpect(status().isForbidden());
    }

    @Test
    void create_whenUserRole_thenCreated() throws Exception {
        ParticipationRequestDto returned = ParticipationRequestDto.builder()
                .id(100L).event(10L).requester(1L).status(RequestStatus.CONFIRMED).build();
        when(eventRequestService.create(1L, 10L)).thenReturn(returned);

        mockMvc.perform(post("/users/1/requests")
                        .param("eventId", "10")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(100));
    }

    @Test
    void create_whenEventIdMissing_thenBadRequest() throws Exception {
        mockMvc.perform(post("/users/1/requests")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER"))))
                .andExpect(status().isBadRequest());
    }
}
