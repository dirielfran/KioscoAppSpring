package com.eareiza.springAngular.DTO;

import com.eareiza.springAngular.model.entity.Cliente;
import com.eareiza.springAngular.model.entity.Factura;
import com.eareiza.springAngular.model.entity.RetiroCaja;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @AllArgsConstructor @NoArgsConstructor
public class CajaDto {
    private Long id;
    private Date fechaopen;
    private Date fechaclose;
    private String user;
    private Double venta;
    private Double efectivo;
    private Double retiros;
    private Double mercadopago;
    private Double pedidosya;
    private Double puntoventa;
    private Double iniciocaja;
    private Double diferencia;
    private Double ganancia;
    private String estado;
}
