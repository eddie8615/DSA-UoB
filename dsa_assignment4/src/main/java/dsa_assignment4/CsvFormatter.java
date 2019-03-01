package dsa_assignment4;

import java.io.PrintWriter;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.regex.Pattern;

import org.apache.log4j.Logger;

/**
 * A very simplified CSV formatter class. This only supports a very limited
 * version of the CSV file format where
 * <ul>
 * <li>There must be a column header row and column headers must be non-empty
 * strings that contain only the upper and lower case letters, digits,
 * underscore, space and hyphens</li>
 * <li>Quotes in field values have no special meanings</li>
 * <li>Rows and fields cannot be split over multiple lines or contain line
 * breaks</li>
 * <li>Fields in non-header rows cannot contain comma characters (',') or
 * vertical whitespace characters (newline, carriage return etc.)</li>
 * <li>There is no character escape mechanism supported</li>
 * <li>There is no support for any field separator other than comma (',')</li>
 * <li>All rows in a CSV file must have exactly the same number of fields as the
 * header (even if some are empty)</li>
 * </ul>
 * The following is an example CSV file that matches these requirements:
 * 
 * <pre>
 * ID,Name,Mark,Penalty
 * 1234567,John Smith,87,-3
 * 1234568,Erik Hasselhoff,43,+4
 * 1234569,Xiaowei Zhang,93.5,-2
 * </pre>
 * 
 */
public class CsvFormatter
{
	private static final Logger  logger    = Logger.getLogger(CsvFormatter.class);

	private Map<String, Integer> headers   = new HashMap<>();
	private String[]             headerStrings;
	private static Pattern       headerPat = Pattern.compile("\\A[-a-zA-Z0-9_ ]+\\z");
	private static Pattern       valuePat  = Pattern.compile("\\A[^,\\v]*\\z");

	/**
	 * Constructs a <code>CsvFormatter</code> object based on an array of column
	 * header strings
	 * 
	 * @param headings
	 *            the <code>String</code> array of column headers
	 * @throws Exception
	 *             if <code>headings</code> contain any duplicates or if any of
	 *             the column headers are null, empty or contain any characters
	 *             other than upper and lower case letters, digits, underscore
	 *             or space
	 */
	public CsvFormatter(String[] headings)
		throws Exception
	{
		setHeadings(headings);
		headerStrings = headings.clone(); // Need to clone this to isolate the class field from other classes
	}

	/**
	 * Constructor that reads the header row from <code>scanner</code>.
	 * 
	 * @param scanner
	 *            An open <code>Scanner</code> object from which the first line
	 *            is read and interpreted as the header row of a CSV file
	 * @throws Exception
	 *             if scanner is not an open <code>Scanner</code> object or from
	 *             which no further lines can be read or if it does not match
	 *             the requirements of a CSV header row
	 */
	public CsvFormatter(Scanner scanner)
		throws Exception
	{
		if (scanner == null)
			throw new Exception("Error: can't read from a null scanner!");
		String line;
		try
		{
			line = scanner.nextLine();
		}
		catch (NoSuchElementException e)
		{
			throw new Exception("No further lines in this file");
		}
		catch (IllegalStateException e)
		{
			throw new Exception("Error: scanner is not open!");
		}

		String[] headings = line.split(",", -1);
		setHeadings(headings);
		headerStrings = headings; // don't need to clone this as it is guaranteed to be isolated from other classes
	}

	/**
	 * A <strong>private</strong> helper function for the constructors that
	 * checks that the header strings matches the conditions required and sets
	 * the class fields accordingly
	 * 
	 * @param headings
	 *            the proposed array of column header strings
	 * @throws Exception
	 *             if the column headers do not match the requirements
	 */
	private void setHeadings(String[] headings)
		throws Exception
	{
		for (int i = 0; i < headings.length; i++)
		{
			if (headers.containsKey(headings[i]))
				throw new Exception("Header row contains duplicate names");
			if (headings[i] == null || headings[i].length() == 0)
				throw new Exception(String.format("Column header %d is null or empty", i));
			if (!headerPat.matcher(headings[i]).matches())
				throw new Exception(String.format("Column header %d contains invalid characters: \"%s\"", i, headings[i]));
			headers.put(headings[i], i);
		}
	}

