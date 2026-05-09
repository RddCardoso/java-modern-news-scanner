import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.List;

public class ScraperService {
    // Removemos o 'static' para que isto seja um serviço que podemos instanciar
    public List<Artigo> extrairNoticias(String url, String seletor) throws IOException{
        var doc = Jsoup.connect(url)
                .userAgent("Mozilla/5.0 (Windows NT10.0; Win64; x64) Chrome/120.0.0.0" )
                .get();

        return doc.select(seletor).stream()
                .map(elemento -> new Artigo(elemento.text(), elemento.absUrl("href")))
                .filter(artigo -> artigo.titulo().toLowerCase().contains("java") ||
                                        artigo.titulo().toLowerCase().contains("ai"))
                .distinct()
                .toList();
    }
}