package cf.ystbot.ystwebsite;

import cf.ystbot.ystwebsite.DiscordConnection.Loader;
import com.mashape.unirest.http.JsonNode;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
public class YSTController {

    @RequestMapping("/dashboard")
    @ResponseBody
    public String main(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) throws Exception{
        if(request.getParameter("code") == null && session.getAttribute("identity") == null)
            response.sendRedirect("https://discord.com/api/oauth2/authorize?client_id=916061178290126898&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fdashboard&response_type=code&scope=identify%20email%20guilds");
        else{
            if(session.getAttribute("identity") != null){
                List<List<String>> servers = new Loader().getGuilds(session.getAttribute("identity").toString());
                String server = """
                        <div style="border: white; border-radius: 10%; border-style: solid; border-width: 1px; display: inline-block;">
                            <center style="position: relative;">
                            <img src="%s" width="50px" style="border-radius: 30%;">
                            <br/>
                            <br/>
                            <text class="font1" style="font-size: 20px; color: white;"></text>
                            <br/>
                            <button style="border: 2px solid yellow; border-radius: 5px; background-color: yellow;position: absolute;bottom: 0;" class="font2">설정하기</button>
                            </center>
                        </div>""";
                StringBuilder list = new StringBuilder("");
                for(List<String> sv : servers){
                    list.append("""
                            <div style="border: white; border-radius: 10%; border-style: solid; border-width: 1px; display: inline-block; width: 300px; height: 400px">
                                <center style="height: 399px;">
                                <img src=" """).append(sv.get(1)).append("""
                            " width="50px" style="border-radius: 30%;">
                            <br/>
                            <br/>
                            <text class="font1" style="font-size: 20px; color: white;">""").append(sv.get(0)).append("""
                                </text>
                                <br/>
                                <button style="border: 2px solid yellow; border-radius: 5px; background-color: yellow; position: absolute;bottom: 0; font-size: 15px;" class="font2">설정하기</button>
                                </center>
                            </div>""");
                }
                return """
                        <html>
                            <head>
                                <title>서버를 선택하세요 | YST Dashboard</title>
                            </head>
                            <style>
                                @import url('https://fonts.googleapis.com/css2?family=Gugi&display=swap');
                                @import url('https://fonts.googleapis.com/css2?family=Nanum+Gothic&display=swap');
                                                
                                .font1{
                                    font-family: 'Gugi', cursive;
                                }
                                .font2{
                                    font-family: 'Nanum Gothic', sans-serif;
                                }
                            </style>
                            <body style="background-color: #545454;">
                                <div>
                                    <img src="logo.png" width="80px"/>
                                    <text class="font1" style="font-size: 30px; color: white; vertical-align: 20px;">YST DashBoard</text>
                                </div>
                                <br/>
                                <br/>
                                <div id="servers">
                                    %s
                                </div>
                            </body>
                        </html>""".formatted(list);
            }else {
                try {
                    JsonNode jsonNode = new Loader().getToken(request.getParameter("code"));
                    session.setAttribute("identity", jsonNode.getObject().getString("access_token"));
                    response.sendRedirect("/dashboard");
                }catch (Exception e){
                    response.sendRedirect("https://discord.com/api/oauth2/authorize?client_id=916061178290126898&redirect_uri=http%3A%2F%2Flocalhost%3A8080%2Fdashboard&response_type=code&scope=identify%20email%20guilds");
                }
            }
        }
        return "";
    }
}
