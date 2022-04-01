var echarts = require("echarts");

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
    }

    const chart = echarts.init(null, null, chartConfig);
  
    chart.setOption(defaultOptions);
  
    return chart.renderToSVGString();
}

exports.renderChart = renderChart;