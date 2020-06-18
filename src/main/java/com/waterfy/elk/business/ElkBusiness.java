package com.waterfy.elk.business;

import com.waterfy.elk.helper.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;


@Service
public class ElkBusiness {

    @Autowired
    Utils utils;

    public void startProcessIndexLogstashKibana(MultipartFile sql) {
        //Write SQL on Folder
        utils.writeFileSql(sql);

        //Write Conf Logstash
        utils.writeConf(utils.writeConfLogstash());

        //Run Comando Restart Logstash
        utils.runRestarLogstash();

    }

    public String [] retornoQueries(){
        return utils.listQueries();
    }

    public void deleteQuery(String arquivo){
            utils.deleteQuery(arquivo);
            utils.writeConf(utils.writeConfLogstash());
    }
}
