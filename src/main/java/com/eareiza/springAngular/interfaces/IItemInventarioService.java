package com.eareiza.springAngular.interfaces;

import java.util.List;

import com.eareiza.springAngular.model.entity.ItemInventario;

public interface IItemInventarioService {
	ItemInventario saveItemInventario(ItemInventario itemInventario);
	List<ItemInventario> getInventarios(Long idProducto, String estado);
	ItemInventario getItemInventario(Long id);
}
