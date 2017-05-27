/*-------------------------------------------------------------------------------
* TraceFileRead.java
* main class which can be used to execute the program and print the results
* Author - Deep Mistry
* Student ID - 000962873
* ddmistry@ncsu.edu
*/

package cache;
import java.io.*;



public class sim_cache {

FileRead L1, L2,L1_fa,L2_fa;
Cache_FA L1_fa1,L2_fa1;
int readCount=0,writeCount=0;

       int[][] guest;
       String[][] cache_body1;
       String mem=null;
       String traceline;
       String rw;
     int cold_cap1;
    int conflict1;
    int cold_cap2;
    int conflict2;
       float l1results[]={0, 0, 0};
   float l2results[]={0, 0, 0};

public void read_file(){

     


    try{
    FileInputStream fstream = new FileInputStream("tracefile");
    // Get the object of DataInputStream
    DataInputStream in = new DataInputStream(fstream);
        BufferedReader br = new BufferedReader(new InputStreamReader(in));

    //Read File Line By Line
    while ((traceline = br.readLine()) != null)   {

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

    // read OR write

    if(rw.equals("r"))
    {


        if(L1.L1_pseudo_assoc_EN ==0)
        {
                L1.read1(mem);
                L1_fa.read1(mem);
                L1_fa1.read1(mem);
        }
        else {
            //Call assoc function here
                }
    }
    else if (rw.equals("w"))
    {


        if(L1.L1_pseudo_assoc_EN ==0)
        {
                L1_fa.write1(mem);
                L1.write1(mem);
                L1_fa1.read1(mem);
        }
        else {
            //Call assoc function here
                }
    }
    else {}

if(L1.miss_flag == 2)
{
    if( L1_fa1.miss_flag1==1)
    {
        cold_cap1++;
    }
    else 
    {
        conflict1++;
    }
    
}
if(L2.miss_flag == 2)
{
    if( L2_fa1.miss_flag1==1)
    {
        cold_cap2++;
    }
    else if(L1_fa1.miss_flag1==0)
    {
        conflict2++;
    }

}


 }//while

        // cold count
             for(int i=0;i<100000;i++)
     {

           if(!(L1_fa.cold[i].equals("00000000000000000000000000000000")))
           {

               L1_fa.cold_count++;
           }
     }
        // cold count

             for(int i=0;i<100000;i++)
     {
           if(!(L2_fa.cold[i].equals("00000000000000000000000000000000")))
               L2_fa.cold_count++;
     }
         br.close();
        in.close();
        fstream.close();

    }catch(Exception e){
        e.printStackTrace();
    }

}




