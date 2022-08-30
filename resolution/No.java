package resolution;
import java.util.ArrayList;
import java.util.List;

public class No {
    private String conteudo;
    private List<No> filhos;
    private double custo;
    private double custoAcumulado;
    private String combinacao;
    private int altura;
    
    public No(String conteudo) {
        this.filhos = new ArrayList<>();
        this.conteudo = conteudo;
    }

    public No acrescentarFilho(No filho) {
        filhos.add(filho);
        return filho;
    }
    
    public String getConteudo() {
        return conteudo;
    }

    public List<No> getFilhos() {
        return filhos;
    }

    public No buscar(String procurado) {
        if (procurado.equals(conteudo)) return this;
        for (No filho : filhos) {
            No achou = filho.buscar(procurado);
            if (achou != null) return achou;
        }
        return null;
    }

	public double getCusto() {
		return custo;
	}

	public void setCusto(double custo) {
		this.custo = custo;
	}

	public double getCustoAcumulado() {
		return custoAcumulado;
	}

	public void setCustoAcumulado(double custoAcumulado) {
		this.custoAcumulado = custoAcumulado;
	}

	public String getCombinacao() {
		return combinacao;
	}

	public void setCombinacao(String combinacao) {
		this.combinacao = combinacao;
	}

	public int getAltura() {
		return altura;
	}

	public void setAltura(int altura) {
		this.altura = altura;
	}
}