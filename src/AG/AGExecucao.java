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
    		
    		arq = new FileWriter("C:\\Users\\Ren�\\Dropbox\\Resultados\\grafico.csv");
			gravar = new PrintWriter(arq);
			gravar.printf("n de epocas; geracao; genes; aptidao; erro quadratico medio; erro percentual absoluto medio;  raiz do erro quadratico medio%n");
		} catch (IOException e) {
			e.printStackTrace();
		}

        //Define a solu��o
        AlgoritmoGenetico.solucao = (10.00); // erro de 10% na previs�o
        //Define os caracteres existentes
        //AlgoritmoGenetico.setCaracteres("!,.:;?�����������������QWERTYUIOPASDFGHJKL�ZXCVBNMqwertyuiopasdfghjkl�zxcvbnm1234567890 ");
        //taxa de crossover de 60%
        AlgoritmoGenetico.setTaxaDeCrossover(0.60);
        //taxa de muta��o inicial de 10%
        AlgoritmoGenetico.setTaxaDeMutacao(0.1);
        //elitismo
        boolean elitismo = true;
        //tamanho da popula��o
        int tamPop = 60;//60
        //numero m�ximo de gera��es
        int numMaxGeracoes = 300;//80

        //define o n�mero de genes do indiv�duo baseado na solu��o
        //int numGenes = AlgoritmoGenetico.getSolucao().length();

        //cria a primeira popula��o aleat�ria
        Populacao populacao = new Populacao(tamPop);

        
        
        boolean temSolucao = false;
        int geracao = 0;

        System.out.println("Iniciando... "/*Aptid�o da solu��o: "+AlgoritmoGenetico.solucao*/);
        
        //loop at� o crit�rio de parada
        while (!temSolucao && geracao < numMaxGeracoes) {
            geracao++;

            //cria nova populacao
            populacao = AlgoritmoGenetico.novaGeracao(populacao, elitismo);
            
            //AlgoritmoGenetico.setTaxaDeMutacao(AlgoritmoGenetico.getTaxaDeMutacao() - 0.0038);
            
            System.out.println("Gera��o " + geracao + " | Aptid�o: " + populacao.getIndivduo(0).aptidao + " | Melhor: " + populacao.getIndivduo(0).genes);
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
            System.out.println("N�mero M�ximo de Gera��es | " + populacao.getIndivduo(0).genes + " " + populacao.getIndivduo(0).aptidao);
        }

        if (temSolucao) {
            System.out.println("Encontrado resultado na gera��o " + geracao + " | " + populacao.getIndivduo(0).genes + " (Aptid�o: " + populacao.getIndivduo(0).aptidao + ")");
        }
        try {
			arq.close();
			gravar.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
    }
}