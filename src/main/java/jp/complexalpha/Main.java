package jp.complexalpha;

import jp.complexalpha.db.AddressInfo;
import jp.complexalpha.db.Select;

import java.io.File;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {

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
    }
}