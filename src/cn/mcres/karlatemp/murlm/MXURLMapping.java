package cn.mcres.karlatemp.murlm;

import cn.mcres.gyhhy.MXLib.yggdrasil.CustomYggdrasil;
import cn.mcres.gyhhy.MXLib.yggdrasil.Yggdrasil;
import cn.mcres.gyhhy.MXLib.yggdrasil.beans.Profile;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.lang.instrument.Instrumentation;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles.Lookup;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.logging.ConsoleHandler;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

public class MXURLMapping {

    static final Lookup lk = Looker.openLookup(MXURLMapping.class, ~0);
    static final MethodHandle getHandlers, getURLStreamHandler;

    static {
        MethodHandle mh = null, m2 = null;
        try {
            mh = lk.unreflectGetter(URL.class.getDeclaredField("handlers"));
            m2 = lk.unreflect(URL.class.getDeclaredMethod("getURLStreamHandler", String.class));
        } catch (NoSuchFieldException | IllegalAccessException | NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(MXURLMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        getHandlers = mh;
        getURLStreamHandler = m2;
    }

    public static URLStreamHandler getURLStreamHandler(String s) {
        try {
            return (URLStreamHandler) getURLStreamHandler.invoke(s);
        } catch (Throwable ex) {
            Logger.getLogger(MXURLMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static Map<String, URLStreamHandler> getHandlers() {
        try {
            return (Map<String, URLStreamHandler>) getHandlers.invoke();
        } catch (Throwable ex) {
            Logger.getLogger(MXURLMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    public static URLStreamHandler getHandler(String scheme) {
        return getHandlers().get(scheme);
    }
    static PrintStream out = System.out;
    public static final Logger lg = Logger.getLogger("MXURLMapping");
    private static final Writer ew = new Writer() {
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
        }

        @Override
        public Writer append(CharSequence csq) throws IOException {
            return this;
        }

        @Override
        public void write(String str, int off, int len) throws IOException {
        }

        @Override
        public void write(int c) throws IOException {
        }

        @Override
        public void write(char[] cbuf) throws IOException {
        }

        @Override
        public void write(String str) throws IOException {
        }

        @Override
        public Writer append(CharSequence csq, int start, int end) throws IOException {
            return this;
        }

        @Override
        public Writer append(char c) throws IOException {
            return this;
        }

        @Override
        public void flush() throws IOException {
        }

        @Override
        public void close() throws IOException {
        }
    };

    static {
        lg.setUseParentHandlers(false);
        lg.addHandler(new ConsoleHandler() {
            @Override
            public void publish(LogRecord record) {
                if (this.isLoggable(record)) {
                    Throwable thr = record.getThrown();
                    record.setThrown(null);
                    String msg = this.getFormatter().formatMessage(record);
                    record.setThrown(thr);
                    String prefix = "[" + record.getLoggerName() + "] ";
                    out.println(prefix + msg);
                    if (thr != null) {
                        PrintWriter pw = new PrintWriter(ew) {
                            @Override
                            public void println(Object x) {
                                MXURLMapping.out.println(prefix + x);
                            }
                        };
                        thr.printStackTrace(pw);
                    }
                }
            }

            @Override
            public void flush() {
            }

            @Override
            public void close() throws SecurityException {
            }
        });
    }
    public static boolean ShowAll = Boolean.getBoolean("mum.sa");

    public static URLConnection openConnectionWW(URL u) throws IOException {
        String ff = u.getHost() + u.getPath();
        while (ff.endsWith("/")) {
            ff = ff.substring(0, ff.length() - 1);
        }
        lg.info("RR " + ff);
        switch (ff) {
            case "timemc.cn/api/yggdrasil":
            case "i.timewk.cn/api/yggdrasil": {
                lg.info("Starting mapping! ");
                return new HC(u);
            }
        }
        return null;
    }

    public static void bootstrap(String option) {
        out.println("Starting URL Mapper");
        getURLStreamHandler("https");
        getURLStreamHandler("http");
        BiConsumer<URL, NetWorkMapping> parser = (url, work) -> {
            String from = url.toExternalForm();
            if (url.getHost().equals("timemc.cn")) {
                work.setURL(url, null, "i.timewk.cn", 0, "i.timewk.cn", null, null, null, null);
            }
            String to = url.toExternalForm();
            if ((!ShowAll) && from.equals(to)) {
                return;
            }
            lg.log(Level.INFO, "Parse {0} to {1}", new Object[]{from, to});
        };
        URLStreamHandler https = getURLStreamHandler("https");
        getHandlers().put("https", new NetWorkMapping(https, parser)/* {

            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                URLConnection open = openConnectionWW(u);
                if (open != null) {
                    if (open instanceof HC) {
                        ((HC) open).setC((HttpURLConnection) super.openConnection(u));
                    }
                    return open;
                }
                return super.openConnection(u); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected URLConnection openConnection(URL u, Proxy p) throws IOException {
                URLConnection open = openConnectionWW(u);
                if (open != null) {
                    if (open instanceof HC) {
                        ((HC) open).setC((HttpURLConnection) super.openConnection(u));
                    }
                    return open;
                }
                return super.openConnection(u, p); //To change body of generated methods, choose Tools | Templates.
            }

        }*/);
        URLStreamHandler http = getURLStreamHandler("http");
        getHandlers().put("http", new NetWorkMapping(http, parser)/* {

            @Override
            protected URLConnection openConnection(URL u, Proxy p) throws IOException {
                URLConnection open = openConnectionWW(u);
                if (open != null) {
                    if (open instanceof HC) {
                        ((HC) open).setC((HttpURLConnection) super.openConnection(u));
                    }
                    return open;
                }
                return super.openConnection(u, p); //To change body of generated methods, choose Tools | Templates.
            }

            @Override
            protected URLConnection openConnection(URL u) throws IOException {
                URLConnection open = openConnectionWW(u);
                if (open != null) {
                    return open;
                }
                return super.openConnection(u); //To change body of generated methods, choose Tools | Templates.
            }

        }*/);
    }

    public static void agentmain(String option, Instrumentation is) {
        bootstrap(option);
    }

    public static void premain(String option, Instrumentation is) {
        bootstrap(option);
    }

    public static void main(String[] args) throws MalformedURLException {
        bootstrap("");
        System.out.println(getURLStreamHandler("https"));
        System.out.println(getURLStreamHandler("http"));
        Yggdrasil ygg = new CustomYggdrasil("https://timemc.cn");
        System.out.println(ygg);
        Profile p = ygg.queryProfiles("Karlatemp")[0];
        p = ygg.queryProfile(p.id.toString(), true);
        System.out.println(p.textures.skin.url);

        URL u = new URL("https://timemc.cn/api/yggdrasil/");
        String ff = u.getHost() + u.getPath();
        while (ff.endsWith("/")) {
            ff = ff.substring(0, ff.length() - 1);
        }
        System.out.println(ff);

    }

}
