package app.biblioteca.config.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;

import app.biblioteca.auth.Usuario;
import app.biblioteca.config.JwtServiceGenerator;

@ExtendWith(MockitoExtension.class)
class JwtServiceGeneratorTest {

    JwtServiceGenerator jwtService;
    Usuario usuario;
    UserDetails userDetails;

    @BeforeEach
    void setup() {
        jwtService = new JwtServiceGenerator();
        jwtService.SECRET_KEY = "f8d3bbacbf87c16f707b6c29ef2b1da36f91a03d315f5f0fdc9de4c441a52f89";

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");
        usuario.setEmail("joao@email.com");
        usuario.setPassword("senha123");
        usuario.setRole("USER");

        userDetails = new UserDetails() {
            @Override
            public String getUsername() { return "joao@email.com"; }
            @Override public String getPassword() { return null; }
            @Override public boolean isEnabled() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
                return java.util.Collections.emptyList();
            }
        };
    }

    @Test
    @DisplayName("TESTE DE UNIDADE -- Gerar payload deve conter campos corretos")
    void testGerarPayload() {
        Map<String, Object> payload = jwtService.gerarPayload(usuario);

        assertEquals("joao@email.com", payload.get("username"));
        assertEquals("1", payload.get("id"));
        assertEquals("USER", payload.get("role"));
        assertEquals("joao@email.com", payload.get("email"));
        assertEquals("João da Silva", payload.get("nome"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Gerar token e extrair username")
    void testGenerateTokenAndExtractUsername() {
        String token = jwtService.generateToken(usuario);
        assertNotNull(token);

        String username = jwtService.extractUsername(token);
        assertEquals("joao@email.com", username);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Extrair claims do token")
    void testExtractClaim() {
        String token = jwtService.generateToken(usuario);

        String email = jwtService.extractClaim(token, claims -> claims.get("email", String.class));
        assertEquals("joao@email.com", email);

        String role = jwtService.extractClaim(token, claims -> claims.get("role", String.class));
        assertEquals("USER", role);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Token válido para UserDetails correto")
    void testIsTokenValid_Success() {
        String token = jwtService.generateToken(usuario);
        boolean isValid = jwtService.isTokenValid(token, userDetails);
        assertTrue(isValid);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Token inválido para UserDetails diferente")
    void testIsTokenValid_Failure() {
        String token = jwtService.generateToken(usuario);

        UserDetails otherUser = new UserDetails() {
            @Override
            public String getUsername() { return "maria@email.com"; }
            @Override public String getPassword() { return null; }
            @Override public boolean isEnabled() { return true; }
            @Override public boolean isCredentialsNonExpired() { return true; }
            @Override public boolean isAccountNonLocked() { return true; }
            @Override public boolean isAccountNonExpired() { return true; }
            @Override public java.util.Collection<org.springframework.security.core.GrantedAuthority> getAuthorities() {
                return java.util.Collections.emptyList();
            }
        };

        boolean isValid = jwtService.isTokenValid(token, otherUser);
        assertFalse(isValid);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Token expirado deve ser inválido")
    void testIsTokenExpired() {
        JwtServiceGenerator shortExpiryJwt = new JwtServiceGenerator() {
        	{
                this.SECRET_KEY = jwtService.SECRET_KEY; // copy key
            }
            @Override
            public String generateToken(Usuario usuario) {
                Map<String, Object> payloadData = this.gerarPayload(usuario);
                return io.jsonwebtoken.Jwts.builder()
                        .setClaims(payloadData)
                        .setSubject(usuario.getUsername())
                        .setIssuedAt(new Date(System.currentTimeMillis()))
                        .setExpiration(new Date(System.currentTimeMillis() - 1000)) // expired
                        .signWith(getSigningKey(), ALGORITMO_ASSINATURA)
                        .compact();
            }
        };

        String token = shortExpiryJwt.generateToken(usuario);

        assertThrows(io.jsonwebtoken.ExpiredJwtException.class, () -> {
            shortExpiryJwt.isTokenValid(token, userDetails);
        });
    }
}
