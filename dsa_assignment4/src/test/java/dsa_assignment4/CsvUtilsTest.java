package dsa_assignment4;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;

public class CsvUtilsTest
{
	private static final Logger logger        = Logger.getLogger(CsvUtilsTest.class);
	private static final Path   dataDir       = CsvUtils.getDataDir();

	@Rule
	public Timeout              globalTimeout = new Timeout(2000, TimeUnit.MILLISECONDS);

	@Test
	public void testCheckStudentIdentification()
	{
		assertNotEquals("Please update the studentID field in CsvUtils.java with your student id number", //
				"MY STUDENT ID", CsvUtils.getStudentID());
		assertNotEquals("Please update the studentName field in CsvUtils.java with your name", //
				"MY NAME", CsvUtils.getStudentName());
	}

	@Test
	public void testCsvCopy()
		throws Exception
	{

		Path fromPath = dataDir.resolve("state-abbrevs.csv");
		Path toPath = dataDir.resolve("temp_copy.csv");

		boolean ret = CsvUtils.copyCsv(fromPath, toPath);

		assertTrue("CsvUtils.copyCsv call failed", ret);

		try (Scanner from = new Scanner(fromPath); Scanner to = new Scanner(toPath))
		{
			CsvFormatter formatter1 = new CsvFormatter(from);
			CsvFormatter formatter2 = new CsvFormatter(to);
			assertArrayEquals("The header rows must be equal", formatter1.getHeaderStrings(), formatter2.getHeaderStrings());
			String[] row1;
			String[] row2;
			for (;;)
			{
				row1 = formatter1.readRow(from);
				row2 = formatter2.readRow(to);
				if (row1 == null)
				{
					assertNull("The CSV file copy must not have more rows than the original", row2);
					break;
				}
				else if (row2 == null)
				{
					assertNull("The CSV file copy must not have less rows than the original", row1);
					break;
				}
				else
					assertArrayEquals("The original and the copy rows must be equal", formatter1.getHeaderStrings(),
							formatter2.getHeaderStrings());
			}
		}
	}

	@Test(expected = Exception.class)
	public void testBadCsvCopy()
		throws Exception
	{

		Path fromPath = dataDir.resolve("bad.csv");
		Path toPath = dataDir.resolve("temp_bad_copy.csv");

		boolean ret = CsvUtils.copyCsv(fromPath, toPath);

		assertTrue("CsvUtils.copyCsv call failed", ret);

		try (Scanner from = new Scanner(fromPath); Scanner to = new Scanner(toPath))
		{
			CsvFormatter formatter1 = new CsvFormatter(from);
			CsvFormatter formatter2 = new CsvFormatter(to);
			assertArrayEquals("The header rows must be equal", formatter1.getHeaderStrings(), formatter2.getHeaderStrings());
			String[] row1;
			String[] row2;
			for (;;)
			{
				row1 = formatter1.readRow(from);
				row2 = formatter2.readRow(to);
				if (row1 == null)
				{
					assertNull("The CSV file copy must not have more rows than the original", row2);
					break;
				}
				else if (row2 == null)
				{
					assertNull("The CSV file copy must not have less rows than the original", row1);
					break;
				}
				else
					assertArrayEquals("The original and the copy rows must be equal", formatter1.getHeaderStrings(),
							formatter2.getHeaderStrings());
			}
		}
	}

	@Test
	public void testSplitSortCsv()
		throws Exception
	{
		Path combinedPath = dataDir.resolve("state-abbrevs.csv");
		int numRowLimit = 9;
		String columnHeader = "abbreviation";

		Path[] splitPaths = CsvUtils.splitSortCsv(combinedPath, columnHeader, numRowLimit);

		assertNotNull("CsvUtils.splitSortCsv should not return a null array of Paths", splitPaths);

		// We do want to check that the split files have no more than numRowLimit non-header rows, so numRowLimit > 0
		// The combined file is not normally ordered so don't check it
		boolean checkCombinedOrder = false;
		boolean checkSplitOrder = true;
		checkCombinedSplit(combinedPath, splitPaths, columnHeader, numRowLimit, checkCombinedOrder, checkSplitOrder);
	}

