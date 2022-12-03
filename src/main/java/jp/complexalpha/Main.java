package jp.complexalpha;

import jp.complexalpha.db.AddressInfo;
import jp.complexalpha.db.Select;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.io.File;
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

        ArrayList<AddressInfo> toList = Select.getToInfo();
        AddressInfo fromInfo = Select.getFromInfo();

        try (PDDocument document = new PDDocument()) {

            PDFont font = PDType0Font.load(document, new File(fontPath));

            //サイズ指定
            PDRectangle rec = new PDRectangle();
            rec.setUpperRightX(283.46458f);
            rec.setUpperRightY(419.52758f);
            rec.setLowerLeftX(0);
            rec.setLowerLeftY(0);

            for(AddressInfo addressInfo:toList){

                PDPage page = new PDPage(rec);
                document.addPage(page);

                try (PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {

                    // Zip code
                    if(addressInfo.zipCode != null) {

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
                    }

                    //住所1
                    if(addressInfo.address1 != null) {
                        for (int i = 0; i < addressInfo.address1.length(); i++) {

                            content.beginText();
                            content.setFont(font, 10);
                            content.newLineAtOffset(250, 380 - i * 10);
                            content.showText(addressInfo.address1.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    //住所2
                    if(addressInfo.address2 != null) {
                        for (int i = 0; i < addressInfo.address2.length(); i++) {

                            content.beginText();
                            content.setFont(font, 10);
                            content.newLineAtOffset(200, 380 - i * 10);
                            content.showText(addressInfo.address2.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 名前
                    if(addressInfo.name != null) {
                        for (int i = 0; i < addressInfo.name.length(); i++) {

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

                    // 差出人 住所1
                    if(fromInfo.address1 != null) {
                        for (int i = 0; i < fromInfo.address1.length(); i++) {

                            content.beginText();
                            content.setFont(font, 10);
                            content.newLineAtOffset(80, 120 - i * 10);
                            content.showText(fromInfo.address1.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 差出人 住所2
                    if(fromInfo.address2 != null) {
                        for (int i = 0; i < fromInfo.address2.length(); i++) {

                            content.beginText();
                            content.setFont(font, 10);
                            content.newLineAtOffset(50, 130 - i * 10);
                            content.showText(fromInfo.address2.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 差出人 名前
                    if(fromInfo.name != null){
                        for (int i = 0; i < fromInfo.name.length(); i++) {

                            content.beginText();
                            content.setFont(font, 10);
                            content.newLineAtOffset(20, 150 - i * 10);
                            content.showText(fromInfo.name.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 差出人 Zip Code
                    if(fromInfo.zipCode != null) {

                        // Zip code 前3桁
                        content.beginText();
                        content.setFont(font, 10);
                        content.newLineAtOffset(10, 10);
                        content.showText(fromInfo.zipCode.substring(0, 3));
                        content.endText();

                        // Zip code 前4桁
                        content.beginText();
                        content.setFont(font, 10);
                        content.newLineAtOffset(50, 10);
                        content.showText(fromInfo.zipCode.substring(3, 7));
                        content.endText();
                    }
                }

            }

            document.save(outFile);
        }
    }
}