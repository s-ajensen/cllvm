#include "string_spec.h"

void string_specs() {
    describe("string_charAt",

        it("gets first index",
            should(string_charAt("hello",0) == 'h'))
        
        it("gets second index",
            should(string_charAt("bye",1) == 'y')));

    describe("string_length",
    
        it("zero length",
            should(string_length("") == 0))
            
        it("length 1",
            should(string_length("a") == 1))
        
        it("length 3",
            should(string_length("abc") == 3)));

    describe("string_compareTo",
                    
        it("compares to empty string",
            should(string_compareTo("","") == 0);
            should(string_compareTo("a","") == 1)
            should(string_compareTo("","a") == -1))
        
        it("compares with first char different",
            should(string_compareTo("z","a") == 25)
            should(string_compareTo("xbc","ae") == 23))
            
        it("compares with second char different",
            should(string_compareTo("ab","ac") == -1))
            
        it("compares with third char different",
            should(string_compareTo("abi","abf") == 3))
            
        it("compares exact same strings",
            should(string_compareTo("hello there","hello there") == 0)
            should(string_compareTo("bye bye","bye bye") == 0)));

    describe("string_equals",
        
        it("empty strings are equal",
            should(string_equals("","")))
            
        it("strings of different lengths are not equal",
            should_not(string_equals("a",""))
            should_not(string_equals("a","ab")))
            
        it("strings with different characters are not equal",
            should_not(string_equals("mkx","abc"))
            should_not(string_equals("c.b dx","c!b dx")))
        
        it("strings with same characters are equal",
            should(string_equals("abc","abc"))
            should(string_equals("c!b dx","c!b dx"))));

    describe("string_copy",
        
        it("copies empty string",
            char* out = string_copy("");
            should(string_equals(out,""));
            free(out);)
            
        it("copies populated string",
            char* out = string_copy("h");
            should(string_equals(out,"h"));
            free(out);
            
            out = string_copy("hi");
            should(string_equals(out,"hi"));
            free(out);
            
            out = string_copy("bye");
            should(string_equals(out,"bye"));
            free(out);));

    describe("string_toLowerCase",
        
        it("does nothing to lowercase string",
            char* out = string_toLowerCase("");
            should(string_equals(out,""));
            free(out);
            
            out = string_toLowerCase("a");
            should(string_equals(out,"a"));
            free(out);)
            
        it("converts uppcase to lowercase",
            char* out = string_toLowerCase("A");
            should(string_equals(out,"a"));
            free(out);
            
            out = string_toLowerCase("Ab");
            should(string_equals(out,"ab"));
            free(out);
            
            out = string_toLowerCase("AB");
            should(string_equals(out,"ab"));
            free(out);));

    describe("string_compareToIgnoreCase",
        
        it("has normal compareTo behavior for strings of same case",
            should(string_compareToIgnoreCase("","") == 0);
            should(string_compareToIgnoreCase("a","") == 1);
            should(string_compareToIgnoreCase("","a") == -1);
            should(string_compareToIgnoreCase("abi","abf") == 3);
            should(string_compareToIgnoreCase("ABI","ABF") == 3);)
            
        it("ignores case",
            should(string_compareToIgnoreCase("abi","Abf") == 3);
            should(string_compareToIgnoreCase("abi","ABF") == 3);
            should(string_compareToIgnoreCase("bye BYE","BYE bye") == 0);));
}