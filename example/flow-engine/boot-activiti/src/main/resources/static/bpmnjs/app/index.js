import $ from 'jquery';
import BpmnModeler from 'bpmn-js/lib/Modeler';
import propertiesPanelModule from 'bpmn-js-properties-panel';
// import propertiesProviderModule from 'bpmn-js-properties-panel/lib/provider/camunda';
// import camundaModdleDescriptor from 'camunda-bpmn-moddle/resources/camunda.json';
import propertiesProviderModule from '../resources/properties-panel/provider/activiti';
import activitiModleDescriptor from '../resources/activiti.json';
import customTranslate from '../resources/customTranslate/customTranslate';
import customControlsModule from '../resources/customControls';
import diagramXML from '../resources/newDiagram.bpmn';
import tools from "../resources/tools";

const proHost = window.location.protocol + "//" + window.location.host;
const href = window.location.href.split("bpmnjs")[0];
const key = href.split(window.location.host)[1];
const publicUrl = proHost + key;

var container = $('#js-drop-zone');
var canvas = $('#js-canvas');

// 添加翻译组件
var customTranslateModule = {
    translate: ['value', customTranslate]
};

var bpmnModeler = new BpmnModeler({
    container: canvas,
    propertiesPanel: {
        parent: '#js-properties-panel'
    },
    additionalModules: [
        propertiesPanelModule,
        propertiesProviderModule,
        customControlsModule,
        customTranslateModule
    ],
    moddleExtensions: {
        // camunda: camundaModdleDescriptor
        activiti: activitiModleDescriptor
    }
});
container.removeClass('with-diagram');

// 判断浏览器支持程度
if (!window.FileList || !window.FileReader) {
    window.alert('您的浏览器不支持拖放，请使用谷歌、火狐或IE10+浏览器。');
} else {
    registerFileDrop(container, openDiagram);
}

/**
 * 创建新的BPMN
 */
function createNewDiagram() {
    openDiagram(diagramXML);
}

/**
 * 打开BPMN XML文件
 * @param xml
 * @returns {Promise<void>}
 */
async function openDiagram(xml) {
    try {
        await bpmnModeler.importXML(xml);
        container.removeClass('with-error').addClass('with-diagram');
    } catch (err) {
        container.removeClass('with-diagram').addClass('with-error');
        container.find('.error pre').text(err.message);
        console.error(err);
    }
}

/**
 * 注册文件拖拽
 * @param container
 * @param callback
 */
function registerFileDrop(container, callback) {
    function handleFileSelect(e) {
        e.stopPropagation();
        e.preventDefault();

        var files = e.dataTransfer.files;
        var file = files[0];
        var reader = new FileReader();

        reader.onload = function (e) {
            var xml = e.target.result;
            console.log(xml);
            callback(xml);
        };
        reader.readAsText(file);
    }

    function handleDragOver(e) {
        e.stopPropagation();
        e.preventDefault();

        e.dataTransfer.dropEffect = 'copy'; // Explicitly show this is a copy.
    }

    container.get(0).addEventListener('dragover', handleDragOver, false);
    container.get(0).addEventListener('drop', handleFileSelect, false);
}

