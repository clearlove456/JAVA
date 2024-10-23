# SSM

## Spring

### IOC

#### 从容器中获取组件

~~~java
@SpringBootApplication
public class Spring01IocApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);

        //获取容器的组件对象
        //组件的四大特性 名字、类型、对象、作用域

        Object person = ioc.getBean("haha");
        System.out.println("对象 = " + person);
    }


    @Bean("haha")
    public Person person(){
        Person person = new Person();
        person.setAge(20);
        person.setName("张三");
        person.setGender("男");
        return person;
    }

}
~~~

#### 组件创建时机和单例特性

组件的创建时机 容器启动过程就会创建对象

所有对象是单实例组件，每次获取直接从容器中拿

#### @Configuration

组件是框架的一些底层配置

可以使用配置类或者配置文件来配置 但是配置文件已经不常使用

配置类也是一个组件

~~~java
@Configuration
public class PersonConfig {
    @Bean("123")
    public Person person(){
        Person person = new Person();
        person.setAge(20);
        person.setName("张四");
        person.setGender("男");
        return person;
    }

    @Bean("1234")
    public Person person1(){
        Person person = new Person();
        person.setAge(20);
        person.setName("张四");
        person.setGender("男");
        return person;
    }
}
~~~

#### 分层注解 @Controller @Service @Repository

这个三个注解都可以被@Componnet替换 但为了易懂还是有所区别

要想这三个注解生效 这些组件必须在主程序所在的包及其子包下

~~~java
@Controller
public class UserController {

}

@Service
public class UserService {

}

@Repository
public class UserDao {

}

~~~

#### @ComponentScan

组件批量扫描

不加注解的是不会被扫描进来

~~~java
@ComponentScan(basePackages = "com.example.spring01ioc")
@SpringBootApplication
public class Spring01IocApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ioc = SpringApplication.run(Spring01IocApplication.class, args);

        //获取容器的组件对象
        //组件的四大特性 名字、类型、对象、作用域

        Object person = ioc.getBean("123");
        System.out.println("对象 = " + person);
    }


    @Bean("haha")
    public Person person(){
        Person person = new Person();
        person.setAge(20);
        person.setName("张三");
        person.setGender("男");
        return person;
    }

}
~~~

#### @Import

导入第三方包

1.@Bean  自己new注册到容器

2.@Import

#### AppConfig

将一些注解放到一个配置类上 使得主启动类比较干净 

~~~java
@Configuration
@ComponentScan(basePackages = "com.example.spring01ioc")
public class AppConfig {
}
~~~

#### @Scope

调整组件的作用域

~~~java
/**
     * @Scope 调整组件的作用域：
     * 1、@Scope("prototype")：非单实例:
     *      容器启动的时候不会创建非单实例组件的对象。
     *      什么时候获取，什么时候创建
     * 2、@Scope("singleton")：单实例： 默认值
     *      容器启动的时候会创建单实例组件的对象。
     *      容器启动完成之前就会创建好
     *    @Lazy：懒加载
     *      容器启动完成之前不会创建懒加载组件的对象
     *      什么时候获取，什么时候创建
     * 3、@Scope("request")：同一个请求单实例
     * 4、@Scope("session")：同一次会话单实例
     *
     * @return
*/

~~~

#### @Conditional

根据生产的情况决定是不是要将组件放进容器

~~~java
@Configuration //告诉Spring容器，这是一个配置类
public class PersonConfig {

    //场景：判断当前电脑的操作系统是windows还是mac
    //  windows 系统，容器中有 bill
    //  mac 系统，容器中有 joseph
    @Conditional(MacCondition.class)
    @Bean("joseph")
    public Person joseph(){
        Person person = new Person();
        person.setName("乔布斯");
        person.setAge(20);
        person.setGender("男");
        return person;
    }

    @Conditional(WindowsCondition.class)
    @Bean("bill")
    public Person bill(){
        Person person = new Person();
        person.setName("比尔盖茨");
        person.setAge(20);
        person.setGender("男");
        return person;
    }

    @Primary //主组件：默认组件
    @Bean("zhangsan")
    public Person haha() {
        Person person = new Person();
        person.setName("张三2");
        person.setAge(20);
        person.setGender("男");
        return person;
    }
}

~~~

#### @Autowired

自动装配 原理：Spring调用 容器.getBean

自动装配流程：

1.按照类型找到这个组件 

​		有且找到一个直接注入

​		找到多个再按照名称去找  多个名称按照第一个写的注入

~~~java
@Controller
public class UserController {

