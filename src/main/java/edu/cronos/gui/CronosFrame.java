/*
 * CronosFrame.java
 *
 * Created on 22/12/2011, 09:05:58
 */
package edu.cronos.gui;

import edu.cronos.CronosAPI;
import edu.cronos.gui.cadastro.CadastroUI;
import edu.cronos.gui.custom.ImageLoader;
import edu.cronos.gui.horarios.HorariosUI;
import edu.cronos.util.os.OSFactory;
import edu.cronos.util.os.OperatingSystem;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * @author Sergio Lisan e Carlos Melo
 */
public class CronosFrame extends javax.swing.JFrame {

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
    public static final String HOME = "home";
    public static final String HORARIOS = "horarios";
    public static final String DOCENTES = "docentes";
    public static final String CADASTROS = "cadastros";
    public static final String RELATORIOS = "relatorios";
    public static final String TURMAS = "turmas";
    public static final String PRESENTATION = "presentation";
    public static final String DISCIPLINAS = "disciplinas";
    public static final String AJUDA = "ajuda";
    public static final String CONFIG = "config";

    public CronosFrame() {
        initComponents();
        setLocationRelativeTo(null);
        setExtendedState(MAXIMIZED_BOTH);
        setLookAndFeel();
        loadPanels();
        Switch(CronosFrame.PRESENTATION);

        // chama a funcao de saida do banco
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent we) {
                CronosAPI.quit();
            }
        });

        setIconImage(ImageLoader.loadIcon());
    }

    /**
     * Verifica qual o sistema operacional e carrega a devida Look And Feel
     */
    private void setLookAndFeel() {
        OperatingSystem os = OSFactory.getOperatingSystem();
        try {
            UIManager.setLookAndFeel(os.getLookAndFeel());
            SwingUtilities.updateComponentTreeUI(this);
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            Alerta.jogarAviso("problemas na definicao do estilo do sistema");
        }
    }

    private void loadPanels() {
        getContentPane().setLayout(new CardLayout());

        getContentPane().add(new Home(this), HOME);
        getContentPane().add(HorariosUI.getInstance(), HORARIOS);
        HorariosUI.getInstance().setMain(this);

        getContentPane().add(new DocenteUI(this), DOCENTES);
        getContentPane().add(new ReportUI(this), RELATORIOS);
        getContentPane().add(new TurmaUI(this), TURMAS);
        getContentPane().add(new DisciplinaUI(this), DISCIPLINAS);
        getContentPane().add(new HelpUI(this), AJUDA);
        getContentPane().add(new ConfigUI(this), CONFIG);
        getContentPane().add(new CadastroUI(this), CADASTROS);
        getContentPane().add(new PresentationUI(this), PRESENTATION);
    }

    /**
     * Muda a visao
     *
     * @param module
     */
    public final void Switch(String module) {
        CardLayout cl = (CardLayout) getContentPane().getLayout();
        cl.show(getContentPane(), module);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("EDU Cronos");
        setMinimumSize(new java.awt.Dimension(1024, 728));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1040, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 605, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
}
