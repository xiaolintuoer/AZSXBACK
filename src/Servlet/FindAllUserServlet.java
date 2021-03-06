package Servlet;

import DB.Bean.User;
import DB.Dao.UserDao;
import DB.server.UserServer;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.List;


/**
 * 功能:显示所有用户的信息
 * 参数需要: username，password
 *权限不足抛出401异常
 * URL:/AZSXBACK_war_exploded/AllUser
 *返回:json字符串数组{"userinfo":[{"uid":["username","password","birthday","phonenumber,email,u_type,registr_date"]}]}
 */
@WebServlet(name = "FindAllUserServlet" ,value = "/AllUser")
public class FindAllUserServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        String pwd = "";
        String username = "";
        //显示请求发生前先判断用户权限，无权限拒绝连接并抛出
        try{
            username = request.getParameter("username");
            pwd = request.getParameter("password");
            UserDao userinfo = new UserServer();
            if(!userinfo.Login(username,pwd)){
                response.sendError(401,"登录失败！");
                return;
            }
            int uid = userinfo.Find_UUid_ByUname(username);
            int type = userinfo.Find_User_type(uid);
            if(type != 2){
                response.sendError(401,"当前用户无访问权限");
                return;
            }
            List<User> userList = userinfo.Find_AllUser();
                String info = Print_Helper(userList);
                PrintWriter out = response.getWriter();
                out.print(info);
        }
        catch (Exception e){
            response.sendError(400,"请检查参数username =?,password=?");
            e.printStackTrace();
        }
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("text/html;charset=utf-8");
        doPost(request,response);
    }

    private String Print_Helper(List<User> userList) {
        StringBuffer sb = new StringBuffer();
        sb.append("{");
        sb.append("\"userinfo\":[{");
        for (int i = 0; i < userList.size(); i++) {
            int uid = userList.get(i).getUid();
            String uame = userList.get(i).getUsername();
            String password = userList.get(i).getPassword();
            Date brithday = userList.get(i).getBirthday();
            String phone = userList.get(i).getPhonenumber();
            String email = userList.get(i).getEmail();
            int u_type = userList.get(i).getU_type();
            Date regtime = userList.get(i).getRegisttime();
            String s_uid = String.valueOf(uid);
            String reg = String.valueOf(regtime);
            String s_brithday = String.valueOf(brithday);
            String s_u_type = String.valueOf(u_type);
            //获取并封装users
            String arr[] = {uame, password, s_brithday, phone, email, s_u_type, reg};
            sb.append("\"" + s_uid + "\":");
            sb.append("[");
            //初始化预读取列表长度
            int n = 0;
            for (String s : arr) {
                n += 1;
                sb.append("\"" + s + "\"");
                //n的比较值为arr的长度
                if (n < arr.length) {
                    sb.append(",");
                }
            }
            if (i < userList.size() - 1) {
                //设置json字符串结尾
                sb.append("]}" + ",");
            } else {
                //设置json数组字符串结尾
                sb.append("}]}");
            }



        }
        return sb.toString();
    }
}
