/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author deep
 */

import java.io.*;
import java.lang.Math;
import java.lang.Integer;


public class FileRead_FA {

       String[][] cache_body;
        int assoc ;
       int  block_size;
       int size;
       int[][] guest;
       String[][] cache_body1;
       int flag=0;
       int L1_pseudo_assoc_EN;
       String traceline;
       int num_sets;
       int LRU[][];
       String cold[];
       int readCount=0;
       int writeCount=0;
       int rhit_count = 0;
       int rmiss_count = 0;
       int whit_count = 0;
       int wmiss_count = 0;
       int L2_readCount=0;
       int L2_writeCount=0;
       int write_backs=0;
       int memaccesses=0;
       int cold_count=0;
       int conflict_count=0;
       String rw;
       String L2ip;
       public FileRead_FA nextlevel;
       public String name;
       public FileRead_FA L1,L2;


// Read a File and decide whether its a cache read / write
      FileRead_FA(int size1,int  block_size1,int assoc1){

          System.out.println("in constructor FA"+assoc1);
          assoc=assoc1;
       block_size=block_size1;
         size=size1;


System.out.println("======= Simulation Configuration =======");
            System.out.println("Block size = " + block_size);
            System.out.println("L1_size = " + size);
            System.out.println("L1_assoc = " + assoc);



        num_sets = size/(block_size*assoc);
        LRU = new int[num_sets][assoc];
        // Initialize the cache body


         cache_body = new String[num_sets][3*assoc];
         cache_body1 = new String[num_sets][3*assoc];
                  cold = new String[100000];
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
                 LRU[i][j]=assoc-j-1;
             }
         }

         for(int i=0; i<100000;i++)
         {
                 cold[i] = "00000000000000000000000000000000";
         }


            nextlevel=null;



    }
