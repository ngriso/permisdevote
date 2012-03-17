package hte.helper;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.hsqldb.Server;
import org.hsqldb.cmdline.SqlTool;

import hte.jpa.CandidateJPA;
import hte.jpa.PropositionJPA;
import hte.jpa.TagJPA;

import java.io.PrintWriter;

public final class HibernateHsqlHelper {

    private HibernateHsqlHelper() {
	}

	public static void generate() {
        Configuration configuration = new Configuration()
                .addAnnotatedClass(CandidateJPA.class)
                .addAnnotatedClass(TagJPA.class)
                .addAnnotatedClass(PropositionJPA.class)
        
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
        hsqlServer.setDatabasePath(0, "mem:db/xskills");
        hsqlServer.start();

        String[] args = {
                "--driver=org.hsqldb.jdbcDriver",
                "--inlineRc=url=jdbc:hsqldb:hsql://localhost/,user=sa,password=",
                "sql/schema.sql"
        };
        SqlTool.objectMain(args);

//        
//        args = new String[]{
//                "--driver=org.hsqldb.jdbcDriver",
//                "--inlineRc=url=jdbc:hsqldb:hsql://localhost/,user=sa,password=",
//                "sql/data.sql"
//        };
//        SqlTool.objectMain(args);

        System.in.read();
    }
}
