package com.eventoapp.models;

import lombok.*;
import org.hibernate.validator.constraints.NotEmpty;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Convidado {

    @Id
    @NotEmpty
    private String rg;

    @NotEmpty
    private String nome;

    @ManyToOne
    private Evento evento;

}
