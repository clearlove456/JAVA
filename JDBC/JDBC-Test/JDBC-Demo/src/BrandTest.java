

/*
    用来测试增删改查
 */

import com.alibaba.druid.pool.DruidDataSourceFactory;
import org.junit.Test;

import javax.sql.DataSource;
import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.*;

/*

    PreparedStatement 是 Java JDBC 中的接口，是 Statement 接口的子接口。它提供了一种预编译 SQL 语句的机制，可以提高执行 SQL 语句的效率，并防止 SQL 注入攻击。以下是 PreparedStatement 接口中一些常用的方法：

    设置参数：

    setInt(int parameterIndex, int x): 设置指定位置的参数为整数。
    setString(int parameterIndex, String x): 设置指定位置的参数为字符串。
    setDouble(int parameterIndex, double x): 设置指定位置的参数为双精度浮点数。
    其他基本数据类型和日期类型的设置方法，具体根据参数类型选择相应的方法。
    执行 SQL 语句：

    execute(): 执行任意 SQL 语句。
    executeQuery(): 执行查询操作，并返回结果集。
    executeUpdate(): 执行更新操作（插入、更新、删除），返回受影响的行数。
    批处理操作：

    addBatch(): 将一组 SQL 语句添加到批处理中。
    executeBatch(): 执行批处理中的所有 SQL 语句。
    参数元数据：

    getParameterMetaData(): 获取关于预备语句参数的元数据。
    清除参数：

    clearParameters(): 清除之前设置的所有参数。
    关闭预备语句：

    close(): 关闭预备语句。
    下面是一个简单的例子，演示如何使用 PreparedStatement：

    java
    Copy code
    import java.sql.Connection;
    import java.sql.DriverManager;
    import java.sql.PreparedStatement;
    import java.sql.SQLException;

    public class PreparedStatementExample {
        public static void main(String[] args) {
            String jdbcUrl = "jdbc:mysql://localhost:3306/your_database";
            String username = "your_username";
            String password = "your_password";

            try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                String sql = "INSERT INTO your_table (column1, column2) VALUES (?, ?)";
                try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
                    preparedStatement.setInt(1, 123);
                    preparedStatement.setString(2, "example");

                    int rowsAffected = preparedStatement.executeUpdate();
                    System.out.println(rowsAffected + " row(s) affected.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
    请注意，在上述例子中，? 是占位符，通过 setInt 和 setString 方法设置实际的参数值。这样可以防止 SQL 注入攻击。

 */

public class BrandTest {


    /*
        查询所有
        select * from tb_brand
        不需要参数
        List<brand>

     */
    @Test
    public void testSelectAll() throws Exception{
        //1.导入jar包

        //2.定义配置文件

        //3.加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        //4.获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        //5.获取数据库连接connection
        Connection conn = dataSource.getConnection();

        //定义sql语句
        String sql = "select * from tb_brand";

        //获取PreparedStatement对象
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //设置参数
        //

        //执行sql
        ResultSet res = pstmt.executeQuery();

        //处理结果 List<brand>
        Brand brand = null;
        List<Brand> list = new ArrayList<>();
        while(res.next()){
            //获取数据
            int id = res.getInt("id");
            String brandName = res.getString("brand_name");
            String companyName = res.getString("company_name");
            int ordered = res.getInt("ordered");
            String description = res.getString("description");
            int status = res.getInt("status");

            //封装brand对象
            brand = new Brand();
            brand.setId(id);
            brand.setBrandName(brandName);
            brand.setCompanyName(companyName);
            brand.setOrdered(ordered);
            brand.setDescription(description);
            brand.setStatus(status);

            list.add(brand);
        }

        System.out.println(list);
//        Iterator iterator = list.iterator();
//        while(iterator.hasNext())
//            System.out.println(iterator.next());

        //释放资源
        res.close();
        conn.close();
        pstmt.close();

    }

