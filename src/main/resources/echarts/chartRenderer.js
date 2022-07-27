const triremeParameters = process.argv;
const tempDirectoryPath = triremeParameters[triremeParameters.length - 1].toString();

if (tempDirectoryPath !== undefined && tempDirectoryPath.length < 0) {
    console.error("Failed to execute rendering scripts due to incorrect installation of Node.js scripts.");
    process.exit(1);
}

const echarts = require(tempDirectoryPath + "/echarts");
const fs = require('fs');
const path = require('path');

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
    const DATA = "data";
    const RENDERER = "renderer";

    const chartConfigJson = getEChartsParameterJson(DATA);
    const chartExportJson = getEChartsParameterJson(RENDERER);

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
    let svgFileName = triremeParameters[4];

    if (svgFileName !== undefined && svgFileName.length < 0) {
        return;
    }

    const writePath = path.join(tempDirectoryPath + "/" + svgFileName);
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
    console.error("Failed to export SVG render due to incorrect configuration parameters.");
}

//TODO: Node.js should terminate on its own
process.exit(0);