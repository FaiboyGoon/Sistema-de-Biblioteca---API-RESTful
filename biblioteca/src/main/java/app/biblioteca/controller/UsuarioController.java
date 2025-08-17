package app.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import app.biblioteca.entity.Usuario;
import app.biblioteca.service.UsuarioService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {
	@Autowired
	private UsuarioService usuarioService;
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Usuario usuario) {
		try {
			String mensagem = this.usuarioService.save(usuario);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao criar o Usuario! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Usuario usuario,@PathVariable long id) {
		try {
			String mensagem = this.usuarioService.update(usuario, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao atualizar o Usuario! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
		try {
			String mensagem = this.usuarioService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao deletar o Usuario! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		try {
			List<Usuario> lista = this.usuarioService.findAll();
			return new ResponseEntity<List<Usuario>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
		try {
			Usuario usuario = this.usuarioService.findById(id);
			return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByEmail")
	public ResponseEntity<?> findByEmail(@RequestParam String email){
		try {
			Usuario usuario = this.usuarioService.findByEmail(email);
			return new ResponseEntity<Usuario>(usuario, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByNome")
	public ResponseEntity<?> findByNome(@RequestParam String nome){
		try {
			List<Usuario> lista = this.usuarioService.findByNome(nome);
			return new ResponseEntity<List<Usuario>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
}
