// var http = require("http");
var chartRenderer = require("./charts");
var filesaver = require("./charts/filesaver");
//console.log(process.argv) <-- used for checking if parameters from Java were sent

const svgString = chartRenderer.renderChart(800, 600);
filesaver.exportSvgToFile(svgString);

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

// return chartRenderer.renderChart(800,600);