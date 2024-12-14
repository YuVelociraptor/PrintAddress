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
import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {

    private final static boolean RULER_FLAG = false;

    private static int TO_NAME_FONT_SIZE = 30;
    private static int TO_NAME_ST_X = 140;

    public static void main(String[] args) throws Exception {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String now = LocalDateTime.now().format(format);
        System.out.println(now);

        String outDir = System.getenv("PDF_OUT_DIR");
        File outFile = new File(outDir + "/out_" + now + ".pdf");

        System.out.println(outFile);

        String fontPath = System.getenv("PDF_FONT");
        System.out.println(fontPath);

        try (PDDocument document = new PDDocument()) {

            ArrayList<AddressInfo> toList = Select.getToInfo();
            AddressInfo fromInfo = Select.getFromInfo();

            PDFont font = PDType0Font.load(document, new File(fontPath));

            // Post card size
            PDRectangle rec = new PDRectangle();
            rec.setUpperRightX(283.46458f);
            rec.setUpperRightY(419.52758f);
            rec.setLowerLeftX(0);
            rec.setLowerLeftY(0);

            for (AddressInfo addressInfo : toList) {

                PDPage page = new PDPage(rec);
                document.addPage(page);

                try (PDPageContentStream content = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, false)) {

                    if (RULER_FLAG) {
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
                    if (addressInfo.zipCode != null) {

                        int zf = 20;
                        float delta = 20;
                        float delta2 = 19;
                        float zx = 125;
                        float zy = 365;

                        // Zip code front 3
                        for (int i = 0; i < 3; i++) {

                            content.beginText();
                            content.setFont(font, zf);
                            content.newLineAtOffset(zx + delta * i, zy);
                            content.showText(addressInfo.zipCode.substring(i, i + 1));
                            content.endText();
                        }

                        // Zip code back 4
                        for (int i = 3; i < 7; i++) {

                            content.beginText();
                            content.setFont(font, zf);
                            content.newLineAtOffset(3 + zx + delta2 * i, zy);
                            content.showText(addressInfo.zipCode.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // To Address
                    ArrayList<String> toAddresses = new ArrayList<>();
                    int addressLetters = 13;

                    int taf = 22;
                    float tay = 340;

                    // Address1
                    if (addressInfo.address1 != null) {
                        for (int i = 0; i < addressInfo.address1.length(); i++) {
                            content.beginText();
                            content.setFont(font, taf);
                            content.newLineAtOffset(250, tay - i * taf);
                            content.showText(addressInfo.address1.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // Address2
                    if (addressInfo.address2 != null) {

                        String address2 = addressInfo.address2;
                        while (address2.length() < 13) {

                            address2 = " " + address2;
                        }

                        for (int i = 0; i < address2.length(); i++) {

                            content.beginText();
                            content.setFont(font, taf);
                            content.newLineAtOffset(220, tay - i * taf);
                            content.showText(address2.substring(i, i + 1));
                            content.endText();
                        }

                        //toIndex++;
                    }

                    // 名字
                    if (addressInfo.family_name != null) {

                        String name = addressInfo.family_name;
                        for (int i = 0; i < name.length(); i++) {

                            content.beginText();
                            content.setFont(font, TO_NAME_FONT_SIZE);
                            content.newLineAtOffset(TO_NAME_ST_X, 320 - i * 30);
                            content.showText(name.substring(i, i + 1));
                            content.endText();

                        }
                        //name += addressInfo.first_name1;
                    }

                    // 名前
                    if (addressInfo.first_names != null) {

                        int cnt = 0;
                        String[] firstNamesArray = addressInfo.first_names.split(",");

                        for (String n : firstNamesArray) {

                            String wn = n + "様";

                            for (int i = 0; i < wn.length(); i++) {

                                content.beginText();
                                content.setFont(font, TO_NAME_FONT_SIZE);
                                content.newLineAtOffset(TO_NAME_ST_X - cnt * TO_NAME_FONT_SIZE + (firstNamesArray.length - 1) * 0.5f * TO_NAME_FONT_SIZE, 320 - TO_NAME_FONT_SIZE * (addressInfo.family_name.length() + 1) - i * TO_NAME_FONT_SIZE);
                                content.showText(wn.substring(i, i + 1));
                                content.endText();
                            }
                            cnt++;
                        }
                    }

                    // 差出人住所名前フォントサイズ
                    int nf = 12;
                    float ay = 240;

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

                    // From Address2
                    if (fromInfo.address2 != null) {

                        String address2 = fromInfo.address2;
                        while (address2.length() < 14) {

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
                    if (fromInfo.family_name != null) {
                        for (int i = 0; i < fromInfo.family_name.length(); i++) {

                            content.beginText();
                            content.setFont(font, nf);
                            content.newLineAtOffset(20, 190 - i * nf);
                            content.showText(fromInfo.family_name.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    if (fromInfo.first_names != null) {
                        for (int i = 0; i < fromInfo.first_names.length(); i++) {

                            content.beginText();
                            content.setFont(font, nf);
                            content.newLineAtOffset(20, 190 - i * nf - (fromInfo.family_name.length() + 1) * nf);
                            content.showText(fromInfo.first_names.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // From Zip Code
                    if (fromInfo.zipCode != null) {

                        int zf = 15;
                        float zx = 19;
                        float zy = 67;
                        // Zip code 前3桁

                        content.beginText();
                        content.setFont(font, zf);
                        content.newLineAtOffset(zx, zy);
                        content.showText(fromInfo.zipCode.substring(0, 3));
                        content.endText();

                        // Zip code back 4
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