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

import app.biblioteca.entity.Genero;
import app.biblioteca.service.GeneroService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/generos")
@CrossOrigin("*")
public class GeneroController {
	@Autowired
	private GeneroService generoService;
	
	@PostMapping("/save")
	public ResponseEntity<String> save(@Valid @RequestBody Genero genero) {
			String mensagem = this.generoService.save(genero);
			return new ResponseEntity<>(mensagem, HttpStatus.CREATED);
	}
	
	@PutMapping("/update/{id}")
	public ResponseEntity<String> update(@Valid @RequestBody Genero genero,@PathVariable long id) {
			String mensagem = this.generoService.update(genero, id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteById/{id}")
	public ResponseEntity<String> delete(@PathVariable long id) {
			String mensagem = this.generoService.delete(id);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	
	@DeleteMapping("/deleteByNome")
	public ResponseEntity<String> deleteByNome(@RequestParam String nome) {
			String mensagem = this.generoService.deleteByNome(nome);
			return new ResponseEntity<>(mensagem, HttpStatus.OK);
	}
	
	@GetMapping("/findAll")
	public ResponseEntity<?> findAll(){
			List<Genero> lista = this.generoService.findAll();
			return new ResponseEntity<List<Genero>>(lista, HttpStatus.OK);
	}
	
	@GetMapping("/findById/{id}")
	public ResponseEntity<?> findById(@PathVariable long id){
			Genero genero = this.generoService.findById(id);
			return new ResponseEntity<Genero>(genero, HttpStatus.OK);
	}
	
	@GetMapping("/findByNome")
	public ResponseEntity<?> findById(@RequestParam String nome){
			List<Genero> lista = this.generoService.findByNome(nome);
			return new ResponseEntity<List<Genero>>(lista, HttpStatus.OK);
	}
}
