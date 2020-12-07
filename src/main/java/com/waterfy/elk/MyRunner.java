package com.waterfy.elk;

import com.waterfy.elk.business.FileBusiness;
import com.waterfy.elk.model.ElkQueryModel;
import com.waterfy.elk.service.ElkQueryService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Log
@Component
public class MyRunner implements CommandLineRunner {

    @Autowired
    FileBusiness fileBusiness;

    @Autowired
    ElkQueryService elkQueryService;

    @Override
    public void run(String... args) throws Exception {
        try {
            log.info("Starting application elk....");
            ElkQueryModel[] elkQueryModels = fileBusiness.readFileDb();
            log.info("BulkLoad db file insert....");
            for (int i = 0; i < elkQueryModels.length; i++){
                ElkQueryModel elkQueryModelToSave = new ElkQueryModel();
                elkQueryModelToSave.setQuery(elkQueryModels[i].getQuery());
                elkQueryModelToSave.setDatabase(elkQueryModels[i].getDatabase());
                elkQueryModelToSave.setLocalArquivo(elkQueryModels[i].getLocalArquivo());
                elkQueryModelToSave.setNomeIndex(elkQueryModels[i].getNomeIndex());
                elkQueryModelToSave.setTipoUpload(elkQueryModels[i].getTipoUpload());
                elkQueryModelToSave.setSchedule(elkQueryModels[i].getSchedule());
                elkQueryModelToSave.setInsertTime(elkQueryModels[i].getInsertTime());
                elkQueryModelToSave.setHeaders(elkQueryModels[i].getHeaders());
                elkQueryService.saveWithoutInsertTime(elkQueryModelToSave);
                log.info(String.format("Insert index %s ok.", elkQueryModelToSave.getQuery()));
            }
            log.info("BulkLoad db file end with success....");
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
