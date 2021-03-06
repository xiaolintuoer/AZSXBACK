package Servlet;

import DB.Dao.DaoImpl.UserDaoImpl;
import DB.Dao.UserDao;
import DB.server.UserServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * created by xiaolintuoer on 2020/1/6
 * 功能:实现用户登录验证
 * URL: /AZSXBACK_war_exploded/Login
 *
 * */
@WebServlet(name = "LoginServlet",value = "/Login"
            )
public class LoginServlet extends HttpServlet {
    /**
     * 参数需要: username，password，mode
     *mode =0,用户名+密码登录
     * mode =1,电话号码+密码登录
     * 如果登录成功返回 {"login": "true"}
     * 登录失败返回 {"login" : "false"}
     * 默认为登录失败
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取表单数据
        String username="";
        String pwd="";
        String mode ="";
        try {
            request.setCharacterEncoding("utf-8");
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/html;charset=utf-8");
            username = request.getParameter("username");
            pwd = request.getParameter("password");
            mode = request.getParameter("mode");
            boolean test = mode.equals("?");


        }catch (Exception e){
            response.sendError(400, "请检查参数username=?,password=?,mode=?" );
            e.printStackTrace();
            return;
        }
        if (mode.equals("0")){
                UserDao userinfo = new UserServer();
                boolean type = userinfo.Login(username,pwd);
                if (type){
                    String str = "{\"login\" : \"true\"}";
                    PrintWriter out = response.getWriter();
                    out.print(str);
                    }
                else{
                String str = "{\"login\" : \"false\"}";
                PrintWriter out = response.getWriter();
                out.print(str);
                }
            }
        else if (mode.equals("1")){
                UserDao userDao = new UserServer();
                String us = userDao.Find_Username_ByPhone(username);
                if(!us.equals("")){
                    boolean type = userDao.Login(us,pwd);
                    if (type){
                        String str = "{\"login\" : \"true\"}";
                        PrintWriter out = response.getWriter();
                        out.print(str);
                        System.out.println(str);
                    }
                    else {
                        String str = "{\"login\" : \"false\"}";
                        PrintWriter out = response.getWriter();
                        out.print(str);
                        System.out.println(str);
                    }
                }else {
                    String str = "{\"login\" : \"false\"}";
                    PrintWriter out = response.getWriter();
                    out.print(str);
                }

            }
        else {
            response.sendError(400,"请检查请求值！");
        }

    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        doPost(request,response);
    }

}
