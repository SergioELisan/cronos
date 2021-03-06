package edu.cronos.horario;

import edu.cronos.CronosAPI;

/**
 * Fabrica o gerador de horario, de acordo com as opcoes do sistema.
 *
 * @author sergio lisan e carlos melo
 */
public class GeraHorarioFactory {

    public static final int CONTINUO = 0;
    public static final int ALTERNADO = 1;

    public static GeraHorario getGerador() {
        int alternancia = CronosAPI.getAlternancia();
        int aulasPorDia = CronosAPI.getAulasDia();

        if (alternancia == ALTERNADO && aulasPorDia == 0) {
            return new GeraHorarioAlternado();

        } else if (alternancia == CONTINUO && aulasPorDia == 0) {
            return new GeraHorarioContinuo();

        } else if (alternancia == ALTERNADO && aulasPorDia == 1) {
            return new GeraHorarioAlternadoQuebrado();

        } else if (alternancia == CONTINUO && aulasPorDia == 1) {
            return new GeraHorarioContinuoQuebrado();
        } else {
            throw new IllegalArgumentException("FAIL! Opcoes corrompidas! -> 'horario' ");
        }
    }
}
