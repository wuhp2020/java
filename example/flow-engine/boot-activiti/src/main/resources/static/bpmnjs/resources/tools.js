import $ from 'jquery';

const tools = {
    /**
     * 获取url参数
     * @param url
     * @returns {{}|*}
     */
    getUrlParam: function (url) {
        var object = {};
        if (url.indexOf("?") != -1) {
            var str = url.split("?")[1];
            var strs = str.split("&");
            for (var i = 0; i < strs.length; i++) {
                object[strs[i].split("=")[0]] = strs[i].split("=")[1]
            }
            return object
        }
        return object[url];
    },

    /**
     * 设置颜色
     * @param json
     * @param bpmnModeler
     */
    setColor(json, bpmnModeler) {
        var modeling = bpmnModeler.get('modeling');
        var elementRegistry = bpmnModeler.get('elementRegistry')
        var elementToColor = elementRegistry.get(json.name);
        if (elementToColor) {
            modeling.setColor([elementToColor], {
                stroke: json.stroke,
                fill: json.fill
            });
        }
    },

    /**
     * 创建颜色数据
     * @param data
     * @returns {[]}
     */
    createColorJson(data) {
        var colorJson = [];
        for (var k in data['highLines']) {
            var par = {
                "name": data['highLines'][k],
                "stroke": "green",
                "fill": "green"
            };
            colorJson.push(par);
        }
        for (var k in data['highFinishedPoints']) {
            var par = {
                "name": data['highFinishedPoints'][k],
                "stroke": "gray",
                "fill": "#eae9e9"
            };
            colorJson.push(par);
        }
        for (var k in data['highIFinishedTasks']) {
            var par = {
                "name": data['highIFinishedTasks'][k],
                "stroke": "green",
                "fill": "#a3d68e"
            };
            colorJson.push(par);
        }
        for (var k in data['highUnfinishedPoints']) {
            var par = {
                "name": data['highUnfinishedPoints'][k],
                "stroke": "green",
                "fill": "yellow"
            };
            colorJson.push(par);
        }
        return colorJson;
    },

    /**
     * 打开弹出框
     * @param id
     */
    openDialog(id) {
        var dom = $("#" + id);
        // 弹出框居中
        var top = dom.height() / 2;
        dom.css({
            "top": "50%",
            "margin-top": "-" + top + "px"
        });
        $(".dialog-mask").fadeIn(300);
        setTimeout(function () {
            dom.show();
        }, 300);
    },

    /**
     * 隐藏弹出框
     * @param id
     */
    hideDialog(id) {
        var dom = $("#" + id);
        $(".dialog-mask").fadeOut(300);
        setTimeout(function () {
            dom.hide();
        }, 300);
    },
}

export default tools;