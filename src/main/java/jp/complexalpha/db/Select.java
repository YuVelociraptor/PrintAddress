package jp.complexalpha.db;

import java.sql.*;
import java.util.ArrayList;

public class Select {

    private final static String url = "jdbc:sqlite:" + System.getenv("ADDRESS_SQLITE");;

    public static ArrayList<ToAddressInfo> getToInfo() throws SQLException {

        String sql = "select * from to_info order by id";

        try(
                Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql);
                ){

            try(ResultSet rs = ps.executeQuery()){

                ArrayList<ToAddressInfo> ret = new ArrayList<ToAddressInfo>();
                while (rs.next()) {

                    ToAddressInfo a = new ToAddressInfo();
                    a.id = rs.getInt("id");
                    a.zipCode = rs.getString("zip_code");
                    a.address1 = rs.getString("address1");
                    a.address2 = rs.getString("address2");
                    a.family_name = rs.getString("family_name");
                    a.first_name1 = rs.getString("first_name1");
                    a.honorific_title1 = rs.getString("honorific_title1");

                    ret.add(a);
                }

                return ret;
            }

        }catch (Exception e){

            e.printStackTrace();
            throw e;
        }
    }

    public static FromAddressInfo getFromInfo() throws SQLException {

        String sql = "select * from from_info where in_use = 1";

        try(
                Connection conn = DriverManager.getConnection(url);
                PreparedStatement ps = conn.prepareStatement(sql);
        ){

            try(ResultSet rs = ps.executeQuery()){

                ArrayList<FromAddressInfo> ret = new ArrayList<FromAddressInfo>();
                while (rs.next()) {

                    FromAddressInfo a = new FromAddressInfo();
                    a.id = rs.getInt("id");
                    a.zipCode = rs.getString("zip_code");
                    a.address1 = rs.getString("address1");
                    a.address2 = rs.getString("address2");
                    a.name = rs.getString("name");

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
