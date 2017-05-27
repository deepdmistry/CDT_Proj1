/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cache;

/**
 *
 * @author deep
 */

import java.io.*;
import java.lang.Math;
import java.lang.Integer;


public class FileRead {

          String bIndex, bTag, boffset = "";
       String[][] cache_body;
       static int assoc=2 ;
      static int  block_size=16;
      static int size=1024;
       int[][] guest;
       String[][] cache_body1;
       String mem=null;
       int flag=0;
       int L1_pseudo_assoc_EN;
       String traceline;
       int num_sets;
       int LRU[][];
       int readCount=0;
       int writeCount=0;
       int rhit_count = 0;
       int rmiss_count = 0;
       int whit_count = 0;
       int wmiss_count = 0;
       String rw;
       String L2ip;
       SimpleLRU ob1 = new SimpleLRU();
       L2_Cache ob8 = new L2_Cache();

    public void readFile(String name){

System.out.println("======= Simulation Configuration =======");
            System.out.println("Block size = " + block_size);
            System.out.println("L1_size = " + size);
            System.out.println("L1_assoc = " + assoc);


            System.out.println("Block size = " + ob8.L2_block_size);
            System.out.println("L2_size = " + ob8.L2_size);
            System.out.println("L2_assoc = " + ob8.L2_assoc);


        num_sets = size/(block_size*assoc);
        LRU = new int[num_sets][assoc];
        // Initialize the cache body


         cache_body = new String[num_sets][3*assoc];
         cache_body1 = new String[num_sets][3*assoc];
         for(int i=0; i<num_sets;i++)
         {
             for(int j=0;j<3*assoc;j++)
             {
                 cache_body[i][j] = "00000000000000000000000000000000";


             }
         }

         for(int i=0; i<num_sets;i++)
         {
             for(int j=0;j<3*assoc;j++)
             {
                 cache_body1[i][j] = "00000000000000000000000000000000";


             }
         }



         for(int i=0; i<num_sets;i++)
         {
             for(int j=0;j<assoc;j++)
             {
                 LRU[i][j]=0;
             }
         }

            // Read tracefile



    try{
    FileInputStream fstream = new FileInputStream("tracefile");
    // Get the object of DataInputStream
    DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

    //Read File Line By Line
    while ((traceline = br.readLine()) != null)   {
    System.out.println("Input: " + traceline);
     //System.out.println(traceline);
     rw = traceline.substring(0,1);
     mem = traceline.substring(2,traceline.length());




      // Hex to binary

    String[]hex={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
  String[]binary={"0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"};


  String hexValue= mem;
  String binValue="";
  for(int i=0;i<hexValue.length();i++)
  {
   char temp=hexValue.charAt(i);
   String temp2=""+temp+"";
   for(int j=0;j<hex.length;j++)
   {
    if(temp2.equalsIgnoreCase(hex[j]))
    {
     binValue=binValue+binary[j];
    }
   }
  }


  //calc the  # of bits of
  int num_bit_block = (int) (Math.log(block_size)/Math.log(2.0));
  int num_bit_index = (int) (Math.log(num_sets)/Math.log(2.0));
  int num_bit_tag = (int) (binValue.length()- num_bit_index - num_bit_block);
  //seperate out the memory into seperate blocks

   boffset = binValue.substring((int)(binValue.length() - num_bit_block),binValue.length());
   bIndex = binValue.substring((int)(binValue.length() -  num_bit_block -  num_bit_index) ,  (int)(binValue.length() - num_bit_block));
   bTag = binValue.substring(0, num_bit_tag);

   // System.out.println(bTag);


    // read OR write



    if(rw.equals("r"))
    {
        readCount++;
        System.out.println("L1: Read request to Address 0x" + mem + " mapped to set #"+ (int)binaryToDecimal(bIndex));
        if(L1_pseudo_assoc_EN ==0)
        {
                read1();
        }
        else {
            //Call assoc function here
                }
    }
    else if (rw.equals("w"))
    {
        writeCount++;
        System.out.println("L1: Write request to Address 0x" + mem + " mapped to set #"+ (int)binaryToDecimal(bIndex));
        if(L1_pseudo_assoc_EN ==0)
        {
                write1();
        }
        else {
            //Call assoc function here
                }
    }
    else {}


 }//while

            // convert the entire bin cache to hex and PRINT THE OUTPUT

for(int i=0;i<num_sets;i++){

    System.out.println("Set "+ i);

 for(int j=0;j<3*assoc;j++){

     long temp= 0;

temp =binaryToDecimal(cache_body[i][j]);

String temp1 =decimal2hex(temp);

//System.out.println(temp1);

cache_body1[i][j] = temp1;


System.out.println("L1_cache:"+cache_body1[i][j]);

 }
}


// PRINT THE LRU

for(int x=0; x<num_sets;x++)
         {
         System.out.println("Row " + x);
             for(int y=0;y<assoc;y++)
             {
                 System.out.println("LRU : " + LRU[x][y]);
             }
         }


// Print the number of HITS and MISSES


/*
// L2 Print ..


    // convert the entire bin cache to hex and PRINT THE OUTPUT



for(int i=0;i<num_sets;i++){

    System.out.println("Set "+ i);

 for(int j=0;j<3*ob8.L2_assoc;j++){

     long temp= 0;

temp = binaryToDecimal(ob8.L2_cache_body[i][j]);

String temp1 = decimal2hex(temp);

//System.out.println(temp1);

ob8.L2_cache_body1[i][j] = temp1;


System.out.println("L2_cache:"+ob8.L2_cache_body1[i][j]);

 }
}

// PRINT THE LRU

for(int x=0; x<num_sets;x++)
         {
         System.out.println("Row " + x);
             for(int y=0;y<ob8.L2_assoc;y++)
             {
                 System.out.println("L2_LRU : " + ob8.L2_LRU[x][y]);
             }
         }

*/
// Print the number of HITS and MISSES


System.out.println("No of L1 reads = " + readCount);
System.out.println("No of L1 read hits = " + rhit_count);
System.out.println("No of L1 read misses = " + rmiss_count);
System.out.println("No of L1 writes = " + writeCount);
System.out.println("No of L1 write hits = " + whit_count);
System.out.println("No of L1 write misses = " + wmiss_count);
/*
System.out.println("No of L2 reads = " + ob8.L2_readCount);
System.out.println("No of L2 read hits = " + ob8.L2_rhit_count);
System.out.println("No of L2 read misses = " + ob8.L2_rmiss_count);
System.out.println("No of L2 writes = " + ob8.L2_writeCount);
System.out.println("No of L2 write hits = " + ob8.L2_whit_count);
System.out.println("No of L2 write misses = " + ob8.L2_wmiss_count);

*/




        br.close();
        in.close();
        fstream.close();

    }catch(Exception e){
        e.printStackTrace();
    }

    }

    public void read1(){


try{



        int bool[]=new int[assoc];



    String tag_cmp="";

    for(int i=0;i<assoc;i++)
        {

        tag_cmp = cache_body[(int)binaryToDecimal(bIndex)][i];

 if(tag_cmp.equals(bTag))
   {

     bool[i]=1;
    }
 else
 {
               bool[i]=0;
 }

    }
int f=0;
for(int i=0;i<assoc;i++)
        {
            if(i==(assoc-1)){
            f=1;
            }

            if(bool[i]==1)
            {

                rhit(i);
                break;
            }
            else
            {
                if (f==1)
                    rmiss(i);

            }
}

if(flag ==1)
    {
        rhit_count++;
    }
    else if(flag == 2)
    {
        rmiss_count++;
    }
}
catch(NullPointerException e)
    {
         System.err.println("Error: " + e.getMessage());
               e.printStackTrace();

    }
    }


    public void write1() {


try{



        int bool[]=new int[assoc];


    int num_sets = size/(assoc*block_size);
    String tag_cmp="";

    for(int i=0;i<assoc;i++)
        {

        tag_cmp = cache_body[(int)binaryToDecimal(bIndex)][i];

 if(tag_cmp.equals(bTag))
   {

               bool[i]=1;
    }
 else
 {
               bool[i]=0;
 }

    }
int f=0;
for(int i=0;i<assoc;i++)
        {
            if(i==(assoc-1)){
            f=1;
            }

            if(bool[i]==1)
            {
                whit(i);
                break;
            }
            else
            {
                if (f==1)
                   wmiss(i);

            }
}

if(flag ==3)
    {
        whit_count++;
    }
    else if(flag == 4)
    {
        wmiss_count++;
    }
}
catch(NullPointerException e)
    {
         System.err.println("Error: " + e.getMessage());
               e.printStackTrace();

    }
    }



    public void rhit(int i)
            {
                 System.out.println("L1: Read Hit at address 0x"+mem );
     // increment hit count

        flag =1;

        int row=(int)binaryToDecimal(bIndex);

       // set valid bit
        cache_body[row][i+2*assoc] = "1";

     // set LRU array


        LRU1(i);
        //objects[i].Enqueue(Cache.cache_body[row][i]);


   }

   public void rmiss(int i)
   {

            System.out.println("L1 : Read miss at address 0x"+mem );
                // increment miss count
            flag=2;

            int row =    (int)binaryToDecimal(bIndex);

            int vi=0;

            for(int j=0;j<assoc;j++)
            {


            // check if there is any invalid block
                if(cache_body[row][j+2*assoc].equals("00000000000000000000000000000000"))
              {
                   vi=1;

    /*
           // Read L2..

                    L2ip = traceline;

                    String temp2 = ob8.L2_readFile(L2ip);

        // Write back 2 L1.
                cache_body[row][i] = temp2;
*/




       // set valid bit
          cache_body[row][j+2*assoc] = "1";

          cache_body[row][j] = bTag;

          //set LRU
                 

            LRU1(j);
 //objects[i].Enqueue(Cache.cache_body[row][i]);
                break;
                }

    else // no invalid block
        {

                vi=2;

            }
            } // for ends
// if the all blocks are valid
                if(vi==2){



                    // find LRU and replace tag


                //String temp = objects[i].Dequeue();
                for(int m=0;m<assoc;m++)
                {
                    if(LRU[row][m] == assoc-1)
                    {

                        // replace tag
                        cache_body[row][m] = bTag;
                         //set LRU
                               

                            LRU1(m);
                         // objects[i].Enqueue(Cache.cache_body[row][m]);

                    // set valid bit
                      cache_body[row][m+2*assoc] = "1";



                /*


                    // check if dirty or not
       if(cache_body[row][i+assoc].equals("00000000000000000000000000000000"))
       { // if clean

           // Call 2 L2..

                    L2ip = traceline;

                    String temp2 = ob8.L2_readFile(L2ip);

        // Write back 2 L1.
                cache_body[row][i] = temp2;

       }
       else // if dirty
       {

           // Construct the address to write back
           String temp = cache_body[row][i];
           String temp4 = temp.concat(bIndex);
           String temp5 =temp4.concat("0");
           Long temp6 = binaryToDecimal(temp5);
           String ip = decimal2hex(temp6);

           // write that address
           L2ip = swap1(ip);
           String temp3 = ob8.L2_readFile(L2ip);


           // Read L2

                    L2ip = traceline;

           String temp9 = ob8.L2_readFile(L2ip);

        // Write back 2 L1.
                cache_body[row][i] = temp9;

       }*/ // L1 comment

     }
   }
  }

}



   public void whit(int i)
            {


     System.out.println("L1 : Write hit at address 0x"+mem );
     // increment hit count
        flag =3;

        int row = (int)binaryToDecimal(bIndex);
         cache_body[row][i] = bTag;

         // set the dirty bit
         cache_body[row][i+assoc] = "1";

       // set valid bit
        cache_body[row][i+2*assoc] = "1";

     // set LRU array
        LRU1(i);
        //objects[i].Enqueue(Cache.cache_body[row][i]);
/*
   // chk if dirty or not
         if(cache_body[row][i+assoc].equals("00000000000000000000000000000000"))
       {
        //if clean replace directly

                cache_body[row][i] = bTag;
       }
       else // if dirty
       {

             // Construct the address to write back
           String temp = cache_body[row][i];
           String temp4 = temp.concat(bIndex);
           String temp5 =temp4.concat("0");
           Long temp6 = binaryToDecimal(temp5);
           String ip = decimal2hex(temp6);

              // write that address
           L2ip = ip;
           String temp3 = ob8.L2_readFile(L2ip);


           // Call 2 L2..
           L2ip = swap1(traceline);
            String temp2 = ob8.L2_readFile(L2ip);


        // Write 2 L1. after updating the value in L2
        cache_body[row][i] = temp2;

       }
*/

    }


    public void wmiss(int i)

   {


                System.out.println("L1: Write miss at address 0x"+mem );
            // increment miss count
                flag = 4;


            int row = (int)binaryToDecimal(bIndex);


            int vi=0;
            for(int j=0;j<assoc;j++)
     {

            // check if there is any invalid block
                if(cache_body[row][j+2*assoc].equals("00000000000000000000000000000000"))
              {
                vi=1;
             
/*
                    L2ip = swap1(traceline);
            String temp2 = ob8.L2_readFile(L2ip);


        // Write 2 L1. after updating the value in L2
        cache_body[row][i] = temp2;
*/


       // set the dirty bit
         cache_body[row][j+assoc] = "1";

       // set valid bit
        cache_body[row][j+2*assoc] = "1";


        cache_body[row][j] = bTag;
        //set LRU
                

        LRU1(j);
        //objects[i].Enqueue(Cache.cache_body[row][i]);
        break;
                }

    else // no invalid block
        {
            vi=2;
    }
            } // for

              if(vi == 2)
              {

                    // find LRU
            for(int m=0;m<assoc;m++)
            {

                //String temp = objects[i].Dequeue();

                    if(LRU[row][m] == assoc-1)
                    {

                        // replace tag
                        cache_body[row][m] = bTag;
                         //set LRU
                               

                         LRU1(m);
                //objects[i].Enqueue(Cache.cache_body[row][m]);

         // set valid bit
          cache_body[row][m+2*assoc] = "1";

                // set the dirty bit
         cache_body[row][m+assoc] = "1";


/*
                 // check if dirty or not
       if(cache_body[row][i+assoc].equals("00000000000000000000000000000000"))
       {
        // clean so .. replace direclty

                L2ip = swap1(traceline);
            String temp2 = ob8.L2_readFile(L2ip);


        // Write 2 L1. after updating the value in L2
        cache_body[row][i] = temp2;

        
       }
       else // dirty
       {
           // Construct the address to write back
           String temp = cache_body[row][i];
           String temp4 = temp.concat(bIndex);
           String temp5 =temp4.concat("0");
           Long temp6 = binaryToDecimal(temp5);
           String ip = decimal2hex(temp6);

           // write that address
           L2ip = swap1(ip);
           String temp3 = ob8.L2_readFile(L2ip);


           // Read L2

                    L2ip = traceline;

                    String temp2 = ob8.L2_readFile(L2ip);

        // Write back 2 L1.
                cache_body[row][i] = temp2;

       }*/



            }

         }


        }
    }
    

     public void LRU1(int i)
{

       
        int row = (int)binaryToDecimal(bIndex);
int a= LRU[row][i];
        for(int x=0; x<assoc;x++)
         {
     //System.out.println("before if");

int b= LRU[row][x];

     if(b<=a)
                 {
                                       LRU[row][x]++;
                                    //         System.out.println("LRU arr x: - "+ x + LRU[row][x]);
                                  //   System.out.println("LRU arr i"+i+ LRU[row][i]);

                 }
       }
     LRU[row][i]=0;
   //  System.out.println("LRU arr : - " + LRU[row][0]);
    // System.out.println("LRU arr : - " + LRU[row][1]);
}

    




public static long binaryToDecimal(String binary) throws NumberFormatException {
  // Initialize result to 0
  long res = 0;

  // Do not continue on an empty string
  if (binary.isEmpty()) {
    throw new NumberFormatException("Empty string is not a binary number");
  }

  // Consider each digit in the string
  for (int i = 0; i < binary.length(); i++) {
    // Get the nth char from the right (first = 0)
    char n = binary.charAt(binary.length() - (i+1));

    // Check if it's a valid bit
    if ((n != '0') && (n != '1')) {
      // And if not, die horribly
      throw new NumberFormatException("Not a binary number");
    } else if (n == '1') {
      // Only add the value if it's a 1
      res += Math.round(Math.pow(2.0, i));
    }
  }

  return res;
}


    public static String decimal2hex(long d) {
        String digits = "0123456789ABCDEF";
        if (d == 0) return "0";
        String hex = "";
        while (d > 0) {
            int digit = (int) (d % 16);                // rightmost digit
            hex = digits.charAt(digit) + hex;  // string concatenation
            d = d / 16;
        }
        return hex;
    }

    public static String swap (String temp)
    {
        String res;
         if(temp.charAt(0) == '0')
        {
            String sub1 = "1";
            String sub2 = temp.substring(1, temp.length());
            res = sub1.concat(sub2);
        }
        else
        {
            String sub1 = "0";
            String sub2 = temp.substring(1, temp.length());
            res = sub1.concat(sub2);
        }
        return res;
    }

     public static String swap1 (String temp)
    {
        String res;
         if(temp.charAt(0) == 'w')
        {
            String sub1 = "r";
            String sub2 = temp.substring(1, temp.length());
            res = sub1.concat(sub2);
        }
        else
        {
            String sub1 = "w";
            String sub2 = temp.substring(1, temp.length());
            res = sub1.concat(sub2);
        }
        return res;
    }

    public static void main(String args[]){
       FileRead f=new FileRead();
       f.readFile("tracefile");
    }

}
