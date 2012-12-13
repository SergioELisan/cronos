package senai.cronos.horario;

import java.sql.SQLException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import senai.cronos.CronosAPI;
import senai.cronos.entidades.Aula;
import senai.cronos.entidades.Docente;
import senai.cronos.entidades.UnidadeCurricular;
import senai.util.Tupla;

public class GeraHorarioContinuo extends GeraHorario {

    @Override
    public void alocarAulas(Map<Date, Tupla<Aula, Aula>> horario) throws ClassNotFoundException, SQLException {
        for (UnidadeCurricular uc : getDisciplinas()) {
            Aula aula = getAula(uc);
            int total = getQuantidadeDeDias(uc, GeraHorario.TURNO_INTEIRO);

            for (int i = 0; i < total; i++) {
                for (Date dia : horario.keySet()) {
                    if (horario.get(dia).getPrimeiro().equals(Aula.PADRAO)) {
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
        List<Docente> docentes = CronosAPI.buscaDocentes(getTurma().getNucleo());
        
        for (Aula aula : wrapper.getAulas() ) {
            Map<Date, Tupla<Boolean, Boolean>> dias = wrapper.getDiasLecionados(aula);

            for (Docente docente : docentes) {
                boolean disponivel = true;

                for (Date dia : dias.keySet()) {
                    if (!docente.getHorarioDocente().isDisponivel(dia, getTurma().getTurno())) {
                        disponivel = false;
                        break;
                    }
                }

                if (disponivel && getTurma().getTurno().isInside(docente.getTurno() )) {
                    docente.getTurno();
                    aula.setDocente(docente);
                    docentes.remove(docente);
                    break;
                }
                
            }

        }
        
    }
    
}