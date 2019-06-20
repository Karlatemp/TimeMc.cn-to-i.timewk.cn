package cn.mcres.karlatemp.murlm;

import cn.mcres.karlatemp.murlm.json.CustomYggdrasilInfo;
import cn.mcres.karlatemp.murlm.json.JsonHelper;
import com.google.gson.Gson;
import com.google.gson.stream.JsonWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import static java.nio.charset.StandardCharsets.UTF_8;
import java.util.List;
import java.util.logging.Level;

public class HC extends HttpURLConnection {

    private HttpURLConnection uc;

    HC(URL u) {
        super(u);
    }
    byte[] data = null;
    int code = 0;

    @Override
    public InputStream getInputStream() throws IOException {
        if (data == null) {
            connect();
        }
        return new ByteArrayInputStream(data);
    }

    void setC(HttpURLConnection uc) {
        this.uc = uc;
    }

    @Override
    public int getResponseCode() throws IOException {
        if (data == null) {
            if (uc != null) {
                connect();
            }
        }
        return code; //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void disconnect() {
        if (uc != null) {
            uc.disconnect();
        }
    }

    @Override
    public boolean usingProxy() {
        return false;
    }

    @Override
    public void connect() throws IOException {
        if (data == null) {
            if (uc != null) {
                uc.connect();
                try {
                    InputStream is = uc.getInputStream();
                    ByteArrayOutputStream datas = new ByteArrayOutputStream();
                    byte[] buf = new byte[1024];
                    while (true) {
                        int lg = is.read(buf);
                        if (lg == -1) {
                            break;
                        }
                        datas.write(buf, 0, lg);
                    }
                    data = datas.toByteArray();
                    this.code = uc.getResponseCode();
                    try {
                        Gson g = JsonHelper.gson;
                        CustomYggdrasilInfo info = g.fromJson(new InputStreamReader(getInputStream(), UTF_8), CustomYggdrasilInfo.class);
                        List<String> dom = info.getSkinDomains();
                        dom.add("localhost");
                        dom.add("timemc.cn");
                        dom.add("timewk.cn");
                        dom.add("github.io");
                        datas.reset();
                        try (OutputStreamWriter wr = new OutputStreamWriter(datas, UTF_8)) {
                            JsonWriter jw = JsonHelper.inlineWriter(wr);
                            g.toJson(info, CustomYggdrasilInfo.class, jw);
                        }
                        data = datas.toByteArray();
                    } catch (Throwable thr) {
                        MXURLMapping.lg.log(Level.SEVERE, null, thr);
                    }
                    System.out.write(data);
                    System.out.println();
                } catch (IOException ie) {
                    throw ie;
                } finally {
                    uc.disconnect();
                }
            }
        }
    }

}