    @Autowired
    UserService userService;
}
~~~

#### @Resource

~~~java
//面试题：@Resource 和 @Autowired 区别？
    //1、@Autowired 和 @Resource 都是做bean的注入用的，都可以放在属性上
    //2、@Resource 具有更强的通用性
~~~

#### @Value

注入属性的

~~~java
@ToString
@Data
@Component
public class Dog {

//    @Autowired // 自动注入组件的。基本类型，自己搞。
    /**
     * 1、@Value("字面值"): 直接赋值
     * 2、@Value("${key}")：动态从配置文件中取出某一项的值。
     * 3、@Value("#{SpEL}")：Spring Expression Language；Spring 表达式语言
     *      更多写法：https://docs.spring.io/spring-framework/reference/core/expressions.html
     *
     */
    @Value("旺财")
    private String name;
    @Value("${dog.age}")
    private Integer age;

    @Value("#{10*20}")
    private String color;

    @Value("#{T(java.util.UUID).randomUUID().toString()}")
    private String id;

    @Value("#{'Hello World!'.substring(0, 5)}")
    private String msg;

    @Value("#{new String('haha').toUpperCase()}")
    private String flag;

    @Value("#{new int[] {1, 2, 3}}")
    private int[] hahaha;

    public Dog() {

        String string = UUID.randomUUID().toString();

        System.out.println("Dog构造器...");
    }
}
~~~

#### @PropertySource

说明属性来源，把指定的文件导入容器中，取值使用

~~~java
//说明属性来源： 把指定的文件导入容器中，供我们取值使用

// 1、classpath:cat.properties；从自己的项目类路径下找
// 2、classpath*:Log4j-charsets.properties；从所有包的类路径下找
@PropertySource("classpath:conf/cat.properties")
@Data
@Component
public class Cat {

    @Value("${cat.name:Tom}") // : 后面是取不到的时候的默认值；
    private String name;
    @Value("${cat.age:20}")
    private int age;

}
~~~

#### ResourceUtils 

获取文件资源

~~~java
File file = ResourceUtils.getFile("classpath:abc.jpg");
~~~

#### @Profile

多环境

### AOP

#### JDK动态代理

~~~java
	@Test
    void contextLoads() {

        MathCalculator target = new MathCalculatorImpl();
        target.add(1, 2);


        InvocationHandler h = new InvocationHandler() {

            /**
             * proxy： 代理对象： 明星经纪人
             * method： 代理对象准备调用目标对象的这个方法；
             * args：方法调用传递的参数
             * @return
             * @throws Throwable
             */
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                //目标方法执行
                System.out.println("瞅瞅参数："+ Arrays.asList(args));
                args[1] = 0;
                System.out.println("改后参数："+ Arrays.asList(args));

                //真正执行之前可以拦截
                Object result = method.invoke(target, args);


                return result;
            }
        };



        //创建动态代理
        /**
         * ClassLoader loader,  类加载器
         * Class<?>[] interfaces, 目标对象实现的接口
         * InvocationHandler h 类似于拦截器
         */
        MathCalculator proxyInstance = (MathCalculator)Proxy.newProxyInstance(target.getClass().getClassLoader(), target.getClass().getInterfaces(), h);


        int add = proxyInstance.add(1, 2);
        System.out.println("最终结果..."+add);

    }
~~~

#### Spring动态代理

1.导入aop依赖

~~~xml
<dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-aop</artifactId>
</dependency>
~~~



2.编写切面Aspect

3.编写通知方法

~~~java
@Component
@Aspect //告诉Spring这个组件是切面
public class LogAspect {

    public void logStart(){
        System.out.println("{切面--日志} 开始");
    }

    public void logEnd(){
        System.out.println("{切面--日志} 结束");
    }

    public void logReturn(){
        System.out.println("{切面--日志} 返回");
    }

    public void logException(){
        System.out.println("{切面--日志} 异常");
    }
}
~~~

4.指定切入点表达式





~~~java
package com.example.spring02aop.aspect;

import org.aspectj.lang.annotation.*;
import org.springframework.stereotype.Component;

@Component
@Aspect //告诉Spring这个组件是切面
public class LogAspect {


