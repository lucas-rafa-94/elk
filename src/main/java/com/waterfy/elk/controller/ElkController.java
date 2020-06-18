package com.waterfy.elk.controller;


import com.waterfy.elk.business.ElkBusiness;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


@RestController
@RequestMapping("api/v1/")
public class ElkController {

    @Autowired
    ElkBusiness elkBusiness;

    @PostMapping("waterfy/elk/queries")
    public void createQueryLogstash(@RequestParam MultipartFile arquivoSql, @RequestParam String ambiente) throws Exception {
            elkBusiness.startProcessIndexLogstashKibana(arquivoSql, ambiente);
    }

    @GetMapping("waterfy/elk/queries")
    public void getQueriesLogstash(){

    }

    @DeleteMapping("waterfy/elk/queries")
    public void deleteQueryLogstash(){

    }
}
