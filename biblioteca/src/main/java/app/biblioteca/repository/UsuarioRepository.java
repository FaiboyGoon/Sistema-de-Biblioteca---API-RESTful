package app.biblioteca.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import app.biblioteca.auth.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long>{

	Usuario findByEmail(String email);
	List<Usuario> findByNome(String nome);
	
	@Modifying
	@Transactional
	@Query("UPDATE Usuario u SET u.password = :novaSenha WHERE id = :id")
	public void alterarSenha(String novaSenha, long id);

}
