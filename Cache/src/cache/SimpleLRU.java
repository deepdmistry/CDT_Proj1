package cache;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author deep
 */

 import java.util.*;
public class SimpleLRU {





  private static final int MAX_ENTRIES = 50;

  private Map mCache = new LinkedHashMap(MAX_ENTRIES, .75F, true) {
    protected boolean removeEldestEntry(Map.Entry eldest) {
      return size() > MAX_ENTRIES;
    }
  };

  public void SimpleLRU1(int i, String tag) {
    
      String numberStr = String.valueOf(i);
      mCache.put(numberStr, tag);

      System.out.println("\rSize = " + mCache.size() + "\tCurrent value = " + i + "\tLast Value in cache = " + mCache.get(numberStr));
      try {
        Thread.sleep(10);
      } catch(InterruptedException ex) {
      }
    

    System.out.println("");
  }

  public static void main(String[] args) {
    //SimpleLRU ob1 = new SimpleLRU();
  //  ob1.SimpleLRU1();
  }
}

