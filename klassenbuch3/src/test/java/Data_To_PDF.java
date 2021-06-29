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
 * Das Dokument hat zwei Seiten und wird Text und ein Bild enthalten.
 *
 * Test ausführen, um das PDF zu erzeugen. Die Datei heißt:
 *
 * /test.pdf
 */
public class Data_To_PDF {

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
        // Implementierung der Thymeleaf Template-Engine
        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        // Die Daten in den Thymeleaf-Vorlagen sind nicht fest codiert. Stattdessen,
        // verwende ich Platzhalter in meinen Templates. Ich fülle diese Platzhalter
        // mit tatsächlichen Daten, indem ich ein Objekt übergeben.

        // Quelldatei
        Data data = (Data) CSV_Reader.getDataFromCSV(CSV_Chooser.csvFilePath());
        // Kontext für das Template
        Context context = new Context();

        // context.setVariable("data", data);

        //
        //int row = 0;
        //
        //for (int i = 0; i < CSV_Reader.getDataFromCSV.size(); i++) {
        //
        //    if(Epoch_Converter.convertEpoch(dataArrayList.get(i).getDateTime()).compareTo(date) >= 0){
        //        System.out.println("row = " + row);
        //        context.setVariable("WEEKDAY_" + row, dataArrayList.get(i).getWeekday());
        //        context.setVariable("DATE_" + row, Epoch_Converter.convertEpoch(dataArrayList.get(i).getDate()));
        //        context.setVariable("SUBJECT_" + row, dataArrayList.get(i).getSubject());
        //        context.setVariable("UNIT-1_" + row, dataArrayList.get(i).getUnit1());
        //        context.setVariable("UNIT-2_" + row, dataArrayList.get(i).getUnit2());
        //        context.setVariable("UNIT-3_" + row, dataArrayList.get(i).getUnit3());
        //        context.setVariable("UNIT-4_" + row, dataArrayList.get(i).getUnit4());
        //        context.setVariable("TEACHER-UNIT-1_" + row, dataArrayList.get(i).teacherUnit1());
        //        context.setVariable("TEACHER-UNIT-2_" + row, dataArrayList.get(i).teacherUnit2());
        //        context.setVariable("TEACHER-UNIT-3_" + row, dataArrayList.get(i).teacherUnit3());
        //        context.setVariable("TEACHER-UNIT-4_" + row, dataArrayList.get(i).teacherUnit4());
        //        row++;
        //    }
        //}


        // Flying Saucer braucht XHTML - nicht nur normales HTML. Um mir das Leben
        // einfacher zu machen, verwenden ich JTidy, um die gerenderte Thymeleaf-Vorlage nach
        // XHTML zu konvertieren. Dies kann bei sehr kompliziertem HTML möglicherweise nicht funktionierten. Aber
        // es ist gut genug für ein einfaches Dokument.

        String renderedHtmlContent = templateEngine.process("template", context);
        String xHtml = convertToXhtml(renderedHtmlContent);

        // https://github.com/flyingsaucerproject/flyingsaucer/blob/master/flying-saucer-pdf/src/main/java/org/xhtmlrenderer/pdf/ITextRenderer.java
        ITextRenderer renderer = new ITextRenderer();


        // FlyingSaucer hat ein Arbeitsverzeichnis. Wenn man diesen Test ausführt, ist das Arbeitsverzeichnis
        // das Stammverzeichnis des Projekts. Alle Dateien (HTML, CSS, etc.) befinden sich jedoch
        // unter "/src/test/resources".