	@Test
	public void mergePairCsvTest()
		throws Exception
	{
		Path split1Path = dataDir.resolve("split_0_state-abbrevs.csv");
		Path split2Path = dataDir.resolve("split_1_state-abbrevs.csv");
		Path combinedPath = dataDir.resolve("temp_merged_0_1_state-abbrevs.csv");
		String columnHeader = "abbreviation";

		boolean ret = CsvUtils.mergePairCsv(split1Path, split2Path, columnHeader, combinedPath);
		assertTrue("CsvUtils.mergePairCsv call failed", ret);

		Path[] splitPaths = { split1Path, split2Path };

		// Since we are merging, we don't care how many rows the split files have, so numRowLimit = 0, meaning it won't be checked
		// The combined file should be ordered so check it
		// This can only work if the split files are ordered, so check that too - even though it is not an error in mergePairCsv
		int numRowLimit = 0;
		boolean checkCombinedOrder = true;
		boolean checkSplitOrder = true;
		checkCombinedSplit(combinedPath, splitPaths, columnHeader, numRowLimit, checkCombinedOrder, checkSplitOrder);
	}

	@Test
	public void mergeListCsvTest()
		throws Exception
	{
		Path[] splitPaths = { //
				dataDir.resolve("split_0_state-abbrevs.csv"), //
				dataDir.resolve("split_1_state-abbrevs.csv"), //
				dataDir.resolve("split_2_state-abbrevs.csv"), //
				dataDir.resolve("split_3_state-abbrevs.csv"), //
				dataDir.resolve("split_4_state-abbrevs.csv"), //
				dataDir.resolve("split_5_state-abbrevs.csv") };

		Path combinedPath = dataDir.resolve("temp_merged_list_state-abbrevs.csv");
		String columnHeader = "abbreviation";

		boolean ret = CsvUtils.mergeListCsv(splitPaths, columnHeader, combinedPath);
		assertTrue("CsvUtils.mergeListCsv call failed", ret);

		// Since we are merging, we don't care how many rows the split files have, so numRowLimit = 0 meaning it won't be checked
		// The combined file should be ordered so check it
		// This can only work if the split files are ordered, so check that too - even though it is not an error in mergeListCsv
		int numRowLimit = 0;
		boolean checkCombinedOrder = true;
		boolean checkSplitOrder = true;
		checkCombinedSplit(combinedPath, splitPaths, columnHeader, numRowLimit, checkCombinedOrder, checkSplitOrder);
	}

	@Test
	public void testFullMergeSortCsv()
		throws Exception
	{
		// Here we split the original unordered file into ordered split files, then merge it back into an ordered result file
		// When we run checkCombinedSplit we need to use one as the combined file and the other as the single split file. It doesn't
		// matter which is which so long as we only check for order on the result file and not on the original file

		Path combinedPath = dataDir.resolve("state-abbrevs.csv");
		Path destPath = dataDir.resolve("temp_merged_state-abbrevs.csv");
		Path[] destPaths = { destPath };
		int numRowLimit = 9;
		String columnHeader = "abbreviation";

		Path[] paths = CsvUtils.splitSortCsv(combinedPath, columnHeader, numRowLimit);
		assertNotNull("CsvUtils.splitSortCsv should not return a null array of Paths", paths);

		// Check that the intermediate splits are fine
		boolean checkCombinedOrder = false;
		boolean checkSplitOrder = true;
		checkCombinedSplit(combinedPath, paths, columnHeader, numRowLimit, checkCombinedOrder, checkSplitOrder);

		boolean ret = CsvUtils.mergeListCsv(paths, columnHeader, destPath);
		assertTrue("CsvUtils.mergeListCsv call failed", ret);

		numRowLimit = 0; // we only needed the row limit for the intermediate split file testing
		checkCombinedOrder = false; // don't check order on the original unsorted file
		checkSplitOrder = true;
		checkCombinedSplit(combinedPath, destPaths, columnHeader, numRowLimit, checkCombinedOrder, checkSplitOrder);
	}

