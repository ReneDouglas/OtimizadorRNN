package AG;

public class Populacao {

    private Individuo[] individuos;
    private IndividuoRNA[] individuosRNA;
    private int tamPopulacao;

    //cria uma população com indivíduos aleatória
    /*public Populacao(int numGenes, int tamPop) {
        tamPopulacao = tamPop;
        individuos = new Individuo[tamPop];
        for (int i = 0; i < individuos.length; i++) {
            individuos[i] = new Individuo(numGenes);
        }
    }*/

    //cria uma população com indivíduos aleatória
    public Populacao(int tamPop) {
        tamPopulacao = tamPop;
        individuosRNA = new IndividuoRNA[tamPop];
        for (int i = 0; i < individuosRNA.length; i++) {
            individuosRNA[i] = new IndividuoRNA();
        }
    }
    
    public Populacao(int tamPop, int genes) {
        tamPopulacao = tamPop;
        individuosRNA = new IndividuoRNA[tamPop];
        for (int i = 0; i < individuosRNA.length; i++) {
            individuosRNA[i] = null;
        }
    }
    
    /*public Populacao(int tamPop) {
        tamPopulacao = tamPop;
        individuosRNA = new IndividuoRNA[tamPop];
        for (int i = 0; i < individuosRNA.length; i++) {
            individuosRNA[i] = null;
        }
    }*/

    //coloca um indivíduo em uma certa posição da população
    public void setIndividuo(IndividuoRNA individuo, int posicao) {
        individuosRNA[posicao] = individuo;
    }

    //coloca um indivíduo na próxima posição disponível da população
    public void setIndividuo(IndividuoRNA individuo) {
        for (int i = 0; i < individuosRNA.length; i++) {
            if (individuosRNA[i] == null) {
                individuosRNA[i] = individuo;
                return;
            }
        }
    }

    //verifoca se algum indivíduo da população possui a solução
    public boolean temSolocao(double solucao) {
        IndividuoRNA i = null;
        
        for (int j = 0; j < individuosRNA.length; j++) {
			if(individuosRNA[j].aptidao <= solucao){
				i = individuosRNA[j];
				break;
			}
		}
        /*for (int j = 0; j < individuos.length; j++) {
            if (individuos[j].getGenes().equals(solucao)) {
                i = individuos[j];
                break;
            }
        }*/
        if (i == null) {
            return false;
        }
        return true;
    }

    //ordena a população pelo valor de aptidão de cada indivíduo, do maior valor para o menor, assim se eu quiser obter o melhor indivíduo desta população, acesso a posição 0 do array de indivíduos
    public void ordenaPopulacao() {
        boolean trocou = true;
        while (trocou) {
            trocou = false;
            for (int i = 0; i < individuosRNA.length - 1; i++) {
                if (individuosRNA[i].aptidao > individuosRNA[i + 1].aptidao) {
                    IndividuoRNA temp = individuosRNA[i];
                    individuosRNA[i] = individuosRNA[i + 1];
                    individuosRNA[i + 1] = temp;
                    trocou = true;
                }
            }
        }
    }


    //número de indivíduos existentes na população
    public int getNumIndividuos() {
        int num = 0;
        for (int i = 0; i < individuosRNA.length; i++) {
            if (individuosRNA[i] != null) {
                num++;
            }
        }
        return num;
    }

    public int getTamPopulacao() {
        return tamPopulacao;
    }

    public IndividuoRNA getIndivduo(int pos) {
        return individuosRNA[pos];
    }
}