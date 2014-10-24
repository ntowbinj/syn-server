import java.util.*;

public class Trie<T>{


    T node = null;
    HashMap<Character, Trie<T>> children = new HashMap<>();

    public T put(String word, T node){
        return put(word, node, 0);
    }

    private T put(String word, T node, int i){
        if(i == word.length()){
            this.node = node;
            return this.node;
        }
        else{
            char c = word.charAt(i);
            Trie<T> child;
            if(!children.containsKey(c)){
                children.put(c, new Trie<T>());
            }
            child = children.get(c);
            return child.put(word, node, i+1);
        }
    }

    public boolean containsKey(String word){
        return this.get(word) != null;
    }

    public boolean remove(T n){
        if(this.node != null && this.node.equals(n)){
            node = null;
            return true;
        }
        else{
            for(Character c: children.keySet()){
                if(children.get(c).remove(n))
                    return true;
            }
            return false;
        }
    }

    public T get(String word){
        return get(word, 0);
    }

    private T get(String word, int i){
        if(i == word.length()){
            return this.node;
        }
        else{
            if(children.containsKey(word.charAt(i))){
                Trie<T> child = children.get(word.charAt(i));
                return child.get(word, i+1);
            }
            else
                return null;
        }
    }

    public Trie<T> getFirst(){
        if(this.node != null)
            return this;
        else{
            for(Character c : children.keySet()){
                Trie<T> result = children.get(c).getFirst();
                if(result != null)
                    return result;
            }
            return null;
        }
    }
            
    public String toString(){
        StringBuffer ret = new StringBuffer();
        if(this.node != null){
            ret.append(this.node.toString() + "\n");
        }
        for(char k: children.keySet()){
            ret.append(children.get(k).toString());
        }
        return ret.toString();
    }
}





        
            






