package mlp;

import java.util.ArrayList;

public class Neuronio {
	
	private static final double bias = 1;
	public ArrayList<Double> entradas;
	public ArrayList<Double> pesos;
	public ArrayList<Double> deltaAntigo;
	public double saida;
	
	public Neuronio() {
		
		this.entradas = new ArrayList<Double>();
		this.pesos = new ArrayList<Double>();
		this.deltaAntigo = new ArrayList<Double>();
		
	}
	
	public void atualizarPeso(double delta, int i){
		this.pesos.set(i, this.pesos.get(i) + delta);
		this.deltaAntigo.set(i, delta);
	}
	
	protected double juncao_aditiva(){
		
		double valor = 0;
		
		for (int i = 0; i < entradas.size(); i++) {
			valor += entradas.get(i)*pesos.get(i);
		}
		return valor + bias;
	}
	
	public void funcao_de_ativacao(){
		
		this.saida = (1.0 / (1.0 + Math.exp(-juncao_aditiva()))); // FUNCAO NAO-SIMETRICA
		//this.saida = 1.7159f * Math.tanh(0.6666f * juncao_aditiva()); // FUNCAO HIPERBOLICA (ANTISSIMETRICA)
	}
	
	public double derivada(){
		
		double x = (1 / (1 + Math.exp(-juncao_aditiva())));
		
		return x * (1 - x);
		
	}

	public static double getBias() {
		return bias;
	}
}
