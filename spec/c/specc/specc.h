#include <stdlib.h>
#include <stdio.h>
#include <stdarg.h>
#include <string.h>

typedef void (*__function)(void);

struct __fun_list { 
  struct __fun_list* next; 
  __function f;
};

typedef struct __fun_list __fun_list;

extern __fun_list* __head; 
extern __fun_list* __tail;

extern size_t __pass_count;
extern size_t __fail_count;
extern size_t __fail_line;
extern int __spec_result;
extern void* __spec_expected_ptr;
extern void* __spec_actual_ptr;
extern char* __current_fmt;

void __fun_list_add(__function f);
void __printf_color(const char* color,const char * format, ...);
const char* __type_to_fmt(const char* type);
void __printf_expected_actual();

void static print_summary(size_t pass_count, size_t fail_count);
void static call_flist(__fun_list* head);
void static free_flist(__fun_list* head);
void static printf_void(void* ptr);

#define SPECC_PASS 1
#define SPECC_FAIL 0

#define SPECC_COLOR_GREEN "\033[0;32m"
#define SPECC_COLOR_RED "\033[1;31m"
#define SPECC_COLOR_CLEAR "\033[0m"

#define module(f,tests) \
  static void f(void) {printf("[%s]\n",__func__); tests}\
  static void __attribute__((constructor)) __construct_##f(void) {\
      __fun_list_add(f);\
  }

#define describe(msg,test) printf("- %s\n",msg); {test}

#define pass() __spec_result = SPECC_PASS; __fail_line = -1
#define __fail() __spec_result = SPECC_FAIL; if (__fail_line == -1) {__fail_line = __LINE__;}

#define it(msg, test) pass(); \
  {test}\
  if (__spec_result == SPECC_PASS) {\
    __pass_count++; \
    __printf_color(SPECC_COLOR_GREEN,"  - %s\n",msg);\
  }\
  else {\
    __fail_count++; \
    __printf_color(SPECC_COLOR_RED,"  X %s\n    - %s:%d",msg,__FILE__,__fail_line);\
    if (__spec_expected_ptr != __spec_actual_ptr) { \
      __printf_expected_actual(); \
    } else { \
      printf("\n"); \
    } \
  } \
  free(__spec_expected_ptr); \
  free(__spec_actual_ptr); \
  __spec_expected_ptr = NULL; \
  __spec_actual_ptr = NULL; \


#define should(assertion) if (!(assertion)) {__fail();}

#define should_not(assertion) should(!(assertion))

#define eq_fail(expected,actual,type)\
  __fail(); \
  type temp_expected = expected; \
  type temp_actual = actual; \
  __spec_expected_ptr = (type*)malloc(sizeof(type)); \
  __spec_actual_ptr = (type*)malloc(sizeof(type)); \
  memcpy(__spec_expected_ptr,&temp_expected,sizeof(type)); \
  memcpy(__spec_actual_ptr,&temp_actual,sizeof(type)); \
  __current_fmt = (char*)__type_to_fmt(#type); \

#define should_eq(expected, actual, type) \
  if (!((expected) == (actual))) { \
    __fail(); \
    eq_fail(expected,actual,type); \
  }

#define should_not_eq(expected, actual, type) \
  if (((expected) == (actual))) { \
    __fail(); \
    eq_fail(expected,actual,type); \
  }

#define str_eq_fail(expected,actual) \
  size_t expected_size = sizeof(char)*strlen(expected); \
  size_t actual_size = sizeof(char)*strlen(actual); \
  __spec_expected_ptr = (char*)malloc(expected_size); \
  __spec_actual_ptr = (char*)malloc(actual_size); \
  strcpy(__spec_expected_ptr,expected); \
  strcpy(__spec_actual_ptr,actual); \
  __current_fmt = "\"%s\""; \

#define should_str_eq(expected, actual) \
  if (strcmp(expected,actual) != 0) { \
    __fail(); \
    str_eq_fail(expected,actual); \
  }

#define should_str_not_eq(expected, actual) \
  if (strcmp(expected,actual) == 0) { \
    __fail(); \
    str_eq_fail(expected,actual); \
  }

#define should_double_eq(expected,actual,err)\
  if (!(expected+err >= actual && expected-err <= actual)) {\
   __fail(); \
   eq_fail(expected,actual,double); \
  }

#define should_double_not_eq(expected,actual,err)\
  if (expected+err >= actual && expected-err <= actual) {\
   __fail(); \
   eq_fail(expected,actual,double); \
  }
  