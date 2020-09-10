package com.waterfy.elk.helper;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
public class Utils {

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

    public String writeBody(String pathname){
        String retorno = "";

            retorno = "jdbc {\n" +
                    "                jdbc_driver_library => \"/home/lucas_rfl_santos_94/postgresql-42.2.14.jar\"\n" +
                    "                jdbc_driver_class => \"org.postgresql.Driver\"\n" +
                    "                jdbc_connection_string => \"jdbc:postgresql://35.208.103.135:6432/gmas-prod\"\n" +
                    "                jdbc_user => \"customerfy\"\n" +
                    "                jdbc_password => \"Customerfy@2020#\"\n" +
                    "                statement_filepath => \"/home/lucas_rfl_santos_94/queries/" +pathname+ "\"\n" +
                    "                schedule => \"*/2 * * * *\"\n" +
                    "                use_column_value => true \"\n" +
                    "                tracking_column => \"id\"\n" +
                    "                type => \""+ new String(pathname.replace(".sql", "")+"_prod")+ "\"\n" +
                    "        }\n";

        return retorno;
    }

    public String [] listQueries(){
        String[] pathnames;
        File f = new File("/home/lucas_rfl_santos_94/queries");
        return f.list();
    }

    public void deleteQuery(String arquivo){
        String[] pathnames;
        File f = new File("/home/lucas_rfl_santos_94/queries");
        pathnames = f.list();

        for(int i = 0; i < pathnames.length; i++){
            if (arquivo.equals(pathnames[i])){
                File fDel = new File("/home/lucas_rfl_santos_94/queries/" + arquivo);
                fDel.delete();
            }
        }
    }

    public String writeConfLogstash(){
        StringBuilder retorno = new StringBuilder();

        retorno.append(writeCabecalho());

        String[] pathnames;
        File f = new File("/home/lucas_rfl_santos_94/queries");

        pathnames = f.list();

        for (String pathname : pathnames) {
            retorno.append(writeBody(pathname));
        }

        retorno.append(writeFooter());

        return retorno.toString();

    }

    public void writeFileSql(MultipartFile sql){
        try{
            byte[] bytes = sql.getBytes();
            Path path = Paths.get("/home/lucas_rfl_santos_94/queries/" + sql.getResource().getFilename().replace(" ", "_"));
            Files.write(path, bytes);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    public void writeConf(String file){
        try {
            Path path = Paths.get("/etc/logstash/conf.d/postgresql.conf");
            byte[] strToBytes = file.getBytes();
            Files.write(path, strToBytes);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

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
}