    /*
        sql insert into tb_brand(brand_name, company_name, ordered, description, status) values(?,?,?,?,?);
        参数 需要除了id以外的所有信息
        结果 true || false
     */
    @Test
    public void testAdd() throws Exception{

        //接收页面提交的参数
        String brandName = "香飘飘";
        String companyName = "香飘飘";
        int ordered = 1;
        String description = "香飘飘真好喝";
        int status = 1;


        //1.导入jar包

        //2.定义配置文件

        //3.加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        //4.获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        //5.获取数据库连接connection
        Connection conn = dataSource.getConnection();

        //定义sql语句
        String sql = "insert into tb_brand(brand_name, company_name, ordered, description, status) values(?,?,?,?,?);";

        //获取PreparedStatement对象
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //设置参数

        pstmt.setString(1, brandName);
        pstmt.setString(2, companyName);
        pstmt.setInt(3, ordered);
        pstmt.setString(4, description);
        pstmt.setInt(5, status);




        //执行sql
        int count = pstmt.executeUpdate();

        //处理结果
        System.out.println(count > 0 ? "successful" : "defeat");

        //释放资源
        conn.close();
        pstmt.close();

    }

    /*
        根据id进行修改
        sql String sql = " update tb_brand\n" +
                "         set brand_name  = ?,\n" +
                "         company_name= ?,\n" +
                "         ordered     = ?,\n" +
                "         description = ?,\n" +
                "         status      = ?\n" +
                "     where id = ?";
        结果 true || false


     */

    @Test
    public void testUpdate() throws Exception{

        //接收页面提交的参数
        String brandName = "香飘飘";
        String companyName = "香飘飘";
        int ordered = 1;
        String description = "蔡徐坤唱跳rap music";
        int status = 1;
        int id = 4;

        //1.导入jar包

        //2.定义配置文件

        //3.加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        //4.获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        //5.获取数据库连接connection
        Connection conn = dataSource.getConnection();

        //定义sql语句
        String sql = " update tb_brand\n" +
                "         set brand_name  = ?,\n" +
                "         company_name= ?,\n" +
                "         ordered     = ?,\n" +
                "         description = ?,\n" +
                "         status      = ?\n" +
                "     where id = ?";

        //获取PreparedStatement对象
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //设置参数

        pstmt.setString(1, brandName);
        pstmt.setString(2, companyName);
        pstmt.setInt(3, ordered);
        pstmt.setString(4, description);
        pstmt.setInt(5, status);
        pstmt.setInt(6, id);




        //执行sql
        int count = pstmt.executeUpdate();

        //处理结果
        System.out.println(count > 0 ? "successful" : "defeat");

        //释放资源
        conn.close();
        pstmt.close();

    }


    /*
        根据id删除


     */

    @Test
    public void testDelete() throws Exception{

        //接收页面提交的参数
//        String brandName = "香飘飘";
//        String companyName = "香飘飘";
//        int ordered = 1;
//        String description = "蔡徐坤唱跳rap music";
//        int status = 1;
        int id = 4;

        //1.导入jar包

        //2.定义配置文件

        //3.加载配置文件
        Properties prop = new Properties();
        prop.load(new FileInputStream("src/druid.properties"));
        //4.获取连接池对象
        DataSource dataSource = DruidDataSourceFactory.createDataSource(prop);

        //5.获取数据库连接connection
        Connection conn = dataSource.getConnection();

        //定义sql语句
        String sql = " delete from tb_brand where id = ?";

        //获取PreparedStatement对象
        PreparedStatement pstmt = conn.prepareStatement(sql);

        //设置参数

//        pstmt.setString(1, brandName);
//        pstmt.setString(2, companyName);
//        pstmt.setInt(3, ordered);
//        pstmt.setString(4, description);
//        pstmt.setInt(5, status);
        pstmt.setInt(1, id);




        //执行sql
        int count = pstmt.executeUpdate();

        //处理结果
        System.out.println(count > 0 ? "successful" : "defeat");

        //释放资源
        conn.close();
        pstmt.close();

    }


}
