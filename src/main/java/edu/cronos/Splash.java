package edu.cronos;

import edu.cronos.gui.custom.SplashPanel;

import javax.swing.*;
import java.awt.*;

/**
 * @author sergio lisan e carlos melo
 */
public class Splash extends JWindow {

    private static Splash instance = new Splash();
    public int MAX = 3;
    private Dimension dim = new Dimension(600, 250);
    private JProgressBar progressBar;
    private int progressStage = 0;

    /**
     * Construtor... Inicializa os componentes e seta como visivel a tela
     */
    private Splash() {
        setSize(dim);
        setLocationRelativeTo(null);
        setContentPane(new SplashPanel());
        init();
    }

    /*
     * Interfaces públicas
     */

    /**
     * Retorna uma instancia unica da Splash Screen
     *
     * @return
     */
    public static Splash getInstance() {
        return instance;
    }

    /**
     * Muda a mensagem de acordo com a evolucao do progresso
     *
     * @param progressStage
     */
    public void upBar() {
        progressBar.setValue(progressStage++);
    }

    /**
     * Inicia a aplicação
     */
    public void start() {
        setVisible(true);
    }

    /**
     * Finaliza este Splash
     */
    public void stop() {
        dispose();
    }

    /**
     * Método que cria os componentes do Splash
     * <p>
     * - A Imagem de fundo - O Label que mostra a fase do progresso - A Barra de
     * Progresso
     */
    private void init() {
        createProgressBar();

        getContentPane().setLayout(null);
        getContentPane().add(progressBar);
    }

    /**
     * Setando parametros da variavel 'progressBar'
     */
    private void createProgressBar() {
        progressBar = new JProgressBar();
        int width = (int) (dim.width * 0.55);
        int height = (int) (dim.height * 0.02);
        int x = (dim.width - width) / 2;
        int y = (int) (dim.height * 0.82);

        progressBar.setForeground(Color.white);
        progressBar.setBackground(Color.decode("0x87AADE"));
        progressBar.setBorder(null);
        progressBar.setBounds(x, y, width, height);
        progressBar.setMinimum(0);
        progressBar.setMaximum(MAX);
    }
}
