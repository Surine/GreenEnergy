package com.greenenergy.greenenergy.MyData;

/**
 * Created by surine on 2017/8/18.
 */

public class NetWorkData {
    //服务器IP端口
    public static String IP_port = "http://surine.cn:6060";
    public static String IP_port_80 = "http://surine.cn:80";
    //使用协议
    public static String lis = "http://surine.cn/GreenEnergyFiles/user_lis.html";
    //押金说明
    public static String money= "http://surine.cn/GreenEnergyFiles/money_lis.html";
    //充值说明
    public static String charge= "http://surine.cn/GreenEnergyFiles/charge_lis.html";
    //关于我们
    public static String about= "http://surine.cn/GreenEnergyFiles/about_lis.html";
    //开源协议
    public static String osl= "http://surine.cn/GreenEnergyFiles/open_lis.html";
    //一键投放
    public static String recyc_note= "http://surine.cn/GreenEnergyFiles/recycle_note.html";
    //APPID bmob后端云
    public static String APPID = "e37593594304a3497ebc49a5d82ec863";
    //短信模板
    public static String smsmodel = "celitea验证码";
    //注册接口
    public static String register_api = IP_port+"/register";
    //登录接口
    public static String login_api = IP_port+"/login";
    //更新接口
    public static String update_api = "http://surine.cn/GreenEnergyFiles/version_manager.php";
   //获取垃圾桶
    public static String get_can_api = IP_port+"/gettrashcan?";
    //扫码投放
    public static String add_score_api = IP_port+"/addscore";
    //获取全部公告
    public static String getallnotice = IP_port+"/getallnotice";
    //获取最后一条公告
    public static String getlastnotice = IP_port+"/getlastnotice";
}
