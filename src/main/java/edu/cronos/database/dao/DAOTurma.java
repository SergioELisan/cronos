package edu.cronos.database.dao;

import edu.cronos.CronosAPI;
import edu.cronos.database.DatabaseUtil;
import edu.cronos.entidades.*;
import edu.cronos.horario.Horario;
import edu.cronos.util.Tupla;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author Sergio Lisan
 */
public class DAOTurma extends DAO<Turma> {

    private static DAO<Turma> instance = new DAOTurma();

    private DAOTurma() {
    }

    public static DAO<Turma> getInstance() {
        return instance;
    }

    @Override
    public void add(Turma u) throws SQLException {
        open();
        String query = DatabaseUtil.query("turma.insert");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, u.getNome());
            ps.setInt(2, u.getNucleo().getId());
            ps.setDate(3, new java.sql.Date(u.getEntrada().getTime()));
            ps.setDate(4, new java.sql.Date(u.getSaida().getTime()));
            ps.setInt(5, u.getTurno().ordinal());
            ps.setInt(6, u.getHabilitacao());
            ps.execute();
        }
        close();
        notifica();
    }

    public void addHorario(Turma t) throws SQLException {
        open();
        String query = DatabaseUtil.query("horario.insert");

        Horario u = t.getHorario();

        for (Date dia : u.getHorario().keySet()) {
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, t.getId());
                ps.setDate(2, new java.sql.Date(dia.getTime()));

                Integer disciplinaID = u.getHorario().get(dia).getPrimeiro().getDisciplina().getId();
                ps.setInt(3, disciplinaID);
                ps.setInt(4, u.getHorario().get(dia).getPrimeiro().getDocente().getMatricula());
                ps.setInt(5, u.getHorario().get(dia).getPrimeiro().getLab().getId());

                Integer disciplina2ID = u.getHorario().get(dia).getSegundo().getDisciplina().getId();
                ps.setInt(6, disciplina2ID);
                ps.setInt(7, u.getHorario().get(dia).getSegundo().getDocente().getMatricula());
                ps.setInt(8, u.getHorario().get(dia).getSegundo().getLab().getId());

                ps.execute();
            }
        }

        close();
        notifica();
    }

    @Override
    public void remove(Serializable id) throws SQLException {
        removeHorario(id);

        open();
        String query = DatabaseUtil.query("turma.delete");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, (Integer) id);
            ps.execute();
        }

        close();
        notifica();
    }

    public void removeHorario(Serializable id) throws SQLException {
        open();
        String query = DatabaseUtil.query("horario.delete");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, (Integer) id);
            ps.execute();
        }

        close();
        notifica();
    }

    @Override
    public void update(Turma u) throws SQLException {
        open();
        String query = DatabaseUtil.query("turma.update");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setString(1, u.getNome());
            ps.setInt(2, u.getNucleo().getId());
            ps.setDate(3, new java.sql.Date(u.getEntrada().getTime()));

            java.sql.Date saida = u.getSaida() == null ? null : new java.sql.Date(u.getSaida().getTime());
            ps.setDate(4, saida);

            ps.setInt(5, u.getTurno().ordinal());
            ps.setInt(6, u.getHabilitacao());
            ps.setInt(7, u.getId());
            ps.execute();
        }
        close();
        notifica();
    }

    public void updateHorario(Turma t) throws SQLException {
        open();
        String query = DatabaseUtil.query("horario.update");

        Horario u = t.getHorario();

        for (Date dia : u.getHorario().keySet()) {
            try (PreparedStatement ps = con.prepareStatement(query)) {
                // update
                ps.setInt(1, u.getHorario().get(dia).getPrimeiro().getDisciplina().getId());
                ps.setInt(2, u.getHorario().get(dia).getPrimeiro().getDocente().getMatricula());
                ps.setInt(3, u.getHorario().get(dia).getPrimeiro().getLab().getId());
                ps.setInt(4, u.getHorario().get(dia).getSegundo().getDisciplina().getId());
                ps.setInt(5, u.getHorario().get(dia).getSegundo().getDocente().getMatricula());
                ps.setInt(6, u.getHorario().get(dia).getSegundo().getLab().getId());

                // where
                ps.setInt(7, t.getId());
                ps.setDate(8, new java.sql.Date(dia.getTime()));

                ps.execute();
            }
        }
        close();
        notifica();
    }

    @Override
    public List<Turma> get() throws SQLException {
        open();
        List<Turma> turmas = new ArrayList<>();
        String query = DatabaseUtil.query("turma.select");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ResultSet rs = ps.executeQuery();

            DAO<Nucleo> daonucleo = DAOFactory.getDao(Nucleo.class);

            while (rs.next()) {
                Turma t = new Turma();
                t.setId(rs.getInt("id"));
                t.setNome(rs.getString("nome"));
                t.setNucleo(daonucleo.get(rs.getInt("nucleo")));
                t.setEntrada(rs.getDate("entrada"));
                t.setSaida(rs.getDate("saida"));
                t.setTurno(Turno.getTurno(rs.getInt("turno")));
                t.setHabilitacao(rs.getInt("habilitacao"));
                t.setHorario(getHorario(t.getId()));
                turmas.add(t);
            }

        } catch (Exception e) {
            System.out.println("DAOTURMA GET(): " + e.getMessage());
            e.printStackTrace();
        }

        close();
        return turmas;
    }

    @Override
    public Turma get(Serializable id) throws SQLException {
        open();
        Turma t = new Turma();
        String query = DatabaseUtil.query("turma.get");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();

            DAO<Nucleo> daonucleo = DAOFactory.getDao(Nucleo.class);

            while (rs.next()) {
                t.setId(rs.getInt("id"));
                t.setNome(rs.getString("nome"));
                t.setNucleo(daonucleo.get(rs.getInt("nucleo")));
                t.setEntrada(rs.getDate("entrada"));
                t.setSaida(rs.getDate("saida"));
                t.setTurno(Turno.getTurno(rs.getInt("turno")));
                t.setHabilitacao(rs.getInt("habilitacao"));
                t.setHorario(getHorario(t.getId()));

            }

        } catch (Exception e) {
            System.out.println("DAOTURMA GET(ID): " + e.getMessage());
            e.printStackTrace();
        }

        close();
        return t;
    }

    public Horario getHorario(Integer turmaID) throws SQLException {
        open();
        Horario wrapper = new Horario();
        String query = DatabaseUtil.query("horario.get");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, turmaID);
            ResultSet rs = ps.executeQuery();

            Map<Date, Tupla<Aula, Aula>> horario = wrapper.getHorario();

            while (rs.next()) {
                Date dia = rs.getDate("dia");

                Aula a1 = Aula.create();
                a1.setDocente(CronosAPI.get(Docente.class, rs.getInt("docente1")));
                a1.setDisciplina(CronosAPI.get(UnidadeCurricular.class, rs.getInt("disciplina1")));
                a1.setLab(CronosAPI.get(Laboratorio.class, rs.getInt("laboratorio1")));

                Aula a2 = Aula.create();
                a2.setDocente(CronosAPI.get(Docente.class, rs.getInt("docente2")));
                a2.setDisciplina(CronosAPI.get(UnidadeCurricular.class, rs.getInt("disciplina2")));
                a2.setLab(CronosAPI.get(Laboratorio.class, rs.getInt("laboratorio2")));

                horario.put(dia, new Tupla<>(a1, a2));
            }


        } catch (Exception e) {
            System.out.println("DAOTURMA GETHORARIO(ID): " + e.getMessage());
            e.printStackTrace();
        }

        close();

        return wrapper;
    }
}