$(function () {
    // 获取url参数
    var param = tools.getUrlParam(window.location.href);

    // 创建BPMN
    if (param.type === 'addBPMN') {
        createNewDiagram();
    } else if (param.type === 'showBPMN') { // 查看BPMN
        $('.buttons li').hide()
        $('#downloadBpmnBtn').parent().show();

        const deploymentId = param.deploymentId || '6d4af2dc-bab0-11ea-b584-3cf011eaafca';
        const resourceName = param.resourceName || 'String.bpmn';
        const instanceId = param.instanceId;

        var requestParams = {
            "deploymentId": deploymentId,
            "resourceName": decodeURI(resourceName)
        }
        // 查看历史流程实例
        if (instanceId) {
            // 高亮渲染历史流程实例
            $.ajax({
                url: publicUrl + 'history/highlight',
                type: 'GET',
                data: {
                    instanceId
                },
                dataType: 'json',
                success: function (result) {
                    var colorJson = tools.createColorJson(result.data);
                    $.ajax({
                        url: publicUrl + 'processDefinition/getDefinitionXML',
                        type: 'GET',
                        data: requestParams,
                        dataType: 'text',
                        success: function (result) {
                            openDiagram(result);
                            setTimeout(function () {
                                for (var i in colorJson) {
                                    tools.setColor(colorJson[i], bpmnModeler)
                                }
                            }, 200);
                        },
                        error: function (err) {
                            console.log(err);
                        }
                    });
                },
                error: function (err) {
                    console.log(err);
                }
            });
        } else { // 查看流程定义
            // 获取流程定义XML
            $.ajax({
                url: publicUrl + 'processDefinition/getDefinitionXML',
                type: 'GET',
                data: requestParams,
                dataType: 'text',
                success: function (result) {
                    openDiagram(result);
                },
                error: function (err) {
                    console.log(err);
                }
            });
        }
    }

    // 点击导入
    $('#uploadFile').on('change', function () {
        uploadFile();
    });

    // 点击导出
    $('#downloadBpmnBtn').on('click', function () {
        downloadBPMN();
    });

    // 点击部署
    $('#deployBpmnBtn').on('click', function () {
        tools.openDialog('alert');
    });

    // 点击部署-确定
    $('.btn-sure').on("click", function () {
        saveBPMN();
    });

    // 点击部署-取消
    $('.btn-cancel').on("click", function () {
        tools.hideDialog('alert');
    });

    /**
     * 上传导入BPMN
     */
    function uploadFile() {
        var file = document.uploadFileForm.uploadFile.files[0];
        var fm = new FormData();
        fm.append("processFile", file);
        $.ajax({
            url: publicUrl + 'processDefinition/uploadFile',
            type: 'POST',
            dataType: 'json',
            data: fm,
            async: false,
            contentType: false,
            processData: false,
            success: function (res) {
                if (res.code == 200) {
                    var url = publicUrl + 'bpmn/' + res.data;
                    openBPMN(url);
                } else {
                    alert(res.msg);
                }
            },
            error: function (err) {
                alert(err);
            }
        });
    }

    /**
     * 下载BPMN
     */
    function downloadBPMN() {
        var downloadLink = $('#downloadBpmnBtn');
        bpmnModeler.saveXML({format: true}, function (err, xml) {
            if (err) {
                return console.error("could not save bpmn!");
            }
            var data = err ? null : xml;
            if (data) {
                var encodedData = encodeURIComponent(data);
                downloadLink.addClass('active').attr({
                    'href': 'data:application/bpmn20-xml;charset=UTF-8,' + encodedData,
                    'download': 'diagram.bpmn'
                });
            } else {
                downloadLink.removeClass('active');
            }
        });
    }

    /**
     * 部署BPMN
     */
    function saveBPMN() {
        bpmnModeler.saveXML({format: true}, function (err, xml) {
            if (err) {
                return console.error("could not save bpmn!");
            }
            var params = {
                "bpmnXmlStr": xml
            };
            $.ajax({
                url: publicUrl + 'processDefinition/addAndDeploymentByString',
                type: 'POST',
                dataType: 'json',
                data: params,
                success: function (res) {
                    tools.hideDialog('alert');
                    if (res.code == 200) {
                        alert("BPMN部署成功！")
                    } else {
                        alert(res.msg);
                    }
                },
                error: function (err) {
                    alert(err);
                }
            });
        });
    }

    /**
     * 在线打开BPMN文件
     * @param url
     */
    function openBPMN(url) {
        $.ajax(url, {dataType: 'text'}).done(async function (xml) {
            try {
                await bpmnModeler.importXML(xml);
                container.removeClass('with-error').addClass('with-diagram');
            } catch (err) {
                console.error(err);
            }
        });
    }
});
