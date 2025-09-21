package app.biblioteca.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import app.biblioteca.auth.Usuario;
import app.biblioteca.repository.UsuarioRepository;

@Service
public class UsuarioService {
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private BCryptPasswordEncoder bcryptEncoder;

	public String save(Usuario usuario) {
		String senhaCriptografada = bcryptEncoder.encode(usuario.getPassword());
		usuario.setPassword(senhaCriptografada);
		
		this.usuarioRepository.save(usuario);
		return "o usuário de nome: "+ usuario.getNome()+ " foi salvo com sucesso!";
	}
	public String update(Usuario usuario, long id) {
		usuario.setId(id);
		
		if(!usuario.getPassword().equals("")) {
			String senhaCriptografada = bcryptEncoder.encode(usuario.getPassword());
			usuario.setPassword(senhaCriptografada);
		}else {
			String senhaCriptografada = this.findSenhaCriptografada(id);
			usuario.setPassword(senhaCriptografada);
		}
		
		this.usuarioRepository.save(usuario);
		return "o usuário: "+ usuario.getNome()+" de id: "+id+ " foi atualizado com sucesso!";
	}
	
	public String delete(long id) {
		this.usuarioRepository.deleteById(id);
		return "o usuário de id: " +id+ " foi deletado com sucesso!";
	}
	
	public List<Usuario> findAll(){
		List<Usuario> lista = this.usuarioRepository.findAll();
		for(int i=0; i<lista.size(); i++) {
			lista.get(i).setPassword("");
		}
		return lista;
	}
	
	
	public Usuario findById(Long id) {
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		if(usuario.isPresent()) {
			Usuario user = usuario.get();
			user.setPassword("");
			return user;
		}
		else
			return null;
	}
	
	public Usuario findByEmail(String email){
		Usuario usuario = this.usuarioRepository.findByEmail(email);
		return usuario;
	}
	
	private String findSenhaCriptografada(long id) {
		Optional<Usuario> usuario = this.usuarioRepository.findById(id);
		if(usuario.isPresent()) {
			Usuario user = usuario.get();
			return user.getPassword();
		}
		else
			return null;
	}
	
	public List<Usuario> findByNome(String nome){
		List<Usuario> lista = this.usuarioRepository.findByNome(nome);
		return lista;
	}
}
