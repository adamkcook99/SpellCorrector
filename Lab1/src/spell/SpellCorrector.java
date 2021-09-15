package spell;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Locale;
import java.util.Scanner;

public class SpellCorrector implements ISpellCorrector {

    private final Trie trie = new Trie();
    private ArrayList<StringBuilder> words1Possiblities = new ArrayList<StringBuilder>(1);
    private ArrayList<StringBuilder> words2Possiblities = new ArrayList<StringBuilder>(1);

    @Override
    public void useDictionary(String dictionaryFileName) throws IOException {
        File file = new File(dictionaryFileName);
        Scanner sc = new Scanner(file);

        while (sc.hasNextLine()){ //adds words to Trie
            Scanner s2 = new Scanner(sc.nextLine());
            while (s2.hasNext()) {
                trie.add(s2.next());
            }
        }
    }


    public void deletion(String inputWord, int switcher){
        for (int i = 0; i < inputWord.length(); i++) {
            StringBuilder word = new StringBuilder(inputWord);
            word.deleteCharAt(i);

            if (switcher == 1) {
                words1Possiblities.add(word);
            } else if (switcher == 2){
                words2Possiblities.add(word);
            }

        }
    }

    public void transposition(String inputWord, int switcher){
        for (int i = 0; i < inputWord.length() -1; i++) {
            StringBuilder word = new StringBuilder(inputWord);
            char w = word.charAt(i);
            char s = word.charAt(i + 1);

            word.setCharAt(i, s);
            word.setCharAt(i +1, w);

            if (switcher == 1) {
                words1Possiblities.add(word);
            } else if (switcher == 2){
                words2Possiblities.add(word);
            }

        }
    }

    public void alteration(String inputWord, int switcher){
        for (int i = 0; i < inputWord.length(); i++) {

            for (char newLetter = 'a'; newLetter <= 'z'; newLetter++) {
                StringBuilder word = new StringBuilder(inputWord);
                word.setCharAt(i, newLetter);

                if (switcher == 1) {
                    words1Possiblities.add(word);
                } else if (switcher == 2){
                    words2Possiblities.add(word);
                }
            }


        }
    }

    public void insertion(String inputWord, int switcher){
        for (int i = 0; i < inputWord.length() +1; i++) {

            for (char newLetter = 'a'; newLetter <= 'z'; newLetter++) {
                StringBuilder word = new StringBuilder(inputWord);
                word.insert(i, newLetter);

                if (switcher == 1) {
                    words1Possiblities.add(word);
                } else if (switcher == 2){
                    words2Possiblities.add(word);
                }
            }
        }
    }


    @Override
    public String suggestSimilarWord(String untouchedInputWord) {

        //check if word is empty
        if (untouchedInputWord == null){
            return null;
        }

        //convert word to lowercase
        String inputWord = untouchedInputWord.toLowerCase(Locale.ROOT);

        ArrayList<StringBuilder> distance1words = new ArrayList<StringBuilder>(1);
        ArrayList<StringBuilder> distance2words = new ArrayList<StringBuilder>(1);

        //if word is spelled correctly
        if (trie.find(inputWord) != null){ //looking for word
            return inputWord;
        }

        //for switcher to know whih array to fill
        int fillwords1 = 1;
        int fillwords2 = 2;

        //deletion
        deletion(inputWord, fillwords1);

        //transpostion: switches letter places
        transposition(inputWord, fillwords1);

        //alteration: switched out letters
        alteration(inputWord, fillwords1);

        //insertion: adds letters
        insertion(inputWord, fillwords1);

        //fill suggested word for distance 1
        for (int i = 0; i < words1Possiblities.size(); i++) {
            StringBuilder word = new StringBuilder(inputWord);

            if (trie.find(words1Possiblities.get(i).toString()) != null){ //adds words that are in the dictonary
                distance1words.add(words1Possiblities.get(i));

            }
        }

        //fill distance 2 word possibilities
        for (int i = 0; i < words1Possiblities.size(); i ++) {
            //deletion
            deletion(words1Possiblities.get(i).toString(), fillwords2);

            //transpostion: switches letter places
            transposition(words1Possiblities.get(i).toString(), fillwords2);

            //alteration: switched out letters
            alteration(words1Possiblities.get(i).toString(), fillwords2);

            //insertion: adds letters
            insertion(words1Possiblities.get(i).toString(), fillwords2);
        }

        //fill distance two words
        for (int i = 0; i < words2Possiblities.size(); i++) {
            StringBuilder word = new StringBuilder(inputWord);

            if (trie.find(words2Possiblities.get(i).toString()) != null){ //if the word is in the dictonary

                if (!distance2words.contains(words2Possiblities.get(i).toString())) {//if the word isn't already there
                    distance2words.add(words2Possiblities.get(i));
                }


            }
        }

            //suggestor

        int max1 = 0;
        int max2 = 0;
        int max1Index = 0;
        int max2Index = 0;
        String suggestedWord;

        //which is found most in the distance1words?
        for (int i = 0; i < distance1words.size(); i++) {
            if (trie.find((distance1words.get(i)).toString()).getValue() > max1){ //if next word appears more times
                max1 =  trie.find((distance1words.get(i)).toString()).getValue(); //max number of appearances
                max1Index = i;

            }
        }

        //which is found most in the distance2words?
        for (int i = 0; i < distance2words.size(); i++) {
            if (trie.find((distance2words.get(i)).toString()).getValue() > max2){ //if next word appears more times
                max2 = trie.find((distance2words.get(i)).toString()).getValue(); //max number of appearances
                max2Index = i;

            }
        }

        //logic for which word to decide
        if(!distance1words.isEmpty()){
            suggestedWord = distance1words.get(max1Index).toString();
        } else if (!distance2words.isEmpty()){
            suggestedWord = distance2words.get(max2Index).toString();
        } else {
            return null;
        }

        //clear words so that next word doesn't get beaten by old words.
        words2Possiblities.clear();
        words1Possiblities.clear();

        return suggestedWord;
    }
}
