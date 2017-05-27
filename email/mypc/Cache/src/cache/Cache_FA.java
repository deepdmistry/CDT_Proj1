/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package cache;

/**
 *
 * @author deep
 */
public class Cache_FA {
    int num_sets_fa=0;
    String[][] cache_body_fa;
    int block_size_fa = 0;
    int size_fa=0;
    public Cache_FA nextlevel_fa;

       
       
       String[][] cache_body1_fa;
       
       String traceline_fa;
       
       int LRU_fa[];
       String cold_fa[];
       int readCount_fa=0;
       int writeCount_fa=0;
       int rhit_count_fa = 0;
       int rmiss_count_fa = 0;
       int whit_count_fa = 0;
       int wmiss_count_fa = 0;
       int L2_readCount_fa=0;
       int L2_writeCount_fa=0;
       int write_backs_fa=0;
       int memaccesses_fa=0;
       int cold_count_fa=0;
       String rw_fa;
       public String name_fa;
       public Cache_FA L1,L2;
       int L1_pseudo_assoc_EN_fa=0;
       int miss_flag1=0;


 Cache_FA(int size1,int  block_size1){

       block_size_fa=block_size1;
         size_fa=size1;


System.out.println("======= Simulation Configuration =======");
            System.out.println("Block size = " + block_size_fa);
            System.out.println("L1_size_fa = " + size_fa);
            
        num_sets_fa = size_fa/block_size_fa;
        
        // Initialize the cache body


         cache_body_fa = new String[num_sets_fa][3];
         cache_body1_fa = new String[num_sets_fa][3];
         LRU_fa = new int[num_sets_fa];
         cold_fa = new String[100000];
         for(int i=0; i<num_sets_fa;i++)
         {
             for(int j=0;j<3;j++)
             {
             cache_body_fa[i][j] = "00000000000000000000000000000000";
             }
         }

          for(int i=0; i<num_sets_fa;i++)
         {
             for(int j=0;j<3;j++)
             {
             cache_body1_fa[i][j] = "00000000000000000000000000000000";
             }
         }

 for(int i=0; i<num_sets_fa;i++)
         {
            
                 LRU_fa[i]=i;

         }
         for(int i=0; i<100000;i++)
         {

                 cold_fa[i]="00000000000000000000000000000000";

         }
            nextlevel_fa=null;


    }

public int read1(String address){

        readCount_fa++;

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
  int num_bit_block = (int) (Math.log(block_size_fa)/Math.log(2.0));
  int num_bit_tag = (int) (binValue.length() - num_bit_block);
  //seperate out the memory into seperate blocks

   String boffset = binValue.substring((int)(binValue.length() - num_bit_block),binValue.length());
   String bTag = binValue.substring(0, num_bit_tag);
//System.out.println("L1: Read request to Address 0x" + address + " mapped to set #"+ (int)L1.binaryToDecimal(bIndex));
   

        for(int i = 0; i< num_sets_fa; i++)
        {
            if(bTag.equals(cache_body_fa[i][0]))
            {
                rhit(i,address);
                this.miss_flag1=0;
                return 1; //hit
             }
        }

                    rmiss(address,bTag);

        this.miss_flag1=1;
//System.out.println("FAAAFAFAAFFAFA" +(this.miss_flag1));


                    if(this.nextlevel_fa != null)
            this.nextlevel_fa.read1(address);
        else
            memaccesses_fa++;
  return 0;

}
// if write
    public int write1(String address) {



       writeCount_fa++;

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
  int num_bit_block = (int) (Math.log(block_size_fa)/Math.log(2.0));

  int num_bit_tag = (int) (binValue.length()- num_bit_block);
  //seperate out the memory into seperate blocks

   String boffset = binValue.substring((int)(binValue.length() - num_bit_block),binValue.length());
   
   String bTag = binValue.substring(0, num_bit_tag);
//System.out.println("L1: Write request to Address 0x" + address + " mapped to set #"+ (int)L1.binaryToDecimal(bIndex));
   
        for(int i = 0; i<num_sets_fa; i++)
        {
            if(bTag.equals(cache_body_fa[i][0]))
            {

                whit(i,address);
                this.miss_flag1=0;
                return 1; //hit
             }
        }

                   wmiss(address,bTag);

           this.miss_flag1=1;
         //  System.out.println("AAAAAAAAAAAA" +(this.miss_flag1));
   if(this.nextlevel_fa != null)
            this.nextlevel_fa.read1(address);
        else
            memaccesses_fa++;
  return 0;
}

// if read hit
    public void rhit(int i,String mem)  {
                 //System.out.println("L1: Read Hit at address 0x"+mem );
     // increment hit count

        rhit_count_fa++;

     // set LRU array
        LRU1(i);
    }

// if read miss
   public void rmiss(String mem,String bTag)  {


           int j=victim();
           // System.out.println("L1 : Read miss at address 0x"+mem );
            // check if there is any invalid block
                if(!(cache_body_fa[j][2].equals("00000000000000000000000000000000")))
              {
                    evict(j);
                }

            cache_body_fa[j][0] = bTag;

       // reset dirty bit

                cache_body_fa[j][1] = "00000000000000000000000000000000";

       // set valid bit
          cache_body_fa[j][2] = "1";

          //set LRU
          LRU1(j);
 // increament count
            rmiss_count_fa++;

   }
// if write hit
   public void whit(int i,String mem) {

    // System.out.println("L1 : Write hit at address 0x"+mem );
     // increment hit count
        rhit_count_fa++;

    
         // set the dirty bit

       cache_body_fa[i][1] = "1";
        LRU1(i);
   }
// if write miss
    public void wmiss(String mem,String bTag)  {
      // System.out.println("L1: Write miss at address 0x"+mem );
            // increment miss count
             wmiss_count_fa++;

             int j=victim();
           // System.out.println("L1 : Read miss at address 0x"+mem );
            // check if there is any invalid block
                if(!(cache_body_fa[j][2].equals("00000000000000000000000000000000")))
              {
                    evict(j);
                }

            cache_body_fa[j][0] = bTag;

       // set dirty bit

                   cache_body_fa[j][1] = "1";


       // set valid bit
          cache_body_fa[j][2] = "1";

          //set LRU
            LRU1(j);

             
         }
// find the block which we want to evict
     public int victim()  {

        int victim_index=0;
        for (int i=0; i<num_sets_fa; i++)
        {
            if(cache_body_fa[i][2].equals("00000000000000000000000000000000"))
            {
                victim_index = i;
                return victim_index;
            }
        }

        int maxLRU=0;
          for(int m=0;m<num_sets_fa;m++)
                {
                    if(LRU_fa[m] > maxLRU)
                    {
                        maxLRU = LRU_fa[m];
                        victim_index = m;
                    }
                }
return victim_index;
     }
// evict the block which is to be written upon
      public void evict(int j)  {
        int victim_mem;

            String tag = cache_body_fa[j][0];
           String temp5 =tag.concat("000000000");
           Long temp6 = binaryToDecimal(temp5);
           String address = decimal2hex(temp6);

             if(cache_body_fa[j][1].equals("1"))
             {
                 write_backs_fa++;


             if(this.nextlevel_fa != null){

                 this.nextlevel_fa.write1(address);
                    this.nextlevel_fa.writeCount_fa++;

             }
                 else{
                 memaccesses_fa++;

                 }


      cache_body_fa[j][1] = "00000000000000000000000000000000";

                }

      }
// update LRU
  public void LRU1(int i){


//int a= LRU[row][i];
        for(int x=0; x<num_sets_fa;x++)
         {
                if(LRU_fa[x]<LRU_fa[i])
                      LRU_fa[x]++;
       }
     LRU_fa[i]=0;
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
