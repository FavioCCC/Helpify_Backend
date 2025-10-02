
package com.webcrafters.helpify.seguridad.servicios;

import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Set;

@Service
public class DetalleUsuarioService implements UserDetailsService {

    private final UsuarioRepositorio usuarioRepositorio;

    public DetalleUsuarioService(UsuarioRepositorio usuarioRepositorio) {
        this.usuarioRepositorio = usuarioRepositorio;
    }

    @Override
    public UserDetails loadUserByUsername(String nombre) throws UsernameNotFoundException {
        Usuario usuario = usuarioRepositorio.findByNombre(nombre)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        Set<GrantedAuthority> authorities = Collections.singleton(
                new SimpleGrantedAuthority(usuario.getRol().getNombre())
        );

        return org.springframework.security.core.userdetails.User
                .withUsername(usuario.getNombre())
                .password(usuario.getPassword())
                .authorities(authorities)
                .build();
    }
}
