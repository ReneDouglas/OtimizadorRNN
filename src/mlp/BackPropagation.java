package mlp;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class BackPropagation {
	
	private MLP mlp;
	public double saidaDesejada;
	private double erroMedioQuadratico;
	private double taxa_de_aprend;
	public double energia_do_erro;
	public ArrayList<Double> somatorio_energia;
	public ArrayList<Double> erro_percentual;
	public double momento;
	
	public BackPropagation(MLP mlp, double taxa_de_aprend, double momento) {
		
		this.mlp = mlp;
		this.taxa_de_aprend = taxa_de_aprend;
		this.momento = momento;
		this.somatorio_energia = new ArrayList<Double>();
		this.erro_percentual = new ArrayList<Double>();
		
		
	}
	
	public void setSaidaDesejada(double saida){
		
		this.saidaDesejada = saida;
	}
	
	public void propagacao(){
		
		for (int i = 0; i < mlp.camadas.size(); i++) {
			for (int j = 0; j < mlp.camadas.get(i).neuronios.size(); j++) {
				if(i > 0){
					for (int h = 0; h < mlp.camadas.get(i-1).neuronios.size(); h++) {
						if(mlp.camadas.get(i).neuronios.get(j).entradas.size() != mlp.camadas.get(i-1).neuronios.size()){
							mlp.camadas.get(i).neuronios.get(j).entradas.add(mlp.camadas.get(i-1).neuronios.get(h).saida);
						}
						else{
							for (int k = 0; k < mlp.camadas.get(i).neuronios.get(j).entradas.size(); k++) {
								mlp.camadas.get(i).neuronios.get(j).entradas.set(k, mlp.camadas.get(i-1).neuronios.get(h).saida);
							}
						}
					}
				}
				//System.out.println("camada: "+i+" pesos: "+mlp.camadas.get(i).neuronios.get(j).pesos.size());
				mlp.camadas.get(i).neuronios.get(j).funcao_de_ativacao();
				
				if(i == (mlp.camadas.size()-1)){
					this.mlp.saidaReal = mlp.camadas.get(mlp.camadas.size()-1).neuronios.get(j).saida; // FUNCAO NAO-SIMÉTRICA
					this.energia_do_erro = ((double)1/2 * ((this.saidaDesejada - this.mlp.saidaReal) * (this.saidaDesejada - this.mlp.saidaReal)));
					this.somatorio_energia.add(this.energia_do_erro);
					this.erro_percentual.add(((this.mlp.saidaReal - this.saidaDesejada)/this.mlp.saidaReal) * 100);
				}
			}
			
		}
	}
	
	public double erro_medio_quad(int num_padroes){
		
		double energia_total = 0.0;
		
		for (int i = 0; i < this.somatorio_energia.size(); i++) {
				energia_total += this.somatorio_energia.get(i);
				
		}
		this.erroMedioQuadratico = (double)1/num_padroes * energia_total;
		return this.erroMedioQuadratico;
	}
	
	public double erro_percentual_absoluto_medio(int num_padroes){
		 
		double somatorio_erro_percentual = 0.00;
		double EMP = 0.00;
		
		for (int i = 0; i < this.erro_percentual.size(); i++) {
			somatorio_erro_percentual += this.erro_percentual.get(i);
		}
		return Math.abs(EMP = somatorio_erro_percentual / num_padroes);
	}
	

	
	public void retropropagacao(){
		
		for (int i = mlp.camadas.size() - 1; i >= 0; i--) {		
			
			if(i == mlp.camadas.size() - 1){	
				
				for (int j = 0; j < mlp.camadas.get(i).neuronios.size(); j++) {
					
					mlp.camadas.get(i).gradiente_local.add((this.saidaDesejada - this.mlp.saidaReal) * 
							mlp.camadas.get(i).neuronios.get(j).derivada());
					
					for (int h = 0; h < mlp.camadas.get(i).neuronios.get(j).pesos.size(); h++) {
						
						this.mlp.camadas.get(i).neuronios.get(j).atualizarPeso(regra_delta(this.taxa_de_aprend, 
								mlp.camadas.get(i).gradiente_local.get(j), 
								mlp.camadas.get(i).neuronios.get(j).entradas.get(h),  this.momento, 
								mlp.camadas.get(i).neuronios.get(j).deltaAntigo.get(h)), h);
					}	
				}	
			}
			else{ //Se for um neuronio oculto
				//gradiente_local_j = Derivada(funcao_ativacao)_j * Somatório_k(gradiente_local_k * peso_k)
					double somatorio = 0.0;
					
					for (int j = 0; j < mlp.camadas.get(i).neuronios.size(); j++) {
						for (int k = 0; k < mlp.camadas.get(i+1).neuronios.size(); k++) {
							
							somatorio += mlp.camadas.get(i+1).gradiente_local.get(k) *
										mlp.camadas.get(i+1).neuronios.get(k).pesos.get(j);
						}
						
						mlp.camadas.get(i).gradiente_local.add(mlp.camadas.get(i).neuronios.get(j).derivada() * somatorio);
						
						for (int w = 0; w < mlp.camadas.get(i).neuronios.get(j).pesos.size(); w++) {
							//System.out.println("camadas: "+i+" neuronio: "+j+ " peso: "+w);
							mlp.camadas.get(i).neuronios.get(j).atualizarPeso(regra_delta(this.taxa_de_aprend,
									(mlp.camadas.get(i).gradiente_local.get(j)), 
									mlp.camadas.get(i).neuronios.get(j).entradas.get(w), this.momento,
									mlp.camadas.get(i).neuronios.get(j).deltaAntigo.get(w)), w);
						}
					}	
			}
			
		}
		
	}
	
	private double regra_delta(double n, double gradiente_local, double entrada, double momento, double deltaAntigo){		
		// Regra Delta = N * gradiente_local * funcao_de_ativacao
		return momento*deltaAntigo + n * gradiente_local * entrada;
		//return n * gradiente_local * entrada;	
	}

}
