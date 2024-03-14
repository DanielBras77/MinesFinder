import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class JanelaDeJogo extends JFrame{
    private JPanel painelJogo;
    private BotaoCampoMinado[][] botoes;
    private CampoMinado campoMinado;
    private TabelaRecordes recordes;

    public JanelaDeJogo(CampoMinado campo, TabelaRecordes tabela) {

        this.campoMinado = campo;
        var nrLinhas = campoMinado.getNrLinhas();
        var nrColunas = campoMinado.getNrColunas();
        this.campoMinado = campo;
        this.recordes = tabela;
        this.botoes = new BotaoCampoMinado[nrLinhas][nrColunas];
        painelJogo.setLayout(new GridLayout(nrLinhas, nrColunas));
        MouseListener mouseListener = new MouseListener() {
            @Override
            public void mouseClicked(MouseEvent e) {
            }
            @Override
            public void mousePressed(MouseEvent e) {
                if (e.getButton() != MouseEvent.BUTTON3) {
                    return;
                }
                var botao = (BotaoCampoMinado) e.getSource();
                var x = botao.getLinha();
                var y = botao.getColuna();
                var estadoQuadricula = campoMinado.getEstadoQuadricula(x, y);
                marcarQuadricula(x,y);
            }
            @Override
            public void mouseReleased(MouseEvent e) {
            }
            @Override
            public void mouseEntered(MouseEvent e) {
            }
            @Override
            public void mouseExited(MouseEvent e) {
            }
        };

        var keyListener = new KeyListener() {
            @Override
            public void keyTyped(KeyEvent e) {
            }
            @Override
            public void keyPressed(KeyEvent e) {
                var botao = (BotaoCampoMinado) e.getSource();
                var x = botao.getLinha(); // ou var linha = botao.getLinha();
                var y = botao.getColuna(); // ou var coluna = botao.getColuna();
                /*
                NOTA: Usámos as variáveis com nomes x, y para ser mais resumido no PDF, mas esta não é uma boa
                prática. Prefira os nomes linha, coluna sempre que puder, para não confundir e introduzir
                possíveis bugs. Isto porque na mente humana utilizamos muito o x na matemática para o eixo
                horizontal e o y para o eixo vertical. Visto que em programação não existe essa associação,
                sendo até o contrário (como é o presente caso abaixo), torna-se fácil cometer erros.
                Já os nomes linha, coluna nunca confundem o programador e sabemos perfeitamente em que situação
                incrementar cada variável respetivamente.
                Por exemplo: Ao subir com o teclado (VK_UP) o x decrementa um valor. Isto é estranho porque
                visualizamos o x no eixo horizontal a andar para a esquerda (sendo isso o VK_LEFT). Mas se o
                código estiver escrito da seguinte forma:
                case KeyEvent.VK_UP -> botoes[--linha < 0 ? nrLinhas - 1 : linha][coluna].requestFocus();
                Agora o código é mais intuitivo e menos propenso a erros.

                */
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP -> botoes[--x < 0 ? nrLinhas - 1 :
                            x][y].requestFocus();
                    case KeyEvent.VK_DOWN -> botoes[(x + 1) %
                            nrLinhas][y].requestFocus();
                    case KeyEvent.VK_LEFT -> botoes[x][--y < 0 ? nrColunas - 1 :
                            y].requestFocus();
                    case KeyEvent.VK_RIGHT -> botoes[x][(y + 1) %
                            nrColunas].requestFocus();
                    case KeyEvent.VK_M -> {
                        marcarQuadricula(x,y);
                    }
                }
            }
            @Override
            public void keyReleased(KeyEvent e) {
            }
        };

        // Criar e adicionar os botões à janela
        for (int linha = 0; linha < nrLinhas; ++linha) {
            for (int coluna = 0; coluna < nrColunas; ++coluna) {

                botoes[linha][coluna] = new BotaoCampoMinado(linha, coluna);
                botoes[linha][coluna].addActionListener(this::btnCampoMinadoActionPerformed);
                botoes[linha][coluna].addMouseListener(mouseListener);
                botoes[linha][coluna].addKeyListener(keyListener);
                painelJogo.add(botoes[linha][coluna]);
            }
        }

        setContentPane(painelJogo);

        // Destrói esta janela, removendo-a completamente da memória.
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        // Causes this Window to be sized to fit the preferred size and layouts of its subcomponents.
        pack();
        setVisible(true);
    }

    public void btnCampoMinadoActionPerformed(ActionEvent e) {
        var botao = (BotaoCampoMinado) e.getSource();
        var x = botao.getLinha();
        var y = botao.getColuna();
        campoMinado.revelarQuadricula(x, y);
        actualizarEstadoBotoes();

        if (campoMinado.isJogoTerminado()) {
            if (campoMinado.isJogadorDerrotado()) {
                JOptionPane.showMessageDialog(null, "Oh, rebentou uma mina", "Perdeu!", JOptionPane.INFORMATION_MESSAGE);
            }
            else{
                JOptionPane.showMessageDialog(null,
                        "Parabéns. Conseguiu descobrir todas as minas em "+ (campoMinado.getDuracaoJogo()/1000)+" segundos",
                        "Vitória", JOptionPane.INFORMATION_MESSAGE);


                boolean novoRecorde=campoMinado.getDuracaoJogo()<recordes.getTempo();
                if (novoRecorde) {
                    String nome=JOptionPane.showInputDialog("Introduza o seu nome");
                    recordes.setRecorde(nome, campoMinado.getDuracaoJogo());
                }

                setVisible(false);
            }
        }
    }

    private void actualizarEstadoBotoes() {
        for (int x = 0; x < campoMinado.getNrLinhas(); x++) {
            for (int y = 0; y < campoMinado.getNrColunas(); y++) {
                botoes[x][y].setEstado(campoMinado.getEstadoQuadricula(x, y));
            }
        }
    }

    private void marcarQuadricula(int x, int y){
        switch (campoMinado.getEstadoQuadricula(x, y)) {
            case CampoMinado.TAPADO -> campoMinado.marcarComoTendoMina(x, y);
            case CampoMinado.MARCADO -> campoMinado.marcarComoSuspeita(x, y);
            case CampoMinado.DUVIDA -> campoMinado.desmarcarQuadricula(x, y);
        }
        actualizarEstadoBotoes();
    }
}
