package cf.ystbot.ystwebsite.DiscordConnection;

import com.mashape.unirest.http.JsonNode;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Loader {

    String clientSecret = "vsFi8psy-T94pK2sMslEFeAgoszgO0MC";
    String clientID = "916061178290126898";
    String redirectURL = "http://localhost:8080/dashboard";
    String scope = "identify%20email%20guilds";
    String basicURL = "https://discord.com/api";

    public String[] getUserInfo(String token){
        return new String[]{};
    }
    
    public JsonNode getToken(String accessToken) throws IOException, UnirestException {
        return Unirest.post(basicURL+"/oauth2/token")
                .header("Content-Type", "application/x-www-form-urlencoded")
                .field("scope", scope)
                .field("client_id", clientID)
                .field("grant_type", "authorization_code")
                .field("client_secret", clientSecret)
                .field("code", accessToken)
                .field("redirect_uri", redirectURL)
                .asJson()
                .getBody();
    }

    public List<List<String>> getGuilds(String token) throws UnirestException {
        List<List<String>> guilds = new ArrayList<>();
        JSONArray jsonArray = Unirest.get(basicURL+"/users/@me/guilds")
                .header("Authorization", "Bearer "+token)
                .asJson()
                .getBody()
                .getArray();
        jsonArray.forEach(obj -> {
            JSONObject jsonObject = new JSONObject(obj.toString());
            System.out.println(jsonObject);
            if(jsonObject.getBoolean("owner")){
                guilds.add(Arrays.asList(jsonObject.getString("name"),
                        (jsonObject.get("icon").toString() == null ? "logo.png" : "https://cdn.discordapp.com/icons/"+jsonObject.getString("id")+"/"+jsonObject.get("icon").toString())+".png",
                        jsonObject.getString("id")));
            }
        });

        return guilds;
    }
}
