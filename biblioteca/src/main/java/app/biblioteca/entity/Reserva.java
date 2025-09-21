package app.biblioteca.entity;

import java.time.LocalDate;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import app.biblioteca.auth.Usuario;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class Reserva {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotNull(message = "A data da reserva é obrigatória")
	@DateTimeFormat
	@Column(columnDefinition = "DATE")
	private LocalDate dataReserva;
	
	@NotNull(message = "O usuário é obrigatório")
	@ManyToOne
	@JsonIgnoreProperties("reservas")
	private Usuario usuario;
	
	@NotNull(message = "O livro é obrigatório")
	@ManyToOne
	@JsonIgnoreProperties("reservas")
	private Livro livro;

}
