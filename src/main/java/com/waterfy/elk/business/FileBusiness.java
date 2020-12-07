package com.waterfy.elk.business;

import com.google.gson.Gson;
import com.waterfy.elk.model.ElkQueryModel;
import com.waterfy.elk.service.ElkQueryService;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;

@Log
@Service
public class FileBusiness {

    @Autowired
    ElkQueryService elkQueryService;

    @Async
    public void writeFileDb(){
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "db.json");
            String dbValues = new Gson().toJson(elkQueryService.findAll());
            Files.write(path, dbValues.getBytes(), StandardOpenOption.CREATE);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public boolean deleteFileDb(){
        try {
            File myObj = new File(System.getProperty("user.dir") + "/db.json");
            if (myObj.delete()){
                return true;
            }else{
                return false;
            }
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public ElkQueryModel[] readFileDb() throws Exception{
        try {
            Path path = Paths.get(System.getProperty("user.dir"), "db.json");
            byte[] data = Files.readAllBytes(path);
            return new Gson().fromJson(new String(data), ElkQueryModel[].class);
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }
}