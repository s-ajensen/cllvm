#include "specc.h"

__fun_list* __head = NULL; 
__fun_list* __tail = NULL;

size_t __pass_count = 0;
size_t __fail_count = 0;
size_t __fail_line = -1;
int __spec_result = -1;
void* __spec_expected_ptr = NULL;
void* __spec_actual_ptr = NULL;
char* __current_fmt = NULL;

void __fun_list_add(__function f) {
  if (!__head) {
    __head = (__fun_list*)malloc(sizeof(__fun_list));
    __head->f = f;
    __head->next = NULL;
    __tail = __head;
  } else {
    __tail->next = (__fun_list*)malloc(sizeof(__fun_list));
    __tail->next->f = f;
    __tail->next->next = NULL;
    __tail = __tail->next;
  }
}

void __printf_color(const char* color, const char * format, ...) {
  va_list args;
  va_start(args,format);
  printf("%s",color);
  vprintf(format,args);
  printf(SPECC_COLOR_CLEAR);
}

const char* __type_to_fmt(const char* type) {
  if      (strcmp("char",type) == 0)
    return "\'%c\'";
  else if (strcmp("int",type) == 0)
    return "%d";
  else if (strcmp("float",type) == 0)
    return "%f";
  else if (strcmp("unsigned int",type) == 0)
    return "%i";
  else if (strcmp("short",type) == 0)
    return "%hi";
  else if (strcmp("unsigned short",type) == 0)
    return "%hu";
  else if (strcmp("long",type) == 0)
    return "%ld";
  else if (strcmp("long double",type) == 0)
    return "%Lf";
  else if (strcmp("double",type) == 0)
    return "%lf";
  else if (strcmp("unsigned long",type) == 0)
    return "%lu";
  else if (strcmp("unsigned long long",type) == 0)
    return "%hu";
  else 
    return "%p";
}

void static printf_void(void* ptr) {
  if      (strcmp("\'%c\'",__current_fmt) == 0)
    printf(__current_fmt,*(char*)ptr);
  else if (strcmp("\"%s\"",__current_fmt) == 0)
    printf(__current_fmt,(char*)ptr);
  else if (strcmp("%d",__current_fmt) == 0)
    printf(__current_fmt,*(int*)ptr);
  else if (strcmp("%f",__current_fmt) == 0)
    printf(__current_fmt,*(float*)ptr);
  else if (strcmp("%i",__current_fmt) == 0)
    printf(__current_fmt,*(unsigned int*)ptr);
  else if (strcmp("%hi",__current_fmt) == 0)
    printf(__current_fmt,*(short*)ptr);
  else if (strcmp("%hu",__current_fmt) == 0)
    printf(__current_fmt,*(unsigned short*)ptr);
  else if (strcmp("%ld",__current_fmt) == 0)
    printf(__current_fmt,*(long*)ptr);
  else if (strcmp("%Lf",__current_fmt) == 0)
    printf(__current_fmt,*(long double*)ptr);
  else if (strcmp("%lf",__current_fmt) == 0)
    printf(__current_fmt,*(double*)ptr);
  else if (strcmp("%lu",__current_fmt) == 0)
    printf(__current_fmt,*(unsigned long*)ptr);
  else if (strcmp("%hu",__current_fmt) == 0)
    printf(__current_fmt,*(unsigned long long*)ptr);
  else
    printf(__current_fmt,ptr);
}

void __printf_expected_actual() {
  printf(" | Expected: ");
  printf_void(__spec_expected_ptr);
  printf(" Actual: ");
  printf_void(__spec_actual_ptr);
  printf("\n");
}

void static print_summary(size_t pass_count, size_t fail_count) {
  size_t total = pass_count + fail_count;
  printf("\nSpec Summary:\n");
  __printf_color(SPECC_COLOR_GREEN,"- %lu Passed\n",pass_count);
  __printf_color(SPECC_COLOR_RED,"- %lu Failed\n",fail_count);
  printf("- %lu Total\n",total);
}

void static call_flist(__fun_list* head) {
  __fun_list* cur = NULL; 
  for(cur = head; cur; cur = cur->next) {
    cur->f(); 
  }
}

void static free_flist(__fun_list* head) {
  if (head) {free_flist(head->next);}
  free(head);
}

int main() {
  call_flist(__head);
  free_flist(__head);
  print_summary(__pass_count,__fail_count);
}