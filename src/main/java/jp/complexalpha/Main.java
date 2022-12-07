package jp.complexalpha;

import jp.complexalpha.db.FromAddressInfo;
import jp.complexalpha.db.ToAddressInfo;
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

    private final static boolean RULER_FLAG = false;

    public static void main(String[] args) {

        DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
        String now = LocalDateTime.now().format(format);
        System.out.println(now);

        String outDir = System.getenv("PDF_OUT_DIR");
        File outFile = new File(outDir + "/out_" + now + ".pdf");

        System.out.println(outFile);

        String fontPath = System.getenv("PDF_FONT");
        System.out.println(fontPath);

        try (PDDocument document = new PDDocument()) {

            ArrayList<ToAddressInfo> toList = Select.getToInfo();
            FromAddressInfo fromInfo = Select.getFromInfo();

            PDFont font = PDType0Font.load(document, new File(fontPath));

            // Post card size
            PDRectangle rec = new PDRectangle();
            rec.setUpperRightX(283.46458f);
            rec.setUpperRightY(419.52758f);
            rec.setLowerLeftX(0);
            rec.setLowerLeftY(0);

            for(ToAddressInfo addressInfo:toList){

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

                        // Zip code front 3
                        for(int i = 0; i < 3; i++){

                            content.beginText();
                            content.setFont(font, zf);
                            content.newLineAtOffset(zx + delta * i, zy);
                            content.showText(addressInfo.zipCode.substring(i, i+1));
                            content.endText();
                        }

                        // Zip code back 4
                        for(int i = 3; i < 7; i++){

                            content.beginText();
                            content.setFont(font, zf);
                            content.newLineAtOffset(2 + zx + delta * i, zy);
                            content.showText(addressInfo.zipCode.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // To Address
                    ArrayList<String> toAddresses = new ArrayList<>();
                    int addressLetters = 13;

                    // Address1
                    if(addressInfo.address1 != null) {

                        toAddresses.add(addressInfo.address1);

                        if(addressInfo.address1.length() > addressLetters){
                            addressLetters = addressInfo.address1.length();
                        }
                    }

                    // Address2
                    if(addressInfo.address2 != null) {

                        String  address2 = addressInfo.address2;
                        while (address2.length() < 13){

                            address2 = " " + address2;
                        }
                        toAddresses.add(address2);

                        if(address2.length() > addressLetters){
                            addressLetters = address2.length();
                        }
                    }

                    int taf = 286 / addressLetters;
                    float tay = 340;

                    int toIndex = 0;
                    for(String s:toAddresses){

                        for (int i = 0; i < s.length(); i++) {

                            content.beginText();
                            content.setFont(font, taf);
                            content.newLineAtOffset(235 - 35 * toIndex, tay - i * taf);
                            content.showText(s.substring(i, i + 1));
                            content.endText();
                        }

                        toIndex++;
                    }

                    // Name
                    String name = "";
                    if(addressInfo.family_name != null) {
                        name += addressInfo.family_name;
                    }

                    if(addressInfo.first_name1 != null){
                        if(!name.equals("")){
                            name += " ";
                        }
                        name += addressInfo.first_name1;
                    }

                    if(addressInfo.honorific_title1 != null){
                        name += addressInfo.honorific_title1;
                    }

                    for (int i = 0; i < name.length(); i++) {
                        content.beginText();
                        content.setFont(font, 30);
                        content.newLineAtOffset(140, 320 - i * 30);
                        content.showText(name.substring(i, i + 1));
                        content.endText();
                    }

                    // From Address Font size
                    int nf = 12;
                    float ay = 230;

                    ArrayList<String> fromAddressses = new ArrayList<>();

                    // Phone Number
                    if(fromInfo.phoneNumber != null){
                        fromAddressses.add(fromInfo.phoneNumber);
                    }

                    // From Address2
                    if(fromInfo.address2 != null) {

                        String  address2 = fromInfo.address2;
                        while (address2.length() < 14){

                            address2 = " " + address2;
                        }

                        fromAddressses.add(address2);
                    }

                    // From Address1
                    if(fromInfo.address1 != null) {
                        fromAddressses.add(fromInfo.address1);
                    }

                    int fromIndex = 0;
                    for(String s:fromAddressses){

                        for (int i = 0; i < s.length(); i++) {

                            content.beginText();
                            content.setFont(font, nf);
                            content.newLineAtOffset(40 + 20 * fromIndex, ay - i * nf);
                            content.showText(s.substring(i, i + 1));
                            content.endText();
                        }

                        fromIndex++;
                    }

                    // From Name
                    if(fromInfo.name != null){
                        for (int i = 0; i < fromInfo.name.length(); i++) {

                            content.beginText();
                            content.setFont(font, nf);
                            content.newLineAtOffset(20, 190 - i * nf);
                            content.showText(fromInfo.name.substring(i, i + 1));
                            content.endText();
                        }
                    }

                    // From Zip Code
                    if(fromInfo.zipCode != null) {

                        int zf = 21;
                        float zx = 14;
                        float zy = 53;
                        // Zip code Front 3
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

        }catch(Exception e){
            e.printStackTrace();
        }
    }
}