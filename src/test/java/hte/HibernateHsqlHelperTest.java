package hte;

import static org.junit.Assert.fail;
import hte.helper.HibernateHsqlHelper;

import org.junit.Test;

public class HibernateHsqlHelperTest {
	
	@Test
	public void generate() {
		HibernateHsqlHelper.generate();
	}
	
	@Test
	public void hsqldb() {
		try {
			HibernateHsqlHelper.hsqldb();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			fail();
		}
	}

}
