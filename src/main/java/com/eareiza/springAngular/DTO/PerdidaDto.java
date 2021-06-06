package com.eareiza.springAngular.DTO;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter @Setter @NoArgsConstructor  @AllArgsConstructor
public class PerdidaDto {
    private Long id;
    private String productoNombre;
    private String user;
    private Date createAt;
    private String producto;
    private String tipo;
    private Double monto;
}
