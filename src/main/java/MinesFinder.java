import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MinesFinder extends JFrame {
    private JPanel painelPrincipal;
    private JButton jogoFácilButton;
    private JButton jogoMédioButton;
    private JButton jogoDifícilButton;
    private JButton sairButton;

    public MinesFinder(String title) {
        super(title);

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();

        sairButton.addActionListener(this::sairButtonActionPerformed);
        jogoFácilButton.addActionListener(this::btnJogoFacilActionPerformed);
        jogoMédioButton.addActionListener(this::btnJogoMédioActionPerformed);
        jogoDifícilButton.addActionListener(this::btnJogoDifícilActionPerformed);
    }

    private void sairButtonActionPerformed (ActionEvent e){
        System.exit(0);
    }

    private void btnJogoFacilActionPerformed(ActionEvent e) {
        new JanelaDeJogo(new CampoMinado(9,9, 10));
    }

    private void btnJogoMédioActionPerformed(ActionEvent e) {
        new JanelaDeJogo(new CampoMinado(16,16, 40));
    }
    private void btnJogoDifícilActionPerformed(ActionEvent e) {
        new JanelaDeJogo(new CampoMinado(16,30, 90));
    }

    public static void main (String[]args){
        // Construir a janela com o nome Mines Findee
        new MinesFinder("Mines Finder").setVisible(true);
    }
}
