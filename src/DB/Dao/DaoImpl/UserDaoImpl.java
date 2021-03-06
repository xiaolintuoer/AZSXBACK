package DB.Dao.DaoImpl;

import DB.Bean.User;
import DB.DBhelper.ConnectionManager;
import DB.Dao.UserDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
/**
 * Date 2020/1/4
 * 功能:实现新闻读取接口
 * */
public class UserDaoImpl implements UserDao {
    @Override
    public int Regist(String username, String password,Date registrdate) {
        //初始化返回值
        int count = 0;
        //获取数据库连接
        Connection conn = ConnectionManager.getConnection();
        //定义SQL字段
        String sql = "insert into user_info (username, password, registr_date) values(?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            //将注册时间转换为SQL时间储存
            java.sql.Date sqlDate=new java.sql.Date(registrdate.getTime());
            //设置占位符
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            pstmt.setDate(3,sqlDate);
            //提交更新,返回更新条数
            count = pstmt.executeUpdate();
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }
        return count;
    }

    /**
     * 功能:根据用户名和密码登录
     * @param username 用户名
     * @param password 用户密码
     * */
    @Override
    public boolean Login(String username, String password) {
        //默认登录状态为false
        boolean is_login = false;
        Connection conn = ConnectionManager.getConnection();
        String sql = "select * from user_info where username=? and password = ?";
        try {
            //创建数据库语句预备对象
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            pstmt.setString(2,password);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()){
                is_login = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }
        return is_login;
    }


    /**
     * 功能：添加用户信息
     * @param username 用户名
     * @param phone_number 电话号码
     * @param email 邮箱
     * @param birthday 生日
     * */
    @Override
    public int Add_UserInfo(String username,String phone_number, String email, Date birthday) {
        //返回添加成功条数
        int count = 1;
        //初始化数据库连接，定义SQL语句
        Connection conn = ConnectionManager.getConnection();
        String sql = "update user_info " +
                "set phone_number= ? ,email = ? ,birthday = ?" +
                "where username = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,phone_number);
            pstmt.setString(2,email);
            //将注册时间转换为SQL时间储存
            java.sql.Date sqlDate=new java.sql.Date(birthday.getTime());
            pstmt.setDate(3,sqlDate);
            pstmt.setString(4,username);
            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }
        //返回更新成功数量
        return count;
    }

    /**
     * 通过uid查询用户状态
     * @param uid 用户id
     * @return 用户状态 int
     */
    @Override
    public int Find_User_type(int uid) {
        //默认状态为1
        int type = 1;
        //初始化数据库连接，sql语句
        Connection conn = ConnectionManager.getConnection();
        String sql = "SELECT u_type from user_info where uid=?";
        try {
            //创建数据库语句预备对象
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,uid);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                type=rs.getInt("u_type");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }

        return type;
    }


    /**
     * 功能:通过用户名查找用户ID
     * @param unmae 用户名
     * @return uuid
     * */
    @Override
    public int Find_UUid_ByUname(String unmae) {
        int uuid = 0;
        Connection conn = ConnectionManager.getConnection();
        String sql = "select uid from user_info where username=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,unmae);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                uuid = rs.getInt("uid");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }

        return uuid;
    }


    /**
     * 功能:通过用户名删除用户
     * @param username 用户名
     * @return 删除行数
     * */
    @Override
    public int Del_User_Byusername(String username) {
        //初始化删除条数
        int count = 0;
        //获取数据库连接
        Connection conn = ConnectionManager.getConnection();
        //定义SQL语句
        String sql = "delete from user_info where username = ?";

        try {
            //设置数据库语句预备对象
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);

            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }
        //返回删除行数
        return count;
    }


    /**
     * 功能:通过uid删除用户
     * @param uid 用户uid
     * @return count 删除行数
     * */
    @Override
    public int Del_User_ByUid(int uid) {
        //初始化删除条数
        int count = 0;
        //获取数据库连接
        Connection conn = ConnectionManager.getConnection();
        //定义SQL语句
        String sql = "delete from user_info where uid = ?";

        try {
            //设置数据库语句预备对象
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,uid);
            //执行sql语句，返回删除量
            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭数据库连接
            ConnectionManager.closeConnection(conn);
        }
        //返回删除行数
        return count;
    }

    /**
     * 功能:更新用户密码
     * @param username 用户名,String
     * @param password 用户密码,String
     * */
    @Override
    public int User_Updata_password(String username, String password) {
        //初始化更新条数，默认0
        int count = 0;
        //创建数据库连接
        Connection conn = ConnectionManager.getConnection();
        //初始化SQL语句
        String sql = "update user_info set password = ? where username = ?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,password);
            pstmt.setString(2,username);
            count = pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();

        }finally {
            //关闭数据库连接
            ConnectionManager.closeConnection(conn);
        }

        return count;
    }

    /**
     * 功能:通过用户名查找用户信息
     * @param username 用户名
     */

    @Override
    public User Find_Userinfo_ByUname(String username) {
        //初始化用户信息储存
        User user_info = new User();

        Connection conn = ConnectionManager.getConnection();
        String sql = "select * from user_info where username=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,username);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                user_info.setUid(rs.getInt(1));
                user_info.setUsername(rs.getString(2));
                user_info.setPassword( rs.getString(3));
                user_info.setBirthday( rs.getDate(4));
                user_info.setPhonenumber(rs.getString(5));
                user_info.setEmail(rs.getString(6));
                user_info.setU_type(rs.getInt(7));
                user_info.setRegisttime(rs.getDate(8));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭数据库连接
            ConnectionManager.closeConnection(conn);
        }
        return user_info;
    }

    /**
     * 功能:通过用户uid更改用户状态
     * @param uid 用户uid
     * @param u_type 用户状态
     * @return count 更新数
     * */
    @Override
    public int UpdateUtype_ByUUid(int uid,int u_type) {
        //初始化更新数
        int count = 0;
        //创建数据库连接
        Connection conn = ConnectionManager.getConnection();
        //定义更新语句
        String sql = "update user_info set u_type = ?  where uid = ?";

        try {
            //创建数据库语句预备对象
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1,u_type);
            pstmt.setInt(2,uid);
            count = pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }

        return count;
    }


   /**
    * 功能:显示所有用户
    * @return  返回User集合
    * */
    @Override
    public List<User> Find_AllUser() {
        //初始化返回值
        List<User> list_User = new ArrayList<User>();
        //初始化数据库连接及数据库字段
        Connection conn = ConnectionManager.getConnection();
        String sql = "select * from user_info";
        try {
            Statement stmt = conn.createStatement();
            ResultSet rs =stmt.executeQuery(sql);
            while (rs.next()){
                User user = new User();
                //获取表达数据并写入
                user.setUid(rs.getInt("uid"));

                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setBirthday(rs.getDate("birthday"));
                user.setPhonenumber(rs.getString("phone_number"));
                user.setEmail(rs.getString("email"));
                user.setU_type(rs.getInt("u_type"));
                user.setRegisttime(rs.getTimestamp("registr_date"));
                list_User.add(user);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭数据库连接
            ConnectionManager.closeConnection(conn);
        }


        return list_User;
    }


    /**
     * 功能:通过电话号码查询用户名
     * @param  phone 类型:String
     * @return username 用户名
     * */
    @Override
    public String Find_Username_ByPhone(String phone) {
        //初始化数据库连接及数据库字段
        String  username = "";
        Connection conn = ConnectionManager.getConnection();
        String sql = "select username from user_info where phone_number=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(sql);
            pstmt.setString(1,phone);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()){
                username = rs.getString("username");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            ConnectionManager.closeConnection(conn);
        }
        return username;
    }
}
