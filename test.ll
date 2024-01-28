; ModuleID = 'HelloWorld'
source_filename = "hello_world.ll"

@hello_world_str = private unnamed_addr constant [14 x i8] c"Hello, World!\0A", align 1

declare i32 @puts(i8*)
declare i32 @printf(i8*, ...)

;define i32 @main() {
;entry:
;    ; Convert the string to a pointer to i8 (C string)
;    %str = getelementptr [14 x i8], [14 x i8]* @hello_world_str, i64 0, i64 0

;    ; Call the puts function from the C standard library
;    call i32 @puts(i8* %str)

;    ; Return 0 from main
;    ret i32 0
;}

%TypeTag = type { i32 }
%Primitive = type { %TypeTag, [8 x i8] }

@long_fmt = private unnamed_addr constant [5 x i8] c"%ld\0A\00", align 1

define i32 @main() {
entry:
    ; allocate memory for a primitive
    %primitive_ptr = alloca %Primitive, align 8

    ; get pointer to the tag of the allocated primitive
    %tag_ptr = getelementptr %Primitive, %Primitive* %primitive_ptr, i32 0, i32 0

    ; set the value of the tag to 0
    store i32 0, i32* %tag_ptr

    ; get pointer to the data of the allocated primitive
    %data_ptr = getelementptr %Primitive, %Primitive* %primitive_ptr, i32 0, i32 1

    ; cast the primitive data to long
    %long_ptr = bitcast [8 x i8]* %data_ptr to i64*

    ; set the value of the long primitive to 42
    store i64 42, i64* %long_ptr, align 8

    ; get the value of the stores long
    %long_value = load i64, i64* %long_ptr, align 8

    %fmt_str = bitcast [4 x i8]* @long_fmt to i8*
    call i32 (i8*, ...) @printf(i8* %fmt_str, i64 %long_value)

    ret i32 0
}