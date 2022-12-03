package jp.complexalpha;

import jp.complexalpha.db.AddressInfo;
import jp.complexalpha.db.Select;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType0Font;

import java.awt.*;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {

    private static boolean RULER_FLAG = false;

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

                    if(RULER_FLAG) {
                        content.moveTo(0, 0);
                        content.lineTo(page.getMediaBox().getWidth(), 0);

                        content.moveTo(page.getMediaBox().getWidth(), 0);
                        content.lineTo(page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

                        content.moveTo(page.getMediaBox().getWidth(), page.getMediaBox().getHeight());
                        content.lineTo(0, page.getMediaBox().getHeight());

                        content.moveTo(0, page.getMediaBox().getHeight());
                        content.lineTo(0, 0);

                        content.moveTo(0, 0);
                        content.lineTo(page.getMediaBox().getWidth(), page.getMediaBox().getHeight());

                        content.moveTo(0, page.getMediaBox().getHeight());
                        content.lineTo(page.getMediaBox().getWidth(), 0);

                        content.setStrokingColor(Color.BLACK);
                        content.setLineWidth(0.5f);
                        content.stroke();


                        for (int i = 0; i < 20; i++) {

                            content.moveTo(i * 10, page.getMediaBox().getHeight());
                            content.lineTo(i * 10, 0);
                        }

                        for (int i = 0; i < 20; i++) {

                            content.moveTo(0, page.getMediaBox().getHeight() - i * 10);
                            content.lineTo(page.getMediaBox().getWidth(), page.getMediaBox().getHeight() - i * 10);
                        }

                        content.setStrokingColor(Color.BLUE);
                        content.setLineWidth(0.5f);
                        content.stroke();
                    }

                    // Zip code
                    if(addressInfo.zipCode != null) {

                        int zf = 27;
                        float delta = 20;
                        float zx = 124;
                        float zy = 366;

                        // Zip code 前3桁
                        for(int i = 0; i < 3; i++){

                            content.beginText();
                            content.setFont(font, zf);
                            content.newLineAtOffset(zx + delta * i, zy);
                            content.showText(addressInfo.zipCode.substring(i, i+1));
                            content.endText();
                        }

                        // Zip code 後4桁
                        for(int i = 3; i < 7; i++){

                            content.beginText();
                            content.setFont(font, zf);
                            content.newLineAtOffset(2 + zx + delta * i, zy);
                            content.showText(addressInfo.zipCode.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    //住所1
                    int taf = 22;
                    float tay = 340;
                    if(addressInfo.address1 != null) {
                        for (int i = 0; i < addressInfo.address1.length(); i++) {

                            content.beginText();
                            content.setFont(font, taf);
                            content.newLineAtOffset(235, tay - i * taf);
                            content.showText(addressInfo.address1.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    //住所2
                    if(addressInfo.address2 != null) {
                        for (int i = 0; i < addressInfo.address2.length(); i++) {

                            content.beginText();
                            content.setFont(font, taf);
                            content.newLineAtOffset(200, tay - i * taf);
                            content.showText(addressInfo.address2.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 名前
                    if(addressInfo.name != null) {

                        String name = addressInfo.name + "様";
                        for (int i = 0; i < name.length(); i++) {

                            content.beginText();
                            content.setFont(font, 30);
                            content.newLineAtOffset(140, 320 - i * 30);
                            content.showText(name.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 差出人住所名前フォントサイズ
                    int nf = 12;
                    float ay = 230;

                    // 差出人 住所1
                    if(fromInfo.address1 != null) {

                        for (int i = 0; i < fromInfo.address1.length(); i++) {

                            content.beginText();
                            content.setFont(font, nf);
                            content.newLineAtOffset(60, ay - i * nf);
                            content.showText(fromInfo.address1.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 差出人 住所2
                    if(fromInfo.address2 != null) {

                        String  address2 = fromInfo.address2;
                        while (address2.length() < 14){

                            address2 = " " + address2;
                        }

                        for (int i = 0; i < address2.length(); i++) {

                            content.beginText();
                            content.setFont(font, 12);
                            content.newLineAtOffset(40, ay - i * nf);
                            content.showText(address2.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 差出人 名前
                    if(fromInfo.name != null){
                        for (int i = 0; i < fromInfo.name.length(); i++) {

                            content.beginText();
                            content.setFont(font, nf);
                            content.newLineAtOffset(20, 190 - i * nf);
                            content.showText(fromInfo.name.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // 差出人 Zip Code
                    if(fromInfo.zipCode != null) {

                        int zf = 21;
                        float zx = 14;
                        float zy = 53;
                        // Zip code 前3桁
                        content.beginText();
                        content.setFont(font, zf);
                        content.newLineAtOffset(zx, zy);
                        content.showText(fromInfo.zipCode.substring(0, 3));
                        content.endText();

                        // Zip code 前4桁
                        content.beginText();
                        content.setFont(font, zf);
                        content.newLineAtOffset(zx + 37, zy);
                        content.showText(fromInfo.zipCode.substring(3, 7));
                        content.endText();
                    }
                }

            }

            document.save(outFile);
        }
    }
}