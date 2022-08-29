package edu.hm.hafner.renderer;

import edu.hm.hafner.util.ResourceTest;

import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;

import org.xmlunit.builder.DiffBuilder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test for simple App.
 */
public class EChartsSvgRendererTest extends ResourceTest {

    /**
     * Compares two SVG strings if they are equal, while ignoring comments and whitespaces.
     * @param testResultSvg SVG string produced by EChartsSvgRenderer
     * @param expectedSvg SVG string equivalent to test result SVG
     * @return Returns true if SVG strings are equal.
     */
    private boolean compareSvgs(String testResultSvg, String expectedSvg) {
        return DiffBuilder.compare(testResultSvg).withTest(expectedSvg)
                .ignoreComments()
                .ignoreWhitespace()
                .build()
                .hasDifferences();
    }

    private final String chartConfigJson = toString("configuration-options/bar-basic.json");

    private final String exportConfigJson = toString("export-options/medium.json");

    /**
     * Tests if the application will throw an IllegalArgumentException if no parameters were provided.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfAllParamsAreEmpty() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            echartsSvgRenderer.createSvgString("", "");
        });
        String expectedMessage = "No parameters were provided";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if no chart config parameter was provided.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfChartConfigIsEmpty() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            echartsSvgRenderer.createSvgString("", exportConfigJson);
        });
        String expectedMessage = "Chart configuration parameter is missing.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if no export config parameter was provided.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfExportConfigIsEmpty() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            echartsSvgRenderer.createSvgString(chartConfigJson, "");
        });
        String expectedMessage = "Export configuration parameter is missing.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if the chart config parameter has invalid JSON.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfChartConfigHasInvalidJson() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            String invalidJson = StringUtils.replaceOnce(chartConfigJson, ":", "");
            echartsSvgRenderer.createSvgString(invalidJson, exportConfigJson);
        });
        String expectedMessage = "Chart configuration parameter has invalid JSON.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if the export config parameter has invalid JSON.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfExportConfigHasInvalidJson() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            String invalidJson = StringUtils.replaceOnce(exportConfigJson, ":", "");
            echartsSvgRenderer.createSvgString(chartConfigJson, invalidJson);
        });
        String expectedMessage = "Export configuration parameter has invalid JSON.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if the chart config parameter is missing series
     * key.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfSeriesKeyMissing() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            echartsSvgRenderer.createSvgString(exportConfigJson, exportConfigJson);
        });
        String expectedMessage = "Series key missing in chart configuration parameter.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if the chart config parameter has an empty series
     * value.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfSeriesIsEmpty() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            String chartConfigJsonWithEmptySeries = toString("configuration-options/series-empty.json");
            echartsSvgRenderer.createSvgString(chartConfigJsonWithEmptySeries, exportConfigJson);
        });
        String expectedMessage = "Invalid series value in the chart configuration parameter.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if the export config parameter is missing the
     * width key.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfWidthMissing() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            echartsSvgRenderer.createSvgString(chartConfigJson, chartConfigJson);
        });
        String expectedMessage = "Width and/or height missing in export configuration parameter.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application will throw an IllegalArgumentException if the export config parameter has width as 0.
     */
    @Test
    public void shouldThrowIllegalArgumentExceptionIfWidthZero() {
        Throwable t = assertThrows(IllegalArgumentException.class, () -> {
            EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
            echartsSvgRenderer.createSvgString(chartConfigJson, toString("export-options/invalid-width.json"));
        });
        String expectedMessage = "Invalid width and/or height values in export configuration parameter.";
        String resultMessage = t.getMessage();

        assertTrue(resultMessage.contains(expectedMessage));
    }

    /**
     * Tests if the application is able to return a basic bar chart as SVG.
     */
    @Test
    public void shouldRenderBasicBarChart() {
        String exportOptions = toString("export-options/medium.json");
        String configOptions = toString("configuration-options/bar-basic.json");
        EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);

        String isExpectedResult = toString("screenshots/bar-basic.svg");
        assertThat(compareSvgs(result, isExpectedResult)).isFalse();
    }

    /**
     * Tests if the application is able to return a stacked bar chart as SVG.
     */
    @Test
    public void shouldRenderStackedBarChart() {
        String exportOptions = toString("export-options/large.json");
        String configOptions = toString("configuration-options/bar-stack.json");
        EChartsSvgRenderer echartsSvgRenderer = new EChartsSvgRenderer();
        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);

        String isExpectedResult = toString("screenshots/bar-stack.svg");
        assertThat(compareSvgs(result, isExpectedResult)).isFalse();
    }
}