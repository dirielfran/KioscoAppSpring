package com.eareiza.springAngular.interfaces;

import java.util.List;

import com.eareiza.springAngular.DTO.EstadisticasComparativaDTO;

public interface IDashboardService {
	
	List<Object[]> findVentasUlt7();
	List<Object[]> findGastosUlt7();
	List<Object[]> findGananciasUlt7();
	List<Object[]> findPerdidasXSemana();
	List<Object[]> findProductosTopMes();
	Double findPatrimonio();
	List<EstadisticasComparativaDTO> findComparativa();
	List<EstadisticasComparativaDTO> findTopX3();
}
