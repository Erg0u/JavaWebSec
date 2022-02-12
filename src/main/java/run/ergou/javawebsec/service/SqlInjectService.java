package run.ergou.javawebsec.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import run.ergou.javawebsec.entity.User;
import run.ergou.javawebsec.mapper.UserMapper;

import javax.annotation.Resource;
import java.sql.*;
import java.util.HashMap;
import java.util.List;

@Service
public class SqlInjectService {
    private final String driver = "com.mysql.cj.jdbc.Driver";
    private final String url = "jdbc:mysql://localhost:3306/java_sec_code?AllowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC";
    private final String username = "root";
    private final String password = "root";

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

//  JDBC-拼接SQL
    public String inject1(String name, String passwd) {

        String sql = "SELECT * FROM users where username = '" + name + "' AND password = '" +  passwd + "'";
        logger.info(sql);

        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);
            StringBuilder ret = new StringBuilder();
            while (resultSet.next()) {
                ret.append(resultSet.getString("username"));
            }
            return ret.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // JDBC-预编译
    public String inject2(String name, String passwd) {

        String sql = "SELECT * FROM users where username = ? AND password = ?";

        try {
            Class.forName(driver);
            Connection conn = DriverManager.getConnection(url, username, password);
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, passwd);

            logger.info(preparedStatement.toString());
            ResultSet resultSet = preparedStatement.executeQuery();
            StringBuilder ret = new StringBuilder();
            while (resultSet.next()) {
                ret.append(resultSet.getString("username"));
            }
            return ret.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Resource
    UserMapper userMapper;
    // mybatis ${}
    public String inject3(String name, String passwd) {
        List<User> users = userMapper.selectByNameAndPass1(name, passwd);
        return users.toString();
    }
    // mybatis #{}
    public String inject4(String name, String passwd) {
        List<User> users = userMapper.selectByNameAndPass2(name, passwd);
        return users.toString();
    }
    // like
    public String inject5(String name) {
        List<User> users = userMapper.like(name);
        return users.toString();
    }
    // 无注入 like
    public String inject6(String name) {
        List<User> users = userMapper.like2(name);
        return users.toString();
    }
    // order by
    public String inject7(String name) {
        List<User> users = userMapper.orderBy(name);
        return users.toString();
    }

    // 无注入 order by
    public String inject8(Integer name) {
        HashMap<Integer, String> fields = new HashMap<>();
        fields.put(1, "username");
        fields.put(2, "description");
        List<User> users = userMapper.orderBy(fields.get(name));
        return users.toString();
    }

    // in
    public String inject9(String ids) {
        List<User> users = userMapper.in(ids);
        return users.toString();
    }

    // 无注入 in
    public String inject10(List<Integer> ids) {
        List<User> users = userMapper.in2(ids);
        return users.toString();
    }

    public String inject11(String names) {
        List<User> users = userMapper.in3(names);
        return users.toString();
    }

    public String inject12(List<String> names) {
        List<User> users = userMapper.in4(names);
        return users.toString();
    }

}
