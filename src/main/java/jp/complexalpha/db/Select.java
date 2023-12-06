package jp.complexalpha.db;

import java.sql.*;
import java.util.ArrayList;

public class Select {

    private final static String url = "jdbc:sqlite:" + System.getenv("ADDRESS_SQLITE");

    public static ArrayList<AddressInfo> getToInfo() throws SQLException {

        String sql = "select * from to_info where not_send = 0 order by id";

        try(
                Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql)
                ){

            try(ResultSet rs = ps.executeQuery()){

                ArrayList<AddressInfo> ret = new ArrayList<>();
                while (rs.next()) {

                    AddressInfo a = new AddressInfo();
                    a.id = rs.getInt("id");
                    a.zipCode = rs.getString("zip_code");
                    a.address1 = rs.getString("address1");
                    a.address2 = rs.getString("address2");
                    a.family_name = rs.getString("family_name");
                    a.first_names = rs.getString("first_names");

                    ret.add(a);
                }

                return ret;
            }

        }catch (Exception e){

            e.printStackTrace();
            throw e;
        }
    }

    public static AddressInfo getFromInfo() throws SQLException {

        String sql = "select * from from_info where in_use = 1";

        try(
                Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql)
        ){

            try(ResultSet rs = ps.executeQuery()){

                ArrayList<AddressInfo> ret = new ArrayList<>();
                while (rs.next()) {

                    AddressInfo a = new AddressInfo();
                    a.id = rs.getInt("id");
                    a.zipCode = rs.getString("zip_code");
                    a.address1 = rs.getString("address1");
                    a.address2 = rs.getString("address2");
                    a.family_name = rs.getString("family_name");
                    a.first_names = rs.getString("first_names");

                    ret.add(a);
                }

                return ret.get(0);
            }

        }catch (Exception e){

            e.printStackTrace();
            throw e;
        }
    }
}
