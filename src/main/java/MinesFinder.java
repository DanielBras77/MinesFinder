import javax.swing.*;
import java.awt.event.ActionEvent;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class MinesFinder extends JFrame {
    private JPanel painelPrincipal;
    private JButton jogoFácilButton;
    private JButton jogoMédioButton;
    private JButton jogoDifícilButton;
    private JButton sairButton;
    private JLabel lblNomeFacil;
    private JLabel lblTempoFacil;
    private JLabel lblNomeMedio;
    private JLabel lblNomeDificil;
    private JLabel lblTempoMedio;
    private JLabel lblTempoDificil;

    private TabelaRecordes recordesFacil;
    private TabelaRecordes recordesMedio;
    private TabelaRecordes recordesDificil;

    public MinesFinder(String title) {
        super(title);

        setContentPane(painelPrincipal);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        pack();

        sairButton.addActionListener(this::sairButtonActionPerformed);
        jogoFácilButton.addActionListener(this::btnJogoFacilActionPerformed);
        jogoMédioButton.addActionListener(this::btnJogoMédioActionPerformed);
        jogoDifícilButton.addActionListener(this::btnJogoDifícilActionPerformed);

        this.recordesFacil = new TabelaRecordes();
        this.recordesMedio = new TabelaRecordes();
        this.recordesDificil = new TabelaRecordes();
        lerRecordesDoDisco();

        lblNomeFacil.setText(recordesFacil.getNome());
        lblTempoFacil.setText(Long.toString(recordesFacil.getTempo()/1000));
        lblNomeMedio.setText(recordesMedio.getNome());
        lblTempoMedio.setText(Long.toString(recordesMedio.getTempo()/1000));
        lblNomeDificil.setText(recordesDificil.getNome());
        lblTempoDificil.setText(Long.toString(recordesDificil.getTempo()/1000));

        recordesFacil.addTabelaRecordesListener(new TabelaRecordesListener() {
            @Override
            public void recordesActualizados(TabelaRecordes recordes) {
                recordesFacilActualizado(recordes);
            }
        });
        recordesMedio.addTabelaRecordesListener(new TabelaRecordesListener() {
            @Override
            public void recordesActualizados(TabelaRecordes recordes) {
                recordesMedioActualizado(recordes);
            }
        });
        recordesDificil.addTabelaRecordesListener(new TabelaRecordesListener() {
            @Override
            public void recordesActualizados(TabelaRecordes recordes) {
                recordesDificilActualizado(recordes);
            }
        });


    }

    private void recordesFacilActualizado(TabelaRecordes recordes) {
        lblNomeFacil.setText(recordes.getNome());
        lblTempoFacil.setText(Long.toString(recordes.getTempo()/1000));
    }

    private void recordesMedioActualizado(TabelaRecordes recordes) {
        lblNomeMedio.setText(recordes.getNome());
        lblTempoMedio.setText(Long.toString(recordes.getTempo()/1000));
    }

    private void recordesDificilActualizado(TabelaRecordes recordes) {
        lblNomeDificil.setText(recordes.getNome());
        lblTempoDificil.setText(Long.toString(recordes.getTempo()/1000));
    }

    private void guardarRecordesDisco() {
        ObjectOutputStream oos = null;
        try {
            File f =new File(System.getProperty("user.home")+ File.separator+"minesfinder.recordes");
            oos = new ObjectOutputStream(new FileOutputStream(f));
            oos.writeObject(recordesFacil);
            oos.writeObject(recordesMedio);
            oos.writeObject(recordesDificil);
            oos.close();
        } catch (IOException ex) {
            Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null,
                    ex);
        }
    }

    private void lerRecordesDoDisco() {
        ObjectInputStream ois = null;
        File f = new File(System.getProperty("user.home")+File.separator+"minesfinder.recordes");
        if (f.canRead()) {
            try {
                ois = new ObjectInputStream(new FileInputStream(f));
                recordesFacil=(TabelaRecordes) ois.readObject();
                recordesMedio=(TabelaRecordes) ois.readObject();
                recordesDificil=(TabelaRecordes) ois.readObject();
                ois.close();
            } catch (IOException ex) {
                Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null, ex);
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(MinesFinder.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }


    private void sairButtonActionPerformed (ActionEvent e){
        System.exit(0);
    }

    private void btnJogoFacilActionPerformed(ActionEvent e) {
        new JanelaDeJogo(new CampoMinado(9,9, 10), recordesFacil);
    }

    private void btnJogoMédioActionPerformed(ActionEvent e) {
        new JanelaDeJogo(new CampoMinado(16,16, 40), recordesMedio);
    }
    private void btnJogoDifícilActionPerformed(ActionEvent e) {
        new JanelaDeJogo(new CampoMinado(16,30, 90), recordesDificil);
    }

    public static void main (String[]args){
        // Construir a janela com o nome MinesFinder
        new MinesFinder("Mines Finder").setVisible(true);
    }
}
