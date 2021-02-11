package com.eareiza.springAngular.interfaces;

import java.util.List;

import com.eareiza.springAngular.model.entity.Usuario;

public interface IUsuariosService {
	public Usuario findByUsername(String username);
	List<Usuario> buscarEmailByPerfil(String perfil);
}
