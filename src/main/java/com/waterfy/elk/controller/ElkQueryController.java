package com.waterfy.elk.controller;

import com.google.gson.Gson;
import com.waterfy.elk.business.ElkQueryBusiness;
import com.waterfy.elk.model.ElkQueryModel;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@Log
@RestController
@RequestMapping("/api/elk")
public class ElkQueryController {
    @Autowired
    ElkQueryBusiness elkQueryBusiness;

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @PostMapping("/query")
    public ResponseEntity<Object> saveQuery(
            @RequestBody ElkQueryModel elkQueryModel
            ){
        log.info(String.format("Request saveQuery | Payload : %s", new Gson().toJson(elkQueryModel)));
        ResponseEntity<Object> obj = elkQueryBusiness.saveQuery(elkQueryModel);
        log.info(String.format("Response saveQuery | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @PostMapping("/losgstash/restart")
    public ResponseEntity<Object> refreshLogstash(){
        log.info(String.format("Request refreshLogstash()"));
        ResponseEntity<Object> obj = elkQueryBusiness.refreshLogstash();
        log.info(String.format("Response refreshLogstash     | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @PutMapping("/query")
    public ResponseEntity<Object> updateQuery(
            @RequestBody ElkQueryModel elkQueryModel
    ){
        log.info(String.format("Request updateQuery | Payload : %s", new Gson().toJson(elkQueryModel)));
        ResponseEntity<Object> obj = elkQueryBusiness.updateQuery(elkQueryModel);
        log.info(String.format("Response updateQuery | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @DeleteMapping("/query/{idQuery}")
    public ResponseEntity<Object> deleteQuery(
            @PathVariable long idQuery
    ){
        log.info(String.format("Request deleteQuery | Payload : %s", new Gson().toJson(idQuery)));
        ResponseEntity<Object> obj = elkQueryBusiness.deleteQueryById(idQuery);
        log.info(String.format("Response updateQuery | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @PostMapping("/csv")
    public ResponseEntity<Object> saveCsv(
            @RequestParam String headers,
            @RequestParam String nomeIndex,
            @RequestParam MultipartFile arquivoCsv
    ){
        log.info(String.format("Request saveCsv | Payload : %s", new Gson().toJson(nomeIndex)));
        ResponseEntity<Object> obj = elkQueryBusiness.saveCsv(arquivoCsv,nomeIndex,headers);
        log.info(String.format("Response saveCsv | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @PutMapping("/csv")
    public ResponseEntity<Object> updateCsv(
            @RequestParam long id,
            @RequestParam String headers,
            @RequestParam String nomeIndex,
            @RequestParam MultipartFile arquivoCsv
    ){
        log.info(String.format("Request updateCsv | Payload : %s", new Gson().toJson(nomeIndex)));
        ResponseEntity<Object> obj = elkQueryBusiness.updateCsv(arquivoCsv,id,nomeIndex,headers);
        log.info(String.format("Response updateCsv | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @GetMapping("/query")
    public ResponseEntity<Object> findAllQuery(){
        log.info(String.format("Request findAllQuery()"));
        ResponseEntity<Object> obj = elkQueryBusiness.findAllQueries();
        log.info(String.format("Response findAllQuery() | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }

    @CrossOrigin(origins = "https://elk-dash.waterfy.net/")
    @GetMapping("/csv")
    public ResponseEntity<Object> findAllCsvs(){
        log.info(String.format("Request findAllCsvs()"));
        ResponseEntity<Object> obj = elkQueryBusiness.findAllCsvs();
        log.info(String.format("Response findAllCsvs() | Payload : %s", new Gson().toJson(obj.getBody())));
        return obj;
    }


}
