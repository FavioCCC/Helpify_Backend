package com.webcrafters.helpify.seguridad.controladores;

import com.webcrafters.helpify.seguridad.DTO.UsuarioDTO;
import com.webcrafters.helpify.seguridad.entidades.Usuario;
import com.webcrafters.helpify.seguridad.repositorios.UsuarioRepositorio;
import com.webcrafters.helpify.seguridad.servicios.DetalleUsuarioService;
import com.webcrafters.helpify.seguridad.util.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.stream.Collectors;

@CrossOrigin(origins = "${ip.frontend}", allowCredentials = "true", exposedHeaders = "Authorization")
@RestController
@RequestMapping("/api")
public class AuthControlador {

    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final DetalleUsuarioService userDetailsService;

    @Autowired
    private UsuarioRepositorio usuarioRepositorio;

    public AuthControlador(AuthenticationManager authenticationManager, JwtUtil jwtUtil, DetalleUsuarioService userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @PostMapping("/autenticar")
    public ResponseEntity<UsuarioDTO> createAuthenticationToken(@RequestBody UsuarioDTO authRequest) throws Exception {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getNombre(), authRequest.getPassword())
        );

        final UserDetails userDetails = userDetailsService.loadUserByUsername(authRequest.getNombre());
        final String token = jwtUtil.generateToken(userDetails);

        Set<String> roles = userDetails.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());

        // Obtener el usuario desde la base de datos
        Usuario usuario = usuarioRepositorio.findByNombre(authRequest.getNombre())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        // Mapear los datos al DTO
        UsuarioDTO usuarioDTO = new UsuarioDTO();
        usuarioDTO.setIdusuario(usuario.getIdusuario());
        usuarioDTO.setNumerodocumento(usuario.getNumerodocumento());
        usuarioDTO.setNombredocumento(usuario.getNombredocumento());
        usuarioDTO.setNombre(usuario.getNombre());
        usuarioDTO.setApellidopaterno(usuario.getApellidopaterno());
        usuarioDTO.setApellidomaterno(usuario.getApellidomaterno());
        usuarioDTO.setCelular(usuario.getCelular());
        usuarioDTO.setCorreo(usuario.getCorreo());
        usuarioDTO.setPassword(usuario.getPassword());
        usuarioDTO.setFecharegistro(usuario.getFecharegistro());
        usuarioDTO.setRoles(roles);
        usuarioDTO.setJwt(token);
        usuarioDTO.setIdRol(usuario.getRol() != null ? usuario.getRol().getIdrol() : null);

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.set("Authorization", token);

        return ResponseEntity.ok().headers(responseHeaders).body(usuarioDTO);
    }
}
