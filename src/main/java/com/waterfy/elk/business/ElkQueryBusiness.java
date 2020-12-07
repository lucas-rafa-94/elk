package com.waterfy.elk.business;


import com.waterfy.elk.helper.Utils;
import com.waterfy.elk.model.ElkQueryModel;
import com.waterfy.elk.model.api.ResponseModel;
import com.waterfy.elk.model.enums.TipoUploadEnum;
import com.waterfy.elk.service.ElkQueryService;
import lombok.extern.java.Log;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;

@Log
@Service
public class ElkQueryBusiness {
    @Autowired
    ElkQueryService elkQueryService;

    @Autowired
    FileBusiness fileBusiness;

    @Autowired
    Utils utils;

    public ResponseEntity<Object> saveQuery(ElkQueryModel elkQueryModel){
        try {
            ElkQueryModel elkQueryModelSaved = elkQueryService.saveOrUpdate(elkQueryModel);
            if(fileBusiness.deleteFileDb()){
                fileBusiness.writeFileDb();
                return new ResponseEntity<>(
                        elkQueryModelSaved,
                        HttpStatus.OK
                );
            }else{
                fileBusiness.writeFileDb();
                return new ResponseEntity<>(
                        new ResponseModel("Arquivo não encontrado!"),
                        HttpStatus.BAD_REQUEST
                );
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<Object> saveCsv(MultipartFile csv,  String nomeIndex, String headers){
        try {
            ElkQueryModel elkQueryModel = new ElkQueryModel();

            elkQueryModel.setNomeIndex(nomeIndex);
            elkQueryModel.setHeaders(headers);
            elkQueryModel.setTipoUpload(TipoUploadEnum.CSV);
            elkQueryModel.setLocalArquivo(utils.writeFileCsv(csv, elkQueryModel.getNomeIndex()));

            ElkQueryModel elkQueryModelSaved = elkQueryService.saveOrUpdate(elkQueryModel);

            if(fileBusiness.deleteFileDb()){
                fileBusiness.writeFileDb();
                return new ResponseEntity<>(
                        elkQueryModelSaved,
                        HttpStatus.OK
                );
            }else{
                fileBusiness.writeFileDb();
                return new ResponseEntity<>(
                        new ResponseModel("Arquivo não encontrado!"),
                        HttpStatus.BAD_REQUEST
                );
            }


        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<Object> updateCsv(MultipartFile csv, long id,  String nomeIndex, String headers){
        try {
            ElkQueryModel elkQueryModel = elkQueryService.findById(id);

            if(elkQueryModel != null){

                elkQueryModel.setNomeIndex(nomeIndex);
                elkQueryModel.setHeaders(headers);
                elkQueryModel.setTipoUpload(TipoUploadEnum.CSV);
                elkQueryModel.setLocalArquivo(utils.writeFileCsv(csv, elkQueryModel.getNomeIndex()));

                ElkQueryModel elkQueryModelSaved = elkQueryService.saveOrUpdate(elkQueryModel);

                if(fileBusiness.deleteFileDb()){
                    fileBusiness.writeFileDb();
                    return new ResponseEntity<>(
                            elkQueryModelSaved,
                            HttpStatus.OK
                    );
                }else{
                    fileBusiness.writeFileDb();
                    return new ResponseEntity<>(
                            new ResponseModel("Arquivo não encontrado!"),
                            HttpStatus.BAD_REQUEST
                    );
                }

            }else {
                return new ResponseEntity<>(
                        new ResponseModel("Objeto não encontrado!"),
                        HttpStatus.BAD_REQUEST
                );
            }

        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<Object> updateQuery(ElkQueryModel elkQueryModel){
        try {
            if(elkQueryService.findById(elkQueryModel.getId()) != null){
                ElkQueryModel elkQueryModelSaved = elkQueryService.saveOrUpdate(elkQueryModel);
                if(fileBusiness.deleteFileDb()){
                    fileBusiness.writeFileDb();
                    return new ResponseEntity<>(
                            elkQueryModelSaved,
                            HttpStatus.OK
                    );
                }else{
                    fileBusiness.writeFileDb();
                    return new ResponseEntity<>(
                            new ResponseModel("Arquivo não encontrado!"),
                            HttpStatus.BAD_REQUEST
                    );
                }
            }else{
                return new ResponseEntity<>(
                        new ResponseModel("Query não encontrada!"),
                        HttpStatus.BAD_REQUEST
                );
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<Object> deleteQueryById(long id){
        try {
            ElkQueryModel elkQueryModel = elkQueryService.findById(id);
            if(elkQueryModel != null){
                if(elkQueryModel.getTipoUpload().name().equals(TipoUploadEnum.CSV.name())){
                    if(utils.deleteQuery(elkQueryModel.getLocalArquivo())){
                        elkQueryService.deleteById(id);
                        if(fileBusiness.deleteFileDb()){
                            fileBusiness.writeFileDb();
                            return new ResponseEntity<>(
                                    new ResponseModel("Deletado com sucesso!"),
                                    HttpStatus.OK
                            );
                        }else{
                            fileBusiness.deleteFileDb();
                            fileBusiness.writeFileDb();
                            return new ResponseEntity<>(
                                    new ResponseModel("Arquivo não encontrado!"),
                                    HttpStatus.BAD_REQUEST
                            );
                        }
                    }else {
                        return new ResponseEntity<>(
                                new ResponseModel("Erro ao deletar csv!"),
                                HttpStatus.BAD_REQUEST
                        );
                    }
                }else {
                    elkQueryService.deleteById(id);
                    fileBusiness.writeFileDb();
                    return new ResponseEntity<>(
                            new ResponseModel("Deletado com sucesso!"),
                            HttpStatus.OK
                    );
                }
            }else{
                return new ResponseEntity<>(
                        new ResponseModel("Objeto não encontrado!"),
                        HttpStatus.BAD_REQUEST
                );
            }
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<Object> findAllQueries(){
        try {
            return new ResponseEntity<>(
                    elkQueryService.findByTipoUpload(TipoUploadEnum.QUERY),
                    HttpStatus.OK
            );
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<Object> findAllCsvs(){
        try {
            return new ResponseEntity<>(
                    elkQueryService.findByTipoUpload(TipoUploadEnum.CSV),
                    HttpStatus.OK
            );
        }catch (Exception e){
            e.printStackTrace();
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }

    public ResponseEntity<Object> refreshLogstash(){
        try{
            List<ElkQueryModel> elkQueryModelsQuery =  elkQueryService.findByTipoUpload(TipoUploadEnum.QUERY);
            //Write Conf Logstash
            utils.writeConf(utils.writeConfLogstash(elkQueryModelsQuery));

            List<ElkQueryModel> elkQueryModelsCsv =  elkQueryService.findByTipoUpload(TipoUploadEnum.CSV);

            FileUtils.cleanDirectory(new File("/home/lucas_rfl_santos_94/csv/"));

            for (int i = 0; i < elkQueryModelsCsv.size(); i ++){
                utils.writeConfCsv(elkQueryModelsCsv.get(i).getNomeIndex(), utils.writeConfLogstashCsv(elkQueryModelsCsv.get(i)));
            }

            //Run Comando Restart Logstash
            utils.runRestarLogstash();

            return new ResponseEntity<>(
                    new ResponseModel("Refresh realizado com sucesso!"),
                    HttpStatus.OK
            );
        }catch (Exception e){
            return new ResponseEntity<>(
                    new ResponseModel(e.getMessage()),
                    HttpStatus.INTERNAL_SERVER_ERROR
            );
        }
    }
}
