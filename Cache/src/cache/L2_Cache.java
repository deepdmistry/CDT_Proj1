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


public class L2_Cache {

          String L2_bIndex, L2_bTag, L2_boffset = "";
       String[][] L2_cache_body;
       static int L2_assoc=4 ;
      static int  L2_block_size=16;
      static int L2_size=8192;
       int[][] L2_guest;
       String[][] L2_cache_body1;
       String L2_mem=null;
       int L2_flag=0;
       int L2_pseudo_assoc_EN;
       int L2_num_sets;
       int L2_LRU[][];
       int L2_readCount=0;
       int L2_writeCount=0;
       int L2_rhit_count = 0;
       int L2_rmiss_count = 0;
       int L2_whit_count = 0;
       int L2_wmiss_count = 0;
       String L2_rw;
       SimpleLRU L2_ob1 = new SimpleLRU();
       String ret_val;

       
    public String L2_readFile(String L2_traceline){



        L2_num_sets = L2_size/(L2_block_size*L2_assoc);
        L2_LRU = new int[L2_num_sets][L2_assoc];
        // Initialize the cache body


         L2_cache_body = new String[L2_num_sets][3*L2_assoc];
         L2_cache_body1 = new String[L2_num_sets][3*L2_assoc];
         for(int i=0; i<L2_num_sets;i++)
         {
             for(int j=0;j<3*L2_assoc;j++)
             {
                 L2_cache_body[i][j] = "00000000000000000000000000000000";

             }
         }

         for(int i=0; i<L2_num_sets;i++)
         {
             for(int j=0;j<3*L2_assoc;j++)
             {
                 L2_cache_body1[i][j] = "00000000000000000000000000000000";


             }
         }



         for(int i=0; i<L2_num_sets;i++)
         {
             for(int j=0;j<L2_assoc;j++)
             {
                 L2_LRU[i][j]=0;
             }
         }





            // Read tracefile



    try{
        
     L2_rw = L2_traceline.substring(0,1);
     L2_mem = L2_traceline.substring(2,L2_traceline.length());




      // Hex to binary

    String[]hex={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
  String[]binary={"0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"};


  String hexValue= L2_mem;
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
  int num_bit_block = (int) (Math.log(L2_block_size)/Math.log(2.0));
  int num_bit_index = (int) (Math.log(L2_num_sets)/Math.log(2.0));
  int num_bit_tag = (int) (binValue.length()- num_bit_index - num_bit_block);
  //seperate out the memory into seperate blocks

   L2_boffset = binValue.substring((int)(binValue.length() - num_bit_block),binValue.length());
   L2_bIndex = binValue.substring((int)(binValue.length() -  num_bit_block -  num_bit_index) ,  (int)(binValue.length() - num_bit_block));
   L2_bTag = binValue.substring(0, num_bit_tag);

   // System.out.println(bTag);


    // read OR write



    if(L2_rw.equals("r"))
    {
        L2_readCount++;
        System.out.println("L1: Read request to Address 0x" + L2_mem + " mapped to set #"+ (int)binaryToDecimal(L2_bIndex));
        if(L2_pseudo_assoc_EN ==0)
        {
                L2_read1();
        }
        else {
            //Call assoc function here
                }
    }
    else if (L2_rw.equals("w"))
    {
        L2_writeCount++;
        System.out.println("L1: Write request to Address 0x" + L2_mem + " mapped to set #"+ (int)binaryToDecimal(L2_bIndex));
        if(L2_pseudo_assoc_EN ==0)
        {
                L2_write1();
        }
        else {
            //Call assoc function here
                }
    }
    else {}


    }catch(Exception e){
        e.printStackTrace();
    }
return ret_val;
    }

    public void L2_read1(){


try{



        int bool[]=new int[L2_assoc];



    String tag_cmp="";

    for(int i=0;i<L2_assoc;i++)
        {

        tag_cmp = L2_cache_body[(int)binaryToDecimal(L2_bIndex)][i];

 if(tag_cmp.equals(L2_bTag))
   {

     bool[i]=1;
    }
 else
 {
               bool[i]=0;
 }

    }
int f=0;
for(int i=0;i<L2_assoc;i++)
        {
            if(i==(L2_assoc-1)){
            f=1;
            }

            if(bool[i]==1)
            {

                L2_rhit(i);
                break;
            }
            else
            {
                if (f==1)
                    L2_rmiss(i);

            }
}

if(L2_flag ==1)
    {
        L2_rhit_count++;
    }
    else if(L2_flag == 2)
    {
        L2_rmiss_count++;
    }
}
catch(NullPointerException e)
    {
         System.err.println("Error: " + e.getMessage());
               e.printStackTrace();

    }
    }


    public void L2_write1() {


try{



        int bool[]=new int[L2_assoc];


    int num_sets = L2_size/(L2_assoc*L2_block_size);
    String tag_cmp="";

    for(int i=0;i<L2_assoc;i++)
        {

        tag_cmp = L2_cache_body[(int)binaryToDecimal(L2_bIndex)][i];

 if(tag_cmp.equals(L2_bTag))
   {

               bool[i]=1;
    }
 else
 {
               bool[i]=0;
 }

    }
int f=0;
for(int i=0;i<L2_assoc;i++)
        {
            if(i==(L2_assoc-1)){
            f=1;
            }

            if(bool[i]==1)
            {
                L2_whit(i);
                break;
            }
            else
            {
                if (f==1)
                   L2_wmiss(i);

            }
}

if(L2_flag ==3)
    {
        L2_whit_count++;
    }
    else if(L2_flag == 4)
    {
        L2_wmiss_count++;
    }
}
catch(NullPointerException e)
    {
         System.err.println("Error: " + e.getMessage());
               e.printStackTrace();

    }
    }



    public void L2_rhit(int i)
            {

        int row = (int)binaryToDecimal(L2_bIndex);
                System.out.println("L2 :Read Hit at address 0x"+L2_mem );
     // increment hit count

        L2_flag =1;

       // set valid bit
        L2_cache_body[row][i+2*L2_assoc] = "1";

        // set LRU
        LRU1(i);

         // return value to L1

         ret_val = L2_bTag;
   }

   public void L2_rmiss(int i)
   {
              System.out.println("L1 : Read miss at address 0x"+L2_mem );
                // increment miss count
            L2_flag=2;


            int row =  (int)binaryToDecimal(L2_bIndex);

            int vi=0;

            for(int j=0;j<L2_assoc;j++)
            {


            // check if there is any invalid block
                if(L2_cache_body[row][j+2*L2_assoc].equals("00000000000000000000000000000000"))
              {
                   vi=1;

           // set valid bit
          L2_cache_body[row][j+2*L2_assoc] = "1";
          //replace
          L2_cache_body[row][j] = L2_bTag;
          ret_val = L2_bTag;

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
                for(int m=0;m<L2_assoc;m++)
                {
                    if(L2_LRU[row][m] == L2_assoc-1)
                    {

                        // replace tag
                        L2_cache_body[row][m] = L2_bTag;
                         //set LRU


                            LRU1(m);
                         // objects[i].Enqueue(Cache.cache_body[row][m]);

                    // set valid bit
                      L2_cache_body[row][m+2*L2_assoc] = "1";

                      ret_val = L2_bTag;

                    }

                }
        }
   }



   public void L2_whit(int i)
            {


     System.out.println("L1 : Write hit at address 0x"+L2_mem );
     // increment hit count
        L2_flag =3;
        // replace
        int row = (int)binaryToDecimal(L2_bIndex);
         L2_cache_body[row][i] = L2_bTag;

         // set the dirty bit
         L2_cache_body[row][i+L2_assoc] = "1";

       // set valid bit
        L2_cache_body[row][i+2*L2_assoc] = "1";

     // set LRU array
                //L2_ob1.SimpleLRU1(i,L2_cache_body[row][i]);

        LRU1(i);
        //objects[i].Enqueue(Cache.cache_body[row][i]);
            ret_val=L2_bTag;

    }


    public void L2_wmiss(int i)

   {


                System.out.println("L1: Write miss at address 0x"+L2_mem );
            // increment miss count
                L2_flag = 4;


            int row = (int)binaryToDecimal(L2_bIndex);


            int vi=0;
            for(int j=0;j<L2_assoc;j++)
     {

            // check if there is any invalid block
                if(L2_cache_body[row][j+2*L2_assoc].equals("00000000000000000000000000000000"))
              {
                vi=1;
             

       // set the dirty bit
         L2_cache_body[row][j+L2_assoc] = "1";

       // set valid bit
        L2_cache_body[row][j+2*L2_assoc] = "1";

        // replace
        L2_cache_body[row][j] = L2_bTag;
        //set LRU
             LRU1(j);
         ret_val = L2_bTag;
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
            for(int m=0;m<L2_assoc;m++)
            {

                //String temp = objects[i].Dequeue();

                    if(L2_LRU[row][m] == L2_assoc-1)
                    {

                        // replace tag
                        L2_cache_body[row][m] = L2_bTag;
                         //set LRU
                            

                         LRU1(m);
                //objects[i].Enqueue(Cache.cache_body[row][m]);

         // set valid bit
          L2_cache_body[row][m+2*L2_assoc] = "1";

                // set the dirty bit
         L2_cache_body[row][m+L2_assoc] = "1";

ret_val= L2_bTag;
                }

            }

         }


        }


     public void LRU1(int i)
{


        int row = (int)binaryToDecimal(L2_bIndex);
int a= L2_LRU[row][i];
        for(int x=0; x<L2_assoc;x++)
         {
     //System.out.println("before if");

int b= L2_LRU[row][x];

     if(b<=a)
                 {
                                       L2_LRU[row][x]++;
                                    //         System.out.println("LRU arr x: - "+ x + LRU[row][x]);
                                  //   System.out.println("LRU arr i"+i+ LRU[row][i]);

                 }
       }
     L2_LRU[row][i]=0;
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

}