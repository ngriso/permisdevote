package p2v;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.webapp.WebAppContext;
import org.junit.rules.ExternalResource;

public class JettyServerRule extends ExternalResource {

    private Server server;
    private int port;
    private String webInfDif;

    public JettyServerRule(int port, String webInfDif) {
        this.port = port;
        this.webInfDif = webInfDif;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        server = new Server(port);
        server.setHandler(new WebAppContext(webInfDif, "/"));
        server.start();
    }

    @Override
    protected void after() {
        super.after();
        try {
            server.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }
}
