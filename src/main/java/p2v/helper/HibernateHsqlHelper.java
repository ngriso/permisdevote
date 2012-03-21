package p2v.helper;

import org.apache.commons.lang3.ArrayUtils;
import p2v.Fetcher;
import p2v.jpa.CandidacyJPA;
import p2v.jpa.QuestionJPA;
import p2v.jpa.ResponseJPA;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.hsqldb.Server;
import org.hsqldb.cmdline.SqlTool;

import p2v.jpa.CandidateJPA;
import p2v.jpa.VoterJPA;
import p2v.jpa.PropositionJPA;
import p2v.jpa.StatsCandidacyJPA;
import p2v.jpa.StatsThemeJPA;
import p2v.jpa.TagJPA;
import p2v.jpa.UserStatsJPA;

import java.io.PrintWriter;

public final class HibernateHsqlHelper {

    private HibernateHsqlHelper() {
	}

    public static void main(String[] args) throws Exception {
        if (ArrayUtils.isNotEmpty(args)) {
            String task = args[0];
            if ("schema".equals(task)) {
                generate();
            } else if("startDB".equals(task)) {
                hsqldb();
            } else if ("startDBFile".equals(task)) {
                startDB();
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

    public static void hsqldb() throws Exception {
        Server hsqlServer = new Server();
        hsqlServer.setLogWriter(new PrintWriter(System.out));
        hsqlServer.setSilent(false);
        hsqlServer.setDatabaseName(0, "");
        hsqlServer.setDatabasePath(0, "mem:db/permisdevote");
        hsqlServer.start();

        String[] args = {
                "--driver=org.hsqldb.jdbcDriver",
                "--inlineRc=url=jdbc:hsqldb:hsql://localhost/,user=sa,password=",
                "sql/schema.sql"
        };
        SqlTool.objectMain(args);
        System.in.read();
    }
    
    public static void startDB() throws Exception {
        Server hsqlServer = new Server();
        hsqlServer.setLogWriter(new PrintWriter(System.out));
        hsqlServer.setSilent(false);
        hsqlServer.setDatabaseName(0, "");
        hsqlServer.setDatabasePath(0, "file:db/permisdevote");
        hsqlServer.start();

        String[] args = {
                "--driver=org.hsqldb.jdbcDriver",
                "--inlineRc=url=jdbc:hsqldb:hsql://localhost/,user=sa,password="};
        SqlTool.objectMain(args);
        System.in.read();
    }

    public static void fillDB() throws Exception {
        new Fetcher().runFetch();
    }
}
