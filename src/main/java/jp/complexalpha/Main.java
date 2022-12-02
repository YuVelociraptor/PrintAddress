package jp.complexalpha;

import jp.complexalpha.db.AddressInfo;
import jp.complexalpha.db.Select;

import java.sql.Array;
import java.sql.SQLException;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws SQLException {


        ArrayList<AddressInfo> list = Select.getInfo();

        list.forEach(s -> System.out.println(s.name));
    }
}