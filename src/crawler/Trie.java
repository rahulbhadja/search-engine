package crawler;

import java.io.Serializable;
import java.util.HashMap;

public class Trie<t> implements Serializable {

	public int size;			// size of Trie
	private Node<t> root;		// root of Trie
	
	public Trie()				// constructor to assign values for root and size of Trie
	{
		this.root = new Node<t>();
		this.size = 0;	
	}
	
	public void put(String key, t value)				// method to insert key value pair in Trie
	{
		HashMap<Character, Node<t>> children = this.root.children;
		Node<t> n = null;
		for(int i= 0;i<= (key.length()-1); ++i)			 
		{
			char ch= key.charAt(i);
			
			if(children.containsKey(ch))
			{
				n=children.get(ch);
			}
			else {
				n= new Node<t> (ch);
				children.put(ch,n);
			}
			
			if(i== (key.length()-1))
			{
				n.value = value;
			}
			
			children = n.children;
		}	
		this.size += 1;
	}

	
	public t searchWord(String word)			// method to search a word in the Trie and return page Index if word is present else null
	{
		HashMap<Character, Node<t>> children = this.root.children;
		Node<t> n =null;
		t pageIndex = null;
		
		for(int i = 0; i<= (word.length()-1); ++i)
		{
			char ch= word.charAt(i);
			
			if(children.containsKey(ch))
			{
				n = children.get(ch);
			}
			else
				return null;
			
			if(i == (word.length()-1))
					{
				pageIndex= n.value;
					}
			children = n.children;
		}
		return pageIndex;
		}
		
	}
	
