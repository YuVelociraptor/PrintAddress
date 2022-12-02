package jp.complexalpha.db;

import java.sql.*;
import java.util.ArrayList;

public class Select {

    private final static String url = "jdbc:sqlite:" + System.getenv("ADDRESS_SQLITE");;

    public static ArrayList<AddressInfo> getInfo() throws SQLException {

        String sql = "select * from to_info order by id";

        try(
                Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql);
                ){

            try(ResultSet rs = ps.executeQuery()){

                ArrayList<AddressInfo> ret = new ArrayList<AddressInfo>();
                while (rs.next()) {

                    AddressInfo a = new AddressInfo();
                    a.id = rs.getInt("id");
                    a.zipCode = rs.getString("zip_code");
                    a.address1 = rs.getString("address1");
                    a.address2 = rs.getString("address2");
                    a.name = rs.getString("name");

                    ret.add(a);
                }

                return ret;
            }

        }catch (Exception e){

            e.printStackTrace();
            throw e;
        }
    }
}
