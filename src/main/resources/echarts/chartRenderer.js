const triremeParameters = process.argv;
const echarts = require(triremeParameters[triremeParameters.length - 1].toString() + "/echarts");
const fs = require('fs');
const path = require('path');
const os = require("os");

/**
 * Returns a JSON object built from an ECharts string parameter.
 * @param jsonTypeValue
 * @returns {null|any}
 */
const getEChartsParameterJson = (jsonTypeValue) => {

    //Removes all characters violating JSON formatting
    const convertStringToJson = (stringParam) => {
        stringParam.replace(/\\/i, '');
        stringParam.replace(/"'/i, '');    // Removes first wrapping double quote
        stringParam.replace(/'"/i, '');    // Removes last wrapping double quote
        return JSON.parse(stringParam);
    }

    if (triremeParameters.length > 0 && jsonTypeValue.length > 0) {
        let arrayElement = "";

        for (let i = 0; i < triremeParameters.length; i++) {
            if (triremeParameters[i].includes(jsonTypeValue)) {
                convertStringToJson(triremeParameters[i]);
                arrayElement = triremeParameters[i];
            }
        }
        return convertStringToJson(arrayElement);
    }
    return null;
}

/**
 * Renders chart model parameter as SVG string.
 * @returns {string}
 */
const renderChart = () => {
    const chartConfigJson = getEChartsParameterJson("data");
    const chartExportJson = getEChartsParameterJson("renderer");

    if (chartConfigJson != null && chartExportJson != null) {
        const chart = echarts.init(null, null, chartExportJson);
        chart.setOption(chartConfigJson);
        return chart.renderToSVGString();
    }
    return "";
}

/**
 * Exports the ECharts svg string to the temp directory of the operating system.
 * @param {String} svgStringParam
 */
const exportSvgToFile = (svgStringParam) => {
    const writePath = path.join(os.tmpdir(), "trireme-echarts-output.svg");
    let svgString = "";

    if (svgStringParam.length > 0) {
        svgString = svgStringParam;
    } else {
        return;
    }

    try {
        fs.writeFileSync(writePath, svgString);
    } catch (err) {
        console.error(err);
    }
}

const svgString = renderChart();

if (svgString.length > 0) {
    exportSvgToFile(svgString);
} else {
    console.log("Failed to export SVG file, because no SVG String parameter was provided.");
}

process.exit(1);