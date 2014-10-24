import java.util.*;

public class TrieSet implements Set<String>{


    boolean isWord;
    HashMap<Character, TrieSet> children = new HashMap<>();

    public boolean add(String word){
        return add(word, 0);
    }

    private boolean add(String word, int i){
        if(i == word.length()){
            boolean was = this.isWord;
            this.isWord = true;
            return was;
        }
        else{
            char c = word.charAt(i);
            TrieSet child;
            if(!children.containsKey(c)){
                children.put(c, new TrieSet());
            }
            child = children.get(c);
            return child.add(word, i+1);
        }
    }

    public boolean contains(Object o){
        return contains((String) o, 0);
    }

    private boolean contains(String word, int i){
        if(i == word.length()){
            return this.isWord;
        }
        else{
            if(children.containsKey(word.charAt(i))){
                TrieSet child = children.get(word.charAt(i));
                return child.contains(word, i+1);
            }
            else
                return false;
        }
    }


    //TODO
    public boolean isEmpty(){
        return size()!=0;
    }
    public int size(){
        throw new UnsupportedOperationException();
    }
    public Object[] toArray(){
        throw new UnsupportedOperationException();
    }
    public Iterator<String> iterator(){
        throw new UnsupportedOperationException();
    }
    public void clear(){
        throw new UnsupportedOperationException();
    }
    public boolean removeAll(Collection<?> c){
        throw new UnsupportedOperationException();
    }
    public boolean retainAll(Collection<?> c){
        throw new UnsupportedOperationException();
    }
    public boolean addAll(Collection<? extends String> c){
        throw new UnsupportedOperationException();
    }
    public boolean containsAll(Collection<?> c){
        throw new UnsupportedOperationException();
    }
    public boolean remove(Object o){
        throw new UnsupportedOperationException();
    }
    public <T> T[] toArray(T[] a){
        throw new UnsupportedOperationException();
    }
    
    public static void main(String[] args){
        TrieSet t = new TrieSet();
        t.add("pigs");
        System.out.println(t.contains("pbgs"));
    }
}





        
            







