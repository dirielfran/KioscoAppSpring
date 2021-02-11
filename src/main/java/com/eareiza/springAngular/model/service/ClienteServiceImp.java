package com.eareiza.springAngular.model.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.eareiza.springAngular.interfaces.IClienteService;
import com.eareiza.springAngular.model.entity.Caja;
import com.eareiza.springAngular.model.entity.Cliente;
import com.eareiza.springAngular.model.entity.Region;
import com.eareiza.springAngular.model.repository.ICajaRepository;
import com.eareiza.springAngular.model.repository.IClientesRepository;

@Service
public class ClienteServiceImp implements IClienteService {

	@Autowired
	private IClientesRepository clienteRepo;
	
	@Autowired
	private ICajaRepository cajaRepo;
	
	@Override
	//anotamos la transaccion como de solo letura
	@Transactional(readOnly = true)
	public List<Cliente> findAll() {
		return clienteRepo.findAll();
	}

	@Override
	@Transactional(readOnly = true)
	public Page<Cliente> findAll(Pageable pagina) {
		return clienteRepo.findAll(pagina);
	}
	@Override
	@Transactional
	//Metodo que guarda un cliente
	public Cliente save(Cliente cliente) {
		return clienteRepo.save(cliente);
	}

	@Override
	@Transactional(readOnly = true)
	//Metodo que busca un cliente por id
	public Cliente findById(Integer id) {
		return clienteRepo.findById(id).orElse(null);
	}

	@Override
	@Transactional
	//Metodo que borra un cliente por id
	public void delete(Integer id) {
		clienteRepo.deleteById(id);
	}
	
	
	@Override
	@Transactional(readOnly = true)
	public List<Region> findAllRegiones() {
		return clienteRepo.findAllRegiones();
	}

	@Override
	public Page<Cliente> buscarXEstado(Pageable pagina) {
		Caja caja = cajaRepo.findByEstado("Abierto");
		if(caja != null){
			Page<Cliente> clientes = clienteRepo.findAllById(caja.getCliente().getId(), pagina);
			if(clientes.hasContent()) {
				return clientes;
			}
		}		
		return clienteRepo.findAll(pagina);
	}


}
