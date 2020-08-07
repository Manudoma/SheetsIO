/**
 * SheetCacheTest.java is part of the "SheeTXT" project (c) by Mark "Grandy" Bishop, 2020.
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package application;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Assert;
import org.junit.Test;

import application.models.CellData;

public class SheetCacheTest {
	private SheetCache testee = new SheetCache();

	private CellData a1 = dataFromRef("A1");
	private CellData b2 = dataFromRef("B2");
	private CellData c3 = dataFromRef("C3");
	private CellData d4 = dataFromRef("D4");
	private CellData ab5 = dataFromRef("AB5");
	private CellData cz55 = dataFromRef("CZ55");

	private List<CellData> testCells = Arrays.asList(a1, b2, c3, d4, ab5, cz55);

	@Test
	public void test_get() {
		testee.setup(testCells);
		// Ensure we get hits for the ones we expect
		Assert.assertEquals("", testee.get(a1));
		Assert.assertEquals("", testee.get(dataFromCoord(0, 0)));
		Assert.assertEquals("", testee.get(cz55));

		// Ensure misses for ones we don't want (e.g. A2)
		Assert.assertNull("Cache hit for unexpected key", testee.get(dataFromRef("A2")));
		Assert.assertNull("Cache hit for unexpected key", testee.get(dataFromCoord(0, 1)));
	}

	@Test
	public void test_update() {
		testee.setup(testCells);

		String a1Message = "0,0 (aka. A1) exists in config";
		String c3Message = "C3 exists in config";
		String cz55Message = "CZ55 exists in config";

		Map<CellData, String> rawCellData = new HashMap<>();
		rawCellData.put(dataFromCoord(0, 0), a1Message);
		rawCellData.put(dataFromRef("C3"), c3Message);
		rawCellData.put(dataFromRef("A2"), "A2 doesn't exist in config");
		rawCellData.put(dataFromRef("B3"), "B3 doesn't exist in config");
		rawCellData.put(dataFromRef("CZ55"), cz55Message);

		// Check initial state; cache hits but empty for those we're about to update
		Assert.assertEquals("", testee.get(a1));
		Assert.assertEquals("", testee.get(c3));
		Assert.assertEquals("", testee.get(cz55));

		testee.update(rawCellData);

		// Ensure raw data doesn't make its way in if wasn't already in
		Assert.assertNull("Cache should have missed", testee.get(dataFromRef("A2")));
		Assert.assertNull("Cache should have missed", testee.get(dataFromRef("B3")));

		// Ensure cache hits have new values, non updated values stay the same
		Assert.assertEquals(a1Message, testee.get(a1));
		Assert.assertEquals(c3Message, testee.get(c3));
		Assert.assertEquals(cz55Message, testee.get(cz55));
		Assert.assertEquals("", testee.get(b2));
		Assert.assertEquals("", testee.get(d4));
		Assert.assertEquals("", testee.get(ab5));
	}

	private CellData dataFromRef(String ref) {
		return new CellData(ref, ref + ".txt");
	}

	private CellData dataFromCoord(int col, int row) {
		return new CellData(col, row);
	}
}
