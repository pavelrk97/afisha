package com.afisha.main.compilation.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import com.afisha.main.compilation.dto.CompilationDto;
import com.afisha.main.compilation.dto.NewCompilationDto;
import com.afisha.main.compilation.service.CompilationService;
import com.afisha.main.config.SecurityConfig;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(CompilationAdminController.class)
@Import(SecurityConfig.class)
class CompilationAdminControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private CompilationService compilationService;

    @MockitoBean
    private JwtDecoder jwtDecoder;

    @Test
    void create_whenNoToken_thenUnauthorized() throws Exception {
        NewCompilationDto input = NewCompilationDto.builder().title("Топ").build();

        mockMvc.perform(post("/admin/compilations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isUnauthorized());
    }

    @Test
    void create_whenUserRole_thenForbidden() throws Exception {
        NewCompilationDto input = NewCompilationDto.builder().title("Топ").build();

        mockMvc.perform(post("/admin/compilations")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_USER")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isForbidden());
    }

    @Test
    void create_whenAdminRole_thenCreated() throws Exception {
        NewCompilationDto input = NewCompilationDto.builder().title("Топ концерты").build();
        CompilationDto returned = CompilationDto.builder()
                .id(1L)
                .title("Топ концерты")
                .pinned(false)
                .build();
        when(compilationService.create(any(NewCompilationDto.class))).thenReturn(returned);

        mockMvc.perform(post("/admin/compilations")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.title").value("Топ концерты"));
    }

    @Test
    void create_whenBlankTitle_thenBadRequest() throws Exception {
        NewCompilationDto input = NewCompilationDto.builder().title("").build();

        mockMvc.perform(post("/admin/compilations")
                        .with(jwt().authorities(new SimpleGrantedAuthority("ROLE_ADMIN")))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(input)))
                .andExpect(status().isBadRequest());
    }
}