/*
 * HorariosExibirPanel.java
 *
 * Created on 22/12/2011, 11:00:30
 */
package edu.cronos.gui.horarios;

import edu.cronos.CronosAPI;
import edu.cronos.database.dao.DAOFactory;
import edu.cronos.database.dao.DAOTurma;
import edu.cronos.entidades.Turma;
import edu.cronos.gui.Alerta;
import edu.cronos.gui.ColorManager;
import edu.cronos.horario.Horario;
import edu.cronos.util.ExportaHorario;
import edu.cronos.util.Observador;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.sql.SQLException;
import java.util.List;

/**
 * @author Sergio Lisan e Carlos Melo
 */
public class HorariosExibirPanel extends javax.swing.JPanel implements HorariosUIClient, Observador {

    private static HorariosExibirPanel instance = new HorariosExibirPanel();
    private final int DELAY = 500;
    private List<HorarioUI> calendarios;
    private Turma turma;
    private JPanel pnTurmas = new JPanel();
    private JPanel pnCalendarios = new JPanel();
    private JPanel pnHorarios = new JPanel();
    private JPanel pnLegendas = new JPanel();
    private JPanel pnLoading = new JPanel();
    private JLabel lbLoading = new JLabel();
    private JLabel setaDireita = new JLabel(">");
    private JLabel setaEsquerda = new JLabel("<");
    private JLabel lbVoltar = new JLabel("voltar");
    private JLabel lbSalvar = new JLabel("salvar");
    private JLabel lbPrint = new JLabel("imprimir");
    private Timer animacao;

    private HorariosExibirPanel() {
        initComponents();
        setLayout(new CardLayout());

        JScrollPane scrollTurmas = new JScrollPane(pnTurmas);
        scrollTurmas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollTurmas.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        scrollTurmas.setOpaque(false);
        scrollTurmas.setBorder(null);
        scrollTurmas.setMaximumSize(new Dimension(1300, 9000));
        scrollTurmas.setMinimumSize(new Dimension(900, 9000));
        scrollTurmas.setPreferredSize(new Dimension(900, 9000));

        scrollTurmas.getViewport().setMaximumSize(new Dimension(1300, 9000));
        scrollTurmas.getViewport().setMinimumSize(new Dimension(900, 9000));
        scrollTurmas.getViewport().setPreferredSize(new Dimension(900, 9000));

        scrollTurmas.getViewport().setOpaque(false);

        pnTurmas.setLayout(new FlowLayout(FlowLayout.LEFT));
        pnTurmas.setMinimumSize(new Dimension(900, 9000));
        pnTurmas.setPreferredSize(new Dimension(900, 9000));
        pnTurmas.setMaximumSize(new Dimension(1300, 9000));
        pnTurmas.setOpaque(false);

        createCalendarComponents();

        lbLoading.setPreferredSize(new Dimension(250, 120));
        lbLoading.setFont(new Font("Segoe UI", Font.PLAIN, 36));
        lbLoading.setForeground(new Color(61, 61, 61));

        pnLoading.setLayout(new FlowLayout(FlowLayout.CENTER, 75, 75));
        pnLoading.setOpaque(false);
        pnLoading.add(lbLoading);

        add(pnLoading, "LOADING");
        add(scrollTurmas, "TURMAS");
        add(pnCalendarios, "CALENDARIOS");

        try {
            CronosAPI.subscribe(Turma.class, this);
        } catch (Exception ex) {
            Alerta.jogarAviso(ex.getMessage());
        }


        show("TURMAS");
    }

    public static HorariosExibirPanel getInstance() {
        return instance;
    }

