package app.biblioteca.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.biblioteca.entity.Usuario;
import app.biblioteca.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;

	public String save(Usuario usuario) {
		this.usuarioRepository.save(usuario);
		return "o usuário: "+ usuario.getNome()+" foi salvo com sucesso!";
	}
	public String update(Usuario usuario, long id) {
		usuario.setId(id);
		this.usuarioRepository.save(usuario);
		return "o usuário: "+ usuario.getNome()+" de id: "+id+ " foi atualizado com sucesso!";
	}
	
	public String delete(long id) {
		this.usuarioRepository.deleteById(id);
		return "o usuário de id: " +id+ " foi deletado com sucesso!";
	}
	
	public List<Usuario> findAll(){
		List<Usuario> lista = this.usuarioRepository.findAll();
		return lista;
	}
	
	public Usuario findById(Long id) {
		Usuario usuario = this.usuarioRepository.findById(id).get();
		return usuario;
	}
	
	public Usuario findByEmail(String email){
		Usuario usuario = this.usuarioRepository.findByEmail(email);
		return usuario;
	}
	
	public List<Usuario> findByNome(String nome){
		List<Usuario> lista = this.usuarioRepository.findByNome(nome);
		return lista;
	}
}
