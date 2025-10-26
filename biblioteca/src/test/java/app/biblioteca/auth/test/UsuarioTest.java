package app.biblioteca.auth.test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.GrantedAuthority;

import app.biblioteca.auth.Usuario;
import app.biblioteca.entity.Reserva;

class UsuarioTest {

    private Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");
        usuario.setEmail("joao@email.com");
        usuario.setPassword("senha123");
        usuario.setRole("USER");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Verifica getters e setters básicos")
    void testGettersAndSetters() {
        assertEquals(1L, usuario.getId());
        assertEquals("João da Silva", usuario.getNome());
        assertEquals("joao@email.com", usuario.getEmail());
        assertEquals("senha123", usuario.getPassword());
        assertEquals("USER", usuario.getRole());

        // Test setting new values
        usuario.setNome("Maria");
        usuario.setEmail("maria@email.com");
        usuario.setPassword("novaSenha");
        usuario.setRole("ADMIN");

        assertEquals("Maria", usuario.getNome());
        assertEquals("maria@email.com", usuario.getEmail());
        assertEquals("novaSenha", usuario.getPassword());
        assertEquals("ADMIN", usuario.getRole());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Verifica implementação UserDetails: getUsername e getPassword")
    void testUserDetailsGetters() {
        assertEquals("joao@email.com", usuario.getUsername());
        assertEquals("senha123", usuario.getPassword());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Verifica autoridades do usuário")
    void testAuthorities() {
        List<GrantedAuthority> authorities = (List<GrantedAuthority>) usuario.getAuthorities();
        assertEquals(1, authorities.size());
        assertEquals("ROLE_USER", authorities.get(0).getAuthority());

        // Alter role and test again
        usuario.setRole("ADMIN");
        authorities = (List<GrantedAuthority>) usuario.getAuthorities();
        assertEquals("ROLE_ADMIN", authorities.get(0).getAuthority());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Verifica métodos de UserDetails de conta ativa")
    void testAccountStatusMethods() {
        assertTrue(usuario.isAccountNonExpired());
        assertTrue(usuario.isAccountNonLocked());
        assertTrue(usuario.isCredentialsNonExpired());
        assertTrue(usuario.isEnabled());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Verifica associação com reservas")
    void testReservasAssociation() {
        Reserva reserva1 = new Reserva();
        Reserva reserva2 = new Reserva();

        usuario.setReservas(List.of(reserva1, reserva2));
        List<Reserva> reservas = usuario.getReservas();

        assertEquals(2, reservas.size());
        assertTrue(reservas.contains(reserva1));
        assertTrue(reservas.contains(reserva2));
    }
}
