package io.javabrains.wordsearchapi.services;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class WordGridService {


    public enum Directions{
        HORIZONTAL,
        VERTICAL,
        DIAGONAL,
        HORIZONTAL_INVERSE,
        VERTICAL_INVERSE,
        DIAGONAL_INVERSE
    }
    private class Coordinate{
        int x;
        int y;
        Coordinate(int x,int y){
            this.x = x;
            this.y = y;
        }
    }

    public char[][] generateGrid(int gridSize, List<String> words){
        List<Coordinate> coordinates= new ArrayList<>();
        char[][] contents = new char[gridSize][gridSize];
        for(int i = 0 ; i< gridSize ; i++) {
            for (int j = 0; j < gridSize; j++) {
                coordinates.add(new Coordinate(i,j));
                contents[i][j] = '_';
            }
        }
        for(String word:words){
            Collections.shuffle(coordinates);
            if(word.length() >= gridSize){
                continue;
            }
            for(Coordinate coordinate: coordinates){
                int x= coordinate.x;
                int y = coordinate.y;
                Directions selectedDirection = getDirectionForFit(contents,word, coordinate);
                if(selectedDirection!=null){
                    switch (selectedDirection) {
                        case HORIZONTAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y] = c;
                            }
                            break;
                        case VERTICAL:
                            for (char c : word.toCharArray()) {
                                contents[x][y++] = c;
                            }
                            break;
                        case DIAGONAL:
                            for (char c : word.toCharArray()) {
                                contents[x++][y++] = c;
                            }
                            break;
                        case HORIZONTAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y] = c;
                            }
                            break;
                        case VERTICAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x][y--] = c;
                            }
                            break;
                        case DIAGONAL_INVERSE:
                            for (char c : word.toCharArray()) {
                                contents[x--][y--] = c;
                            }
                            break;
                    }
                    break;
                }

            }
        }
        randomFill(contents);
        return contents;
    }
    private void randomFill(char[][] contents){
        int gridSize=contents.length;
        String allLetters="ABCDEFGHIJKLMNOPQRSTUVWXYZ";
        for(int i=0;i<gridSize;i++){
            for(int j=0;j<gridSize;j++){
                if(contents[i][j]=='_'){
                    int randomIndex= ThreadLocalRandom.current().nextInt(0,allLetters.length());
                    contents[i][j]=allLetters.charAt(randomIndex);
                }
            }
        }

    }
    private Directions getDirectionForFit(char[][] contents, String word, Coordinate coordinate){
        List<Directions> directions = Arrays.asList(Directions.values());
        Collections.shuffle(directions);
        for (Directions direction: directions ){
            if(doesFit(contents,word, coordinate, direction)){
                return direction;
            }
        }
        return null;
    }
    public boolean doesFit(char[][] contents, String word, Coordinate coordinate, Directions direction){
        int gridSize = contents[0].length;
        switch (direction){
            case HORIZONTAL:

                if(coordinate.x+word.length() > gridSize) return false;
                for( int i=0; i< word.length() ; i++){
                    char c = contents[coordinate.x+i][coordinate.y];
                    if('_' != contents[coordinate.x+i][coordinate.y] && word.charAt(i)!= c){ return false; }
                }
                break;
            case VERTICAL:
                if(coordinate.y+word.length()> gridSize){return  false; }
                for( int i=0; i< word.length() ; i++){
                    char c = contents[coordinate.x][coordinate.y+i ];
                    if('_' != contents[coordinate.x][coordinate.y+i ] && word.charAt(i) != c ){ return false; }
                }
                break;
            case DIAGONAL:
                if(coordinate.y+word.length()> gridSize || coordinate.x + word.length() > gridSize ){ return false;}
                for( int i=0; i< word.length() ; i++){
                    char c = contents[coordinate.x+i][coordinate.y + i];
                    if('_' != contents[coordinate.x+i][coordinate.y + i] && word.charAt(i)!=c ){ return false; }
                }
                break;
            case HORIZONTAL_INVERSE:
                if(coordinate.x < word.length() ) return false;
                for( int i=0; i< word.length() ; i++){
                    char c = contents[coordinate.x-i][coordinate.y];
                    if('_' != contents[coordinate.x-i][coordinate.y] && word.charAt(i)!=c ){ return false; }
                }
                break;
            case VERTICAL_INVERSE:
                if(coordinate.y< word.length() ){return  false; }
                for( int i=0; i< word.length() ; i++){
                    char c = contents[coordinate.x][coordinate.y-i ];
                    if('_' != contents[coordinate.x][coordinate.y-i ] && word.charAt(i)!=c){ return false; }
                }
                break;
            case DIAGONAL_INVERSE:
                if(coordinate.y< word.length() || coordinate.x < word.length() ){ return false;}
                for( int i=0; i< word.length() ; i++){
                    char c = contents[coordinate.x-i][coordinate.y - i];
                    if('_' != contents[coordinate.x-i][coordinate.y - i] && word.charAt(i)!=c ){ return false; }
                }
                break;
        }
        return true;
    }

    public  void displayGrid(char[][] contents){
        int gridSize = contents[0].length;
        for(int i = 0 ; i< gridSize ; i++) {
            for (int j = 0; j < gridSize; j++) {
                System.out.print(contents[i][j]+" ");
            }
            System.out.println("");
        }
    }


}
