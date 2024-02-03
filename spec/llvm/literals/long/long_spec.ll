declare i32 @printf(i8*, ...)

%TypeTag = type { i32 }
%Primitive = type { %TypeTag, [8 x i8] }

declare %Primitive* @eval() 

@long_fmt = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1
@pass_fmt = private unnamed_addr constant [37 x i8] c"[long_spec] test passed! value: %ld\0A\00", align 1
@fail_fmt = private unnamed_addr constant [49 x i8] c"[long_spec] test failed! expected: %ld got: %ld\0A\00", align 1

define i32 @main() {
entry:
  %ptr_result = call %Primitive* @eval()
  %ptr_tag = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 0
  %type_tag = load i32, i32* %ptr_tag, align 4
  %ptr_value = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 1
  
  %ptr_expected = alloca i64
  store i64 19, i64* %ptr_expected, align 8
  %expected = load i64, i64* %ptr_expected, align 8
  %actual = load i64, i64* %ptr_value, align 8
  
  %should_pass = icmp eq i64 %actual, %expected
  br i1 %should_pass, label %pass, label %fail

pass:
  call i32 (i8*, ...) @printf(i8* @pass_fmt, i64 %expected)
  br label %end

fail:
  call i32 (i8*, ...) @printf(i8* @fail_fmt, i64 %expected, i64 %actual)
  br label %end

end:
  ret i32 0
}