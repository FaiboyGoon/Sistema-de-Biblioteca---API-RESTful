package app.biblioteca.auth.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.biblioteca.auth.Login;
import app.biblioteca.auth.LoginController;
import app.biblioteca.auth.LoginService;
import app.biblioteca.config.JwtAuthenticationFilter;
import app.biblioteca.config.JwtServiceGenerator;

@ExtendWith(SpringExtension.class)
@WebMvcTest(LoginController.class)
@AutoConfigureMockMvc(addFilters = false)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;
    
    @MockitoBean
    JwtServiceGenerator jwtServiceGenerator;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockitoBean
    private LoginService loginService;

    private Login login;

    @BeforeEach
    void setup() {
        login = new Login();
        login.setUsername("joao@email.com");
        login.setPassword("senha123");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- POST /api/login retorna token com status 200")
    void testLogar_Success() throws Exception {
        when(loginService.logar(any(Login.class))).thenReturn("TOKEN123");

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(login)))
                .andExpect(status().isOk())
                .andExpect(content().string("TOKEN123"));

        verify(loginService, times(1)).logar(any(Login.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- POST /api/login lança exception se serviço falha")
    void testLogar_Failure() throws Exception {
        when(loginService.logar(any(Login.class))).thenThrow(new RuntimeException("Usuário não encontrado"));

        mockMvc.perform(post("/api/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(new ObjectMapper().writeValueAsString(login)))
        		.andExpect(status().isBadRequest());

        verify(loginService, times(1)).logar(any(Login.class));
    }
}
