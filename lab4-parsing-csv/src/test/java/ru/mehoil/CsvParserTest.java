package ru.mehoil;

import org.junit.jupiter.api.Test;
import ru.mehoil.parser.CsvParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

/**
 * {@link CsvParser} tests.
 *
 * @author Mikhail Dorokhov
 * @since 0.0.1
 */
public class CsvParserTest {

    private final CsvParser parser = new CsvParser();

    @Test
    public void testHappyPath() throws IOException {
        final var csvContent = """
                id;name;gender;birthdate;division;salary
                1;John;Male;01.01.1990;IT;5000
                2;Jane;Female;02.02.1995;HR;4500
                """;
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(2, result.persons().size(), "Should parse two valid persons");
        assertEquals(0, result.errors().size(), "No errors expected");

        final var person1 = result.persons().getFirst();
        assertEquals(1, person1.id());
        assertEquals("John", person1.name());
        assertEquals("Male", person1.gender());
        assertEquals(LocalDate.of(1990, 1, 1), person1.birthdate());
        assertEquals("IT", person1.division().name());
        assertEquals(1, person1.division().id());
        assertEquals(5000, person1.salary());

        final var person2 = result.persons().get(1);
        assertEquals(2, person2.id());
        assertEquals("Jane", person2.name());
        assertEquals("Female", person2.gender());
        assertEquals(LocalDate.of(1995, 2, 2), person2.birthdate());
        assertEquals("HR", person2.division().name());
        assertEquals(2, person2.division().id());
        assertEquals(4500, person2.salary());
    }

    @Test
    public void testInvalidLines() throws IOException {
        final var csvContent = """
                id;name;gender;birthdate;division;salary
                1;John;Male;01.01.1990;IT;5000
                2;Jane;Female;02.02.1995;HR
                3;Bob;Male;03.03.1980;Sales;6000;extra
                """;
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(1, result.persons().size(), "Only one valid person should be parsed");
        assertEquals(2, result.errors().size(), "Two lines should be invalid");

        assertTrue(result.errors().get(0).contains("Invalid line: 2;Jane;Female;02.02.1995;HR"));
        assertTrue(result.errors().get(1).contains("Invalid line: 3;Bob;Male;03.03.1980;Sales;6000;extra"));
    }

    @Test
    public void testParsingErrors() throws IOException {
        final var csvContent = """
                id;name;gender;birthdate;division;salary
                1;John;Male;01.01.1990;IT;5000
                abc;Jane;Female;02.02.1995;HR;4500
                3;Bob;Male;invalid_date;Sales;6000
                4;Alice;Female;04.04.1985;Finance;not_a_number
                """;
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(1, result.persons().size(), "Only one valid person should be parsed");
        assertEquals(3, result.errors().size(), "Three lines should have parsing errors");

        assertTrue(result.errors().get(0).contains("Error parsing line: abc;Jane;Female;02.02.1995;HR;4500"));
        assertTrue(result.errors().get(1).contains("Error parsing line: 3;Bob;Male;invalid_date;Sales;6000"));
        assertTrue(result.errors().get(2).contains("Error parsing line: 4;Alice;Female;04.04.1985;Finance;not_a_number"));
    }

    @Test
    public void testDivisionReuse() throws IOException {
        final var csvContent = """
                id;name;gender;birthdate;division;salary
                1;John;Male;01.01.1990;IT;5000
                2;Jane;Female;02.02.1995;IT;4500
                3;Bob;Male;03.03.1980;HR;6000
                """;
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(3, result.persons().size(), "Should parse three persons");
        assertEquals(0, result.errors().size(), "No errors expected");

        final var itDivision = result.persons().getFirst().division();
        assertEquals("IT", itDivision.name());
        assertEquals(1, itDivision.id());
        assertSame(itDivision, result.persons().get(1).division(), "IT division should be reused");

        final var hrDivision = result.persons().get(2).division();
        assertEquals("HR", hrDivision.name());
        assertEquals(2, hrDivision.id());
    }

    @Test
    public void testEmptyFile() throws IOException {
        final var csvContent = "id;name;gender;birthdate;division;salary\n";
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(0, result.persons().size(), "No persons should be parsed");
        assertEquals(0, result.errors().size(), "No errors expected");
    }

    @Test
    public void testOnlyInvalidLines() throws IOException {
        final var csvContent = """
                id;name;gender;birthdate;division;salary
                1;John;Male;01.01.1990;IT
                2;Jane;Female;02.02.1995;HR;4500;extra
                """;
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(0, result.persons().size(), "No valid persons should be parsed");
        assertEquals(2, result.errors().size(), "Two lines should be invalid");

        assertTrue(result.errors().get(0).contains("Invalid line: 1;John;Male;01.01.1990;IT"));
        assertTrue(result.errors().get(1).contains("Invalid line: 2;Jane;Female;02.02.1995;HR;4500;extra"));
    }

    @Test
    public void testDivisionNameTrimming() throws IOException {
        final var csvContent = """
                id;name;gender;birthdate;division;salary
                1;John;Male;01.01.1990; IT ;5000
                2;Jane;Female;02.02.1995;IT;4500
                """;
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(2, result.persons().size(), "Should parse two persons");
        assertEquals(0, result.errors().size(), "No errors expected");

        final var division1 = result.persons().get(0).division();
        assertEquals("IT", division1.name(), "Division name should be trimmed");
        assertSame(division1, result.persons().get(1).division(), "Trimmed division should be reused");
    }

    @Test
    public void testInvalidDate() throws IOException {
        final var csvContent = """
                id;name;gender;birthdate;division;salary
                1;John;Male;01-01-1990;IT;5000
                """;
        final var reader = new BufferedReader(new StringReader(csvContent));
        final var result = parser.parseCsv(reader);

        assertEquals(0, result.persons().size(), "No persons should be parsed with invalid date");
        assertEquals(1, result.errors().size(), "One parsing error expected");
        assertTrue(result.errors().getFirst().contains("Error parsing line: 1;John;Male;01-01-1990;IT;5000"));
    }
}
