package edu.mitin.performance.factory;

import com.google.gson.Gson;
import edu.mitin.performance.factory.Compiler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

@Component
public class Parser {
    @Value("${compilers.file}")
    private String jsonPath;

    public List<Compiler> getCompilersFromFile() {
        List<Compiler> compilerList = new LinkedList<>();
        try (FileReader is = new FileReader(new File(jsonPath))) {
            final Object object = new JSONParser().parse(is);
            JSONObject jsonObject = (JSONObject) object;
            JSONArray compilers = (JSONArray) jsonObject.get("compilers");
            compilers.forEach(o -> {
                final JSONObject json = (JSONObject) o;
                compilerList.add(new Gson().fromJson(json.toJSONString(), Compiler.class));
            });
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return compilerList;
    }
}
