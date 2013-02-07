package fr.afnic.commons.beans.list;

import junit.framework.TestCase;

import org.junit.Test;

import fr.afnic.commons.beans.operations.OperationView;

public class ResultListTest {

    private final Column c1 = new Column("Numéro de qualification", "id_qualification", true);
    private final Column c2 = new Column("Nom du Be", "legal_status");
    private final Column c3 = new Column("Date de création", "create_date");
    private final Column c4 = new Column("Source", "source", true);

    private ResultList createResult() {
        /*   ResultList result = new ResultList(OperationView.ValorizationInPendingFreeze);

           result.addColumn(this.c1);
           result.addColumn(this.c2);
           result.addColumn(this.c3);

           Line line = new fr.afnic.commons.beans.list.Line();
           line.addValue(this.c1, "value 1 ");
           line.addValue(this.c2, "value 2 ");
           result.addLine(line);*/

        return null;
    }

    @Test
    public void addOneColumnLineOnOneColumnResult() {

        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);
        result.addColumn(this.c1);

        Line line = new Line();
        String value = "value 1 ";
        line.addValue(this.c1, value);
        result.addLine(line);

        TestCase.assertNotNull(result.getColumns());
        TestCase.assertEquals(1, result.getColumns().size());

        TestCase.assertNotNull(result.getLines());
        TestCase.assertEquals(1, result.getLines().size());

        Line firstLine = result.getLines().get(0);
        TestCase.assertNotNull(firstLine.getValue(this.c1));
        TestCase.assertEquals(value, firstLine.getValue(this.c1));
    }

    @Test
    public void addColumnLineWithoutAddingColumn() {
        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);

        Line line = new Line();
        String value = "value 1";
        line.addValue(this.c1, value);

        try {
            result.addLine(line);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("Cannot add line with value for unknown column: " + this.c1.getReference() + ". Call addColumn() before addLine()", e.getMessage());
        }
    }

    @Test
    public void addColumnLineWithoutIdentifierColumn() {
        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);
        result.addColumn(this.c2);

        Line line = new Line();
        String value = "value 1";
        line.addValue(this.c2, value);

        try {
            result.addLine(line);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("Cannot add line without adding column set as identifier.", e.getMessage());
        }
    }

    @Test
    public void addColumnLineWithTwoIdentifierColumns() {
        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);
        result.addColumn(this.c1);

        try {
            result.addColumn(this.c4);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("Cannot add more than one identifier column. Already added " + this.c1.getReference(), e.getMessage());
        }
    }

    @Test
    public void addColumnTwice() {
        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);
        result.addColumn(this.c1);

        try {
            result.addColumn(this.c1);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("Column " + this.c1.getReference() + " is already added.", e.getMessage());
        }
    }

    @Test
    public void addColumnLineWithEmptyIdentifierColumn() {
        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);
        result.addColumn(this.c1);

        Line line = new Line();
        line.addValue(this.c1, "");

        try {
            result.addLine(line);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("Cannot not add line with empty value for identifier column " + this.c1.getReference() + ".", e.getMessage());
        }
    }

    @Test
    public void addColumnLineWithNullIdentifierColumn() {
        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);
        result.addColumn(this.c1);

        Line line = new Line();
        line.addValue(this.c1, null);

        try {
            result.addLine(line);
            TestCase.fail("No exception thrown");
        } catch (IllegalArgumentException e) {
            TestCase.assertEquals("Cannot not add line with null value for identifier column " + this.c1.getReference() + ".", e.getMessage());
        }
    }

    @Test
    public void addOneColumnLineOnTwoColumnResult() {

        QualificationResultList result = new QualificationResultList(OperationView.ValorizationInPendingFreeze);
        result.addColumn(this.c1);
        result.addColumn(this.c2);

        Line line = new Line();
        String value = "value 1 ";
        line.addValue(this.c1, value);
        result.addLine(line);

        TestCase.assertNotNull(result.getColumns());
        TestCase.assertEquals(2, result.getColumns().size());

        TestCase.assertNotNull(result.getLines());
        TestCase.assertEquals(1, result.getLines().size());

        Line firstLine = result.getLines().get(0);
        TestCase.assertNotNull(firstLine.getValue(this.c1));
        TestCase.assertEquals(value, firstLine.getValue(this.c1));
        TestCase.assertEquals("", firstLine.getValue(this.c2));

    }

}
