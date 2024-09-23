package com.jfranco.springboot.oauth.springboot_servicio_oauth.services;

import com.formacionbdi.springboot.app.commons.usuarios.models.entity.Usuario;

public interface IUsuarioService {
	
	public Usuario findByUsername(String username);

}
