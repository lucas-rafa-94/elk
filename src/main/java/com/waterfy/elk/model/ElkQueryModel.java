package com.waterfy.elk.model;

import com.waterfy.elk.model.enums.BancoDeDadosEnum;
import com.waterfy.elk.model.enums.ScheduleEnums;
import com.waterfy.elk.model.enums.TipoUploadEnum;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class ElkQueryModel {
    @Id
    @GeneratedValue
    private long id;
    private String query;
    private BancoDeDadosEnum database;
    private TipoUploadEnum tipoUpload;
    private String localArquivo;
    private ScheduleEnums schedule;
    private String nomeIndex;
    private String headers;
    private Date insertTime;
}
