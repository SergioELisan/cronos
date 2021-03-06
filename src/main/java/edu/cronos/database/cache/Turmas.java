package edu.cronos.database.cache;

import edu.cronos.database.dao.DAOFactory;
import edu.cronos.entidades.Nucleo;
import edu.cronos.entidades.Turma;
import edu.cronos.util.Observador;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Carlos Melo e sergio Lisan
 */
public final class Turmas implements Observador, Cache<Turma> {

    private static Turmas instance;
    private List<Turma> turmas;

    private Turmas() {
        try {
            DAOFactory.getDao(Turma.class).registra(this);
            update();
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Problemas com o vetor de turmas");
        }
    }

    public static Turmas instance() {
        return instance;
    }

    /**
     * Inicia o cache
     */
    public static void start() {
        instance = new Turmas();
    }

    /**
     * Método que retorno o objeto turma tendo como parametro de entrada o nome.
     *
     * @param nome String com o nome da turma
     * @return objeto Turma
     */
    public Turma buscaTurma(String nome) throws ClassNotFoundException, SQLException {
        for (Turma turma : getTurmas()) {
            if (turma.getNome().equals(nome)) {
                return turma;
            }
        }
        return null;
    }

    /**
     *      */
    public Integer getIDTurma() throws ClassNotFoundException, SQLException {
        Integer id;
        Turma t = getTurmas().get(getTurmas().size());

        if (getTurmas().isEmpty()) {
            id = 0;
        } else {
            id = t.getId();
        }
        return id + 1;
    }

    /**
     * retorna as turmas de um nucleo
     *
     * @param nucleo
     * @return
     */
    public List<Turma> buscaTurma(Nucleo nucleo) throws ClassNotFoundException, SQLException {
        List<Turma> _turmas = new ArrayList<>();
        for (Turma turma : getTurmas()) {
            if (turma.getNucleo().equals(nucleo)) {
                _turmas.add(turma);
            }
        }
        return _turmas;
    }

    @Override
    public void update() {
        try {
            turmas = DAOFactory.getDao(Turma.class).get();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

    /**
     * @return the turmas
     */
    public List<Turma> getTurmas() {
        return turmas;
    }

    @Override
    public List<Turma> get() {
        return getTurmas();
    }

    @Override
    public Turma get(Class c, Integer id) {
        for (Turma t : turmas) {
            if (t.getId().equals(id)) {
                return t;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        turmas.clear();
    }
}
