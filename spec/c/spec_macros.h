#include <assert.h>
#include <stdio.h>

extern size_t __pass_count;
extern size_t __fail_count;

void print_summary();

#define describe(msg,test) printf("- %s\n",msg);\
                                                {test}

#define it(msg, test) printf("  - %s\n",msg);\
                                             {test}

#define pass() __pass_count++
#define fail() __fail_count++; printf("    - [failed!] %s:%d\n",__FILE__,__LINE__)

#define should(assertion) if (assertion) {pass();} else {fail();}

#define should_not(assertion) if (assertion) {fail();} else {pass();}