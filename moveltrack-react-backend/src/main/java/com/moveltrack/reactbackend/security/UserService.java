package com.moveltrack.reactbackend.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.moveltrack.reactbackend.model.Pessoa;
import com.moveltrack.reactbackend.model.Usuario;
import com.moveltrack.reactbackend.rest.api.repository.PessoaRepository;
import com.moveltrack.reactbackend.rest.api.repository.UsuarioRepository;

@Service
public class UserService implements UserDetailsService {

	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired PessoaRepository pessoaRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Usuario usuario = usuarioRepository.findByNomeUsuario(username);
		if(usuario == null)
			throw new UsernameNotFoundException("Usuário ou senha não conferem.");
		
		Pessoa pessoa = pessoaRepository.findByUsuario(usuario);
		return new UsuarioSistema(usuario, getPermissoes(usuario),pessoa);
	}

	private Collection<? extends GrantedAuthority> getPermissoes(Usuario usuario) {
		Set<SimpleGrantedAuthority> authorities = new HashSet<>();
		usuario.getPermissoes().forEach(f -> authorities.add(new SimpleGrantedAuthority(f.getDescricao())));
		usuario.getPerfil().getPermissoes().forEach(f -> authorities.add(new SimpleGrantedAuthority(f.getDescricao())));
		return authorities;
	}
}