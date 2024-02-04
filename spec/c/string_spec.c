#include "string_spec.h"

void string_specs() {
    describe("string",

        it("tests",
            should(test() == 0))
        
        it("fails",
            int a = 1;
            should(test() == a)))
}