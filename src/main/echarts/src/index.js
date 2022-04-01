var http = require("http");
var chartRenderer = require("./charts");
console.log(process.argv)
http
  .createServer(function (req, res) {
    res.writeHead(200, {
      "Content-Type": "application/xml"
    });
    res.write(chartRenderer.renderChart(800, 600));
    res.end();
  })
  .listen(8080);