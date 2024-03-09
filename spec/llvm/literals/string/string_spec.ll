declare i32 @printf(i8*, ...)
declare i32 @strcmp(i8*,i8*)

%TypeTag = type { i32 }
%Primitive = type { %TypeTag, ptr }

declare %Primitive* @eval() 

@pass_fmt = private unnamed_addr constant [38 x i8] c"[string_spec] test passed! value: %s\0A\00", align 1
@fail_fmt = private unnamed_addr constant [49 x i8] c"[string_spec] test failed! expected: %s got: %s\0A\00", align 1

define i32 @main() {
entry:
  %ptr_result = call %Primitive* @eval()
  %ptr_tag = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 0
  %type_tag = load i32, i32* %ptr_tag, align 4
  %ptr_val = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 1

  %ptr_actual = load i8*, i8** %ptr_val, align 8
  %ptr_expected = alloca [6 x i8]
  store [6 x i8] c"Hello\00", [6 x i8]* %ptr_expected, align 1

  %should_pass = call i32 (i8*,i8*) @strcmp(i8* %ptr_expected, i8* %ptr_actual)

  switch i32 %should_pass, label %fail [
    i32 0, label %pass
  ]

pass:
  call i32 (i8*, ...) @printf(i8* @pass_fmt, i8* %ptr_expected)
  br label %end

fail:
  call i32 (i8*, ...) @printf(i8* @fail_fmt, i8* %ptr_expected, i8* %ptr_actual)
  br label %end

end:
  ret i32 0
}