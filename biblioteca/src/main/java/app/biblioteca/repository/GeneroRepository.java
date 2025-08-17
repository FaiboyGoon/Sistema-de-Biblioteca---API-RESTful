package app.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import app.biblioteca.entity.Genero;

public interface GeneroRepository extends JpaRepository<Genero, Long>{
	List<Genero> findByNome(String nome);
	
	@Transactional
	long deleteByNome(String nome);
	
}
