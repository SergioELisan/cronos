package edu.cronos.database.cache;

import edu.cronos.database.dao.DAOFactory;
import edu.cronos.entidades.Docente;
import edu.cronos.entidades.Nucleo;
import edu.cronos.entidades.Proficiencia;
import edu.cronos.entidades.UnidadeCurricular;
import edu.cronos.util.Observador;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author Carlos Melo e Sergio Lisan
 */
public final class Docentes implements Observador, Cache<Docente> {

    private static Docentes instance;
    private List<Docente> docentes = new ArrayList<>();

    private Docentes() {
        try {
            DAOFactory.getDao(Docente.class).registra(this);
            update();
        } catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(Docentes.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static Docentes instance() {
        return instance;
    }

    /**
     * Inicia o cache
     */
    public static void start() {
        instance = new Docentes();
    }

    /**
     * retorna docentes de um determinado nucleo
     *
     * @param nucleo
     * @return
     */
    public List<Docente> buscaDocentes(Nucleo nucleo) throws ClassNotFoundException, SQLException {
        List<Docente> _docentes = new ArrayList<>();
        for (Docente doc : get()) {
            if (doc.getNucleo().equals(nucleo)) {
                _docentes.add(doc);
            }
        }
        return _docentes;
    }

    /**
     * Busca um docente pelo nome
     *
     * @param nome
     * @return
     */
    public Docente buscaDocenteNome(String nome) throws ClassNotFoundException, SQLException {
        for (Docente dc : get()) {
            if (dc.getNome().equals(nome)) {
                return dc;
            }
        }
        return null;
    }

    /**
     * procura um docente por sua matricula
     *
     * @param matricula
     * @return
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    public Docente buscaDocenteMatricula(String matricula) throws ClassNotFoundException, SQLException {
        for (Docente dc : get()) {
            if (dc.getMatricula().equals(Integer.parseInt(matricula))) {
                return dc;
            }
        }
        return null;
    }

    public boolean existeDocente(String matricula) throws ClassNotFoundException, SQLException {
        for (Docente dc : get()) {
            if (dc.getMatricula().equals(Integer.parseInt(matricula))) {
                return true;
            }
        }
        return false;
    }

    /**
     * Método que retorna os melhores docentes para uma determinada unidade
     * curricular
     *
     * @param uc
     * @return
     */
    public List<Docente> bestDocentes(UnidadeCurricular uc) throws Exception {
        List<Docente> bestDocentes = new ArrayList<>();
        List<Proficiencia> p;
        int lecionado = 0;
        for (Docente doc : buscaDocentes(uc.getNucleo())) {
            p = doc.getProficiencias();
            for (Proficiencia pr : p) {
                if (pr.getDisciplina().equals(uc)) {
                    lecionado = pr.getLecionado();
                    break;
                }
            }
            if (doc.getProficiencia(uc) != null && lecionado > 5) {
                bestDocentes.add(doc);
            }
        }

        return bestDocentes;
    }

    @Override
    public void update() {
        try {
            docentes = DAOFactory.getDao(Docente.class).get();
        } catch (ClassNotFoundException | SQLException ex) {
            ex.printStackTrace(System.err);
        }
    }

    @Override
    public List<Docente> get() {
        return docentes;
    }

    @Override
    public Docente get(Class c, Integer id) {
        for (Docente docente : docentes) {
            if (docente.getMatricula().equals(id)) {
                return docente;
            }
        }
        return null;
    }

    @Override
    public void clear() {
        docentes.clear();
    }
}
