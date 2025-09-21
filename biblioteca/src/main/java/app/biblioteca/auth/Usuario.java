package app.biblioteca.auth;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.ArrayList;
import java.util.Collection;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import app.biblioteca.entity.Reserva;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.OneToMany;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Getter
@Setter
@Entity
public class Usuario implements UserDetails {
	

	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@NotBlank(message = "O nome de usuário é obrigatório")
	private String nome;
	
	@NotBlank(message = "O e-mail do usuário é obrigatório")
	@Email(message = "O e-mail deve ser válido")
	@Column(unique = true)
	private String email;
	
	@OneToMany(mappedBy = "usuario", cascade = CascadeType.ALL)
	@JsonIgnoreProperties("usuario")
	private List<Reserva> reservas;
	
	@NotNull(message = "A senha é obrigatória")
	private String password;
	
	private String role;

	@Override
	@JsonIgnore
	public Collection<? extends GrantedAuthority> getAuthorities() {
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ROLE_" + this.role));
		return authorities;
	}

	@Override
	public String getPassword() {
		return password;
	}

	@Override
	public String getUsername() {
		return email;
	}
	
	@Override
	@JsonIgnore
	public boolean isAccountNonExpired() {
	    return true;
	}

	@Override
	@JsonIgnore
	public boolean isAccountNonLocked() {
	    return true;
	}

	@Override
	@JsonIgnore
	public boolean isCredentialsNonExpired() {
	    return true;
	}

	@Override
	@JsonIgnore
	public boolean isEnabled() {
	    return true;
	}

}
