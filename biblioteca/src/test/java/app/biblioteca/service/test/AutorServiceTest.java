package app.biblioteca.service.test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.NoSuchElementException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import app.biblioteca.entity.Autor;
import app.biblioteca.repository.AutorRepository;
import app.biblioteca.service.AutorService;

@ExtendWith(MockitoExtension.class)
class AutorServiceTest {

    @Mock
    AutorRepository autorRepository;

    @InjectMocks
    AutorService autorService;

    Autor autor;

    @BeforeEach
    void setup() {
        autor = new Autor();
        autor.setId(1L);
        autor.setNome("Gabriel Garcia Marquez");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando criar um autor, deve retornar uma mensagem de sucesso")
    void testSave() {
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        String result = autorService.save(autor);
        assertEquals("o Autor: 'Gabriel Garcia Marquez' foi salvo com sucesso!", result);

        verify(autorRepository, times(1)).save(autor);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Testando atualizar autor, deve retornar uma mensagem de sucesso")
    void testUpdate() {
        when(autorRepository.save(any(Autor.class))).thenReturn(autor);

        String result = autorService.update(autor, 1L);
        assertEquals("o autor: 'Gabriel Garcia Marquez' de id: '1' foi atualizado com sucesso!", result);
        assertEquals(1L, autor.getId());

        verify(autorRepository, times(1)).save(autor);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa deletar um autor pelo ID, deve retornar uma mensagem de sucesso")
    void testDelete() {
        doNothing().when(autorRepository).deleteById(1L);

        String result = autorService.delete(1L);
        assertEquals("o autor de id: '1' foi deletado com sucesso!", result);

        verify(autorRepository, times(1)).deleteById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa deletar um autor pelo nome, deve retornar uma mensagem de sucesso")
    void testDeleteByNome() {
        when(autorRepository.deleteByNome("Gabriel Garcia Marquez")).thenReturn(1L);

        String result = autorService.deleteByNome("Gabriel Garcia Marquez");
        assertEquals("os autores de nome: 'Gabriel Garcia Marquez' foram deletados com sucesso!", result);

        verify(autorRepository, times(1)).deleteByNome("Gabriel Garcia Marquez");
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar todos os autores, deve retornar uma lista de autores")
    void testFindAll() {
        List<Autor> autores = Arrays.asList(autor);
        when(autorRepository.findAll()).thenReturn(autores);

        List<Autor> result = autorService.findAll();
        assertEquals(1, result.size());
        assertEquals("Gabriel Garcia Marquez", result.get(0).getNome());

        verify(autorRepository, times(1)).findAll();
    }
    
    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar um autor por ID, deve retornar um autor")
    void testFindById() {
        when(autorRepository.findById(1L)).thenReturn(Optional.of(autor));

        Autor result = autorService.findById(1L);
        assertEquals("Gabriel Garcia Marquez", result.getNome());

        verify(autorRepository, times(1)).findById(1L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar um autor por uma ID que não existe, deve lançar uma exception")
    void testFindById_NotFound() {
        when(autorRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> autorService.findById(2L));

        verify(autorRepository, times(1)).findById(2L);
    }

    @Test
    @DisplayName("TESTE DE INTEGRAÇÃO -- Cenário que testa procurar todos os autores por um nome, deve retornar uma lista filtrada de autores")
    void testFindByNomeContainingIgnoreCase() {
        List<Autor> autores = Arrays.asList(autor);
        when(autorRepository.findByNomeContainingIgnoreCase("Gabriel")).thenReturn(autores);

        List<Autor> result = autorService.findByNomeContainingIgnoreCase("Gabriel");
        assertEquals(1, result.size());
        assertEquals("Gabriel Garcia Marquez", result.get(0).getNome());

        verify(autorRepository, times(1)).findByNomeContainingIgnoreCase("Gabriel");
    }
}
