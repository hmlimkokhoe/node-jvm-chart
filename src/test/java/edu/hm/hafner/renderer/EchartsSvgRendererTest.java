package edu.hm.hafner.renderer;

import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Unit test for simple App.
 */
public class EchartsSvgRendererTest {

    private static final Logger LOG = LoggerFactory.getLogger(EchartsSvgRenderer.class);

    /**
     * Tests if the application is able to return an svg string.
     */
    @Test
    public void shouldReturnAnSVGString() {
        EchartsSvgRenderer echartsSvgRenderer = new EchartsSvgRenderer();
        String result = echartsSvgRenderer.render();

        String expectedResult = "<svg width=\"800\" height=\"600\" xmlns=\"http://www.w3.org/2000/svg\" xmlns:xlink=\"http://www.w3.org/1999/xlink\" version=\"1.1\" baseProfile=\"full\" viewBox=\"0 0 800 600\">\n" +
                "<rect width=\"800\" height=\"600\" x=\"0\" y=\"0\" id=\"0\" fill=\"none\" fill-opacity=\"1\"></rect>\n" +
                "<path d=\"M80 530.5L720 530.5\" fill=\"none\" stroke=\"#E0E6F1\"></path>\n" +
                "<path d=\"M80 412.5L720 412.5\" fill=\"none\" stroke=\"#E0E6F1\"></path>\n" +
                "<path d=\"M80 295.5L720 295.5\" fill=\"none\" stroke=\"#E0E6F1\"></path>\n" +
                "<path d=\"M80 177.5L720 177.5\" fill=\"none\" stroke=\"#E0E6F1\"></path>\n" +
                "<path d=\"M80 60.5L720 60.5\" fill=\"none\" stroke=\"#E0E6F1\"></path>\n" +
                "<path d=\"M80 530.5L720 530.5\" fill=\"none\" stroke=\"#6E7079\" stroke-linecap=\"round\"></path>\n" +
                "<path d=\"M80.5 530L80.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<path d=\"M171.5 530L171.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<path d=\"M263.5 530L263.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<path d=\"M354.5 530L354.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<path d=\"M445.5 530L445.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<path d=\"M537.5 530L537.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<path d=\"M628.5 530L628.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<path d=\"M720.5 530L720.5 535\" fill=\"none\" stroke=\"#6E7079\"></path>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"end\" style=\"font-size:12px;font-family:sans-serif;\" transform=\"translate(72 530)\" fill=\"#6E7079\">0</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"end\" style=\"font-size:12px;font-family:sans-serif;\" transform=\"translate(72 412.5)\" fill=\"#6E7079\">50</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"end\" style=\"font-size:12px;font-family:sans-serif;\" transform=\"translate(72 295)\" fill=\"#6E7079\">100</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"end\" style=\"font-size:12px;font-family:sans-serif;\" transform=\"translate(72 177.5)\" fill=\"#6E7079\">150</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"end\" style=\"font-size:12px;font-family:sans-serif;\" transform=\"translate(72 60)\" fill=\"#6E7079\">200</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"middle\" style=\"font-size:12px;font-family:sans-serif;\" y=\"6\" transform=\"translate(125.7143 538)\" fill=\"#6E7079\">Mon</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"middle\" style=\"font-size:12px;font-family:sans-serif;\" y=\"6\" transform=\"translate(217.1429 538)\" fill=\"#6E7079\">Tue</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"middle\" style=\"font-size:12px;font-family:sans-serif;\" y=\"6\" transform=\"translate(308.5714 538)\" fill=\"#6E7079\">Wed</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"middle\" style=\"font-size:12px;font-family:sans-serif;\" y=\"6\" transform=\"translate(400 538)\" fill=\"#6E7079\">Thu</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"middle\" style=\"font-size:12px;font-family:sans-serif;\" y=\"6\" transform=\"translate(491.4286 538)\" fill=\"#6E7079\">Fri</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"middle\" style=\"font-size:12px;font-family:sans-serif;\" y=\"6\" transform=\"translate(582.8571 538)\" fill=\"#6E7079\">Sat</text>\n" +
                "<text dominant-baseline=\"central\" text-anchor=\"middle\" style=\"font-size:12px;font-family:sans-serif;\" y=\"6\" transform=\"translate(674.2857 538)\" fill=\"#6E7079\">Sun</text>\n" +
                "<path d=\"M94.2 530l63.1 0l0 -282l-63.1 0Z\" fill=\"#5470c6\" class=\"zr0-cls-0\"></path>\n" +
                "<path d=\"M185.6 530l63.1 0l0 -470l-63.1 0Z\" fill=\"#5470c6\" class=\"zr0-cls-1\"></path>\n" +
                "<path d=\"M277 530l63.1 0l0 -352.5l-63.1 0Z\" fill=\"#5470c6\" class=\"zr0-cls-2\"></path>\n" +
                "<path d=\"M368.5 530l63.1 0l0 -188l-63.1 0Z\" fill=\"#5470c6\" class=\"zr0-cls-3\"></path>\n" +
                "<path d=\"M459.9 530l63.1 0l0 -164.5l-63.1 0Z\" fill=\"#5470c6\" class=\"zr0-cls-4\"></path>\n" +
                "<path d=\"M551.3 530l63.1 0l0 -258.5l-63.1 0Z\" fill=\"#5470c6\" class=\"zr0-cls-5\"></path>\n" +
                "<path d=\"M642.7 530l63.1 0l0 -305.5l-63.1 0Z\" fill=\"#5470c6\" class=\"zr0-cls-6\"></path>\n" +
                "<style ><![CDATA[\n" +
                ".zr0-cls-0 {\n" +
                "animation:zr0-ani-0 1s cubic-bezier(0.65,0,0.35,1) both;\n" +
                "}\n" +
                ".zr0-cls-1 {\n" +
                "animation:zr0-ani-1 1s cubic-bezier(0.65,0,0.35,1) both;\n" +
                "}\n" +
                ".zr0-cls-2 {\n" +
                "animation:zr0-ani-2 1s cubic-bezier(0.65,0,0.35,1) both;\n" +
                "}\n" +
                ".zr0-cls-3 {\n" +
                "animation:zr0-ani-3 1s cubic-bezier(0.65,0,0.35,1) both;\n" +
                "}\n" +
                ".zr0-cls-4 {\n" +
                "animation:zr0-ani-4 1s cubic-bezier(0.65,0,0.35,1) both;\n" +
                "}\n" +
                ".zr0-cls-5 {\n" +
                "animation:zr0-ani-5 1s cubic-bezier(0.65,0,0.35,1) both;\n" +
                "}\n" +
                ".zr0-cls-6 {\n" +
                "animation:zr0-ani-6 1s cubic-bezier(0.65,0,0.35,1) both;\n" +
                "}\n" +
                "@keyframes zr0-ani-0 {\n" +
                "0% {\n" +
                "d:path(\"M94.2 530l63.1 0l0 0l-63.1 0Z\");\n" +
                "}\n" +
                "100% {\n" +
                "d:path(\"M94.2 530l63.1 0l0 -282l-63.1 0Z\");\n" +
                "}\n" +
                "}\n" +
                "@keyframes zr0-ani-1 {\n" +
                "0% {\n" +
                "d:path(\"M185.6 530l63.1 0l0 0l-63.1 0Z\");\n" +
                "}\n" +
                "100% {\n" +
                "d:path(\"M185.6 530l63.1 0l0 -470l-63.1 0Z\");\n" +
                "}\n" +
                "}\n" +
                "@keyframes zr0-ani-2 {\n" +
                "0% {\n" +
                "d:path(\"M277 530l63.1 0l0 0l-63.1 0Z\");\n" +
                "}\n" +
                "100% {\n" +
                "d:path(\"M277 530l63.1 0l0 -352.5l-63.1 0Z\");\n" +
                "}\n" +
                "}\n" +
                "@keyframes zr0-ani-3 {\n" +
                "0% {\n" +
                "d:path(\"M368.5 530l63.1 0l0 0l-63.1 0Z\");\n" +
                "}\n" +
                "100% {\n" +
                "d:path(\"M368.5 530l63.1 0l0 -188l-63.1 0Z\");\n" +
                "}\n" +
                "}\n" +
                "@keyframes zr0-ani-4 {\n" +
                "0% {\n" +
                "d:path(\"M459.9 530l63.1 0l0 0l-63.1 0Z\");\n" +
                "}\n" +
                "100% {\n" +
                "d:path(\"M459.9 530l63.1 0l0 -164.5l-63.1 0Z\");\n" +
                "}\n" +
                "}\n" +
                "@keyframes zr0-ani-5 {\n" +
                "0% {\n" +
                "d:path(\"M551.3 530l63.1 0l0 0l-63.1 0Z\");\n" +
                "}\n" +
                "100% {\n" +
                "d:path(\"M551.3 530l63.1 0l0 -258.5l-63.1 0Z\");\n" +
                "}\n" +
                "}\n" +
                "@keyframes zr0-ani-6 {\n" +
                "0% {\n" +
                "d:path(\"M642.7 530l63.1 0l0 0l-63.1 0Z\");\n" +
                "}\n" +
                "100% {\n" +
                "d:path(\"M642.7 530l63.1 0l0 -305.5l-63.1 0Z\");\n" +
                "}\n" +
                "}\n" +
                "]]>\n" +
                "\n" +
                "</style>\n" +
                "</svg>";

        //LOG.info(result);
        assertTrue(result.equals(expectedResult));
        //Assert.assertThat(result, result.contains(""));
    }
}
