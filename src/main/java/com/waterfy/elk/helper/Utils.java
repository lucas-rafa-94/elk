package com.waterfy.elk.helper;

import com.waterfy.elk.model.ElkQueryModel;
import com.waterfy.elk.model.enums.BancoDeDadosEnum;
import com.waterfy.elk.model.enums.ScheduleEnums;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Service
public class Utils {

    @Value("${db.prod.url}")
    public String dbUrlProd;
    @Value("${db.prod.user}")
    public String dbUrlUserProd;
    @Value("${db.prod.pass}")
    public String dbUrlPassProd;

    @Value("${db.qa.url}")
    public String dbUrlQa;
    @Value("${db.qa.user}")
    public String dbUrlUserQa;
    @Value("${db.qa.pass}")
    public String dbUrlPassQa;

    @Value("${dir.logstash}")
    public String logstashDir;

    public String writeCabecalho(){
        return "input {\n";
    }

    public String writeFooter(){
        return "}\n" +
                "\n" +
                "output {\n" +
                "        elasticsearch {\n" +
                "                hosts => \"http://localhost:9200\"\n" +
                "                index => \"%{type}\"\n" +
                "                document_id => \"%{id}\"\n" +
                "        }\n" +
                "}";
    }

    public String scheduleToString(ScheduleEnums scheduleEnums){
        if (scheduleEnums.equals(ScheduleEnums.DEZMINUTOS)){
            return "*/10 * * * *";
        }else if(scheduleEnums.equals(ScheduleEnums.VINTEMINUTOS)){
            return "*/20 * * * *";
        }else if(scheduleEnums.equals(ScheduleEnums.TRINTAMINUTOS)){
            return "*/30 * * * *";
        }else if(scheduleEnums.equals(ScheduleEnums.UMAHORA)){
            return "0 * * * *";
        }else if(scheduleEnums.equals(ScheduleEnums.SEISHORAS)){
            return "0 */6 * * *";
        }else if(scheduleEnums.equals(ScheduleEnums.DIARIO)){
            return "0 */12 * * *";
        }else {
            return "0 */12 * * *";
        }
    }

    public String writeBodyQuery(ElkQueryModel elkQueryModel) {
        String retorno = "";
        if (elkQueryModel.getDatabase().name().equals(BancoDeDadosEnum.PRODUCAO.name())) {
            retorno = "jdbc {\n" +
                    "                jdbc_driver_library => \"/home/lucas_rfl_santos_94/postgresql-42.2.14.jar\"\n" +
                    "                jdbc_driver_class => \"org.postgresql.Driver\"\n" +
                    "                jdbc_connection_string => \"" + dbUrlProd + "\"\n" +
                    "                jdbc_user => \"" + dbUrlUserProd + "\"\n" +
                    "                jdbc_password => \"" + dbUrlPassProd + "\"\n" +
                    "                statement => \"" + elkQueryModel.getQuery() + "\"\n" +
                    "                schedule => \"" + scheduleToString(elkQueryModel.getSchedule()) + "\"\n" +
                    "                type => \"" + elkQueryModel.getNomeIndex().replace(" ", "_") + "\"\n" +
                    "        }\n";
        } else {
            retorno = "jdbc {\n" +
                    "                jdbc_driver_library => \"/home/lucas_rfl_santos_94/postgresql-42.2.14.jar\"\n" +
                    "                jdbc_driver_class => \"org.postgresql.Driver\"\n" +
                    "                jdbc_connection_string => \"" + dbUrlQa + "\"\n" +
                    "                jdbc_user => \"" + dbUrlUserQa + "\"\n" +
                    "                jdbc_password => \"" + dbUrlPassQa + "\"\n" +
                    "                statement => \"" + elkQueryModel.getQuery() + "\"\n" +
                    "                schedule => \"" + scheduleToString(elkQueryModel.getSchedule()) + "\"\n" +
                    "                type => \"" + elkQueryModel.getNomeIndex().replace(" ", "_") + "\"\n" +
                    "        }\n";
        }
        return retorno;
    }


    public String writeBodyCsv(ElkQueryModel elkQueryModel){
        String retorno = "file {\n" +
                        "                path => \"" +elkQueryModel.getLocalArquivo()+ "\"\n" +
                        "                start_position => \"beginning\"\n" +
                        "                sincedb_path => \"/dev/null\"\n" +
                        "        }\n" +
                        "        }\n" +
                        "filter {\n" +
                                "csv {\n" +
                        "                separator =>  \",\"\n" +
                        "                skip_header => \"true\"\n" +
                        "                columns => \"" +elkQueryModel.getHeaders()+ "\"\n" +
                        "        }\n";
        return retorno;
    }

    public String writeConfLogstash(List<ElkQueryModel> list){
        StringBuilder retorno = new StringBuilder();

        retorno.append(writeCabecalho());

        for (int i = 0; i < list.size(); i++){
            retorno.append(writeBodyQuery(list.get(i)));
        }

        retorno.append(writeFooter());

        return retorno.toString();

    }

    public String writeConfLogstashCsv(ElkQueryModel elkQueryModel){

        StringBuilder retorno = new StringBuilder();

        retorno.append(writeCabecalho());

        retorno.append(writeBodyCsv(elkQueryModel));

        retorno.append(writeFooter());

        return retorno.toString();

    }


    public String writeFileCsv(MultipartFile csv, String nomeIndex) throws Exception{
        try{
            byte[] bytes = csv.getBytes();
//            Path path = Paths.get("/home/lucas_rfl_santos_94/csv/" + sql.getResource().getFilename().replace(" ", "_"));
            Path path = Paths.get("/home/lucas_rfl_santos_94/csv/" + nomeIndex);
            Files.write(path, bytes);
            return path.toString();
        }catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    public void writeConf(String file){
        try {
//            Path path = Paths.get("/etc/logstash/conf.d/postgresql.conf");
            Path path = Paths.get(logstashDir + "/postgresql.conf");
            byte[] strToBytes = file.getBytes();
            Files.write(path, strToBytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void writeConfCsv(String nomeCsv, String file){
        try {
//            Path path = Paths.get("/etc/logstash/conf.d/postgresql.conf");
            Path path = Paths.get(logstashDir + nomeCsv.trim() + ".conf");
            byte[] strToBytes = file.getBytes();
            Files.write(path, strToBytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Async
    public void runRestarLogstash(){

        try {
            Process process = Runtime.getRuntime().exec("sh /home/lucas_rfl_santos_94/restart.sh");
            BufferedReader reader =
                    new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }

            int exitCode = process.waitFor();
            System.out.println("\nExited with error code : " + exitCode);

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    public boolean deleteQuery(String arquivo){
        File f = new File(arquivo);
        return f.delete();
    }
}
