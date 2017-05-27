package cache;
import java.io.*;

public class CactiRun {
/*
	public static void main(String args[]) {
		float results[]={0, 0, 0};
		int rc = getCactiResults(
			Integer.parseInt(args[0]),
			Integer.parseInt(args[1]),
			Integer.parseInt(args[2]),
			results);
		System.out.printf("%8s %4s %2s",
			(args[0]),
			(args[1]),
			(args[2]));
		if (rc!=0) {
			//System.out.printf("This run did not work\n");
			System.out.printf(" -\n");
		}else{
			System.out.printf(" %.4f %.4f %.4f\n",
				results[0], results[1], results[2]);
		}

	}
*/
	public static int getCactiResults (int size, int blocksize, int assoc, float results[]) {

		String s = null;
		String command = null;
		int rc = 3;

		if (assoc == size/blocksize) command = new String("./cacti "+size+" "+blocksize+" FA 45nm 1");
		else command = new String("./cacti "+size+" "+blocksize+" "+assoc+" 45nm 1");

		try {
			Process p = Runtime.getRuntime().exec(command);
			BufferedReader stdInput = new BufferedReader(new InputStreamReader(p.getInputStream()));
			BufferedReader stdError = new BufferedReader(new InputStreamReader(p.getErrorStream()));

			while ((s = stdError.readLine()) != null) {
				if (s.contains("Access time")) {
					assert (s.contains(":"));
					String[] splits = s.split(":");
					float accessTime = Float.parseFloat(splits[1].trim());
					results[0]=accessTime;
					//System.out.printf("Access time is %3.4f\n",accessTime);
					rc --;
				}
				else if (s.contains("Total dynamic read energy per access")) {
					assert (s.contains(":"));
					String[] splits = s.split(":");
					float energy = Float.parseFloat(splits[1].trim());
					results[1]=energy;
					//System.out.printf("Energy is %3.4f\n",energy);
					rc --;
				}
				else if (s.contains("Cache height x width")) {
					assert (s.contains(":"));
					String[] splits = s.split(":");
					assert (splits[1].contains("x"));
					String[] dimensions = splits[1].split("x");
					float area = Float.parseFloat(dimensions[0].trim()) * Float.parseFloat(dimensions[1].trim());
					//System.out.printf("Area is %3.4f\n",area);
					results[2]=area;
					rc --;
				}
			}
			return (rc);
		}

		catch (IOException e) {
                    e.printStackTrace();
			System.out.println("Caught Exception");
			return (rc);
        }


	}
   
}

