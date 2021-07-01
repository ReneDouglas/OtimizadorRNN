package mlp;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Treinar {
	
	private double learning;
	private final double momentum;
	private ArrayList<Double> entradas_treinamento;
	private ArrayList<Double> entradas_validacao;
	private ArrayList<Double> entradas_teste;
	private ArrayList<Double> saidas_treinamento;
	private ArrayList<Double> saidas_validacao;
	private ArrayList<Double> saidas_teste;
	private int num_padroes;
	private double erro_treinamento = 0.0;
	private double erro_validacao = 0.0;
	public double erro_teste = 0.0;
	private double erro_medio_percentual_trein = 0.0;
	private double erro_medio_percentual_val = 0.0;
	public double erro_medio_percentual_test = 0.0;
	private ArrayList<Neuronio> neuronios;
	private MLP mlp;
	private BackPropagation bp;
	private ArrayList<Camada> camadas;
	//private Camada oculta;
	private Camada saida;
	private double entradas_trein[][], entradas_val[][], entradas_test[][];
	public double saida_desejada_teste[];
	public double saida_real_teste[];
	private FileWriter arq;
	private PrintWriter gravar;
	private SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm"); 
	private DecimalFormat df = new DecimalFormat("#.##");
	private NumberFormat nf = NumberFormat.getInstance();
	
	public Treinar(double taxa_aprend, double momento, int n_camadas, int n_por_cam[]) {
		
		this.learning = taxa_aprend;
		this.momentum = momento;
		
		//sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm");
		//System.out.println("Início: "+sdf.format(new Date()));
		
		cabecalho();
		preparar(this.learning, this.momentum, n_camadas, n_por_cam);
		treinamento();
		//validacao();
		teste();
		
		informacoes();

	}
	
	private void treinamento(){
		
		//this.num_padroes = this.entradas_treinamento.size();
		this.num_padroes = this.entradas_trein.length;
		this.mlp.randomizarPesos(num_padroes(entradas_trein)/*this.num_padroes*/, this.entradas_trein[0].length/*this.entradas_treinamento.size()*/);
		
		int epoca = 0;
		int controle_trein = 0;
		boolean loop = true;
		double erro_validacao_anterior = 1.0;
		/*double saida_desejada_trein[] = new double[this.entradas_treinamento.size()];
		double saida_real_trein[] = new double[this.entradas_treinamento.size()];
		double saida_desejada_validacao[] = new double[this.entradas_validacao.size()];
		double saida_real_validacao[] = new double[this.entradas_validacao.size()];*/
		double saida_desejada_trein[] = new double[this.entradas_trein.length];
		double saida_real_trein[] = new double[this.entradas_trein.length];
		double saida_desejada_validacao[] = new double[this.entradas_val.length];
		double saida_real_validacao[] = new double[this.entradas_val.length];
		
		do{
			//this.num_padroes = this.entradas_treinamento.size();
			this.num_padroes = this.entradas_trein.length;
			while(controle_trein < this.num_padroes){
					
				for (int i = 0; i < camadas.get(0).neuronios.size(); i++) {
					if(camadas.get(0).neuronios.get(i).entradas.size() == 0){
						for (int j = 0; j < this.entradas_trein[controle_trein].length; j++) {
							camadas.get(0).neuronios.get(i).entradas.add(entradas_trein[controle_trein][j]);
						}	
						
					}
					else{
						for (int j = 0; j < this.entradas_trein[controle_trein].length; j++) {
							camadas.get(0).neuronios.get(i).entradas.set(j, entradas_trein[controle_trein][j]);
						}
						
					}
				}
				
				/*for (int i = 0; i < oculta.neuronios.size(); i++) {
					if(oculta.neuronios.get(i).entradas.size() == 0){
						oculta.neuronios.get(i).entradas.add(this.entradas_treinamento.get(controle_trein));
					}
					else{
						oculta.neuronios.get(i).entradas.set(0, this.entradas_treinamento.get(controle_trein));
					}
				}*/
				
				this.bp.setSaidaDesejada(this.saidas_treinamento.get(controle_trein));
				this.bp.propagacao();
				this.bp.retropropagacao();
				
				saida_desejada_trein[controle_trein] = this.bp.saidaDesejada;
				saida_real_trein[controle_trein] = this.mlp.saidaReal;
				
				for (int i = 0; i < this.mlp.camadas.size(); i++) {
					this.mlp.camadas.get(i).gradiente_local.clear();
				}
				controle_trein++;
			}
			
			if(controle_trein >= this.saidas_treinamento.size())controle_trein = 0;
			
			this.erro_treinamento = this.bp.erro_medio_quad(num_padroes(entradas_trein));//matriz completa
			erro_medio_percentual_trein = this.bp.erro_percentual_absoluto_medio(num_padroes(entradas_trein));
			try {
				erro_medio_percentual_trein = nf.parse(df.format(erro_medio_percentual_trein)).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			this.bp.somatorio_energia.clear();
			this.bp.erro_percentual.clear();
			
			// ********** VALIDACAO **********
			
			//this.num_padroes = this.entradas_validacao.size();
			this.num_padroes = this.entradas_val.length;
			int controle_val = 0;
			
			while(controle_val < this.num_padroes){
				
				/*for (int i = 0; i < oculta.neuronios.size(); i++) {
					if(oculta.neuronios.get(i).entradas.size() == 0){
						oculta.neuronios.get(i).entradas.add(this.entradas_validacao.get(controle_val));
					}
					else{
						oculta.neuronios.get(i).entradas.set(0, this.entradas_validacao.get(controle_val));
					}
				}*/
				
				for (int i = 0; i < camadas.get(0).neuronios.size(); i++) {
					if(camadas.get(0).neuronios.get(i).entradas.size() == 0){
						for (int j = 0; j < this.entradas_val[controle_val].length; j++) {
							camadas.get(0).neuronios.get(i).entradas.add(entradas_val[controle_val][j]);
						}	
						
					}
					else{
						for (int j = 0; j < this.entradas_val[controle_val].length; j++) {
							camadas.get(0).neuronios.get(i).entradas.set(j, entradas_val[controle_val][j]);
						}
						
					}
				}
				
				//this.bp.setSaidaDesejada(this.saidas_validacao.get(controle_val));
				this.bp.setSaidaDesejada(this.saidas_validacao.get(controle_val));
				this.bp.propagacao();
				
				//saida_desejada_validacao[controle_val] = this.bp.saidaDesejada;
				saida_desejada_validacao[controle_val] = this.bp.saidaDesejada;
				saida_real_validacao[controle_val] = this.mlp.saidaReal;
				
				for (int i = 0; i < this.mlp.camadas.size(); i++) {
					this.mlp.camadas.get(i).gradiente_local.clear();
				}
					
				controle_val++;
				
			}
			controle_val = 0;
			epoca ++;
			
			this.erro_validacao = this.bp.erro_medio_quad(num_padroes(entradas_val));
			erro_medio_percentual_val = this.bp.erro_percentual_absoluto_medio(num_padroes(entradas_val));
			try {
				erro_medio_percentual_val = nf.parse(df.format(erro_medio_percentual_val)).doubleValue();
			} catch (ParseException e) {
				e.printStackTrace();
			}
			
			this.bp.somatorio_energia.clear();
			this.bp.erro_percentual.clear();
			
			/*if(epoca >= 1000){
				if(epoca%100==0){//10000
					//System.out.println("ET: "+this.erro_treinamento+"\tErro Validação: "+this.erro_validacao+"\t epocas: "+epoca);
					//this.gravar.printf("%d; %f %n", epoca, erro_validacao);
					if(this.erro_validacao >= erro_validacao_anterior) loop = false; 
					else erro_validacao_anterior = this.erro_validacao;
				}
			}else{
				if(epoca%1000==0){
					this.gravar.printf("%d; %f %n", epoca, erro_validacao);
				}
			}*/
			if(epoca >= 10000) loop = false;
			//this.gravar.printf("%d; %f %n", epoca, erro_validacao);
		
		}while(loop);
		
		//try { arq.close(); } catch (IOException e) {e.printStackTrace();}
		
		System.out.println("----------- Treinamento -----------");
		for (int i = 0; i < saida_desejada_trein.length; i++) {
			System.out.println("Desejada: "+saida_desejada_trein[i]+" Real: "+saida_real_trein[i]);
		}
		System.out.println("Epocas: "+epoca+" Erro Médio Quadrático: "+this.erro_treinamento+" RMSE: "+Math.sqrt(this.erro_treinamento)+" Erro Percentual Absoluto Médio: "+erro_medio_percentual_trein+"%");
		System.out.println();
		System.out.println("----------- Validação -----------");
		for (int i = 0; i < saida_desejada_validacao.length; i++) {
			System.out.println("Desejada: "+saida_desejada_validacao[i]+" Real: "+saida_real_validacao[i]);
		}
		System.out.println("Epocas: "+epoca+" Erro Médio Quadrático: "+this.erro_validacao+" RMSE: "+Math.sqrt(this.erro_validacao)+" Erro Percentual Absoluto Médio: "+erro_medio_percentual_val+"%");
		
	}
	
	private void teste(){
	
	this.num_padroes = this.entradas_test.length;
	//this.mlp.randomizarPesos(this.num_padroes);
	saida_desejada_teste = new double[this.entradas_test.length];
	saida_real_teste = new double[this.entradas_test.length];
	int epoca = 0;
	int controle = 0;
	
	while(controle < this.num_padroes){
				
		/*for (int i = 0; i < oculta.neuronios.size(); i++) {
			if(oculta.neuronios.get(i).entradas.size() == 0) oculta.neuronios.get(i).entradas.add(this.entradas_teste.get(controle));
			else oculta.neuronios.get(i).entradas.set(0, this.entradas_teste.get(controle));
		}*/
		
		for (int i = 0; i < camadas.get(0).neuronios.size(); i++) {
			if(camadas.get(0).neuronios.get(i).entradas.size() == 0){
				for (int j = 0; j < this.entradas_test[controle].length; j++) {
					camadas.get(0).neuronios.get(i).entradas.add(entradas_test[controle][j]);
				}
				
			}
			else{
				for (int j = 0; j < this.entradas_test[controle].length; j++) {
					camadas.get(0).neuronios.get(i).entradas.set(j, entradas_test[controle][j]);
				}
				
			}
		}
			
		this.bp.setSaidaDesejada(this.saidas_teste.get(controle));
		this.bp.propagacao();
		saida_desejada_teste[controle] = this.bp.saidaDesejada;
		saida_real_teste[controle] = this.mlp.saidaReal;
		//this.bp.retropropagacao();
		//this.bp.propagacao();
			
		for (int i = 0; i < this.mlp.camadas.size(); i++) {
			this.mlp.camadas.get(i).gradiente_local.clear();
		}
				
		controle++;
	}
		controle = 0;
		epoca ++;
		
		this.erro_teste = this.bp.erro_medio_quad(/*num_padroes*/num_padroes(entradas_test));
		erro_medio_percentual_test = this.bp.erro_percentual_absoluto_medio(num_padroes(entradas_test));
		try {
			erro_medio_percentual_test = nf.parse(df.format(erro_medio_percentual_test)).doubleValue();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		this.bp.somatorio_energia.clear();
		this.bp.erro_percentual.clear();
		
		System.out.println();
		System.out.println("----------- Teste -----------");

		
		for (int i = 0; i < saida_real_teste.length; i++) {
			System.out.println("Desejada: "+saida_desejada_teste[i]+" Real: "+saida_real_teste[i]);
		}
		System.out.println("Epocas: "+epoca+" Erro Médio Quadrático: "+this.erro_teste+" RMSE: "+Math.sqrt(this.erro_teste)+" Erro Percentual Absoluto Médio: "+erro_medio_percentual_test+"%");
		System.out.println();
		System.out.println("Término: "+sdf.format(new Date()));
		
	}
	
	private void preparar(double taxa_aprend, double momento, int n_camadas, int n_por_cam[]){
		
		//arquivo();
		
		this.entradas_treinamento = new ArrayList<>();
		this.entradas_validacao = new ArrayList<>();
		this.entradas_teste = new ArrayList<>();
		this.saidas_treinamento = new ArrayList<>();
		this.saidas_validacao = new ArrayList<>();
		this.saidas_teste = new ArrayList<>();
		this.neuronios = new ArrayList<>();
		
		this.mlp = new MLP();
		this.bp = new BackPropagation(this.mlp, taxa_aprend, momento);
		
		camadas = new ArrayList<Camada>();
		
		for (int i = 0; i < n_camadas; i++) {
			camadas.add(new Camada());
		}
		
		//this.oculta = new Camada();
		this.saida = new Camada();
		
		entradas_trein = new double[][]{
			
			/*{0.000, 0.0010661}, {0.0010661, 0.025586354}, {0.025586354, 0.086353945}, {0.086353945, 0.092750533},
			{0.092750533, 0.039445629}, {0.039445629, 0.011727079}, {0.011727079, 0.009594883}, {0.009594883, 0.007462687},
			{0.007462687, 0.003198294}, {0.003198294, 0.008528785},//2007 
			
			{0.008528785, 0.007462687}, {0.007462687, 0.001066098},
			{0.001066098, 0.002132196}, {0.002132196, 0.029850746}, {0.029850746, 0.171641791}, {0.171641791, 0.131130064},
			{0.131130064, 0.019189765}, {0.019189765, 0.009594883}, {0.009594883, 0.003198294}, {0.003198294, 0.002132196},
			{0.002132196, 0.00533049}, {0.00533049, 0.000},//2008 
			
			{0.000, 0.003198294}, {0.003198294, 0.013859275},{0.013859275, 0.009594883}, {0.009594883, 0.011727079}, 
			{0.011727079, 0.00533049}, {0.00533049, 0.001066098},{0.001066098, 0.000}, {0.000, 0.002132196}, 
			{0.002132196, 0.002132196}, {0.002132196, 0.009594883},{0.009594883, 0.00533049}, {0.00533049, 0.001066098}, //2009
		
			{0.001066098, 0.002132196}, {0.002132196, 0.003198294},{0.003198294, 0.003198294}, {0.003198294, 0.011727079},
			{0.011727079, 0.020255864}, {0.020255864, 0.079957356},{0.079957356, 0.143923241}, {0.143923241, 0.053304904}, 
			{0.053304904, 0.028784648}, {0.028784648, 0.008528785},{0.008528785, 0.015991471}, {0.015991471, 0.017057569}, //2010*/
			
			
			{0.000, 0.003}, {0.003, 0.013}, {0.013, 0.009}, {0.009, 0.011}, {0.011, 0.005}, {0.005, 0.001}, {0.001, 0.000},
			{0.000, 0.002}, {0.002, 0.002}, {0.002, 0.009}, {0.009, 0.005}, {0.005, 0.001},//2009
			
			{0.001, 0.002}, {0.002, 0.003}, {0.003, 0.003}, {0.003, 0.011}, {0.011, 0.019}, {0.019, 0.075}, {0.075, 0.135},
			{0.135, 0.050}, {0.050, 0.027}, {0.027, 0.008}, {0.008, 0.015}, {0.015, 0.016}, //2010
			
			{0.016, 0.056}, {0.056, 0.141}, {0.141, 0.231}, {0.231, 0.430}, {0.430, 0.187}, {0.187, 0.064}, {0.064, 0.034}, 
			{0.034, 0.015}, {0.015, 0.009}, {0.009, 0.013}, {0.013, 0.021}, {0.021, 0.035},//2011
			
			{0.035, 0.089}, {0.089, 0.109}, {0.109, 0.664}, {0.664, 0.938}, {0.938, 0.589}, {0.589, 0.156}, {0.156, 0.069}, 
			{0.069, 0.037}, {0.037, 0.007}, {0.007, 0.000}, {0.000, 0.008}, {0.008, 0.005},//2012
			
			
			
 					//{0.000, 0.001, 0.024, 0.081, 0.087, 0.037, 0.011, 0.009, 0.007, 0.003, 0.008, 0.007},//2007
					//{0.001, 0.002, 0.028, 0.161, 0.123, 0.018, 0.009, 0.003, 0.002, 0.005, 0.000, 0.003},//2008
					//{0.013, 0.009, 0.011, 0.005, 0.001, 0.000, 0.002, 0.002, 0.009, 0.005, 0.001, 0.002},//2009
					//{0.003, 0.003, 0.011, 0.019, 0.075, 0.135, 0.050, 0.027, 0.008, 0.015, 0.016, 0.056},//2010
					//{0.141, 0.231, 0.430, 0.187, 0.064, 0.034, 0.015, 0.009, 0.013, 0.021, 0.035, 0.089},//2011
					//{0.109, 0.664, 0.938, 0.589, 0.156, 0.069, 0.037, 0.007, 0.000, 0.008, 0.005, 0.006},//2012
		};
		
		entradas_val = new double[][]{
			
			{0.008, 0.007}, {0.007, 0.001}, {0.001, 0.002}, {0.002, 0.028}, {0.028, 0.161}, {0.161, 0.123}, {0.123, 0.018},
			{0.018, 0.009}, {0.009, 0.003}, {0.003, 0.002}, {0.002, 0.005}, {0.005, 0.000},// 2008
			
			
			/*{0.017057569, 0.059701493}, {0.059701493, 0.150319829}, {0.150319829, 0.246268657}, {0.246268657, 0.458422175},
			{0.458422175, 0.199360341}, {0.199360341, 0.068230277}, {0.068230277, 0.036247335}, {0.036247335, 0.015991471},
			{0.015991471, 0.009594883}, {0.009594883, 0.013859275}, {0.013859275, 0.02238806}, {0.02238806, 0.037313433},//2011*/
			//2011
					
		};
		
		entradas_test = new double[][]{
			
			{0.0, 0.001}, {0.001, 0.024}, {0.024, 0.081}, {0.081, 0.087}, {0.087, 0.037}, {0.037, 0.011}, {0.011, 0.009},
			{0.009, 0.007}, {0.007, 0.003}, {0.003, 0.008}, // 2007
			
			
			/*{0.037313433, 0.094882729}, {0.094882729, 0.116204691}, {0.116204691, 0.707889126}, {0.707889126, 1.0},
			{1.0, 0.62793177}, {0.62793177, 0.166311301}, {0.166311301, 0.073560768}, {0.073560768, 0.039445629},
			{0.039445629, 0.007462687}, {0.007462687, 0.000}, {0.000, 0.008}, {0.008528785, 0.00533049},//2012*/
			
		};
		
		this.saidas_treinamento.add(0.013);//2009
		this.saidas_treinamento.add(0.009);
		this.saidas_treinamento.add(0.011);
		this.saidas_treinamento.add(0.005);
		this.saidas_treinamento.add(0.001);
		this.saidas_treinamento.add(0.000);
		this.saidas_treinamento.add(0.002);
		this.saidas_treinamento.add(0.002);
		this.saidas_treinamento.add(0.009);
		this.saidas_treinamento.add(0.005);
		this.saidas_treinamento.add(0.001);
		this.saidas_treinamento.add(0.002);
		
		this.saidas_treinamento.add(0.003);//2010
		this.saidas_treinamento.add(0.003);
		this.saidas_treinamento.add(0.011);
		this.saidas_treinamento.add(0.019);
		this.saidas_treinamento.add(0.075);
		this.saidas_treinamento.add(0.135);
		this.saidas_treinamento.add(0.050);
		this.saidas_treinamento.add(0.027);
		this.saidas_treinamento.add(0.008);
		this.saidas_treinamento.add(0.015);
		this.saidas_treinamento.add(0.016);
		this.saidas_treinamento.add(0.056);
		
		this.saidas_treinamento.add(0.141);//2011
		this.saidas_treinamento.add(0.231);
		this.saidas_treinamento.add(0.430);
		this.saidas_treinamento.add(0.187);
		this.saidas_treinamento.add(0.064);
		this.saidas_treinamento.add(0.034);
		this.saidas_treinamento.add(0.015);
		this.saidas_treinamento.add(0.009);
		this.saidas_treinamento.add(0.013);
		this.saidas_treinamento.add(0.021);
		this.saidas_treinamento.add(0.035);
		this.saidas_treinamento.add(0.089);
		
		this.saidas_treinamento.add(0.109);//2012
		this.saidas_treinamento.add(0.664);
		this.saidas_treinamento.add(0.938);
		this.saidas_treinamento.add(0.589);
		this.saidas_treinamento.add(0.156);
		this.saidas_treinamento.add(0.069);
		this.saidas_treinamento.add(0.037);
		this.saidas_treinamento.add(0.007);
		this.saidas_treinamento.add(0.000);
		this.saidas_treinamento.add(0.008);
		this.saidas_treinamento.add(0.005);
		this.saidas_treinamento.add(0.006);
		
		this.saidas_validacao.add(0.001);//2008
		this.saidas_validacao.add(0.002);
		this.saidas_validacao.add(0.028);
		this.saidas_validacao.add(0.161);
		this.saidas_validacao.add(0.123);
		this.saidas_validacao.add(0.018);
		this.saidas_validacao.add(0.009);
		this.saidas_validacao.add(0.003);
		this.saidas_validacao.add(0.002);
		this.saidas_validacao.add(0.005);
		this.saidas_validacao.add(0.000);
		this.saidas_validacao.add(0.003);
		
		this.saidas_teste.add(0.024);//2007
		this.saidas_teste.add(0.081);
		this.saidas_teste.add(0.087);
		this.saidas_teste.add(0.037);
		this.saidas_teste.add(0.011);
		this.saidas_teste.add(0.009);
		this.saidas_teste.add(0.007);
		this.saidas_teste.add(0.003);
		this.saidas_teste.add(0.008);
		this.saidas_teste.add(0.007);
		
	
		
		/****** VALIDAÇÃO ******/
		/*this.entradas_treinamento.add(0.000);// 0.0
		this.entradas_treinamento.add(0.001);// 1.0
		this.entradas_treinamento.add(0.002);// 2.0
		this.entradas_treinamento.add(0.003);// 3.0
		this.entradas_treinamento.add(0.004);
		this.entradas_treinamento.add(0.005);
		this.entradas_validacao.add(0.006);// 4.0
		this.entradas_validacao.add(0.007);// 5.0
		this.entradas_teste.add(0.008);// 8.0
		this.entradas_teste.add(0.009);// 9.0*/
		
		/*this.saidas_treinamento.add(0.003);// 0.003
		this.saidas_treinamento.add(0.004);// 0.004
		this.saidas_treinamento.add(0.007);// 0.007
		this.saidas_treinamento.add(0.012);// 0.012
		this.saidas_treinamento.add(0.019);// 0.019
		this.saidas_treinamento.add(0.028);// 0.028
		this.saidas_validacao.add(0.039);// 0.039
		this.saidas_validacao.add(0.052);// 0.052
		this.saidas_teste.add(0.067);// 0.067
		this.saidas_teste.add(0.084);// 0.084*/
		/****** VALIDAÇÃO ******/
		
		Neuronio n;
		
		/*for (int i = 0; i < n_neuronios; i++) {
			n = new Neuronio();
			this.neuronios.add(n);
		}*/
		
		//System.out.println("neuronios: "+this.neuronios.size());
	
		for (int i = 0; i < n_camadas; i++) {
			for (int j = 0; j < n_por_cam[i]; j++) {
				camadas.get(i).neuronios.add(new Neuronio());
			}
		}
		
		this.saida.neuronios.add(new Neuronio());
		
		for (int i = 0; i < n_camadas; i++) {
			this.mlp.addCamada(camadas.get(i));
		}
		this.mlp.addCamada(this.saida);
		//System.out.println("camadas "+mlp.camadas.size());
		/*for (int i = 0; i < n_camadas; i++) {
			System.out.println("camada "+ i + "neuronios: "+ mlp.camadas.get(i).neuronios.size());
		}*/
	}
	
	private void informacoes(){
		
		System.out.println();
		System.out.println("---------------------- Informações ----------------------");
		System.out.println("Rede Neural(MLP) - Taxa de Aprendizagem: "+this.learning+" Momento: "+this.momentum);
		System.out.println("Qtd. de Neurônios por Camada Oculta: ");
		for (int i = 0; i < this.mlp.camadas.size(); i++) {
			System.out.println("Camada "+i+": "+this.mlp.camadas.get(i).neuronios.size());
		}
		System.out.println();
		System.out.println("---------- Pesos por Neurônio da Camada Oculta ----------");
		System.out.println();
		
		for (int i = 0; i < this.mlp.camadas.size(); i++) {
			System.out.println(">>> Camada "+i);
			for (int j = 0; j < this.mlp.camadas.get(i).neuronios.size(); j++){
				System.out.print("Neurônio: "+j+"\n");
				for (int k = 0; k < this.mlp.camadas.get(i).neuronios.get(j).pesos.size(); k++) {
					System.out.println("\tPeso: "+this.mlp.camadas.get(i).neuronios.get(j).pesos.get(k));
				}
			}
			
		}
		
		System.out.println("---------- Pesos por Neurônio da Camada de Saída ----------");
		System.out.println("Neurônio: 0 \tPeso:"+this.mlp.camadas.get(this.mlp.camadas.size()-1).neuronios.get(0).pesos.get(0));
	}
	
	private void cabecalho(){
		
		System.out.println("-----------------------------------");
		System.out.println("Universidade Federal Rural de Pernambuco(UFRPE)");
		System.out.println("Unidade Acadêmica de Serra Talhada(UAST)");
		System.out.println("Curso: Bacharelado em Sistemas de Informação");
		System.out.println("Trabalho de Conclusão de Curso");
		System.out.println("Aluno: Renê Douglas Nobre de Morais");
		System.out.println("Orientador: Glauber Magalhães Pires");
		System.out.println("-----------------------------------");
		System.out.println();
		System.out.println("Teste de Validação da Rede Neural Artificial - Multilayer Perceptrons (MLP)");
		System.out.println();
		
	}
	
	private void arquivo(){
		
		try {
			this.arq = new FileWriter("grafico.csv");
			this.gravar = new PrintWriter(arq);
			this.gravar.printf("epoca; erro%n");
		} catch (IOException e) {
			e.printStackTrace();
		}
		
	}
	
	private int num_padroes(double matriz[][]){
		
		int count = 0;
		
		for (int i = 0; i < matriz.length; i++) {
			for (int j = 0; j < matriz[i].length; j++) {
				count ++;
			}
		}
		return count;
	}
	
	public static void main(String[] args) {
		
		int v[] = new int[1];
		v[0] = 3;
		
		//96|410101010
		//62|110000000
		//62|410101010
		//learning, momentum, camadas, vetor de neuronios p camada
		Treinar t = new Treinar(0.5, 0.1, 1, v);
		//753010510 407.98
	}

}
