package com.eareiza.springAngular.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class FacturaDto {
    private Long id;
    private String user;
    private Date createAt;
    private String descripcion;
    private Double mercadopago;
    private Double pedidosya;
    private Double puntoventa;
    private Double montocomision;
    private Double pedidosyaefectivo;
    private Double total;
    private Long clienteId;
    private Long cajaId;
    private Long comisionId;
}