        String baseUrl = FileSystems
                // getDefault() gibt das Standard-Dateisystem zurück. Das Standard-Dateisystem erzeugt Objekte, die den Zugriff auf die
                // für die virtuelle Java-Maschine zugänglichen Dateisysteme ermöglichen. Das Arbeitsverzeichnis des Dateisystems ist das
                // aktuelle Benutzerverzeichnis, benannt durch die Systemeigenschaft user.dir. Dies ermöglicht die Interoperabilität mit der Klasse java.io.File.
                // Der erste Aufruf einer der von dieser Klasse definierten Methoden findet das Standard-Provider-Objekt.
                // Wenn die Systemeigenschaft java.nio.file.spi.DefaultFileSystemProvider nicht definiert ist, ist der Standardanbieter ein System-Standardanbieter, der zum Erstellen des Standarddateisystems aufgerufen wird.
                .getDefault()
                // getPath() konvertiert eine Pfadzeichenfolge oder eine Folge von Zeichenfolgen, die zusammen eine Pfadzeichenfolge bilden, in einen Pfad.
                // typischerweise werden sie mit dem Namenstrennzeichen als Trennzeichen zusammengefügt. Wenn der Namenstrenner beispielsweise "/" ist und getPath("/foo", "bar", "nicole")
                // aufgerufen wird, wird die Pfadzeichenfolge "/foo/bar/nicole" in einen Pfad umgewandelt.
                .getPath("src", "test", "resources")
                // getPath() gibt einen URI zurück, die diesen Pfad repräsentiert.
                // Ein URI (Uniform Resource Identifier) ist eine eindeutige Zeichenfolge, die eine logische oder physische Ressource identifiziert, die von Webtechnologien verwendet wird.
                .toUri()
                // toURL() konstruiert eine URL aus dem URI
                .toURL()
                // toString() konstruiert eine String-Repräsentation dieser URL.
                .toString();
        renderer.setDocumentFromString(xHtml, baseUrl);
        renderer.layout();

        // Hier wird die PDF erstellt:
        OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
        renderer.createPDF(outputStream);
        outputStream.close();
    }



    static class Data {
        private String weekday;
        private String date;
        private String subject;
        private String unit1;
        private String teacherUnit1;
        private String unit2;
        private String teacherUnit2;
        private String unit3;
        private String teacherUnit3;
        private String unit4;
        private String teacherUnit4;

        public String getWeekday() {
            return weekday;
        }
        public void setWeekday(String weekday) {
            this.weekday = weekday;
        }

        public String getDate() {
            return date;
        }
        public void setDate(String date) {
            this.date = date;
        }

        public String getSubject() {
            return subject;
        }
        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getUnit1() {
            return unit1;
        }
        public void setUnit1(String unit1) {
            this.unit1 = unit1;
        }

        public String getTeacherUnit1() {
            return teacherUnit1;
        }
        public void setTeacherUnit1(String teacherUnit1) {
            this.teacherUnit1 = teacherUnit1;
        }

        public String getUnit2() {
            return unit2;
        }
        public void setUnit2(String unit2) {
            this.unit2 = unit2;
        }

        public String getTeacherUnit2() {
            return teacherUnit2;
        }
        public void setTeacherUnit2(String teacherUnit2) {
            this.teacherUnit2 = teacherUnit2;
        }

        public String getUnit3() {
            return unit3;
        }
        public void setUnit3(String unit3) {
            this.unit3 = unit3;
        }

        public String getTeacherUnit3() {
            return teacherUnit3;
        }
        public void setTeacherUnit3(String teacherUnit3) {
            this.teacherUnit3 = teacherUnit3;
        }

        public String getUnit4() {
            return unit4;
        }
        public void setUnit4(String unit4) {
            this.unit4 = unit4;
        }

        public String getTeacherUnit4() {
            return teacherUnit4;
        }
        public void setTeacherUnit4(String teacherUnit4) {
            this.teacherUnit4 = teacherUnit4;
        }
    }


    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        // Ein ByteArrayInputStream enthält einen internen Puffer, der Bytes enthält, die aus dem Stream gelesen werden können.
        // Ein interner Zähler hält das nächste Byte fest, das von der Read-Methode geliefert wird.
        // Das Schließen eines ByteArrayInputStream hat keine Auswirkung. Die Methoden in dieser Klasse können aufgerufen werden,
        // nachdem der Stream geschlossen wurde, ohne eine IOException zu erzeugen.
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        // Erzeugt einen neuen ByteArrayOutputStream. Die Pufferkapazität beträgt zunächst 32 Byte, die Größe wird bei Bedarf erhöht.
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        // Wandelt den Inhalt des Puffers in eine Zeichenkette um, indem die Bytes mit dem angegebenen Zeichensatz dekodiert werden.
        // Diese Methode ist äquivalent zu toString(charset), die einen Zeichensatz annimmt.
        return outputStream.toString(UTF_8);
    }
}