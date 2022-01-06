package cs146F20.tang.project4;
import static org.junit.Assert.*;
import java.io.*;

import org.junit.Test;


public class RBTTester {

	
	@Test
    //Test the Red Black Tree
	public void test() {
		RedBlackTree rbt = new RedBlackTree();
        rbt.insert("D");
        rbt.insert("B");
        rbt.insert("A");
        rbt.insert("C");
        rbt.insert("F");
        rbt.insert("E");
        rbt.insert("H");
        rbt.insert("G");
        rbt.insert("I");
        rbt.insert("J");
        assertEquals("DBACFEHGIJ", makeString(rbt));
        String str=     "Color: 1, Key:D Parent: \n"+
                        "Color: 1, Key:B Parent: D\n"+
                        "Color: 1, Key:A Parent: B\n"+
                        "Color: 1, Key:C Parent: B\n"+
                        "Color: 1, Key:F Parent: D\n"+
                        "Color: 1, Key:E Parent: F\n"+
                        "Color: 0, Key:H Parent: F\n"+
                        "Color: 1, Key:G Parent: H\n"+
                        "Color: 1, Key:I Parent: H\n"+
                        "Color: 0, Key:J Parent: I\n";
		assertEquals(str, makeStringDetails(rbt));
            
    }
    
    
    //tester for spell checker
	@Test
	public void spellChecker() {
		RedBlackTree rbt = new RedBlackTree();
		int counter = 0; // keeps track of number of words missed
		int numWords = 0; // keeps track of number of words in the poem
		
		try {
			
			// bufferedreader for dictionary
			BufferedReader dictionaryRead = new BufferedReader(new FileReader("dictionary.txt"));
			String dictLine;
			
			long start = System.nanoTime(); // start timer
			while((dictLine = dictionaryRead.readLine()) != null) { // read until the end of the file
				rbt.addNode(dictLine);  // add the node to the RBT
			}
			long time = System.nanoTime() - start; // end the timer
			System.out.println("Nanoseconds to create dictionary with RBTree: " + time); // prints time with description
			
			// bufferedreader for the poem
			BufferedReader poemReader = new BufferedReader(new FileReader("poem1.txt"));
			
			
			String stringLine;
			
			start = System.nanoTime(); // start timer
			while((stringLine = poemReader.readLine()) != null) { // read until the end of the file
				String[] arrayLine = stringLine.split(" "); // split each liine by spaces into an array
				for(String x : arrayLine) {
					numWords++;
					if(rbt.lookup(x) == null) // if the String is not in the tree dictionary, add to the counter
						counter++;
				}
			}
			time = System.nanoTime() - start; // end timer
			System.out.println("Nanoseconds to spellcheck poem with dictionary RBTree: " + time); // prints time with description
			
			System.out.println("Number of words not found: " + counter);
			System.out.println("Number of words found: " + (numWords - counter));
			System.out.println("Number of words in poem: " + numWords);
			
			dictionaryRead.close();
			poemReader.close();
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		
	}
    
    public static String makeString(RedBlackTree t)
    {
       class MyVisitor implements RedBlackTree.Visitor {
          String result = "";
          public void visit(RedBlackTree.Node n)
          {
             result = result + n.key;
          }
       };
       MyVisitor v = new MyVisitor();
       t.preOrderVisit(v);
       return v.result;
    }

    public static String makeStringDetails(RedBlackTree t) {
    	{
    	       class MyVisitor implements RedBlackTree.Visitor {
    	          String result = "";
    	          public void visit(RedBlackTree.Node n){
    	        	  
    	        	  if(n.parent == null) {
    	        		  result = result + "Color: " + n.color + ", Key:" + n.key + " Parent: \n";
    	        	  }
    	        	  else if(!(n.key).equals(""))
    	        		  result = result +"Color: "+n.color+", Key:"+n.key+" Parent: "+n.parent.key+"\n";
    	             
    	          }
    	       };
    	       MyVisitor v = new MyVisitor();
    	       t.preOrderVisit(v);
    	       return v.result;
    	 }
    }    
 }
  
