package hte;

import hte.helper.HibernateHsqlHelper;
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

}
