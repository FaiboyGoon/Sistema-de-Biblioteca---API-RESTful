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

import app.biblioteca.entity.Autor;
import app.biblioteca.service.AutorService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/autores")
public class AutorController {
	@Autowired
	private AutorService autorService;
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Autor autor) {
		try {
			String mensagem = this.autorService.save(autor);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao salvar o Autor! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Autor autor,@PathVariable long id) {
		try {
			String mensagem = this.autorService.update(autor, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao atualizar o Autor! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
		try {
			String mensagem = this.autorService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao deletar o Autor! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	@DeleteMapping("/deleteByNome")
	public ResponseEntity<String> deleteByNome(@RequestParam String nome) {
		try {
			String mensagem = this.autorService.deleteByNome(nome);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<>("Erro ao deletar o Autor! Erro: " + e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
		try {
			List<Autor> lista = this.autorService.findAll();
			return new ResponseEntity<List<Autor>>(lista, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
		try {
			Autor autor = this.autorService.findById(id);
			return new ResponseEntity<Autor>(autor, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	@GetMapping("/findByNome")
	public ResponseEntity<?> findByNomeContainingIgnoreCase(@RequestParam String nome){
		try {
			Autor autor = this.autorService.findByNomeContainingIgnoreCase(nome);
			return new ResponseEntity<Autor>(autor, HttpStatus.OK);
		}catch(Exception e) {
			return new ResponseEntity<String>(e.getMessage(), HttpStatus.BAD_REQUEST);
		}
	}
	
	
}
