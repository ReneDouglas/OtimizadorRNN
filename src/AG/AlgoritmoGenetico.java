package AG;

import java.util.Random;

public class AlgoritmoGenetico {

    //private static String solucao;
	public static double solucao;
    private static double taxaDeCrossover;
    private static double taxaDeMutacao;
    private static String caracteres;

    public static Populacao novaGeracao(Populacao populacao, boolean elitismo) {
        Random r = new Random();
        //nova população do mesmo tamanho da antiga
        Populacao novaPopulacao = new Populacao(populacao.getTamPopulacao(), 0);

        //se tiver elitismo, mantém o melhor indivíduo da geração atual
        if (elitismo) {
            novaPopulacao.setIndividuo(populacao.getIndivduo(0));
        }

        //insere novos indivíduos na nova população, até atingir o tamanho máximo
        while (novaPopulacao.getNumIndividuos() < novaPopulacao.getTamPopulacao()) {
            //seleciona os 2 pais por torneio
            IndividuoRNA[] pais = selecaoTorneio(populacao);

            IndividuoRNA[] filhos = new IndividuoRNA[2];
            // pais continuam na proxima gera��o - ES estrat�gias evolucionarias (somente com muta��o)
            //verifica a taxa de crossover, se sim realiza o crossover, se não, mantém os pais selecionados para a próxima geração
            if (r.nextDouble() <= taxaDeCrossover) {
                filhos = crossover(pais[1], pais[0]);
            } else {
                filhos[0] = new IndividuoRNA(pais[0].genes);
                filhos[1] = new IndividuoRNA(pais[1].genes);
            }
            
            
            //adiciona os filhos na nova geração
            novaPopulacao.setIndividuo(filhos[0]);
            novaPopulacao.setIndividuo(filhos[1]);
        }
        
        //Calcula nova aptid�o dos indiv�duos
        //ordena a nova população
        novaPopulacao.ordenaPopulacao();
        return novaPopulacao;
    }

    	// MODIFICAR CROSSOVER
    public static IndividuoRNA[] crossover(IndividuoRNA individuo1, IndividuoRNA individuo2) {
        Random r = new Random();
        
        /*//sorteia o ponto de corte
        int pontoCorte1 = r.nextInt((individuo1.genes.length()/2) -2) + 1;
        int pontoCorte2 = r.nextInt((individuo1.genes.length()/2) -2) + individuo1.genes.length()/2;*/
 
        IndividuoRNA[] filhos = new IndividuoRNA[2];

        //pega os genes dos pais
        String genePai1 = individuo1.genes;
        String genePai2 = individuo2.genes;

        String geneFilho1 = "";
        String geneFilho2 = "";

        /*//realiza o corte, // 1234
        geneFilho1 = genePai1.substring(0, pontoCorte1);
        geneFilho1 += genePai2.substring(pontoCorte1, pontoCorte2);
        geneFilho1 += genePai1.substring(pontoCorte2, genePai1.length());
        
        geneFilho2 = genePai2.substring(0, pontoCorte1);
        geneFilho2 += genePai1.substring(pontoCorte1, pontoCorte2);
        geneFilho2 += genePai2.substring(pontoCorte2, genePai2.length());*/
        
        /******* CROSSOVER DE UM PONTO
		geneFilho1 += genePai2.substring(0, 2) + genePai1.substring(2);
		geneFilho2 += genePai1.substring(0, 2) + genePai2.substring(2);
		*****/
		//CROSSOVER DE DOIS PONTOS
		geneFilho1 += genePai1.substring(0, 1) + genePai2.substring(1, 2) + genePai1.substring(2);
		geneFilho2 += genePai2.substring(0, 1) + genePai1.substring(1, 2) + genePai2.substring(2);
        
        //cria o novo indivíduo com os genes dos pais
        filhos[0] = new IndividuoRNA(geneFilho1);
        filhos[1] = new IndividuoRNA(geneFilho2);

        return filhos;
    }

    public static IndividuoRNA[] selecaoTorneio(Populacao populacao) {
        Random r = new Random();
        Populacao populacaoIntermediaria = new Populacao(6, 0);

        //seleciona 6 indivíduos aleatóriamente na população
        for (int i = 0; i < 6; i++) {
        	populacaoIntermediaria.setIndividuo(populacao.getIndivduo(r.nextInt(populacao.getTamPopulacao())));
		}

        //ordena a população
        populacaoIntermediaria.ordenaPopulacao();
        
        IndividuoRNA[] pais = new IndividuoRNA[2];

        //seleciona os 2 melhores deste população
        pais[0] = populacaoIntermediaria.getIndivduo(0);
        pais[1] = populacaoIntermediaria.getIndivduo(1);

        return pais;
    }

    /*public static String getSolucao() {
        return solucao;
    }

    public static void setSolucao(String solucao) {
        AlgoritmoGenetico.solucao = solucao;
    }*/

    public static double getTaxaDeCrossover() {
        return taxaDeCrossover;
    }

    public static void setTaxaDeCrossover(double taxaDeCrossover) {
        AlgoritmoGenetico.taxaDeCrossover = taxaDeCrossover;
    }

    public static double getTaxaDeMutacao() {
        return taxaDeMutacao;
    }

    public static void setTaxaDeMutacao(double taxaDeMutacao) {
        AlgoritmoGenetico.taxaDeMutacao = taxaDeMutacao;
    }

    public static String getCaracteres() {
        return caracteres;
    }

    public static void setCaracteres(String caracteres) {
        AlgoritmoGenetico.caracteres = caracteres;
    }
    
    
}