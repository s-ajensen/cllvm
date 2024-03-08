#include "specc/specc.h"
#include "../../src/c/string.h"

module(string, {
    describe("string_charAt",

        it("gets first index",
            should_eq('h',string_charAt("hello",0),char))
        
        it("gets second index",
            should_eq('y',string_charAt("bye",1),char)));

    describe("string_length",
    
        it("zero length",
            should_eq(0,string_length(""),int))
            
        it("length 1",
            should_eq(1,string_length("a"),int))
        
        it("length 3",
            should_eq(3,string_length("abc"),int)));

    describe("string_compareTo",
                    
        it("compares to empty string",
            should_eq(0,string_compareTo("",""),int);
            should_eq(1,string_compareTo("a",""),int)
            should_eq(-1,string_compareTo("","a"),int))
        
        it("compares with first char different",
            should_eq(25,string_compareTo("z","a"),int)
            should_eq(23,string_compareTo("xbc","ae"),int))
            
        it("compares with second char different",
            should_eq(-1,string_compareTo("ab","ac"),int))
            
        it("compares with third char different",
            should_eq(3,string_compareTo("abi","abf"),int))
            
        it("compares exact same strings",
            should_eq(0,string_compareTo("hello there","hello there"),int)
            should_eq(0,string_compareTo("bye bye","bye bye"),int)));

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
            should_str_eq("",out);
            free(out);)
            
        it("copies populated string",
            char* out = string_copy("h");
            should_str_eq("h",out);
            free(out);
            
            out = string_copy("hi");
            should_str_eq("hi",out);
            free(out);
            
            out = string_copy("bye");
            should_str_eq("bye",out);
            free(out);));

    describe("string_toLowerCase",
        
        it("does nothing to lowercase string",
            char* out = string_toLowerCase("");
            should_str_eq("",out);
            free(out);
            
            out = string_toLowerCase("a");
            should_str_eq("a",out);
            free(out);)
            
        it("converts uppcase to lowercase",
            char* out = string_toLowerCase("A");
            should_str_eq("a",out);
            free(out);
            
            out = string_toLowerCase("Ab");
            should_str_eq("ab",out);
            free(out);
            
            out = string_toLowerCase("AB");
            should_str_eq("ab",out);
            free(out);));

    describe("string_compareToIgnoreCase",
        
        it("has normal compareTo behavior for strings of same case",
            should_eq(0,string_compareToIgnoreCase("",""),int);
            should_eq(1,string_compareToIgnoreCase("a",""),int);
            should_eq(-1,string_compareToIgnoreCase("","a"),int);
            should_eq(3,string_compareToIgnoreCase("abi","abf"),int);
            should_eq(3,string_compareToIgnoreCase("ABI","ABF"),int);)
            
        it("ignores case",
            should_eq(3,string_compareToIgnoreCase("abi","Abf"),int);
            should_eq(3,string_compareToIgnoreCase("abi","ABF"),int);
            should_eq(0,string_compareToIgnoreCase("bye BYE","BYE bye"),int);));
});