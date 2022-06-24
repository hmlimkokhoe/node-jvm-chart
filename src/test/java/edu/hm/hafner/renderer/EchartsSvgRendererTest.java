package edu.hm.hafner.renderer;


import edu.hm.hafner.util.ResourceTest;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple App.
 */
public class EchartsSvgRendererTest extends ResourceTest {

    private String formatStringOutput(String output) {
        return output.replaceAll("\\s+","");
    }

    /**
     * Tests if the application is able to return a basic bar chart as SVG.
     */
    @Test
    public void shouldRenderBasicBarChart() {
        EchartsSvgRenderer echartsSvgRenderer = new EchartsSvgRenderer();

        String exportOptions = toString("export-options/export-stack.json");
        String configOptions = toString("configuration-options/bar-basic.json");
        exportOptions = exportOptions.replace("\n", "");
        configOptions = configOptions.replace("\n", "");

        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);
        result = formatStringOutput(result);

        String isExpectedResult = toString("screenshots/bar-basic.svg");
        isExpectedResult = formatStringOutput(isExpectedResult);

        assertThat(result).isEqualTo(isExpectedResult);
    }

    /**
     * Tests if the application is able to return a stacked bar chart as SVG.
     */
    @Test
    public void shouldRenderStackedBarChart() {
        EchartsSvgRenderer echartsSvgRenderer = new EchartsSvgRenderer();

        String exportOptions = toString("export-options/export-basic.json");
        String configOptions = toString("configuration-options/bar-stack.json");
        exportOptions = exportOptions.replace("\n", "");
        configOptions = configOptions.replace("\n", "");

        String result = echartsSvgRenderer.createSvgString(configOptions, exportOptions);
        result = formatStringOutput(result);

        String isExpectedResult = toString("screenshots/bar-stack.svg");
        isExpectedResult = formatStringOutput(isExpectedResult);

        assertThat(result).isEqualTo(isExpectedResult);
    }
}