    /**
     * 1、告诉Spring，以下通知何时何地运行？
     *      何时？
     *         @Before：方法执行之前运行。
     *         @AfterReturning：方法执行正常返回结果运行。
     *         @AfterThrowing：方法抛出异常运行。
     *         @After：方法执行之后运行
     *      何地？
     *         切入点表达式：
     *           execution(方法的全签名)：
     *             全写法：[public] int [com.atguigu.spring.aop.calculator.MathCalculator].add(int,int) [throws ArithmeticException]
     *             省略写法：int add(int i,int j)
     *             通配符：
     *               *：表示任意字符
     *               ..：
     *                   1）、参数位置：表示多个参数，任意类型
     *                   2）、类型位置：代表多个层级
     *             最省略: * *(..)
     *
     * 2、通知方法的执行顺序：
     *      1、正常链路：  前置通知->目标方法->返回通知->后置通知
     *      2、异常链路：  前置通知->目标方法->异常通知->后置通知
     *
     * 3、JoinPoint： 包装了当前目标方法的所有信息
     */

    @Before("execution(int *(int, int))")
    public void logStart(){
        System.out.println("{切面--日志} 开始");
    }

    @After("execution(int *(int, int))")
    public void logEnd(){
        System.out.println("{切面--日志} 结束");
    }

    @AfterReturning("execution(int *(int, int))")
    public void logReturn(){
        System.out.println("{切面--日志} 返回");
    }

    @AfterThrowing("execution(int *(int, int))")
    public void logException(){
        System.out.println("{切面--日志} 异常");
    }
}

~~~



任何组件都要被Spring管理才能生效

~~~java
@SpringBootTest
public class AopTest {


    @Autowired
    MathCalculator mathCalculator;

    @Test
    public void test(){

        //直接声明不能被spring管理
        //MathCalculator mathCalculator = new MathCalculatorImpl();

        System.out.println(mathCalculator);
        mathCalculator.add(1, 2);
    }
}
~~~

### 事物

### SpringMVC

#### @RequeestMapping

~~~java
//@ResponseBody
//@Controller
//告诉Spring这是一个控制器（处理请求的组件）
@RestController //前后分离开发就用这个
public class HelloController {


    /**
     *
     * 精确路径必须全局唯一
     * 路径位置通配符： 多个都能匹配上，那就精确优先
     *      *: 匹配任意多个字符（0~N）； 不能匹配多个路径
     *      **： 匹配任意多层路径 只能放在最后
     *      ?: 匹配任意单个字符（1）
     *   精确程度： 完全匹配 > ? > * > **
     *
     * @return
     */
    @ResponseBody  //把返回值放到响应体中； 每次请求进来执行目标方法
    @RequestMapping("/hello")
    public String handle() {
        System.out.println("handle()方法执行了!");
        return "Hello,Spring MVC! 你好!~~~"; //默认认为返回值是跳到一个页面
    }



    @ResponseBody
    @RequestMapping("/he?ll")
    public String handle01() {
        System.out.println("handle01方法执行了!");
        return "handle01";
    }

    // /hellr


    @RequestMapping("/he*ll")
    public String handle02() {
        System.out.println("handle02方法执行了!");
        return "handle02";
    }


    @ResponseBody //
    @RequestMapping("/he/**")
    public String handle03() {
        System.out.println("handle03方法执行了!");
        return "handle03";
    }

}
~~~

~~~java
/**
 * 测试请求限定
 */
@RestController
public class RequestMappingLimitController {


    /**
     * 请求方式：
     *      GET, HEAD, POST, PUT, PATCH, DELETE, OPTIONS, TRACE
     *  Postman
     * @return
     */
    @RequestMapping(value = "/test01",method = {RequestMethod.DELETE,RequestMethod.GET})
    public String test01(){
        return "hello world";
    }

    /**
     * 请求参数：params = {"username","age"}
     * 1）、username：  表示请求必须包含username参数
     * 2）、age=18：   表示请求参数中必须包含age=18的参数
     * 3）、gender!=1：  表示请求参数中不能包含gender=1的参数
     * @return
     */
    @RequestMapping(value = "/test02",params = {"age=18","username","gender!=1"})
    public String test02(){
        return "test02";
    }


    /**
     * 请求头：headers = {"haha"}
     * 1）、haha：  表示请求中必须包含名为haha的请求头
     * 2）、hehe!=1：  表示请求头中 的 hehe 不能是1；（hehe=0，不带hehe）
     * @return
     */
    @RequestMapping(value = "/test03",headers = "haha")
    public String test03(){
        return "test03";
    }


    /**
     * 请求内容类型：consumes = {"application/json"}; 消费什么数据；
     * Media Type：媒体类型
     * 1）、application/json：  表示浏览器必须携带 json 格式的数据。
     * @return
     */
    @RequestMapping(value = "/test04",consumes = "application/json")
    public String test04(){
        return "test04";
    }

