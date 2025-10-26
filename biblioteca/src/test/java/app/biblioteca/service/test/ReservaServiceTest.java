package app.biblioteca.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import app.biblioteca.entity.Livro;
import app.biblioteca.entity.Reserva;
import app.biblioteca.auth.Usuario;
import app.biblioteca.exception.ReservationFullException;
import app.biblioteca.repository.ReservaRepository;
import app.biblioteca.service.LivroService;
import app.biblioteca.service.ReservaService;

@ExtendWith(MockitoExtension.class)
class ReservaServiceTest {

    @Mock
    ReservaRepository reservaRepository;

    @Mock
    LivroService livroService;

    @InjectMocks
    ReservaService reservaService;

    Reserva reserva;
    Livro livro;
    Usuario usuario;

    @BeforeEach
    void setup() {

        usuario = new Usuario();
        usuario.setId(1L);
        usuario.setNome("João da Silva");

        livro = new Livro();
        livro.setId(1L);
        livro.setTitulo("Cem Anos de Solidão");

        reserva = new Reserva();
        reserva.setId(1L);
        reserva.setLivro(livro);
        reserva.setUsuario(usuario);
        reserva.setDataReserva(LocalDate.now());
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando criar uma reserva quando livro disponível, deve retornar sucesso")
    void testSave_Success() {
        when(livroService.verificarDisponibilidade(livro.getId())).thenReturn(true);
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        String result = reservaService.save(reserva);
        assertEquals("A reserva no dia: "+ reserva.getDataReserva() +" foi criada com sucesso!", result);

        verify(livroService, times(1)).verificarDisponibilidade(livro.getId());
        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando criar uma reserva quando livro não disponível, deve lançar ReservationFullException")
    void testSave_Failure_BookFull() {
        when(livroService.verificarDisponibilidade(livro.getId())).thenReturn(false);

        assertThrows(ReservationFullException.class, () -> reservaService.save(reserva));

        verify(livroService, times(1)).verificarDisponibilidade(livro.getId());
        verify(reservaRepository, times(0)).save(any(Reserva.class));
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando atualizar uma reserva, deve retornar mensagem de sucesso")
    void testUpdate() {
        when(reservaRepository.save(any(Reserva.class))).thenReturn(reserva);

        String result = reservaService.update(reserva, 1L);
        assertEquals("a reserva no dia:"+ reserva.getDataReserva() +" foi atualizada com sucesso!", result);
        assertEquals(1L, reserva.getId());

        verify(reservaRepository, times(1)).save(reserva);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando deletar uma reserva pelo ID, deve retornar mensagem de sucesso")
    void testDelete() {
        doNothing().when(reservaRepository).deleteById(1L);

        String result = reservaService.delete(1L);
        assertEquals("a reserva de id:1 foi deletada com sucesso!", result);

        verify(reservaRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar todas as reservas, deve retornar lista de reservas")
    void testFindAll() {
        List<Reserva> reservas = Arrays.asList(reserva);
        when(reservaRepository.findAll()).thenReturn(reservas);

        List<Reserva> result = reservaService.findAll();
        assertEquals(1, result.size());
        assertEquals(reserva.getId(), result.get(0).getId());

        verify(reservaRepository, times(1)).findAll();
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar reserva por ID, deve retornar uma reserva")
    void testFindById() {
        when(reservaRepository.findById(1L)).thenReturn(Optional.of(reserva));

        Reserva result = reservaService.findById(1L);
        assertEquals(reserva.getId(), result.getId());

        verify(reservaRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando procurar reserva por ID inexistente, deve lançar uma exception")
    void testFindById_NotFound() {
        when(reservaRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> reservaService.findById(2L));

        verify(reservaRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando buscar reservas depois de uma data, deve retornar lista filtrada")
    void testFindByDataReservaAfter() {
        List<Reserva> reservas = Arrays.asList(reserva);
        LocalDate data = LocalDate.now().minusDays(1);
        when(reservaRepository.findByDataReservaAfter(data)).thenReturn(reservas);

        List<Reserva> result = reservaService.findByDataReservaAfter(data);
        assertEquals(1, result.size());

        verify(reservaRepository, times(1)).findByDataReservaAfter(data);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando buscar reservas antes de uma data, deve retornar lista filtrada")
    void testFindByDataReservaBefore() {
        List<Reserva> reservas = Arrays.asList(reserva);
        LocalDate data = LocalDate.now().plusDays(1);
        when(reservaRepository.findByDataReservaBefore(data)).thenReturn(reservas);

        List<Reserva> result = reservaService.findByDataReservaBefore(data);
        assertEquals(1, result.size());

        verify(reservaRepository, times(1)).findByDataReservaBefore(data);
    }
}
