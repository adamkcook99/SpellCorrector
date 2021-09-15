package spell;

import java.util.Random;

public class Trie implements ITrie{

    private final Node root;
    private int wordCount = 0;
    private int nodeCount = 1;
    private String hashWord;

    public Trie(){

        root = new Node();

    }


    @Override
    public void add(String word) {

        int level = 0;
        int length = word.length();
        int index = 0;

        Node advance = root;

        for (level = 0; level < length; level++) {
            index = word.charAt(level) - 'a'; //lower case

            if (advance.getChildren()[index] == null) { //if the node doesn't exsist
                advance.getChildren()[index] = new Node(); //new node
                nodeCount++;
            }
            advance = advance.getChildren()[index]; //advance to next node
        }

        if (advance.getValue() == 0) {

            wordCount++;
        }

        advance.incrementCount();

    }

    @Override
    public INode find(String word) {
        int level = 0;
        int length = word.length();
        int index = 0;

        Node advance = root;


        for (level = 0; level < length; level++) {
            index = word.charAt(level) - 'a'; //lower case


            if (advance.getChildren()[index] == null) { //if hasn't reached length and is reaching null
                return null;
            }

            advance = advance.getChildren()[index]; //advance to next node

        }

        if (advance.getValue() >= 1) {
            return advance;
        } else {
            return null;
        }

    }


    @Override
    public int getWordCount() {
        return wordCount;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }


    public Node getRoot() {
        return root;
    }


    @Override
    public String toString(){

        StringBuilder curWord = new StringBuilder();
        StringBuilder output = new StringBuilder();
        toString_Helper(root, curWord, output);
        return output.toString();
    }

    private void toString_Helper(Node n, StringBuilder curWord, StringBuilder output) {
        if (n.getValue() > 0) {
            //append the node's word to the output
            output.append(curWord.toString());
            output.append("\n");
        }

        for (int i = 0; i < n.getChildren().length; ++i){
            INode child = n.getChildren()[i];

            if (child != null){
                char childLetter = (char)('a' + i);
                curWord.append(childLetter);
                toString_Helper((Node) child, curWord, output);
                curWord.deleteCharAt(curWord.length() -1);

            }

        }
    }

    @Override
    public boolean equals(Object o) {

        if (o ==  null) {//is o-- null?
            return false;
        }
        if ( o == this) {//is o == this?
            return true;
        }
        if ( o.getClass() != this.getClass()) { // do this and o have the same class?
            return false;
        }

        Trie t = (Trie)o; //cast to trie for later commands

        //do this and t have the same wordCount and nodeCount?
        if (t.getNodeCount() != this.getNodeCount()) {
            return false;
        }
         if (t.getWordCount() != this.getWordCount()) {
             return false;
         }


        //recursion
        Node n1 = new Node();
        Node n2 = new Node();

        n1 = t.getRoot();
        n2 = this.getRoot();

       return equals_Helper(n1,n2);

    }

    private boolean equals_Helper(Node n1, Node n2) {
        if (n1 == null && n2!= null){
            return false;
        }
        if (n1 != null && n2 == null){
            return false;
        }


        if (!(n1 == null && n2 == null)) {
            if (n1.getValue() == n2.getValue()) { //do n1 and n2 have the same count?

                for (int i = 0; i < 26; i++) {
                    //recurse on the children and compare the child subtrees

                    if (! equals_Helper(n1.getChildren()[i], n2.getChildren()[i])) {
                        return false;
                    }
                }

            } else {
                return false;
            }
        }
        return true;
    }

    @Override
    public int hashCode(){
        //int start = 7;
        int hash = 6;

        for (int i = 0; i < 26; i++) {

            if (root.getChildren()[i] != null) {
                hash = i * wordCount * nodeCount;
                break;
            }
        }

        return hash;



    }

}

