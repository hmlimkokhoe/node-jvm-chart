// console.log(process.argv) <-- used for checking if parameters from Java were sent
//const http = require("http");

// const echarts = require("/home/lim/Projects/trireme-java/src/main/resources/echarts/node_modules/echarts");
const echarts = require("echarts");
const fs = require('fs');
const path = require('path');
const os = require("os");

const defaultConfig = {
    renderer: "svg",
    ssr: true,
    width: 400,
    height: 300
}

const defaultOptions = {
    xAxis: {
        type: "category",
        data: ["Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"]
    },
    yAxis: {
        type: "value"
    },
    series: [
        {
            data: [120, 200, 150, 80, 70, 110, 130],
            type: "bar"
        }
    ]
}

/**
 * Renders chart model parameter as SVG string.
 * @param widthParam
 * @param heightParam
 * @returns {string}
 */
function renderChart(widthParam, heightParam) {
    let chartConfig = Object.assign({}, defaultConfig);

    if (widthParam > 0 || heightParam > 0 ) {
        chartConfig.width = widthParam;
        chartConfig.height = heightParam;
    } else {
        return "";
    }

    const chart = echarts.init(null, null, chartConfig);
    chart.setOption(defaultOptions);
    return chart.renderToSVGString();
}

/**
 * Exports the ECharts svg string to the temp directory of the operating system.
 * @param {String} svgStringParam
 */
exportSvgToFile = (svgStringParam) => {
    const writePath = path.join(os.tmpdir(), "echartsSvg.txt");
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

const svgString = renderChart(800, 600);
exportSvgToFile(svgString);

/*
http
  .createServer(function (req, res) {
    res.writeHead(200, {
      "Content-Type": "application/xml"
    });
    res.write(chartRenderer.renderChart(800, 600));
    res.end();
  })
  .listen(8080);
*/
process.exit(1);