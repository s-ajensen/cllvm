; ModuleID = 'HelloWorld'
source_filename = "hello_world.ll"

declare i32 @printf(i8*, ...)

;define i32 @main() {
;entry:
    ; allocate memory for a primitive
;    %primitive_ptr = alloca %Primitive, align 8

    ; get pointer to the tag of the allocated primitive
;    %tag_ptr = getelementptr %Primitive, %Primitive* %primitive_ptr, i32 0, i32 0

    ; set the value of the tag to 0
;    store i32 0, i32* %tag_ptr

    ; get pointer to the data of the allocated primitive
;    %data_ptr = getelementptr %Primitive, %Primitive* %primitive_ptr, i32 0, i32 1

    ; cast the primitive data to long
;    %long_ptr = bitcast [8 x i8]* %data_ptr to i64*

    ; set the value of the long primitive to 42
;    store i64 42, i64* %long_ptr, align 8

    ; get the value of the stores long
;    %long_value = load i64, i64* %long_ptr, align 8

;    %fmt_str = bitcast [4 x i8]* @long_fmt to i8*
;    call i32 (i8*, ...) @printf(i8* %fmt_str, i64 %long_value)

;    ret i32 0
;}

;@long_fmt = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1

%TypeTag = type { i32 }
%Primitive = type { %TypeTag, [8 x i8] }
@result = external global %Primitive*

define void @eval() {
entry:
    %ptr_0 = alloca %Primitive, align 8
    %ptr_1 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 0
    store i32 0, i32* %ptr_1
    %ptr_3 = getelementptr %Primitive, %Primitive* %ptr_0, i32 0, i32 1
    %ptr_4 = bitcast [8 x i8]* %ptr_3 to i64*
    store i64 124, i64* %ptr_4, align 8
    store %Primitive* %ptr_0, %Primitive** @result, align 8
    ret void
}

;define i32 @main() {
;    call void @eval()
;    %ptr_result = load %Primitive*, %Primitive** @result, align 8
;    %ptr_value = getelementptr %Primitive, %Primitive* %ptr_result, i32 0, i32 1
;    %ptr_i64 = bitcast [8 x i8]* %ptr_value to i64*
;    %result_i64 = load i64, i64* %ptr_i64, align 8
;    call i32 (i8*, ...) @printf(i8* @long_fmt, i64 %result_i64)

;    ret i32 0;
;}