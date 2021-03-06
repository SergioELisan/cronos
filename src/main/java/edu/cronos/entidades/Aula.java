package edu.cronos.entidades;

import java.util.Objects;

/**
 * @author Carlos Melo e sergio lisan
 */
public class Aula {

    /**
     * Aula padrao, vazia, usada para ocupar um espaço e evitar nulos
     */
    public static final Aula VAZIA = Aula.create();
    private Integer id = 0;
    private UnidadeCurricular disciplina = new UnidadeCurricular();
    private Docente docente = new Docente();
    private Laboratorio lab = new Laboratorio();
    private Turma turma = new Turma();

    public Aula() {
    }

    /**
     * Cria uma instancia padrao de Aula
     *
     * @return aula
     */
    public static Aula create() {
        return new Aula();
    }

    public UnidadeCurricular getDisciplina() {
        return disciplina;
    }

    public void setDisciplina(UnidadeCurricular disciplina) {
        this.disciplina = disciplina;
    }

    public Docente getDocente() {
        return docente;
    }

    public void setDocente(Docente docente) {
        this.docente = docente;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Laboratorio getLab() {
        return lab;
    }

    public void setLab(Laboratorio lab) {
        this.lab = lab;
    }

    public Turma getTurma() {
        return turma;
    }

    public void setTurma(Turma turma) {
        this.turma = turma;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Aula other = (Aula) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        if (!Objects.equals(this.disciplina, other.disciplina)) {
            return false;
        }
        if (!Objects.equals(this.docente, other.docente)) {
            return false;
        }
        if (!Objects.equals(this.lab, other.lab)) {
            return false;
        }
        return Objects.equals(this.turma, other.turma);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 53 * hash + Objects.hashCode(this.id);
        hash = 53 * hash + Objects.hashCode(this.disciplina);
        hash = 53 * hash + Objects.hashCode(this.docente);
        hash = 53 * hash + Objects.hashCode(this.lab);
        hash = 53 * hash + Objects.hashCode(this.turma);
        return hash;
    }

    @Override
    public String toString() {
        return "Aula{" + "id=" + id + ", disciplina=" + disciplina + ", docente=" + docente + ", lab=" + lab + '}';
    }
}
