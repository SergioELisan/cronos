package edu.cronos.horario;

import edu.cronos.CronosAPI;
import edu.cronos.entidades.Aula;
import edu.cronos.entidades.Docente;
import edu.cronos.entidades.UnidadeCurricular;
import edu.cronos.util.Tupla;
import edu.cronos.util.date.DateUtil;

import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

public class GeraHorarioAlternadoQuebrado extends GeraHorario {

    private Docente lastDocente = Docente.PADRAO;

    @Override
    public void alocarAulas(Map<Date, Tupla<Aula, Aula>> horario) throws ClassNotFoundException, SQLException {
        int modo = 0;
        for (UnidadeCurricular uc : getDisciplinas()) {
            Aula aula = getAula(uc);
            int total = getQuantidadeDeDias(uc, GeraHorario.TURNO_METADE);

            for (int i = 0; i < total; i++) {
                OUTER:
                for (Date dia : horario.keySet()) {
                    String diaSemana = DateUtil.getNomeDia(dia);
                    if (modo == 0) {
                        switch (diaSemana) {
                            case DateUtil.SEG:
                            case DateUtil.QUA:
                            case DateUtil.SEX:
                                if (horario.get(dia).getPrimeiro().equals(Aula.VAZIA)) {
                                    horario.get(dia).setPrimeiro(aula);

                                    break OUTER;
                                }
                        }
                    } else if (modo == 1) {
                        switch (diaSemana) {
                            case DateUtil.SEG:
                            case DateUtil.QUA:
                            case DateUtil.SEX:
                                if (horario.get(dia).getSegundo().equals(Aula.VAZIA)) {
                                    horario.get(dia).setSegundo(aula);

                                    break OUTER;
                                }
                        }
                    } else if (modo == 2) {
                        switch (diaSemana) {
                            case DateUtil.TER:
                            case DateUtil.QUI:
                                if (horario.get(dia).getPrimeiro().equals(Aula.VAZIA)) {
                                    horario.get(dia).setPrimeiro(aula);

                                    break OUTER;
                                }
                        }
                    } else {
                        switch (diaSemana) {
                            case DateUtil.TER:
                            case DateUtil.QUI:
                                if (horario.get(dia).getSegundo().equals(Aula.VAZIA)) {
                                    horario.get(dia).setSegundo(aula);

                                    break OUTER;
                                }

                        }
                    }

                }
            }

            modo = (modo == 3) ? 0 : ++modo;
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
                    Integer metade = dias.get(dia).getPrimeiro() ? Tupla.PRIMEIRA : Tupla.SEGUNDA;

                    if (!docente.getHorarioDocente().isDisponivel(dia, getTurma().getTurno(), metade)) {
                        disponivel = false;
                        break;
                    }
                }

                if (disponivel && !lastDocente.equals(docente)
                        && getTurma().getTurno().isInside(docente.getTurno())) {
                    aula.setDocente(docente);
                    lastDocente = docente;
                    break;
                }
            }

        }
    }
}
