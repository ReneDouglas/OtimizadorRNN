package AG;

import java.util.Random;

import mlp.Treinar;

public class IndividuoRNA {
	
	public String genes = "";
	public double aptidao = 0.0;
	public double mse;
	public double rmse;
	public double mape;
	public int tamGen = 3;
	public int maxCam = 2; //Nº máximo de camadas
	public int nNeuronPorCam = 25;
	
	public IndividuoRNA() {
		
		/* Calculo para descobrir qual será o tamanho do gene de acordo com o nº de camadas
		   os 3 primeiros genes são: taxa de aprendizagem, momentum e nº de camadas. Ex.: 963101010 */
		Random r = new Random();
		
		for (int i = 0; i < tamGen; i++) {	
			
			if(i != tamGen-1) genes += ""+(r.nextInt(8)+1);
			else genes += ""+(r.nextInt(maxCam-1)+1);
			
		}
		//tamGen = tamGen + maxCam * 2;
		
		int tam = Integer.parseInt(genes.substring(2));
		int n = 0;
		
		for (int i = 0; i < tam; i++) {
			
			n = r.nextInt(25)+1;//25
			
			if(n <= 9) genes += "0"+n;
			else genes += n;
			
		}
		
		geraAptidao();	
	}
	
	public IndividuoRNA(String genes) {
		// ADICIONAR MUTACAO
		this.genes = genes;
		Random r = new Random();
		
		if (r.nextDouble() <= AlgoritmoGenetico.getTaxaDeMutacao()) {
			
			this.genes = mutacao(genes);
		}
		
		geraAptidao();
	}
	
	public String mutacao(String a){
		
		StringBuilder s = new StringBuilder(a);
		Random r2 = new Random();
		int pos = r2.nextInt(a.length());
		String sobeum = "1";
		String sobedois = "2";
		String desceum = "8";
		
		if(pos == 2) mutacao(a);
		else{
			if(Character.toString(a.charAt(pos)).equals("0")){
				s.setCharAt(pos, sobeum.charAt(0));
			}
			else if(Character.toString(a.charAt(pos)).equals("1")){
				s.setCharAt(pos, sobedois.charAt(0));
			}
			else if(Character.toString(a.charAt(pos)).equals("9")){
				s.setCharAt(pos, desceum.charAt(0));
			}
			else{
				int c;
				c = Integer.parseInt(String.valueOf(a.charAt(pos)));
				if(r2.nextBoolean()==true){	
					c = c+1;
				} else c = c-1;
				s.setCharAt(pos, String.valueOf(c).charAt(0));
			}
		}
		return s.toString();
	}
	
	public void mutacao(){
		
		StringBuilder s = new StringBuilder(this.genes);
		Random r = new Random();
		
		if (r.nextDouble() <= AlgoritmoGenetico.getTaxaDeMutacao()) {
			
			Random r2 = new Random();
			int pos = r2.nextInt(this.genes.length());
			String sobeum = "1";
			String sobedois = "2";
			String desceum = "8";
			
			if(pos == 2 || pos == 3) mutacao();
			else{
				if(Character.toString(this.genes.charAt(pos)).equals("0")){
					s.setCharAt(pos, sobeum.charAt(0));
				}
				else if(Character.toString(this.genes.charAt(pos)).equals("1")){
					s.setCharAt(pos, sobedois.charAt(0));
				}
				else if(Character.toString(this.genes.charAt(pos)).equals("9")){
					s.setCharAt(pos, desceum.charAt(0));
				}
				else{
					int c;
					c = Integer.parseInt(String.valueOf(this.genes.charAt(pos)));
					if(r2.nextBoolean()==true){	
						c = c+1;
					} else c = c-1;
					s.setCharAt(pos, String.valueOf(c).charAt(0));
				}
			}
			this.genes = s.toString();
		}
	}
	//aptidao
	
	public void geraAptidao(){
		
		double taxa_aprendizagem;
		double momento;
		int n_camadas;
		int n[];
		//double saidas[];
		int a = 3;
		int b = 5;
		
		taxa_aprendizagem = (Double.parseDouble(genes.substring(0, 1)))/10;
		momento = (Double.parseDouble(genes.substring(1, 2)))/10;
		n_camadas = Integer.parseInt(genes.substring(2, 3));
		n = new int[n_camadas];
		
		for (int i = 0; i < n.length; i++) {
			n[i] = Integer.parseInt(genes.substring(a, b));
			a += 2;
			b += 2;
		}
		
		Treinar rna = new Treinar(taxa_aprendizagem, momento, n_camadas, n);
		//saidas = rna.saida_real_teste;
		this.mse = rna.erro_teste;
		this.mape = rna.erro_medio_percentual_test;
		this.rmse = Math.sqrt(this.mse);
		
		this.aptidao = this.mse;
		System.out.println(genes+" "+mape);
		
	}

}
