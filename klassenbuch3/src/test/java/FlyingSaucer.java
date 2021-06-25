import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

/**
 * Dies ist ein JUnit-Test, der ein PDF unter Verwendung von Flying Saucer
 * und Thymeleaf-Vorlagen erzeugt. Das PDF zeigt ein mit CSS gestyltes Dokument an.
 * Das Dokument hat zwei Seiten und wird Text und Bilder enthalten.
 *
 * Führe diesen Test aus, um das PDF zu erzeugen. Die Datei heißt:
 *
 * /test.pdf
 */
public class FlyingSaucer {

    private static final String OUTPUT_FILE = "test.pdf";
    private static final String UTF_8 = "UTF-8";

    @Test
    public void generatePdf() throws Exception {

        // Ich richte eine Thymeleaf-Rendering-Engine ein. Alle Thymeleaf-Vorlagen
        // sind HTML-basierte Dateien, die sich unter "src/test/resources" befinden. Neben der
        // der Haupt-HTML-Datei, gibt es auch Teilbereiche wie einen Footer oder
        // eine Kopfzeile. Man kann diese Teilbereiche in verschiedenen Dokumenten wiederverwenden.

        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(UTF_8);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Die Daten in den Thymeleaf-Vorlagen sind nicht fest codiert. Stattdessen,
        // verwende ich Platzhalter in meinen Templates. Ich fülle diese Platzhalter
        // mit tatsächlichen Daten, indem ich ein Objekt übergeben.

        Data data = CSV_Reader.getListFromCSV();

        Context context = new Context();
        context.setVariable("data", data);

        // Flying Saucer braucht XHTML - nicht nur normales HTML. Um mir das Leben
        // einfacher zu machen, verwenden ich JTidy, um die gerenderte Thymeleaf-Vorlage nach
        // XHTML zu konvertieren. Dies kann bei sehr kompliziertem HTML möglicherweise nicht funktionierten. Aber
        // es ist gut genug für ein einfaches Dokument.

        String renderedHtmlContent = templateEngine.process("template", context);
        String xHtml = convertToXhtml(renderedHtmlContent);

        ITextRenderer renderer = new ITextRenderer();


        // FlyingSaucer hat ein Arbeitsverzeichnis. Wenn man diesen Test ausführt, ist das Arbeitsverzeichnis
        // das Stammverzeichnis des Projekts. Alle Dateien (HTML, CSS, etc.) befinden sich jedoch
        // unter "/src/test/resources".

        String baseUrl = FileSystems
                .getDefault()
                .getPath("src", "test", "resources")
                .toUri()
                .toURL()
                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();

        // Hier wird die PDF erstellt:
        OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
        renderer.createPDF(outputStream);
        outputStream.close();
    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }
}