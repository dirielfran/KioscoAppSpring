package com.eareiza.springAngular.model.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.eareiza.springAngular.interfaces.IItemInventarioService;
import com.eareiza.springAngular.model.entity.ItemInventario;
import com.eareiza.springAngular.model.repository.IItemInventarioRepository;

@Service
public class ItemInventarioServiceImpl implements IItemInventarioService {

	@Autowired
	IItemInventarioRepository itemInvRepo;
	
	@Override
	public ItemInventario saveItemInventario(ItemInventario itemInventario) {
		return itemInvRepo.save(itemInventario);
	}
	
	@Override
	public List<ItemInventario> getInventarios(Long idProducto, String estado) {
		return itemInvRepo.findItemInventario(idProducto, estado);
	}
	
	@Override
	public ItemInventario getItemInventario(Long id) {
		return itemInvRepo.findById(id).orElse(null);
	}

}
