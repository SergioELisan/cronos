package edu.cronos.horario;

import edu.cronos.CronosAPI;
import edu.cronos.entidades.Aula;
import edu.cronos.entidades.Docente;
import edu.cronos.entidades.UnidadeCurricular;
import edu.cronos.util.Tupla;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class GeraHorarioContinuo extends GeraHorario {

    private Docente lastDocente = Docente.PADRAO;

    @Override
    public void alocarAulas(Map<Date, Tupla<Aula, Aula>> horario) throws ClassNotFoundException, SQLException {
        for (UnidadeCurricular uc : getDisciplinas()) {
            Aula aula = getAula(uc);
            int total = getQuantidadeDeDias(uc, GeraHorario.TURNO_INTEIRO);

            for (int i = 0; i < total; i++) {
                for (Date dia : horario.keySet()) {
                    if (horario.get(dia).getPrimeiro().equals(Aula.VAZIA)) {
                        horario.get(dia).setPrimeiro(aula);
                        horario.get(dia).setSegundo(aula);

                        break;
                    }
                }
            }

        }

    }

    @Override
    public void alocarDocentes(Map<Date, Tupla<Aula, Aula>> horario) throws Exception {
        Horario wrapper = new Horario(horario);

        for (Aula aula : wrapper.getAulas()) {
            Map<Date, Tupla<Boolean, Boolean>> dias = wrapper.getDiasLecionados(aula);

            for (Docente docente : CronosAPI.bestDocentes(aula.getDisciplina())) {
                boolean disponivel = true;

                for (Date dia : dias.keySet()) {
                    if (!docente.getHorarioDocente().isDisponivel(dia, getTurma().getTurno())) {
                        disponivel = false;
                        break;
                    }
                }

                if ((disponivel && !lastDocente.equals(docente))
                        && getTurma().getTurno().isInside(docente.getTurno())) {
                    aula.setDocente(docente);
                    lastDocente = docente;
                    break;
                }

            }

        }

    }
}