    /**
     * 响应内容类型：produces = {"text/plain;charset=utf-8"}; 生产什么数据；
     * @return
     */
    @RequestMapping(value = "/test05",produces = "text/html;charset=utf-8")
    public String test05(){
        return "<h1>你好，张三</h1>";
    }
}

~~~

#### @RequestParam 请求处理

~~~~java

@RestController
public class RequestTestController {


    /**
     * 请求参数：username=zhangsan&password=12345&cellphone=12345456&agreement=on
     * 要求：变量名和参数名保持一致
     * 1、没有携带：包装类型自动封装为null，基本类型封装为默认值
     * 2、携带：自动封装
     * @return
     */
    @RequestMapping("/handle01")
    public String handle01(String username,
                           String password,
                           String cellphone,
                           boolean agreement){
        System.out.println(username);
        System.out.println(password);
        System.out.println(cellphone);
        System.out.println(agreement);
        return "ok";
    }

    /**
     * username=zhangsan&password=123456&cellphone=1234&agreement=on
     * @RequestParam: 取出某个参数的值，默认一定要携带。
     *      required = false：非必须携带；
     *      defaultValue = "123456"：默认值，参数可以不带。
     *
     * 无论请求参数带到了 请求体中还是 url? 后面，他们都是请求参数。都可以直接用@RequestParam或者同一个变量名获取到
     * @param name
     * @param pwd
     * @param phone
     * @param ok
     * @return
     */
    @RequestMapping("/handle02")
    public String handle02(@RequestParam("username") String name,
                           @RequestParam(value = "password",defaultValue = "123456") String pwd,
                           @RequestParam("cellphone") String phone,
                           @RequestParam(value = "agreement",required = false) boolean ok){
        System.out.println(name);
        System.out.println(pwd);
        System.out.println(phone);
        System.out.println(ok);

        return "ok";
    }


    /**
     * 如果目标方法参数是一个 pojo；SpringMVC 会自动把请求参数 和 pojo 属性进行匹配；
     * 效果：
     *      1、pojo的所有属性值都是来自于请求参数
     *      2、如果请求参数没带，封装为null；
     * @param person
     * @return
     */
    //请求体：username=zhangsan&password=111111&cellphone=222222&agreement=on
    @RequestMapping("/handle03")
    public String handle03(Person person){
        System.out.println(person);
        return "ok";
    }


    /**
     * @RequestHeader：获取请求头信息
     * @param host
     * @param ua
     * @return
     */
    @RequestMapping("/handle04")
    public String handle04(@RequestHeader(value = "host",defaultValue = "127.0.0.1") String host,
                           @RequestHeader("user-agent") String ua){
        System.out.println(host);
        System.out.println(ua);
        return "ok~"+host;
    }


    /**
     * @CookieValue：获取cookie值
     * @param haha
     * @return
     */
    @RequestMapping("/handle05")
    public String handle05(@CookieValue("haha") String haha){
        return "ok：cookie是：" + haha;
    }

    /**
     * 使用pojo级联封装复杂属性
     * @param person
     * @return
     */
    @RequestMapping("/handle06")
    public String handle06(Person person){
        System.out.println(person);
        return "ok";
    }


    /**
     * @RequestBody: 获取请求体json数据，自动转为person对象
     * 测试接受json数据
     * 1、发出：请求体中是json字符串，不是k=v
     * 2、接受：@RequestBody Person person
     *
     * @RequestBody Person person
     *      1、拿到请求体中的json字符串
     *      2、把json字符串转为person对象
     * @param person
     * @return
     */
    @RequestMapping("/handle07")
    public String handle07(@RequestBody Person person){
        System.out.println(person);
        //自己把字符串转为对象。
        return "ok";
    }


    /**
     * 文件上传；
     * 1、@RequestParam 取出文件项，封装为MultipartFile，就可以拿到文件内容
     * @param person
     * @return
     */
    @RequestMapping("/handle08")
    public String handle08(Person person,
                           @RequestParam("headerImg") MultipartFile headerImgFile,
                           @RequestPart("lifeImg") MultipartFile[] lifeImgFiles) throws IOException {

        //1、获取原始文件名
        String originalFilename = headerImgFile.getOriginalFilename();
        //2、文件大小
        long size = headerImgFile.getSize();
        //3、获取文件流
        InputStream inputStream = headerImgFile.getInputStream();
        System.out.println(originalFilename + " ==> " + size);
        //4、文件保存
        headerImgFile.transferTo(new File("D:\\img\\" + originalFilename));
        System.out.println("===============以上处理了头像=================");
        if (lifeImgFiles.length > 0) {
            for (MultipartFile imgFile : lifeImgFiles) {
                imgFile.transferTo(new File("D:\\img\\" + imgFile.getOriginalFilename()));
            }
            System.out.println("=======生活照保存结束==========");
        }
        System.out.println(person);
        return "ok!!!";
    }


