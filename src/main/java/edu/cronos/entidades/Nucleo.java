package edu.cronos.entidades;

import java.io.Serializable;
import java.util.Objects;

/**
 * Classe que representa um nucleo, que é um agrupador semantico de docentes,
 * turma e unidades curriculares, da escola.
 *
 * @author Carlos Melo e Sergio Lisan
 */
public class Nucleo implements Comparable<Nucleo>, Serializable {

    /**
     * Nucleo comum a todas as disciplinas
     */
    public static final Nucleo COMUM = Nucleo.getCommon();
    private Integer id = 0;
    private String nome = "";
    private String desc = "";

    public Nucleo() {
    }

    public Nucleo(String nome, String desc) {
        this.nome = nome;
        this.desc = desc;
    }

    /**
     * metodo que gera um nucleo padrao
     *
     * @return nucleo comum
     */
    private static Nucleo getCommon() {
        Nucleo n = new Nucleo();
        n.setId(1);
        n.setNome("Comum");
        n.setDesc("Núcleo comum");

        return n;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Nucleo other = (Nucleo) obj;
        return Objects.equals(this.nome, other.nome);
    }

    public int hashCode() {
        int hash = 5;
        hash = 47 * hash + Objects.hashCode(this.id);
        hash = 47 * hash + Objects.hashCode(this.nome);
        hash = 47 * hash + Objects.hashCode(this.desc);
        return hash;
    }

    public String toString() {
        return nome;
    }

    @Override
    public int compareTo(Nucleo t) {
        return nome.compareTo(t.nome);
    }
}
