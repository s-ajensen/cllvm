declare i32 @printf(i8*, ...)

%TypeTag = type { i32 }
%Primitive = type { %TypeTag, [8 x i8] }

declare void @eval()
@result = global %Primitive* null, align 8

@long_fmt = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1

define i32 @main() {
    call void @eval()
    %ptr_result = load %Primitive*, %Primitive** @result, align 8
    %ptr_value = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 1
    %ptr_i64 = bitcast [8 x i8]* %ptr_value to i64*
    %result_i64 = load i64, i64* %ptr_i64, align 8
    call i32 (i8*, ...) @printf(i8* @long_fmt, i64 %result_i64)

    ret i32 0;
}