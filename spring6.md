1.xml文件的配置

学习了关于对象的创建

普通属性配置

对象属性配置

数组属性配置

list属性配置

map属性配置

p命名空间

外部属性的注入 egg:数据库的连接配置

单实例与多实例



ioc的生命周期

1.调用无参构造生成对象

2.注入相关属性

3.后置 在初始化之前

4.初始化

5.后置 初始化之后

6.生成一个对象 可以使用了

7.销毁



controller service dao

这三层之间会调用其他层的方法 那么在一层之间添加其他层的对象 这时候再去配置xml时可以使用自动配置 分别配置完各个层的<bean>后添加autowrite 可以自动匹配属性对应的对象





基于注解的方式实现注入







## 注解

### Ioc

@Component  

@Service

@Controller

@Respository

@Autowired    注入属性的   用在属性上、setter方法上、构造方法上、构造方法参数上。 

@@Qualifier   配合@AutoWired使用根据名称注入

@Resource jdk自带的先根据name属性 未指定则使用属性名（默认为首字母小写的）为name 如果还是迷不醒则使用byType装配 作用在 属性和 setter方法上



| 注解        | 说明                                                         |
| ----------- | ------------------------------------------------------------ |
| @Component  | 该注解用于描述 Spring 中的 Bean，它是一个泛化的概念，仅仅表示容器中的一个组件（Bean），并且可以作用在应用的任何层次，例如 Service 层、Dao 层等。  使用时只需将该注解标注在相应类上即可。 |
| @Repository | 该注解用于将数据访问层（Dao 层）的类标识为 Spring 中的 Bean，其功能与 @Component 相同。 |
| @Service    | 该注解通常作用在业务层（Service 层），用于将业务层的类标识为 Spring 中的 Bean，其功能与 @Component 相同。 |
| @Controller | 该注解通常作用在控制层（如SpringMVC 的 Controller），用于将控制层的类标识为 Spring 中的 Bean，其功能与 @Component 相同。 |







### Aop

@Aspect  //声明切面类

@Before  

@After

@AfterReturning

@AfterThrowing

@Around



- 前置通知：使用@Before注解标识，在被代理的目标方法**前**执行
- 返回通知：使用@AfterReturning注解标识，在被代理的目标方法**成功结束**后执行（**寿终正寝**）
- 异常通知：使用@AfterThrowing注解标识，在被代理的目标方法**异常结束**后执行（**死于非命**）
- 后置通知：使用@After注解标识，在被代理的目标方法**最终结束**后执行（**盖棺定论**）
- 环绕通知：使用@Around注解标识，使用try...catch...finally结构围绕**整个**被代理的目标方法，包括上面四种通知对应的所有位置





## jdbcTemplate

