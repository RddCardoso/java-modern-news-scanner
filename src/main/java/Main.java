import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args){
        // 1. Criamos a instância do serviço
        ScraperService scraper = new ScraperService();
        List<Artigo> todasAsNoticias = new ArrayList<>();

        try{

            System.out.println("--- A extrair 10 Páginas do Pplware ---");

            for (int i= 1; i<=10; i++){
                //Criamos o URL dinamicamente: page/1, page/2, etc.
                String urlPaginada = "https://pplware.sapo.pt/page/" + i + "/";

                System.out.println("A ler página " + i + "...");

                List<Artigo> paginaAtual = scraper.extrairNoticias(urlPaginada, ".post-title a");
                todasAsNoticias.addAll(paginaAtual);

                // 1% de Cortesia: Não sobrecarregar o servidor (Sleep de 1 seg)
                Thread.sleep(1000);
            }

            // Remover duplicados globais que possam ter vindo de barras laterais/widgets
            List<Artigo> listaFinal = todasAsNoticias.stream().distinct().toList();

            System.out.println("Total de notícias extraídas: " + listaFinal.size());
            listaFinal.forEach(System.out::println);

        } catch (IOException | InterruptedException e) {
            System.err.println("Erro: " + e.getMessage());
        }
    }
}