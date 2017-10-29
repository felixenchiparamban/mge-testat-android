package ch.hsr.mge.gadgeothek;

import org.junit.Test;

import ch.hsr.mge.gadgeothek.util.ValidationUtil;

import static junit.framework.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

/**
 * Created by felix on 29/10/2017.
 */

public class ValidationUtilTest {
    @Test
    public void testIsPasswordValid() throws Exception {
        assertTrue(ValidationUtil.isPasswordValid("Hello"));
        assertFalse(ValidationUtil.isPasswordValid("Hell"));
    }


}
