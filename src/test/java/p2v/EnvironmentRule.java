package p2v;

import org.eclipse.jetty.webapp.WebAppContext;
import org.hsqldb.Server;
import org.junit.rules.ExternalResource;
import p2v.helper.HibernateHsqlHelper;

public class EnvironmentRule extends ExternalResource {

    private org.eclipse.jetty.server.Server jetty;
    private int port;
    private String webInfDif;

    private Server hsqlServer;
    private boolean fillDB;
    
    public EnvironmentRule(boolean fillDB, int port, String webInfDif) {
        this.fillDB = fillDB;
        this.port = port;
        this.webInfDif = webInfDif;
    }

    @Override
    protected void before() throws Throwable {
        super.before();
        hsqlServer = HibernateHsqlHelper.startDB("mem:db/permisdevote");
        if (fillDB) {
            fillDB();
        }
        jetty = new org.eclipse.jetty.server.Server(port);
        jetty.setHandler(new WebAppContext(webInfDif, "/"));
        jetty.start();
    }

    private void fillDB() throws Exception {
        HibernateHsqlHelper.fillDB();
    }

    @Override
    protected void after() {
        super.after();
        try {
            jetty.stop();
            hsqlServer.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }
}