    /**
     * HttpEntity：封装请求头、请求体； 把整个请求拿过来
     *    泛型：<String>：请求体类型； 可以自动转化
     *
     *
     * @return
     */
    @RequestMapping("/handle09")
    public String handle09(HttpEntity<Person> entity){

        //1、拿到所有请求头
        HttpHeaders headers = entity.getHeaders();
        System.out.println("请求头："+headers);
        //2、拿到请求体
        Person body = entity.getBody();
        System.out.println("请求体："+body);
        return "Ok~~~";
    }


    /**
     * 接受原生 API
     * @param request
     * @param response
     */
    @RequestMapping("/handle10")
    public void handle10(HttpServletRequest request,
                           HttpServletResponse response,
                         HttpMethod method) throws IOException {
        System.out.println("请求方式："+method);
        String username = request.getParameter("username");
        System.out.println(username);
        response.getWriter().write("ok!!!"+username);
    }



}

~~~~

如果返回的是对象那么，框架自动会把对象转化为json

#### 响应

~~~java
@RestController
public class ResponseTestController {


    /**
     * 会自动的把返回的对象转为json格式
     *
     * @return
     */
//    @ResponseBody //把返回的内容。写到响应体中
    @RequestMapping("/resp01")
    public Person resp01() {
        Person person = new Person();
        person.setUsername("张三");
        person.setPassword("1111");
        person.setCellphone("22222");
        person.setAgreement(false);
        person.setSex("男");
        person.setHobby(new String[]{"足球", "篮球"});
        person.setGrade("三年级");

        return person;
    }


    /**
     * 文件下载
     * HttpEntity：拿到整个请求数据
     * ResponseEntity：拿到整个响应数据（响应头、响应体、状态码）
     *
     * @return
     */
    @RequestMapping("/download")
    public ResponseEntity<InputStreamResource> download() throws IOException {

        //以上代码永远别改
        FileInputStream inputStream = new FileInputStream("C:\\Users\\53409\\Pictures\\Saved Pictures\\必应壁纸（1200张）\\AutumnNeuschwanstein_EN-AU10604288553_1920x1080.jpg");
        //一口气读会溢出
//        byte[] bytes = inputStream.readAllBytes();
        //1、文件名中文会乱码：解决：
        String encode = URLEncoder.encode("哈哈美女.jpg", "UTF-8");
        //以下代码永远别改
        //2、文件太大会oom（内存溢出）
        InputStreamResource resource = new InputStreamResource(inputStream);
        return ResponseEntity.ok()
                //内容类型：流
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                //内容大小
                .contentLength(inputStream.available())
                //  Content-Disposition ：内容处理方式
                .header("Content-Disposition", "attachment;filename="+encode)
                .body(resource);
    }
}

~~~

#### 跨域

~~~java
@Configuration
public class WebConfig {

    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")  // 允许所有路径
                        .allowedOrigins("http://example.com")  // 允许来自指定域名的请求
                        .allowedMethods("GET", "POST", "PUT", "DELETE")  // 允许的请求方法
                        .allowedHeaders("*")  // 允许的请求头
                        .allowCredentials(true)  // 是否允许携带凭证
                        .maxAge(3600);  // 缓存时间，单位秒
            }
        };
    }
}
~~~

#### 拦截器

### Mybatis

#### 参数取值

#{} 表示取值

${} 表示拼接

当接口函数只有一个参数时 在xml中可以直接写它的属性

当接口函数有多个参数时 要加@Param注解 在xml中要使用   对象.属性

#### 返回值

查出的结果为基本类型的时候直接写全类名 比如int long bigdecimal； 当查出对象列表时 用对象类名 ；当查出为map时候， 采用注解@MapKey("**") 表示**为其中的key 而且value并不是查出来的对象 而是对象的Hashmap形式 key--value对

查出的结果为自定义封装类型时 

和数据库字段匹配不上的字段会被封装为null怎么办

0.使JavaBean和数据库名一样

1.使用列别名

2.开启驼峰命名

3.使用ResultMap

