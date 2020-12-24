/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.vison.webmvc;

/**
 *
 * @author vison.cao <visonforcoding@gmail.com>
 */
public class ResponseCode {

    public static Integer success = 0;
    public static Integer dbInsertFail = 101;
    public static Integer parametrErrror = 510;

    /**
     * 未登录
     */
    public static Integer unLogin = 401;

    public static Integer loginFail = 402;
}
