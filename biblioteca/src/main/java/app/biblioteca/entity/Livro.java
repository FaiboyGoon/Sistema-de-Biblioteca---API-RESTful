package app.biblioteca.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Entity
public class Livro {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "O título é obrigatório")
	private String titulo;

	@NotNull(message = "O Autor é obrigatório")
	@ManyToOne/*(cascade = CascadeType.ALL)*/
	@JsonIgnoreProperties("livros")
	private Autor autor;

	@OneToMany(mappedBy = "livro", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("livro")
	private List<Reserva> reservas;
	
	@ManyToMany/*(cascade = CascadeType.ALL)*/
	@JoinTable(name = "livro_genero")
	@JsonIgnoreProperties("livros")
	private List<Genero> generos;
	
	public Livro() {
		super();
	}
}