4.使用ResultMap封装自己的规则

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.atguigu.mybatis.mapper.EmpReturnValueMapper">


    <!--   有别名，但不用 -->
    <select id="countEmp" resultType="long">
        select count(*)
        from t_emp
    </select>
    <select id="getEmpSalaryById" resultType="java.math.BigDecimal">
        select emp_salary
        from t_emp
        where id = #{id}
    </select>

    <!--   返回集合，写集合中元素类型 -->
    <select id="getAll" resultType="com.atguigu.mybatis.bean.Emp">
        select *
        from t_emp
    </select>

    <!-- 返回map集合，  resultType 写map中value元素类型 -->
    <select id="getAllMap" resultType="com.atguigu.mybatis.bean.Emp">
        select *
        from t_emp
    </select>


    <!--
        默认封装规则（resultType）：JavaBean 中的属性名 去数据库表中 找对应列名的值。一一映射封装。
        自定义结果集（resultMap）：我们来告诉MyBatis 如何把结果封装到Bean中;
             明确指定每一列如何封装到指定的Bean中
      -->
    <resultMap id="EmpRM" type="com.atguigu.mybatis.bean.Emp">
        <!--id：声明主键映射规则-->
        <id column="id" property="id"></id>
        <!--result：声明普通列映射规则-->
        <result column="emp_name" property="empName"></result>
        <result column="age" property="age"></result>
        <result column="emp_salary" property="empSalary"></result>
    </resultMap>

    <!-- resultMap：指定自定义映射规则   -->
    <select id="getEmpById" resultMap="EmpRM">
        select *
        from t_emp
        where id = #{id}
    </select>
~~~

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.atguigu.mybatis.mapper.OrderMapper">


    <!--   自定义结果集 -->
    <resultMap id="OrderRM" type="com.atguigu.mybatis.bean.Order">
        <id column="id" property="id"></id>
        <result column="address" property="address"></result>
        <result column="amount" property="amount"></result>
        <result column="customer_id" property="customerId"></result>
        <!--      一对一关联封装  -->
        <association property="customer"
                     javaType="com.atguigu.mybatis.bean.Customer">
            <id column="c_id" property="id"></id>
            <result column="customer_name" property="customerName"></result>
            <result column="phone" property="phone"></result>
        </association>
    </resultMap>


    <select id="getOrderByIdWithCustomer"
            resultMap="OrderRM">
        select o.*,
               c.id c_id,
               c.customer_name,
               c.phone
        from t_order o
                 left join t_customer c on o.customer_id = c.id
        where o.id = #{id}
    </select>
</mapper>
~~~

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.atguigu.mybatis.mapper.CustomerMapper">

    <resultMap id="CutomerRM" type="com.atguigu.mybatis.bean.Customer">
        <id column="c_id" property="id"></id>
        <result column="customer_name" property="customerName"></result>
        <result column="phone" property="phone"></result>
<!--
collection：说明 一对N 的封装规则
ofType: 集合中元素的类型
-->
        <collection property="orders" ofType="com.atguigu.mybatis.bean.Order">
            <id column="id" property="id"></id>
            <result column="address" property="address"></result>
            <result column="amount" property="amount"></result>
            <result column="c_id" property="customerId"></result>
        </collection>
    </resultMap>


    <select id="getCustomerByIdWithOrders" resultMap="CutomerRM">
        select c.id c_id,
               c.customer_name,
               c.phone,
               o.*
        from t_customer c
                 left join t_order o on c.id = o.customer_id
        where c.id = #{id}
    </select>

    <select id="getAllCustomersWithOrders"
            resultMap="com.atguigu.mybatis.mapper.OrderCustomerStepMapper.CustomerOrdersStepRM">
        select * from t_customer
    </select>
</mapper>
~~~

#### 分步查询

