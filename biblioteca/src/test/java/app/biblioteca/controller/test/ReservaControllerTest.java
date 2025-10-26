package app.biblioteca.controller.test;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.time.LocalDate;
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
import app.biblioteca.controller.ReservaController;
import app.biblioteca.entity.Autor;
import app.biblioteca.entity.Livro;
import app.biblioteca.entity.Reserva;
import app.biblioteca.exception.ReservationFullException;
import app.biblioteca.service.LivroService;
import app.biblioteca.service.ReservaService;

@WebMvcTest(ReservaController.class)
@AutoConfigureMockMvc(addFilters = false)
class ReservaControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    ReservaService reservaService;
    
    @MockitoBean
    LivroService livroService;

    @MockitoBean
    JwtServiceGenerator jwtServiceGenerator;

    @MockitoBean
    JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    ObjectMapper objectMapper;

    Reserva reserva;
    Livro livro;
    Usuario usuario;
    Autor autor;

    @BeforeEach
    void setup() {
    	autor = new Autor();
    	autor.setId(1L);
    	autor.setNome("Miguel de Cervantes");
    	
        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Dom Quixote");
        livro.setAutor(autor);
        
        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João");
        usuario.setEmail("joao@email.com");
        usuario.setPassword("123456");
        
        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setLivro(livro);
        reserva.setDataReserva(LocalDate.of(2025, 10, 26));
        reserva.setUsuario(usuario);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Salvar reserva com sucesso deve retornar status 201 CREATED e mensagem de sucesso")
    void testSaveReserva() throws Exception {
    	when(reservaService.save(any(Reserva.class))).thenReturn("A reserva no dia: " + reserva.getDataReserva() + " foi criada com sucesso!");


        mockMvc.perform(post("/api/reservas/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isCreated())
                .andExpect(content().string("A reserva no dia: 2025-10-26 foi criada com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Atualizar reserva deve retornar mensagem de sucesso e status 200 OK")
    void testUpdateReserva() throws Exception {
        when(reservaService.update(any(Reserva.class), eq(1L)))
            .thenReturn("a reserva no dia:2025-10-26 foi atualizada com sucesso!");

        mockMvc.perform(put("/api/reservas/update/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isOk())
                .andExpect(content().string("a reserva no dia:2025-10-26 foi atualizada com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Deletar reserva deve retornar status 200 OK e mensagem de sucesso")
    void testDeleteReserva() throws Exception {
        when(reservaService.delete(1L))
            .thenReturn("a reserva de id:1 foi deletada com sucesso!");

        mockMvc.perform(delete("/api/reservas/deleteById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(content().string("a reserva de id:1 foi deletada com sucesso!"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Buscar todas as reservas deve retornar uma lista de todas as reservas")
    void testFindAllReservas() throws Exception {
        when(reservaService.findAll()).thenReturn(Arrays.asList(reserva));

        mockMvc.perform(get("/api/reservas/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].livro.titulo").value("Dom Quixote"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Buscar reserva pelo id dela, deve retornar a reserva")
    void testFindById() throws Exception {
        when(reservaService.findById(1L)).thenReturn(reserva);

        mockMvc.perform(get("/api/reservas/findById/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.livro.titulo").value("Dom Quixote"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Buscar reservas após uma data, deve retornar lista filtrada")
    void testFindByDataReservaAfter() throws Exception {
        when(reservaService.findByDataReservaAfter(LocalDate.of(2025, 1, 1)))
            .thenReturn(Arrays.asList(reserva));

        mockMvc.perform(get("/api/reservas/findByDataReservaAfter")
                .param("data", "2025-01-01"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].livro.titulo").value("Dom Quixote"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Buscar reservas antes de uma data, deve retornar lista filtrada")
    void testFindByDataReservaBefore() throws Exception {
        when(reservaService.findByDataReservaBefore(LocalDate.of(2025, 12, 31)))
            .thenReturn(Arrays.asList(reserva));

        mockMvc.perform(get("/api/reservas/findByDataReservaBefore")
                .param("data", "2025-12-31"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].livro.titulo").value("Dom Quixote"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Lista de reservas vazia deve retornar lista vazia")
    void testFindAllEmptyList() throws Exception {
        when(reservaService.findAll()).thenReturn(Collections.emptyList());

        mockMvc.perform(get("/api/reservas/findAll"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Buscar reserva inexistente deve retornar status 404 Not Found e mensagem de erro")
    void testFindByIdNotFound() throws Exception {
        when(reservaService.findById(999L)).thenThrow(new NoSuchElementException("Reserva não encontrada"));

        mockMvc.perform(get("/api/reservas/findById/{id}", 999L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Objeto não encontrado"));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que tenta reservar quando as reservas de um livro já estão cheias, deve retornar status 409 Conflict e mensagem avisando que o livro já alcançou o limite de reservas")
    void testReservationFullException() throws Exception {
    	when(livroService.verificarDisponibilidade(anyLong())).thenReturn(false);
    	
        when(reservaService.save(any(Reserva.class)))
            .thenThrow(new ReservationFullException("O livro já possui muitas reservas, mais reservas não serão possíveis"));

        mockMvc.perform(post("/api/reservas/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(reserva)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("O livro já possui muitas reservas, mais reservas não serão possíveis"));
    }

}