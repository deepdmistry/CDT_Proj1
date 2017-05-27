#include <stdio.h>
#include <assert.h>
#include <string.h>


///////////////////////////////////////////////////////////////////////////
// If return value is >0, CACTI failed on this cache configuration.
// Always check the return value!
///////////////////////////////////////////////////////////////////////////
int get_cacti_results(unsigned int SIZE, unsigned int BLOCKSIZE, unsigned int ASSOC, float *AccessTime, float *Energy, float *Area) 
{
	char command[128];
	FILE *pipe;

        char buffer[128];
	char *substring;
	float Height, Width;

	int errflag = 3;

	/////////////////////////////////////////////////////////
	// 1. Generate the cacti command.
	/////////////////////////////////////////////////////////
	if (ASSOC == (SIZE/BLOCKSIZE))
	   sprintf(command, "./cacti %d %d FA 45nm 1   2>&1", SIZE, BLOCKSIZE);	// fully-associative case
	else
	   sprintf(command, "./cacti %d %d %d 45nm 1   2>&1", SIZE, BLOCKSIZE, ASSOC);

	/////////////////////////////////////////////////////////
	// 2. Execute cacti, and create a pipe between
	//    this process and the cacti process.
	/////////////////////////////////////////////////////////
	pipe = popen(command, "r");
	assert(pipe);

	/////////////////////////////////////////////////////////
	// 3. Extract the key results from cacti.
	//
	//    Format of key outputs from CACTI 6.0 (examples given):
	//
	//    Access time (ns): 0.24435
	//    Total dynamic read energy per access (nJ):0.0064104
	//    Cache height x width (mm): 0.402309 x 0.218135
	/////////////////////////////////////////////////////////

	while (fgets(buffer, 128, pipe)) {
	  if ((substring = strstr(buffer, "Access time"))) {
	     assert(substring = strstr(buffer, ":"));
	     sscanf(substring, ": %f", AccessTime);
	     //printf("%s", buffer);
	     errflag--;
	  }
	  else if ((substring = strstr(buffer, "Total dynamic read energy per access"))) {
	     assert(substring = strstr(buffer, ":"));
	     sscanf(substring, ":%f", Energy);
	     //printf("%s", buffer);
	     errflag--;
	  }
	  else if ((substring = strstr(buffer, "Cache height x width"))) {
	     assert(substring = strstr(buffer, ":"));
	     sscanf(substring, ": %f x %f", &Height, &Width);
	     *Area = Height*Width;
	     //printf("%s", buffer);
	     errflag--;
	  }
	}

	/////////////////////////////////////////////////////////
	// 4. Close the pipe.
	/////////////////////////////////////////////////////////
	pclose(pipe);

	return(errflag);
}


main(int argc, char *argv[]) {
	unsigned int SIZE, BLOCKSIZE, ASSOC;
	float AccessTime, Energy, Area;
	int errflag;

	///////////////////////////////////////////////////////////////////////////
	// Example #1
	///////////////////////////////////////////////////////////////////////////
	SIZE = 16384;
	BLOCKSIZE = 32;
	ASSOC = 4;

	errflag = get_cacti_results(SIZE, BLOCKSIZE, ASSOC, &AccessTime, &Energy, &Area);	

	printf("SIZE = %d, BLOCKSIZE = %d, ASSOC = %d:\n", SIZE, BLOCKSIZE, ASSOC);
	if (errflag) {
	   printf("\tERROR: CACTI gave up on this cache configuration.\n");
	}
	else {
	   printf("\tAccessTime = %f\n", AccessTime);
	   printf("\tEnergy     = %f\n", Energy);
	   printf("\tArea       = %f\n", Area);
	}

	///////////////////////////////////////////////////////////////////////////
	// Example #2
	///////////////////////////////////////////////////////////////////////////
	SIZE = 1024;
	BLOCKSIZE = 64;
	ASSOC = 16;	// small, fully-associative cache

	errflag = get_cacti_results(SIZE, BLOCKSIZE, ASSOC, &AccessTime, &Energy, &Area);	

	printf("SIZE = %d, BLOCKSIZE = %d, ASSOC = %d:\n", SIZE, BLOCKSIZE, ASSOC);
	if (errflag) {
	   printf("\tERROR: CACTI gave up on this cache configuration.\n");
	}
	else {
	   printf("\tAccessTime = %f\n", AccessTime);
	   printf("\tEnergy     = %f\n", Energy);
	   printf("\tArea       = %f\n", Area);
	}

	///////////////////////////////////////////////////////////////////////////
	// Example #3
	///////////////////////////////////////////////////////////////////////////
	SIZE = 65536;
	BLOCKSIZE = 16;
	ASSOC = 8;

	errflag = get_cacti_results(SIZE, BLOCKSIZE, ASSOC, &AccessTime, &Energy, &Area);	

	printf("SIZE = %d, BLOCKSIZE = %d, ASSOC = %d:\n", SIZE, BLOCKSIZE, ASSOC);
	if (errflag) {
	   printf("\tERROR: CACTI gave up on this cache configuration.\n");
	}
	else {
	   printf("\tAccessTime = %f\n", AccessTime);
	   printf("\tEnergy     = %f\n", Energy);
	   printf("\tArea       = %f\n", Area);
	}

	///////////////////////////////////////////////////////////////////////////
	// Example #4
	///////////////////////////////////////////////////////////////////////////
	SIZE = 65536;
	BLOCKSIZE = 16;
	ASSOC = 4096;	// wow! large, fully-associative cache

	errflag = get_cacti_results(SIZE, BLOCKSIZE, ASSOC, &AccessTime, &Energy, &Area);	

	printf("SIZE = %d, BLOCKSIZE = %d, ASSOC = %d:\n", SIZE, BLOCKSIZE, ASSOC);
	if (errflag) {
	   printf("\tERROR: CACTI gave up on this cache configuration.\n");
	}
	else {
	   printf("\tAccessTime = %f\n", AccessTime);
	   printf("\tEnergy     = %f\n", Energy);
	   printf("\tArea       = %f\n", Area);
	}
}

