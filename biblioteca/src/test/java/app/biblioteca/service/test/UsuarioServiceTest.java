package app.biblioteca.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import app.biblioteca.auth.Usuario;
import app.biblioteca.repository.UsuarioRepository;
import app.biblioteca.service.UsuarioService;

@ExtendWith(MockitoExtension.class)
class UsuarioServiceTest {

    @Mock
    UsuarioRepository usuarioRepository;

    @Mock
    BCryptPasswordEncoder bcryptEncoder;

    @InjectMocks
    UsuarioService usuarioService;

    Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");
        usuario.setEmail("joao@email.com");
        usuario.setPassword("senha123");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando criar usuário, deve retornar mensagem de sucesso")
    void testSave() {
        when(bcryptEncoder.encode(usuario.getPassword())).thenReturn("hashedSenha");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String result = usuarioService.save(usuario);
        assertEquals("o usuário de nome: João da Silva foi salvo com sucesso!", result);
        assertEquals("hashedSenha", usuario.getPassword());

        verify(bcryptEncoder, times(1)).encode("senha123");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando atualizar usuário com nova senha, deve retornar mensagem de sucesso")
    void testUpdate_WithPassword() {
        usuario.setPassword("novaSenha");
        when(bcryptEncoder.encode("novaSenha")).thenReturn("hashedNovaSenha");
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String result = usuarioService.update(usuario, 1L);
        assertEquals("o usuário: João da Silva de id: 1 foi atualizado com sucesso!", result);
        assertEquals("hashedNovaSenha", usuario.getPassword());

        verify(bcryptEncoder, times(1)).encode("novaSenha");
        verify(usuarioRepository, times(1)).save(usuario);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando atualizar usuário sem nova senha, deve manter senha anterior")
    void testUpdate_WithoutPassword() {
        usuario.setPassword("");
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));
        when(usuarioRepository.save(any(Usuario.class))).thenReturn(usuario);

        String result = usuarioService.update(usuario, 1L);
        assertEquals("o usuário: João da Silva de id: 1 foi atualizado com sucesso!", result);

        verify(usuarioRepository, times(1)).findById(1L);
        verify(usuarioRepository, times(1)).save(usuario);
        verify(bcryptEncoder, times(0)).encode(any());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando deletar usuário, deve retornar mensagem de sucesso")
    void testDelete() {
        doNothing().when(usuarioRepository).deleteById(1L);

        String result = usuarioService.delete(1L);
        assertEquals("o usuário de id: 1 foi deletado com sucesso!", result);

        verify(usuarioRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar todos usuários, senhas devem ser limpas")
    void testFindAll() {
        Usuario u2 = new Usuario();
        u2.setId(2L);
        u2.setNome("Maria");
        u2.setPassword("outraSenha");

        when(usuarioRepository.findAll()).thenReturn(Arrays.asList(usuario, u2));

        List<Usuario> result = usuarioService.findAll();
        assertEquals(2, result.size());
        assertEquals("", result.get(0).getPassword());
        assertEquals("", result.get(1).getPassword());

        verify(usuarioRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar usuário por ID existente")
    void testFindById_Found() {
        when(usuarioRepository.findById(1L)).thenReturn(Optional.of(usuario));

        Usuario result = usuarioService.findById(1L);
        assertEquals("João da Silva", result.getNome());
        assertEquals("", result.getPassword());

        verify(usuarioRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar usuário por ID inexistente, deve retornar null")
    void testFindById_NotFound() {
        when(usuarioRepository.findById(2L)).thenReturn(Optional.empty());

        Usuario result = usuarioService.findById(2L);
        assertNull(result);

        verify(usuarioRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar usuário por email deve retornar um usuário")
    void testFindByEmail() {
        when(usuarioRepository.findByEmail("joao@email.com")).thenReturn(usuario);

        Usuario result = usuarioService.findByEmail("joao@email.com");
        assertEquals("João da Silva", result.getNome());

        verify(usuarioRepository, times(1)).findByEmail("joao@email.com");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar usuário por nome deve retornar uma lista de usuários")
    void testFindByNome() {
        Usuario u2 = new Usuario();
        u2.setId(2L);
        u2.setNome("João da Silva");

        when(usuarioRepository.findByNome("João da Silva")).thenReturn(Arrays.asList(usuario, u2));

        List<Usuario> result = usuarioService.findByNome("João da Silva");
        assertEquals(2, result.size());

        verify(usuarioRepository, times(1)).findByNome("João da Silva");
    }
}
