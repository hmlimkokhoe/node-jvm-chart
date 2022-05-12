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

    private static final Logger LOG = LoggerFactory.getLogger(EchartsSvgRenderer.class);

    /**
     * Tests if the application is able to return an svg string.
     * TODO:
     * TODO: file as svg type
     */
    @Test
    public void shouldReturnAnSVGString() {
        EchartsSvgRenderer echartsSvgRenderer = new EchartsSvgRenderer();
        String result = echartsSvgRenderer.render(); //TODO: render(defaultOptions, defaultParams)

        String isExpectedResult = toString("echartsSvg.svg");

        //LOG.info(result);
        assertThat(result).isEqualTo(isExpectedResult);
        //Assert.assertThat(result, result.contains(""));
    }

    //TODO: More tests with different parameters
}
