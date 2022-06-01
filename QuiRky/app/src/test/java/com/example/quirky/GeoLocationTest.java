package com.example.quirky;

import org.junit.*;

import static org.junit.Assert.*;

import com.example.quirky.models.GeoLocation;

public class GeoLocationTest {
    private GeoLocation geo;
    private static final double grace = 0.0000001; // Grace room for double calculations

    // Test that the constructors work
    @Test
    public void Constructors() {

        // Empty Constructor tests
        geo = new GeoLocation();
        assertEquals(0, geo.getExactLat(), grace);
        assertEquals(0, geo.getExactLong(), grace);

        assertEquals(0, geo.getApproxLat());
        assertEquals(0, geo.getApproxLong());

        assertEquals("", geo.getDescription());


        // Given location constructor tests
        double Lat = 10.003; int LatFloor = 10;
        double Long = 5.002; int LongFloor = 5;
        geo = new GeoLocation(Lat, Long);

        assertEquals(Lat, geo.getExactLat(), grace);
        assertEquals(Long, geo.getExactLong(), grace);

        assertEquals(LatFloor, geo.getApproxLat());
        assertEquals(LongFloor, geo.getApproxLong());

        assertEquals("", geo.getDescription());


        // Full constructor tests
        String description = "WickMonalds on East Side";
        geo = new GeoLocation(Lat, Long, description);

        assertEquals(Lat, geo.getExactLat(), grace);
        assertEquals(Long, geo.getExactLong(), grace);

        assertEquals(LatFloor, geo.getApproxLat());
        assertEquals(LongFloor, geo.getApproxLong());

        assertEquals(description, geo.getDescription());
    }

    // Test that the setters work
    @Test
    public void Setters() {
        double Lat = 73.45; int LatFloor = 73;
        double Long = 28.548; int LongFloor = 28;

        geo = new GeoLocation();

        geo.setExactLat(Lat);
        assertEquals(Lat, geo.getExactLat(), grace);
        assertEquals(LatFloor, geo.getApproxLat());

        geo.setExactLong(Long);
        assertEquals(Long, geo.getExactLong(), grace);
        assertEquals(LongFloor, geo.getApproxLong());

        String description = "WickMonalds on the West Side";
        geo.setDescription(description);
        assertEquals(description, geo.getDescription());
    }

    // Test that protection against illegal coordinates works in the constructors and setters
    @Test
    public void IllegalCoordinates() {
        double IllegalPositive = 190.023;
        double IllegalNegative = -200.32;
        double Legal = 10.05;

        RuntimeException uncaught = new RuntimeException("An illegal value was not caught by GeoLocation!");

        try {
            geo = new GeoLocation(IllegalPositive, Legal);
            throw uncaught;
        } catch (AssertionError ignored) {}

        try {
            geo = new GeoLocation(IllegalNegative, Legal);
            throw uncaught;
        } catch (AssertionError ignored) {}

        try {
            geo = new GeoLocation(Legal, IllegalPositive);
            throw uncaught;
        } catch (AssertionError ignored) {}

        try {
            geo = new GeoLocation(Legal, IllegalNegative);
            throw uncaught;
        } catch (AssertionError ignored) {}


        geo = new GeoLocation();

        try {
            geo.setExactLat(IllegalPositive);
            throw uncaught;
        } catch (AssertionError ignored) {}

        try {
            geo.setExactLat(IllegalNegative);
            throw uncaught;
        } catch (AssertionError ignored) {}

        try {
            geo.setExactLong(IllegalPositive);
            throw uncaught;
        } catch (AssertionError ignored) {}

        try {
            geo.setExactLong(IllegalNegative);
            throw uncaught;
        } catch (AssertionError ignored) {}

    }
}
