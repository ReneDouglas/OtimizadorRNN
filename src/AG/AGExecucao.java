package AG;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class AGExecucao {
	

    public static void main(String[] args) {
    	
    	FileWriter arq = null;
    	PrintWriter gravar = null;
    	
    	try {
    		
    		arq = new FileWriter("C:\\Users\\Renê\\Dropbox\\Resultados\\grafico.csv");
			gravar = new PrintWriter(arq);
			gravar.printf("n de epocas; geracao; genes; aptidao; erro quadratico medio; erro percentual absoluto medio;  raiz do erro quadratico medio%n");
		} catch (IOException e) {
			e.printStackTrace();
		}

        //Define a solução
        AlgoritmoGenetico.solucao = (10.00); // erro de 10% na previsão
        //Define os caracteres existentes
        //AlgoritmoGenetico.setCaracteres("!,.:;?áÁãÃâÂõÕôÔóÓéêÉÊíQWERTYUIOPASDFGHJKLÇZXCVBNMqwertyuiopasdfghjklçzxcvbnm1234567890 ");
        //taxa de crossover de 60%
        AlgoritmoGenetico.setTaxaDeCrossover(0.60);
        //taxa de mutação inicial de 10%
        AlgoritmoGenetico.setTaxaDeMutacao(0.1);
        //elitismo
        boolean elitismo = true;
        //tamanho da população
        int tamPop = 60;//60
        //numero máximo de gerações
        int numMaxGeracoes = 300;//80

        //define o número de genes do indivíduo baseado na solução
        //int numGenes = AlgoritmoGenetico.getSolucao().length();

        //cria a primeira população aleatória
        Populacao populacao = new Populacao(tamPop);

        
        
        boolean temSolucao = false;
        int geracao = 0;

        System.out.println("Iniciando... "/*Aptidão da solução: "+AlgoritmoGenetico.solucao*/);
        
        //loop até o critério de parada
        while (!temSolucao && geracao < numMaxGeracoes) {
            geracao++;

            //cria nova populacao
            populacao = AlgoritmoGenetico.novaGeracao(populacao, elitismo);
            
            //AlgoritmoGenetico.setTaxaDeMutacao(AlgoritmoGenetico.getTaxaDeMutacao() - 0.0038);
            
            System.out.println("Geração " + geracao + " | Aptidão: " + populacao.getIndivduo(0).aptidao + " | Melhor: " + populacao.getIndivduo(0).genes);
            gravar.printf("%d; %d; %s; %f; %f; %s; %f%n",
            		5000,
            		geracao, 
            		populacao.getIndivduo(0).genes,
            		populacao.getIndivduo(0).aptidao,
            		populacao.getIndivduo(0).mse,
            		populacao.getIndivduo(0).mape+"%",
            		populacao.getIndivduo(0).rmse);
            //verifica se tem a solucao
            //temSolucao = populacao.temSolocao(AlgoritmoGenetico.solucao);
        }
        
        if (geracao == numMaxGeracoes) {
            System.out.println("Número Máximo de Gerações | " + populacao.getIndivduo(0).genes + " " + populacao.getIndivduo(0).aptidao);
        }

        if (temSolucao) {
            System.out.println("Encontrado resultado na geração " + geracao + " | " + populacao.getIndivduo(0).genes + " (Aptidão: " + populacao.getIndivduo(0).aptidao + ")");
        }
        try {
			arq.close();
			gravar.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}