package spell;

public class Node implements INode {
    private int count; //how many times we have the same word
    private Node[] children; //array of children nodes


    public Node(){
        count = 0;
        children = new Node[26];
    }

    @Override
    public int getValue() {
        return count;
    }


    public void incrementCount(){
        count++;
    }


    @Override
    public void incrementValue() {

    }

    @Override
    public Node[] getChildren() {
        return children;
    }
}
