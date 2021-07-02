package io.javabrains.wordsearchapi.controller;

import io.javabrains.wordsearchapi.services.WordGridService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController("/")
public class WordSearchController {
    @Autowired
    public WordGridService wordGridService;

    @GetMapping("/getgrid")
    public String createWordGrid(@RequestParam int gridSize,@RequestParam String wordsString){
        List<String> words= Arrays.asList(wordsString.split(","));
        char[][] grid=wordGridService.generateGrid(gridSize, words);
        StringBuilder sb=new StringBuilder();
        String str="";
        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                str+=grid[i][j]+" ";
            }
            str+="\r\n";
        }
        return str;
    }

}
