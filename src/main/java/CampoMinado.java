import java.util.Random;

public class CampoMinado {

    private boolean[][] minas;

    public static final int VAZIO = 0;
    /* de 1 a 8 são o número de minas à volta */
    public static final int TAPADO = 9;
    public static final int DUVIDA = 10;
    public static final int MARCADO = 11;
    public static final int REBENTADO = 12;

    private int[][] estado;

    private int nrLinhas; // ou largura
    private int nrColunas; // ou altura
    private int nrMinas;

    private boolean jogoTerminado;
    private boolean jogadorDerrotado;

    private boolean primeiraJogada;

    private long instanteInicioJogo;
    private long duracaoJogo;


    public CampoMinado(int nrLinhas, int nrColunas, int nrMinas) {
        this.nrLinhas = nrLinhas;
        this.nrColunas = nrColunas;
        this.nrMinas = nrMinas;
        this.minas = new boolean[nrLinhas][nrColunas]; // Valores começam a false
        this.estado = new int[nrLinhas][nrColunas]; // Valores começam a 0
        this.jogoTerminado = false;
        this.jogadorDerrotado = false;
        this.primeiraJogada = true;

        for (var x = 0; x < nrLinhas; ++x) {
            for (var y = 0; y < nrColunas; ++y) {
                estado[x][y] = TAPADO;
            }
        }
    }

    public int getEstadoQuadricula(int x, int y) {
        return estado[x][y];
    }
    public boolean hasMina(int x, int y) {
        return minas[x][y];
    }

    public int getNrLinhas() {
        return nrLinhas;
    }

    public int getNrColunas() {
        return nrColunas;
    }

    public void revelarQuadricula(int x, int y) {

        if (jogoTerminado || estado[x][y] < TAPADO) {
            return;
        }

        if (primeiraJogada) {
            primeiraJogada = false;
            colocarMinas(x, y);

            instanteInicioJogo = System.currentTimeMillis();
        }

        if (minas[x][y]){

            estado[x][y] = REBENTADO;
            jogadorDerrotado = true;
            jogoTerminado = true;
            duracaoJogo = System.currentTimeMillis() - instanteInicioJogo;
        }
        else{

            int vizinhas = contarMinasVizinhas(x,y);

            if(vizinhas == 0){
                revelarQuadriculasVizinhas(x, y);
                estado[x][y] = 0;
            }
            else{
                estado[x][y] = vizinhas;
            }

            if (isVitoria()){
                jogoTerminado = true;
                jogadorDerrotado = false;
                duracaoJogo =System.currentTimeMillis() - instanteInicioJogo;
            }

        }
    }

    public void marcarComoTendoMina(int x, int y){
        if(estado[x][y] == TAPADO || estado[x][y] == DUVIDA){
            estado[x][y] = MARCADO;
        }
    }

    public void marcarComoSuspeita(int x, int y){
        if(estado[x][y] == TAPADO || estado[x][y] == MARCADO){
            estado[x][y] = DUVIDA;
        }
    }

    public void desmarcarQuadricula(int x, int y){
        if(estado[x][y] == MARCADO || estado[x][y] == DUVIDA){
            estado[x][y] = TAPADO;
        }
    }



    private void colocarMinas(int exceptoX, int exceptoY) {
        var aleatorio = new Random();
        var x = 0;
        var y = 0;
        for (var i = 0; i < nrMinas; ++i) {
            do {
                x = aleatorio.nextInt(nrLinhas);
                y = aleatorio.nextInt(nrColunas);
            } while (minas[x][y] || (x == exceptoX && y == exceptoY));

            minas[x][y] = true;
        }
    }

    private int contarMinasVizinhas(int x, int y) {
        var numMinasVizinhas = 0;
        for (var i = Math.max(0, x - 1); i < Math.min(nrLinhas, x + 2); ++i) {
            for (var j = Math.max(0, y - 1); j < Math.min(nrColunas, y + 2); ++j)
            {
                if (minas[i][j]) {
                    ++numMinasVizinhas;
                }
            }
        }
        return numMinasVizinhas;
    }

    private void revelarQuadriculasVizinhas(int x, int y) {

        for (var i = Math.max(0, x - 1); i < Math.min(nrLinhas, x + 2); ++i) {
            for (var j = Math.max(0, y - 1); j < Math.min(nrColunas, y + 2); ++j)
            {
                if (estado[i][j] >= TAPADO) { // Verificar se a quadricula ainda não foi revelada

                    int numMinasVizinhas = contarMinasVizinhas(i, j);
                    estado[i][j] = numMinasVizinhas;

                    if (numMinasVizinhas == 0) {
                        revelarQuadriculasVizinhas(i, j);
                    }
                }

            }
        }
    }

    private boolean isVitoria() {
        for (int i = 0; i < nrLinhas; ++i) {
            for (var j = 0 ; j < nrColunas; ++j) {
                if (!minas[i][j] && estado[i][j] >= TAPADO) {
                    return false;
                }
            }
        }
        return true;
    }

    public long getDuracaoJogo() {
        if (primeiraJogada) {
            return 0;
        }
        if (!jogoTerminado) {
            return System.currentTimeMillis() - instanteInicioJogo;
        }
        return duracaoJogo;
    }

    public boolean isJogoTerminado(){
        return jogoTerminado;
    }

    public boolean isJogadorDerrotado(){
        return jogadorDerrotado;
    }

}