// if read
    public int read1(String address){



        readCount++;




      // Hex to binary

    String[]hex={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
  String[]binary={"0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"};


  String hexValue= address;
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
  
  int num_bit_tag = (int) (binValue.length() - num_bit_block);
  //seperate out the memory into seperate blocks

   String boffset = binValue.substring((int)(binValue.length() - num_bit_block),binValue.length());
   String bIndex = "00000000000000000000000000000000";//binValue.substring((int)(binValue.length() -  num_bit_block -  num_bit_index) ,  (int)(binValue.length() - num_bit_block));
   String bTag = binValue.substring(0, num_bit_tag);
//System.out.println("L1: Read request to Address 0x" + address + " mapped to set #"+ (int)L1.binaryToDecimal(bIndex));
   int row = (int)binaryToDecimal(bIndex);

        for(int i = 0; i< assoc; i++)
        {
            if(bTag.equals(cache_body[row][i]))
            {
                rhit(i,address,bIndex);
                return 1; //hit
             }
        }

                    rmiss(address,bIndex,bTag);
//cold miss
             for(int i=0;i<100000;i++)
     {
           if((!(cold[i].equals(address))))
           {
               if(cold[i].equals("00000000000000000000000000000000"))
               {cold[i]=address;
               break;}
           }
           else{
               break;
           }

     }

                    if(this.nextlevel != null)
            this.nextlevel.read1(address);
        else
            memaccesses++;
  return 0;

}
// if write
    public int write1(String address) {



       writeCount++;

      // Hex to binary

    String[]hex={"0","1","2","3","4","5","6","7","8","9","a","b","c","d","e","f"};
  String[]binary={"0000","0001","0010","0011","0100","0101","0110","0111","1000","1001","1010","1011","1100","1101","1110","1111"};


  String hexValue= address;

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
  
  int num_bit_tag = (int) (binValue.length()- num_bit_block);
  //seperate out the memory into seperate blocks

   String boffset = binValue.substring((int)(binValue.length() - num_bit_block),binValue.length());
   String bIndex = "00000000000000000000000000000000";//binValue.substring((int)(binValue.length() -  num_bit_block -  num_bit_index) ,  (int)(binValue.length() - num_bit_block));
   String bTag = binValue.substring(0, num_bit_tag);
//System.out.println("L1: Write request to Address 0x" + address + " mapped to set #"+ (int)L1.binaryToDecimal(bIndex));
   int row = (int)binaryToDecimal(bIndex);

        for(int i = 0; i< assoc; i++)
        {
            if(bTag.equals(cache_body[row][i]))
            {

                whit(i,address,bIndex);
                return 1; //hit
             }
        }

   wmiss(address,bIndex,bTag);
//cold miss
             for(int i=0;i<100000;i++)
     {
           if((!(cold[i].equals(address))))
           {
               if(cold[i].equals("00000000000000000000000000000000"))
               {cold[i]=address;
               break;}
           }
           else{
               break;
           }

     }

   if(this.nextlevel != null)
            this.nextlevel.read1(address);
        else
            memaccesses++;
  return 0;
}
// if read hit
    public void rhit(int i,String mem,String bIndex)  {
                 //System.out.println("L1: Read Hit at address 0x"+mem );
     // increment hit count

        rhit_count++;

     // set LRU array
        LRU1(i,bIndex);
    }
// if read miss
   public void rmiss(String mem,String bIndex,String bTag)  {

      int newRow=victim((int)binaryToDecimal(bIndex));
      int row =       (int)binaryToDecimal(bIndex);
           // System.out.println("L1 : Read miss at address 0x"+mem );
            // check if there is any invalid block
                if(!(cache_body[row][newRow+2*assoc].equals("00000000000000000000000000000000")))
              {

                    evict(row,newRow,bIndex);
                }

            cache_body[row][newRow] = bTag;

       // reset dirty bit

                cache_body[row][newRow+assoc] = "00000000000000000000000000000000";

       // set valid bit
          cache_body[row][newRow+2*assoc] = "1";

          //set LRU
            LRU1(newRow,bIndex);
 // increament count
            rmiss_count++;

            }
// if write hit
   public void whit(int i,String mem,String bIndex) {

    // System.out.println("L1 : Write hit at address 0x"+mem );
     // increment hit count
        rhit_count++;

        int row = (int)binaryToDecimal(bIndex);
         // set the dirty bit

       cache_body[row][i+assoc] = "1";
        LRU1(i,bIndex);
   }
// if write miss
    public void wmiss(String mem,String bIndex,String bTag)  {
      // System.out.println("L1: Write miss at address 0x"+mem );
            // increment miss count
             wmiss_count++;

int newRow=victim((int)binaryToDecimal(bIndex));

      int row =       (int)binaryToDecimal(bIndex);
            // check if there is any invalid block
                if(!(cache_body[row][newRow+2*assoc].equals("00000000000000000000000000000000")))
              {
                 evict(row,newRow,bIndex);
                }


            cache_body[row][newRow] = bTag;

       // set dirty bit

                   cache_body[row][newRow+assoc] = "1";


       // set valid bit
          cache_body[row][newRow+2*assoc] = "1";

          //set LRU
            LRU1(newRow,bIndex);

         }
// find the block which we want to evict
     public int victim(int row)  {

        int victim_index=0;
        for (int i=0; i<assoc; i++)
        {
            if(cache_body[row][i+2*assoc].equals("00000000000000000000000000000000"))
            {
                victim_index = i;
                return victim_index;
            }
        }

        int maxLRU=0;
          for(int m=0;m<assoc;m++)
                {
                    if(LRU[row][m] > maxLRU)
                    {
                        maxLRU = LRU[row][m];
                        victim_index = m;
                    }
                }
return victim_index;
     }
// evict the block which is to be written upon
      public void evict(int row,int newRow,String bIndex)  {
        int victim_mem;

        String tag = cache_body[row][newRow];
            String temp4 = tag.concat(bIndex);
           String temp5 =temp4.concat("0000");
           Long temp6 = binaryToDecimal(temp5);
           String address = decimal2hex(temp6);

             if(cache_body[row][newRow+assoc].equals("1"))
             {
                 write_backs++;


             if(this.nextlevel != null){

                 this.nextlevel.write1(address);
                    this.nextlevel.writeCount++;

             }
                 else{
                 memaccesses++;

                 }


      cache_body[row][newRow+assoc] = "00000000000000000000000000000000";

                }

      }
// update LRU
  public void LRU1(int i,String bIndex ){


        int row = (int)binaryToDecimal(bIndex);
int a= LRU[row][i];
        for(int x=0; x<assoc;x++)
         {


int b= LRU[row][x];

     if(b<a)
                 {
                      LRU[row][x]++;
                 }
       }
     LRU[row][i]=0;
}
// convert binary to decimal
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
// convert decimal to hex
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

}
