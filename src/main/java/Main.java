import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args){
        List<Artigo> noticias = null;
        try {
            noticias = extrairNoticias("https://news.ycombinator.com/", ".titleline > a");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Imprime cada notícia usando Method Reference (atalho moderno do Java)
        noticias.forEach(System.out::println);
    }

    public static List<Artigo> extrairNoticias(String url, String selector) throws IOException{
        var doc = Jsoup.connect(url).get();
        return doc.select(selector).stream()
                .map(elemento -> new Artigo(elemento.text(), elemento.absUrl("href")))
                .filter(artigo -> artigo.titulo().toLowerCase().contains("java") ||
                                artigo.titulo().toLowerCase().contains("ai"))
                .toList();
    }
}