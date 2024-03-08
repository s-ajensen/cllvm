declare i32 @printf(i8*, ...)

%TypeTag = type { i32 }
%Primitive = type { %TypeTag, ptr }

declare %Primitive* @eval() 

@double_fmt = private unnamed_addr constant [4 x i8] c"%f\0A\00", align 1
@pass_fmt = private unnamed_addr constant [38 x i8] c"[double_spec] test passed! value: %f\0A\00", align 1
@fail_fmt = private unnamed_addr constant [49 x i8] c"[double_spec] test failed! expected: %f got: %f\0A\00", align 1

define i32 @main() {
entry:
  %ptr_result = call %Primitive* @eval()
  %ptr_tag = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 0
  %type_tag = load i32, i32* %ptr_tag, align 4
  %ptr_value = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 1
  
  %ptr_expected = alloca double
  store double 3.14, double* %ptr_expected, align 8
  %expected = load double, double* %ptr_expected, align 8
  %ptr_actual = load double*, ptr %ptr_value, align 8
  %actual = load double, double* %ptr_actual
  
  %should_pass = fcmp ueq double %actual, %expected
  br i1 %should_pass, label %pass, label %fail

pass:
  call i32 (i8*, ...) @printf(i8* @pass_fmt, double %expected)
  br label %end

fail:
  call i32 (i8*, ...) @printf(i8* @fail_fmt, double %expected, double %actual)
  br label %end

end:
  ret i32 0
}