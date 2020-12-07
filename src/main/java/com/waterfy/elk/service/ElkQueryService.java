package com.waterfy.elk.service;

import com.waterfy.elk.model.ElkQueryModel;
import com.waterfy.elk.model.enums.BancoDeDadosEnum;
import com.waterfy.elk.model.enums.TipoUploadEnum;
import com.waterfy.elk.repository.ElkQueryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ElkQueryService {
    @Autowired
    ElkQueryRepository elkQueryRepository;

    public ElkQueryModel saveOrUpdate(ElkQueryModel elkQueryModel) throws Exception{
        try{
            elkQueryModel.setInsertTime(new Date());
            return elkQueryRepository.save(elkQueryModel);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public ElkQueryModel saveWithoutInsertTime(ElkQueryModel elkQueryModel) throws Exception{
        try{
            return elkQueryRepository.save(elkQueryModel);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<ElkQueryModel> findAll() throws Exception{
        try{
            return elkQueryRepository.findAll();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<ElkQueryModel> findByDatabase(BancoDeDadosEnum bancoDeDadosEnum) throws Exception{
        try{
            return elkQueryRepository.findByDatabase(bancoDeDadosEnum);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public List<ElkQueryModel> findByTipoUpload(TipoUploadEnum tipoUploadEnum) throws Exception{
        try{
            return elkQueryRepository.findByTipoUpload(tipoUploadEnum);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public ElkQueryModel findById(long id) throws Exception{
        try{
            return elkQueryRepository.findById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void deleteById(long id) throws Exception{
        try{
            elkQueryRepository.deleteById(id);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

}
