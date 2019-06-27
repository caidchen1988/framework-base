//echart
var memoryEchart = echarts.init(document.getElementById('memory_main'));
var memoryData = new Array(3);
//定义图表样式
var memoryOption = {
    tooltip : {
        trigger: 'axis',
        axisPointer : {            // 坐标轴指示器，坐标轴触发有效
            type : 'shadow'        // 默认为直线，可选为：'line' | 'shadow'
        },
        formatter: function (params) {
            var dataIndex = params[0].dataIndex;
            var res = params[0].axisValue;
            if(dataIndex==0 || dataIndex==1){
                res += '<br/>累计回收次数：' + params[0].data;
                res += '<br/>累计回收时间：' + params[1].data + "ms";
                res += '<br/>平均回收时间：' + parseInt(params[1].data/params[0].data) + "ms";
            }else{
                res += '<br/>已用内存量：' + params[0].data + "MB";
                if(params[0].axisValue!='峰值内存消耗'){
                    var maxData = memoryData[2];
                    res += '<br/>可用内存量：' + params[1].data + "MB";
                    res += '<br/>最大内存量：' + maxData[dataIndex] + "MB";
                }
            }
            return res;
        }
    },
    color: ['#ff0000','#91C7AE'],
    legend: {
        data: ['已用内存(MB)', '可用内存(MB)']
    },
    grid: {
        left: '3%',
        right: '4%',
        bottom: '3%',
        containLabel: true
    },
    xAxis:  {
        type: 'value'
    },
    yAxis: {
        type: 'category',
        data: ['OldGenGC','EdenGC','Old Gen','Survivor Space','Eden Space','峰值内存消耗','JVM总内存']
    },
    series: [
        {
            name: '已用内存(MB)',
            type: 'bar',
            stack: '总量',
            barWidth: 60,
            label: {
                normal: {
                    show: true,
                    position: 'insideRight'
                }
            },
            data: memoryData[0]
        },
        {
            name: '可用内存(MB)',
            type: 'bar',
            stack: '总量',
            barWidth: 60,
            label: {
                normal: {
                    show: true,
                    position: 'insideRight'
                }
            },
            data: memoryData[1]
        }
    ]
};

memoryEchart.setOption(memoryOption);

//刷新图表数据
function refreshMemoryData() {
    memoryEchart.setOption({
        series: [{
            data: memoryData[0]
        },{
            data: memoryData[1]
        }]
    });
}

//与websocket建立连接
var memorySocket;
function initMemorySocket() {
    if(memorySocket!=undefined || memorySocket!=null){
        memorySocket.close("3000", "断开连接");
    }
    var wsUrl = 'ws://'+$('#hid_host').val()+'/websocket/jvm/monitor';
    console.log(wsUrl);
    memorySocket = new WebSocket(wsUrl);
    memorySocket.onopen = function (evt) {
        console.log("Connection the jvm monitor server success!!!");
    };
    memorySocket.onmessage = function (evt) {
        var memory = $.parseJSON(evt.data);
        var peakUsed = memory.old.usedPeak + memory.eden.usedPeak;
        var usedData = [memory.oldGc.gcCount, memory.edenGc.gcCount, memory.old.used, memory.survivor.used, memory.eden.used, peakUsed, memory.totalUsedMemery];
        var peakMax = parseInt((memory.old.maxPeak + memory.eden.maxPeak)*0.8);
        var usable = [memory.oldGc.gcTime, memory.edenGc.gcTime, memory.old.max-memory.old.used,
            memory.survivor.max-memory.survivor.used, memory.eden.max-memory.eden.used, 0, memory.totalMaxMemery-memory.totalUsedMemery];
        var maxData = [memory.oldGc.gcTime, memory.edenGc.gcTime, memory.old.max, memory.survivor.max, memory.eden.max, 0, memory.totalMaxMemery];
        memoryData[0] = usedData;
        memoryData[1] = usable;
        memoryData[2] = maxData;
        refreshMemoryData();
    };
    memorySocket.onerror = function (evt) {
        memorySocket.close();
    };
}

//断开监控连接
function closeMemoryMonitor() {
    if(memorySocket!=undefined || memorySocket!=null){
        memorySocket.close("3000", "断开连接");
    }
    memoryData[0] = [];
    memoryData[1] = [];
    memoryData[2] = [];
    refreshMemoryData();
    $.messager.show({ title: '系统提示', msg: '已断开监控连接！'});
    console.log("Disconnect the jvm monitor server success!!!");
}

$(function () {
    initMemorySocket();
});