#include "spec_macros.h"

size_t __pass_count = 0;
size_t __fail_count = 0;

void print_summary() {
  size_t total = __pass_count + __fail_count;
  printf("A total of %lu tests ran with %lu failures.",total,__fail_count);
}