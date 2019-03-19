package com.ywxt.Service.ServerManage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ywxt.Dao.ServerManage.ConfigManageRepository;
import com.ywxt.Domain.ServerManage.ConfigManage;
import com.ywxt.Domain.ServerManage.ServerInfo;
import com.ywxt.Utils.ExceptionUtil;
import com.ywxt.Utils.StreamUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class ConfigManageService {

    @Autowired
    private ConfigManageRepository configManageRepository;

    @Autowired
    private ServerInfoService serverInfoService;

    /**
     * 通过服务器id获取上传记录
     * @param serverId
     * @return
     */
    public List<ConfigManage> list(Long serverId) {
        return configManageRepository.findByServerIdOrderByCreateTimeDesc(serverId);
    }

    /**
     * 上传文件相关
     * @param file
     * @param id
     * @param fileType
     * @throws IOException
     */
    @Transactional
    public void upload(MultipartFile file, Long id, String fileType) throws IOException {

        String content = null;
        String fileName = file.getOriginalFilename();
        if(fileName.equals("https_proxy.conf")||fileName.equals("http_proxy.conf")){
            content = StreamUtil.convertStreamToString(file.getInputStream());
        }
        //获取服务器信息
        ServerInfo serverInfo = serverInfoService.getById(id);
        ExceptionUtil.isTrue(serverInfo==null,"服务器id不正确");
        String serverAddress = "http://"+serverInfo.getIp()+":8888/upload";
        //String version = autoVersion(id);

        // 获取文件后缀
        String prefix=fileName.substring(fileName.lastIndexOf("."));
        String name1 = fileName.substring(0,fileName.lastIndexOf("."));

        final File excelFile = File.createTempFile(name1, prefix);
        // MultipartFile to File
        file.transferTo(excelFile);


        ConfigManage configManage = new ConfigManage();
        configManage.setServerId(id);
        //configManage.setVersion(version);
        configManage.setFileName(file.getOriginalFilename());
        configManage.setFileType(fileType);
        configManage.setContent(content);
        configManage.setUpdateTime(new Date());
        configManage.setCreateTime(new Date());

        JSONObject jsonObject = sendPost(serverAddress,excelFile,fileType,fileName);
        ExceptionUtil.isTrue(jsonObject==null,"文件发送错误");
        //删除临时文件
        if (excelFile.exists()) {
            excelFile.delete();
        }
        if(jsonObject.get("status").equals("success")){
            configManage.setState(1);
        }else {
            configManage.setState(0);
            configManage.setRunResult(0);
        }
        configManage.setId(jsonObject.getString("code"));
        configManageRepository.saveAndFlush(configManage);

        //保存上传历史20条，超过20条删除
        List<ConfigManage> list = configManageRepository.findByServerIdOrderByCreateTimeDesc(id);

        if(list.size()>20){
            List<ConfigManage> temps = list.subList(19,list.size()-1);
            if(CollectionUtils.isNotEmpty(temps)){
                configManageRepository.deleteInBatch(temps);
            }
        }
    }

    //上传到服务器
    private JSONObject sendPost(String serverAddress, File file, String fileType, String name) {
        JSONObject jsonObject = null;
        try {
            CloseableHttpClient httpClient = HttpClients.createDefault();
            HttpPost uploadFile = new HttpPost(serverAddress);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
//            builder.addTextBody("field1", "yes", ContentType.TEXT_PLAIN);

            builder.addBinaryBody(
                    "fileName",
                    new FileInputStream(file),
                    ContentType.APPLICATION_OCTET_STREAM,
                    name
            );
            builder.addTextBody("fileType",fileType);
//            builder.addTextBody("id",id+"");

            HttpEntity multipart = builder.build();
            uploadFile.setEntity(multipart);
            CloseableHttpResponse response = httpClient.execute(uploadFile);
            HttpEntity responseEntity = response.getEntity();
            String sResponse= EntityUtils.toString(responseEntity, "UTF-8");
            System.out.println("Post 返回结果"+sResponse);
            jsonObject = JSON.parseObject(sResponse);
        }catch (Exception e){
            log.error("文件发送错误+{}" +e.getMessage());
        }
        return jsonObject;
    }

//    //上传文件到服务器
//    private String uploadToServer(InputStream inputStream, String fileName, String serverAddress) {
//        String code = "";
//        try {
//            URL url = new URL(serverAddress);
//            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
//            connection.setDoOutput(true);
//            connection.setDoInput(true);
//            connection.setUseCaches(false);
//            connection.setRequestMethod("POST");
//            connection.setRequestProperty("connection", "Keep-Alive");
//            connection.setRequestProperty("user-agent",
//                    "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
//            connection.setRequestProperty("Charsert", "UTF-8");
//            connection.setRequestProperty("Content-Type",
//                    "multipart/form-data");
//            connection.setRequestProperty("Content-Disposition",
//                    "form-data;filename="+"\""+fileName+"\"");
//            connection.setRequestProperty("Content-Type",
//                    "application/octet-stream");
//
//            OutputStream outputStream = new DataOutputStream(connection.getOutputStream());
//
//            DataInputStream in = new DataInputStream(inputStream);
//            int bytes = 0;
//            byte[] bufferOut = new byte[1024];
//            while ((bytes = in.read(bufferOut)) != -1) {
//                outputStream.write(bufferOut, 0, bytes);
//            }
//            in.close();
//            outputStream.flush();
//            outputStream.close();
//            //获取响应
//            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(
//                    connection.getInputStream(),"utf-8"));
//            while ((code = bufferedReader.readLine()) !=null){};
//            //ExceptionUtil.isTrue(!(code.equals("200")),"配置文件上传失败");
//        } catch (IOException e) {
//            log.error("{},上传配置文件失败"+serverAddress);
//            e.printStackTrace();
//        }
//        return code;
//    }

//    //产生版本号
//    private String autoVersion(Long serverId) {
//        String version = "1.0.0";
//        List<ConfigManage> configManages = list(serverId);
//        if(CollectionUtils.isNotEmpty(configManages)){
//            String[] temps = configManages.get(0).getVersion().split("\\.");
//            if(Integer.parseInt(temps[2])<99){
//                temps[2] = (Integer.parseInt(temps[2])+1)+"";
//            }else{
//                temps[2] ="0";
//                temps[1] = (Integer.parseInt(temps[1])+1)+"";
//            }
//
//            if(temps[1].equals("100")){
//                temps[1] ="0";
//                temps[0] = (Integer.parseInt(temps[1])+1)+"";
//            }
//            version = StringUtils.join(temps,".");
//        }
//        return version;
//    }

    //更新配置文件运行结果
    @Transactional
    public void updateState(Integer runResult,String id) {
        ConfigManage configManage = configManageRepository.getOne(id);
        ExceptionUtil.isTrue(configManage==null,"id不存在");
        configManage.setRunResult(runResult);
        configManage.setUpdateTime(new Date());
        configManageRepository.saveAndFlush(configManage);
    }
}
