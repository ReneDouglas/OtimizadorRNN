package mlp;

import java.util.ArrayList;

public class Camada {
	
	public ArrayList<Neuronio> neuronios;
	public ArrayList<Double> saida;
	public ArrayList<Double> gradiente_local;
	
	public Camada() {
		
		this.neuronios = new ArrayList<Neuronio>();
		this.saida = new ArrayList<Double>();
		this.gradiente_local = new ArrayList<Double>();
	}
	
	public void addNeuronio(Neuronio neuronio){
		
		this.neuronios.add(neuronio);
		
	}
	
	private void saida_camada(){
		
		for (int i = 0; i < saida.size(); i++) {
			this.saida.add(neuronios.get(i).saida);
		}
		
	}

}