原生分步需要我们调用两次

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.atguigu.mybatis.mapper.OrderCustomerStepMapper">


    <!--   按照id查询客户 -->
    <select id="getCustomerById" resultMap="CustomerOrdersStepRM">
        select *
        from t_customer
        where id = #{id}
    </select>

    <!--   按照客户id查询他的所有订单  resultType="com.atguigu.mybatis.bean.Order" -->
    <select id="getOrdersByCustomerId" resultType="com.atguigu.mybatis.bean.Order">
        select *
        from t_order
        where customer_id = #{cId}
    </select>


    <!--   分步查询的自定义结果集： -->
    <resultMap id="CustomerOrdersStepRM" type="com.atguigu.mybatis.bean.Customer">
        <id column="id" property="id"></id>
        <result column="customer_name" property="customerName"></result>
        <result column="phone" property="phone"></result>
        <collection property="orders"
                    select="com.atguigu.mybatis.mapper.OrderCustomerStepMapper.getOrdersByCustomerId"
                    column="id">
        </collection>
        <!--    告诉MyBatis，封装 orders 属性的时候，是一个集合，
                但是这个集合需要调用另一个 方法 进行查询；select：来指定我们要调用的另一个方法
                column：来指定我们要调用方法时，把哪一列的值作为传递的参数，交给这个方法
                   1）、column="id"： 单传参：id传递给方法
                   2）、column="{cid=id,name=customer_name}"：多传参（属性名=列名）；
                        cid=id：cid是属性名，它是id列的值
                        name=customer_name：name是属性名，它是customer_name列的值
        -->

    </resultMap>

    <select id="getCustomerByIdAndOrdersStep" resultMap="CustomerOrdersStepRM">
        select *
        from t_customer
        where id = #{id}
    </select>


    <!--   分步查询：自定义结果集；封装订单的分步查询  -->
    <resultMap id="OrderCustomerStepRM" type="com.atguigu.mybatis.bean.Order">
        <id column="id" property="id"></id>
        <result column="address" property="address"></result>
        <result column="amount" property="amount"></result>
        <result column="customer_id" property="customerId"></result>
        <!--       customer属性关联一个对象，启动下一次查询，查询这个客户 -->
        <association property="customer"
                     select="com.atguigu.mybatis.mapper.OrderCustomerStepMapper.getCustomerById"
                     column="customer_id">
        </association>
    </resultMap>

    <select id="getOrderByIdAndCustomerStep" resultMap="OrderCustomerStepRM">
        select *
        from t_order
        where id = #{id}
    </select>


    <!--   查询订单 + 下单的客户 + 客户的其他所有订单 -->
    <select id="getOrderByIdAndCustomerAndOtherOrdersStep"
            resultMap="OrderCustomerStepRM">
        select * from t_order where id = #{id}
    </select>


</mapper>
~~~

分步查询中，最后一步要使用resultType 否则会陷入无限的循环

#### Dynamic SQL

~~~xml
<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd" >
<mapper namespace="com.atguigu.mybatis.mapper.EmpDynamicSQLMapper">



<!--
 sql：抽取可复用的sql片段
 include：引用sql片段，refid属性：sql片段的id
 -->
    <sql id="column_names">
        id,emp_name empName,age,emp_salary empSalary
    </sql>


    <!-- where 版
     if标签：判断；
         test：判断条件； java代码怎么写，它怎么写
     where标签：解决 where 后面 语法错误问题（多and、or, 无任何条件多where）
     -->
<!--    <select id="queryEmpByNameAndSalary" resultType="com.atguigu.mybatis.bean.Emp">-->
<!--        select * from t_emp-->
<!--        <where>-->
<!--            <if test="name != null">-->
<!--                emp_name= #{name}-->
<!--            </if>-->
<!--            <if test="salary != null">-->
<!--                and emp_salary = #{salary};-->
<!--            </if>-->
<!--        </where>-->
<!--    </select>-->



<!-- trim版本实现where
    prefix：前缀 ; 如果标签体中有东西，就给它们拼一个前缀
    suffix：后缀
    prefixOverrides：前缀覆盖； 标签体中最终生成的字符串，如果以指定前缀开始，就覆盖成空串
    suffixOverrides：后缀覆盖

-->
    <select id="queryEmpByNameAndSalary" resultType="com.atguigu.mybatis.bean.Emp">
        select * from t_emp
        <trim prefix="where" prefixOverrides="and || or">
            <if test="name != null">
                emp_name= #{name}
            </if>
            <if test="salary != null">
                and emp_salary = #{salary}
            </if>
        </trim>

    </select>



    <!--  set：和where一样，解决语法错误问题。
       update t_emp where id=1
      -->
<!--    <update id="updateEmp">-->
<!--        update t_emp-->
<!--            <set>-->
<!--                <if test="empName != null">-->
<!--                    emp_name = #{empName},-->
<!--                </if>-->
<!--                <if test="empSalary != null">-->
<!--                    emp_salary = #{empSalary},-->
<!--                </if>-->
<!--                <if test="age!=null">-->
<!--                    age = #{age}-->
<!--                </if>-->
<!--            </set>-->
<!--        where id = #{id}-->
<!--    </update>-->

