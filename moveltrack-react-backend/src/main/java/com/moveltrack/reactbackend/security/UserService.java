package com.moveltrack.reactbackend.security;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moveltrack.reactbackend.model.Pessoa;
import com.moveltrack.reactbackend.rest.api.repository.PessoaRepository;

@Service
public class UserService implements UserDetailsService {



	@Autowired
	private PessoaRepository pessoaRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		
		Pessoa pessoa = pessoaRepository.findByCpf(username);
		if(pessoa == null)
			throw new UsernameNotFoundException("Usuário ou senha não conferem.");
		return new UsuarioSistema(pessoa, Collections.emptyList());
	}

//	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
//		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
//		usuario.getFuncionalidades().forEach(f -> authorities.add(new SimpleGrantedAuthority(f.getDescricao().toLowerCase())));
//
//		return authorities;
//	}
}