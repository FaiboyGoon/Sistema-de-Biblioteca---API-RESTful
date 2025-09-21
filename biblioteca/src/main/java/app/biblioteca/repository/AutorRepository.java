package app.biblioteca.repository;


import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import app.biblioteca.entity.Autor;

public interface AutorRepository extends JpaRepository<Autor, Long>{
	List<Autor> findByNomeContainingIgnoreCase(String nome);
	
	@Transactional
	long deleteByNome(String nome);
}
