import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class GardenTest {
	
	private Garden g1 = new Garden(15, 20);
	private Garden g2 = new Garden();
	private PlantSpecies testSpecies = new PlantSpecies("a", "b", "c", 0, 0, 0, true, 0, 0, 0);
	private PlacedPlant testPlant = new PlacedPlant(0, 0, testSpecies);
	private Conditions c = new Conditions(SoilType.ANY, MoistureType.ANY, LightType.ANY);
	
	
	@BeforeEach
	void beforeEach() {
		g1 = new Garden(15, 20);
		g2 = new Garden();
		testSpecies = new PlantSpecies("a", "b", "c", 0, 0, 0, true, 0, 0, 0);
		testPlant = new PlacedPlant(0, 0, testSpecies);
		c = new Conditions(SoilType.ANY, MoistureType.ANY, LightType.ANY);
	}
	
	@Test
	void testGetNumLeps() {
		assertEquals(15, g1.getNumLeps());
		assertEquals(0, g2.getNumLeps());
	}

	@Test
	void testSetNumLeps() {
		g1.setNumLeps(200);
		assertEquals(200, g1.getNumLeps());
		g2.setNumLeps(15);
		assertEquals(15, g2.getNumLeps());
	}
	
	@Test
	void testAddNumLeps() {
		g1.addNumLeps(15);
		assertEquals(30, g1.getNumLeps());
	}

	@Test
	void testGetCost() {
		assertEquals(20, g1.getCost());
	}

	@Test
	void testSetCost() {
		g1.setCost(100);
		assertEquals(100, g1.getCost());
		g2.setCost(30);
		assertEquals(30, g2.getCost());
	}
	
	@Test
	void testAddCost() {
		g1.addCost(250);
		assertEquals(270, g1.getCost());
	}

	@Test
	void testGetOutline() {
		assertTrue(g1.getOutline().isEmpty());
	}

	@Test
	void testGetPlants() {
		assertEquals(0, g1.getPlants().size());
	}

	@Test
	void testGetSections() {
		assertEquals(g1.getSections().size(), 0);
	}

	@Test
	void testGetLeps() {
		assertEquals(0, g1.getLeps().size());
	}

	@Test
	void testGetCompostBin() {
		assertTrue(g1.getCompostBin().isEmpty());
		assertTrue(g2.getCompostBin().isEmpty());
	}
	@Test
	void testUpdateOutline() {
		g1.updateOutline(0, 0);
		assertEquals(new Vector2(0,0), g1.outline.get(0));
		g2.updateOutline(5, 5);
		g2.updateOutline(5, 10);
		assertEquals(new Vector2(5, 10), g2.outline.get(1));
		
	}
	@Test
	void testClearBoundaries() {
		g1.clearOutline();
		assertEquals(0, g1.getOutline().size());
		assertEquals(0, g1.getPolygonCorners().size());
	}
	
	@Test
	void testGetExtremes() {
		g1.updateOutline(3, 3);
		g1.updateOutline(5, 2);
		g1.updateOutline(1, 9);
		g1.updateOutline(2, 1);
		g1.setPolygonCorners(1, 0);
		g1.setPolygonCorners(4, 1);
		g1.setPolygonCorners(0, 4);
		g1.setPolygonCorners(4, 4);
		ArrayList<Vector2> extrema = g1.getExtremes();
		assertEquals(new Vector2(1,0), extrema.get(0));
		assertEquals(new Vector2(5,2), extrema.get(1));
		assertEquals(new Vector2(1,9), extrema.get(2));
		assertEquals(new Vector2(0,4), extrema.get(3));
	}
	
	@Test
	void testAddSection() {
		g1.addSection(c);
		ArrayList<Conditions> temp = new ArrayList<>();
		temp.add(c);
		assertEquals(g1.getSections(), temp);
	}
	
	@Test
	void addToGarden() {
		g1.addToGarden(testPlant);
		assertFalse(g1.getPlants().isEmpty());
	}
	
	@Test
	void testSetGardenImageInfo() {
		g1.setGardenImageInfo(0, 0, null);
		assertNull(g1.getGardenData());
		assertEquals(0, g1.getWidth());
		assertEquals(0, g1.getHeight());
	}
	
	@Test
	void testSetPlotImageInfo() {
		g1.setPlotImageInfo(0, 0, null);
		assertNull(g1.plotData);
		assertEquals(0, g1.plotWidth);
		assertEquals(0, g1.plotHeight);
	}
	
}
