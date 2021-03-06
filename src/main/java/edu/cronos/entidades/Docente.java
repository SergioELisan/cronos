package edu.cronos.entidades;

import edu.cronos.horario.GeradorHorarioDocente;
import edu.cronos.horario.HorarioDocente;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author Carlos Melo e sergio lisan
 */
public class Docente implements Comparable<Docente> {

    public static final Docente PADRAO = new Docente();
    /**
     * matricula que serve como identificador unico do docente
     */
    private Integer matricula = 1;
    /**
     * nome do docente
     */
    private String nome = "Professor EDU";
    /**
     * Inteiro que contem um numero arbitrario sobre a formacao do docente
     */
    private Formacao formacao = Formacao.MEDIO;
    /**
     * data de contratacao do docente
     */
    private Date contratacao = new Date();
    /**
     * nucleo em que o docente preferencialmente trabalha
     */
    private Nucleo nucleo = new Nucleo();
    /**
     * lista de proficiencias do docente
     */
    private List<Proficiencia> proficiencias = new ArrayList<>();
    /**
     * pontuacao do docente. esse atributo é relacionado com a formacao e o
     * tempo de casa do docente
     */
    private int score = 0;
    /**
     * Turno de operacao do docente
     */
    private Turno primeiroTurno = null;
    /**
     * docentes geralmente trabalham em dois turnos, nao necessariamento
     * seguidos
     */
    private Turno segundoTurno = null;

    public Docente() {
    }

    @Override
    public int compareTo(Docente o) {
        return this.nome.compareTo(o.nome);
    }

    public Date getContratacao() {
        return contratacao;
    }

    public void setContratacao(Date contratacao) {
        this.contratacao = contratacao;
    }

    public Formacao getFormacao() {
        return formacao;
    }

    public void setFormacao(Formacao formacao) {
        this.formacao = formacao;
    }

    public Integer getMatricula() {
        return matricula;
    }

    public void setMatricula(Integer matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Nucleo getNucleo() {
        return nucleo;
    }

    public void setNucleo(Nucleo nucleo) {
        this.nucleo = nucleo;
    }

    public HorarioDocente getHorarioDocente() throws Exception {
        return new GeradorHorarioDocente().generate(this);
    }

    /**
     * retorna o turno do docente de acordo com a enumeracao Turno
     *
     * @return
     */
    public Turno getTurno() {
        if (segundoTurno.equals(primeiroTurno)) {
            return primeiroTurno;
        } else if (primeiroTurno.equals(Turno.MANHA) && segundoTurno.equals(Turno.TARDE)) {
            return Turno.MANHA_TARDE;
        } else if (primeiroTurno.equals(Turno.MANHA) && segundoTurno.equals(Turno.NOITE)) {
            return Turno.MANHA_NOITE;
        } else {
            return Turno.TARDE_NOITE;
        }

    }

    public void setTurno(Turno turno) {
        if (turno.equals(Turno.MANHA) || turno.equals(Turno.TARDE) || turno.equals(Turno.NOITE)) {
            primeiroTurno = turno;
            segundoTurno = turno;
        } else if (turno.equals(Turno.MANHA_TARDE)) {
            primeiroTurno = Turno.MANHA;
            segundoTurno = Turno.TARDE;
        } else if (turno.equals(Turno.MANHA_NOITE)) {
            primeiroTurno = Turno.MANHA;
            segundoTurno = Turno.NOITE;
        } else {
            primeiroTurno = Turno.TARDE;
            segundoTurno = Turno.NOITE;
        }
    }

    public Turno getPrimeiroTurno() {
        return primeiroTurno;
    }

    public void setPrimeiroTurno(Turno primeiroTurno) {
        this.primeiroTurno = primeiroTurno;
    }

    public List<Proficiencia> getProficiencias() {
        return proficiencias;
    }

    public void setProficiencias(List<Proficiencia> proficiencias) {
        this.proficiencias = proficiencias;
    }

    /**
     * retorna a proficiencia de uma disciplina
     *
     * @param uc
     * @return
     */
    public Proficiencia getProficiencia(UnidadeCurricular uc) {
        for (Proficiencia p : proficiencias) {
            if (p.getDisciplina().equals(uc)) {
                return p;
            }
        }
        return null;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void limpaProficiencia() {
        this.proficiencias.removeAll(proficiencias);
    }

    public Turno getSegundoTurno() {
        return segundoTurno;
    }

    public void setSegundoTurno(Turno segundoTurno) {
        this.segundoTurno = segundoTurno;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Docente other = (Docente) obj;
        if (!(this.matricula == other.matricula)) {
            return false;
        }
        return this.nome.equals(other.nome);
    }

    @Override
    public int hashCode() {
        int hash = 5;
        hash = 79 * hash + Objects.hashCode(this.matricula);
        hash = 79 * hash + Objects.hashCode(this.nome);
        hash = 79 * hash + (this.formacao != null ? this.formacao.hashCode() : 0);
        hash = 79 * hash + Objects.hashCode(this.contratacao);
        hash = 79 * hash + Objects.hashCode(this.nucleo);
        hash = 79 * hash + Objects.hashCode(this.proficiencias);
        hash = 79 * hash + this.score;
        hash = 79 * hash + (this.primeiroTurno != null ? this.primeiroTurno.hashCode() : 0);
        hash = 79 * hash + (this.segundoTurno != null ? this.segundoTurno.hashCode() : 0);
        return hash;
    }

    @Override
    public String toString() {
        return "Docente { "
                + "matricula = " + matricula
                + ", nome = " + nome
                + ", formacao = " + formacao
                + ", contratacao = " + contratacao
                + ", nucleo = " + nucleo
                + ", proficiencias = " + proficiencias
                + ", score = " + score
                + ", primeiroTurno = " + primeiroTurno
                + ", segundoTurno = " + segundoTurno + '}';
    }
}
