package app.biblioteca.auth.test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import app.biblioteca.auth.Login;
import app.biblioteca.auth.LoginRepository;
import app.biblioteca.auth.LoginService;
import app.biblioteca.auth.Usuario;
import app.biblioteca.config.JwtServiceGenerator;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    LoginRepository loginRepository;

    @Mock
    JwtServiceGenerator jwtService;

    @Mock
    AuthenticationManager authenticationManager;

    @InjectMocks
    LoginService loginService;

    Login login;
    Usuario usuario;

    @BeforeEach
    void setup() {
        // Setup login request
        login = new Login();
        login.setUsername("joao@email.com");
        login.setPassword("senha123");

        // Setup mock user in repository
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");
        usuario.setEmail("joao@email.com");
        usuario.setPassword("senha123");
        usuario.setRole("USER");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Logar com usuário válido deve gerar token")
    void testLogar_Success() {
        when(loginRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("TOKEN123");

        Authentication auth = new UsernamePasswordAuthenticationToken("joao@email.com", "senha123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

        String token = loginService.logar(login);
        assertEquals("TOKEN123", token);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(loginRepository, times(1)).findByEmail("joao@email.com");
        verify(jwtService, times(1)).generateToken(usuario);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Gerar token chama AuthenticationManager e JwtService")
    void testGerarToken_Success() {
        when(loginRepository.findByEmail("joao@email.com")).thenReturn(Optional.of(usuario));
        when(jwtService.generateToken(usuario)).thenReturn("TOKEN123");

        Authentication auth = new UsernamePasswordAuthenticationToken("joao@email.com", "senha123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

        String token = loginService.gerarToken(login);
        assertEquals("TOKEN123", token);

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(loginRepository, times(1)).findByEmail("joao@email.com");
        verify(jwtService, times(1)).generateToken(usuario);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Gerar token com email não existente lança Exception")
    void testGerarToken_UserNotFound() {
        when(loginRepository.findByEmail("joao@email.com")).thenReturn(Optional.empty());

        Authentication auth = new UsernamePasswordAuthenticationToken("joao@email.com", "senha123");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(auth);

        assertThrows(RuntimeException.class, () -> loginService.gerarToken(login));

        verify(authenticationManager, times(1)).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(loginRepository, times(1)).findByEmail("joao@email.com");
        verify(jwtService, times(0)).generateToken(any(Usuario.class));
    }
}