	/**
	 * // * Return a copy of the header strings
	 * 
	 * @return a copy of the header strings, isolated from the internal class
	 *         fields
	 */
	public String[] getHeaderStrings()
	{
		return headerStrings.clone();
	}

	/**
	 * Get the column number of the column with a particular header
	 * 
	 * @param name
	 *            The column header required
	 * @return the column number of the column with that header, or null if that
	 *         name is not one of the column headers
	 */
	public Integer getColumnNumber(String name)
	{
		return headers.get(name);
	}

	/**
	 * Reads an return a CSV row from an open scanner
	 * 
	 * @param scanner
	 *            The open scanner to read from
	 * @return The array of strings which form the fields of the row
	 * @throws Exception
	 *             if scanner is not an open <code>Scanner</code> object or from
	 *             which no further lines can be read or if the row does not
	 *             have the same number of fields as the header
	 */
	public String[] readRow(Scanner scanner)
		throws Exception
	{
		if (scanner == null)
			throw new Exception("Error: can't read from a null scanner!");

		try
		{
			if (!scanner.hasNextLine())
				return null;
		}
		catch (IllegalStateException e)
		{
			throw new Exception("Error: scanner is not open!");
		}

		String line = scanner.nextLine();

		String[] row = line.split(",", -1);
		if (row.length != headerStrings.length)
			throw new Exception(String.format("Invalid row in CSV file: expected %d fields but got %d. Line: \"%s\"", headerStrings.length,
					row.length, line));

		return row;
	}

	/**
	 * Write to row to a CSV file
	 * 
	 * @param writer
	 *            The open <code>PrintWriter</code> through which to write the
	 *            row
	 * @param row
	 *            The row to write
	 * @throws Exception
	 *             if <code>writer</code> or <code>row</code> is null or the row
	 *             does not match the CSV requirements
	 * 
	 */
	public void writeRow(PrintWriter writer, String[] row)
		throws Exception
	{
		if (row == null)
			throw new Exception("Attempted to write a null row to CSV file!");
		if (row.length != headerStrings.length)
			throw new Exception(String.format("Attempted to write invalid row to CSV file: %d fields required but tried to write %d",
					headerStrings.length, row.length));
		for (int i = 0; i < row.length; i++)
		{
			if (!valuePat.matcher(row[i]).matches())
				throw new Exception(String.format("Field %d of row contains invalid characters: \"%s\"", i, row[i]));
		}
		if (writer == null)
			throw new Exception("Attempted to write to a null PrintWriter!");
		writer.println(String.join(",", row));
	}

	/**
	 * Write the header to a CSV file
	 * 
	 * @param writer
	 *            The open <code>PrintWriter</code> through which to write the
	 *            header
	 * @throws Exception
	 *             if <code>writer</code> is null
	 */
	public void writeHeader(PrintWriter writer)
		throws Exception
	{
		if (writer == null)
			throw new Exception("Attempted to write to a null PrintWriter!");
		writer.println(String.join(",", headerStrings));
	}

	/**
	 * This nested class is a comparator class whose objects can compare two CSV
	 * file rows from the same CSV format (i.e. has exactly the same column
	 * headers and number of columns), based on a given column.
	 *
	 */
	public class RowComparator implements Comparator<String[]>
	{
		/**
		 * The column number of the field that is used for the comparison
		 */
		private int comparisonField;

		/**
		 * The default constructor is private and should never be used
		 * 
		 * @throws Exception
		 *             if it ever gets called
		 */
		private RowComparator()
			throws Exception
		{
			throw new Exception("Error: the RowComparator default constructor should never be called!");
		}

		/**
		 * Constructs a comparator that can compare two rows based on the order
		 * of the strings in the column specified by the given column header
		 * 
		 * @param colName
		 *            the column header name of the column used for the
		 *            comparison
		 * @throws Exception
		 *             if this CSV file format does not have, as a header, the
		 *             given <code>colName</code> argument
		 */
		public RowComparator(String colName)
			throws Exception
		{
			Integer fieldNum = headers.get(colName);
			if (fieldNum == null)
				throw new Exception(String.format(
						"Error: the comparison column header name \"%s\" does not match any column header in this CSV file", colName));
			comparisonField = fieldNum;
		}

		@Override
		public int compare(String[] o1, String[] o2)
		{
			return o1[comparisonField].compareTo(o2[comparisonField]);
		}

	}
}
