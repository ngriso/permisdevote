package p2v.hte;

import p2v.helper.HibernateHsqlHelper;
import org.junit.Test;

public class HibernateHsqlHelperTest {

    @Test
    public void generate() {
        HibernateHsqlHelper.generate();
    }

    @Test
    public void hsqldb() throws Exception {
        HibernateHsqlHelper.hsqldb();
    }

    @Test
    public void fillDB() throws Exception {
        HibernateHsqlHelper.fillDB();
    }
    
    @Test
    public void startDB() throws Exception {
        HibernateHsqlHelper.startDB();
    }

}
