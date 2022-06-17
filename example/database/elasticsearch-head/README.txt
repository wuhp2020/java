
#nginx代理配置
upstream manage {
    server 192.168.1.110:8082;
}

location  /manage {
    proxy_set_header   Host             $host;
    proxy_set_header   x-forwarded-for  $remote_addr;
    proxy_set_header   X-Real-IP        $remote_addr;
    proxy_pass  http://manage;
}

#安装以来
cnpm install

#启动服务
npm run dev

#编译打包
npm run build
