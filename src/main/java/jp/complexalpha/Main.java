package jp.complexalpha;

import jp.complexalpha.db.AddressInfo;
import jp.complexalpha.db.Select;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;
import org.apache.pdfbox.util.Matrix;

import java.io.File;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws Exception {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String now = LocalDateTime.now().format(format);
        System.out.println(now);

        String outDir = System.getenv("PDF_OUT_DIR");
        File outFile = new File(outDir + "/out_" + now + ".pdf");

        System.out.println(outFile);

        String fontPath = System.getenv("PDF_FONT");
        System.out.println(fontPath);

        ArrayList<AddressInfo> list = Select.getInfo();

        list.forEach(s -> System.out.println(s.name));

        try (PDDocument document = new PDDocument()) {

            PDFont font = PDType0Font.load(document, new File(fontPath));

            //サイズ指定
            PDRectangle rec = new PDRectangle();
            rec.setUpperRightX(283.46458f);
            rec.setUpperRightY(419.52758f);
            rec.setLowerLeftX(0);
            rec.setLowerLeftY(0);

            for(AddressInfo addressInfo:list){

                PDPage page = new PDPage(rec);
                document.addPage(page);

                try (PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {

                    // Zip code 前3桁
                    content.beginText();
                    content.setFont(font, 10);
                    content.newLineAtOffset(200, 400);
                    content.showText(addressInfo.zipCode.substring(0, 3));
                    content.endText();

                    // Zip code 前4桁
                    content.beginText();
                    content.setFont(font, 10);
                    content.newLineAtOffset(250, 400);
                    content.showText(addressInfo.zipCode.substring(3, 7));
                    content.endText();

                    //住所1
                    for(int i = 0; i < addressInfo.address1.length(); i++){

                        content.beginText();
                        content.setFont(font, 10);
                        content.newLineAtOffset(250, 380 - i * 10);
                        content.showText(addressInfo.address1.substring(i, i + 1));
                        content.endText();
                    }

                    //住所2
                    for(int i = 0; i < addressInfo.address2.length(); i++){

                        content.beginText();
                        content.setFont(font, 10);
                        content.newLineAtOffset(200, 380 - i * 10);
                        content.showText(addressInfo.address2.substring(i, i + 1));
                        content.endText();
                    }

                    // 名前
                    for(int i = 0; i < addressInfo.name.length(); i++){

                        content.beginText();
                        content.setFont(font, 30);
                        content.newLineAtOffset(140, 360 - i * 30);
                        content.showText(addressInfo.name.substring(i, i + 1));
                        content.endText();
                    }

                    //様
                    content.beginText();
                    content.setFont(font, 30);
                    content.newLineAtOffset(140, 360 - addressInfo.name.length() * 30);
                    content.showText("様");
                    content.endText();
                }

            }

            document.save(outFile);
        }
    }
}