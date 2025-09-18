package app.biblioteca.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin("*")
public class AutorController {
	@Autowired
	private AutorService autorService;
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Autor autor) {
			String mensagem = this.autorService.save(autor);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Autor autor,@PathVariable long id) {
			String mensagem = this.autorService.update(autor, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
			String mensagem = this.autorService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	@DeleteMapping("/deleteByNome")
	public ResponseEntity<String> deleteByNome(@RequestParam String nome) {
			String mensagem = this.autorService.deleteByNome(nome);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
			List<Autor> lista = this.autorService.findAll();
			return new ResponseEntity<List<Autor>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
			Autor autor = this.autorService.findById(id);
			return new ResponseEntity<Autor>(autor, HttpStatus.OK);
	}
	
	@GetMapping("/findByNome")
	public ResponseEntity<?> findByNomeContainingIgnoreCase(@RequestParam String nome){
			Autor autor = this.autorService.findByNomeContainingIgnoreCase(nome);
			return new ResponseEntity<Autor>(autor, HttpStatus.OK);
	}
	
	
}
