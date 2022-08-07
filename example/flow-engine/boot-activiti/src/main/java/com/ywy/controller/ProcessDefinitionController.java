package com.ywy.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywy.config.GlobalConfig;
import com.ywy.rest.PageParam;
import com.ywy.rest.PageResult;
import com.ywy.rest.Result;
import org.activiti.engine.RepositoryService;
import org.activiti.engine.repository.Deployment;
import org.activiti.engine.repository.ProcessDefinition;
import org.activiti.engine.repository.ProcessDefinitionQuery;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.InputStream;
import java.util.*;
import java.util.zip.ZipInputStream;

/**
 * 流程定义Controller
 * @author ywy
 * @version 1.0.0
 * @date 2021-04-06 17:13
 */
@RestController
@RequestMapping("processDefinition")
public class ProcessDefinitionController {
    @Autowired
    private RepositoryService repositoryService;

    /**
     * 通过bpmn xml字符串添加流程定义
     * @param bpmnXmlStr
     * @param deploymentName
     * @return
     */
    @PostMapping("addAndDeploymentByString")
    public Result addAndDeploymentByString(String bpmnXmlStr, String deploymentName) {
        try {
            Deployment deployment = repositoryService.createDeployment().addString("createWithBPMNJS.bpmn", bpmnXmlStr)
                    .name(deploymentName).deploy();
            return Result.success("部署流程成功", deployment.getId());
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("部署流程失败");
        }
    }

    /**
     * 上传bpmn文件流并添加流程定义
     * @param multipartFile
     * @param deploymentName
     * @return
     */
    @PostMapping("uploadStreamAndDeployment")
    public Result uploadStreamAndDeployment(@RequestParam("processFile") MultipartFile multipartFile, String deploymentName) {
        try {
            // 获取上传文件名
            String filename = multipartFile.getOriginalFilename();
            // 获取文件扩展名
            String extension = FilenameUtils.getExtension(filename);
            // 获取文件字节流
            InputStream inputStream = multipartFile.getInputStream();

            Deployment deployment = null;
            if ("zip".equals(extension)) {
                ZipInputStream zipInputStream = new ZipInputStream(inputStream);
                deployment = repositoryService.createDeployment().addZipInputStream(zipInputStream)
                        .name(deploymentName).deploy();
            } else {
                deployment = repositoryService.createDeployment().addInputStream(filename, inputStream)
                        .name(deploymentName).deploy();
            }
            return Result.success("上传流程成功", deployment.getId() + ";" + filename);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("上传流程失败");
        }
    }

    /**
     * 上传bpmn文件
     * @param multipartFile
     * @return
     */
    @PostMapping("uploadFile")
    public Result uploadFile(@RequestParam("processFile") MultipartFile multipartFile) {
        try {
            if (multipartFile.isEmpty()) {
                return Result.failure("文件不存在");
            }
            // 获取上传文件名
            String filename = multipartFile.getOriginalFilename();
            String suffixName = filename.substring(filename.lastIndexOf("."));
            String filePath = GlobalConfig.bpmnFileUploadPath;
            filename = UUID.randomUUID() + suffixName;
            File file = new File(filePath + filename);
            if (!file.getParentFile().exists()) {
                file.getParentFile().mkdirs();
            }
            multipartFile.transferTo(file);

            return Result.success("上传文件成功", filename);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("上传文件失败");
        }
    }

    /**
     * 获取流程定义列表
     * @return
     */
    @GetMapping("getDefinitions")
    public Result getDefinitions(PageParam pageParam) {
        try {
            List<Map<String, Object>> listMap = new ArrayList<>();
            ProcessDefinitionQuery processDefinitionQuery = repositoryService.createProcessDefinitionQuery();
            String params = pageParam.getParams();
            if (params != null) {
                JSONObject jsonObject = JSON.parseObject(params);
                String name = jsonObject.getString("name");
                if (StringUtils.isNotBlank(name)) {
                    processDefinitionQuery.processDefinitionName(name);
                }
            }
            long count = processDefinitionQuery.count();
            List<ProcessDefinition> list = processDefinitionQuery.listPage(pageParam.getOffset(), pageParam.getLimit());
            list.sort((o1, o2) -> o2.getVersion() - o1.getVersion());

            for (ProcessDefinition pd : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", pd.getId());
                map.put("deploymentId", pd.getDeploymentId());
                map.put("name", pd.getName());
                map.put("resourceName", pd.getResourceName());
                map.put("key", pd.getKey());
                map.put("version", pd.getVersion());
                listMap.add(map);
            }
            PageResult pageResult = new PageResult(count, listMap);
            return Result.success("获取流程定义成功", pageResult);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("获取流程定义失败");
        }
    }

    /**
     * 获取流程定义XML
     * @param deploymentId
     * @param resourceName
     * @param response
     */
    @GetMapping("getDefinitionXML")
    public void getDefinitionXML(String deploymentId, String resourceName, HttpServletResponse response) {
        try {
            InputStream inputStream = repositoryService.getResourceAsStream(deploymentId, resourceName);
            int count = inputStream.available();
            byte[] bytes = new byte[count];
            response.setContentType("text/xml");
            ServletOutputStream outputStream = response.getOutputStream();
            while (inputStream.read(bytes) != -1) {
                outputStream.write(bytes);
            }
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取流程部署列表
     * @return
     */
    @GetMapping("getDeployments")
    public Result getDeployments() {
        try {
            List<Map<String, Object>> listMap = new ArrayList<>();
            List<Deployment> list = repositoryService.createDeploymentQuery().list();
            for (Deployment dep : list) {
                Map<String, Object> map = new HashMap<>();
                map.put("id", dep.getId());
                map.put("name", dep.getName());
                map.put("deploymentTime", dep.getDeploymentTime());
                listMap.add(map);
            }
            return Result.success("获取流程部署成功", listMap);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("获取流程部署失败");
        }
    }

    /**
     * 删除流程部署
     * @param deploymentId
     * @return
     */
    @PostMapping("deleteDeployment")
    public Result deleteDeployment(String deploymentId) {
        try {
            repositoryService.deleteDeployment(deploymentId, true);
            return Result.success("删除流程定义成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return Result.failure("删除流程定义失败");
        }
    }
}
