declare i32 @printf(i8*, ...)

%TypeTag = type { i32 }
%Primitive = type { %TypeTag, [8 x i8] }

declare %Primitive* @eval()

@long_fmt = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1
@double_fmt = private unnamed_addr constant [4 x i8] c"%f\0A\00", align 1

define i32 @main() {
entry:
    %ptr_result = call %Primitive* @eval()
    %ptr_tag = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 0
    %type_tag = load i32, i32* %ptr_tag, align 4

    %is_double = icmp eq i32 %type_tag, 1

    br i1 %is_double, label %handle_double, label %handle_i64

handle_double:
    %ptr_value_d = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 1
    %ptr_double = bitcast [8 x i8]* %ptr_value_d to double*
    %result_double = load double, double* %ptr_double, align 8
    call i32 (i8*, ...) @printf(i8* @double_fmt, double %result_double)
    br label %end

handle_i64:
    %ptr_value_l = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 1
    %ptr_i64 = bitcast [8 x i8]* %ptr_value_l to i64*
    %result_i64 = load i64, i64* %ptr_i64, align 8
    call i32 (i8*, ...) @printf(i8* @long_fmt, i64 %result_i64)
    br label %end

end:
    ret i32 0;
}