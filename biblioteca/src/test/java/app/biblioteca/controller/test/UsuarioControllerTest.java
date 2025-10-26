package app.biblioteca.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import app.biblioteca.auth.Usuario;
import app.biblioteca.config.JwtAuthenticationFilter;
import app.biblioteca.config.JwtServiceGenerator;
import app.biblioteca.controller.UsuarioController;
import app.biblioteca.service.UsuarioService;

@WebMvcTest(UsuarioController.class)
@AutoConfigureMockMvc(addFilters = false)
class UsuarioControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UsuarioService usuarioService;

    @MockitoBean
    JwtServiceGenerator jwtServiceGenerator;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    ObjectMapper objectMapper;

    Usuario usuario;

    @BeforeEach
    void setup() {
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João Silva");
        usuario.setEmail("joao@example.com");
        usuario.setPassword("");
        usuario.setRole("USER");
    }

    @Test
    @DisplayName("Salvar usuário com sucesso deve retornar 201 CREATED e mensagem")
    void testSaveUsuario() throws Exception {
        when(usuarioService.save(any(Usuario.class)))
            .thenReturn("o usuário de nome: João Silva foi salvo com sucesso!");

        mockMvc.perform(post("/api/usuarios/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isCreated())
                .andExpect(content().string("o usuário de nome: João Silva foi salvo com sucesso!"));
    }

    @Test
    @DisplayName("Salvar usuário com campos inválidos deve retornar 400 Bad Request")
    void testSaveUsuarioValidationError() throws Exception {
        Usuario invalid = new Usuario();

        mockMvc.perform(post("/api/usuarios/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.nome").value("O nome de usuário é obrigatório"))
                .andExpect(jsonPath("$.errors.email").value("O e-mail do usuário é obrigatório"))
                .andExpect(jsonPath("$.errors.password").value("A senha é obrigatória"));
    }

    @Test
    @DisplayName("Atualizar usuário com sucesso deve retornar 200 OK e mensagem")
    void testUpdateUsuario() throws Exception {
        when(usuarioService.update(any(Usuario.class), eq(1L)))
            .thenReturn("o usuário: João Silva de id: 1 foi atualizado com sucesso!");

        mockMvc.perform(put("/api/usuarios/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isOk())
                .andExpect(content().string("o usuário: João Silva de id: 1 foi atualizado com sucesso!"));
    }

    @Test
    @DisplayName("Atualizar usuário com dados inválidos deve retornar 400 Bad Request")
    void testUpdateUsuarioValidationError() throws Exception {
        Usuario invalid = new Usuario();

        mockMvc.perform(put("/api/usuarios/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalid)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.errors.nome").value("O nome de usuário é obrigatório"))
                .andExpect(jsonPath("$.errors.email").value("O e-mail do usuário é obrigatório"))
                .andExpect(jsonPath("$.errors.password").value("A senha é obrigatória"));
    }

    @Test
    @DisplayName("Deletar usuário deve retornar 200 OK e mensagem")
    void testDeleteUsuario() throws Exception {
        when(usuarioService.delete(1L))
            .thenReturn("o usuário de id: 1 foi deletado com sucesso!");

        mockMvc.perform(delete("/api/usuarios/deleteById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("o usuário de id: 1 foi deletado com sucesso!"));
    }

    @Test
    @DisplayName("Buscando todos os usuários deve retornar uma lista")
    void testFindAllUsuarios() throws Exception {
        when(usuarioService.findAll()).thenReturn(Arrays.asList(usuario));

        mockMvc.perform(get("/api/usuarios/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].nome").value("João Silva"))
                .andExpect(jsonPath("$[0].password").value(""));
    }

    @Test
    @DisplayName("Buscar usuário por ID deve retornar o usuário")
    void testFindById() throws Exception {
        when(usuarioService.findById(1L)).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/findById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.nome").value("João Silva"))
                .andExpect(jsonPath("$.password").value(""));
    }

    @Test
    @DisplayName("Buscar usuário por email deve retornar o usuário daquele email")
    void testFindByEmail() throws Exception {
        when(usuarioService.findByEmail("joao@example.com")).thenReturn(usuario);

        mockMvc.perform(get("/api/usuarios/findByEmail")
                .param("email", "joao@example.com"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Silva"));
    }

    @Test
    @DisplayName("Buscar usuários por nome deve retornar uma lista filtrada")
    void testFindByNome() throws Exception {
        when(usuarioService.findByNome("João")).thenReturn(Arrays.asList(usuario));

        mockMvc.perform(get("/api/usuarios/findByNome")
                .param("nome", "João"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nome").value("João Silva"));
    }

    @Test
    @DisplayName("Lista de usuários vazia deve retornar array vazio")
    void testFindAllEmptyList() throws Exception {
        when(usuarioService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/usuarios/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Buscar usuário inexistente deve retornar 404 Not Found")
    void testFindByIdNotFound() throws Exception {
        when(usuarioService.findById(999L)).thenThrow(new NoSuchElementException("Usuário não encontrado"));

        mockMvc.perform(get("/api/usuarios/findById/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Objeto não encontrado"));
    }

    @Test
    @DisplayName("Salvar usuário com email duplicado deve retornar 400 Bad Request")
    void testDataIntegrityViolationException() throws Exception {
        DataIntegrityViolationException exception = new DataIntegrityViolationException(
            "could not execute statement",
            new RuntimeException("Duplicate entry 'joao@example.com' for key 'email'")
        );

        when(usuarioService.save(any(Usuario.class))).thenThrow(exception);

        mockMvc.perform(post("/api/usuarios/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuario)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Usuário com este email já existe"));
    }
}
