import java.io.FileInputStream;
import java.sql.*;
import java.util.Properties;

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;

public class Main {
    public static void main(String[] args) throws Exception {

        //1.注册驱动-反射去加载jar包中的com.jdbc.Driver这个类中的DriverManager.registerDriver(new Driver());
        Class.forName("com.mysql.cj.jdbc.Driver");
        //2.获取连接对象
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/atguigudb?characterEncoding=utf-8", "root", "sc20020502");
        System.out.println(conn);
        //3.定义sql
        String sql="SELECT *\n" +
                "FROM employees";
        //4.需要创建statement
        //Statement statement = (Statement) conn.createStatement();
//        //5.statement执行sql,返回 插入了几行
//        int i = ((java.sql.Statement) statement).executeUpdate(sql);
//        System.out.println("向数据库t_dept表中插入了"+i+"行数据");


        PreparedStatement preparedStatement = conn.prepareStatement(sql);
        ResultSet resultSet = preparedStatement.executeQuery();

        while ( resultSet.next()){
            System.out.println( resultSet.getInt("employee_id") + "\t" + resultSet.getDouble("salary"));
        }

        //6.关闭资源
        //((java.sql.Statement) statement).close();
        preparedStatement.close();
        conn.close();


    }

    //druid使用
    @Test
    public void druidTest() throws Exception{
        //1.导入jar包

        //2.定义配置文件

        //3.加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        //4.获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        //5.获取数据库连接connection
        Connection connection = dataSource.getConnection();

        System.out.println(connection);
        //System.out.println(System.getProperty("user.dir"));
    }
}