	/**
	 * Most of the CsvUtils methods involve either splitting or merging CSV
	 * files into CSV files. This method is a general test helper method that
	 * validates that the split set matches the merged set in appropriate ways
	 * and hence can be used by most the test methods. The validations include
	 * that
	 * <ul>
	 * <li>the CSV format matches</li>
	 * <li>the rows of the merged file matches the rows of the split
	 * file(s)</li>
	 * <li>(optionally) the rows of the merged file are in order</li>
	 * <li>(optionally) the rows of the split file(s) are in order</li>
	 * <li>(optionally) none of the split file(s) have more than the
	 * <code>numRowLimit</code> number of rows</li>
	 * </ul>
	 * 
	 * @param combinedPath
	 *            the relative path of the combined (merged) file
	 * @param splitPaths
	 *            the array of relative paths of the split file(s)
	 * @param columnHeader
	 *            the column that appropriate file(s) should be ordered on (if
	 *            enabled)
	 * @param numRowLimit
	 *            if greater than 0, the maximum number of value rows (excluding
	 *            the header row) that the split files should have. If 0, then
	 *            the lengths of the split files will not be checked
	 * @param checkCombinedOrder
	 *            If true, check that the combined file is in order by the
	 *            column with header name <code>columnHeader</code>
	 * @param checkSplitOrder
	 *            If true, check that the split file(s) is(are) in order by the
	 *            column with header name <code>columnHeader</code>
	 * @throws Exception
	 *             if CSV formats are wrong or incompatible, or I/O errors occur
	 */
	private void checkCombinedSplit(Path combinedPath, Path[] splitPaths, String columnHeader, int numRowLimit, boolean checkCombinedOrder,
			boolean checkSplitOrder)
		throws Exception
	{
		// First get the combined header and all the combined rows
		int columnNumber = -1;
		String[] combinedHeader;
		ArrayList<String[]> combinedRows = new ArrayList<>();
		try (Scanner combined = new Scanner(combinedPath))
		{
			CsvFormatter formatter = new CsvFormatter(combined);
			columnNumber = formatter.getColumnNumber(columnHeader);
			combinedHeader = formatter.getHeaderStrings();
			String[] row;
			String lastColVal = "";
			while ((row = formatter.readRow(combined)) != null)
			{
				if (checkCombinedOrder)
				{
					assertTrue("The combined file rows are not in order", lastColVal.compareTo(row[columnNumber]) <= 0);
					lastColVal = row[columnNumber];
				}
				combinedRows.add(row);
			}
		}

		// For each path in splitPaths, check that 
		// 1. the header from each split file matches the combined header
		// 2. that all the rows in each file are in the combined file
		// 3. that the rows are in ascending order by the values in required column
		// 4. there are no rows in any split file that is not in the combined file
		for (Path path : splitPaths)
		{
			int splitRowCount = 0;
			try (Scanner split = new Scanner(path))
			{
				CsvFormatter formatter = new CsvFormatter(split);
				//assert that the headers match
				assertArrayEquals("Header of split file does not match the header of the combined file: ", combinedHeader,
						formatter.getHeaderStrings());
				String[] row;
				String lastColVal = "";
				// for each row in the split file
				while ((row = formatter.readRow(split)) != null)
				{
					splitRowCount++;
					// find the location of that row in the list of source file rows
					int index = 0;
					while (index < combinedRows.size() && !Arrays.equals(row, combinedRows.get(index)))
						index++;
					// assert that we found the row in the source file rows
					assertTrue(String.format("Could not find row \"%s\" from one of the split files in the combined file",
							Arrays.toString(row)), index < combinedRows.size());
					// remove it from the list of source file rows
					combinedRows.remove(index);

					if (checkSplitOrder)
					{
						assertTrue("One of the split files is not in order", lastColVal.compareTo(row[columnNumber]) <= 0);
						lastColVal = row[columnNumber];
					}
				}
				// Only check that the count of rows in the split file is less than the numRowLimit if we are supposed to
				// We are supposed when testing that for splitting a file, not when we are merging files
				if (numRowLimit > 0)
					assertTrue("The number of rows in a split file was greater than the row limit", splitRowCount <= numRowLimit);
			}
		}
		// assert that we found all the files in the source file rows
		assertTrue("Some rows in the combined file were not found in any of the split files", combinedRows.isEmpty());
	}

}
