package p2v.helper;

import org.apache.commons.lang3.ArrayUtils;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.hsqldb.Server;
import p2v.Fetcher;
import p2v.jpa.CandidacyJPA;
import p2v.jpa.CandidateJPA;
import p2v.jpa.PropositionJPA;
import p2v.jpa.QuestionJPA;
import p2v.jpa.ResponseJPA;
import p2v.jpa.StatsCandidacyJPA;
import p2v.jpa.StatsThemeJPA;
import p2v.jpa.TagJPA;
import p2v.jpa.UserStatsJPA;
import p2v.jpa.VoterJPA;

import java.io.PrintWriter;

public final class HibernateHsqlHelper {

    private HibernateHsqlHelper() {
    }

    public static void main(String[] args) throws Exception {
        if (ArrayUtils.isNotEmpty(args)) {
            String task = args[0];
            if ("schema".equals(task)) {
                generate();
            } else if ("startDB".equals(task)) {
                startMemoryDB();
            } else if ("startDBFile".equals(task)) {
                startPersistentDB();
            } else if ("fill".equals(task)) {
                fillDB();
            }
        }
    }

    public static void generate() {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(CandidateJPA.class)
                .addAnnotatedClass(TagJPA.class)
                .addAnnotatedClass(PropositionJPA.class)
                .addAnnotatedClass(VoterJPA.class)
                .addAnnotatedClass(CandidacyJPA.class)
                .addAnnotatedClass(QuestionJPA.class)
                .addAnnotatedClass(ResponseJPA.class)
                .addAnnotatedClass(StatsCandidacyJPA.class)
                .addAnnotatedClass(StatsThemeJPA.class)
                .addAnnotatedClass(UserStatsJPA.class)

                .setProperty(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");

        new SchemaExport(configuration)
                .setOutputFile("sql/schema.sql")
                .setDelimiter(";")
                .setFormat(true)
                .execute(Target.interpret(true, false), SchemaExport.Type.CREATE);
    }

    public static void startMemoryDB() throws Exception {
        startDB("mem:db/permisdevote");
        System.in.read();
    }

    public static void startPersistentDB() throws Exception {
        startDB("file:db/permisdevote");
        System.in.read();
    }

    public static Server startDB(String dbURL) {
        Server hsqlServer = new Server();
        hsqlServer.setLogWriter(new PrintWriter(System.out));
        hsqlServer.setSilent(true);
        hsqlServer.setDatabaseName(0, "");
        hsqlServer.setDatabasePath(0, dbURL);
        hsqlServer.start();
        return hsqlServer;
    }

    public static void fillDB() throws Exception {
        new Fetcher().runFetch();
    }
}
