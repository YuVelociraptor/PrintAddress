package jp.complexalpha.temp;

import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Xxxxx {

    public static void main(String... args){

        try {
            new Xxxxx().getList();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getList() throws IOException {

        System.out.println("Hello World");

        try (InputStream in = this.getClass().getResourceAsStream("/mybatis-config.xml")) {

            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(in, getMyBatisPros());
        }
    }

    public static Properties getMyBatisPros(){

        Properties mybatisProps = new Properties();
        mybatisProps.put("driver", "org.sqlite.JDBC");
        mybatisProps.put("url", "jdbc:sqlite:" + System.getenv("ADDRESS_SQLITE"));

        return mybatisProps;
    }
}
