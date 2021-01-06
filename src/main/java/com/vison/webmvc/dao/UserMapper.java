package com.vison.webmvc.dao;

import com.vison.webmvc.entity.User;
import org.apache.ibatis.annotations.Select;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public interface UserMapper {

    @Select("SELECT * FROM user WHERE id = #{id}")
    User selectUser(int id);

}
