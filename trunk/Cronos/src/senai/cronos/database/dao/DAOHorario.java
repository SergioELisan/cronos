package senai.cronos.database.dao;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import senai.cronos.Fachada;
import senai.cronos.util.Observador;
import senai.cronos.database.Database;
import senai.cronos.entidades.*;
import senai.cronos.util.Contador;
import senai.cronos.util.Tupla;

/**
 *
 * @author Sergio Lisan
 */
public class DAOHorario implements DAO<Horario> {

   @Override
    public void add(Horario u) throws SQLException {
        open();
        String query = Database.query("horario.insert");

        for (Date dia : u.getHorario().keySet()) {
            try (PreparedStatement ps = con.prepareStatement(query)) {
                ps.setInt(1, u.getTurma().getId());
                ps.setDate(2, new java.sql.Date(dia.getTime()));
                ps.setInt(3, u.getHorario().get(dia).getPrimeiro().getDisciplina().getId());
                ps.setInt(4, u.getHorario().get(dia).getPrimeiro().getDocente().getMatricula());
                ps.setInt(5, u.getHorario().get(dia).getPrimeiro().getLab().getId());
                ps.setInt(6, u.getHorario().get(dia).getSegundo().getDisciplina().getId());
                ps.setInt(7, u.getHorario().get(dia).getSegundo().getDocente().getMatricula());
                ps.setInt(8, u.getHorario().get(dia).getSegundo().getLab().getId());

                ps.execute();
            }
        }
        close();
    }

    @Override
    public void remove(Serializable id) throws SQLException {
        open();
        String query = Database.query("horario.delete");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, (Integer) id);
            ps.execute();
        }
        
        close();
    }

    @Override
    public void update(Horario u) throws SQLException {
        open();
        String query = Database.query("horario.update");

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
                ps.setInt(7, u.getTurma().getId());
                ps.setDate(8, new java.sql.Date(dia.getTime()));

                ps.execute();
            }
        }
        close();
    }

    @Override
    public List<Horario> get() throws SQLException {
        open();
        List<Horario> horarios = new ArrayList<>();
        
        try {
            for(Turma turma : Fachada.<Turma>get(Turma.class) ) {
                Integer id = turma.getId();
                horarios.add(this.get(id));
            }
        } catch (ClassNotFoundException ex) {
            ex.printStackTrace();
        }
        
        close();
        return horarios;
    }

    @Override
    public Horario get(Serializable id) throws SQLException {
        open();
        Horario h = new Horario();
        String query = Database.query("horario.get");

        try (PreparedStatement ps = con.prepareStatement(query)) {
            ps.setInt(1, (Integer) id);
            ResultSet rs = ps.executeQuery();

            Map<Date, Tupla<Aula, Aula>> horario = new TreeMap<>();

            Turma t = Fachada.<Turma>get(Turma.class, (Integer) id);

            while (rs.next()) {
                Date dia = rs.getDate("dia");

                Aula a1 = new Aula();

                a1.setDocente(Fachada.<Docente>get(Docente.class, rs.getInt("docente1") ) ) ;
                a1.setDisciplina(Fachada.<UnidadeCurricular>get(UnidadeCurricular.class, rs.getInt("disciplina1") ) );
                a1.setLab(Fachada.<Laboratorio>get(Laboratorio.class, rs.getInt("laboratorio1") ) );

                Aula a2 = new Aula();
                a2.setDocente(Fachada.<Docente>get(Docente.class, rs.getInt("docente2") ) ) ;
                a2.setDisciplina(Fachada.<UnidadeCurricular>get(UnidadeCurricular.class, rs.getInt("disciplina2") ) );
                a2.setLab(Fachada.<Laboratorio>get(Laboratorio.class, rs.getInt("laboratorio2") ) );

                horario.put(dia, new Tupla<>(a1, a2));
            }

            h.setTurma(t);
            h.setHorario(horario);

        } catch (Exception e) {
        }

        close();
        
        return h;
    }
    
    @Override
    public void close() throws SQLException {
        con.close();
    }

    @Override
    public void open() throws SQLException {
        con = Database.conexao();
        Contador.horario++;
    }
    
    @Override
    public void registra(Observador o) {
        observadores.add(o);
    }

    @Override
    public void remove(Observador o) {
        observadores.remove(o);
    }

    @Override
    public void notifica() {
        for(Observador o : observadores)
            o.update();
    }
    
    private List<Observador> observadores = new ArrayList<>();
    
    
    private Connection con;
}