#include <assert.h>
#include <stdio.h>

#define describe(msg,test) printf("- %s\n",msg);\
                                                test

#define it(msg, test) printf("  - %s\n",msg);\
                                             test

#define should(assertion) if (assertion) {printf("    - passed!\n");} else {printf("    - failed!\n"); assert(assertion);}