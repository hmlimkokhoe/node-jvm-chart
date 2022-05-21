package edu.hm.hafner.renderer;


import edu.hm.hafner.util.ResourceTest;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit test for simple App.
 */
public class EchartsSvgRendererTest extends ResourceTest {

    private static final Logger LOG = LoggerFactory.getLogger(EchartsSvgRendererTest.class);

    /**
     * Tests if the application is able to return an svg string.
     */
    @Test
    public void shouldReturnAnSVGString() {
        EchartsSvgRenderer echartsSvgRenderer = new EchartsSvgRenderer();

        String exportOptions = toString("exportOptions.json");
        String configOptions = toString("configOptions.json");
        exportOptions = exportOptions.replace("\n", "");
        configOptions = configOptions.replace("\n", "");

        String result = echartsSvgRenderer.render(configOptions, exportOptions);
        String isExpectedResult = toString("echartsSvg.svg");

        assertThat(result).isEqualTo(isExpectedResult);
    }

    //TODO: More tests with different parameters
}
