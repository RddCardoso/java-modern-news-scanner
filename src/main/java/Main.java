import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args){
        // 1. Criamos a instância do serviço
        ScraperService scraper = new ScraperService();
        List<Artigo> noticias = null;

        try{
            // 2. Pedimos ao serviço para fazer o trabalho
            noticias = scraper.extrairNoticias("https://news.ycombinator.com/", ".titleline > a");

            // 3. Mostramos o resultado
            if (noticias != null){
                noticias.forEach(System.out::println);
            }

        } catch (IOException e){
            System.err.println("Erro ao ligar ao serviço de notícias" + e.getMessage());
        }
    }
}