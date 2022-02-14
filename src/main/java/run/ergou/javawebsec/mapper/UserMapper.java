package run.ergou.javawebsec.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import run.ergou.javawebsec.entity.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface UserMapper extends BaseMapper<User> {
    @Select("SELECT * FROM users WHERE username = '${name}' AND password = '${passwd}'")
    List<User> selectByNameAndPass1(@Param("name") String name, @Param("passwd") String passwd);

    @Select("SELECT * FROM users WHERE username = #{name} AND password = #{passwd}")
    List<User> selectByNameAndPass2(@Param("name") String name, @Param("passwd") String passwd);

    @Select("SELECT * FROM users WHERE username LIKE '%${name}%'")
    List<User> like(@Param("name") String name);

    @Select("SELECT * FROM users WHERE username LIKE concat('%', #{name}, '%')")
    List<User> like2(@Param("name") String name);

    @Select("SELECT * FROM users ORDER BY ${name}")
    List<User> orderBy(@Param("name") String name);

    @Select("SELECT * FROM users WHERE id in (${ids})")
    List<User> in(@Param("ids") String ids);

    List<User> in2(@Param("ids") List<Integer> ids);

    @Select("SELECT * FROM users WHERE username in (${names})")
    List<User> in3(@Param("names") String names);

    List<User> in4(@Param("names") List<String> names);
}
