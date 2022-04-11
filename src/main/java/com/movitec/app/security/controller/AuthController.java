package com.movitec.app.security.controller;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.movitec.app.dto.Mensaje;
import com.movitec.app.entity.TipoDocumento;
import com.movitec.app.security.dto.Contraseñas;
import com.movitec.app.security.dto.JwtDto;
import com.movitec.app.security.dto.LoginUsuario;
import com.movitec.app.security.dto.NuevoUsuario;
import com.movitec.app.security.entity.Rol;
import com.movitec.app.security.entity.Usuario;
import com.movitec.app.security.enums.RolNombre;
import com.movitec.app.security.jwt.JwtProvider;
import com.movitec.app.security.service.RolService;
import com.movitec.app.security.service.UsuarioService;

@RestController
@RequestMapping("/auth")
public class AuthController {
	@Autowired
	PasswordEncoder passwordEncoder;
	
	@Autowired
	AuthenticationManager authenticationManager;
	
	@Autowired
	UsuarioService usuarioService;
	
	@Autowired
	RolService rolService;
	
	@Autowired
	JwtProvider jwtProvider;
	
	@PostMapping("/nuevo")
	public ResponseEntity<?> nuevo(@Valid @RequestBody NuevoUsuario nuevoUsuario, BindingResult bindingResult){
		if(bindingResult.hasErrors())
			return new ResponseEntity(new Mensaje("campos mal puestos o email invalido"), HttpStatus.BAD_REQUEST);
		if(usuarioService.existsByNombreUsuario(nuevoUsuario.getNombreUsuario()))
			return new ResponseEntity(new Mensaje("ese nombre ya existe"), HttpStatus.BAD_REQUEST);
		if(usuarioService.existsByEmail(nuevoUsuario.getEmail()))
			return new ResponseEntity(new Mensaje("ese email ya existe"), HttpStatus.BAD_REQUEST);
		TipoDocumento documento = new TipoDocumento();
		documento.setId(nuevoUsuario.getTipoDocumento().getId());
		documento.setTipo(nuevoUsuario.getTipoDocumento().getTipo());
		Usuario usuario = new Usuario(nuevoUsuario.getNombre(),nuevoUsuario.getApellidos(),nuevoUsuario.getTelefono(),nuevoUsuario.getDireccion(),documento,nuevoUsuario.getDocumento(),nuevoUsuario.getEmail(),nuevoUsuario.getEstado(),nuevoUsuario.getNombreUsuario(),passwordEncoder.encode(nuevoUsuario.getPassword()));
		Set<Rol> roles = new HashSet<>();
		if ( nuevoUsuario.getRoles().contains("user"))
			roles.add(rolService.getByRolNombre(RolNombre.ROLE_USER).get());
		if ( nuevoUsuario.getRoles().contains("admin"))
			roles.add(rolService.getByRolNombre(RolNombre.ROLE_ADMIN).get());
		usuario.setRoles(roles);
		usuarioService.save(usuario);
		return new ResponseEntity(new Mensaje("usuario guardado"),HttpStatus.CREATED);
	}
	
	@PostMapping("/login")
	public ResponseEntity<JwtDto> login(@Valid @RequestBody LoginUsuario loginUsuario, BindingResult bindingResult){
		if (bindingResult.hasErrors())
			return new ResponseEntity(new Mensaje("campos mal puestos"), HttpStatus.BAD_REQUEST);
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginUsuario.getNombreUsuario(), loginUsuario.getPassword()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		String jwt = jwtProvider.generateToken(authentication);
		UserDetails userDetails = (UserDetails) authentication.getPrincipal();
		Usuario user = usuarioService.getByNombreUsuario(userDetails.getUsername()).get();
		JwtDto jwtDto = new JwtDto(jwt,user.getNombreUsuario(),user.getNombre(),user.getApellidos(),user.getTelefono(),user.getDireccion(),user.getEstado(),userDetails.getAuthorities());
		return new ResponseEntity(jwtDto, HttpStatus.OK);
	}
	
	@PutMapping("/editar/{id}")
	public void editar(@PathVariable Integer id, @RequestBody NuevoUsuario usuario){
		Optional<Usuario> user = usuarioService.getById(id);
		if (user.isPresent()) {
			Usuario u = user.get();
			u.setNombre(usuario.getNombre());
			u.setApellidos(usuario.getApellidos());
			u.setDocumento(usuario.getDocumento());
			u.setEmail(usuario.getEmail());
			u.setEstado(usuario.getEstado());
			u.setNombreUsuario(usuario.getNombreUsuario());
			u.setTipoDocumento(usuario.getTipoDocumento());
			u.setDireccion(usuario.getDireccion());
			u.setTelefono(usuario.getTelefono());
			usuarioService.save(u);
		}
	}
	
	@PutMapping("/cambiar")
	public void cambiarContraseña(@RequestBody Contraseñas contras) {
		Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(contras.getNomUsuario(), contras.getPassAniguo()));
		SecurityContextHolder.getContext().setAuthentication(authentication);
		Usuario u = usuarioService.getByNombreUsuario(contras.getNomUsuario()).get();
		u.setPassword(passwordEncoder.encode(contras.getPassNuevo()));
		usuarioService.save(u);
	}
}
