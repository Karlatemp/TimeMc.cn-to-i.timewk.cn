package cn.mcres.karlatemp.murlm;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.net.InetAddress;
import java.net.Proxy;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLStreamHandler;
import static cn.mcres.karlatemp.murlm.MXURLMapping.lk;
import java.lang.invoke.MethodType;
import java.net.MalformedURLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.function.BiConsumer;

public class NetWorkMapping extends URLStreamHandler {

    private final URLStreamHandler p;
    private final BiConsumer<URL, NetWorkMapping> parser;

    public NetWorkMapping(URLStreamHandler ush) {
        this(ush, null);
    }

    public NetWorkMapping(URLStreamHandler ush, BiConsumer<URL, NetWorkMapping> parser) {
        this.p = ush;
        this.parser = parser;
    }
    private static MethodHandle hostsEqual,
            toExternalForm,
            setURL,
            sameFile,
            parseURL,
            getHostAddress,
            getDefaultPort,
            equals,
            openConnection,
            openConnection_p;

    static {
        final Class<URLStreamHandler> sh = URLStreamHandler.class;
        try {
            hostsEqual = lk.findVirtual(sh, "hostsEqual", MethodType.methodType(boolean.class, URL.class, URL.class));
            toExternalForm = lk.findVirtual(sh, "toExternalForm", MethodType.methodType(String.class, URL.class));
//            setURL(URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query, String ref) {
            setURL = lk.findVirtual(sh, "setURL", MethodType.methodType(void.class, URL.class, String.class, String.class, int.class, String.class, String.class, String.class, String.class, String.class));
            sameFile = lk.findVirtual(sh, "sameFile", MethodType.methodType(boolean.class, URL.class, URL.class));
            parseURL = lk.findVirtual(sh, "parseURL", MethodType.methodType(void.class, URL.class, String.class, int.class, int.class));
            getHostAddress = lk.findVirtual(sh, "getHostAddress", MethodType.methodType(InetAddress.class, URL.class));
            getDefaultPort = lk.findVirtual(sh, "getDefaultPort", MethodType.methodType(int.class));
            equals = lk.findVirtual(sh, "equals", MethodType.methodType(boolean.class, URL.class, URL.class));
            openConnection = lk.findVirtual(sh, "openConnection", MethodType.methodType(URLConnection.class, URL.class));
            openConnection_p = lk.findVirtual(sh, "openConnection", MethodType.methodType(URLConnection.class, URL.class, Proxy.class));
        } catch (NoSuchMethodException ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public static void main(String[] args) {

    }

    @Override
    protected void parseURL(URL u, String spec, int start, int limit) {
        super.parseURL(u, spec, start, limit);
        if (parser != null) {
            parser.accept(u, this);
        }
    }

    @Override
    protected boolean hostsEqual(URL u1, URL u2) {
        try {
            return (boolean) hostsEqual.invoke(p, u1, u2);
        } catch (Throwable ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return false;
    }

    @Override
    protected String toExternalForm(URL u) {
        return super.toExternalForm(u); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean sameFile(URL u1, URL u2) {
        try {
            return (boolean) sameFile.invoke(p, u1, u2);
        } catch (Throwable ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.sameFile(u1, u2); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected synchronized InetAddress getHostAddress(URL u) {
        try {
            return (InetAddress) getHostAddress.invoke(p, u);
        } catch (Throwable ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.getHostAddress(u); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected int getDefaultPort() {
        try {
            return (int) getDefaultPort.invoke(p);
        } catch (Throwable ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.getDefaultPort(); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected boolean equals(URL u1, URL u2) {
        try {
            return (boolean) equals.invoke(p, u1, u2);
        } catch (Throwable ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.equals(u1, u2); //To change body of generated methods, choose Tools | Templates.
    }

    protected URL parseURL(URL u) {
        if (parser != null) {
            parser.accept(u, this);
            return u;
        }
        return u;
    }

    public URL copy(URL u) {
        try {
            URL ux = new URL(u, null, this);
            setURL(ux, u.getProtocol(), u.getHost(), u.getPort(), u.getAuthority(), u.getUserInfo(), u.getPath(), u.getQuery(), u.getRef());
            return ux;
        } catch (MalformedURLException ex) {
            return u;
        }
    }

    @Override
    public void setURL(URL u, String protocol, String host, int port, String authority, String userInfo, String path, String query, String ref) {
        if (protocol == null) {
            protocol = u.getProtocol();
        }
        if (host == null) {
            host = u.getHost();
        }
        if (port < 1) {
            port = u.getPort();
        }
        if (authority == null) {
            authority = u.getAuthority();
        }
        if (userInfo == null) {
            userInfo = u.getUserInfo();
        }
        if (path == null) {
            path = u.getPath();
        }
        if (query == null) {
            query = u.getQuery();
        }
        if (ref == null) {
            ref = u.getRef();
        }
        super.setURL(u, protocol, host, port, authority, userInfo, path, query, ref); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected URLConnection openConnection(URL u, Proxy p) throws IOException {
        try {
            return (URLConnection) openConnection_p.invoke(this.p, parseURL(u), p);
        } catch (Throwable ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        return super.openConnection(u, p); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    protected URLConnection openConnection(URL u) throws IOException {
        try {
            return (URLConnection) openConnection.invoke(this.p, parseURL(u));
        } catch (Throwable ex) {
            Logger.getLogger(NetWorkMapping.class.getName()).log(Level.SEVERE, null, ex);
        }
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
