//var http = require("http");
/*var trireme = require('trireme-support');
if (trireme.isTrireme()) {
    console.log('We are running on Trireme.');
}*/

// console.log(process.cwd());
//const echarts = require("./echarts");
console.log('==== START OF NODE-JS LOGGING ====');

const echarts = require("/home/lim/Projects/trireme-java/src/main/resources/echarts/node_modules/echarts");
const fs = require('fs');
const path = require('path');

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

//console.log(process.argv) <-- used for checking if parameters from Java were sent

const svgString = renderChart(800, 600);
// console.log("Incoming string in Node.js...")
console.log(svgString)

exportSvgToFile = (svgStringParam) => {
    let svgString = "";

    if (svgStringParam.length > 0) {
        svgString = svgStringParam;
    }

    const writePath = path.join(process.cwd(), "output/svg.txt");

    fs.writeFile(writePath, svgString, (err) => {
        if (err) {
            console.log("Failed to export SVG:");
            return console.error(err);
        }
    });
}

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
console.log('==== END OF NODE-JS LOGGING ====');
return svgString;
