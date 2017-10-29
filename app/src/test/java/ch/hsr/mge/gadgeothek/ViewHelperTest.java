package ch.hsr.mge.gadgeothek;

import org.junit.Test;

import ch.hsr.mge.gadgeothek.helper.ViewHelper;
import ch.hsr.mge.gadgeothek.helper.ViewTimeUnit;

import static junit.framework.Assert.assertEquals;

/**
 * Created by felix on 29/10/2017.
 */

public class ViewHelperTest {

    @Test
    public void testGetApproximateTimeUnit() {
        assertEquals(ViewTimeUnit.DAYS, ViewHelper.getAppropriateTimeUnit(90));
        assertEquals(ViewTimeUnit.MONTHS, ViewHelper.getAppropriateTimeUnit(100));
        assertEquals(ViewTimeUnit.MONTHS, ViewHelper.getAppropriateTimeUnit(350));
        assertEquals(ViewTimeUnit.MONTHS, ViewHelper.getAppropriateTimeUnit(364));
        assertEquals(ViewTimeUnit.YEARS, ViewHelper.getAppropriateTimeUnit(365));
    }

    @Test
    public void testGetApproximate() {
        assertEquals(100, ViewHelper.getApproximate(100, ViewTimeUnit.DAYS));
        assertEquals(3, ViewHelper.getApproximate(100, ViewTimeUnit.MONTHS));
        assertEquals(11, ViewHelper.getApproximate(350, ViewTimeUnit.MONTHS));
        assertEquals(12, ViewHelper.getApproximate(365, ViewTimeUnit.MONTHS));
        assertEquals(0, ViewHelper.getApproximate(364, ViewTimeUnit.YEARS));
        assertEquals(1, ViewHelper.getApproximate(365, ViewTimeUnit.YEARS));
    }
}