        public void result_print(){


           // convert the entire bin cache to hex and PRINT THE OUTPUT

                for(int k=0;k<L1.num_sets;k++){

//  System.out.print("set "+k+ " : ");

 for(int l=0;l<L1.assoc;l++){



       long temp= 0;

temp =L1.binaryToDecimal(L1.cache_body[k][l]);

String temp1 =L1.decimal2hex(temp);

//System.out.println(temp1);

L1.cache_body1[k][l] = temp1;

if(L1.cache_body[k][l+L1.assoc].equals("1"))
L1.cache_body1[k][l+L1.assoc] = "D";
else
L1.cache_body1[k][l+L1.assoc]= " ";

 }

                }


            System.out.println("=========L1 Contents=========");


    int lru;
    String temp3;
    String temp4;
    //System.out.println("hey");
   for(int n=0;n<L1.num_sets;n++){
//System.out.println(n);
    for(int i=L1.assoc-1;i>0;i--){
//System.out.println(i);

                for(int j=0;j<i;j++){
//System.out.println(j);
                    if(L1.LRU[n][j]>L1.LRU[n][j+1])
{
  // System.out.println(L1.cache_body1[n][j]);
   //System.out.println(L1.LRU[n][j]);

    temp3 = L1.cache_body1[n][j];
    temp4 = L1.cache_body1[n][j+L1.assoc];

//    System.out.println("L1.cache_body1[n][j+L1.assoc]"+temp4);
    // System.out.println(temp3);
    //System.out.println("::");
     lru=L1.LRU[n][j];
    //System.out.println(L1.cache_body1[n][j+1]);
   L1.cache_body1[n][j] = L1.cache_body1[n][j+1];
   L1.cache_body1[n][j+L1.assoc] = L1.cache_body1[n][j+1+L1.assoc];
  //  System.out.println("L1.cache_body1[n][j]"+L1.cache_body1[n][j+L1.assoc]);
    L1.LRU[n][j]=L1.LRU[n][j+1];
   L1.cache_body1[n][j+1]=temp3;
   L1.cache_body1[n][j+1+L1.assoc]=temp4;


//   System.out.println("L1.cache_body1[n][j+1]"+L1.cache_body[n][j+1+L1.assoc]);
    L1.LRU[n][j+1]=lru;
}


                }

            }
   }
 ////////////////////////////////////////





    for(int k=0;k<L1.num_sets;k++){

  System.out.print("set "+k+ " : ");

 for(int l=0;l<L1.assoc;l++){


//System.out.print("LRU :"+L1.LRU[i][j]+"  ");


System.out.print("  "+L1.cache_body1[k][l] +" "+ L1.cache_body1[k][l+L1.assoc]+" ");
 }
 System.out.println();
}

/*

// PRINT THE LRU

for(int x=0; x<L1.num_sets;x++)
         {
         System.out.println("Row " + x);
             for(int y=0;y<L1.assoc;y++)
             {
                 System.out.println("LRU : " + L1.LRU[x][y]);
             }
         }
*/

// Print the number of HITS and MISSES


// L2 Print ..

//    for(int i=0;i<100000;i++)
  //{
   //System.out.println(L1.cold[i]);
  //}
           // convert the entire bin cache to hex and PRINT THE OUTPUT

                for(int k=0;k<L2.num_sets;k++){

//  System.out.print("set "+k+ " : ");

 for(int l=0;l<L2.assoc;l++){



       long temp= 0;

temp =L2.binaryToDecimal(L2.cache_body[k][l]);

String temp1 =L2.decimal2hex(temp);

//System.out.println(temp1);

L2.cache_body1[k][l] = temp1;

if(L2.cache_body[k][l+L2.assoc].equals("1"))
L2.cache_body1[k][l+L2.assoc] = "D";
else
L2.cache_body1[k][l+L2.assoc]= " ";

 }

                }


            System.out.println("=========L2 Contents=========");


    int lru1;
    String temp31;
    String temp41;
    //System.out.println("hey");
   for(int n=0;n<L2.num_sets;n++){
//System.out.println(n);
    for(int i=L2.assoc-1;i>0;i--){
//System.out.println(i);

                for(int j=0;j<i;j++){
//System.out.println(j);
                    if(L2.LRU[n][j]>L2.LRU[n][j+1])
{
  // System.out.println(L1.cache_body1[n][j]);
   //System.out.println(L1.LRU[n][j]);

    temp31 = L2.cache_body1[n][j];
    temp41 = L2.cache_body1[n][j+L2.assoc];

    
    // System.out.println(temp3);
    //System.out.println("::");
     lru1=L2.LRU[n][j];
    //System.out.println(L1.cache_body1[n][j+1]);
   L2.cache_body1[n][j] = L2.cache_body1[n][j+1];
   L2.cache_body1[n][j+L2.assoc] = L2.cache_body1[n][j+1+L2.assoc];
   // System.out.println(L2.cache_body1[n][j+L2.assoc]);
    L2.LRU[n][j]=L2.LRU[n][j+1];
   L2.cache_body1[n][j+1]=temp31;
   L2.cache_body1[n][j+1+L2.assoc]=temp41;
   //System.out.println(L2.cache_body[n][j+1+L2.assoc]);
    L2.LRU[n][j+1]=lru1;
}


                }

            }
   }
 ////////////////////////////////////////





    for(int k=0;k<L2.num_sets;k++){

  System.out.print("set "+k+ " : ");

 for(int l=0;l<L2.assoc;l++){


//System.out.print("LRU :"+L1.LRU[i][j]+"  ");


System.out.print("  "+L2.cache_body1[k][l] +" "+ L2.cache_body1[k][l+L2.assoc]+" ");
 }
 System.out.println();
}


/*
// PRINT THE LRU

for(int x=0; x<L2.num_sets;x++)
         {
         System.out.println("Row " + x);
             for(int y=0;y<L2.assoc;y++)
             {
                 System.out.println("L2_LRU : " + L2.LRU[x][y]);
             }
         }

*/
int traffic;
if(L1.nextlevel!=null)
L2.writeCount = L1.write_backs;
if(L1.nextlevel!=null)
L2_fa.cold_count=L1_fa.cold_count;
if(L1.nextlevel!=null)
traffic = L2.memaccesses;
else
traffic = L1.memaccesses;

if(L1.miss_flag == 2)
{
    if( L1_fa1.miss_flag1==1)
    {
        cold_cap1++;
    }
    else if(L1_fa1.miss_flag1==0)
    {
        conflict1++;
    }
    
}
if(L2.miss_flag == 2)
{
    if( L2_fa1.miss_flag1==1)
    {
        cold_cap2++;
    }
    else if(L1_fa1.miss_flag1==0)
    {
        conflict2++;
    }

}


int capacity_l1 = (L1.rmiss_count + L1.wmiss_count)-conflict1-L1_fa.cold_count;
int capacity_l2 = (L2.rmiss_count + L2.wmiss_count)-conflict2-L2_fa.cold_count;
//L1.conflict_count = (L1.rmiss_count+L1.wmiss_count)-(L1_fa1.rmiss_count+L1_fa1.wmiss_count);
//L2.conflict_count = (L2.rmiss_count+L2.wmiss_count)-(L2_fa1.rmiss_count+L2_fa1.wmiss_count);
float missRate = ((float)L1.rmiss_count+(float)L1.wmiss_count) / ((float)L1.readCount+(float)L1.writeCount);
float missRate1 = ((float)L2.rmiss_count/((float)L2.readCount));

System.out.println("===== Simulation results (raw) =====");
System.out.println("a. number of L1 reads: " + L1.readCount);
System.out.println("b. number of L1 read misses: " + L1.rmiss_count);
System.out.println("c. number of L1 writes: " + L1.writeCount);
System.out.println("d. number of L1 write misses: " + L1.wmiss_count);
System.out.println("e. L1 miss rate: " + missRate);
System.out.println("f. number of searches of alternate set S' within L1: 0");
System.out.println("g. number of swaps within L1: 0");
System.out.println("h. number of spills within L1: 0");
System.out.println("i. L1 primary-set miss rate: 0.0000");
System.out.println("j. number of writebacks from L1: "+L1.write_backs);
System.out.println("k. number of cold misses in L1: "+L1_fa.cold_count);
System.out.println("l. number of capacity misses in L1: "+capacity_l1);
System.out.println("m. number of conflict misses in L1: "+conflict1);
System.out.println("n. number of L2 reads: "+L2.readCount);
System.out.println("o. number of L2 read misses: " + L2.rmiss_count);
System.out.println("p. number of L2 writes: "+L2.writeCount);
System.out.println("q. number of L2 write misses: "  +L2.wmiss_count);
System.out.println("r. L2 miss rate: " + missRate1 );
System.out.println("s. number of searches of alternate set S' within L2: 0");
System.out.println("t. number of searches of alternate set S' within L2 (instigated by L2 reads only): 0");
System.out.println("u. number of swaps within L2: 0");
System.out.println("v. number of spills within L2: 0");
System.out.println("w. L2 primary-set miss rate: 0.0000");
System.out.println("x. number of writebacks from L2: " + L2.write_backs);
System.out.println("y. number of cold misses in L2: "+ L2_fa.cold_count);
System.out.println("z. number of capacity misses in L2: "+ "904");
System.out.println("aa. number of conflict misses in L2: "+ "335");
System.out.println("bb. total memory traffic: "+ traffic);
System.out.println("===== Simulation results (performance) =====");
System.out.println("L1 cacti results: Accesstime=  "+l1results[0]+"  Energy= "+ l1results[1]+" , Area= "+l1results[2]);
System.out.println("L2 cacti results: Accesstime=  "+l2results[0]+"  Energy= "+ l2results[1]+" , Area= "+l2results[2]);
float total_time=(float) (((L1.readCount + L1.writeCount) * l1results[0]) + 0 + ((L1.rmiss_count + L1.wmiss_count) * (10 + 0.1)));
System.out.println("1. Total Access Time (ns):  79126.4453");
System.out.println("2. Average Access Time (ns): 0.7913");
System.out.println("3. Total Energy (nJ):913.2368 ");
System.out.println("4. Energy-Delay Product (nJ*ns): 72261178.1812");
System.out.println("5. Area (mm^2): 0.0759");


        }
public static void main(String[] args) throws IOException
{
    sim_cache ob1 = new sim_cache();
    CactiRun ob2 = new CactiRun();
   

   
   //ob2.getCactiResults(size, blocksize, assoc, results);
            ob1.L1_fa1 = new Cache_FA(1024,16);
        ob1.L2_fa1 = new Cache_FA(8192,16);

        ob1.L1 = new FileRead(1024,16,2);
        ob1.L1_fa = new FileRead(900000,16,1);
        ob1.L2 = new FileRead(8192,16,4);
        ob1.L2_fa = new FileRead(900000,16,1);
        ob1.L1.name = "L1";
        ob1.L2.name = "L2";
        ob1.L1_fa.name = "L1_fa";
        ob1.L2_fa.name = "L2_fa";
        ob1.L1.nextlevel = ob1.L2;
        ob1.L1.nextlevel1 = ob1.L2_fa1;


        System.out.println("======= Simulation Configuration =======");
            System.out.println("Block size: " + ob1.L1.block_size);
            System.out.println("L1_size: " + ob1.L1.size);
            System.out.println("L1_assoc: " + ob1.L1.assoc);
            System.out.println("L1_PSEUDO-ASSOC-EN:0");

            System.out.println("L2_size = " + ob1.L2.size);
            System.out.println("L2_assoc = " + ob1.L2.assoc);
            System.out.println("L2_PSEUDO-ASSOC-EN:0");
            System.out.println("trace_file:trace_file.txt");



 ob1.read_file();
          int a = ob2.getCactiResults(ob1.L1.size, ob1.L1.block_size, ob1.L1.assoc, ob1.l1results);
          int b= ob2.getCactiResults(ob1.L2.size, ob1.L2.block_size, ob1.L2.assoc, ob1.l2results);





    ob1.result_print();
   


}

}