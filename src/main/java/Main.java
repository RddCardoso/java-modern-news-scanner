import org.jsoup.Jsoup;
import java.io.IOException;
import java.util.List;

public class Main {
    public static void main(String[] args){
        String url = "https://news.ycombinator.com"; // Um site leve para testar

        try{
           // 1. Conectar ao site e obter o documento HTML
           var doc = Jsoup.connect(url).get();
           System.out.println("Ligado a: " + doc.title());

           // 2. Extrair notícias e transformar em objectos 'Artigo' usando Streams
            // 2.1. O "Íman": Procura no HTML todos os links (<a>) que estão dentro de elementos com a classe .titleline
            List<Artigo> noticias = doc.select(".titleline > a").stream()

                    // 2.2. O "Transfomador": Pega em cada elemento HTML bruto e cria um objecto 'Artigo' (Título + Link)
                    .map(elemento -> new Artigo(elemento.text(), elemento.absUrl("href")))

                    // 2.3. O "Filtro": Só deixa passar os artigos cujo título (em minúsculas) contenha "java OU "ai"
                    .filter(artigo -> artigo.titulo().toLowerCase().contains("java") ||
                                    artigo.titulo().toLowerCase().contains("ai"))

                    // 2.4. O "Empacotador": Recolhe todos os que passaram no filtro e guarda-os numa lista final
                    .toList(); // Java 16+ simplificou o .collect(Collectors.toList())

            // 3. Mostrar os resultados
            if(noticias.isEmpty()) {
                System.out.println("Nenhuma notícia relevante encontrada hoje.");
            } else {
                noticias.forEach(n -> System.out.println("🔥 " + n.titulo() + "\n🔗" + n.link() + "\n"));
            }

        } catch (IOException e) {
            System.err.println("Erro ao aceder ao site: " + e.getMessage());
        }
    }
}