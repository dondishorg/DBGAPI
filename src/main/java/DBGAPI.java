import net.dv8tion.jda.core.JDA;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class DBGAPI {
    private final String TOKEN;
    private static final OkHttpClient client = new OkHttpClient();
    private static final Logger log = LoggerFactory.getLogger(DBGAPI.class);
    private static final String API_URL = "https://discordbots.group/api";
    private final JDA jda;
    private final String botid;

    public DBGAPI(String token, String botid) {
        TOKEN = token;
        jda = null;
        this.botid = botid;
    }

    public DBGAPI(JDA jda) {
        this.jda = jda;
        TOKEN = jda.getToken();
        botid = jda.getSelfUser().getId();
        ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
        service.scheduleAtFixedRate(this::postServerCount, 0, 30, TimeUnit.MINUTES);
    }

    public static void postServerCount(long servercount, String TOKEN, String botid) {
        try {
            JSONObject json = new JSONObject().put("count", servercount);
            RequestBody body = RequestBody.create(MediaType.parse("application/json; charset=utf-8"), json.toString());
            Request request = new Request.Builder()
                    .post(body)
                    .url(API_URL + "/bot/"+botid)
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("authorization", TOKEN)
                    .build();
            Response response = client.newCall(request).execute();
            if (response.code()!=200)
                throw new IOException("ERROR " + response.code() + "\n" + response.message());
        } catch (JSONException | IOException e) {
            log.error("Error while posting to the api! " + e.getMessage());
        }
    }

    public void postServerCount(long servercount) {
        postServerCount(servercount, TOKEN, botid);
    }

    public static void postServerCount(JDA jda) {
        postServerCount(jda.getGuildCache().size(), jda.getToken(), jda.getSelfUser().getId());
    }

    public void postServerCount() {
        if (jda==null)
            throw new IllegalStateException("Cannot post without arguments when JDA is not defined!");
        postServerCount(jda);
    }

    public static JSONObject getBotInfo(String id) {
        try {
            Request request = new Request.Builder().url(API_URL + "/bot/" + id).get().build();
            Response response = client.newCall(request).execute();
            ResponseBody body = response.body();
            if (response.code() != 200 || body == null)
                throw new IOException("ERROR " + response.code() + "\n" + response.message());
            return new JSONObject(body.string());
        } catch (IOException e) {
            log.error("Error while getting info from the api! " + e.getMessage());
            return null;
        }
    }
}
