#include "string.h"

// TODO - IndexOutOfBounds exception? Idk how this will work. Segfault for now.
char string_charAt(char* s, size_t idx) {
    return s[idx];
}

size_t string_length(char* s) {
    return strlen(s);
}

int string_compareTo(char* s1, char* s2) {
    size_t s1_size = string_length(s1);
    size_t s2_size = string_length(s2);
    size_t smaller = s1_size < s2_size ? s1_size : s2_size;
    for (size_t i = 0; i < smaller; i++) {
        char c1 = string_charAt(s1,i);
        char c2 = string_charAt(s2,i);
        if (c1 != c2) {
            return c1 - c2;
        }
    }
    return s1_size - s2_size;
}

int string_equals(char* s1,char* s2) {
    return string_compareTo(s1,s2) == 0;
}

char* string_copy(char* s) {
    char* out = malloc(sizeof(char)*(string_length(s)+1));
    strcpy(out,s);
    return out;
}

char* string_toLowerCase(char* s) {
    char* out = string_copy(s);
    size_t len = string_length(s);
    for (int i = 0; i < len; i++) {
        out[i] = tolower(out[i]);
    }
    return out;
}

// TODO - could be optimized if necessary
int string_compareToIgnoreCase(char* s1,char* s2) {
    char* lower1 = string_toLowerCase(s1);
    char* lower2 = string_toLowerCase(s2);
    int out = string_compareTo(lower1,lower2);
    free(lower1);
    free(lower2);
    return out;
}