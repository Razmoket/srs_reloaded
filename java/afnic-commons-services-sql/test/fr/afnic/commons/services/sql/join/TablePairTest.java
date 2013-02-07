/*
 * $Id: $ $Revision: $ $Author: $
 */

package fr.afnic.commons.services.sql.join;

import junit.framework.Assert;

import org.junit.Test;

import fr.afnic.commons.services.sql.query.Nicope;

/**
 * Teste le fonctionnement de TablePair.
 * 
 * @author ginguene
 * 
 */
public class TablePairTest {

    @Test
    public void testOwnEquals() {

        final TablePair pair = new TablePair(Nicope.NOMDEDOMAINE_TABLE, Nicope.ADHERENT_TABLE);

        Assert.assertEquals(pair, pair);

    }

    @Test
    public void testEqualsWithInversePaire() {

        final TablePair pair1 = new TablePair(Nicope.NOMDEDOMAINE_TABLE, Nicope.ADHERENT_TABLE);

        final TablePair pair2 = new TablePair(Nicope.ADHERENT_TABLE, Nicope.NOMDEDOMAINE_TABLE);

        Assert.assertEquals(pair1, pair2);

    }

    @Test
    public void testEqualsWithNotEqualsPairs() {

        final TablePair pair1 = new TablePair(Nicope.NOMDEDOMAINE_TABLE, Nicope.ADHERENT_TABLE);

        final TablePair pair2 = new TablePair(Nicope.ADHERENT_TABLE, Nicope.TICKETDESCETAT_TABLE);

        Assert.assertFalse(pair1.equals(pair2));

    }

    @Test
    public void testHashCodeWithInversePaire() {

        final TablePair pair1 = new TablePair(Nicope.NOMDEDOMAINE_TABLE, Nicope.ADHERENT_TABLE);

        final TablePair pair2 = new TablePair(Nicope.ADHERENT_TABLE, Nicope.NOMDEDOMAINE_TABLE);

        Assert.assertEquals(pair1.hashCode(), pair2.hashCode());

    }
}
