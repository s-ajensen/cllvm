@.str = private unnamed_addr constant [13 x i8] c"hello world\0A\00"

declare i32 @printf(i8*, ...)
declare i8 @string_charAt(ptr,i64)

@char_fmt = private unnamed_addr constant [4 x i8] c"%c\0A\00"

define i32 @main() {
    %cast210 = getelementptr [13 x i8],[13 x i8]* @.str, i64 0, i64 0

    %out = call i8 @string_charAt(i8* %cast210, i64 2)
    call i32 @printf(i8* @char_fmt, i8 %out)
    ret i32 0
}