package hte;

import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.Target;
import org.hsqldb.Server;
import org.hsqldb.cmdline.SqlTool;

import java.io.PrintWriter;

public class HibernateHsqlHelper {

    public void generate() {
        Configuration configuration = new Configuration()
//                .addAnnotatedClass(Category.class)
                .setProperty(Environment.DIALECT, "org.hibernate.dialect.HSQLDialect");

        new SchemaExport(configuration)
                .setOutputFile("sql/schema.sql")
                .setDelimiter(";")
                .setFormat(true)
                .execute(Target.interpret(true, false), SchemaExport.Type.CREATE);
    }

    public void hsqldb() throws Exception {
        Server hsqlServer = new Server();
        hsqlServer.setLogWriter(new PrintWriter(System.out));
        hsqlServer.setSilent(false);
        hsqlServer.setDatabaseName(0, "");
        hsqlServer.setDatabasePath(0, "mem:db/xskills");
        hsqlServer.start();

        String[] args = {
                "--driver=org.hsqldb.jdbcDriver",
                "--inlineRc=url=jdbc:hsqldb:hsql://localhost/,user=sa,password=",
                "webapp/sql/schema.sql"
        };
        SqlTool.objectMain(args);

        args = new String[]{
                "--driver=org.hsqldb.jdbcDriver",
                "--inlineRc=url=jdbc:hsqldb:hsql://localhost/,user=sa,password=",
                "webapp/sql/data.sql"
        };
        SqlTool.objectMain(args);

        System.in.read();
    }
}
