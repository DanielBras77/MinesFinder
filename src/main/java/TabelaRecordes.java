import java.io.Serializable;
import java.util.ArrayList;

public class TabelaRecordes implements Serializable {

    private String nomeJogador;
    private long tempoJogo;

    // O transient serve para não guardar a lista de listeners
    private transient ArrayList<TabelaRecordesListener> listeners;

    public TabelaRecordes() {
        this.nomeJogador = "Anónimo";
        this.tempoJogo = 999999;
        listeners = new ArrayList<>();
    }

    public String getNome() {
        return nomeJogador;
    }

    public long getTempo() {
        return tempoJogo;
    }

    public void setRecorde(String nomeJogador, long tempoJogo) {
        if (tempoJogo < this.tempoJogo){
            this.nomeJogador = nomeJogador;
            this.tempoJogo = tempoJogo;
            notifyRecordesActualizados();
        }
    }

    public void addTabelaRecordesListener(TabelaRecordesListener list) {
        if (listeners == null) {
            listeners = new ArrayList<>();
        }
        listeners.add(list);
    }
    public void removeTabelaRecordesListener(TabelaRecordesListener list) {
        if (listeners != null){
            listeners.remove(list);
        }
    }

    private void notifyRecordesActualizados() {
        if (listeners != null) {
            for (TabelaRecordesListener list:listeners)
                list.recordesActualizados(this);
        }
    }
}
