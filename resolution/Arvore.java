package resolution;
import java.util.ArrayList;
import java.util.List;

public class Arvore {
    private No raiz;
    private List<No> folhas;

    public Arvore(String conteudoRaiz) {
        this.raiz = new No(conteudoRaiz);
        this.folhas = new ArrayList<No>();
    }

    public No getRaiz() {
        return raiz;
    }

    public No buscar(String procurado) {
        return raiz.buscar(procurado);
    }
    
    public void caminhar(No no) {
    	if (no != null) {
    		System.out.println(no.getConteudo() + "\t" + no.getCusto() + "\t" + no.getCustoAcumulado());
    		List<No> filhos = no.getFilhos();
            for(No filho : filhos) {
                caminhar(filho);
            }
    	}
    }
    
    public List<No> getFolhas() {
    	caminharFolhas(this.raiz);
    	return this.folhas;
    }
    
    // Metodo interno utilizado pelo getFolhas para retornar nos sem filhos
    private void caminharFolhas(No no) {
    	if (no.equals(this.raiz))
    		this.folhas.clear();
    	if (no != null) {
    		List<No> filhos = no.getFilhos();
            for(No filho : filhos) {
            	caminharFolhas(filho);
            }
            if (filhos.isEmpty())
        		this.folhas.add(no);
    	}
    }
}