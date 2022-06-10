启动项目, 会自动生成28张表:
act_ge_ 通用数据表, ge是general的缩写

act_hi_ 历史数据表, hi是history的缩写, 对应HistoryService接口
查询历史流程实例(act_hi_procinst表)
查询历史活动(act_hi_actinst表)
查询历史任务(act_hi_taskinst表)
查询历史流程变量(act_hi_varinst表)

act_id_ 身份数据表, id是identity的缩写, 对应IdentityService接口

act_re_ 流程存储表, re是repository的缩写, 对应RepositoryService接口, 存储流程部署和流程定义等静态数据

act_ru_ 运行时数据表, ru是runtime的缩写, 对应RuntimeService接口和TaskService接口, 存储流程实例和用户任务等动态数据

#####################################


