#include <stdlib.h>
#include <ctype.h>
#include <string.h>

char string_charAt(char*,size_t);
int string_compareTo(char*, char*);
int string_compareToIgnoreCase(char*,char*);
char* string_concat(char*,char*);
int string_contains(char*,char*);
int string_endsWith(char*,char*);
int string_equals(char*,char*);
int string_equalsIgnoreCase(char*,char*);
char* string_copy(char*);
size_t string_indexOf(char*,char);
size_t string_indexOfFrom(char*,char,size_t);
size_t string_indexOfStr(char*,char*);
size_t string_indexOfStrFrom(char*,char*,size_t);
int string_isEmpty(char*);
//char* string_join(char*,vector_of_strings)
size_t string_lastIndexOf(char*,char);
size_t string_lastIndexOfFrom(char*,char,size_t);
size_t string_lastIndexOfStr(char*,char*);
size_t string_lastIndexOfStrFrom(char*,char*,size_t);
size_t string_length(char*);
int string_matches(char*,char*); // regex. this will be annyoing i think
char* string_replace(char*,char,char); // regex again
char* string_replaceStr(char*,char*,char*); // ...
char* string_replaceFirst(char*,char*,char*); // ...
char* string_split(char*,char*); // ...
char* string_splitLimit(char*,char*,size_t limit); // ...
char* string_startsWith(char*,char*);
char* string_startsWithOffset(char*,char*,size_t);
char* string_substring(char*,size_t,size_t);
char* string_toLowerCase(char*);
char* string_toUpperCase(char*);
char* trim(char*);