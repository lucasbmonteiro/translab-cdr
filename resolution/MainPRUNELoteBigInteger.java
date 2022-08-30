package resolution;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MainPRUNELoteBigInteger {
	
	// Variaveis de controle para armazenar a combinacao de trajetorias otima (caminho ate um no folha).
	static String menor;
	static double min;
	static boolean interromper = false;

	// Armazenam estatisticas do algoritmo
	static int qtdNos;
	static BigInteger qtdCombinacoes = new BigInteger("0");
	
	public static void main(String [] args) throws IOException {
		
		FileWriter arq = new FileWriter("d:\\DFS_Prune_Saida.txt");
	    PrintWriter gravarArq = new PrintWriter(arq);
		
		// Quantas vezes cada cenario (mesma quantidade de voos e trajetorias) sera executada
		int repeticoes = 30;
		
		int limiteTempo = 120000; // em milissegundos
		
		// Cenarios de teste (voos por rodada) 
		int [] qtdVoos = {3, 3, 3, 3, 3, 3, 3, 3, 3, 
						  9, 9, 9, 9, 9, 9, 9, 9, 9, 
						  15, 15, 15, 15, 15, 15, 15, 15, 15,
						  20, 20, 20, 20, 20, 20, 20, 20, 20,
						  30, 30, 30, 30, 30, 30, 30, 30, 30};

		// Cenarios de teste (trajetorias por voo)
		int [] qtdTrajetorias = {3, 10, 20, 30,
								 3, 10, 20, 30,
								 3, 10, 20, 30,
								 3, 10, 20, 30,
								 3, 10, 20, 30};

		int cenarios = 0;
		
		if (qtdVoos.length < qtdTrajetorias.length)
			cenarios = qtdVoos.length; // quantidade de posicoes nos dois arrays acima
		else 
			cenarios = qtdTrajetorias.length; // quantidade de posicoes nos dois arrays acima
		
		String saida = "";
		String notacaoCientifica = "";
		String notacaoCientifica2 = "";
		
		long duracao = 0;
		long duracaoTotal = 0;
	    BigInteger estadosPossiveis = new BigInteger("0");
	    BigInteger estadosVerificados = new BigInteger("0");
	    
	    double duracaoAvg = 0;
	    
	    int pulado = 0;
	    
		for (int i = 0; i < cenarios; i++) {
			for (int j = 1; j <= repeticoes; j++) {
				
				if (duracao > limiteTempo && qtdVoos[i] == pulado)
			    	break;
				else {
					pulado = 0;
					duracao = 0;
				}
				
				saida = qtdVoos[i] + "_" + qtdTrajetorias[i] + "_" + j + " ::: ";
				
				saida += dfs(qtdVoos[i], qtdTrajetorias[i], limiteTempo);
				
				if (interromper) {
					duracao = Integer.MAX_VALUE;
					pulado = qtdVoos[i];
					break;
				}
				else {
					duracaoTotal += Long.parseLong(saida.substring(saida.indexOf("ms") + 5, saida.indexOf("estados possiveis") - 3));
				    estadosPossiveis = estadosPossiveis.add(new BigInteger(saida.substring(saida.indexOf("possiveis") + 11, saida.indexOf("estados verificados") - 3)));
				    estadosVerificados = estadosVerificados.add(new BigInteger(saida.substring(saida.indexOf("verificados") + 13, saida.indexOf("selecionado") - 3)));
				    
				    duracao = Long.parseLong(saida.substring(saida.indexOf("ms") + 5, saida.indexOf("estados possiveis") - 3));
				}
			    
			    if (duracao > limiteTempo) {
			    	pulado = qtdVoos[i];
			    	break;
			    }
			    
				System.out.println(saida);
				gravarArq.print(saida);
				gravarArq.print("\n");
			}
			
			if (!interromper) {
				estadosPossiveis = estadosPossiveis.divide(new BigInteger(Long.toString(repeticoes)));
				
				if (estadosPossiveis.toString().length() < 2)
					notacaoCientifica = estadosPossiveis.toString();
				else
					notacaoCientifica = "10^" + ((int) estadosPossiveis.toString().length() - 1);
				
				
				estadosVerificados = estadosVerificados.divide(new BigInteger(Long.toString(repeticoes)));
				
				if (estadosVerificados.toString().length() < 2)
					notacaoCientifica2 = estadosVerificados.toString();
				else
					notacaoCientifica2 = "10^" + ((int) estadosVerificados.toString().length() - 1);
			
			}
			
			interromper = false;
				
			if (duracao > limiteTempo) {				
				System.out.println("ABORTANDO... " + qtdVoos[i] + "_" + qtdTrajetorias[i] + "\n");
				gravarArq.print("ABORTANDO... " + qtdVoos[i] + "_" + qtdTrajetorias[i]);
				gravarArq.print("\n");
				gravarArq.print("\n");
				
			}
			else {
				duracaoAvg = (double) duracaoTotal / (double) repeticoes;
				
				saida = ">>> " + qtdVoos[i] + "_" + qtdTrajetorias[i] + "_" + repeticoes + " ::: " + 
						  "duracao avg (ms): " + String.format("%,.2f", duracaoAvg) + 
					   " | estados possiveis: " + notacaoCientifica + 
					   " | estados verificados: " + notacaoCientifica2;
				
				System.out.println(saida + "\n");
				gravarArq.print(saida);
				gravarArq.print("\n");
				gravarArq.print("\n");
			}
			
			estadosPossiveis = new BigInteger("0");
			estadosVerificados = new BigInteger("0");
			duracaoTotal = 0;
		}
		
		arq.close();
	}

	public static String dfs(int qtdVoos, int qtdTrajetorias, int limiteTempo) {
		String saida = "";
		
		Arvore arvore = new Arvore("root");
		
		menor = "";
		min = 0;
		qtdNos = 0;
		qtdCombinacoes = new BigInteger("0");
		
		// Armazena os voos envolvidos no conflito. Cada letra indica um voo.
		List<String> voos = new ArrayList<String>();
		
		for (int i = 65; i < (65 + qtdVoos); i++)
			voos.add(String.valueOf((char) i));
	    	
		// Armazena a quantidade de trajetorias para cada conflito, por exemplo, A_0, A_1, ... A_149.
		int qtdEstrategias = qtdTrajetorias;
		
		DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
		Date date = new Date();
		// System.out.println(dateFormat.format(date) + "\tCalculando...");
		saida = dateFormat.format(date);
		
		double agora = System.currentTimeMillis();
				
		// Inicializa o no raiz
		arvore.getRaiz().setCombinacao("");
		arvore.getRaiz().setAltura(0);
		
		// Percorre recursivamente (em profundidade) cada combinacao de estrategias
		for (int i = 0; i < qtdEstrategias; i++) {
			percorre(arvore.getRaiz(), i, voos, qtdEstrategias, limiteTempo, agora);
			
			if (interromper)
				return "";
		}
		
		agora = System.currentTimeMillis() - agora;

		date = new Date();
		
		BigInteger espBusca = new BigInteger(Integer.toString(qtdEstrategias));
		espBusca = espBusca.pow(voos.size());
		
		saida += " | duracao (ms): " + (long) agora + 
				 " | estados possiveis: " + espBusca + 
				 " | estados verificados: " + qtdCombinacoes + 
				 " | selecionado: " + menor + 
				 " | custo: " + String.format("%,.2f", min);
	
		return saida;
	}
	
	private static void percorre(No no, int ordem, List<String> voos, int qtdEstrategias, int limiteTempo, double inicio) {
		if (System.currentTimeMillis() - inicio > limiteTempo) {
			interromper = true;
		}
		
		if (!interromper) {
			// Seta o nome do voo (por exemplo, A_1)
			No novo = new No(voos.get(no.getAltura()) + "_" + ordem);
			
			// Define um custo aleatorio para essa trajetoria. 
			novo.setCusto(Math.random() * 100);
			novo.setCustoAcumulado(novo.getCusto() + no.getCustoAcumulado());
			novo.setAltura(no.getAltura() + 1);
			
			// Monta a string que armazena a combinacao de trajetorias.
			if (no.getCombinacao().equals(""))
				novo.setCombinacao(novo.getConteudo());
			else
				novo.setCombinacao(no.getCombinacao() + ":" + novo.getConteudo());
			
			qtdNos++;
			
			// Algoritmo em si, que verifica se a busca deve continuar ou se o valor atual ja
			// e maior do que o minimo encontrado, ou seja, ponto de poda.
			if (novo.getAltura() == voos.size()) {
				qtdCombinacoes = qtdCombinacoes.add(new BigInteger("1"));
				if (min == 0) {
					min = novo.getCustoAcumulado();
					menor = novo.getCombinacao();
				} else
					if (min > novo.getCustoAcumulado()) {
						min = novo.getCustoAcumulado();
						menor = novo.getCombinacao();
					}
			} else {
				if (novo.getCustoAcumulado() < min || min == 0) {
					for (int i = 0; i < qtdEstrategias; i++) {
						percorre(novo, i, voos, qtdEstrategias, limiteTempo, inicio);
					}
				}
			}
			
		}
	}
}