    /**
     * inicializa os components voltados para calendarios
     */
    private void createCalendarComponents() {
        lbVoltar.setPreferredSize(new Dimension(100, 25));
        lbVoltar.setOpaque(true);
        lbVoltar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbVoltar.setHorizontalAlignment(JLabel.CENTER);
        lbVoltar.setForeground(Color.white);
        lbVoltar.setBackground(ColorManager.getColor("button"));
        lbVoltar.addMouseListener(new HorariosUI.LinkHandler());
        lbVoltar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                show("TURMAS");
            }
        });

        lbSalvar.setPreferredSize(new Dimension(100, 25));
        lbSalvar.setOpaque(true);
        lbSalvar.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbSalvar.setHorizontalAlignment(JLabel.CENTER);
        lbSalvar.setForeground(Color.white);
        lbSalvar.setBackground(ColorManager.getColor("button"));
        lbSalvar.addMouseListener(new HorariosUI.LinkHandler());
        lbSalvar.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                saveHorario();
            }
        });

        lbPrint.setPreferredSize(new Dimension(100, 25));
        lbPrint.setOpaque(true);
        lbPrint.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        lbPrint.setHorizontalAlignment(JLabel.CENTER);
        lbPrint.setForeground(Color.white);
        lbPrint.setBackground(ColorManager.getColor("button"));
        lbPrint.addMouseListener(new HorariosUI.LinkHandler());
        lbPrint.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                print();
            }
        });

        JPanel toolbox = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        toolbox.setPreferredSize(new Dimension(1366, 40));
        toolbox.setOpaque(false);
        toolbox.add(lbVoltar);
        toolbox.add(lbPrint);

        pnHorarios.setLayout(new CardLayout());
        pnHorarios.setBackground(new Color(20, 20, 200, 1));

        setaDireita.setHorizontalAlignment(JLabel.CENTER);
        setaDireita.setOpaque(true);
        setaDireita.setBackground(Color.white);
        setaDireita.setFont(new Font("Segoe UI", Font.BOLD, 26));
        setaDireita.setPreferredSize(new Dimension(40, 150));
        setaDireita.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                next();
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                JLabel lb = (JLabel) evt.getSource();
                lb.setForeground(Color.WHITE);
                lb.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                JLabel lb = (JLabel) evt.getSource();
                lb.setForeground(Color.BLACK);
                lb.setBackground(Color.WHITE);
            }
        });

        setaEsquerda.setHorizontalAlignment(JLabel.CENTER);
        setaEsquerda.setOpaque(true);
        setaEsquerda.setBackground(Color.white);
        setaEsquerda.setFont(new Font("Segoe UI", Font.BOLD, 26));
        setaEsquerda.setPreferredSize(new Dimension(40, 150));
        setaEsquerda.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent evt) {
                previous();
            }

            @Override
            public void mouseEntered(MouseEvent evt) {
                JLabel lb = (JLabel) evt.getSource();
                lb.setForeground(Color.WHITE);
                lb.setBackground(Color.BLUE);
            }

            @Override
            public void mouseExited(MouseEvent evt) {
                JLabel lb = (JLabel) evt.getSource();
                lb.setForeground(Color.BLACK);
                lb.setBackground(Color.WHITE);
            }
        });

        JScrollPane scrollLegendas = new JScrollPane(pnLegendas);
        scrollLegendas.setBorder(null);
        scrollLegendas.setMaximumSize(new Dimension(810, 300));
        scrollLegendas.setPreferredSize(new Dimension(810, 300));
        scrollLegendas.setMinimumSize(new Dimension(810, 300));
        scrollLegendas.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        pnLegendas.setPreferredSize(new Dimension(810, 800));
        pnLegendas.setBackground(Color.WHITE);
        pnLegendas.setOpaque(true);

        pnCalendarios.setLayout(new BorderLayout());
        pnCalendarios.setOpaque(false);

        pnCalendarios.add(toolbox, BorderLayout.NORTH);
        pnCalendarios.add(setaDireita, BorderLayout.EAST);
        pnCalendarios.add(setaEsquerda, BorderLayout.WEST);
        pnCalendarios.add(pnHorarios, BorderLayout.CENTER);
        pnCalendarios.add(scrollLegendas, BorderLayout.SOUTH);
    }

    /**
     * carrega as turmas para forma de tiles
     *
     * @throws ClassNotFoundException
     * @throws SQLException
     */
    private void loadTurmas() {
        Thread t = new Thread(new HorariosUI.LoadTurmas(pnTurmas, this));
        t.start();
    }

    /**
     * Inicia a animacao de carregamento
     */
    private void startLoading() {
        show("LOADING");
        animacao = new Timer(DELAY, new HorariosUI.LoadingEffect(lbLoading));
        animacao.start();
    }

    /**
     * pausa a animacao de carregamento
     */
    private void stopLoading() {
        animacao.stop();
        animacao = null;
    }

    /**
     * troca a exibicao dos paines ou para TURMAS ou para CALENDARIOS
     *
     * @param panel
     */
    private void show(String panel) {
        ((CardLayout) getLayout()).show(this, panel);
    }

    /**
     * vai um calendario pra frente
     */
    private void next() {
        ((CardLayout) pnHorarios.getLayout()).next(pnHorarios);
    }

    /**
     * vai um calendario para tras
     */
    private void previous() {
        ((CardLayout) pnHorarios.getLayout()).previous(pnHorarios);
    }

    /**
     * Salva o horario no banco de dados
     */
    private void saveHorario() {
        try {
            DAOTurma dao = (DAOTurma) DAOFactory.getDao(Turma.class);
            dao.addHorario(turma);

            JOptionPane.showMessageDialog(null, "Salvo com sucesso!");
        } catch (ClassNotFoundException | SQLException e) {
            JOptionPane.showMessageDialog(null, "FAIL! Erro ao Salvar Horario:\n" + e);
            show("TURMAS");
        }
    }

    /**
     * Imprime o horario em PDF
     */
    private void print() {
        try {
            new ExportaHorario().exportarHorarioTurma(turma);
            JOptionPane.showMessageDialog(null, "Arquivo enviado para a Área de Trabalho");
        } catch (Exception ex) {
            Alerta.jogarAviso("Nao foi possivel gerar o arquivo Excel:\n" + ex);
            ex.printStackTrace(System.out);
        }
    }

    /**
     * Gera um horario a partir de um ID de uma turma, e exibe
     *
     * @param id
     */
    @Override
    public void action(final Integer id) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    pnLegendas.removeAll();
                    pnHorarios.removeAll();

                    turma = CronosAPI.get(Turma.class, id);
                    Horario horario = turma.getHorario();

                    if (!horario.isVazio()) {

                        HorarioUIFactory factory = new HorarioUIFactory(turma);
                        calendarios = factory.getCalendarios();

                        for (HorarioUI calendario : calendarios) {
                            pnHorarios.add(calendario, calendario.getMes().toLowerCase());
                        }

                        for (JLabel legenda : factory.getLegendas()) {
                            pnLegendas.add(legenda);
                        }

                        stopLoading();
                        show("CALENDARIOS");
                        ((CardLayout) pnHorarios.getLayout()).show(pnHorarios, calendarios.get(calendarios.size() - 1).getMes());
                    } else {
                        JOptionPane.showMessageDialog(null, "Horario inexistente! Tente criar um na opção 'gerar'. ");
                        stopLoading();
                        show("TURMAS");
                    }


                } catch (ClassNotFoundException | SQLException | HeadlessException e) {
                    JOptionPane.showMessageDialog(null, "FAIL! Erro ao Exibir Horario:\n" + e);
                    show("TURMAS");
                    e.printStackTrace(System.err);
                }
            }
        });

        thread.start();
        startLoading();
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        setBackground(new java.awt.Color(255, 255, 255));
        setMaximumSize(new java.awt.Dimension(1360, 600));
        setMinimumSize(new java.awt.Dimension(1015, 600));
        setOpaque(false);
        setPreferredSize(new java.awt.Dimension(1360, 600));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 1360, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
                layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGap(0, 600, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents
    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables

    @Override
    public void update() {
        loadTurmas();
    }
}