<!--   trim： 版本实现 set
suffix="where id = #{id}"
-->
    <update id="updateEmp">
        update t_emp
            <trim prefix="set" suffixOverrides="," >
                <if test="empName != null">
                    emp_name = #{empName},
                </if>
                <if test="empSalary != null">
                    emp_salary &lt; #{empSalary},
                </if>
                <if test="age!=null">
                    age = #{age}
                </if>
            </trim>
        where id = #{id}
    </update>
    <update id="updateBatchEmp">
        <foreach collection="emps" item="e" separator=";">
            update t_emp
            <set>
                <if test="e.empName != null">
                    emp_name = #{e.empName},
                </if>
                <if test="e.empSalary != null">
                    emp_salary = #{e.empSalary},
                </if>
                <if test="e.age!=null">
                    age = #{e.age}
                </if>
            </set>
            where id=#{e.id}
        </foreach>
    </update>


    <select id="queryEmpByNameAndSalaryWhen" resultType="com.atguigu.mybatis.bean.Emp">
        select * from t_emp
        <where>
            <choose>
                <when test="name != null">
                    emp_name= #{name}
                </when>
                <when test="salary > 3000">
                    emp_salary = #{salary}
                </when>
                <otherwise>
                    id = 1
                </otherwise>
            </choose>
        </where>
    </select>

<!--  for(Integer id :ids)
foreach: 遍历List,Set,Map,数组
        collection：指定要遍历的集合名
        item：将当前遍历出的元素赋值给指定的变量
        separator：指定在每次遍历时，元素之间拼接的分隔符
        open：遍历开始前缀； 不开始遍历就不会有这个
        close：遍历结束后缀
-->

    <select id="getEmpsByIdIn" resultType="com.atguigu.mybatis.bean.Emp">
        select
            <include refid="column_names"></include>
            from t_emp
            <if test="ids != null">
                <foreach collection="ids" item="id" separator="," open="where id IN (" close=")">
                    #{id}
                </foreach>
            </if>

    </select>




    <insert id="addEmps">
        insert into t_emp(emp_name,age,emp_salary)
        values
        <foreach collection="emps" item="emp" separator=",">
            (#{emp.empName},#{emp.age},#{emp.empSalary})
        </foreach>
    </insert>
</mapper>
~~~

#### 分页 PageHelper

分页的Interceptor,每次SQL查询之前会自动拼装分页数据

第一页 limit 0, 10

第二页 limit 10, 10

第三页 limit 20, 10

计算规则 pageNum = 1, pageSize = 10

startIndex = (pageNum - 1) * pageSize



如何使用

1.导入依赖

2.配置pagehelper拦截器





后端需要的数据

1.前端传来的页码



响应前端需要的数据

1.总页码 / 总记录数

2.当前页码

3.本页数据

~~~java
@SpringBootTest
public class PageTest {

    @Autowired
    EmpService empService;

    @Test
    void test02(){

        //后端收到前端传来的页码


        //响应前端需要的数据：
        //1、总页码、总记录数
        //2、当前页码
        //3、本页数据
        PageHelper.startPage(3,5);
        // 紧跟着 startPage 之后 的方法就会执行的 SQL 分页查询
        List<Emp> all = empService.getAll();
        System.out.println("============");

        //以后给前端返回它
        PageInfo<Emp> info = new PageInfo<>(all);

        //当前第几页
        System.out.println("当前页码："+info.getPageNum());
        //总页码
        System.out.println("总页码："+info.getPages());
        //总记录
        System.out.println("总记录数："+info.getTotal());
        //有没有下一页
        System.out.println("有没有下一页："+info.isHasNextPage());
        //有没有上一页
        System.out.println("有没有上一页："+info.isHasPreviousPage());
        //本页数据
        System.out.println("本页数据："+info.getList());

    }

    @Test
    void test01(){

        /**
         * 原理：拦截器；
         * 原业务底层：select * from emp;
         * 拦截做两件事：
         * 1）、统计这个表的总数量
         * 2）、给原业务底层SQL 动态拼接上 limit 0,5;
         *
         * ThreadLocal： 同一个线程共享数据
         *    1、第一个查询从 ThreadLocal 中获取到共享数据，执行分页
         *    2、第一个执行完会把 ThreadLocal 分页数据删除
         *    3、以后的查询，从 ThreadLocal 中拿不到分页数据，就不会分页
         *
         */
        PageHelper.startPage(3,5);
        // 紧跟着 startPage 之后 的方法就会执行的 SQL 分页查询
        List<Emp> all = empService.getAll();
        for (Emp emp : all) {
            System.out.println(emp);
        }

        System.out.println("===============");
        List<Emp> all1 = empService.getAll();
        System.out.println(all1.size());
    }
}
~~~

