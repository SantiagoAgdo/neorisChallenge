package com.neoris.santiago.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "cliente")
@NoArgsConstructor
@Data
public class ClienteEntity  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false, name = "id")
    private Integer id;

    @Column(name = "contrasena")
    private String contrasena;

    @Column(name = "estado")
    private Boolean estado;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "id_persona", nullable = false)
    private PersonaEntity persona;

    public ClienteEntity(Integer id, String contrasena, Boolean estado, PersonaEntity persona) {
        this.id = id;
        this.contrasena = contrasena;
        this.estado = estado;
        this.persona = persona;
    }
}
